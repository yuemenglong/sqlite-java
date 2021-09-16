package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.Addr;
import io.github.yuemenglong.sqlite.common.Assert;
import io.github.yuemenglong.sqlite.lemon.util.Replacer;

import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.yuemenglong.sqlite.lemon.Action.Type.NOT_USED;
import static io.github.yuemenglong.sqlite.lemon.Action.Type.REDUCE;
import static io.github.yuemenglong.sqlite.lemon.Common.MAXRHS;
import static io.github.yuemenglong.sqlite.lemon.Symbol.SymbolType.NONTERMINAL;
import static io.github.yuemenglong.sqlite.lemon.Symbol.SymbolType.TERMINAL;
import static io.github.yuemenglong.sqlite.common.Util.*;

@SuppressWarnings("unused")
public class Report {
  public static String fileMakeName(Lemon lemp, String suffix) {
    String name = lemp.filename;
    name = name.substring(0, name.indexOf('.'));
    return String.format("%s%s", name, suffix);
  }

  public static FileInputStream fileOpenRead(Lemon lemp, String suffix) throws FileNotFoundException {
    try {
      return new FileInputStream(fileMakeName(lemp, suffix));
    } catch (Throwable e) {
      return null;
    }
  }

  public static FileOutputStream fileOpenWrite(Lemon lemp, String suffix) throws FileNotFoundException {
    lemp.outname = fileMakeName(lemp, suffix);
    return new FileOutputStream(lemp.outname);
  }

  public static void Reprint(Lemon lemp) {
    Rule rp;
    Symbol sp;
    int i, j, maxlen, len, ncolumns, skip;
    System.out.printf("// Reprint of input file \"%s\".\n// Symbols:\n", lemp.filename);
    maxlen = 10;
    for (i = 0; i < lemp.nsymbol; i++) {
      sp = lemp.symbols[i];
      len = sp.name.length();
      if (len > maxlen) maxlen = len;
    }
    ncolumns = 76 / (maxlen + 5);
    if (ncolumns < 1) ncolumns = 1;
    skip = (lemp.nsymbol + ncolumns - 1) / ncolumns;
    for (i = 0; i < skip; i++) {
      System.out.print("//");
      for (j = i; j < lemp.nsymbol; j += skip) {
        sp = lemp.symbols[j];
        Assert.assertTrue(sp.index == j);
        String fmt = String.format("%d.%d", maxlen, maxlen);
        System.out.printf(" %3d %-" + fmt + "s", j, sp.name);
      }
      System.out.println();
    }
    for (rp = lemp.rule; rp != null; rp = rp.next) {
      System.out.printf("%s", rp.lhs.name);
      System.out.print(" ::=");
      for (i = 0; i < rp.nrhs; i++) {
        System.out.printf(" %s", rp.rhs[i].name);
      }
      System.out.print(".");
      if (rp.precsym != null) System.out.printf(" [%s]", rp.precsym.name);
      System.out.print("\n");
    }
  }

  public static void configPrint(OutputStream fp, Config cfp) throws IOException {
    Rule rp = cfp.rp;
    fp.write(String.format("%s ::=", rp.lhs.name).getBytes());
    for (int i = 0; i < rp.nrhs; i++) {
      if (i == cfp.dot) fp.write(" *".getBytes());
      if (i == rp.nrhs) break;
      fp.write(String.format(" %s", rp.rhs[i].name).getBytes());
    }
  }

  public static void setPrint(OutputStream out, byte[] set, Lemon lemp) throws IOException {
    String spacer = "";
    out.write(String.format("%12s[", "").getBytes());
    for (int i = 0; i < lemp.nterminal; i++) {
      if (Set.find(set, i) != 0) {
        out.write(String.format("%s%s", spacer, lemp.symbols[i].name).getBytes());
        spacer = " ";
      }
    }
    out.write("\n".getBytes());
  }

  public static void plinkPrint(OutputStream out, Plink plp, String tag) throws IOException {
    while (plp != null) {
      out.write(String.format("%12s%s (state %2d) ", "", tag, plp.cfp.stp.index).getBytes());
      configPrint(out, plp.cfp);
      out.write("\n".getBytes());
      plp = plp.next;
    }
  }

