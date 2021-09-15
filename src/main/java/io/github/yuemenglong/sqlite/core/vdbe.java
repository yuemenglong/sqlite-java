package io.github.yuemenglong.sqlite.core;

import io.github.yuemenglong.sqlite.common.*;
import io.github.yuemenglong.sqlite.core.sqliteint.*;

import static io.github.yuemenglong.sqlite.common.Util.isspace;
import static io.github.yuemenglong.sqlite.core.dbbe.*;
import static io.github.yuemenglong.sqlite.core.sqliteH.SQLITE_ABORT;
import static io.github.yuemenglong.sqlite.core.sqliteH.SQLITE_OK;
import static io.github.yuemenglong.sqlite.core.util.*;

import io.github.yuemenglong.sqlite.core.dbbe.*;

import java.util.Arrays;


@SuppressWarnings("unused")
public class vdbe {

  /*
   ** A single VDBE is an opaque structure named "Vdbe".  Only routines
   ** in the source file sqliteVdbe.c are allowed to see the insides
   ** of this structure.
   */

  /*
   ** A single instruction of the virtual machine has an opcode
   ** and as many as three operands.  The instruction is recorded
   ** as an instance of the following structure:
   */
  public static class VdbeOp {
    int opcode;         /* What operation to perform */
    int p1;             /* First operand */
    int p2;             /* Second parameter (often the jump destination) */
    CharPtr p3;         /* Third parameter */

    public VdbeOp(int opcode, int p1, int p2, int p3) {
      this(opcode, p1, p2, null);
    }

    public VdbeOp(int opcode, int p1, int p2, String p3) {
      this.opcode = opcode;
      this.p1 = p1;
      this.p2 = p2;
      if (p3 != null) {
        this.p3 = new CharPtr(p3);
      }
    }
  }

  /*
   ** The following macro converts a relative address in the p2 field
   ** of a VdbeOp structure into a negative number so that
   ** sqliteVdbeAddOpList() knows that the address is relative.  Calling
   ** the macro again restores the address.
   */
  public static int ADDR(int X) {
    return (-1 - (X));
  }

  /*
   ** These are the available opcodes.
   **
   ** If any of the values changes or if opcodes are added or removed,
   ** be sure to also update the zOpName[] array in sqliteVdbe.c to
   ** mirror the change.
   **
   ** The source tree contains an AWK script named renumberOps.awk that
   ** can be used to renumber these opcodes when new opcodes are inserted.
   */
  public static final int OP_Open = 1;
  public static final int OP_Close = 2;
  public static final int OP_Fetch = 3;
  public static final int OP_Fcnt = 4;
  public static final int OP_New = 5;
  public static final int OP_Put = 6;
  public static final int OP_Distinct = 7;
  public static final int OP_Found = 8;
  public static final int OP_NotFound = 9;
  public static final int OP_Delete = 10;
  public static final int OP_Field = 11;
  public static final int OP_KeyAsData = 12;
  public static final int OP_Key = 13;
  public static final int OP_Rewind = 14;
  public static final int OP_Next = 15;

  public static final int OP_Destroy = 16;
  public static final int OP_Reorganize = 17;

  public static final int OP_ResetIdx = 18;
  public static final int OP_NextIdx = 19;
  public static final int OP_PutIdx = 20;
  public static final int OP_DeleteIdx = 21;

  public static final int OP_MemLoad = 22;
  public static final int OP_MemStore = 23;

  public static final int OP_ListOpen = 24;
  public static final int OP_ListWrite = 25;
  public static final int OP_ListRewind = 26;
  public static final int OP_ListRead = 27;
  public static final int OP_ListClose = 28;

  public static final int OP_SortOpen = 29;
  public static final int OP_SortPut = 30;
  public static final int OP_SortMakeRec = 31;
  public static final int OP_SortMakeKey = 32;
  public static final int OP_Sort = 33;
  public static final int OP_SortNext = 34;
  public static final int OP_SortKey = 35;
  public static final int OP_SortCallback = 36;
  public static final int OP_SortClose = 37;

  public static final int OP_FileOpen = 38;
  public static final int OP_FileRead = 39;
  public static final int OP_FileField = 40;
  public static final int OP_FileClose = 41;

  public static final int OP_AggReset = 42;
  public static final int OP_AggFocus = 43;
  public static final int OP_AggIncr = 44;
  public static final int OP_AggNext = 45;
  public static final int OP_AggSet = 46;
  public static final int OP_AggGet = 47;

  public static final int OP_SetInsert = 48;
  public static final int OP_SetFound = 49;
  public static final int OP_SetNotFound = 50;
  public static final int OP_SetClear = 51;

  public static final int OP_MakeRecord = 52;
  public static final int OP_MakeKey = 53;

