package io.github.yuemenglong.sqlite.core;

import io.github.yuemenglong.sqlite.common.CharPtr;
import io.github.yuemenglong.sqlite.common.FILE;
import io.github.yuemenglong.sqlite.common.Ptr;

import static io.github.yuemenglong.sqlite.common.Util.*;
import static io.github.yuemenglong.sqlite.core.build.*;
import static io.github.yuemenglong.sqlite.core.dbbe.*;
import static io.github.yuemenglong.sqlite.core.main.*;
import static io.github.yuemenglong.sqlite.core.parse.*;
import static io.github.yuemenglong.sqlite.core.sqliteH.*;
import static io.github.yuemenglong.sqlite.core.tokenize.*;
import static io.github.yuemenglong.sqlite.core.vdbe.*;
import static io.github.yuemenglong.sqlite.core.sqliteint.*;
import static io.github.yuemenglong.sqlite.core.util.*;
import static java.lang.System.exit;

public class shell {
  /*
   ** This routine reads a line of text from standard input, stores
   ** the text in memory obtained from malloc() and returns a pointer
   ** to the text.  NULL is returned at end of file, or if malloc()
   ** fails.
   **
   ** The interface is like "readline" but no command-line editing
   ** is done.
   */
  public static void add_history(CharPtr s) {
  }

  public static CharPtr readline(String zPrompt) {
    return getline(zPrompt);
  }

  public static CharPtr getline(String zPrompt) {
    CharPtr zLine;
    int nLine;
    int n;
    int eol;

//    if (zPrompt != null && zPrompt.get() != 0) {
    if (zPrompt != null) {
      System.out.printf("%s", zPrompt);
//      fflush(stdout);
    }
    nLine = 10000;
    zLine = new CharPtr(nLine);
    if (zLine == null) return null;
    n = 0;
    eol = 0;
    FILE stdin = FILE.stdin();
    return stdin.fgets(zLine, nLine);
//    while (eol == 0) {
//      if (n + 100 > nLine) {
//        nLine = nLine * 2 + 100;
//        zLine = realloc(zLine, nLine);
//        if (zLine == 0) return 0;
//      }
//      if (fgets( & zLine[n],nLine - n, stdin)==0 ){
//        if (n == 0) {
//          free(zLine);
//          return 0;
//        }
//        zLine[n] = 0;
//        eol = 1;
//        break;
//      }
//      while (zLine[n]) {
//        n++;
//      }
//      if (n > 0 && zLine[n - 1] == '\n') {
//        n--;
//        zLine[n] = 0;
//        eol = 1;
//      }
//    }
//    zLine = realloc(zLine, n + 1);
//    return zLine;
  }

  /*
   ** Retrieve a single line of input text.  "isatty" is true if text
   ** is coming from a terminal.  In that case, we issue a prompt and
   ** attempt to use "readline" for command-line editing.  If "isatty"
   ** is false, use "getline" instead of "readline" and issue to prompt.
   **
   ** zPrior is a string of prior text retrieved.  If not the empty
   ** string, then issue a continuation prompt.
   */
  static CharPtr one_input_line(CharPtr zPrior, int isatty) {
    String zPrompt;
    CharPtr zResult;
    if (isatty == 0) {
      return readline(null);
    }
    if (zPrior != null && zPrior.get(0) != 0) {
      zPrompt = "   ...> ";
    } else {
      zPrompt = "sqlite> ";
    }
    zResult = readline(zPrompt);
    if (zResult != null) add_history(zResult);
    return zResult;
  }

