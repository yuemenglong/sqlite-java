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

public class insert {
  /*
   ** This routine is call to handle SQL of the following forms:
   **
   **    insert into TABLE (IDLIST) values(EXPRLIST)
   **    insert into TABLE (IDLIST) select
   **
   ** The IDLIST following the table name is always optional.  If omitted,
   ** then a list of all columns for the table is substituted.  The IDLIST
   ** appears in the pColumn parameter.  pColumn is NULL if IDLIST is omitted.
   **
   ** The pList parameter holds EXPRLIST in the first form of the INSERT
   ** statement above, and pSelect is NULL.  For the second form, pList is
   ** NULL and pSelect is a pointer to the select statement used to generate
   ** data for the insert.
   */
  public static void sqliteInsert(
          Parse pParse,        /* Parser context */
          Token pTableName,    /* Name of table into which we are inserting */
          ExprList pList,      /* List of values to be inserted */
          int pSelect,      /* A SELECT statement to use as the data source */
          IdList pColumn       /* Column names corresponding to IDLIST. */
  ) {
    sqliteInsert(pParse, pTableName, pList, null, pColumn);
  }

  public static void sqliteInsert(
          Parse pParse,        /* Parser context */
          Token pTableName,    /* Name of table into which we are inserting */
          int pList,      /* List of values to be inserted */
          Select pSelect,      /* A SELECT statement to use as the data source */
          IdList pColumn       /* Column names corresponding to IDLIST. */
  ) {
    sqliteInsert(pParse, pTableName, null, pSelect, pColumn);
  }

