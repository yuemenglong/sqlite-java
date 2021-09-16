package io.github.yuemenglong.sqlite.core;

import io.github.yuemenglong.sqlite.common.Addr;
import io.github.yuemenglong.sqlite.common.CharPtr;
import io.github.yuemenglong.sqlite.common.FILE;

import java.util.concurrent.atomic.AtomicInteger;

import static io.github.yuemenglong.sqlite.common.Util.*;
import static io.github.yuemenglong.sqlite.core.build.*;
import static io.github.yuemenglong.sqlite.core.parse.*;
import static io.github.yuemenglong.sqlite.core.select.sqliteParseInfoReset;
import static io.github.yuemenglong.sqlite.core.vdbe.*;
import static io.github.yuemenglong.sqlite.core.sqliteint.*;
import static io.github.yuemenglong.sqlite.core.util.*;

public class tokenize {
  /*
   ** All the keywords of the SQL language are stored as in a hash
   ** table composed of instances of the following structure.
   */
  public static class Keyword {
    public CharPtr zName;             /* The keyword name */
    public int len;                 /* Number of characters in the keyword */
    public int tokenType;           /* The token value for this keyword */
    public Keyword pNext;          /* Next keyword with the same hash */

    public Keyword(String zName, int len, int tokenType, int n) {
      this.zName = new CharPtr(zName);
      this.len = len;
      this.tokenType = tokenType;
    }
  }

  /*
   ** These are the keywords
   */
  public static Keyword[] aKeywordTable = {
          new Keyword("ALL", 0, TK_ALL, 0),
          new Keyword("AND", 0, TK_AND, 0),
          new Keyword("AS", 0, TK_AS, 0),
          new Keyword("ASC", 0, TK_ASC, 0),
          new Keyword("BETWEEN", 0, TK_BETWEEN, 0),
          new Keyword("BY", 0, TK_BY, 0),
          new Keyword("CHECK", 0, TK_CHECK, 0),
          new Keyword("CONSTRAINT", 0, TK_CONSTRAINT, 0),
          new Keyword("COPY", 0, TK_COPY, 0),
          new Keyword("CREATE", 0, TK_CREATE, 0),
          new Keyword("DEFAULT", 0, TK_DEFAULT, 0),
          new Keyword("DELETE", 0, TK_DELETE, 0),
          new Keyword("DELIMITERS", 0, TK_DELIMITERS, 0),
          new Keyword("DESC", 0, TK_DESC, 0),
          new Keyword("DISTINCT", 0, TK_DISTINCT, 0),
          new Keyword("DROP", 0, TK_DROP, 0),
          new Keyword("EXCEPT", 0, TK_EXCEPT, 0),
          new Keyword("EXPLAIN", 0, TK_EXPLAIN, 0),
          new Keyword("FROM", 0, TK_FROM, 0),
          new Keyword("GLOB", 0, TK_GLOB, 0),
          new Keyword("GROUP", 0, TK_GROUP, 0),
          new Keyword("HAVING", 0, TK_HAVING, 0),
          new Keyword("IN", 0, TK_IN, 0),
          new Keyword("INDEX", 0, TK_INDEX, 0),
          new Keyword("INSERT", 0, TK_INSERT, 0),
          new Keyword("INTERSECT", 0, TK_INTERSECT, 0),
          new Keyword("INTO", 0, TK_INTO, 0),
          new Keyword("IS", 0, TK_IS, 0),
          new Keyword("ISNULL", 0, TK_ISNULL, 0),
          new Keyword("KEY", 0, TK_KEY, 0),
          new Keyword("LIKE", 0, TK_LIKE, 0),
          new Keyword("NOT", 0, TK_NOT, 0),
          new Keyword("NOTNULL", 0, TK_NOTNULL, 0),
          new Keyword("NULL", 0, TK_NULL, 0),
          new Keyword("ON", 0, TK_ON, 0),
          new Keyword("OR", 0, TK_OR, 0),
          new Keyword("ORDER", 0, TK_ORDER, 0),
          new Keyword("PRIMARY", 0, TK_PRIMARY, 0),
          new Keyword("SELECT", 0, TK_SELECT, 0),
          new Keyword("SET", 0, TK_SET, 0),
          new Keyword("TABLE", 0, TK_TABLE, 0),
          new Keyword("UNION", 0, TK_UNION, 0),
          new Keyword("UNIQUE", 0, TK_UNIQUE, 0),
          new Keyword("UPDATE", 0, TK_UPDATE, 0),
          new Keyword("USING", 0, TK_USING, 0),
          new Keyword("VACUUM", 0, TK_VACUUM, 0),
          new Keyword("VALUES", 0, TK_VALUES, 0),
          new Keyword("WHERE", 0, TK_WHERE, 0),
  };

  /*
   ** This is the hash table
   */
  public static final int KEY_HASH_SIZE = 37;
  public static Keyword[] apHashTable = new Keyword[KEY_HASH_SIZE];


