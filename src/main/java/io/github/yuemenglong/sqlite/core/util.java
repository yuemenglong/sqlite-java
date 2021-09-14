package io.github.yuemenglong.sqlite.core;

import io.github.yuemenglong.sqlite.common.Addr;
import io.github.yuemenglong.sqlite.common.CharPtr;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

import static io.github.yuemenglong.sqlite.common.Util.isdigit;

public class util {

  CharPtr sqliteMalloc(int n) {
    return new CharPtr(n);
  }

  /*
   ** Free memory previously obtained from sqliteMalloc()
   */
  void sqliteFree(CharPtr p) {
  }

  /*
   ** Resize a prior allocation.  If p==0, then this routine
   ** works just like sqliteMalloc().  If n==0, then this routine
   ** works just like sqliteFree().
   */
  CharPtr sqliteRealloc(CharPtr p, int n) {
    if (p == null) {
      return new CharPtr(n);
    }
    if (n == 0) {
      return null;
    }
    CharPtr ret = new CharPtr(n);
    ret.memcpy(p, n);
    return ret;
  }

  public static CharPtr sqliteStrDup(CharPtr z) {
    z = z.dup();
    CharPtr zNew = new CharPtr(z.strlen() + 1);
    zNew.strcpy(z);
    return zNew;
  }

  public static CharPtr sqliteStrNDup(CharPtr z, int n) {
    z = z.dup();
    CharPtr zNew = new CharPtr(n + 1);
    zNew.memcpy(z, n);
    zNew.ptr(n).set(0);
    return zNew;
  }

  ///*
//** Create a string from the 2nd and subsequent arguments (up to the
//** first NULL argument), store the string in memory obtained from
//** sqliteMalloc() and make the pointer indicated by the 1st argument
//** point to that string.
//*/
  public static void sqliteSetString(Addr<CharPtr> pz, CharPtr zFirst, CharPtr... args) {
    int nByte;
    CharPtr zResult;

    if (pz == null) return;
    nByte = zFirst.strlen() + 1;
    for (CharPtr arg : args) {
      nByte += arg.strlen();
    }
    zResult = new CharPtr(nByte);
    pz.set(zResult.dup());
    zResult.strcpy(zFirst);
    zResult.move(zResult.strlen());
    for (CharPtr arg : args) {
      zResult.strcpy(arg);
      zResult.move(arg.strlen());
    }
  }

  /*
   ** Works like sqliteSetString, but each string is now followed by
   ** a length integer.  -1 means use the whole string.
   */
  public static void sqliteSetNString(Addr<CharPtr> pz, Object... args) {
    if (args.length > 1 && args[args.length - 1] instanceof Integer && ((int) args[args.length - 1] == 0)) {
      args = Arrays.copyOf(args, args.length - 1);
    }
    int nByte;
    CharPtr z;
    CharPtr zResult;
    int n;

    if (pz == null) return;
    nByte = 0;
    for (int i = 1; i < args.length; i += 2) {
      n = (int) args[i];
      if (n <= 0) n = ((CharPtr) args[i - 1]).strlen();
      nByte += n;
    }
    zResult = new CharPtr(nByte + 1);
    pz.set(zResult.dup());
    for (int i = 1; i < args.length; i += 2) {
      z = (CharPtr) args[i - 1];
      n = (int) args[i];
      if (n <= 0) n = z.strlen();
      zResult.strncpy(z, n);
      zResult.move(n);
    }
  }

  ///*
//** Convert an SQL-style quoted string into a normal string by removing
//** the quote characters.  The conversion is done in-place.  If the
//** input does not begin with a quote character, then this routine
//** is a no-op.
//*/
  public static void sqliteDequote(CharPtr z) {
    z = z.dup();
    int quote;
    int i, j;
    quote = z.get();
    if (quote != '\'' && quote != '"') return;
    for (i = 1, j = 0; z.get(i) != 0; i++) {
      if (z.get(i) == quote) {
        if (z.get(i + 1) == quote) {
          z.set(j++, quote);
          i++;
        } else {
          z.set(j++, 0);
          break;
        }
      } else {
        z.set(j++, z.get(i));
      }
    }
  }

  ///* An array to map all upper-case characters into their corresponding
//** lower-case character.
//*/
  public static char[] UpperToLower = {
          0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17,
          18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
          36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53,
          54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 97, 98, 99, 100, 101, 102, 103,
          104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121,
          122, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107,
          108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125,
          126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143,
          144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161,
          162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176, 177, 178, 179,
          180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190, 191, 192, 193, 194, 195, 196, 197,
          198, 199, 200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215,
          216, 217, 218, 219, 220, 221, 222, 223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233,
          234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251,
          252, 253, 254, 255
  };