  public static int printAction(Action ap, OutputStream fp, int indent) throws IOException {
    int result = 1;
    switch (ap.type) {
      case SHIFT:
        fp.write(String.format("%" + indent + "s shift  %d", ap.sp.name, ap.x.stp.index).getBytes());
        break;
      case REDUCE:
        fp.write(String.format("%" + indent + "s reduce %d", ap.sp.name, ap.x.rp.index).getBytes());
        break;
      case ACCEPT:
        fp.write(String.format("%" + indent + "s accept", ap.sp.name).getBytes());
        break;
      case ERROR:
        fp.write(String.format("%" + indent + "s error", ap.sp.name).getBytes());
        break;
      case CONFLICT:
        fp.write(String.format("%" + indent + "s reduce %-3d ** Parsing conflict **",
                ap.sp.name, ap.x.rp.index).getBytes());
        break;
      case SH_RESOLVED:
      case RD_RESOLVED:
      case NOT_USED:
        result = 0;
        break;
    }
    return result;
  }

  public static void reportOutput(Lemon lemp) throws IOException {
    int i;
    State stp;
    Config cfp;
    Action ap;
    OutputStream fp;

    fp = fileOpenWrite(lemp, ".out");
    fp.write("\b".getBytes());
    for (i = 0; i < lemp.nstate; i++) {
      stp = lemp.sorted[i];
      fp.write(String.format("State %d:\n", stp.index).getBytes());
      if (lemp.basisflag != 0) cfp = stp.bp;
      else cfp = stp.cfp;
      while (cfp != null) {
        if (cfp.dot == cfp.rp.nrhs) {
          String buf = String.format("(%d)", cfp.rp.index);
          fp.write(String.format("    %5s ", buf).getBytes());
        } else {
          fp.write("          ".getBytes());
        }
        configPrint(fp, cfp);
        fp.write("\n".getBytes());
        setPrint(fp, cfp.fws, lemp);
        plinkPrint(fp, cfp.fplp, "To  ");
        plinkPrint(fp, cfp.bplp, "From");
        if (lemp.basisflag != 0) cfp = cfp.bp;
        else cfp = cfp.next;
      }
      fp.write("\n".getBytes());
      for (ap = stp.ap; ap != null; ap = ap.next) {
        if (printAction(ap, fp, 30) != 0) fp.write("\n".getBytes());
      }
      fp.write("\n".getBytes());
    }
    fp.close();
  }

  public static String pathsearch(String argv0, String name, int modemask) {
    int cp = strrchr(argv0, '/');
    if (cp != 0) {
      char c = argv0.charAt(cp);
      String sub = argv0.substring(0, cp);
      return String.format("%s/%s", sub, name);
    } else {
      String pathlist = getenv("PATH");
      if (pathlist == null) pathlist = ".:/bin:/usr/bin";
      String[] paths = pathlist.split(":");
      for (String path : paths) {
        if (access(path, modemask) == 0) return path;
      }
      return null;
    }
  }

  public static int computeAction(Lemon lemp, Action ap) {
    int act;
    switch (ap.type) {
      case SHIFT:
        act = ap.x.stp.index;
        break;
      case REDUCE:
        act = ap.x.rp.index + lemp.nstate;
        break;
      case ERROR:
        act = lemp.nstate + lemp.nrule;
        break;
      case ACCEPT:
        act = lemp.nstate + lemp.nrule + 1;
        break;
      default:
        act = -1;
        break;
    }
    return act;
  }

  public static void tpltXfer(String name, BufferedReader br, OutputStream out, Addr<Integer> lineno) throws IOException {
    int i, iStart;
    String line;
    while ((line = br.readLine()) != null) {
      if (line.length() >= 2 && line.charAt(0) == '%' && line.charAt(1) == '%') {
        break;
      }
      lineno.set(lineno.get() + 1);
      iStart = 0;
      if (name != null) {
        for (i = 0; i < line.length(); i++) {
          if (line.charAt(i) == 'P' && strncmp(line.substring(i), "Parse", 5) == 0
                  && (i == 0 || !isalpha((byte) line.charAt(i - 1)))
          ) {
            if (i > iStart) out.write(String.format("%." + (i - iStart) + "s", line.substring(iStart)).getBytes());
            out.write(name.getBytes());
            i += 4;
            iStart = i + 1;
          }
        }
      }
      out.write(line.substring(iStart).getBytes());
      out.write("\n".getBytes());
    }
  }

