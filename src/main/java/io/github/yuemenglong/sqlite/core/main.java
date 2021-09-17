package io.github.yuemenglong.sqlite.core;

import io.github.yuemenglong.sqlite.common.Addr;
import io.github.yuemenglong.sqlite.common.CharPtr;
import io.github.yuemenglong.sqlite.common.Ptr;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.yuemenglong.sqlite.core.build.*;
import static io.github.yuemenglong.sqlite.core.dbbe.*;
import static io.github.yuemenglong.sqlite.core.parse.*;
import static io.github.yuemenglong.sqlite.core.sqliteH.*;
import static io.github.yuemenglong.sqlite.core.tokenize.*;
import static io.github.yuemenglong.sqlite.core.vdbe.*;
import static io.github.yuemenglong.sqlite.core.sqliteint.*;
import static io.github.yuemenglong.sqlite.core.util.*;

public class main {
  /*
   ** This is the callback routine for the code that initializes the
   ** database.  Each callback contains text of a CREATE TABLE or
   ** CREATE INDEX statement that must be parsed to yield the internal
   ** structures that describe the tables.
   **
   ** This callback is also called with argc==2 when there is meta
   ** information in the sqlite_master file.  The meta information is
   ** contained in argv[1].  Typical meta information is the file format
   ** version.
   */
  public static int sqliteOpenCb(Object pDb, int argc, Ptr<CharPtr> argv, Ptr<CharPtr> azColName) {
    sqlite db = (sqlite) pDb;
    Parse sParse = new Parse();
    int nErr;

    if (argc == 2) {
      Pattern re = Pattern.compile("file format (\\d+)");
      Matcher m = re.matcher(argv.get(1).toZeroString());
      if (!m.matches()) {
        return 0;
      }
      db.file_format = Integer.parseInt(m.group(1));
//      if (sscanf(argv[1], "file format %d", db.file_format) == 1) {
//        return 0;
//      }
      /* Unknown meta information.  Ignore it. */
      return 0;
    }
    if (argc != 1) return 0;
//    memset(&sParse, 0, sizeof(sParse));
    sParse.db = db;
    sParse.initFlag = 1;
    nErr = sqliteRunParser(sParse, argv.get(0), null);
    return nErr;
  }