  //
///*
//** This function computes a hash on the name of a keyword.
//** Case is not significant.
//*/
  public static int sqliteHashNoCase(CharPtr z, int n) {
    z = z.dup();
    int h = 0;
    int c;
    if (n <= 0) n = z.strlen();
    while (n-- > 0 && (c = z.cpp()) != 0) {
      h = h << 3 ^ h ^ UpperToLower[c];
    }
    if (h < 0) h = -h;
    return h;
  }

  //
///*
//** Some systems have stricmp().  Others have strcasecmp().  Because
//** there is no consistency, we will define our own.
//*/
  public static int sqliteStrICmp(CharPtr zLeft, CharPtr zRight) {
    CharPtr a = zLeft.dup();
    CharPtr b = zRight.dup();
    while (a.get() != 0 && UpperToLower[a.get()] == UpperToLower[b.get()]) {
      a.cpp();
      b.cpp();
    }
    return a.get() - b.get();
  }

  public static int sqliteStrNICmp(CharPtr zLeft, CharPtr zRight, int N) {
    CharPtr a = zLeft.dup();
    CharPtr b = zRight.dup();
    while (N-- > 0 && a.get() != 0 && UpperToLower[a.get()] == UpperToLower[b.get()]) {
      a.cpp();
      b.cpp();
    }
    return N < 0 ? 0 : a.get() - b.get();
  }

  //
///* Notes on string comparisions.
//**
//** We want the main string comparision function used for sorting to
//** sort both numbers and alphanumeric words into the correct sequence.
//** The same routine should do both without prior knowledge of which
//** type of text the input represents.  It should even work for strings
//** which are a mixture of text and numbers.
//**
//** To accomplish this, we keep track of a state number while scanning
//** the two strings.  The states are as follows:
//**
//**    1      Beginning of word
//**    2      Arbitrary text
//**    3      Integer
//**    4      Negative integer
//**    5      Real number
//**    6      Negative real
//**
//** The scan begins in state 1, beginning of word.  Transitions to other
//** states are determined by characters seen, as shown in the following
//** chart:
//**
//**      Current State         Character Seen  New State
//**      --------------------  --------------  -------------------
//**      0 Beginning of word   "-"             3 Negative integer
//**                            digit           2 Integer
//**                            space           0 Beginning of word
//**                            otherwise       1 Arbitrary text
//**
//**      1 Arbitrary text      space           0 Beginning of word
//**                            digit           2 Integer
//**                            otherwise       1 Arbitrary text
//**
//**      2 Integer             space           0 Beginning of word
//**                            "."             4 Real number
//**                            digit           2 Integer
//**                            otherwise       1 Arbitrary text
//**
//**      3 Negative integer    space           0 Beginning of word
//**                            "."             5 Negative Real num
//**                            digit           3 Negative integer
//**                            otherwise       1 Arbitrary text
//**
//**      4 Real number         space           0 Beginning of word
//**                            digit           4 Real number
//**                            otherwise       1 Arbitrary text
//**
//**      5 Negative real num   space           0 Beginning of word
//**                            digit           5 Negative real num
//**                            otherwise       1 Arbitrary text
//**
//** To implement this state machine, we first classify each character
//** into on of the following categories:
//**
//**      0  Text
//**      1  Space
//**      2  Digit
//**      3  "-"
//**      4  "."
//**
//** Given an arbitrary character, the array charClass[] maps that character
//** into one of the atove categories.
//*/
  public static char[] charClass = {
          /* x0 x1 x2 x3 x4 x5 x6 x7 x8 x9 xA xB xC xD xE xF */
/* 0x */   0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0,
/* 1x */   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/* 2x */   1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 4, 0,
/* 3x */   2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0,
/* 4x */   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/* 5x */   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/* 6x */   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/* 7x */   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/* 8x */   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/* 9x */   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/* Ax */   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/* Bx */   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/* Cx */   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/* Dx */   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/* Ex */   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
/* Fx */   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
  };
  public static final int N_CHAR_CLASS = 5;
  //
///*
//** Given the current state number (0 thru 5), this array figures
//** the new state number given the character class.
//*/
  public static final char[] stateMachine = {
          /* Text,  Space, Digit, "-", "." */
          1, 0, 2, 3, 1,      /* State 0: Beginning of word */
          1, 0, 2, 1, 1,      /* State 1: Arbitrary text */
          1, 0, 2, 1, 4,      /* State 2: Integer */
          1, 0, 3, 1, 5,      /* State 3: Negative integer */
          1, 0, 4, 1, 1,      /* State 4: Real number */
          1, 0, 5, 1, 1,      /* State 5: Negative real num */
  };