  public static final int OP_Goto = 54;
  public static final int OP_If = 55;
  public static final int OP_Halt = 56;

  public static final int OP_ColumnCount = 57;
  public static final int OP_ColumnName = 58;
  public static final int OP_Callback = 59;

  public static final int OP_Integer = 60;
  public static final int OP_String = 61;
  public static final int OP_Null = 62;
  public static final int OP_Pop = 63;
  public static final int OP_Dup = 64;
  public static final int OP_Pull = 65;

  public static final int OP_Add = 66;
  public static final int OP_AddImm = 67;
  public static final int OP_Subtract = 68;
  public static final int OP_Multiply = 69;
  public static final int OP_Divide = 70;
  public static final int OP_Min = 71;
  public static final int OP_Max = 72;
  public static final int OP_Like = 73;
  public static final int OP_Glob = 74;
  public static final int OP_Eq = 75;
  public static final int OP_Ne = 76;
  public static final int OP_Lt = 77;
  public static final int OP_Le = 78;
  public static final int OP_Gt = 79;
  public static final int OP_Ge = 80;
  public static final int OP_IsNull = 81;
  public static final int OP_NotNull = 82;
  public static final int OP_Negative = 83;
  public static final int OP_And = 84;
  public static final int OP_Or = 85;
  public static final int OP_Not = 86;
  public static final int OP_Concat = 87;
  public static final int OP_Noop = 88;

  public static final int OP_MAX = 88;

  /*
   ** Prototypes for the VDBE interface.  See comments on the implementation
   ** for a description of what each of these routines does.
   */

  public static class Op extends VdbeOp {

    public Op(int opcode, int p1, int p2, int p3) {
      super(opcode, p1, p2, p3);
    }

    public Op(int opcode, int p1, int p2, String p3) {
      super(opcode, p1, p2, p3);
    }
  }

  ///*
  //** A cursor is a pointer into a database file.  The database file
  //** can represent either an SQL table or an SQL index.  Each file is
  //** a bag of key/data pairs.  The cursor can loop over all key/data
  //** pairs (in an arbitrary order) or it can retrieve a particular
  //** key/data pair given a copy of the key.
  //**
  //** Every cursor that the virtual machine has open is represented by an
  //** instance of the following structure.
  //*/
  public static class Cursor {
    DbbeCursor pCursor;  /* The cursor structure of the backend */
    int index;            /* The next index to extract */
    int keyAsData;        /* The OP_Field command works on key instead of data */
  }

  //
  ///*
  //** A sorter builds a list of elements to be sorted.  Each element of
  //** the list is an instance of the following structure.
  //*/
  //typedef struct Sorter Sorter;
  public static class Sorter {
    int nKey;           /* Number of bytes in the key */
    CharPtr zKey;         /* The key by which we will sort */
    int nData;          /* Number of bytes in the data */
    CharPtr pData;        /* The data associated with this key */
    Sorter pNext;      /* Next in the list */
  }

  //
  ///*
  //** Number of buckets used for merge-sort.
  //*/
  public static final int NSORT = 30;

  //
  ///*
  //** A single level of the stack is an instance of the following
  //** structure.  Except, string values are stored on a separate
  //** list of of pointers to character.  The reason for storing
  //** strings separately is so that they can be easily passed
  //** to the callback function.
  //*/
  public static class Stack {
    int i;         /* Integer value */
    int n;         /* Number of characters in string value, including '\0' */
    int flags;     /* Some combination of STK_Null, STK_Str, STK_Dyn, etc. */
    double r;      /* Real value */
  }

  //typedef struct Stack Stack;
  //
  ///*
  //** Memory cells use the same structure as the stack except that space
  //** for an arbitrary string is added.
  //*/
  public static class Mem {
    Stack s;       /* All values of the memory cell besides string */
    CharPtr z;       /* String value for this memory cell */
  }

  //typedef struct Mem Mem;
  //
  ///*
  //** Allowed values for Stack.flags
  //*/
  public static final int STK_Null = 0x0001;   /* Value is NULL */
  public static final int STK_Str = 0x0002;   /* Value is a string */
  public static final int STK_Int = 0x0004;   /* Value is an integer */
  public static final int STK_Real = 0x0008;   /* Value is a real number */
  public static final int STK_Dyn = 0x0010;   /* Need to call sqliteFree() on zStack[*] */

  //
  ///*
  //** An Agg structure describes an Aggregator.  Each Agg consists of
  //** zero or more Aggregator elements (AggElem).  Each AggElem contains
  //** a key and one or more values.  The values are used in processing
  //** aggregate functions in a SELECT.  The key is used to implement
  //** the GROUP BY clause of a select.
  //*/
  //typedef struct Agg Agg;
  //typedef struct AggElem AggElem;
  public static class Agg {
    int nMem;              /* Number of values stored in each AggElem */
    AggElem pCurrent;     /* The AggElem currently in focus */
    int nElem;             /* The number of AggElems */
    int nHash;             /* Number of slots in apHash[] */
    AggElem[] apHash;      /* A hash array for looking up AggElems by zKey */
    AggElem pFirst;       /* A list of all AggElems */
  }

