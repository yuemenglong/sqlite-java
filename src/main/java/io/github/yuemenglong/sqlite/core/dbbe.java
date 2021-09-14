package io.github.yuemenglong.sqlite.core;

import io.github.yuemenglong.sqlite.common.CharPtr;
import io.github.yuemenglong.sqlite.common.FILE;
import io.github.yuemenglong.sqlite.common.GDBM.*;

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
    CharPtr azTemp;     /* Names of the temporary files */
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

}
