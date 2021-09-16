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
    public dbbe.Dbbe pBe;                 /* The backend driver */
    public int flags;                 /* Miscellanous flags */
    public int file_format;           /* What file format version is this database? */
    public int nTable;                /* Number of tables in the database */
    public Object pBusyArg;            /* 1st Argument to the busy callback */
    //    int (*xBusyCallback)(void *,const char*,int);  /* The busy callback */
    public xBusyCallback xBusyCallback;
    public Table[] apTblHash = new Table[N_HASH];  /* All tables of the database */
    public Index[] apIdxHash = new Index[N_HASH];  /* All indices of the database */
  }

  public static final int SQLITE_VdbeTrace = 0x00000001;
  public static final int SQLITE_Initialized = 0x00000002;
  public static final int SQLITE_FileFormat = 2;

  public static class Column {
    public CharPtr zName;     /* Name of this column */
    public CharPtr zDflt;     /* Default value of this column */
    public int notNull;     /* True if there is a NOT NULL constraint */
  }

  public static class Table {
    public CharPtr zName;     /* Name of the table */
    public Table pHash;    /* Next table with same hash on zName */
    public int nCol;        /* Number of columns in this table */
    public Column[] aCol;    /* Information about each column */
    public int readOnly;    /* True if this table should not be written by the user */
    public Index pIndex;   /* List of SQL indexes on this table. */
  }

  public static class Index {
    public CharPtr zName;     /* Name of this index */
    public Index pHash;    /* Next index with the same hash on zName */
    public int nColumn;     /* Number of columns in the table used by this index */
    //    int *aiColumn;   /* Which columns are used by this index.  1st is 0 */
    public int[] aiColumn;   /* Which columns are used by this index.  1st is 0 */
    public Table pTable;   /* The SQL table being indexed */
    public int isUnique;    /* True if keys must all be unique */
    public Index pNext;    /* The next index associated with the same table */
  }

  public static class Token {
    public CharPtr z;      /* Text of the token */
    public int n;        /* Number of characters in this token */
  }

  public static class Expr {
    public int op;                /* Operation performed by this node */
    public Expr pLeft;
    public Expr pRight;  /* Left and right subnodes */
    public ExprList pList;       /* A list of expressions used as a function argument */
    public Token token;           /* An operand token */
    public Token span;            /* Complete text of the expression */
    public int iTable, iColumn;   /* When op==TK_COLUMN, then this expr node means the
     ** iColumn-th field of the iTable-th table.  When
     ** op==TK_FUNCTION, iColumn holds the function id */
    public int iAgg;              /* When op==TK_COLUMN and pParse->useAgg==TRUE, pull
     ** result from the iAgg-th element of the aggregator */
    public Select pSelect;       /* When the expression is a sub-select */
  }

  public static class ExprList {
    public int nExpr;             /* Number of expressions on the list */
    public A[] a;

    public static class A {
      public Expr pExpr;           /* The list of expressions */
      public CharPtr zName;           /* Token associated with this expression */
      public int sortOrder;        /* 1 for DESC or 0 for ASC */
      public int isAgg;            /* True if this is an aggregate like count(*) */
      public int done;             /* A flag to indicate when processing is finished */
    }                 /* One entry for each expression */
  }

  //
  ///*
  //** A list of identifiers.
  //*/
  public static class IdList {
    public int nId;         /* Number of identifiers on the list */
    public A[] a;

    public static class A {
      public CharPtr zName;      /* Text of the identifier. */
      public CharPtr zAlias;     /* The "B" part of a "A AS B" phrase.  zName is the "A" */
      public Table pTab;      /* An SQL table corresponding to zName */
      public int idx;          /* Index in some Table.aCol[] of a column named zName */
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
    public Parse pParse;
    public IdList pTabList;    /* List of tables in the join */
    public int iContinue;       /* Jump here to continue with next record */
    public int iBreak;          /* Jump here to break out of the loop */
    public int base;            /* Index of first Open opcode */
    public Index[] aIdx = new Index[32];     /* Indices used for each table */
  }

  //
  ///*
  //** An instance of the following public static class ure contains all information
  //** needed to generate code for a single SELECT statement.
  //*/
  public static class Select {
    public int isDistinct;        /* True if the DISTINCT keyword is present */
    public ExprList pEList;      /* The fields of the result */
    public IdList pSrc;          /* The FROM clause */
    public Expr pWhere;          /* The WHERE clause */
    public ExprList pGroupBy;    /* The GROUP BY clause */
    public Expr pHaving;         /* The HAVING clause */
    public ExprList pOrderBy;    /* The ORDER BY clause */
    public int op;                /* One of: TK_UNION TK_ALL TK_INTERSECT TK_EXCEPT */
    public Select pPrior;        /* Prior select in a compound select statement */
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
    public int isAgg;        /* if TRUE contains an aggregate function */
    public Expr pExpr;      /* The expression */
  }
  //
  ///*
  //** An SQL parser context
  //*/

  public interface sqlite_callback {
    int call(Object obj, int n, Ptr<CharPtr> sa, CharPtr[] sb);
  }

  public static class Parse {
    public sqlite db;          /* The main database public static class ure */
    public sqlite_callback xCallback;  /* The callback function */
    public Object pArg;          /* First argument to the callback function */
    public CharPtr zErrMsg;       /* An error message */
    public Token sErrToken;     /* The token at which the error occurred */
    public Token sFirstToken;   /* The first token parsed */
    public Token sLastToken;    /* The last token parsed */
    public Table pNewTable;    /* A table being conpublic static class ed by CREATE TABLE */
    public vdbe.Vdbe pVdbe;         /* An engine for executing database bytecode */
    public int colNamesSet;     /* TRUE after OP_ColumnCount has been issued to pVdbe */
    public int explain;         /* True if the EXPLAIN flag is found on the query */
    public int initFlag;        /* True if reparsing CREATE TABLEs */
    public int nErr;            /* Number of errors seen */
    public int nTab;            /* Number of previously allocated cursors */
    public int nMem;            /* Number of memory cells used so far */
    public int nSet;            /* Number of sets used so far */
    public int nAgg;            /* Number of aggregate expressions */
    public AggExpr[] aAgg;       /* An array of aggregate expressions */
    public int iAggCount;       /* Index of the count(*) aggregate in aAgg[] */
    public int useAgg;          /* If true, extract field values from the aggregator
     ** while generating expressions.  Normally false */
  }
}