  /*
   ** An pointer to an instance of this structure is passed from
   ** the main program to the callback.  This is used to communicate
   ** state and mode information.
   */
  public static class callback_data {
    public sqlite db;            /* The database */
    public int cnt;               /* Number of records displayed so far */
    public FILE out;             /* Write results here */
    public int mode;              /* An output mode setting */
    public int showHeader;        /* True to show column names in List or Column mode */
    public int escape;            /* Escape this character when in MODE_List */
    public CharPtr zDestTable = new CharPtr(250);  /* Name of destination table when MODE_Insert */
    public CharPtr separator = new CharPtr(20);    /* Separator character for MODE_List */
    public int[] colWidth = new int[100];     /* Requested width of each column when in column mode*/
    public int[] actualWidth = new int[100];  /* Actual width of each column */
  }

  ;

  /*
   ** These are the allowed modes.
   */
  public static final int MODE_Line = 0; /* One column per line.  Blank line between records */
  public static final int MODE_Column = 1; /* One record per line in neat columns */
  public static final int MODE_List = 2; /* One record per line with a separator */
  public static final int MODE_Html = 3; /* Generate an XHTML table */
  public static final int MODE_Insert = 4; /* Generate SQL "insert" statements */

  /*
   ** Number of elements in an array
   */
//  #define ArraySize(X)  (sizeof(X)/sizeof(X[0]))

  /*
   ** Return TRUE if the string supplied is a number of some kinds.
   */
  static int is_numeric(CharPtr z) {
    z = z.dup();
    int seen_digit = 0;
    if (z.get() == '-' || z.get() == '+') {
      z.cpp();
    }
    while (isdigit(z.get())) {
      seen_digit = 1;
      z.cpp();
    }
    if (seen_digit != 0 && z.get() == '.') {
      z.cpp();
      while (isdigit(z.get())) {
        z.cpp();
      }
    }
    if (seen_digit != 0 && (z.get() == 'e' || z.get() == 'E')
            && (isdigit(z.get(1)) || ((z.get(1) == '-' || z.get(1) == '+') && isdigit(z.get(2))))
    ) {
      z.move(2);
      while (isdigit(z.get())) {
        z.cpp();
      }
    }
    return (seen_digit != 0 && z.get() == 0) ? 1 : 0;
  }

  /*
   ** Output the given string as a quoted string using SQL quoting conventions.
   */
  public static void output_quoted_string(FILE out, CharPtr z) {
    z = z.dup();
    int i;
    int nSingle = 0;
    int nDouble = 0;
    for (i = 0; z.get(i) != 0; i++) {
      if (z.get(i) == '\'') nSingle++;
      else if (z.get(i) == '"') nDouble++;
    }
    if (nSingle == 0) {
      out.fprintf("'%s'", z);
    } else if (nDouble == 0) {
      out.fprintf("\"%s\"", z);
    } else {
      out.fprintf("'");
      while (z.get() != 0) {
        for (i = 0; z.get(i) != 0 && z.get(i) != '\''; i++) {
        }
        if (i == 0) {
          out.fprintf("''");
          z.cpp();
        } else if (z.get(i) == '\'') {
          out.fprintf("%.*s''", i, z);
          z.move(i + 1);
        } else {
          out.fprintf("%s'", z);
          break;
        }
      }
    }
  }

  /*
   ** Output the given string with characters that are special to
   ** HTML escaped.
   */
  static void output_html_string(FILE out, CharPtr z) {
    int i;
    while (z.get() != 0) {
      for (i = 0; z.get(i) != 0 && z.get(i) != '<' && z.get(i) != '&'; i++) {
      }
      if (i > 0) {
        out.fprintf("%.*s", i, z);
      }
      if (z.get(i) == '<') {
        out.fprintf("&lt;");
      } else if (z.get(i) == '&') {
        out.fprintf("&amp;");
      } else {
        break;
      }
      z.move(i + 1);
    }
  }