  public static class AggElem {
    CharPtr zKey;            /* The key to this AggElem */
    AggElem pHash;        /* Next AggElem with the same hash on zKey */
    AggElem pNext;        /* Next AggElem in a list of them all */
    Mem[] aMem;           /* The values for this AggElem */
  }

  //
  ///*
  //** A Set structure is used for quick testing to see if a value
  //** is part of a small set.  Sets are used to implement code like
  //** this:
  //**            x.y IN ('hi','hoo','hum')
  //*/
  //typedef struct Set Set;
  //typedef struct SetElem SetElem;
  public static class Set {
    SetElem pAll;         /* All elements of this set */
    SetElem[] apHash = new SetElem[41];   /* A hash array for all elements in this set */
  }

  public static class SetElem {
    SetElem pHash;        /* Next element with the same hash on zKey */
    SetElem pNext;        /* Next element in a list of them all */
    CharPtr zKey;          /* Value of this key */
  }

  //
  ///*
  //** An instance of the virtual machine
  //*/
  public static class Vdbe {
    Dbbe pBe;          /* Opaque context structure used by DB backend */
    FILE trace;        /* Write an execution trace here, if not NULL */
    int nOp;            /* Number of instructions in the program */
    int nOpAlloc;       /* Number of slots allocated for aOp[] */
    Op[] aOp;            /* Space to hold the virtual machine's program */
    int nLabel;         /* Number of labels used */
    int nLabelAlloc;    /* Number of slots allocated in aLabel[] */
    Integer[] aLabel; /* Space to hold the labels */
    int tos;            /* Index of top of stack */
    int nStackAlloc;    /* Size of the stack */
    Stack[] aStack;      /* The operand stack, except string values */
    CharPtr[] zStack;      /* Text or binary values of the stack */
    CharPtr[] azColName;   /* Becomes the 4th parameter to callbacks */
    int nCursor;        /* Number of slots in aCsr[] */
    Cursor[] aCsr;       /* On element of this array for each open cursor */
    int nList;          /* Number of slots in apList[] */
    FILE[] apList;      /* An open file for each list */
    int nSort;          /* Number of slots in apSort[] */
    Sorter[] apSort;    /* An open sorter list */
    FILE pFile;        /* At most one open file handler */
    int nField;         /* Number of file fields */
    CharPtr[] azField;     /* Data for each file field */
    CharPtr zLine;        /* A single line from the input file */
    int nLineAlloc;     /* Number of spaces allocated for zLine */
    int nMem;           /* Number of memory locations currently allocated */
    Mem[] aMem;          /* The memory locations */
    Agg agg;            /* Aggregate information */
    int nSet;           /* Number of sets allocated */
    Set[] aSet;          /* An array of sets */
    int nFetch;         /* Number of OP_Fetch instructions executed */
  }

  /*
   ** Create a new virtual database engine.
   */
  public static Vdbe sqliteVdbeCreate(Dbbe pBe) {
    Vdbe p = new Vdbe();
    p.pBe = pBe;
    return p;
  }

  /*
   ** Turn tracing on or off
   */
  public static void sqliteVdbeTrace(Vdbe p, FILE trace) {
    p.trace = trace;
  }

  /*
   ** Add a new instruction to the list of instructions current in the
   ** VDBE.  Return the address of the new instruction.
   **
   ** Parameters:
   **
   **    p               Pointer to the VDBE
   **
   **    op              The opcode for this instruction
   **
   **    p1, p2, p3      Three operands.
   **
   **    lbl             A symbolic label for this instruction.
   **
   ** Symbolic labels are negative numbers that stand for the address
   ** of instructions that have yet to be coded.  When the instruction
   ** is coded, its real address is substituted in the p2 field of
   ** prior and subsequent instructions that have the lbl value in
   ** their p2 fields.
   */
  public static int sqliteVdbeAddOp(Vdbe p, int op, int p1, int p2, int p3, int lbl) {
    Assert.assertTrue(p3 == 0);
    return sqliteVdbeAddOp(p, op, p1, p2, null, lbl);
  }

