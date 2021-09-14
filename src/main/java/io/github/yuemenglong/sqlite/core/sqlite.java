package io.github.yuemenglong.sqlite.core;

public class sqlite {
  //  typedef int (*sqlite_callback)(void*,int,char**, char**);

  public static final int SQLITE_OK = 0;    /* Successful result */
  public static final int SQLITE_INTERNAL = 1;    /* An internal logic error in SQLite */
  public static final int SQLITE_ERROR = 2;  /* SQL error or missing database */
  public static final int SQLITE_PERM = 3; /* Access permission denied */
  public static final int SQLITE_ABORT = 4; /* Callback routine requested an abort */
  public static final int SQLITE_BUSY = 5; /* One or more database files are locked */
  public static final int SQLITE_NOMEM = 6; /* A malloc() failed */
  public static final int SQLITE_READONLY = 7; /* Attempt to write a readonly database */
}
