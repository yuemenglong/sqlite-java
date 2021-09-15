package io.github.yuemenglong.sqlite.core;

import io.github.yuemenglong.sqlite.common.Addr;
import io.github.yuemenglong.sqlite.common.CharPtr;
import io.github.yuemenglong.sqlite.common.Ptr;

public class sqliteint {
  public static final int N_HASH = 51;
  public static final String MASTER_NAME = "sqlite_master";
  public static final int FN_Unknown = 0;
  public static final int FN_Count = 1;
  public static final int FN_Min = 2;
  public static final int FN_Max = 3;
  public static final int FN_Sum = 4;
  public static final int FN_Avg = 5;
  public static final int FN_Fcnt = 6;

  public interface xBusyCallback {
    int call(Object obj, CharPtr s, int n);
  }

  public static class sqlite {
    dbbe.Dbbe pBe;                 /* The backend driver */
    int flags;                 /* Miscellanous flags */
    int file_format;           /* What file format version is this database? */
    int nTable;                /* Number of tables in the database */
    Object pBusyArg;            /* 1st Argument to the busy callback */
    //    int (*xBusyCallback)(void *,const char*,int);  /* The busy callback */
    xBusyCallback xBusyCallback;
    Table[] apTblHash = new Table[N_HASH];  /* All tables of the database */
    Index[] apIdxHash = new Index[N_HASH];  /* All indices of the database */
  }

  public static final int SQLITE_VdbeTrace = 0x00000001;
  public static final int SQLITE_Initialized = 0x00000002;
  public static final int SQLITE_FileFormat = 2;

  public static class Column {
    CharPtr zName;     /* Name of this column */
    CharPtr zDflt;     /* Default value of this column */
    int notNull;     /* True if there is a NOT NULL constraint */
  }

  public static class Table {
    CharPtr zName;     /* Name of the table */
    Table pHash;    /* Next table with same hash on zName */
    int nCol;        /* Number of columns in this table */
    Column[] aCol;    /* Information about each column */
    int readOnly;    /* True if this table should not be written by the user */
    Index pIndex;   /* List of SQL indexes on this table. */
  }

  public static class Index {
    CharPtr zName;     /* Name of this index */
    Index pHash;    /* Next index with the same hash on zName */
    int nColumn;     /* Number of columns in the table used by this index */
    //    int *aiColumn;   /* Which columns are used by this index.  1st is 0 */
    int[] aiColumn;   /* Which columns are used by this index.  1st is 0 */
    Table pTable;   /* The SQL table being indexed */
    int isUnique;    /* True if keys must all be unique */
    Index pNext;    /* The next index associated with the same table */
  }

  public static class Token {
    CharPtr z;      /* Text of the token */
    int n;        /* Number of characters in this token */
  }

  public static class Expr {
    int op;                /* Operation performed by this node */
    Expr pLeft;
    Expr pRight;  /* Left and right subnodes */
    ExprList pList;       /* A list of expressions used as a function argument */
    Token token;           /* An operand token */
    Token span;            /* Complete text of the expression */
    int iTable, iColumn;   /* When op==TK_COLUMN, then this expr node means the
     ** iColumn-th field of the iTable-th table.  When
     ** op==TK_FUNCTION, iColumn holds the function id */
    int iAgg;              /* When op==TK_COLUMN and pParse->useAgg==TRUE, pull
     ** result from the iAgg-th element of the aggregator */
    Select pSelect;       /* When the expression is a sub-select */
  }

  public static class ExprList {
    int nExpr;             /* Number of expressions on the list */
    A[] a;

    public static class A {
      Expr pExpr;           /* The list of expressions */
      CharPtr zName;           /* Token associated with this expression */
      int sortOrder;        /* 1 for DESC or 0 for ASC */
      int isAgg;            /* True if this is an aggregate like count(*) */
      int done;             /* A flag to indicate when processing is finished */
    }                 /* One entry for each expression */
  }

  //
  ///*
  //** A list of identifiers.
  //*/
  public static class IdList {
    int nId;         /* Number of identifiers on the list */
    A[] a;

    public static class A {
      CharPtr zName;      /* Text of the identifier. */
      CharPtr zAlias;     /* The "B" part of a "A AS B" phrase.  zName is the "A" */
      Table pTab;      /* An SQL table corresponding to zName */
      int idx;          /* Index in some Table.aCol[] of a column named zName */
    }
  }