  public static int sqliteVdbeAddOp(Vdbe p, int op, int p1, int p2, CharPtr p3, int lbl) {
    int i, j;

    i = p.nOp;
    p.nOp++;
    if (i >= p.nOpAlloc) {
      int oldSize = p.nOpAlloc;
      p.nOpAlloc = p.nOpAlloc * 2 + 10;
//      p.aOp = new Op[p.nOpAlloc];
      p.aOp = sqliteRealloc(p.aOp, p.nOpAlloc);
//      memset(&p.aOp[oldSize], 0, (p.nOpAlloc-oldSize)*sizeof(Op));
    }
    p.aOp[i].opcode = op;
    p.aOp[i].p1 = p1;
    if (p2 < 0 && (-1 - p2) < p.nLabel && p.aLabel[-1 - p2] >= 0) {
      p2 = p.aLabel[-1 - p2];
    }
    p.aOp[i].p2 = p2;
    if (p3 != null && p3.get(0) != 0) {
      p.aOp[i].p3 = sqliteStrDup(p3);
    } else {
      p.aOp[i].p3 = null;
    }
    if (lbl < 0 && (-lbl) <= p.nLabel) {
      p.aLabel[-1 - lbl] = i;
      for (j = 0; j < i; j++) {
        if (p.aOp[j].p2 == lbl) p.aOp[j].p2 = i;
      }
    }
    return i;
  }

  /*
   ** Resolve label "x" to be the address of the next instruction to
   ** be inserted.
   */
  public static void sqliteVdbeResolveLabel(Vdbe p, int x) {
    int j;
    if (x < 0 && (-x) <= p.nLabel) {
      p.aLabel[-1 - x] = p.nOp;
      for (j = 0; j < p.nOp; j++) {
        if (p.aOp[j].p2 == x) p.aOp[j].p2 = p.nOp;
      }
    }
  }

  /*
   ** Return the address of the next instruction to be inserted.
   */
  public static int sqliteVdbeCurrentAddr(Vdbe p) {
    return p.nOp;
  }

  /*
   ** Add a whole list of operations to the operation stack.  Return the
   ** address of the first operation added.
   */
  public static int sqliteVdbeAddOpList(Vdbe p, int nOp, VdbeOp[] aOp) {
    int addr;
    if (p.nOp + nOp >= p.nOpAlloc) {
      int oldSize = p.nOpAlloc;
      p.nOpAlloc = p.nOpAlloc * 2 + nOp + 10;
      p.aOp = sqliteRealloc(p.aOp, p.nOpAlloc);
//      memset(&p.aOp[oldSize], 0, (p.nOpAlloc-oldSize)*sizeof(Op));
    }
    addr = p.nOp;
    if (nOp > 0) {
      int i;
      for (i = 0; i < nOp; i++) {
        int p2 = aOp[i].p2;
        if (p2 < 0) p2 = addr + ADDR(p2);
        sqliteVdbeAddOp(p, aOp[i].opcode, aOp[i].p1, p2, aOp[i].p3, 0);
      }
    }
    return addr;
  }

  /*
   ** Change the value of the P3 operand for a specific instruction.
   ** This routine is useful when a large program is loaded from a
   ** static array using sqliteVdbeAddOpList but we want to make a
   ** few minor changes to the program.
   */
  public static void sqliteVdbeChangeP3(Vdbe p, int addr, CharPtr zP3, int n) {
    if (p != null && addr >= 0 && p.nOp > addr && zP3 != null) {
      sqliteSetNString(p.aOp[addr].p3, zP3, n, 0);
    }
  }

  /*
   ** If the P3 operand to the specified instruction appears
   ** to be a quoted string token, then this procedure removes
   ** the quotes.
   **
   ** The quoting operator can be either a grave ascent (ASCII 0x27)
   ** or a double quote character (ASCII 0x22).  Two quotes in a row
   ** resolve to be a single actual quote character within the string.
   */
  public static void sqliteVdbeDequoteP3(Vdbe p, int addr) {
    CharPtr z;
    if (addr < 0 || addr >= p.nOp) return;
    z = p.aOp[addr].p3;
    sqliteDequote(z);
  }

  /*
   ** On the P3 argument of the given instruction, change all
   ** strings of whitespace characters into a single space and
   ** delete leading and trailing whitespace.
   */
  public static void sqliteVdbeCompressSpace(Vdbe p, int addr) {
    CharPtr z;
    int i, j;
    if (addr < 0 || addr >= p.nOp) return;
    z = p.aOp[addr].p3;
    i = j = 0;
    while (isspace(z.get(i))) {
      i++;
    }
    while (z.get(i) != 0) {
      if (isspace(z.get(i))) {
        z.set(j++, ' ');
        while (isspace(z.get(++i))) {
        }
      } else {
        z.set(j++, z.get(i++));
      }
    }
    while (i > 0 && isspace(z.get(i - 1))) {
      z.set(i - 1, 0);
      i--;
    }
  }