  /*
   ** This function looks up an identifier to determine if it is a
   ** keyword.  If it is a keyword, the token code of that keyword is
   ** returned.  If the input is not a keyword, TK_ID is returned.
   */
  public static int sqliteKeywordCode(CharPtr z, int n) {
    int h;
    Keyword p;
    if (aKeywordTable[0].len == 0) {
      /* Initialize the keyword hash table */
      int i;
      n = aKeywordTable.length;
      for (i = 0; i < n; i++) {
        aKeywordTable[i].len = (aKeywordTable[i].zName.strlen());
        h = sqliteHashNoCase(aKeywordTable[i].zName, aKeywordTable[i].len);
        h %= KEY_HASH_SIZE;
        aKeywordTable[i].pNext = apHashTable[h];
        apHashTable[h] = aKeywordTable[i];
      }
    }
    h = sqliteHashNoCase(z, n) % KEY_HASH_SIZE;
    for (p = apHashTable[h]; p != null; p = p.pNext) {
      if (p.len == n && sqliteStrNICmp(p.zName, z, n) == 0) {
        return p.tokenType;
      }
    }
    return TK_ID;
  }

  /*
   ** Return the length of the token that begins at z.get(0).  Return
   ** -1 if the token is (or might be) incomplete.  Store the token
   ** type in *tokenType before returning.
   */
  public static int sqliteGetToken(CharPtr z, Addr<Integer> tokenType) {
    int i;
    switch (z.get()) {
      case ' ':
      case '\t':
      case '\n':
      case '\f':
      case '\r': {
        for (i = 1; z.get(i) != 0 && isspace(z.get(i)); i++) {
        }
        tokenType.set(TK_SPACE);
        return i;
      }
      case '-': {
        if (z.get(1) == 0) return -1;
        if (z.get(1) == '-') {
          for (i = 2; z.get(i) != 0 && z.get(i) != '\n'; i++) {
          }
          tokenType.set(TK_COMMENT);
          return i;
        }
        tokenType.set(TK_MINUS);
        return 1;
      }
      case '(': {
        tokenType.set(TK_LP);
        return 1;
      }
      case ')': {
        tokenType.set(TK_RP);
        return 1;
      }
      case ';': {
        tokenType.set(TK_SEMI);
        return 1;
      }
      case '+': {
        tokenType.set(TK_PLUS);
        return 1;
      }
      case '*': {
        tokenType.set(TK_STAR);
        return 1;
      }
      case '/': {
        tokenType.set(TK_SLASH);
        return 1;
      }
      case '=': {
        tokenType.set(TK_EQ);
        return 1 + (z.get(1) == '=' ? 1 : 0);
      }
      case '<': {
        if (z.get(1) == '=') {
          tokenType.set(TK_LE);
          return 2;
        } else if (z.get(1) == '>') {
          tokenType.set(TK_NE);
          return 2;
        } else {
          tokenType.set(TK_LT);
          return 1;
        }
      }
      case '>': {
        if (z.get(1) == '=') {
          tokenType.set(TK_GE);
          return 2;
        } else {
          tokenType.set(TK_GT);
          return 1;
        }
      }
      case '!': {
        if (z.get(1) != '=') {
          tokenType.set(TK_ILLEGAL);
          return 2;
        } else {
          tokenType.set(TK_NE);
          return 2;
        }
      }
      case '|': {
        if (z.get(1) != '|') {
          tokenType.set(TK_ILLEGAL);
          return 1;
        } else {
          tokenType.set(TK_CONCAT);
          return 2;
        }
      }
      case ',': {
        tokenType.set(TK_COMMA);
        return 1;
      }
      case '\'':
      case '"': {
        int delim = z.get(0);
        for (i = 1; z.get(i) != 0; i++) {
          if (z.get(i) == delim) {
            if (z.get(i + 1) == delim) {
              i++;
            } else {
              break;
            }
          }
        }
        if (z.get(i) != 0) i++;
        tokenType.set(TK_STRING);
        return i;
      }
      case '.': {
        if (!isdigit(z.get(1))) {
          tokenType.set(TK_DOT);
          return 1;
        }
        /* Fall thru into the next case */
      }
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9': {
        tokenType.set(TK_INTEGER);
        for (i = 1; z.get(i) != 0 && isdigit(z.get(i)); i++) {
        }
        if (z.get(i) == '.') {
          i++;
          while (z.get(i) != 0 && isdigit(z.get(i))) {
            i++;
          }
          tokenType.set(TK_FLOAT);
        }
        if ((z.get(i) == 'e' || z.get(i) == 'E') &&
                (isdigit(z.get(i + 1))
                        || ((z.get(i + 1) == '+' || z.get(i + 1) == '-') && isdigit(z.get(i + 2)))
                )
        ) {
          i += 2;
          while (z.get(i) != 0 && isdigit(z.get(i))) {
            i++;
          }
          tokenType.set(TK_FLOAT);
        } else if (z.get(0) == '.') {
          tokenType.set(TK_FLOAT);
        }
        return i;
      }
      case 'a':
      case 'b':
      case 'c':
      case 'd':
      case 'e':
      case 'f':
      case 'g':
      case 'h':
      case 'i':
      case 'j':
      case 'k':
      case 'l':
      case 'm':
      case 'n':
      case 'o':
      case 'p':
      case 'q':
      case 'r':
      case 's':
      case 't':
      case 'u':
      case 'v':
      case 'w':
      case 'x':
      case 'y':
      case 'z':
      case '_':
      case 'A':
      case 'B':
      case 'C':
      case 'D':
      case 'E':
      case 'F':
      case 'G':
      case 'H':
      case 'I':
      case 'J':
      case 'K':
      case 'L':
      case 'M':
      case 'N':
      case 'O':
      case 'P':
      case 'Q':
      case 'R':
      case 'S':
      case 'T':
      case 'U':
      case 'V':
      case 'W':
      case 'X':
      case 'Y':
      case 'Z': {
        for (i = 1; z.get(i) != 0 && (isalnum(z.get(i)) || z.get(i) == '_'); i++) {
        }
        tokenType.set(sqliteKeywordCode(z, i));
        return i;
      }
      default: {
        break;
      }
    }
    tokenType.set(TK_ILLEGAL);
    return 1;
  }

  /*
   ** Run the parser on the given SQL string.  The parser structure is
   ** passed in.  Return the number of errors.
   */
  public static int sqliteRunParser(Parse pParse, CharPtr zSql, CharPtr pzErrMsg) {
    int nErr = 0;
    int i;
    Object pEngine;
    int once = 1;
    FILE trace = null;
//    extern void *sqliteParserAlloc(void*(*)(int));
//    extern void sqliteParserFree(void*, void(*)(void*));
//    extern int sqliteParser(void*, int, ...);
//    extern void sqliteParserTrace(FILE*, CharPtr );

    i = 0;
    sqliteParseInfoReset(pParse);
    pEngine = new Object();//sqliteParserAlloc((void*(*)(int))malloc);
    if (pEngine == null) {
      sqliteSetString(pzErrMsg, "out of memory", 0);
      return 1;
    }
//    sqliteParserTrace(trace, "parser: ");
    while (nErr == 0 && i >= 0 && zSql.get(i) != 0) {
      AtomicInteger tokenType = new AtomicInteger();

      pParse.sLastToken.z = zSql.ptr(i);
      pParse.sLastToken.n = sqliteGetToken(zSql.ptr(i), new Addr<>(
              tokenType::get,
              tokenType::set
      ));
      i += pParse.sLastToken.n;
      if (once != 0) {
        pParse.sFirstToken = pParse.sLastToken;
        once = 0;
      }
      switch (tokenType.get()) {
        case TK_SPACE:
          break;
        case TK_COMMENT: {
          /* Various debugging modes can be turned on and off using
           ** special SQL comments.  Check for the special comments
           ** here and take approriate action if found.
           */
          break;
        }
        case TK_ILLEGAL:
          sqliteSetNString(pzErrMsg, "unrecognized token: \"", -1,
                  pParse.sLastToken.z, pParse.sLastToken.n, "\"", 1, 0);
          nErr++;
          break;
        default:
//          sqliteParser(pEngine, tokenType, pParse.sLastToken, pParse);
          if (pParse.zErrMsg != null && pParse.sErrToken.z != null) {
            sqliteSetNString(pzErrMsg, "near \"", -1,
                    pParse.sErrToken.z, pParse.sErrToken.n,
                    "\": ", -1,
                    pParse.zErrMsg, -1,
                    0);
            nErr++;
            sqliteFree(pParse.zErrMsg);
            pParse.zErrMsg = null;
          }
          break;
      }
    }
    if (nErr == 0) {
//      sqliteParser(pEngine, 0, pParse.sLastToken, pParse);
      if (pParse.zErrMsg != null && pParse.sErrToken.z != null) {
        sqliteSetNString(pzErrMsg, "near \"", -1,
                pParse.sErrToken.z, pParse.sErrToken.n,
                "\": ", -1,
                pParse.zErrMsg, -1,
                0);
        nErr++;
        sqliteFree(pParse.zErrMsg);
        pParse.zErrMsg = null;
      }
    }
//    sqliteParserFree(pEngine, free);
    if (pParse.zErrMsg != null) {
      if (pzErrMsg != null) {
        sqliteFree(pzErrMsg);
        pzErrMsg.update(pParse.zErrMsg);
      } else {
        sqliteFree(pParse.zErrMsg);
      }
      if (nErr == 0) nErr++;
    }
    if (pParse.pVdbe != null) {
      sqliteVdbeDelete(pParse.pVdbe);
      pParse.pVdbe = null;
    }
    if (pParse.pNewTable != null) {
      sqliteDeleteTable(pParse.db, pParse.pNewTable);
      pParse.pNewTable = null;
    }
    sqliteParseInfoReset(pParse);
    return nErr;
  }
}