  /*
   ** Attempt to read the database schema and initialize internal
   ** data structures.  Return one of the SQLITE_ error codes to
   ** indicate success or failure.
   **
   ** After the database is initialized, the SQLITE_Initialized
   ** bit is set in the flags field of the sqlite structure.  An
   ** attempt is made to initialize the database as soon as it
   ** is opened.  If that fails (perhaps because another process
   ** has the sqlite_master table locked) than another attempt
   ** is made the first time the database is accessed.
   */
  public static int sqliteInit(sqlite db, CharPtr pzErrMsg) {
    Vdbe vdbe;
    int rc;

    /*
     ** The master database table has a structure like this
     */
    String master_schema =
            "CREATE TABLE " + MASTER_NAME + " (\n" +
                    "  type text,\n" +
                    "  name text,\n" +
                    "  tbl_name text,\n" +
                    "  sql text\n" +
                    ")";

    /* The following program is used to initialize the internal
     ** structure holding the tables and indexes of the database.
     ** The database contains a special table named "sqlite_master"
     ** defined as follows:
     **
     **    CREATE TABLE sqlite_master (
     **        type       text,    --  Either "table" or "index" or "meta"
     **        name       text,    --  Name of table or index
     **        tbl_name   text,    --  Associated table
     **        sql        text     --  The CREATE statement for this object
     **    );
     **
     ** The sqlite_master table contains a single entry for each table
     ** and each index.  The "type" column tells whether the entry is
     ** a table or index.  The "name" column is the name of the object.
     ** The "tbl_name" is the name of the associated table.  For tables,
     ** the tbl_name column is always the same as name.  For indices, the
     ** tbl_name column contains the name of the table that the index
     ** indexes.  Finally, the "sql" column contains the complete text of
     ** the CREATE TABLE or CREATE INDEX statement that originally created
     ** the table or index.
     **
     ** If the "type" column has the value "meta", then the "sql" column
     ** contains extra information about the database, such as the
     ** file format version number.  All meta information must be processed
     ** before any tables or indices are constructed.
     **
     ** The following program invokes its callback on the SQL for each
     ** table then goes back and invokes the callback on the
     ** SQL for each index.  The callback will invoke the
     ** parser to build the internal representation of the
     ** database scheme.
     */
    VdbeOp[] initProg = {
            new VdbeOp(OP_Open, 0, 0, MASTER_NAME),
            new VdbeOp(OP_Next, 0, 9, 0),           /* 1 */
            new VdbeOp(OP_Field, 0, 0, 0),
            new VdbeOp(OP_String, 0, 0, "meta"),
            new VdbeOp(OP_Ne, 0, 1, 0),
            new VdbeOp(OP_Field, 0, 0, 0),
            new VdbeOp(OP_Field, 0, 3, 0),
            new VdbeOp(OP_Callback, 2, 0, 0),
            new VdbeOp(OP_Goto, 0, 1, 0),
            new VdbeOp(OP_Rewind, 0, 0, 0),           /* 9 */
            new VdbeOp(OP_Next, 0, 17, 0),           /* 10 */
            new VdbeOp(OP_Field, 0, 0, 0),
            new VdbeOp(OP_String, 0, 0, "table"),
            new VdbeOp(OP_Ne, 0, 10, 0),
            new VdbeOp(OP_Field, 0, 3, 0),
            new VdbeOp(OP_Callback, 1, 0, 0),
            new VdbeOp(OP_Goto, 0, 10, 0),
            new VdbeOp(OP_Rewind, 0, 0, 0),           /* 17 */
            new VdbeOp(OP_Next, 0, 25, 0),           /* 18 */
            new VdbeOp(OP_Field, 0, 0, 0),
            new VdbeOp(OP_String, 0, 0, "index"),
            new VdbeOp(OP_Ne, 0, 18, 0),
            new VdbeOp(OP_Field, 0, 3, 0),
            new VdbeOp(OP_Callback, 1, 0, 0),
            new VdbeOp(OP_Goto, 0, 18, 0),
            new VdbeOp(OP_Halt, 0, 0, 0),           /* 25 */
    };

    /* Create a virtual machine to run the initialization program.  Run
     ** the program.  The delete the virtual machine.
     */
    vdbe = sqliteVdbeCreate(db.pBe);
    if (vdbe == null) {
      sqliteSetString(pzErrMsg, "out of memory", 0);
      return 1;
    }
    sqliteVdbeAddOpList(vdbe, initProg.length, initProg);
    rc = sqliteVdbeExec(vdbe, main::sqliteOpenCb, db, pzErrMsg,
            db.pBusyArg, db.xBusyCallback);
    sqliteVdbeDelete(vdbe);
    if (rc == SQLITE_OK && db.file_format < 2 && db.nTable > 0) {
      sqliteSetString(pzErrMsg, "obsolete file format", 0);
      rc = SQLITE_ERROR;
    }
    if (rc == SQLITE_OK) {
      Table pTab;
      CharPtr azArg[] = new CharPtr[2];
      azArg[0] = new CharPtr(master_schema);
      azArg[1] = null;
      sqliteOpenCb(db, 1, new Ptr<>(azArg), null);
      pTab = sqliteFindTable(db, new CharPtr(MASTER_NAME));
      if (pTab != null) {
        pTab.readOnly = 1;
      }
      db.flags |= SQLITE_Initialized;
    } else {
//      sqliteStrRealloc(pzErrMsg);
    }
    return rc;
  }

  /*
   ** Open a new SQLite database.  Construct an "sqlite" structure to define
   ** the state of this database and return a pointer to that structure.
   **
   ** An attempt is made to initialize the in-memory data structures that
   ** hold the database schema.  But if this fails (because the schema file
   ** is locked) then that step is deferred until the first call to
   ** sqlite_exec().
   */
  public static sqlite sqlite_open(CharPtr zFilename, int mode, CharPtr pzErrMsg) {
    sqlite db;
    int rc;

    /* Allocate the sqlite data structure */
    db = new sqlite();// sqliteMalloc(sizeof(sqlite));
    if (pzErrMsg != null) pzErrMsg.assign(new CharPtr(1024));
    if (db == null) {
      sqliteSetString(pzErrMsg, "out of memory", 0);
//      sqliteStrRealloc(pzErrMsg);
      return null;
    }

    /* Open the backend database driver */
    db.pBe = sqliteDbbeOpen(zFilename, (mode & 0222) != 0 ? 1 : 0, mode != 0 ? 1 : 0, pzErrMsg);
    if (db.pBe == null) {
//      sqliteStrRealloc(pzErrMsg);
      sqliteFree(db);
      return null;
    }

    /* Assume file format 1 unless the database says otherwise */
    db.file_format = 1;

    /* Attempt to read the schema */
    rc = sqliteInit(db, pzErrMsg);
    if (rc != SQLITE_OK && rc != SQLITE_BUSY) {
      sqlite_close(db);
      return null;
    } else {
//      free( * pzErrMsg);
//      *pzErrMsg = 0;
//      pzErrMsg.update(new CharPtr(1024));
    }
    return db;
  }