  /*
   ** Create a new symbolic label for an instruction that has yet to be
   ** coded.  The symbolic label is really just a negative number.  The
   ** label can be used as the P2 value of an operation.  Later, when
   ** the label is resolved to a specific address, the VDBE will scan
   ** through its operation list and change all values of P2 which match
   ** the label into the resolved address.
   **
   ** The VDBE knows that a P2 value is a label because labels are
   ** always negative and P2 values are suppose to be non-negative.
   ** Hence, a negative P2 value is a label that has yet to be resolved.
   */
  public static int sqliteVdbeMakeLabel(Vdbe p) {
    int i;
    i = p.nLabel++;
    if (i >= p.nLabelAlloc) {
      p.nLabelAlloc = p.nLabelAlloc * 2 + 10;
      p.aLabel = sqliteRealloc(p.aLabel, p.nLabelAlloc);
    }
    p.aLabel[i] = -1;
    return -1 - i;
  }

  /*
   ** Reset an Agg structure.  Delete all its contents.
   */
  public static void AggReset(Agg p) {
    int i;
    while (p.pFirst != null) {
      AggElem pElem = p.pFirst;
      p.pFirst = pElem.pNext;
      for (i = 0; i < p.nMem; i++) {
        if ((pElem.aMem[i].s.flags & STK_Dyn) != 0) {
//          sqliteFree(pElem.aMem[i].z);
        }
      }
//      sqliteFree(pElem);
    }
//    sqliteFree(p.apHash);
//    memset(p, 0, sizeof(*p));
  }

  /*
   ** Add the given AggElem to the hash array
   */
  public static void AggEnhash(Agg p, AggElem pElem) {
    int h = sqliteHashNoCase(pElem.zKey, 0) % p.nHash;
    pElem.pHash = p.apHash[h];
    p.apHash[h] = pElem;
  }

  /*
   ** Change the size of the hash array to the amount given.
   */
  public static void AggRehash(Agg p, int nHash) {
    int size;
    AggElem pElem;
    if (p.nHash == nHash) return;
//    size = nHash * sizeof(AggElem *);
    p.apHash = new AggElem[nHash];//sqliteRealloc(p.apHash, size);
//    memset(p.apHash, 0, size);
    p.nHash = nHash;
    for (pElem = p.pFirst; pElem != null; pElem = pElem.pNext) {
      AggEnhash(p, pElem);
    }
  }

  /*
   ** Insert a new element and make it the current element.
   **
   ** Return 0 on success and 1 if memory is exhausted.
   */
  public static int AggInsert(Agg p, CharPtr zKey) {
    AggElem pElem;
    int i;
    if (p.nHash <= p.nElem * 2) {
      AggRehash(p, p.nElem * 2 + 19);
    }
    if (p.nHash == 0) return 1;
    pElem = new AggElem();
//    pElem = sqliteMalloc(sizeof(AggElem) + strlen(zKey) + 1 +
//            (p.nMem - 1) * sizeof(pElem.aMem[0]));
    pElem.aMem = new Mem[p.nMem];
//    pElem.zKey = ( char*)&pElem.aMem[p.nMem];
    pElem.zKey = new CharPtr(zKey.strlen() + 1);// (char*)&pElem.aMem[p.nMem];
    pElem.zKey.strcpy(zKey);
    AggEnhash(p, pElem);
    pElem.pNext = p.pFirst;
    p.pFirst = pElem;
    p.nElem++;
    p.pCurrent = pElem;
    for (i = 0; i < p.nMem; i++) {
      pElem.aMem[i].s.flags = STK_Null;
    }
    return 0;
  }

  /*
   ** Get the AggElem currently in focus
   */

  /*
   ** Get the AggElem currently in focus
   */
//        #define AggInFocus(P)   ((P).pCurrent ? (P).pCurrent : _AggInFocus(&(P)))
  public static AggElem AggInFocus(Agg P) {
    return P.pCurrent != null ? P.pCurrent : _AggInFocus(P);
  }

  public static AggElem _AggInFocus(Agg p) {
    AggElem pFocus = p.pFirst;
    if (pFocus != null) {
      p.pCurrent = pFocus;
    } else {
      AggInsert(p, new CharPtr(""));
      pFocus = p.pCurrent = p.pFirst;
    }
    return pFocus;
  }

  /*
   ** Erase all information from a Set
   */
  public static void SetClear(Set p) {
    SetElem pElem, pNext;
    for (pElem = p.pAll; pElem != null; pElem = pNext) {
      pNext = pElem.pNext;
//      sqliteFree(pElem);
    }
//    memset(p, 0, sizeof( * p));
  }