  //
///* This routine does a comparison of two strings.  Case is used only
//** if useCase!=0.  Numbers compare in numerical order.
//*/
  public static int privateStrCmp(CharPtr atext, CharPtr btext, int useCase) {
    int result = 0;
    int cclass = 0;
    CharPtr a = atext.dup();
    CharPtr b = btext.dup();
    int ca;
    int cb;

    if (useCase != 0) {
      do {
        if ((ca = a.cpp()) != (cb = b.cpp())) break;
        cclass = stateMachine[cclass * N_CHAR_CLASS + charClass[ca]];
      } while (ca != 0);
    } else {
      char[] map = UpperToLower;
      do {
        if ((ca = map[a.cpp()]) != (cb = map[b.cpp()])) break;
        cclass = stateMachine[cclass * N_CHAR_CLASS + charClass[ca]];
      } while (ca != 0);
    }
    switch (cclass) {
      case 0:
      case 1: {
        if (isdigit(ca) && isdigit(cb)) {
          cclass = 2;
        }
        break;
      }
      default: {
        break;
      }
    }
    switch (cclass) {
      case 2:
      case 3: {
        if (isdigit(ca)) {
          if (isdigit(cb)) {
            int acnt, bcnt;
            acnt = bcnt = 0;
            while (isdigit(a.cpp())) acnt++;
            while (isdigit(b.cpp())) bcnt++;
            result = acnt - bcnt;
            if (result == 0) result = ca - cb;
          } else {
            result = 1;
          }
        } else if (isdigit(cb)) {
          result = -1;
        } else if (ca == '.') {
          result = 1;
        } else if (cb == '.') {
          result = -1;
        } else {
          result = ca - cb;
          cclass = 2;
        }
        if (cclass == 3) result = -result;
        break;
      }
      case 0:
      case 1:
      case 4: {
        result = ca - cb;
        break;
      }
      case 5: {
        result = cb - ca;
      }
      ;
    }
    return result;
  }

  //
///* This comparison routine is what we use for comparison operations
//** in an SQL expression.  (Ex:  name<'Hello' or value<5).  Compare two
//** strings.  Use case only as a tie-breaker.  Numbers compare in
//** numerical order.
//*/
  public static int sqliteCompare(CharPtr atext, CharPtr btext) {
    int result;
    result = privateStrCmp(atext, btext, 0);
    if (result == 0) result = privateStrCmp(atext, btext, 1);
    return result;
  }

  ///*
//** This routine is used for sorting.  Each key is a list of one or more
//** null-terminated strings.  The list is terminated by two nulls in
//** a row.  For example, the following text is key with three strings:
//**
//**            +one\000-two\000+three\000\000
//**
//** Both arguments will have the same number of strings.  This routine
//** returns negative, zero, or positive if the first argument is less
//** than, equal to, or greater than the first.  (Result is a-b).
//**
//** Every string begins with either a "+" or "-" character.  If the
//** character is "-" then the return value is negated.  This is done
//** to implement a sort in descending order.
//*/
  public static int sqliteSortCompare(CharPtr a, CharPtr b) {
    a = a.dup();
    b = b.dup();
    int len;
    int res = 0;

    while (res == 0 && a.get() != 0 && b.get() != 0) {
      res = sqliteCompare(a.ptr(1), b.ptr(1));
      if (res == 0) {
        len = a.strlen() + 1;
        a.move(len);
        b.move(len);
      }
    }
    if (a.get() == '-') res = -res;
    return res;
  }