  /*
   ** This is the callback routine that the SQLite library
   ** invokes for each row of a query result.
   */
  public static int callback(Object pArg, int nArg, Ptr<CharPtr> azArg, Ptr<CharPtr> azCol) {
    int i;
    callback_data p = (callback_data) pArg;
    switch (p.mode) {
      case MODE_Line: {
        if (p.cnt++ > 0) p.out.fprintf("\n");
        for (i = 0; i < nArg; i++) {
          p.out.fprintf("%s = %s\n", azCol.get(i), azArg.get(i) != null ? azArg.get(i) : null);
        }
        break;
      }
      case MODE_Column: {
        if (p.cnt++ == 0) {
          for (i = 0; i < nArg; i++) {
            int w, n;
            if (i < (p.colWidth.length)) {
              w = p.colWidth[i];
            } else {
              w = 0;
            }
            if (w <= 0) {
              w = strlen(azCol.get(i));
              if (w < 10) w = 10;
              n = strlen(azArg.get(i));
              if (w < n) w = n;
            }
            if (i < ArraySize(p.actualWidth)) {
              p.actualWidth[i] = w;
            }
            if (p.showHeader != 0) {
              p.out.fprintf("%-*.*s%s", w, w, azCol.get(i), i == nArg - 1 ? "\n" : "  ");
            }
          }
          if (p.showHeader != 0) {
            for (i = 0; i < nArg; i++) {
              int w;
              if (i < ArraySize(p.actualWidth)) {
                w = p.actualWidth[i];
              } else {
                w = 10;
              }
              p.out.fprintf("%-*.*s%s", w, w, "-----------------------------------" +
                              "----------------------------------------------------------",
                      i == nArg - 1 ? "\n" : "  ");
            }
          }
        }
        for (i = 0; i < nArg; i++) {
          int w;
          if (i < ArraySize(p.actualWidth)) {
            w = p.actualWidth[i];
          } else {
            w = 10;
          }
          p.out.fprintf("%-*.*s%s", w, w,
                  azArg.get(i) != null ? azArg.get(i) : "", i == nArg - 1 ? "\n" : "  ");
        }
        break;
      }
      case MODE_List: {
        if (p.cnt++ == 0 && p.showHeader != 0) {
          for (i = 0; i < nArg; i++) {
            p.out.fprintf("%s%s", azCol.get(i), i == nArg - 1 ? "\n" : p.separator);
          }
        }
        for (i = 0; i < nArg; i++) {
          CharPtr z = azArg.get(i);
          if (z == null) z = new CharPtr("");
          while (z.get() != 0) {
            int j;
            for (j = 0; z.get(j) != 0 && z.get(j) != p.escape && z.get(j) != '\\'; j++) {
            }
            if (j > 0) {
              p.out.fprintf("%.*s", j, z);
            }
            if (z.get(j) != 0) {
              p.out.fprintf("\\%c", z.get(j));
              z.cpp();
            }
            z.move(j);
          }
          p.out.fprintf("%s", i == nArg - 1 ? "\n" : p.separator);
        }
        break;
      }
      case MODE_Html: {
        if (p.cnt++ == 0 && p.showHeader != 0) {
          p.out.fprintf("<TR>");
          for (i = 0; i < nArg; i++) {
            p.out.fprintf("<TH>%s</TH>", azCol.get(i));
          }
          p.out.fprintf("</TR>\n");
        }
        p.out.fprintf("<TR>");
        for (i = 0; i < nArg; i++) {
          p.out.fprintf("<TD>");
          output_html_string(p.out, azArg.get(i) != null ? azArg.get(i) : new CharPtr(""));
          p.out.fprintf("</TD>\n");
        }
        p.out.fprintf("</TD></TR>\n");
        break;
      }
      case MODE_Insert: {
        p.out.fprintf("INSERT INTO '%s' VALUES(", p.zDestTable);
        for (i = 0; i < nArg; i++) {
          CharPtr zSep = new CharPtr(i > 0 ? "," : "");
          if (azArg.get(i) == null) {
            p.out.fprintf("%sNULL", zSep);
          } else if (is_numeric(azArg.get(i)) != 0) {
            p.out.fprintf("%s%s", zSep, azArg.get(i));
          } else {
            if (zSep.get(0) != 0) p.out.fprintf("%s", zSep);
            output_quoted_string(p.out, azArg.get(i));
          }
        }
        p.out.fprintf(");\n");
      }
    }
    return 0;
  }