  /*
   ** Insert a new element into the set
   */
  public static void SetInsert(Set p, CharPtr zKey) {
    SetElem pElem;
    int h = sqliteHashNoCase(zKey, 0) % (p.apHash.length);
    for (pElem = p.apHash[h]; pElem != null; pElem = pElem.pHash) {
      if (pElem.zKey.strcmp(zKey) == 0) return;
    }
    pElem = new SetElem();//sqliteMalloc(sizeof( * pElem) + strlen(zKey) );
    pElem.zKey = new CharPtr(zKey.strlen());
    pElem.zKey.strcpy(zKey);
//    strcpy(pElem.zKey, zKey);
    pElem.pNext = p.pAll;
    p.pAll = pElem;
    pElem.pHash = p.apHash[h];
    p.apHash[h] = pElem;
  }

  /*
   ** Return TRUE if an element is in the set.  Return FALSE if not.
   */
  public static int SetTest(Set p, CharPtr zKey) {
    SetElem pElem;
    int h = sqliteHashNoCase(zKey, 0) % (p.apHash.length);
    for (pElem = p.apHash[h]; pElem != null; pElem = pElem.pHash) {
      if ((pElem.zKey.strcmp(zKey)) == 0) return 1;
    }
    return 0;
  }

  /*
   ** Convert the given stack entity into a string if it isn't one
   ** already.  Return non-zero if we run out of memory.
   **
   ** NULLs are converted into an empty string.
   */
//#define Stringify(P,I) \
//          ((P.aStack[I].flags & STK_Str)==0 ? hardStringify(P,I) : 0)
  public static int Stringify(Vdbe p, int i) {
    if ((p.aStack[i].flags & STK_Str) == 0) {
      return hardStringify(p, i);
    } else {
      return 0;
    }
  }

  public static int hardStringify(Vdbe p, int i) {
    CharPtr zBuf = new CharPtr(30);
    int fg = p.aStack[i].flags;
    if ((fg & STK_Real) != 0) {
      zBuf.sprintf("%g", p.aStack[i].r);
    } else if ((fg & STK_Int) != 0) {
      zBuf.sprintf("%d", p.aStack[i].i);
    } else {
      p.zStack[i] = new CharPtr("");
      p.aStack[i].n = 1;
      p.aStack[i].flags |= STK_Str;
      return 0;
    }
    p.zStack[i] = sqliteStrDup(zBuf);
    if (p.zStack[i] == null) return 1;
    p.aStack[i].n = p.zStack[i].strlen() + 1;
    p.aStack[i].flags |= STK_Str | STK_Dyn;
    return 0;
  }

  /*
   ** Release the memory associated with the given stack level
   */
  public static void Release(Vdbe P, int I) {
    if (((P).aStack[I].flags & STK_Dyn) != 0) {
      hardRelease(P, I);
    }
  }

  public static void hardRelease(Vdbe p, int i) {
//    sqliteFree(p.zStack[i]);
    p.zStack[i] = null;
    p.aStack[i].flags &= ~(STK_Str | STK_Dyn);
  }


  /*
   ** Convert the given stack entity into a integer if it isn't one
   ** already.
   **
   ** Any prior string or real representation is invalidated.
   ** NULLs are converted into 0.
   */
  public static void Integerify(Vdbe P, int I) {
    if (((P).aStack[(I)].flags & STK_Int) == 0) {
      hardIntegerify(P, I);
    }
  }

  public static void hardIntegerify(Vdbe p, int i) {
    if ((p.aStack[i].flags & STK_Real) != 0) {
      p.aStack[i].i = (int) p.aStack[i].r;
      Release(p, i);
    } else if ((p.aStack[i].flags & STK_Str) != 0) {
      p.aStack[i].i = p.zStack[i].atoi();
      Release(p, i);
    } else {
      p.aStack[i].i = 0;
    }
    p.aStack[i].flags = STK_Int;
  }

  /*
   ** Get a valid Real representation for the given stack element.
   **
   ** Any prior string or integer representation is retained.
   ** NULLs are converted into 0.0.
   */
  public static void Realify(Vdbe P, int I) {
    if (((P).aStack[(I)].flags & STK_Real) == 0) {
      hardRealify(P, I);
    }
  }

  public static void hardRealify(Vdbe p, int i) {
    if ((p.aStack[i].flags & STK_Str) != 0) {
      p.aStack[i].r = p.zStack[i].atof();
    } else if ((p.aStack[i].flags & STK_Int) != 0) {
      p.aStack[i].r = p.aStack[i].i;
    } else {
      p.aStack[i].r = 0.0;
    }
    p.aStack[i].flags |= STK_Real;
  }

  /*
   ** Pop the stack N times.  Free any memory associated with the
   ** popped stack elements.
   */
  public static void PopStack(Vdbe p, int N) {
    if (p.zStack == null) return;
    while (p.tos >= 0 && N-- > 0) {
      int i = p.tos--;
      if ((p.aStack[i].flags & STK_Dyn) != 0) {
//        sqliteFree(p.zStack[i]);
      }
      p.aStack[i].flags = 0;
      p.zStack[i] = null;
    }
  }

