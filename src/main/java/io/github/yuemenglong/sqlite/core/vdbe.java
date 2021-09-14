package io.github.yuemenglong.sqlite.core;

import io.github.yuemenglong.sqlite.common.Addr;
import io.github.yuemenglong.sqlite.common.CharPtr;
import io.github.yuemenglong.sqlite.common.FILE;
import io.github.yuemenglong.sqlite.core.sqliteint.*;
import io.github.yuemenglong.sqlite.core.dbbe.*;

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
    CharPtr p3;           /* Third parameter */
  }

  ;

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

  ;

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

  ;
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

  ;

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

  ;
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
    Mem[] aMem = new Mem[1];           /* The values for this AggElem */
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
    char[] zKey = new char[1];          /* Value of this key */
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
    Op aOp;            /* Space to hold the virtual machine's program */
    int nLabel;         /* Number of labels used */
    int nLabelAlloc;    /* Number of slots allocated in aLabel[] */
    Addr<Integer> aLabel; /* Space to hold the labels */
    int tos;            /* Index of top of stack */
    int nStackAlloc;    /* Size of the stack */
    Stack aStack;      /* The operand stack, except string values */
    CharPtr zStack;      /* Text or binary values of the stack */
    CharPtr azColName;   /* Becomes the 4th parameter to callbacks */
    int nCursor;        /* Number of slots in aCsr[] */
    Cursor aCsr;       /* On element of this array for each open cursor */
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
    Mem aMem;          /* The memory locations */
    Agg agg;            /* Aggregate information */
    int nSet;           /* Number of sets allocated */
    Set aSet;          /* An array of sets */
    int nFetch;         /* Number of OP_Fetch instructions executed */
  }

  ;
}