  public static InputStream tpltOpen(Lemon lemp) throws FileNotFoundException {
//    String templatename = "lempar.c";
    String templatename = "lempar.java";
    String buf;
    String tpltname;
    int cp = strrchr(lemp.filename, '.');
    if (cp != 0) {
      buf = String.format("%s.lt", lemp.filename.substring(0, cp));
    } else {
      buf = String.format("%s.lt", lemp.filename);
    }
    if (access(buf, 4) == 0) {
      tpltname = buf;
    } else {
      tpltname = pathsearch(lemp.argv0, templatename, 0);
    }
    if (tpltname == null) {
      System.err.printf("Can't find the parser driver template file \"%s\".\n", templatename);
      return null;
    }
    return new FileInputStream(tpltname);
  }

  public static void tpltPrint(OutputStream out, Lemon lemp, String str, int strln, Addr<Integer> lineno) throws IOException {
    if (str == null) return;
    out.write(String.format("// #line %d \"%s\"\n", strln, lemp.filename).getBytes());
    int p = 0;
    while (p < str.length()) {
      if (str.charAt(p) == '\n') lineno.set(lineno.get() + 1);
      out.write(str.charAt(p));
      p += 1;
    }
    out.write(String.format("\n// #line %d \"%s\"\n", lineno.get() + 2, lemp.outname).getBytes());
  }

  public static void emitDestructorCode(OutputStream out, Symbol sp, Lemon lemp, Addr<Integer> lineno) throws IOException {
    int cp;
    int linecnt = 0;
    byte[] b;
    if (sp.type == TERMINAL) {
      b = lemp.tokendest == null ? null : lemp.tokendest.getBytes();
      if (b == null) return;
      out.write(String.format("// #line %d \"%s\"\n{", lemp.tokendestln, lemp.filename).getBytes());
    } else {
      b = sp.destructor == null ? null : sp.destructor.getBytes();
      if (b == null) return;
      out.write(String.format("// #line %d \"%s\"\n{", sp.destructorln, lemp.filename).getBytes());
    }
    for (cp = 0; cp < b.length; cp++) {
      if (b[cp] == '$' && b[cp + 1] == '$') {
        out.write(String.format("(yypminor.yy%d)", sp.dtnum).getBytes());
        cp++;
        continue;
      }
      if (b[cp] == '\n') linecnt++;
      out.write(b[cp]);
    }
    lineno.set(lineno.get() + 3 + linecnt);
    out.write(String.format("}\n// #line %d \"%s\"\n", lineno.get(), lemp.outname).getBytes());
  }

  public static int hasDestructor(Symbol sp, Lemon lemp) {
    boolean ret;
    if (sp.type == TERMINAL) {
      ret = lemp.tokendest != null;
    } else {
      ret = sp.destructor != null;
    }
    return ret ? 1 : 0;
  }