  /*
   ** Close an existing SQLite database
   */
  public static void sqlite_close(sqlite db) {
    int i;
    sqliteDbbeClose(db.pBe);
    for (i = 0; i < N_HASH; i++) {
      Table pNext, pList = db.apTblHash[i];
      db.apTblHash[i] = null;
      while (pList != null) {
        pNext = pList.pHash;
        pList.pHash = null;
        sqliteDeleteTable(db, pList);
        pList = pNext;
      }
    }
    sqliteFree(db);
  }

  /*
   ** Return TRUE if the given SQL string ends in a semicolon.
   */
  public static int sqlite_complete(CharPtr zSql) {
    int i;
    int lastWasSemi = 0;

    i = 0;
    while (i >= 0 && zSql.get(i) != 0) {
      AtomicInteger tokenType = new AtomicInteger();
      int n;

      n = sqliteGetToken(zSql.ptr(i), new Addr<>(
              tokenType::get,
              tokenType::set
      ));
      switch (tokenType.get()) {
        case TK_SPACE:
        case TK_COMMENT:
          break;
        case TK_SEMI:
          lastWasSemi = 1;
          break;
        default:
          lastWasSemi = 0;
          break;
      }
      i += n;
    }
    return lastWasSemi;
  }

  /*
   ** Execute SQL code.  Return one of the SQLITE_ success/failure
   ** codes.  Also write an error message into memory obtained from
   ** malloc() and make *pzErrMsg point to that message.
   **
   ** If the SQL is a query, then for each row in the query result
   ** the xCallback() function is called.  pArg becomes the first
   ** argument to xCallback().  If xCallback=NULL then no callback
   ** is invoked, even for queries.
   */
  public static int sqlite_exec(
          sqlite db,                 /* The database on which the SQL executes */
          CharPtr zSql,                 /* The SQL to be executed */
          sqlite_callback xCallback,  /* Invoke this callback routine */
          Object pArg,                 /* First argument to xCallback() */
          CharPtr pzErrMsg             /* Write error messages here */
  ) {
    Parse sParse = new Parse();
    int rc;

    if (pzErrMsg != null) pzErrMsg.assign(new CharPtr(1024));
    if ((db.flags & SQLITE_Initialized) == 0) {
      rc = sqliteInit(db, pzErrMsg);
      if (rc != SQLITE_OK) return rc;
    }
//    memset( & sParse, 0, sizeof(sParse));
    sParse.db = db;
    sParse.xCallback = xCallback;
    sParse.pArg = pArg;
    rc = sqliteRunParser(sParse, zSql, pzErrMsg);
//    sqliteStrRealloc(pzErrMsg);
    return rc;
  }

  /*
   ** This routine implements a busy callback that sleeps and tries
   ** again until a timeout value is reached.  The timeout value is
   ** an integer number of milliseconds passed in as the first
   ** argument.
   */
  public static int sqlite_default_busy_callback(
          Object Timeout,           /* Maximum amount of time to wait */
          CharPtr NotUsed,     /* The name of the table that is busy */
          int count                /* Number of times table has been busy */
  ) {
    int rc;
    int delay = 10000;
    int prior_delay = 0;
    int timeout = (int) Timeout;
    int i;
    if ((count + 1) * 1000 > timeout) {
      return 0;
    }
    try {
      Thread.sleep(1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return 1;
  }

  /*
   ** This routine sets the busy callback for an Sqlite database to the
   ** given callback function with the given argument.
   */
  public static void sqlite_busy_handler(
          sqlite db,
          xBusyCallback xBusy,
//          int (*xBusy)(void*,const char*,int),
          Object pArg
  ) {
    db.xBusyCallback = xBusy;
    db.pBusyArg = pArg;
  }

  /*
   ** This routine installs a default busy handler that waits for the
   ** specified number of milliseconds before returning 0.
   */
  public static void sqlite_busy_timeout(sqlite db, int ms) {
    if (ms > 0) {
      sqlite_busy_handler(db, main::sqlite_default_busy_callback, ms);
    } else {
      sqlite_busy_handler(db, null, null);
    }
  }
}