  public static void sqliteInsert(
          Parse pParse,        /* Parser context */
          Token pTableName,    /* Name of table into which we are inserting */
          ExprList pList,      /* List of values to be inserted */
          Select pSelect,      /* A SELECT statement to use as the data source */
          IdList pColumn       /* Column names corresponding to IDLIST. */
  ) {
    Table pTab;          /* The table to insert into */
    CharPtr zTab;           /* Name of the table into which we are inserting */
    int i;
    int j;
    int idx;        /* Loop counters */
    Vdbe v;              /* Generate code into this virtual machine */
    Index pIdx;          /* For looping over indices of the table */
    int srcTab;           /* Date comes from this temporary cursor if >=0 */
    int nColumn;          /* Number of columns in the data */
    int base;             /* First available cursor */
    int iCont = 0, iBreak = 0;    /* Beginning and end of the loop over srcTab */

    /* Locate the table into which we will be inserting new information.
     */
    zTab = sqliteTableNameFromToken(pTableName);
    pTab = sqliteFindTable(pParse.db, zTab);
    sqliteFree(zTab);
    if (pTab == null) {
      sqliteSetNString(pParse.zErrMsg, "no such table: ", 0,
              pTableName.z, pTableName.n, 0);
      pParse.nErr++;
//    goto insert_cleanup;
      return;
    }
    if (pTab.readOnly != 0) {
      sqliteSetString(pParse.zErrMsg, "table ", pTab.zName,
              " may not be modified", 0);
      pParse.nErr++;
//    goto insert_cleanup;
      return;
    }

    /* Allocate a VDBE
     */
    v = sqliteGetVdbe(pParse);
    if (v == null) return;//goto insert_cleanup;

    /* Figure out how many columns of data are supplied.  If the data
     ** is comming from a SELECT statement, then this step has to generate
     ** all the code to implement the SELECT statement and leave the data
     ** in a temporary table.  If data is coming from an expression list,
     ** then we just have to count the number of expressions.
     */
    if (pSelect != null) {
      int rc;
      srcTab = pParse.nTab++;
      sqliteVdbeAddOp(v, OP_Open, srcTab, 1, 0, 0);
      rc = sqliteSelect(pParse, pSelect, SRT_Table, srcTab);
      if (rc != 0) return;//goto insert_cleanup;
      Assert.assertTrue(pSelect.pEList != null);
      nColumn = pSelect.pEList.nExpr;
    } else {
      srcTab = -1;
      Assert.assertTrue(pList != null);
      nColumn = pList.nExpr;
    }

    /* Make sure the number of columns in the source data matches the number
     ** of columns to be inserted into the table.
     */
    if (pColumn == null && nColumn != pTab.nCol) {
      CharPtr zNum1 = new CharPtr(30);
      CharPtr zNum2 = new CharPtr(30);
      zNum1.sprintf("%d", nColumn);
      zNum2.sprintf("%d", pTab.nCol);
      sqliteSetString(pParse.zErrMsg, "table ", pTab.zName,
              " has ", zNum2, " columns but ",
              zNum1, " values were supplied", 0);
      pParse.nErr++;
//    goto insert_cleanup;
      return;
    }
    if (pColumn != null && nColumn != pColumn.nId) {
      CharPtr zNum1 = new CharPtr(30);
      CharPtr zNum2 = new CharPtr(30);
      zNum1.sprintf("%d", nColumn);
      zNum2.sprintf("%d", pColumn.nId);
      sqliteSetString(pParse.zErrMsg, zNum1, " values for ",
              zNum2, " columns", 0);
      pParse.nErr++;
//    goto insert_cleanup;
      return;
    }

    /* If the INSERT statement included an IDLIST term, then make sure
     ** all elements of the IDLIST really are columns of the table and
     ** remember the column indices.
     */
    if (pColumn != null) {
      for (i = 0; i < pColumn.nId; i++) {
        pColumn.a[i].idx = -1;
      }
      for (i = 0; i < pColumn.nId; i++) {
        for (j = 0; j < pTab.nCol; j++) {
          if (sqliteStrICmp(pColumn.a[i].zName, pTab.aCol[j].zName) == 0) {
            pColumn.a[i].idx = j;
            break;
          }
        }
        if (j >= pTab.nCol) {
          sqliteSetString(pParse.zErrMsg, "table ", pTab.zName,
                  " has no column named ", pColumn.a[i].zName, 0);
          pParse.nErr++;
//        goto insert_cleanup;
          return;
        }
      }
    }

    /* Open cursors into the table that is received the new data and
     ** all indices of that table.
     */
    base = pParse.nTab;
    sqliteVdbeAddOp(v, OP_Open, base, 1, pTab.zName, 0);
    for (idx = 1, pIdx = pTab.pIndex; pIdx != null; pIdx = pIdx.pNext, idx++) {
      sqliteVdbeAddOp(v, OP_Open, idx + base, 1, pIdx.zName, 0);
    }

    /* If the data source is a SELECT statement, then we have to create
     ** a loop because there might be multiple rows of data.  If the data
     ** source is an expression list, then exactly one row will be inserted
     ** and the loop is not used.
     */
    if (srcTab >= 0) {
      sqliteVdbeAddOp(v, OP_Rewind, srcTab, 0, 0, 0);
      iBreak = sqliteVdbeMakeLabel(v);
      iCont = sqliteVdbeAddOp(v, OP_Next, srcTab, iBreak, 0, 0);
    }

    /* Create a new entry in the table and fill it with data.
     */
    sqliteVdbeAddOp(v, OP_New, 0, 0, 0, 0);
    if (pTab.pIndex != null) {
      sqliteVdbeAddOp(v, OP_Dup, 0, 0, 0, 0);
    }
    for (i = 0; i < pTab.nCol; i++) {
      if (pColumn == null) {
        j = i;
      } else {
        for (j = 0; j < pColumn.nId; j++) {
          if (pColumn.a[j].idx == i) break;
        }
      }
      if (pColumn != null && j >= pColumn.nId) {
        CharPtr zDflt = pTab.aCol[i].zDflt;
        if (zDflt == null) {
          sqliteVdbeAddOp(v, OP_Null, 0, 0, 0, 0);
        } else {
          sqliteVdbeAddOp(v, OP_String, 0, 0, zDflt, 0);
        }
      } else if (srcTab >= 0) {
        sqliteVdbeAddOp(v, OP_Field, srcTab, i, 0, 0);
      } else {
        sqliteExprCode(pParse, pList.a[j].pExpr);
      }
    }
    sqliteVdbeAddOp(v, OP_MakeRecord, pTab.nCol, 0, 0, 0);
    sqliteVdbeAddOp(v, OP_Put, base, 0, 0, 0);

    /* Create appropriate entries for the new data row in all indices
     ** of the table.
     */
    for (idx = 1, pIdx = pTab.pIndex; pIdx != null; pIdx = pIdx.pNext, idx++) {
      if (pIdx.pNext != null) {
        sqliteVdbeAddOp(v, OP_Dup, 0, 0, 0, 0);
      }
      for (i = 0; i < pIdx.nColumn; i++) {
        idx = pIdx.aiColumn[i];
        if (pColumn == null) {
          j = idx;
        } else {
          for (j = 0; j < pColumn.nId; j++) {
            if (pColumn.a[j].idx == idx) break;
          }
        }
        if (pColumn != null && j >= pColumn.nId) {
          CharPtr zDflt = pTab.aCol[idx].zDflt;
          if (zDflt == null) {
            sqliteVdbeAddOp(v, OP_Null, 0, 0, 0, 0);
          } else {
            sqliteVdbeAddOp(v, OP_String, 0, 0, zDflt, 0);
          }
        } else if (srcTab >= 0) {
          sqliteVdbeAddOp(v, OP_Field, srcTab, idx, 0, 0);
        } else {
          sqliteExprCode(pParse, pList.a[j].pExpr);
        }
      }
      sqliteVdbeAddOp(v, OP_MakeKey, pIdx.nColumn, 0, 0, 0);
      sqliteVdbeAddOp(v, OP_PutIdx, idx + base, 0, 0, 0);
    }

    /* The bottom of the loop, if the data source is a SELECT statement
     */
    if (srcTab >= 0) {
      sqliteVdbeAddOp(v, OP_Goto, 0, iCont, 0, 0);
      sqliteVdbeAddOp(v, OP_Noop, 0, 0, 0, iBreak);
    }

    insert_cleanup:
    if (pList != null) sqliteExprListDelete(pList);
    if (pSelect != null) sqliteSelectDelete(pSelect);
    sqliteIdListDelete(pColumn);
  }

}