  @SuppressWarnings("StatementWithEmptyBody")
  public static void emitCode(OutputStream out, Rule rp, Lemon lemp, Addr<Integer> lineno) throws IOException {
    int cp, xp;
    int linecnt = 0;
    int i;
    byte lhsused = 0;
    byte[] used = new byte[MAXRHS];
    for (i = 0; i < rp.nrhs; i++) used[i] = 0;
    if (rp.code != null) {
      out.write(String.format("// #line %d \"%s\"\n{", rp.line, lemp.filename).getBytes());
      String s = rp.code;
      byte[] b = s.getBytes();
      for (cp = 0; cp < b.length; cp++) {
        if (isalpha(b[cp]) && (cp == 0 || !isalnum(b[cp - 1]))) {
          int saved;
          for (xp = cp + 1; isalnum(b[xp]); xp++) ;
          saved = xp;
          if (rp.lhsalias != null && strcmp(s.substring(cp, cp + rp.lhsalias.length()), rp.lhsalias) == 0) {
            out.write(String.format("yygotominor.yy%d", rp.lhs.dtnum).getBytes());
            cp = xp;
            lhsused = 1;
          } else {
            for (i = 0; i < rp.nrhs; i++) {
              if (rp.rhsalias[i] != null && strcmp(s.substring(cp, cp + rp.rhsalias[i].length()), rp.rhsalias[i]) == 0) {
//                out.write(String.format("yymsp[%d].minor.yy%d", i - rp.nrhs + 1, rp.rhs[i].dtnum).getBytes());
                out.write(String.format("yypParser.offset(%d).minor.yy%d", i - rp.nrhs + 1, rp.rhs[i].dtnum).getBytes());
                cp = xp;
                used[i] = 1;
                break;
              }
            }
          }
          xp = saved;
        }
        if (b[cp] == '\n') linecnt++;
        out.write(b[cp]);
      }
      lineno.set(lineno.get() + 3 + linecnt);
      out.write(String.format("}\n// #line %d \"%s\"\n", lineno.get(), lemp.outname).getBytes());
    }
    if (rp.lhsalias != null && lhsused == 0) {
      Error.msg(lemp.filename, rp.ruleline,
              "Label \"%s\" for \"%s(%s)\" is never used.",
              rp.lhsalias, rp.lhs.name, rp.lhsalias);
      lemp.errorcnt++;
    }
    for (i = 0; i < rp.nrhs; i++) {
      if (rp.rhsalias[i] != null && used[i] == 0) {
        Error.msg(lemp.filename, rp.ruleline,
                "Label $%s$ for \"%s(%s)\" is never used.",
                rp.rhsalias[i], rp.rhs[i].name, rp.rhsalias[i]);
        lemp.errorcnt++;
      } else if (rp.rhsalias[i] == null) {
        if (hasDestructor(rp.rhs[i], lemp) != 0) {
          out.write(String.format("  yy_destructor(%d,&yymsp[%d].minor);\n",
                  rp.rhs[i].index, i - rp.nrhs + 1).getBytes());
          lineno.set(lineno.get() + 1);
        } else {
          out.write(String.format("        /* No destructor defined for %s */\n",
                  rp.rhs[i].name).getBytes());
          lineno.set(lineno.get() + 1);
        }
      }
    }
  }

  public static void printStackUnion(OutputStream out, Lemon lemp, Addr<Integer> plineno, int mhflag) throws IOException {
    int lineno = plineno.get();
    String[] types;
    int arraysize;
    int maxdtlength;
    byte[] stddt;
    int i, j;
    int hash;
    String name;

    arraysize = lemp.nsymbol * 2;
    types = new String[arraysize];
    maxdtlength = 0;
    for (i = 0; i < lemp.nsymbol; i++) {
      int len;
      Symbol sp = lemp.symbols[i];
      if (sp.datatype == null) continue;
      len = sp.datatype.length();
      if (len > maxdtlength) maxdtlength = len;
    }
    for (i = 0; i < lemp.nsymbol; i++) {
      Symbol sp = lemp.symbols[i];
      int cp;
      if (sp == lemp.errsym) {
        sp.dtnum = arraysize + 1;
        continue;
      }
      if (sp.type != NONTERMINAL || sp.datatype == null) {
        sp.dtnum = 0;
        continue;
      }
      String s = sp.datatype;
      byte[] b = s.getBytes();
      cp = 0;
      j = 0;
      stddt = new byte[maxdtlength * 2 + 1];
      while (isspace(b[cp])) cp++;
      while (cp < b.length) stddt[j++] = b[cp++];
      while (j > 0 && isspace(stddt[j - 1])) j--;
      stddt[j] = 0;
      hash = 0;
      for (j = 0; stddt[j] != 0; j++) {
        hash = hash * 53 + stddt[j];
      }
      if (hash < 0) hash = -hash;
      hash = hash % arraysize;
      while (types[hash] != null) {
        if (strcmp(types[hash], new String(stddt, 0, j)) == 0) {
          sp.dtnum = hash + 1;
          break;
        }
        hash++;
        if (hash > arraysize) hash = 0;
      }
      if (types[hash] == null) {
        sp.dtnum = hash + 1;
        types[hash] = new String(stddt, 0, j);
      }
    }
    name = lemp.name != null ? lemp.name : "Parse";
    lineno = plineno.get();
    if (mhflag != 0) {
      out.write("#if INTERFACE\n".getBytes());
      lineno++;
    }
    Replacer.regist(String.format("%sTOKENTYPE", name), lemp.tokentype != null ? lemp.tokentype : "Object");
//    out.write(String.format("#define %sTOKENTYPE %s\n", name,
//            lemp.tokentype != null ? lemp.tokentype : "void*").getBytes());
//    lineno++;
    if (mhflag != 0) {
      out.write("#endif\n".getBytes());
      lineno++;
    }
    out.write("public static class YYMINORTYPE {\n".getBytes());
//    out.write("typedef union {\n".getBytes());
    lineno++;
    out.write(String.format("  public %sTOKENTYPE yy0;\n", name).getBytes());
    lineno++;
    for (i = 0; i < arraysize; i++) {
      if (types[i] == null) continue;
      out.write(String.format("  public %s yy%d;\n", types[i], i + 1).getBytes());
      lineno++;
    }
    out.write(String.format("  public int yy%d;\n", lemp.errsym.dtnum).getBytes());
    lineno++;
    out.write("}\n".getBytes());
    lineno++;
    plineno.set(lineno);
  }

