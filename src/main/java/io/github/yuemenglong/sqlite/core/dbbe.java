package io.github.yuemenglong.sqlite.core;

import io.github.yuemenglong.sqlite.common.Addr;
import io.github.yuemenglong.sqlite.common.CharPtr;
import io.github.yuemenglong.sqlite.common.FILE;
import io.github.yuemenglong.sqlite.common.GDBM.*;

import static io.github.yuemenglong.sqlite.common.Util.*;
import static io.github.yuemenglong.sqlite.core.sqlite.*;
import static io.github.yuemenglong.sqlite.core.util.*;

import java.io.File;
import java.util.concurrent.atomic.AtomicReference;


public class dbbe {
  //
  /*
   ** Information about each open disk file is an instance of this
   ** structure.  There will only be one such structure for each
   ** disk file.  If the VDBE opens the same file twice (as will happen
   ** for a self-join, for example) then two DbbeCursor structures are
   ** created but there is only a single BeFile structure with an
   ** nRef of 2.
   */
  public static class BeFile {
    CharPtr zName;            /* Name of the file */
    GDBM_FILE dbf;          /* The file itself */
    int nRef;               /* Number of references */
    int delOnClose;         /* Delete when closing */
    int writeable;          /* Opened for writing */
    BeFile pNext;
    BeFile pPrev;  /* Next and previous on list of open files */
  }

  /*
   ** The following structure holds the current state of the RC4 algorithm.
   ** We use RC4 as a random number generator.  Each call to RC4 gives
   ** a random 8-bit number.
   **
   ** Nothing in this file or anywhere else in SQLite does any kind of
   ** encryption.  The RC4 algorithm is being used as a PRNG (pseudo-random
   ** number generator) not as an encryption device.
   */
  public static class rc4 {
    int i;
    int j;
    int[] s = new int[256];
  }

  /*
   ** The complete database is an instance of the following structure.
   */
  public static class Dbbe {
    CharPtr zDir;        /* The directory containing the database */
    int write;         /* True for write permission */
    BeFile pOpen;     /* List of open files */
    int nTemp;         /* Number of temporary files created */
    FILE[] apTemp;     /* Space to hold temporary file pointers */
    CharPtr[] azTemp;     /* Names of the temporary files */
    rc4 rc4 = new rc4();    /* The random number generator */
  }

  /*
   ** An cursor into a database file is an instance of the following structure.
   ** There can only be a single BeFile structure for each disk file, but
   ** there can be multiple DbbeCursor structures.  Each DbbeCursor represents
   ** a cursor pointing to a particular part of the open BeFile.  The
   ** BeFile.nRef field hold a count of the number of DbbeCursor structures
   ** associated with the same disk file.
   */
  public static class DbbeCursor {
    Dbbe pBe;         /* The database of which this record is a part */
    BeFile pFile;     /* The database file for this table */
    datum key;         /* Most recently used key */
    datum data;        /* Most recent data */
    int needRewind;    /* Next key should be the first */
    int readPending;   /* The fetch hasn't actually been done yet */
  }


  /*
   ** Initialize the RC4 PRNG.  "seed" is a pointer to some random
   ** data used to initialize the PRNG.
   */
  public static void rc4init(rc4 p, CharPtr seed, int seedlen) {
    int i;
    char[] k = new char[256];
    p.j = 0;
    p.i = 0;
    for (i = 0; i < 256; i++) {
      p.s[i] = i;
      k[i] = seed.get(i % seedlen);
    }
    for (i = 0; i < 256; i++) {
      int t;
      p.j = (p.j + p.s[i] + k[i]) & 0xff;
      t = p.s[p.j];
      p.s[p.j] = p.s[i];
      p.s[i] = t;
    }
  }

  /*
   ** Get a single 8-bit random value from the RC4 PRNG.
   */
  public static int rc4byte(rc4 p) {
    int t;
    p.i = (p.i + 1) & 0xff;
    p.j = (p.j + p.s[p.i]) & 0xff;
    t = p.s[p.i];
    p.s[p.i] = p.s[p.j];
    p.s[p.j] = t;
    t = p.s[p.i] + p.s[p.j];
    return t & 0xff;
  }


