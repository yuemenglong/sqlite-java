package io.github.yuemenglong.sqlite.common;

public class GDBM {
  public static final int GDBM_READER = 0;  /* A reader. */
  public static final int GDBM_WRITER = 1;  /* A writer. */
  public static final int GDBM_WRCREAT = 2;  /* A writer.  Create the db if needed. */
  public static final int GDBM_NEWDB = 3;  /* A writer.  Always create a new db. */
  public static final int GDBM_FAST = 0x10;  /* Write fast! => No fsyncs.  OBSOLETE. */
  public static final int GDBM_SYNC = 0x20;  /* Sync operations to the disk. */
  public static final int GDBM_NOLOCK = 0x40;/* Don't do file locking operations. */

  /* Parameters to gdbm_store for simple insertion or replacement in the
     case that the key is already in the database. */
  public static final int GDBM_INSERT = 0;  /* Never replace old data with new. */
  public static final int GDBM_REPLACE = 1;  /* Always replace old data with new. */

  /* Parameters to gdbm_setopt, specifing the type of operation to perform. */
  public static final int GDBM_CACHESIZE = 1;       /* Set the cache size. */
  public static final int GDBM_FASTMODE = 2;      /* Toggle fast mode.  OBSOLETE. */
  public static final int GDBM_SYNCMODE = 3;  /* Turn on or off sync operations. */
  public static final int GDBM_CENTFREE = 4;/* Keep all free blocks in the header. */
  public static final int GDBM_COALESCEBLKS = 5;/* Attempt to coalesce free blocks. */

  public static class GDBM_FILE {

    public GDBM_FILE(String path) {

    }

    public void gdbm_close() {
    }

    public void gdbm_reorganize() {
    }

    public void gdbm_sync() {
    }

    public datum gdbm_fetch(datum key) {
      return null;
    }

    /*
     ** Return 1 if the given key is already in the table.  Return 0
     ** if it is not.
     */
    public int gdbm_exists(datum key) {
      return 0;
    }

    public datum gdbm_firstkey() {
      return null;
    }

    public datum gdbm_nextkey(datum key) {
      return null;
    }

    public int gdbm_store(datum key, datum data, int gdbmReplace) {
      return 0;
    }

    // 0 succ
    public int gdbm_delete(datum key) {
      return 0;
    }
  }

  public static class datum {
    public CharPtr dptr;
    public int dsize;
  }
}