  public static void printTemp(ByteArrayOutputStream bos) {
    System.out.println(new String(bos.toByteArray()));
  }

  public static void reportTable(Lemon lemp, int mhflag) throws IOException {
    String line;
    final AtomicInteger lineno = new AtomicInteger();
    Addr<Integer> plineno = new Addr<>(lineno::get, lineno::set);
    State stp;
    Action ap;
    Rule rp;
    int i;
    int tablecnt;
    String name;
    InputStream in = tpltOpen(lemp);
    if (in == null) {
      throw new RuntimeException("No InputStream");
    }
    OutputStream out0 = fileOpenWrite(lemp, ".java");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    lineno.set(1);
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    tpltXfer(lemp.name, br, out, plineno);
    tpltPrint(out, lemp, lemp.include, lemp.includeln, plineno);
    if (mhflag != 0) {
      name = fileMakeName(lemp, ".h");
      out.write(String.format("#include \"%s\"\n", name).getBytes());
      lineno.incrementAndGet();
    }
    tpltXfer(lemp.name, br, out, plineno);
    if (mhflag != 0) {
      String prefix;
      out.write("#if INTERFACE\n".getBytes());
      lineno.incrementAndGet();
      if (lemp.tokenprefix != null) prefix = lemp.tokenprefix;
      else prefix = "";
      for (i = 1; i < lemp.nterminal; i++) {
        out.write(String.format("#define %s%-30s %2d\n", prefix, lemp.symbols[i].name, i).getBytes());
        lineno.incrementAndGet();
      }
      out.write("#endif\n".getBytes());
      lineno.incrementAndGet();
    }
    tpltXfer(lemp.name, br, out, plineno);
    /* Generate the defines */
    out.write("/* \001 */\n".getBytes());
    Replacer.regist("YYCODETYPE", "int");
//    out.write(String.format("#define YYCODETYPE %s\n",
//            lemp.nsymbol > 250 ? "int" : "unsigned char").getBytes());
//    lineno.incrementAndGet();
    Replacer.regist("YYNOCODE", lemp.nsymbol + 1);
//    out.write(String.format("#define YYNOCODE %d\n", lemp.nsymbol + 1).getBytes());
//    lineno.incrementAndGet();
    Replacer.regist("YYACTIONTYPE", "int");
//    out.write(String.format("#define YYACTIONTYPE %s\n",
//            lemp.nstate + lemp.nrule > 250 ? "int" : "unsigned char").getBytes());
//    lineno.incrementAndGet();
    printStackUnion(out, lemp, plineno, mhflag);
    if (lemp.stacksize != null) {
      if (atoi(lemp.stacksize) <= 0) {
        Error.msg(lemp.filename, 0,
                "Illegal stack size: [%s].  The stack size should be an integer constant.",
                lemp.stacksize);
        lemp.errorcnt++;
        lemp.stacksize = "100";
      }
      Replacer.regist("YYSTACKDEPTH", lemp.stacksize);
//      out.write(String.format("#define YYSTACKDEPTH %s\n", lemp.stacksize).getBytes());
//      lineno.incrementAndGet();
    } else {
      Replacer.regist("YYSTACKDEPTH", 100);
//      out.write("#define YYSTACKDEPTH 100\n".getBytes());
//      lineno.incrementAndGet();
    }
    if (mhflag != 0) {
      out.write("#if INTERFACE\n".getBytes());
      lineno.incrementAndGet();
    }
    name = lemp.name != null ? lemp.name : "Parse";
    String arg = lemp.arg;
    if (arg == null) {
      arg = "Object ignore";
    }
    Replacer.regist("ParseARGDECL", arg.split(" ")[1]);
    Replacer.regist("ParseXARGDECL", arg);
    Replacer.regist("ParseANSIARGDECL", arg);
//    if (lemp.arg != null && lemp.arg.charAt(0) != 0) {
//      byte[] arg = lemp.arg.getBytes();
//      i = (arg.length);
//      while (i >= 1 && isspace(arg[i - 1])) i--;
//      while (i >= 1 && (isalnum(arg[i - 1]) || arg[i - 1] == '_')) i--;
//      out.write(String.format("#define %sARGDECL ,%s\n", name, new String(arg, 0, i)).getBytes());
//      lineno.incrementAndGet();
//      out.write(String.format("#define %sXARGDECL %s;\n", name, new String(arg)).getBytes());
//      lineno.incrementAndGet();
//      out.write(String.format("#define %sANSIARGDECL ,%s\n", name, new String(arg)).getBytes());
//      lineno.incrementAndGet();
//    } else {
//      out.write(String.format("#define %sARGDECL\n", name).getBytes());
//      lineno.incrementAndGet();
//      out.write(String.format("#define %sXARGDECL\n", name).getBytes());
//      lineno.incrementAndGet();
//      out.write(String.format("#define %sANSIARGDECL\n", name).getBytes());
//      lineno.incrementAndGet();
//    }
    if (mhflag != 0) {
      out.write("#endif\n".getBytes());
      lineno.incrementAndGet();
    }
    Replacer.regist("YYNSTATE", lemp.nstate);
    Replacer.regist("YYNRULE", lemp.nrule);
    Replacer.regist("YYERRORSYMBOL", lemp.errsym.index);
    Replacer.regist("YYERRSYMDT", String.format("yy%d", lemp.errsym.dtnum));
//    out.write(String.format("#define YYNSTATE %d\n", lemp.nstate).getBytes());
//    lineno.incrementAndGet();
//    out.write(String.format("#define YYNRULE %d\n", lemp.nrule).getBytes());
//    lineno.incrementAndGet();
//    out.write(String.format("#define YYERRORSYMBOL %d\n", lemp.errsym.index).getBytes());
//    lineno.incrementAndGet();
//    out.write(String.format("#define YYERRSYMDT yy%d\n", lemp.errsym.dtnum).getBytes());
//    lineno.incrementAndGet();
    tpltXfer(lemp.name, br, out, plineno);

    tablecnt = 0;

    /* Loop over parser states */
    int idx = 0;
    for (i = 0; i < lemp.nstate; i++) {
      int tablesize;              /* size of the hash table */
      int j, k;                    /* Loop counter */
      int[] collide = new int[2048];          /* The collision chain for the table */
      Action[] table = new Action[2048]; /* Build the hash table here */

      /* Find the number of actions and initialize the hash table */
      stp = lemp.sorted[i];
      stp.tabstart = tablecnt;
      stp.naction = 0;
      for (ap = stp.ap; ap != null; ap = ap.next) {
        if (ap.sp.index != lemp.nsymbol && computeAction(lemp, ap) >= 0) {
          stp.naction++;
        }
      }
      tablesize = 1;
      while (tablesize < stp.naction) tablesize += tablesize;
      for (j = 0; j < tablesize; j++) {
        table[j] = null;
        collide[j] = -1;
      }

      /* Hash the actions into the hash table */
      stp.tabdfltact = lemp.nstate + lemp.nrule;
      for (ap = stp.ap; ap != null; ap = ap.next) {
        int action = computeAction(lemp, ap);
        int h;
        if (ap.sp.index == lemp.nsymbol) {
          stp.tabdfltact = action;
        } else if (action >= 0) {
          h = ap.sp.index & (tablesize - 1);
          ap.collide = table[h];
          table[h] = ap;
        }
      }

      /* Resolve collisions */
      for (j = k = 0; j < tablesize; j++) {
        if (table[j] != null && table[j].collide != null) {
          while (table[k] != null) k++;
          table[k] = table[j].collide;
          collide[j] = k;
          table[j].collide = null;
          if (k < j) j = k - 1;
        }
      }

      /* Print the hash table */
      out.write(String.format("/* State %d */\n", stp.index).getBytes());
      lineno.incrementAndGet();
      for (j = 0; j < tablesize; j++) {
        out.write(String.format("/* %d */", idx).getBytes());
        idx += 1;
        if (table[j] == null) {
          out.write("  new yyActionEntry(YYNOCODE,0,-1), /* Unused */\n".getBytes());
//          out.write("  {YYNOCODE,0,0}, /* Unused */\n".getBytes());
        } else {
          out.write(String.format("  new yyActionEntry(%4d,%4d, ",
                  table[j].sp.index,
                  computeAction(lemp, table[j])).getBytes());
          if (collide[j] >= 0) {
            out.write(String.format("%-4d ), /* ",
                    collide[j] + tablecnt).getBytes());
          } else {
            out.write("-1   ), /* ".getBytes());
          }
          printAction(table[j], out, 22);
          out.write(" */\n".getBytes());
        }
        lineno.incrementAndGet();
      }

      /* Update the table count */
      tablecnt += tablesize;
    }
    tpltXfer(lemp.name, br, out, plineno);
    lemp.tablesize = tablecnt;

    for (i = 0; i < lemp.nstate; i++) {
      int tablesize;
      stp = lemp.sorted[i];
      tablesize = 1;
      while (tablesize < stp.naction) tablesize += tablesize;
      out.write(String.format("  new yyStateEntry( %d, %d, %d ),\n",
              stp.tabstart,
              tablesize - 1,
              stp.tabdfltact).getBytes());
      lineno.incrementAndGet();
    }
    tpltXfer(lemp.name, br, out, plineno);

    /* Generate a table containing the symbolic name of every symbol */
    for (i = 0; i < lemp.nsymbol; i++) {
      line = String.format("\"%s\",", lemp.symbols[i].name);
      out.write(String.format("  %-15s", line).getBytes());
      if ((i & 3) == 3) {
        out.write("\n".getBytes());
        lineno.incrementAndGet();
      }
    }
    if ((i & 3) != 0) {
      out.write("\n".getBytes());
      lineno.incrementAndGet();
    }
    // Header
    tpltXfer(lemp.name, br, out, plineno);
    reportHeader(lemp, out);

    tpltXfer(lemp.name, br, out, plineno);

    /* Generate code which executes every time a symbol is popped from
     ** the stack while processing errors or while destroying the parser.
     ** (In other words, generate the %destructor actions) */
    if (lemp.tokendest != null) {
      for (i = 0; i < lemp.nsymbol; i++) {
        Symbol sp = lemp.symbols[i];
        if (sp == null || sp.type != TERMINAL) continue;
        out.write(String.format("    case %d:\n", sp.index).getBytes());
        lineno.incrementAndGet();
      }
      //noinspection ConstantConditions,StatementWithEmptyBody
      for (i = 0; i < lemp.nsymbol && lemp.symbols[i].type != TERMINAL; i++) ;
      if (i < lemp.nsymbol) {
        emitDestructorCode(out, lemp.symbols[i], lemp, plineno);
        out.write("      break;\n".getBytes());
        lineno.incrementAndGet();
      }
    }
    for (i = 0; i < lemp.nsymbol; i++) {
      Symbol sp = lemp.symbols[i];
      if (sp == null || sp.type == TERMINAL || sp.destructor == null) continue;
      out.write(String.format("    case %d:\n", sp.index).getBytes());
      lineno.incrementAndGet();
      emitDestructorCode(out, lemp.symbols[i], lemp, plineno);
      out.write("      break;\n".getBytes());
      lineno.incrementAndGet();
    }
    tpltXfer(lemp.name, br, out, plineno);

    /* Generate code which executes whenever the parser stack overflows */
    tpltPrint(out, lemp, lemp.overflow, lemp.overflowln, plineno);
    tpltXfer(lemp.name, br, out, plineno);

    /* Generate the table of rule information
     **
     ** Note: This code depends on the fact that rules are number
     ** sequentually beginning with 0.
     */
    for (rp = lemp.rule; rp != null; rp = rp.next) {
//      out.write(String.format("  { %d, %d },\n", rp.lhs.index, rp.nrhs).getBytes());
      out.write(String.format("  new yyRuleInfo( %d, %d ),\n", rp.lhs.index, rp.nrhs).getBytes());
      lineno.incrementAndGet();
    }
    tpltXfer(lemp.name, br, out, plineno);

    /* Generate code which execution during each REDUCE action */
    for (rp = lemp.rule; rp != null; rp = rp.next) {
      out.write(String.format("      case %d:\n", rp.index).getBytes());
      lineno.incrementAndGet();
      out.write(String.format("        YYTRACE(\"%s ::=", rp.lhs.name).getBytes());
      for (i = 0; i < rp.nrhs; i++) out.write(String.format(" %s", rp.rhs[i].name).getBytes());
      out.write("\");\n".getBytes());
      lineno.incrementAndGet();
      emitCode(out, rp, lemp, plineno);
      out.write("        break;\n".getBytes());
      lineno.incrementAndGet();
    }
    tpltXfer(lemp.name, br, out, plineno);

    /* Generate code which executes if a parse fails */
    tpltPrint(out, lemp, lemp.failure, lemp.failureln, plineno);
    tpltXfer(lemp.name, br, out, plineno);

    /* Generate code which executes when a syntax error occurs */
    tpltPrint(out, lemp, lemp.error, lemp.errorln, plineno);
    tpltXfer(lemp.name, br, out, plineno);

    /* Generate code which executes when the parser accepts its input */
    tpltPrint(out, lemp, lemp.accept, lemp.acceptln, plineno);
    tpltXfer(lemp.name, br, out, plineno);

    /* Append any addition code the user desires */
    tpltPrint(out, lemp, lemp.extracode, lemp.extracodeln, plineno);

    in.close();
    String outs = new String(out.toByteArray());
    out0.write(Replacer.doReplace(outs).getBytes());
    out0.close();
    out.close();
  }