  /*
   ** This routine opens a new database.  For the GDBM driver
   ** implemented here, the database name is the name of the directory
   ** containing all the files of the database.
   **
   ** If successful, a pointer to the Dbbe structure is returned.
   ** If there are errors, an appropriate error message is left
   ** in *pzErrMsg and NULL is returned.
   */
  public static Dbbe sqliteDbbeOpen(
          CharPtr zName,     /* The name of the database */
          int writeFlag,         /* True if we will be writing to the database */
          int createFlag,        /* True to create database if it doesn't exist */
          CharPtr pzErrMsg        /* Write error messages (if any) here */
  ) {
    Dbbe pNew;
    CharPtr zMaster;

    if (writeFlag == 0) createFlag = 0;
    File dir = new File(zName.toZeroString());
    if (!dir.exists()) {
      if (!dir.mkdirs()) {
        sqliteSetString(pzErrMsg, createFlag != 0 ?
                        new CharPtr("can't find or create directory \"") : new CharPtr("can't find directory \""),
                zName, new CharPtr("\""));
        return null;
      }
    }
    if (!dir.isDirectory()) {
      sqliteSetString(pzErrMsg, new CharPtr("not a directory: \""), zName, new CharPtr("\""));
      return null;
    }
    pNew = new Dbbe();
    pNew.zDir = new CharPtr(zName.strlen() + 1);
    pNew.zDir.strcpy(zName);
    pNew.write = writeFlag;
    pNew.pOpen = null;
    CharPtr rnd = new CharPtr("" + System.currentTimeMillis());
    rc4init(pNew.rc4, rnd, rnd.strlen());
    return pNew;
  }

  /*
   ** Completely shutdown the given database.  Close all files.  Free all memory.
   */
  void sqliteDbbeClose(Dbbe pBe) {
    BeFile pFile;
    BeFile pNext;
    int i;
    for (pFile = pBe.pOpen; pFile != null; pFile = pNext) {
      pNext = pFile.pNext;
      pFile.dbf.gdbm_close();
//      memset(pFile, 0, sizeof( * pFile));
//      sqliteFree(pFile);
    }
    for (i = 0; i < pBe.nTemp; i++) {
      if (pBe.apTemp[i] != null) {
        unlink(pBe.azTemp[i].toZeroString());
        pBe.apTemp[i].close();
//        sqliteFree(pBe . azTemp[i]);
        pBe.apTemp[i] = null;
        pBe.azTemp[i] = null;
        break;
      }
    }
//    sqliteFree(pBe . azTemp);
//    sqliteFree(pBe . apTemp);
//    memset(pBe, 0, sizeof( * pBe));
//    sqliteFree(pBe);
  }

  /*
   ** Translate the name of an SQL table (or index) into the name
   ** of a file that holds the key/data pairs for that table or
   ** index.  Space to hold the filename is obtained from
   ** sqliteMalloc() and must be freed by the calling function.
   */
  public static CharPtr sqliteFileOfTable(Dbbe pBe, CharPtr zTable) {
    CharPtr zFile = new CharPtr();
    int i;
    sqliteSetString(zFile, pBe.zDir, new CharPtr("/"), zTable, new CharPtr(".tbl"));
    for (i = (pBe.zDir.strlen()) + 1; zFile.get(i) != 0; i++) {
      int c = zFile.get(i);
      if (isupper(c)) {
        zFile.set(i, tolower(c));
      } else if (!isalnum(c) && c != '-' && c != '_' && c != '.') {
        zFile.set(i, '+');
      }
    }
    return zFile;
  }

  /*
   ** Generate a random filename with the given prefix.  The new filename
   ** is written into zBuf[].  The calling function must insure that
   ** zBuf[] is big enough to hold the prefix plus 20 or so extra
   ** characters.
   **
   ** Very random names are chosen so that the chance of a
   ** collision with an existing filename is very very small.
   */
  static void randomName(rc4 pRc4, CharPtr zBuf, CharPtr zPrefix) {
    int i, j;
    char[] zRandomChars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
//    strcpy(zBuf, zPrefix);
    zBuf.strcpy(zPrefix);
//    j = strlen(zBuf);
    j = zBuf.strlen();
    for (i = 0; i < 15; i++) {
      int c = rc4byte(pRc4) % (zRandomChars.length - 1);
      zBuf.set(j++, zRandomChars[c]);
    }
    zBuf.set(j, 0);
  }