  /*
   ** This is a different callback routine used for dumping the database.
   ** Each row received by this callback consists of a table name,
   ** the table type ("index" or "table") and SQL to create the table.
   ** This routine should print text sufficient to recreate the table.
   */
  static int dump_callback(Object pArg, int nArg, Ptr<CharPtr> azArg, Ptr<CharPtr> azCol) {
    callback_data pData = (callback_data) pArg;
    if (nArg != 3) return 1;
    pData.out.fprintf("%s;\n", azArg.get(2));
    if (strcmp(azArg.get(1).toZeroString(), "table") == 0) {
      callback_data d2 = new callback_data();
      CharPtr zSql = new CharPtr(1000);
      d2 = pData;
      d2.mode = MODE_List;
      d2.escape = '\t';
      d2.separator.strcpy("\t");
      pData.out.fprintf("COPY '%s' FROM STDIN;\n", azArg.get(0));
      zSql.sprintf("SELECT * FROM '%s'", azArg.get(0));
      sqlite_exec(pData.db, zSql, shell::callback, d2, null);
      pData.out.fprintf("\\.\n");
    }
    pData.out.fprintf("VACUUM '%s';\n", azArg.get(0));
    return 0;
  }

  /*
   ** Text of a help message
   */
  public static String zHelp =
          ".dump ?TABLE? ...      Dump the database in an text format\n" +
                  ".exit                  Exit this program\n" +
                  ".explain               Set output mode suitable for EXPLAIN\n" +
                  ".header ON|OFF         Turn display of headers on or off\n" +
                  ".help                  Show this message\n" +
                  ".indices TABLE         Show names of all indices on TABLE\n" +
                  ".mode MODE             Set mode to one of \"line\", \"column\", " +
                  "\"list\", or \"html\"\n" +
                  ".mode insert TABLE     Generate SQL insert statements for TABLE\n" +
                  ".output FILENAME       Send output to FILENAME\n" +
                  ".output stdout         Send output to the screen\n" +
                  ".schema ?TABLE?        Show the CREATE statements\n" +
                  ".separator STRING      Change separator string for \"list\" mode\n" +
                  ".tables ?PATTERN?      List names of tables matching a pattern\n" +
                  ".timeout MS            Try opening locked tables for MS milliseconds\n" +
                  ".width NUM NUM ...     Set column widths for \"column\" mode\n";