  //
///*
//** Compare two strings for equality where the first string can
//** potentially be a "glob" expression.  Return true (1) if they
//** are the same and false (0) if they are different.
//**
//** Globbing rules:
//**
//**      '*'       Matches any sequence of zero or more characters.
//**
//**      '?'       Matches exactly one character.
//**
//**     [...]      Matches one character from the enclosed list of
//**                characters.
//**
//**     [^...]     Matches one character not in the enclosed list.
//**
//** With the [...] and [^...] matching, a ']' character can be included
//** in the list by making it the first character after '[' or '^'.  A
//** range of characters can be specified using '-'.  Example:
//** "[a-z]" matches any single lower-case letter.  To match a '-', make
//** it the last character in the list.
//**
//** This routine is usually quick, but can be N**2 in the worst case.
//**
//** Hints: to match '*' or '?', put them in "[]".  Like this:
//**
//**         abc[*]xyz        Matches "abc*xyz" only
//*/
  public static int sqliteGlobCompare(CharPtr zPattern, CharPtr zCharPtr) {
    zPattern = zPattern.dup();
    zCharPtr = zCharPtr.dup();
    char c;
    int invert;
    int seen;
    char c2;

    while ((c = zPattern.get()) != 0) {
      switch (c) {
        case '*':
          while (zPattern.get(1) == '*') zPattern.cpp();
          if (zPattern.get(1) == 0) return 1;
          c = zPattern.get(1);
          if (c == '[' || c == '?') {
            while (zCharPtr.get() != 0 && sqliteGlobCompare(zPattern.ptr(1), zCharPtr) == 0) {
              zCharPtr.cpp();
            }
            return zCharPtr.get() != 0 ? 1 : 0;
          } else {
            while ((c2 = zCharPtr.get()) != 0) {
              while (c2 != 0 && c2 != c) {
                c2 = zCharPtr.ppc();
              }
              if (c2 == 0) return 0;
              if (sqliteGlobCompare(zPattern.ptr(1), zCharPtr) != 0) return 1;
              zCharPtr.cpp();
            }
            return 0;
          }
        case '?':
          if (zCharPtr.get() == 0) return 0;
          break;
        case '[':
          seen = 0;
          invert = 0;
          c = zCharPtr.get();
          if (c == 0) return 0;
          c2 = zPattern.ppc();
          if (c2 == '^') {
            invert = 1;
            c2 = zPattern.ppc();
          }
          if (c2 == ']') {
            if (c == ']') seen = 1;
            c2 = zPattern.ppc();
          }
          while ((c2 = zPattern.get()) != 0 && c2 != ']') {
            if (c2 == '-' && zPattern.get(1) != ']' && zPattern.get(1) != 0) {
              if (c > zPattern.get(-1) && c < zPattern.get(1)) seen = 1;
            } else if (c == c2) {
              seen = 1;
            }
            zPattern.cpp();
          }
          if (c2 == 0 || (seen ^ invert) == 0) return 0;
          break;
        default:
          if (c != zCharPtr.get()) return 0;
          break;
      }
      zPattern.cpp();
      zCharPtr.cpp();
    }
    return zCharPtr.get() == 0 ? 1 : 0;
  }

  //
///*
//** Compare two strings for equality using the "LIKE" operator of
//** SQL.  The '%' character matches any sequence of 0 or more
//** characters and '_' matches any single character.  Case is
//** not significant.
//**
//** This routine is just an adaptation of the sqliteGlobCompare()
//** routine above.
//*/
  public static int sqliteLikeCompare(CharPtr zPattern, CharPtr zCharPtr) {
    zPattern = zPattern.dup();
    zCharPtr = zCharPtr.dup();
    char c;
    char c2;

    while ((c = UpperToLower[zPattern.get()]) != 0) {
      switch (c) {
        case '%':
          while (zPattern.get(1) == '%') zPattern.cpp();
          if (zPattern.get(1) == 0) return 1;
          c = UpperToLower[0xff & zPattern.get(1)];
          if (c == '_') {
            while (zCharPtr.get() != 0 && sqliteLikeCompare(zPattern.ptr(1), zCharPtr) == 0) {
              zCharPtr.cpp();
            }
            return zCharPtr.get() != 0 ? 1 : 0;
          } else {
            while ((c2 = UpperToLower[zCharPtr.get()]) != 0) {
              while (c2 != 0 && c2 != c) {
                c2 = UpperToLower[zCharPtr.ppc()];
              }
              if (c2 == 0) return 0;
              if (sqliteLikeCompare(zPattern.ptr(1), zCharPtr) != 0) return 1;
              zCharPtr.cpp();
            }
            return 0;
          }
        case '_':
          if (zCharPtr.get() == 0) return 0;
          break;
        default:
          if (c != UpperToLower[zCharPtr.get()]) return 0;
          break;
      }
      zPattern.cpp();
      zCharPtr.cpp();
    }
    return zCharPtr.get() == 0 ? 1 : 0;
  }

  public static void main(String[] args) {
    CharPtr a = new CharPtr("ASDF");
    CharPtr b = new CharPtr("asdf");
    int ret = sqliteCompare(a, b);
    System.out.println(ret);
    System.out.println(a);
    System.out.println(b);
//    AtomicReference<CharPtr> p = new AtomicReference<>(new CharPtr("asdf"));
//    sqliteSetNString(new Addr<>(p::get, p::set),
//            new CharPtr("1first"), 2,
//            new CharPtr("2second"), 3,
//            new CharPtr("3third"), 4);
//    System.out.println(p.get());
  }
}