  /*
   ** Open a new table cursor.  Write a pointer to the corresponding
   ** DbbeCursor structure into *ppCursr.  Return an integer success
   ** code:
   **
   **    SQLITE_OK          It worked!
   **
   **    SQLITE_NOMEM       sqliteMalloc() failed
   **
   **    SQLITE_PERM        Attempt to access a file for which file
   **                       access permission is denied
   **
   **    SQLITE_BUSY        Another thread or process is already using
   **                       the corresponding file and has that file locked.
   **
   **    SQLITE_READONLY    The current thread already has this file open
   **                       readonly but you are trying to open for writing.
   **                       (This can happen if a SELECT callback tries to
   **                       do an UPDATE or DELETE.)
   **
   ** If zTable is 0 or "", then a temporary database file is created and
   ** a cursor to that temporary file is opened.  The temporary file
   ** will be deleted from the disk when it is closed.
   */
  int sqliteDbbeOpenCursor(
          Dbbe pBe,              /* The database the table belongs to */
          CharPtr zTable,     /* The SQL name of the file to be opened */
          int writeable,          /* True to open for writing */
          Addr<DbbeCursor> ppCursr    /* Write the resulting table pointer here */
  ) {
    CharPtr zFile;            /* Name of the table file */
    DbbeCursor pCursr;     /* The new table cursor */
    BeFile pFile;          /* The underlying data file for this table */
    int rc = SQLITE_OK;     /* Return value */
    int rw_mask;            /* Permissions mask for opening a table */
    int mode;               /* Mode for opening a table */

//  *ppCursr = 0;
    ppCursr.set(null);
    pCursr = new DbbeCursor();//sqliteMalloc( sizeof(*pCursr) );
    if (zTable != null) {
      zFile = sqliteFileOfTable(pBe, zTable);
      for (pFile = pBe.pOpen; pFile != null; pFile = pFile.pNext) {
        if (pFile.zName.strcmp(zFile) == 0) break;
      }
    } else {
      pFile = null;
      zFile = null;
    }
    if (pFile == null) {
//      if( writeable!=0 ){
//        rw_mask = GDBM_WRCREAT | GDBM_FAST;
//        mode = 0640;
//      }else{
//        rw_mask = GDBM_READER;
//        mode = 0640;
//      }
      pFile = new BeFile();//sqliteMalloc( sizeof(*pFile) );
      if (zFile != null) {
        if (writeable == 0 || pBe.write != 0) {
//          pFile.dbf = gdbm_open(zFile, 0, rw_mask, mode, 0);
          pFile.dbf = new GDBM_FILE(zFile.toZeroString());//gdbm_open(zFile, 0, rw_mask, mode, 0);
        } else {
          pFile.dbf = null;
        }
      } else {
        int limit;
        rc4 pRc4;
        CharPtr zRandom = new CharPtr(50);
        pRc4 = pBe.rc4;
        zFile = null;
        limit = 5;
        do {
          randomName(pBe.rc4, zRandom, new CharPtr("_temp_table_"));
//          sqliteFree(zFile);
          zFile = sqliteFileOfTable(pBe, zRandom);
          pFile.dbf = new GDBM_FILE(zFile.toZeroString());//gdbm_open(zFile, 0, rw_mask, mode, 0);
        } while (pFile.dbf == null && limit-- >= 0);
        pFile.delOnClose = 1;
      }
      pFile.writeable = writeable;
      pFile.zName = zFile;
      pFile.nRef = 1;
      pFile.pPrev = null;
      if (pBe.pOpen != null) {
        pBe.pOpen.pPrev = pFile;
      }
      pFile.pNext = pBe.pOpen;
      pBe.pOpen = pFile;
      if (pFile.dbf == null) {
        if (writeable == 0 && access(zFile.toZeroString(), 0) != 0) {
          /* Trying to read a non-existant file.  This is OK.  All the
           ** reads will return empty, which is what we want. */
          rc = SQLITE_OK;
        } else if (pBe.write == 0) {
          rc = SQLITE_READONLY;
        } else if (access(zFile.toZeroString(), 0) != 0) {
          rc = SQLITE_PERM;
        } else {
          rc = SQLITE_BUSY;
        }
      }
    } else {
//      sqliteFree(zFile);
      pFile.nRef++;
      if (writeable != 0 && pFile.writeable == 0) {
        rc = SQLITE_READONLY;
      }
    }
    pCursr.pBe = pBe;
    pCursr.pFile = pFile;
    pCursr.readPending = 0;
    pCursr.needRewind = 1;
    if (rc != SQLITE_OK) {
      sqliteDbbeCloseCursor(pCursr);
      ppCursr.set(null);
    } else {
      ppCursr.set(pCursr);
    }
    return rc;
  }