  //
  ///*
  //** The WHERE clause processing routine has two halves.  The
  //** first part does the start of the WHERE loop and the second
  //** half does the tail of the WHERE loop.  An instance of
  //** this public static class ure is returned by the first half and passed
  //** into the second half to give some continuity.
  //*/
  public static class WhereInfo {
    Parse pParse;
    IdList pTabList;    /* List of tables in the join */
    int iContinue;       /* Jump here to continue with next record */
    int iBreak;          /* Jump here to break out of the loop */
    int base;            /* Index of first Open opcode */
    Index[] aIdx = new Index[32];     /* Indices used for each table */
  }

  //
  ///*
  //** An instance of the following public static class ure contains all information
  //** needed to generate code for a single SELECT statement.
  //*/
  public static class Select {
    int isDistinct;        /* True if the DISTINCT keyword is present */
    ExprList pEList;      /* The fields of the result */
    IdList pSrc;          /* The FROM clause */
    Expr pWhere;          /* The WHERE clause */
    ExprList pGroupBy;    /* The GROUP BY clause */
    Expr pHaving;         /* The HAVING clause */
    ExprList pOrderBy;    /* The ORDER BY clause */
    int op;                /* One of: TK_UNION TK_ALL TK_INTERSECT TK_EXCEPT */
    Select pPrior;        /* Prior select in a compound select statement */
  }

  ;
  //
  ///*
  //** The results of a select can be distributed in several ways.
  //*/
  public static final int SRT_Callback = 1;  /* Invoke a callback with each row of result */
  public static final int SRT_Mem = 2;  /* Store result in a memory cell */
  public static final int SRT_Set = 3;  /* Store result as unique keys in a table */
  public static final int SRT_Union = 5;  /* Store result as keys in a table */
  public static final int SRT_Except = 6;  /* Remove result from a UNION table */
  public static final int SRT_Table = 7;  /* Store result as data with a unique key */

  //
  ///*
  //** When a SELECT uses aggregate functions (like "count(*)" or "avg(f1)")
  //** we have to do some additional analysis of expressions.  An instance
  //** of the following public static class ure holds information about a single subexpression
  //** somewhere in the SELECT statement.  An array of these public static class ures holds
  //** all the information we need to generate code for aggregate
  //** expressions.
  //**
  //** Note that when analyzing a SELECT containing aggregates, both
  //** non-aggregate field variables and aggregate functions are stored
  //** in the AggExpr array of the Parser public static class ure.
  //**
  //** The pExpr field points to an expression that is part of either the
  //** field list, the GROUP BY clause, the HAVING clause or the ORDER BY
  //** clause.  The expression will be freed when those clauses are cleaned
  //** up.  Do not try to delete the expression attached to AggExpr.pExpr.
  //**
  //** If AggExpr.pExpr==0, that means the expression is "count(*)".
  //*/
  public static class AggExpr {
    int isAgg;        /* if TRUE contains an aggregate function */
    Expr pExpr;      /* The expression */
  }
  //
  ///*
  //** An SQL parser context
  //*/

  public interface sqlite_callback {
    int call(Object obj, int n, Ptr<CharPtr> sa, CharPtr[] sb);
  }

  public static class Parse {
    sqlite db;          /* The main database public static class ure */
    sqlite_callback xCallback;  /* The callback function */
    Object pArg;          /* First argument to the callback function */
    CharPtr zErrMsg;       /* An error message */
    Token sErrToken;     /* The token at which the error occurred */
    Token sFirstToken;   /* The first token parsed */
    Token sLastToken;    /* The last token parsed */
    Table pNewTable;    /* A table being conpublic static class ed by CREATE TABLE */
    vdbe.Vdbe pVdbe;         /* An engine for executing database bytecode */
    int colNamesSet;     /* TRUE after OP_ColumnCount has been issued to pVdbe */
    int explain;         /* True if the EXPLAIN flag is found on the query */
    int initFlag;        /* True if reparsing CREATE TABLEs */
    int nErr;            /* Number of errors seen */
    int nTab;            /* Number of previously allocated cursors */
    int nMem;            /* Number of memory cells used so far */
    int nSet;            /* Number of sets used so far */
    int nAgg;            /* Number of aggregate expressions */
    AggExpr[] aAgg;       /* An array of aggregate expressions */
    int iAggCount;       /* Index of the count(*) aggregate in aAgg[] */
    int useAgg;          /* If true, extract field values from the aggregator
     ** while generating expressions.  Normally false */
  }
}