  /*
   ** Make sure space has been allocated to hold at least N
   ** stack elements.  Allocate additional stack space if
   ** necessary.
   **
   ** Return 0 on success and non-zero if there are memory
   ** allocation errors.
   */

  public static int NeedStack(Vdbe P, int N) {
    return (((P).nStackAlloc <= (N)) ? hardNeedStack(P, N) : 0);
  }


  public static int hardNeedStack(Vdbe p, int N) {
    int oldAlloc;
    int i;
    if (N >= p.nStackAlloc) {
      oldAlloc = p.nStackAlloc;
      p.nStackAlloc = N + 20;
      p.aStack = sqliteRealloc(p.aStack, p.nStackAlloc);// * sizeof(p.aStack[0]));
      p.zStack = sqliteRealloc(p.zStack, p.nStackAlloc);// * sizeof( char*));
      for (i = oldAlloc; i < p.nStackAlloc; i++) {
        p.zStack[i] = null;
        p.aStack[i].flags = 0;
      }
    }
    return 0;
  }


  /*
   ** Clean up the VM after execution.
   **
   ** This routine will automatically close any cursors, list, and/or
   ** sorters that were left open.
   */
  public static void Cleanup(Vdbe p) {
    int i;
    PopStack(p, p.tos + 1);
//    sqliteFree(p.azColName);
    p.azColName = null;
    for (i = 0; i < p.nCursor; i++) {
      if (p.aCsr[i].pCursor != null) {
        sqliteDbbeCloseCursor(p.aCsr[i].pCursor);
        p.aCsr[i].pCursor = null;
      }
    }
//    sqliteFree(p.aCsr);
    p.aCsr = null;
    p.nCursor = 0;
    for (i = 0; i < p.nMem; i++) {
      if ((p.aMem[i].s.flags & STK_Dyn) != 0) {
//        sqliteFree(p.aMem[i].z);
      }
    }
//    sqliteFree(p.aMem);
    p.aMem = null;
    p.nMem = 0;
    for (i = 0; i < p.nList; i++) {
      if (p.apList[i] != null) {
        sqliteDbbeCloseTempFile(p.pBe, p.apList[i]);
        p.apList[i] = null;
      }
    }
//    sqliteFree(p.apList);
    p.apList = null;
    p.nList = 0;
    for (i = 0; i < p.nSort; i++) {
      Sorter pSorter;
      while ((pSorter = p.apSort[i]) != null) {
        p.apSort[i] = pSorter.pNext;
//        sqliteFree(pSorter.zKey);
//        sqliteFree(pSorter.pData);
//        sqliteFree(pSorter);
      }
    }
//    sqliteFree(p.apSort);
    p.apSort = null;
    p.nSort = 0;
    if (p.pFile != null) {
      if (p.pFile.isStdin()) p.pFile.close();
//      if (p.pFile != stdin) fclose(p.pFile);
      p.pFile = null;
    }
    if (p.azField != null) {
//      sqliteFree(p.azField);
      p.azField = null;
    }
    p.nField = 0;
    if (p.zLine == null) {
//      sqliteFree(p.zLine);
      p.zLine = null;
    }
    p.nLineAlloc = 0;
    AggReset(p.agg);
    for (i = 0; i < p.nSet; i++) {
      SetClear(p.aSet[i]);
    }
//    sqliteFree(p.aSet);
    p.aSet = null;
    p.nSet = 0;
  }

  /*
   ** Delete an entire VDBE.
   */
  public static void sqliteVdbeDelete(Vdbe p) {
    int i;
    if (p == null) return;
    Cleanup(p);
    if (p.nOpAlloc == 0) {
      p.aOp = null;
      p.nOp = 0;
    }
    for (i = 0; i < p.nOp; i++) {
//      sqliteFree(p.aOp[i].p3);
    }
//    sqliteFree(p.aOp);
//    sqliteFree(p.aLabel);
//    sqliteFree(p.aStack);
//    sqliteFree(p.zStack);
//    sqliteFree(p);
  }