  /*
   ** Drop a table from the database.  The file on the disk that corresponds
   ** to this table is deleted.
   */
  void sqliteDbbeDropTable(Dbbe pBe, CharPtr zTable) {
    CharPtr zFile;            /* Name of the table file */

    zFile = sqliteFileOfTable(pBe, zTable);
    unlink(zFile.toZeroString());
//    sqliteFree(zFile);
  }

  /*
   ** Reorganize a table to reduce search times and disk usage.
   */
  int sqliteDbbeReorganizeTable(Dbbe pBe, CharPtr zTable) {
    AtomicReference<DbbeCursor> pCrsr = new AtomicReference<>();
    int rc;

    rc = sqliteDbbeOpenCursor(pBe, zTable, 1,
            new Addr<>(pCrsr::get, pCrsr::set));
    if (rc != SQLITE_OK) {
      return rc;
    }
    if (pCrsr.get() != null && pCrsr.get().pFile != null && pCrsr.get().pFile.dbf != null) {
      pCrsr.get().pFile.dbf.gdbm_reorganize();
//      gdbm_reorganize(pCrsr.pFile.get().dbf);
    }
    if (pCrsr.get() != null) {
      sqliteDbbeCloseCursor(pCrsr.get());
    }
    return SQLITE_OK;
  }

  /*
   ** Close a cursor previously opened by sqliteDbbeOpenCursor().
   **
   ** There can be multiple cursors pointing to the same open file.
   ** The underlying file is not closed until all cursors have been
   ** closed.  This routine decrements the BeFile.nref field of the
   ** underlying file and closes the file when nref reaches 0.
   */
  void sqliteDbbeCloseCursor(DbbeCursor pCursr) {
    BeFile pFile;
    Dbbe pBe;
    if (pCursr == null) return;
    pFile = pCursr.pFile;
    pBe = pCursr.pBe;
    pFile.nRef--;
    if (pFile.dbf != null) {
      pFile.dbf.gdbm_sync();
//      gdbm_sync(pFile.dbf);
    }
    if (pFile.nRef <= 0) {
      if (pFile.dbf != null) {
        pFile.dbf.gdbm_close();
//        gdbm_close(pFile.dbf);
      }
      if (pFile.pPrev != null) {
        pFile.pPrev.pNext = pFile.pNext;
      } else {
        pBe.pOpen = pFile.pNext;
      }
      if (pFile.pNext != null) {
        pFile.pNext.pPrev = pFile.pPrev;
      }
      if (pFile.delOnClose != 0) {
        unlink(pFile.zName.toZeroString());
      }
//      sqliteFree(pFile.zName);
//      memset(pFile, 0, sizeof( * pFile));
//      sqliteFree(pFile);
    }
//    if (pCursr.key.dptr) free(pCursr.key.dptr);
//    if (pCursr.data.dptr) free(pCursr.data.dptr);
//    memset(pCursr, 0, sizeof( * pCursr));
//    sqliteFree(pCursr);
  }

  /*
   ** Clear the given datum
   */
  static void datumClear(datum p) {
//    if (p.dptr!=null) free(p.dptr);
    p.dptr = null;
    p.dsize = 0;
  }

}