  public static void reportHeader(Lemon lemp) throws IOException {
    reportHeader(lemp, fileOpenWrite(lemp, ".h"));
  }

  public static void reportHeader(Lemon lemp, OutputStream out) throws IOException {
    String prefix;
    String line;
    String pattern;
    int i = 0;

    if (lemp.tokenprefix != null) prefix = lemp.tokenprefix;
    else prefix = "";
    FileInputStream in = fileOpenRead(lemp, ".h");
    if (in != null) {
      BufferedReader br = new BufferedReader(new InputStreamReader(in));
      for (i = 1; i < lemp.nterminal && (line = br.readLine()) != null; i++) {
//        pattern = String.format("#define %s%-30s %2d\n", prefix, lemp.symbols[i].name, i);
        pattern = String.format("public static final int %s%-30s = %2d;\n", prefix, lemp.symbols[i].name, i);
        if (strcmp(line, pattern) != 0) break;
      }
      in.close();
      if (i == lemp.nterminal) {
        /* No change in the file.  Don't rewrite it. */
        return;
      }
    }
//    FileOutputStream out = fileOpenWrite(lemp, ".h");
    for (i = 1; i < lemp.nterminal; i++) {
      out.write(String.format("public static final int  %s%-30s = %2d;\n", prefix, lemp.symbols[i].name, i).getBytes());
    }
    out.close();
  }

  public static void compressTables(Lemon lemp) {
    State stp;
    Action ap;
    Rule rp;
    int i;
    int cnt;

    for (i = 0; i < lemp.nstate; i++) {
      stp = lemp.sorted[i];

      /* Find the first REDUCE action */
      for (ap = stp.ap; ap != null && ap.type != REDUCE; ap = ap.next) ;
      if (ap == null) continue;

      /* Remember the rule used */
      rp = ap.x.rp;

      /* See if all other REDUCE acitons use the same rule */
      cnt = 1;
      for (ap = ap.next; ap != null; ap = ap.next) {
        if (ap.type == REDUCE) {
          if (ap.x.rp != rp) break;
          cnt++;
        }
      }
      if (ap != null || cnt == 1) continue;

      /* Combine all REDUCE actions into a single default */
      for (ap = stp.ap; ap != null && ap.type != REDUCE; ap = ap.next) ;
      assert (ap != null);
      ap.sp = Symbol.new_("{default}");
      for (ap = ap.next; ap != null; ap = ap.next) {
        if (ap.type == REDUCE) ap.type = NOT_USED;
      }
      stp.ap = Action.sort(stp.ap);
    }
  }
}
