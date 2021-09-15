package io.github.yuemenglong.sqlite.core;

import io.github.yuemenglong.sqlite.common.Addr;
import io.github.yuemenglong.sqlite.common.Assert;
import io.github.yuemenglong.sqlite.common.CharPtr;

import java.util.concurrent.atomic.AtomicInteger;

import static io.github.yuemenglong.sqlite.core.build.*;
import static io.github.yuemenglong.sqlite.core.parse.*;
import static io.github.yuemenglong.sqlite.core.select.*;
import static io.github.yuemenglong.sqlite.core.vdbe.*;
import static io.github.yuemenglong.sqlite.core.sqliteint.*;
import static io.github.yuemenglong.sqlite.core.util.*;
import static io.github.yuemenglong.sqlite.core.expr.*;
import static io.github.yuemenglong.sqlite.core.where.*;

public class update {
  /*
   ** Process an UPDATE statement.
   */
  public static void sqliteUpdate(
          Parse pParse,         /* The parser context */
          Token pTableName,     /* The table in which we should change things */
          ExprList pChanges,    /* Things to be changed */
          Expr pWhere           /* The WHERE clause.  May be null */
  ) {
    int i, j;              /* Loop counters */
    Table pTab;           /* The table to be updated */
    IdList pTabList = null;  /* List containing only pTab */
    int end, addr;         /* A couple of addresses in the generated code */
    WhereInfo pWInfo;     /* Information about the WHERE clause */
    Vdbe v;               /* The virtual database engine */
    Index pIdx;           /* For looping over indices */
    int nIdx;              /* Number of indices that need updating */
    int base;              /* Index of first available table cursor */
    Index[] apIdx = null;     /* An array of indices that need updating too */
    int[] aXRef = null;        /* aXRef[i] is the index in pChanges.a[] of the
     ** an expression for the i-th column of the table.
     ** aXRef[i]==-1 if the i-th column is not changed. */

    /* Locate the table which we want to update.  This table has to be
     ** put in an IdList structure because some of the subroutines we
     ** will be calling are designed to work with multiple tables and expect
     ** an IdList* parameter instead of just a Table* parameger.
     */
    pTabList = sqliteIdListAppend(null, pTableName);
    for (i = 0; i < pTabList.nId; i++) {
      pTabList.a[i].pTab = sqliteFindTable(pParse.db, pTabList.a[i].zName);
      if (pTabList.a[i].pTab == null) {
        sqliteSetString(pParse.zErrMsg, "no such table: ",
                pTabList.a[i].zName, 0);
        pParse.nErr++;
//      goto update_cleanup;
        return;
      }
      if (pTabList.a[i].pTab.readOnly != 0) {
        sqliteSetString(pParse.zErrMsg, "table ", pTabList.a[i].zName,
                " may not be modified", 0);
        pParse.nErr++;
//      goto update_cleanup;
        return;
      }
    }
    pTab = pTabList.a[0].pTab;
    aXRef = new int[pTab.nCol];//sqliteMalloc(sizeof( int) *pTab.nCol );
    if (aXRef == null) return;//goto update_cleanup;
    for (i = 0; i < pTab.nCol; i++) aXRef[i] = -1;

    /* Resolve the column names in all the expressions in both the
     ** WHERE clause and in the new values.  Also find the column index
     ** for each column to be updated in the pChanges array.
     */
    if (pWhere != null) {
      sqliteExprResolveInSelect(pParse, pWhere);
    }
    for (i = 0; i < pChanges.nExpr; i++) {
      sqliteExprResolveInSelect(pParse, pChanges.a[i].pExpr);
    }
    if (pWhere != null) {
      if (sqliteExprResolveIds(pParse, pTabList, pWhere) != 0) {
//      goto update_cleanup;
        return;
      }
      if (sqliteExprCheck(pParse, pWhere, 0, null) != 0) {
//      goto update_cleanup;
        return;
      }
    }
    for (i = 0; i < pChanges.nExpr; i++) {
      if (sqliteExprResolveIds(pParse, pTabList, pChanges.a[i].pExpr) != 0) {
//      goto update_cleanup;
        return;
      }
      if (sqliteExprCheck(pParse, pChanges.a[i].pExpr, 0, null) != 0) {
//      goto update_cleanup;
        return;
      }
      for (j = 0; j < pTab.nCol; j++) {
        if (sqliteStrICmp(pTab.aCol[j].zName, pChanges.a[i].zName) == 0) {
          aXRef[j] = i;
          break;
        }
      }
      if (j >= pTab.nCol) {
        sqliteSetString(pParse.zErrMsg, "no such column: ",
                pChanges.a[i].zName, 0);
        pParse.nErr++;
//      goto update_cleanup;
        return;
      }
    }

    /* Allocate memory for the array apIdx[] and fill it pointers to every
     ** index that needs to be updated.  Indices only need updating if their
     ** key includes one of the columns named in pChanges.
     */
    for (nIdx = 0, pIdx = pTab.pIndex; pIdx != null; pIdx = pIdx.pNext) {
      for (i = 0; i < pIdx.nColumn; i++) {
        if (aXRef[pIdx.aiColumn[i]] >= 0) break;
      }
      if (i < pIdx.nColumn) nIdx++;
    }
    apIdx = new Index[nIdx];//sqliteMalloc(sizeof(Index *) * nIdx);
    if (apIdx == null) return;//goto update_cleanup;
    for (nIdx = 0, pIdx = pTab.pIndex; pIdx != null; pIdx = pIdx.pNext) {
      for (i = 0; i < pIdx.nColumn; i++) {
        if (aXRef[pIdx.aiColumn[i]] >= 0) break;
      }
      if (i < pIdx.nColumn) apIdx[nIdx++] = pIdx;
    }

    /* Begin generating code.
     */
    v = sqliteGetVdbe(pParse);
    if (v == null) return; //goto update_cleanup;

    /* Begin the database scan
     */
    sqliteVdbeAddOp(v, OP_ListOpen, 0, 0, 0, 0);
    pWInfo = sqliteWhereBegin(pParse, pTabList, pWhere, 1);
    if (pWInfo == null) return;//goto update_cleanup;

    /* Remember the index of every item to be updated.
     */
    sqliteVdbeAddOp(v, OP_ListWrite, 0, 0, 0, 0);

    /* End the database scan loop.
     */
    sqliteWhereEnd(pWInfo);

    /* Rewind the list of records that need to be updated and
     ** open every index that needs updating.
     */
    sqliteVdbeAddOp(v, OP_ListRewind, 0, 0, 0, 0);
    base = pParse.nTab;
    sqliteVdbeAddOp(v, OP_Open, base, 1, pTab.zName, 0);
    for (i = 0; i < nIdx; i++) {
      sqliteVdbeAddOp(v, OP_Open, base + i + 1, 1, apIdx[i].zName, 0);
    }

    /* Loop over every record that needs updating.  We have to load
     ** the old data for each record to be updated because some columns
     ** might not change and we will need to copy the old value.
     ** Also, the old data is needed to delete the old index entires.
     */
    end = sqliteVdbeMakeLabel(v);
    addr = sqliteVdbeAddOp(v, OP_ListRead, 0, end, 0, 0);
    sqliteVdbeAddOp(v, OP_Dup, 0, 0, 0, 0);
    sqliteVdbeAddOp(v, OP_Fetch, base, 0, 0, 0);

    /* Delete the old indices for the current record.
     */
    for (i = 0; i < nIdx; i++) {
      sqliteVdbeAddOp(v, OP_Dup, 0, 0, 0, 0);
      pIdx = apIdx[i];
      for (j = 0; j < pIdx.nColumn; j++) {
        sqliteVdbeAddOp(v, OP_Field, base, pIdx.aiColumn[j], 0, 0);
      }
      sqliteVdbeAddOp(v, OP_MakeKey, pIdx.nColumn, 0, 0, 0);
      sqliteVdbeAddOp(v, OP_DeleteIdx, base + i + 1, 0, 0, 0);
    }

    /* Compute a completely new data for this record.
     */
    for (i = 0; i < pTab.nCol; i++) {
      j = aXRef[i];
      if (j < 0) {
        sqliteVdbeAddOp(v, OP_Field, base, i, 0, 0);
      } else {
        sqliteExprCode(pParse, pChanges.a[j].pExpr);
      }
    }

    /* Insert new index entries that correspond to the new data
     */
    for (i = 0; i < nIdx; i++) {
      sqliteVdbeAddOp(v, OP_Dup, pTab.nCol, 0, 0, 0); /* The KEY */
      pIdx = apIdx[i];
      for (j = 0; j < pIdx.nColumn; j++) {
        sqliteVdbeAddOp(v, OP_Dup, j + pTab.nCol - pIdx.aiColumn[j], 0, 0, 0);
      }
      sqliteVdbeAddOp(v, OP_MakeKey, pIdx.nColumn, 0, 0, 0);
      sqliteVdbeAddOp(v, OP_PutIdx, base + i + 1, 0, 0, 0);
    }

    /* Write the new data back into the database.
     */
    sqliteVdbeAddOp(v, OP_MakeRecord, pTab.nCol, 0, 0, 0);
    sqliteVdbeAddOp(v, OP_Put, base, 0, 0, 0);

    /* Repeat the above with the next record to be updated, until
     ** all record selected by the WHERE clause have been updated.
     */
    sqliteVdbeAddOp(v, OP_Goto, 0, addr, 0, 0);
    sqliteVdbeAddOp(v, OP_ListClose, 0, 0, 0, end);

    update_cleanup:
    sqliteFree(apIdx);
    sqliteFree(aXRef);
    sqliteIdListDelete(pTabList);
    sqliteExprListDelete(pChanges);
    sqliteExprDelete(pWhere);
    return;
  }

}