  /*
   ** If an input line begins with "." then invoke this routine to
   ** process that line.
   */
  static void do_meta_command(CharPtr zLine, sqlite db, callback_data p) {
    int i = 1;
    int nArg = 0;
    int n, c;
    CharPtr[] azArg = new CharPtr[50];

    /* Parse the input line into tokens.
     */
    while (zLine.get(i) != 0 && nArg < ArraySize(azArg)) {
      while (isspace(zLine.get(i))) {
        i++;
      }
      if (zLine.get(i) == '\'' || zLine.get(i) == '"') {
        int delim = zLine.get(i++);
        azArg[nArg++] = zLine.ptr(i);
        while (zLine.get(i) != 0 && zLine.get(i) != delim) {
          i++;
        }
        if (zLine.get(i) == delim) {
          zLine.set(i++, 0);
        }
      } else {
        azArg[nArg++] = zLine.ptr(i);
        while (zLine.get(i) != 0 && !isspace(zLine.get(i))) {
          i++;
        }
        if (zLine.get(i) != 0) zLine.set(i++, 0);
      }
    }

    /* Process the input line.
     */
    if (nArg == 0) return;
    n = strlen(azArg[0]);
    c = azArg[0].get(0);

    if (c == 'd' && strncmp(azArg[0].toZeroString(), "dump", n) == 0) {
      CharPtr zErrMsg = null;
      CharPtr zSql = new CharPtr(1000);
      if (nArg == 1) {
        zSql.sprintf("SELECT name, type, sql FROM sqlite_master " +
                "WHERE type!='meta' " +
                "ORDER BY tbl_name, type DESC, name");
        sqlite_exec(db, zSql, shell::dump_callback, p, zErrMsg);
      } else {
//        int i;
        for (i = 1; i < nArg && zErrMsg == null; i++) {
          zSql.sprintf("SELECT name, type, sql FROM sqlite_master " +
                  "WHERE tbl_name LIKE '%.800s' AND type!='meta' " +
                  "ORDER BY type DESC, name", azArg[i]);
          sqlite_exec(db, zSql, shell::dump_callback, p, zErrMsg);

        }
      }
      if (zErrMsg != null) {
        FILE.stderr().fprintf("Error: %s\n", zErrMsg);
//        free(zErrMsg);
      }
    } else if (c == 'e' && strncmp(azArg[0].toZeroString(), "exit", n) == 0) {
      exit(0);
    } else if (c == 'e' && strncmp(azArg[0].toZeroString(), "explain", n) == 0) {
      p.mode = MODE_Column;
      p.showHeader = 1;
      p.colWidth[0] = 4;
      p.colWidth[1] = 12;
      p.colWidth[2] = 5;
      p.colWidth[3] = 5;
      p.colWidth[4] = 40;
    } else if (c == 'h' && strncmp(azArg[0].toZeroString(), "header", n) == 0 && nArg > 1) {
      int j;
      CharPtr z = azArg[1];
      int val = (azArg[1].atoi());
      for (j = 0; z.get(j) != 0; j++) {
        if (isupper(z.get(j))) z.set(j, tolower(z.get(j)));
      }
      if (strcmp(z.toZeroString(), "on") == 0) {
        val = 1;
      } else if (strcmp(z.toZeroString(), "yes") == 0) {
        val = 1;
      }
      p.showHeader = val;
    } else if (c == 'h' && strncmp(azArg[0].toZeroString(), "help", n) == 0) {
      FILE.stderr().fprintf(zHelp);
    } else if (c == 'i' && strncmp(azArg[0].toZeroString(), "indices", n) == 0 && nArg > 1) {
      callback_data data = new callback_data();
      CharPtr zErrMsg = null;
      CharPtr zSql = new CharPtr(1000);
//      memcpy( & data, p, sizeof(data));
      data.showHeader = 0;
      data.mode = MODE_List;
      zSql.sprintf("SELECT name FROM sqlite_master " +
              "WHERE type='index' AND tbl_name LIKE '%.800s' " +
              "ORDER BY name", azArg[1]);
      sqlite_exec(db, zSql, shell::callback, data, zErrMsg);
      if (zErrMsg != null) {
        FILE.stderr().fprintf("Error: %s\n", zErrMsg);
//        free(zErrMsg);
      }
    } else if (c == 'm' && strncmp(azArg[0].toZeroString(), "mode", n) == 0 && nArg >= 2) {
      int n2 = strlen(azArg[1]);
      if (strncmp(azArg[1].toZeroString(), "line", n2) == 0) {
        p.mode = MODE_Line;
      } else if (strncmp(azArg[1].toZeroString(), "column", n2) == 0) {
        p.mode = MODE_Column;
      } else if (strncmp(azArg[1].toZeroString(), "list", n2) == 0) {
        p.mode = MODE_List;
      } else if (strncmp(azArg[1].toZeroString(), "html", n2) == 0) {
        p.mode = MODE_Html;
      } else if (strncmp(azArg[1].toZeroString(), "insert", n2) == 0) {
        p.mode = MODE_Insert;
        if (nArg >= 3) {
          p.zDestTable.sprintf("%.*s", (int) ((p.zDestTable.memsize()) - 1), azArg[2]);
        } else {
          p.zDestTable.sprintf("table");
        }
      }
    } else if (c == 'o' && strncmp(azArg[0].toZeroString(), "output", n) == 0 && nArg == 2) {
      if (!p.out.isStdout()) {
        p.out.close();
//        fclose(p.out);
      }
      if (strcmp(azArg[1].toZeroString(), "stdout") == 0) {
//        p.out = stdout;
        p.out = FILE.stdout();
      } else {
//        p.out = fopen(azArg[1], "w");
        p.out = FILE.openWrite(azArg[1].toZeroString());//, "w");
        if (p.out == null) {
          FILE.stderr().fprintf("can't write to \"%s\"\n", azArg[1]);
//          p.out = stdout;
          p.out = FILE.stdout();
        }
      }
    } else if (c == 's' && strncmp(azArg[0].toZeroString(), "schema", n) == 0) {
      callback_data data = new callback_data();
      CharPtr zErrMsg = null;
      CharPtr zSql = new CharPtr(1000);
//      memcpy( & data, p, sizeof(data));
      data.showHeader = 0;
      data.mode = MODE_List;
      if (nArg > 1) {
        zSql.sprintf("SELECT sql FROM sqlite_master " +
                        "WHERE tbl_name LIKE '%.800s' AND type!='meta'" +
                        "ORDER BY type DESC, name",
                azArg[1]);
      } else {
        zSql.sprintf("SELECT sql FROM sqlite_master " +
                "WHERE type!='meta' " +
                "ORDER BY tbl_name, type DESC, name");
      }
      sqlite_exec(db, zSql, shell::callback, data, zErrMsg);
      if (zErrMsg != null) {
        FILE.stderr().fprintf("Error: %s\n", zErrMsg);
//        free(zErrMsg);
      }
    } else if (c == 's' && strncmp(azArg[0].toZeroString(), "separator", n) == 0 && nArg == 2) {
      p.separator.sprintf("%.*s", (int) ArraySize(p.separator) - 1, azArg[1]);
    } else if (c == 't' && n > 1 && strncmp(azArg[0].toZeroString(), "tables", n) == 0) {
      callback_data data = new callback_data();
      CharPtr zErrMsg = null;
      CharPtr zSql = new CharPtr(1000);
//      memcpy( & data, p, sizeof(data));
      data.showHeader = 0;
      data.mode = MODE_List;
      if (nArg == 1) {
        zSql.sprintf(
                "SELECT name FROM sqlite_master " +
                        "WHERE type='table' " +
                        "ORDER BY name");
      } else {
        zSql.sprintf(
                "SELECT name FROM sqlite_master " +
                        "WHERE type='table' AND name LIKE '%%%.100s%%' " +
                        "ORDER BY name", azArg[1]);
      }
      sqlite_exec(db, zSql, shell::callback, data, zErrMsg);
      if (zErrMsg != null) {
        FILE.stderr().fprintf("Error: %s\n", zErrMsg);
//        free(zErrMsg);
      }
    } else if (c == 't' && n > 1 && strncmp(azArg[0].toZeroString(), "timeout", n) == 0 && nArg >= 2) {
      sqlite_busy_timeout(db, (azArg[1].atoi()));
    } else if (c == 'w' && strncmp(azArg[0].toZeroString(), "width", n) == 0) {
      int j;
      for (j = 1; j < nArg && j < ArraySize(p.colWidth); j++) {
        p.colWidth[j - 1] = (azArg[j].atoi());
      }
    } else {
      FILE.stderr().fprintf("unknown command: \"%s\". Enter \".help\" for help\n",
              azArg[0]);
    }
  }