  /*
   ** A translation from opcode numbers to opcode names.  Used for testing
   ** and debugging only.
   **
   ** If any of the numeric OP_ values for opcodes defined in sqliteVdbe.h
   ** change, be sure to change this array to match.  You can use the
   ** "opNames.awk" awk script which is part of the source tree to regenerate
   ** this array, then copy and paste it into this file, if you want.
   */
  public static String[] zOpName = {null,
          "Open", "Close", "Fetch", "Fcnt",
          "New", "Put", "Distinct", "Found",
          "NotFound", "Delete", "Field", "KeyAsData",
          "Key", "Rewind", "Next", "Destroy",
          "Reorganize", "ResetIdx", "NextIdx", "PutIdx",
          "DeleteIdx", "MemLoad", "MemStore", "ListOpen",
          "ListWrite", "ListRewind", "ListRead", "ListClose",
          "SortOpen", "SortPut", "SortMakeRec", "SortMakeKey",
          "Sort", "SortNext", "SortKey", "SortCallback",
          "SortClose", "FileOpen", "FileRead", "FileField",
          "FileClose", "AggReset", "AggFocus", "AggIncr",
          "AggNext", "AggSet", "AggGet", "SetInsert",
          "SetFound", "SetNotFound", "SetClear", "MakeRecord",
          "MakeKey", "Goto", "If", "Halt",
          "ColumnCount", "ColumnName", "Callback", "Integer",
          "String", "Null", "Pop", "Dup",
          "Pull", "Add", "AddImm", "Subtract",
          "Multiply", "Divide", "Min", "Max",
          "Like", "Glob", "Eq", "Ne",
          "Lt", "Le", "Gt", "Ge",
          "IsNull", "NotNull", "Negative", "And",
          "Or", "Not", "Concat", "Noop",
  };

  /*
   ** Given the name of an opcode, return its number.  Return 0 if
   ** there is no match.
   **
   ** This routine is used for testing and debugging.
   */
  public static int sqliteVdbeOpcode(CharPtr zName) {
    int i;
    for (i = 1; i <= OP_MAX; i++) {
      if (sqliteStrICmp(zName, new CharPtr(zOpName[i])) == 0) return i;
    }
    return 0;
  }

  /*
   ** Give a listing of the program in the virtual machine.
   **
   ** The interface is the same as sqliteVdbeExec().  But instead of
   ** running the code, it invokes the callback once for each instruction.
   ** This feature is used to implement "EXPLAIN".
   */
  public static int sqliteVdbeList(
          Vdbe p,                   /* The VDBE */
          sqlite_callback xCallback, /* The callback */
          Object pArg,                /* 1st argument to callback */
          CharPtr pzErrMsg            /* Error msg written here */
  ) {
    int i, rc;
    CharPtr[] azValue = new CharPtr[6];
    CharPtr zAddr = new CharPtr(20);
    CharPtr zP1 = new CharPtr(20);
    CharPtr zP2 = new CharPtr(20);
    CharPtr[] azColumnNames = Arrays.stream(new String[]{
            "addr", "opcode", "p1", "p2", "p3", null
    }).map(x -> {
      if (x == null) {
        return null;
      } else {
        return new CharPtr(x);
      }
    }).toArray(value -> new CharPtr[0]);

    if (xCallback == null) return 0;
    azValue[0] = zAddr;
    azValue[2] = zP1;
    azValue[3] = zP2;
    azValue[5] = null;
    rc = SQLITE_OK;
    /* if( pzErrMsg ){ *pzErrMsg = 0; } */
    for (i = 0; rc == SQLITE_OK && i < p.nOp; i++) {
      zAddr.sprintf("%d", i);
      zP1.sprintf("%d", p.aOp[i].p1);
      zP2.sprintf("%d", p.aOp[i].p2);
      azValue[4] = p.aOp[i].p3;
      azValue[1] = new CharPtr(zOpName[p.aOp[i].opcode]);
      if (xCallback.call(pArg, 5, azValue, azColumnNames) != 0) {
        rc = SQLITE_ABORT;
      }
    }
    return rc;
  }

  /*
   ** The parameters are pointers to the head of two sorted lists
   ** of Sorter structures.  Merge these two lists together and return
   ** a single sorted list.  This routine forms the core of the merge-sort
   ** algorithm.
   **
   ** In the case of a tie, left sorts in front of right.
   */
  public static Sorter Merge(Sorter pLeft, Sorter pRight) {
    Sorter sHead = new Sorter();
    Sorter pTail;
    pTail = sHead;
    pTail.pNext = null;
    while (pLeft != null && pRight != null) {
      int c = sqliteSortCompare(pLeft.zKey, pRight.zKey);
      if (c <= 0) {
        pTail.pNext = pLeft;
        pLeft = pLeft.pNext;
      } else {
        pTail.pNext = pRight;
        pRight = pRight.pNext;
      }
      pTail = pTail.pNext;
    }
    if (pLeft != null) {
      pTail.pNext = pLeft;
    } else if (pRight != null) {
      pTail.pNext = pRight;
    }
    return sHead.pNext;
  }

  public static int sqliteVdbeExec(
          Vdbe p,                   /* The VDBE */
          sqlite_callback xCallback, /* The callback */
          Object pArg,                /* 1st argument to callback */
          CharPtr pzErrMsg,           /* Error msg written here */
          Object pBusyArg,            /* 1st argument to the busy callback */
          xBusyCallback xBusy
//          int (*xBusy)(void*,const char*,int)  /* Called when a file is busy */
  ) {
    return 0;
  }
}
