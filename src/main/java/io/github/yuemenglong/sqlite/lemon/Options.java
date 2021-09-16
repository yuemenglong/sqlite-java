package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.Addr;

import java.io.IOException;
import java.io.OutputStream;
import java.util.function.Consumer;

import static io.github.yuemenglong.sqlite.common.Util.*;
import static io.github.yuemenglong.sqlite.lemon.Options.Type.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class Options {
  public enum Type {
    OPT_FLAG, OPT_INT, OPT_DBL, OPT_STR,
    OPT_FFLAG, OPT_FINT, OPT_FDBL, OPT_FSTR
  }

  public Options(Type type, String label, Addr<Object> arg, String message) {
    this.type = type;
    this.label = label;
    this.arg = arg;
    this.message = message;
  }

  public Type type;
  public String label;
  public Addr<Object> arg;
  public String message;

  private static String[] argv;
  private static Options[] op;
  private static String emsg = "Command line syntax error: ";
  private static OutputStream errstream;

  public static boolean ISOPT(String x) {
    byte[] b = x.getBytes();
    return b[0] == '-' || b[0] == '+' || strchr(x, '=') != 0;
  }

  /*
   ** Print the command line with a carrot pointing to the k-th character
   ** of the n-th field.
   */
  public static void errline(int n, int k, OutputStream err) throws IOException {
    int spcnt, i;
    spcnt = 0;
    if (argv[0] != null) err.write(String.format("%s", argv[0]).getBytes());
    assert argv[0] != null;
    spcnt = strlen(argv[0]) + 1;
    for (i = 1; i < n && argv[i] != null; i++) {
      err.write(String.format(" %s", argv[i]).getBytes());
      spcnt += strlen(argv[i] + 1);
    }
    spcnt += k;
    for (; argv[i] != null; i++) err.write(String.format(" %s", argv[i]).getBytes());
    if (spcnt < 20) {
      err.write(String.format("\n%" + spcnt + "s^-- here\n", "").getBytes());
    } else {
      err.write(String.format("\n%" + (spcnt - 7) + "shere --^\n", "").getBytes());
    }
  }

  /*
   ** Return the index of the N-th non-switch argument.  Return -1
   ** if N is out of range.
   */
  public static int argindex(int n) {
    if (n == 0) {
      return 0;
    }
    int i;
    int dashdash = 0;
    if (argv != null && argv[0] != null) {
      for (i = 1; i < argv.length; i++) {
        if (dashdash != 0 || !ISOPT(argv[i])) {
          if (n == 0) return i;
          n--;
        }
        if (strcmp(argv[i], "--") == 0) dashdash = 1;
      }
    }
    return -1;
  }


  /*
   ** Process a flag command line argument.
   */
  static int handleflags(int i, OutputStream err) throws IOException {
    int v;
    int errcnt = 0;
    int j;
    for (j = 0; op[j].label != null; j++) {
      if (strcmp(argv[i].substring(1), op[j].label) == 0) break;
    }
    v = argv[i].charAt(0) == '-' ? 1 : 0;
    if (op[j].label == null) {
      if (err != null) {
        err.write(String.format("%sundefined option.\n", emsg).getBytes());
        errline(i, 1, err);
      }
      errcnt++;
    } else if (op[j].type == OPT_FLAG) {
      op[j].arg.set(v);
    } else if (op[j].type == OPT_FFLAG) {
      ((Consumer) (op[j].arg.get())).accept(v);
    } else {
      if (err != null) {
        err.write(String.format("%smissing argument on switch.\n", emsg).getBytes());
        errline(i, 1, err);
      }
      errcnt++;
    }
    return errcnt;
  }

  /*
   ** Process a command line switch which has an argument.
   */
  static int handleswitch(int i, OutputStream err) throws IOException {
    int lv = 0;
    double dv = 0.0;
    int sv = 0;
    int cp;
    int j;
    int errcnt = 0;
    String s = argv[i];
    byte[] b = s.getBytes();
    cp = strchr(argv[i], '=');
    b[cp] = 0;
    for (j = 0; op[j].label != null; j++) {
      if (strcmp(argv[i].substring(0, cp), op[j].label) == 0) break;
    }
    b[cp] = '=';
    if (op[j].label == null) {
      if (err != null) {
        err.write(String.format("%sundefined option.\n", emsg).getBytes());
        errline(i, 0, err);
      }
      errcnt++;
    } else {
      cp++;
      switch (op[j].type) {
        case OPT_FLAG:
        case OPT_FFLAG:
          if (err != null) {
            err.write(String.format("%soption requires an argument.\n", emsg).getBytes());
            errline(i, 0, err);
          }
          errcnt++;
          break;
        case OPT_DBL:
        case OPT_FDBL:
          try {
            dv = strtod(s.substring(cp));
          } catch (Throwable e) {
            if (err != null) {
              err.write(String.format("%sillegal character in floating-point argument.\n", emsg).getBytes());
              errline(i, cp, err);
            }
            errcnt++;
          }
          break;
        case OPT_INT:
        case OPT_FINT:
          try {
            lv = (int) strtol(s.substring(cp), 0);
          } catch (Throwable e) {
            if (err != null) {
              err.write(String.format("%sillegal character in integer argument.\n", emsg).getBytes());
              errline(i, cp, err);
            }
            errcnt++;
          }
          break;
        case OPT_STR:
        case OPT_FSTR:
          sv = cp;
          break;
      }
      switch (op[j].type) {
        case OPT_FLAG:
        case OPT_FFLAG:
          break;
        case OPT_DBL:
          op[j].arg.set(dv);
          break;
        case OPT_FDBL:
          ((Consumer) (op[j].arg.get())).accept(dv);
          break;
        case OPT_INT:
          op[j].arg.set(lv);
          break;
        case OPT_FINT:
          ((Consumer) (op[j].arg.get())).accept(lv);
          break;
        case OPT_STR:
          op[j].arg.set(argv[i].substring(sv));
          break;
        case OPT_FSTR:
          ((Consumer) (op[j].arg.get())).accept(sv);
          break;
      }
    }
    return errcnt;
  }

  public static int optInit(String[] a, Options[] o, OutputStream err) throws IOException {
    int errcnt = 0;
    argv = a;
    op = o;
    errstream = err;
    if (argv != null && argv[0] != null && op != null) {
      int i;
      for (i = 1; i < argv.length; i++) {
        if (argv[i].charAt(0) == '+' || argv[i].charAt(0) == '-') {
          errcnt += handleflags(i, err);
        } else if (strchr(argv[i], '=') > 0) {
          errcnt += handleswitch(i, err);
        }
      }
    }
    if (errcnt > 0) {
      err.write(String.format("Valid command line options for \"%s\" are:\n", a[0]).getBytes());
      optPrint();
      System.exit(1);
    }
    return 0;
  }

  public static int optNArgs() {
    int cnt = 0;
    int dashdash = 0;
    int i;
    if (argv.length > 0 && argv[0] != null) {
      cnt = 1;
      for (i = 1; i < argv.length; i++) {
        if (dashdash != 0 || !ISOPT(argv[i])) cnt++;
        if (strcmp(argv[i], "--") == 0) dashdash = 1;
      }
    }
    return cnt;
  }

  public static String optArg(int n) {
    int i;
    i = argindex(n);
    return i >= 0 ? argv[i] : null;
  }

  public static void optErr(int n) throws IOException {
    int i;
    i = argindex(n);
    if (i >= 0) errline(i, 0, errstream);
  }

  public static void optPrint() throws IOException {
    int i;
    int max, len;
    max = 0;
    for (i = 0; op[i].label != null; i++) {
      len = strlen(op[i].label) + 1;
      switch (op[i].type) {
        case OPT_FLAG:
        case OPT_FFLAG:
          break;
        case OPT_INT:
        case OPT_FINT:
          len += 9;       /* length of "<integer>" */
          break;
        case OPT_DBL:
        case OPT_FDBL:
          len += 6;       /* length of "<real>" */
          break;
        case OPT_STR:
        case OPT_FSTR:
          len += 8;       /* length of "<string>" */
          break;
      }
      if (len > max) max = len;
    }
    for (i = 0; op[i].label != null; i++) {
      switch (op[i].type) {
        case OPT_FLAG:
        case OPT_FFLAG:
          errstream.write(String.format("  -%-" + max + "s  %s\n", op[i].label, op[i].message).getBytes());
          break;
        case OPT_INT:
        case OPT_FINT:
          errstream.write(String.format("  %s=<integer>%" + (max - strlen(op[i].label) - 9) + "s  %s\n", op[i].label,
                  "", op[i].message).getBytes());
          break;
        case OPT_DBL:
        case OPT_FDBL:
          errstream.write(String.format("  %s=<real>%" + (max - strlen(op[i].label) - 6) + "s  %s\n", op[i].label,
                  "", op[i].message).getBytes());
          break;
        case OPT_STR:
        case OPT_FSTR:
          errstream.write(String.format("  %s=<string>%" + (max - strlen(op[i].label) - 8) + "s  %s\n", op[i].label,
                  "", op[i].message).getBytes());
          break;
      }
    }
  }
}