  int main(int argc, Ptr<CharPtr> argv) {
    sqlite db;
    CharPtr zErrMsg = null;
    CharPtr argv0 = argv.get(0);
    callback_data data = new callback_data();

//    memset( & data, 0, sizeof(data));
    data.mode = MODE_List;
    data.separator.strcpy("|");
    data.showHeader = 0;
    while (argc >= 2 && argv.get(1).get(0) == '-') {
      if (argv.get(1).strcmp("-html") == 0) {
        data.mode = MODE_Html;
        argc--;
        argv.cpp();
      } else if (argv.get(1).strcmp("-list") == 0) {
        data.mode = MODE_List;
        argc--;
        argv.cpp();
      } else if (argv.get(1).strcmp("-line") == 0) {
        data.mode = MODE_Line;
        argc--;
        argv.cpp();
      } else if (argc >= 3 && argv.get(0).strcmp("-separator") == 0) {
        data.separator.sprintf("%.*s", (int) (data.separator.memsize()) - 1, argv.get(2));
        argc -= 2;
        argv.move(2);
      } else if (argv.get(1).strcmp("-header") == 0) {
        data.showHeader = 1;
        argc--;
        argv.cpp();
      } else if (argv.get(1).strcmp("-noheader") == 0) {
        data.showHeader = 0;
        argc--;
        argv.cpp();
      } else {
        FILE.stderr().fprintf("%s: unknown option: %s\n", argv0, argv.get(1));
        return 1;
      }
    }
    if (argc != 2 && argc != 3) {
      FILE.stderr().fprintf("Usage: %s ?OPTIONS? FILENAME ?SQL?\n", argv0);
      exit(1);
    }
    data.db = db = sqlite_open(argv.get(1), 0666, zErrMsg);
    if (db == null) {
      data.db = db = sqlite_open(argv.get(1), 0444, zErrMsg);
      if (db == null) {
        if (zErrMsg != null) {
          FILE.stderr().fprintf("Unable to open database \"%s\": %s\n", argv.get(1), zErrMsg);
        } else {
          FILE.stderr().fprintf("Unable to open database %s\n", argv.get(1));
        }
        exit(1);
      } else {
        System.out.printf("Database \"%s\" opened READ ONLY!\n", argv.get(1));
      }
    }
    data.out = FILE.stdout();
    if (argc == 3) {
      if (sqlite_exec(db, argv.get(2), shell::callback, data, zErrMsg) != 0 && zErrMsg != null) {
        FILE.stderr().fprintf("SQL error: %s\n", zErrMsg);
        exit(1);
      }
    } else {
      CharPtr zLine;
      CharPtr zSql = null;
      int nSql = 0;
//      int istty = isatty(0);
      int istty = 1;
      if (istty != 0) {
        System.out.printf(
                "Enter \".help\" for instructions\n"
        );
      }
      while ((zLine = one_input_line(zSql, istty)) != null) {
        if (zLine != null && zLine.get(0) == '.') {
          do_meta_command(zLine, db, data);
//          free(zLine);
          continue;
        }
        if (zSql == null) {
          nSql = strlen(zLine);
          zSql = new CharPtr(nSql + 1);//malloc(nSql + 1);
          strcpy(zSql, zLine);
        } else {
          int len = strlen(zLine);
          zSql = realloc(zSql, nSql + len + 2);
          if (zSql == null) {
            fprintf(stderr(), "%s: out of memory!\n", argv0);
            exit(1);
          }
          strcpy(zSql.ptr(nSql++), "\n");
          strcpy(zSql.ptr(nSql), zLine);
          nSql += len;
        }
        free(zLine);
        if (sqlite_complete(zSql) != 0) {
          data.cnt = 0;
          if (sqlite_exec(db, zSql, shell::callback, data, zErrMsg) != 0
                  && zErrMsg != null) {
            printf("SQL error: %s\n", zErrMsg);
            free(zErrMsg);
            zErrMsg = null;
          }
          free(zSql);
          zSql = null;
          nSql = 0;
        }
      }
    }
    sqlite_close(db);
    return 0;
  }
}
