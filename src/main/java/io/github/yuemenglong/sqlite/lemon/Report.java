package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.util.Addr;
import io.github.yuemenglong.sqlite.util.Assert;

import java.io.*;

import static io.github.yuemenglong.sqlite.lemon.Common.MAXRHS;
import static io.github.yuemenglong.sqlite.util.Util.*;

public class Report {
  public static String fileMakeName(Lemon lemp, String suffix) {
    String name = lemp.filename;
    name = name.substring(0, name.indexOf('.'));
    return String.format("%s.%s", name, suffix);
  }

  public static FileInputStream fileOpenRead(Lemon lemp, String suffix) throws FileNotFoundException {
    return new FileInputStream(fileMakeName(lemp, suffix));
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
    FileOutputStream fp;

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

  public static void tpltXfer(String name, FileInputStream in, FileOutputStream out, Addr<Integer> lineno) throws IOException {
    int i, iStart;
    String line;
    BufferedReader br = new BufferedReader(new InputStreamReader(in));
    while ((line = br.readLine()) != null && (line.charAt(0) != '%' || line.charAt(1) != '%')) {
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
    }
  }

  public static InputStream tpltOpen(Lemon lemp) throws FileNotFoundException {
    String templatename = "lempar.c";
    String buf;
    String tpltname;
    int cp = strrchr(lemp.filename, '.');
    if (cp != 0) {
      buf = String.format("%s.lt", lemp.filename.substring(0, cp));
    } else {
      buf = String.format("%s.lt", lemp.filename);
    }
    if (access(buf, 004) == 0) {
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
    out.write(String.format("#line %d \"%s\"\n", strln, lemp.filename).getBytes());
    int p = 0;
    while (p < str.length()) {
      if (str.charAt(p) == '\n') lineno.set(lineno.get() + 1);
      out.write(str.charAt(p));
      p += 1;
    }
    out.write(String.format("\n#line %d \"%s\"\n", lineno.get() + 2, lemp.outname).getBytes());
  }

  public static void emitDestructorCode(OutputStream out, Symbol sp, Lemon lemp, Addr<Integer> lineno) throws IOException {
    int cp;
    int linecnt = 0;
    byte[] b;
    if (sp.type == Symbol.SymbolType.TERMINAL) {
      b = lemp.tokendest == null ? null : lemp.tokendest.getBytes();
      if (b == null) return;
      out.write(String.format("#line %d \"%s\"\n{", lemp.tokendestln, lemp.filename).getBytes());
    } else {
      b = sp.destructor == null ? null : sp.destructor.getBytes();
      if (b == null) return;
      out.write(String.format("#line %d \"%s\"\n{", sp.destructorln, lemp.filename).getBytes());
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
    out.write(String.format("}\n#line %d \"%s\"\n", lineno.get(), lemp.outname).getBytes());
  }

  public static int hasDestructor(Symbol sp, Lemon lemp) {
    boolean ret;
    if (sp.type == Symbol.SymbolType.TERMINAL) {
      ret = lemp.tokendest != null;
    } else {
      ret = sp.destructor != null;
    }
    return ret ? 1 : 0;
  }

  public static void emitCode(OutputStream out, Rule rp, Lemon lemp, Addr<Integer> lineno) throws IOException {
    int cp, xp;
    int linecnt = 0;
    int i;
    byte lhsused = 0;
    byte[] used = new byte[MAXRHS];
    for (i = 0; i < rp.nrhs; i++) used[i] = 0;
    lhsused = 0;
    if (rp.code != null) {
      out.write(String.format("#line %d \"%s\"\n{", rp.line, lemp.filename).getBytes());
      String s = rp.code;
      byte[] b = s.getBytes();
      for (cp = 0; cp < b.length; cp++) {
        if (isalpha(b[cp]) && (cp == 0 || !isalnum(b[cp - 1]))) {
          int saved = 0;
          for (xp = cp + 1; isalnum(b[xp]); xp++) ;
          saved = xp;
          if (rp.lhsalias != null && strcmp(s.substring(cp), rp.lhsalias) == 0) {
            out.write(String.format("yygotominor.yy%d", rp.lhs.dtnum).getBytes());
            cp = xp;
            lhsused = 1;
          } else {
            for (i = 0; i < rp.nrhs; i++) {
              if (rp.rhsalias[i] != null && strcmp(s.substring(cp), rp.rhsalias[i]) == 0) {
                out.write(String.format("yymsp[%d].minor.yy%d", i - rp.nrhs + 1, rp.rhs[i].dtnum).getBytes());
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
      out.write(String.format("}\n#line %d \"%s\"\n", lineno.get(), lemp.outname).getBytes());
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
  //
  ///*
  //** Print the definition of the union used for the parser's data stack.
  //** This union contains fields for every possible data type for tokens
  //** and nonterminals.  In the process of computing and printing this
  //** union, also set the ".dtnum" field of every terminal and nonterminal
  //** symbol.
  //*/
  //void print_stack_union(out,lemp,plineno,mhflag)
  //FILE *out;                  /* The output stream */
  //struct lemon *lemp;         /* The main info structure for this parser */
  //int *plineno;               /* Pointer to the line number */
  //int mhflag;                 /* True if generating makeheaders output */
  //{
  //  int lineno = *plineno;    /* The line number of the output */
  //  char **types;             /* A hash table of datatypes */
  //  int arraysize;            /* Size of the "types" array */
  //  int maxdtlength;          /* Maximum length of any ".datatype" field. */
  //  char *stddt;              /* Standardized name for a datatype */
  //  int i,j;                  /* Loop counters */
  //  int hash;                 /* For hashing the name of a type */
  //  char *name;               /* Name of the parser */
  //
  //  /* Allocate and initialize types[] and allocate stddt[] */
  //  arraysize = lemp.nsymbol * 2;
  //  types = (char**)malloc( arraysize * sizeof(char*) );
  //  for(i=0; i<arraysize; i++) types[i] = 0;
  //  maxdtlength = 0;
  //  for(i=0; i<lemp.nsymbol; i++){
  //    int len;
  //    struct symbol *sp = lemp.symbols[i];
  //    if( sp.datatype==0 ) continue;
  //    len = strlen(sp.datatype);
  //    if( len>maxdtlength ) maxdtlength = len;
  //  }
  //  stddt = (char*)malloc( maxdtlength*2 + 1 );
  //  if( types==0 || stddt==0 ){
  //    fprintf(stderr,"Out of memory.\n");
  //    exit(1);
  //  }
  //
  //  /* Build a hash table of datatypes. The ".dtnum" field of each symbol
  //  ** is filled in with the hash index plus 1.  A ".dtnum" value of 0 is
  //  ** used for terminal symbols and for nonterminals which don't specify
  //  ** a datatype using the %type directive. */
  //  for(i=0; i<lemp.nsymbol; i++){
  //    struct symbol *sp = lemp.symbols[i];
  //    char *cp;
  //    if( sp==lemp.errsym ){
  //      sp.dtnum = arraysize+1;
  //      continue;
  //    }
  //    if( sp.type!=NONTERMINAL || sp.datatype==0 ){
  //      sp.dtnum = 0;
  //      continue;
  //    }
  //    cp = sp.datatype;
  //    j = 0;
  //    while( isspace(*cp) ) cp++;
  //    while( *cp ) stddt[j++] = *cp++;
  //    while( j>0 && isspace(stddt[j-1]) ) j--;
  //    stddt[j] = 0;
  //    hash = 0;
  //    for(j=0; stddt[j]; j++){
  //      hash = hash*53 + stddt[j];
  //    }
  //    if( hash<0 ) hash = -hash;
  //    hash = hash%arraysize;
  //    while( types[hash] ){
  //      if( strcmp(types[hash],stddt)==0 ){
  //        sp.dtnum = hash + 1;
  //        break;
  //      }
  //      hash++;
  //      if( hash>=arraysize ) hash = 0;
  //    }
  //    if( types[hash]==0 ){
  //      sp.dtnum = hash + 1;
  //      types[hash] = (char*)malloc( strlen(stddt)+1 );
  //      if( types[hash]==0 ){
  //        fprintf(stderr,"Out of memory.\n");
  //        exit(1);
  //      }
  //      strcpy(types[hash],stddt);
  //    }
  //  }
  //
  //  /* Print out the definition of YYTOKENTYPE and YYMINORTYPE */
  //  name = lemp.name ? lemp.name : "Parse";
  //  lineno = *plineno;
  //  if( mhflag ){ out.write(String.format("#if INTERFACE\n"); lineno++; }
  //  out.write(String.format("#define %sTOKENTYPE %s\n",name,
  //    lemp.tokentype?lemp.tokentype:"void*");  lineno++;
  //  if( mhflag ){ out.write(String.format("#endif\n"); lineno++; }
  //  out.write(String.format("typedef union {\n"); lineno++;
  //  out.write(String.format("  %sTOKENTYPE yy0;\n",name); lineno++;
  //  for(i=0; i<arraysize; i++){
  //    if( types[i]==0 ) continue;
  //    out.write(String.format("  %s yy%d;\n",types[i],i+1); lineno++;
  //    free(types[i]);
  //  }
  //  out.write(String.format("  int yy%d;\n",lemp.errsym.dtnum); lineno++;
  //  free(stddt);
  //  free(types);
  //  out.write(String.format("} YYMINORTYPE;\n"); lineno++;
  //  *plineno = lineno;
  //}
  //
  ///* Generate C source code for the parser */
  //void ReportTable(lemp, mhflag)
  //struct lemon *lemp;
  //int mhflag;     /* Output in makeheaders format if true */
  //{
  //  FILE *out, *in;
  //  char line[LINESIZE];
  //  int  lineno;
  //  struct state *stp;
  //  struct action *ap;
  //  struct rule *rp;
  //  int i;
  //  int tablecnt;
  //  char *name;
  //
  //  in = tplt_open(lemp);
  //  if( in==0 ) return;
  //  out = file_open(lemp,".c","w");
  //  if( out==0 ){
  //    fclose(in);
  //    return;
  //  }
  //  lineno = 1;
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //
  //  /* Generate the include code, if any */
  //  tplt_print(out,lemp,lemp.include,lemp.includeln,&lineno);
  //  if( mhflag ){
  //    char *name = file_makename(lemp, ".h");
  //    out.write(String.format("#include \"%s\"\n", name); lineno++;
  //    free(name);
  //  }
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //
  //  /* Generate #defines for all tokens */
  //  if( mhflag ){
  //    char *prefix;
  //    out.write(String.format("#if INTERFACE\n"); lineno++;
  //    if( lemp.tokenprefix ) prefix = lemp.tokenprefix;
  //    else                    prefix = "";
  //    for(i=1; i<lemp.nterminal; i++){
  //      out.write(String.format("#define %s%-30s %2d\n",prefix,lemp.symbols[i].name,i);
  //      lineno++;
  //    }
  //    out.write(String.format("#endif\n"); lineno++;
  //  }
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //
  //  /* Generate the defines */
  //  out.write(String.format("/* \001 */\n");
  //  out.write(String.format("#define YYCODETYPE %s\n",
  //    lemp.nsymbol>250?"int":"unsigned char");  lineno++;
  //  out.write(String.format("#define YYNOCODE %d\n",lemp.nsymbol+1);  lineno++;
  //  out.write(String.format("#define YYACTIONTYPE %s\n",
  //    lemp.nstate+lemp.nrule>250?"int":"unsigned char");  lineno++;
  //  print_stack_union(out,lemp,&lineno,mhflag);
  //  if( lemp.stacksize ){
  //    if( atoi(lemp.stacksize)<=0 ){
  //      Error.msg(lemp.filename,0,
  //"Illegal stack size: [%s].  The stack size should be an integer constant.",
  //        lemp.stacksize);
  //      lemp.errorcnt++;
  //      lemp.stacksize = "100";
  //    }
  //    out.write(String.format("#define YYSTACKDEPTH %s\n",lemp.stacksize);  lineno++;
  //  }else{
  //    out.write(String.format("#define YYSTACKDEPTH 100\n");  lineno++;
  //  }
  //  if( mhflag ){
  //    out.write(String.format("#if INTERFACE\n"); lineno++;
  //  }
  //  name = lemp.name ? lemp.name : "Parse";
  //  if( lemp.arg && lemp.arg[0] ){
  //    int i;
  //    i = strlen(lemp.arg);
  //    while( i>=1 && isspace(lemp.arg[i-1]) ) i--;
  //    while( i>=1 && (isalnum(lemp.arg[i-1]) || lemp.arg[i-1]=='_') ) i--;
  //    out.write(String.format("#define %sARGDECL ,%s\n",name,&lemp.arg[i]);  lineno++;
  //    out.write(String.format("#define %sXARGDECL %s;\n",name,lemp.arg);  lineno++;
  //    out.write(String.format("#define %sANSIARGDECL ,%s\n",name,lemp.arg);  lineno++;
  //  }else{
  //    out.write(String.format("#define %sARGDECL\n",name);  lineno++;
  //    out.write(String.format("#define %sXARGDECL\n",name);  lineno++;
  //    out.write(String.format("#define %sANSIARGDECL\n",name);  lineno++;
  //  }
  //  if( mhflag ){
  //    out.write(String.format("#endif\n"); lineno++;
  //  }
  //  out.write(String.format("#define YYNSTATE %d\n",lemp.nstate);  lineno++;
  //  out.write(String.format("#define YYNRULE %d\n",lemp.nrule);  lineno++;
  //  out.write(String.format("#define YYERRORSYMBOL %d\n",lemp.errsym.index);  lineno++;
  //  out.write(String.format("#define YYERRSYMDT yy%d\n",lemp.errsym.dtnum);  lineno++;
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //
  //  /* Generate the action table.
  //  **
  //  ** Each entry in the action table is an element of the following
  //  ** structure:
  //  **   struct yyActionEntry {
  //  **       YYCODETYPE            lookahead;
  //  **       YYACTIONTYPE          action;
  //  **       struct yyActionEntry *next;
  //  **   }
  //  **
  //  ** The entries are grouped into hash tables, one hash table for each
  //  ** parser state.  The hash table has a size which is the smallest
  //  ** power of two needed to hold all entries.
  //  */
  //  tablecnt = 0;
  //
  //  /* Loop over parser states */
  //  for(i=0; i<lemp.nstate; i++){
  //    int tablesize;              /* size of the hash table */
  //    int j,k;                    /* Loop counter */
  //    int collide[2048];          /* The collision chain for the table */
  //    struct action *table[2048]; /* Build the hash table here */
  //
  //    /* Find the number of actions and initialize the hash table */
  //    stp = lemp.sorted[i];
  //    stp.tabstart = tablecnt;
  //    stp.naction = 0;
  //    for(ap=stp.ap; ap; ap=ap.next){
  //      if( ap.sp.index!=lemp.nsymbol && compute_action(lemp,ap)>=0 ){
  //        stp.naction++;
  //      }
  //    }
  //    tablesize = 1;
  //    while( tablesize<stp.naction ) tablesize += tablesize;
  //    assert( tablesize<= sizeof(table)/sizeof(table[0]) );
  //    for(j=0; j<tablesize; j++){
  //      table[j] = 0;
  //      collide[j] = -1;
  //    }
  //
  //    /* Hash the actions into the hash table */
  //    stp.tabdfltact = lemp.nstate + lemp.nrule;
  //    for(ap=stp.ap; ap; ap=ap.next){
  //      int action = compute_action(lemp,ap);
  //      int h;
  //      if( ap.sp.index==lemp.nsymbol ){
  //        stp.tabdfltact = action;
  //      }else if( action>=0 ){
  //        h = ap.sp.index & (tablesize-1);
  //        ap.collide = table[h];
  //        table[h] = ap;
  //      }
  //    }
  //
  //    /* Resolve collisions */
  //    for(j=k=0; j<tablesize; j++){
  //      if( table[j] && table[j].collide ){
  //        while( table[k] ) k++;
  //        table[k] = table[j].collide;
  //        collide[j] = k;
  //        table[j].collide = 0;
  //        if( k<j ) j = k-1;
  //      }
  //    }
  //
  //    /* Print the hash table */
  //    out.write(String.format("/* State %d */\n",stp.index); lineno++;
  //    for(j=0; j<tablesize; j++){
  //      if( table[j]==0 ){
  //        out.write(String.format(
  //          "  {YYNOCODE,0,0}, /* Unused */\n");
  //      }else{
  //        out.write(String.format("  {%4d,%4d, ",
  //          table[j].sp.index,
  //          compute_action(lemp,table[j]));
  //        if( collide[j]>=0 ){
  //          out.write(String.format("&yyActionTable[%4d] }, /* ",
  //            collide[j] + tablecnt);
  //        }else{
  //          out.write(String.format("0                    }, /* ");
  //        }
  //        PrintAction(table[j],out,22);
  //        out.write(String.format(" */\n");
  //      }
  //      lineno++;
  //    }
  //
  //    /* Update the table count */
  //    tablecnt += tablesize;
  //  }
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //  lemp.tablesize = tablecnt;
  //
  //  /* Generate the state table
  //  **
  //  ** Each entry is an element of the following structure:
  //  **    struct yyStateEntry {
  //  **      struct yyActionEntry *hashtbl;
  //  **      int mask;
  //  **      YYACTIONTYPE actionDefault;
  //  **    }
  //  */
  //  for(i=0; i<lemp.nstate; i++){
  //    int tablesize;
  //    stp = lemp.sorted[i];
  //    tablesize = 1;
  //    while( tablesize<stp.naction ) tablesize += tablesize;
  //    out.write(String.format("  { &yyActionTable[%d], %d, %d},\n",
  //      stp.tabstart,
  //      tablesize - 1,
  //      stp.tabdfltact); lineno++;
  //  }
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //
  //  /* Generate a table containing the symbolic name of every symbol */
  //  for(i=0; i<lemp.nsymbol; i++){
  //    sprintf(line,"\"%s\",",lemp.symbols[i].name);
  //    out.write(String.format("  %-15s",line);
  //    if( (i&3)==3 ){ out.write(String.format("\n"); lineno++; }
  //  }
  //  if( (i&3)!=0 ){ out.write(String.format("\n"); lineno++; }
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //
  //  /* Generate code which executes every time a symbol is popped from
  //  ** the stack while processing errors or while destroying the parser.
  //  ** (In other words, generate the %destructor actions) */
  //  if( lemp.tokendest ){
  //    for(i=0; i<lemp.nsymbol; i++){
  //      struct symbol *sp = lemp.symbols[i];
  //      if( sp==0 || sp.type!=TERMINAL ) continue;
  //      out.write(String.format("    case %d:\n",sp.index); lineno++;
  //    }
  //    for(i=0; i<lemp.nsymbol && lemp.symbols[i].type!=TERMINAL; i++);
  //    if( i<lemp.nsymbol ){
  //      emit_destructor_code(out,lemp.symbols[i],lemp,&lineno);
  //      out.write(String.format("      break;\n"); lineno++;
  //    }
  //  }
  //  for(i=0; i<lemp.nsymbol; i++){
  //    struct symbol *sp = lemp.symbols[i];
  //    if( sp==0 || sp.type==TERMINAL || sp.destructor==0 ) continue;
  //    out.write(String.format("    case %d:\n",sp.index); lineno++;
  //    emit_destructor_code(out,lemp.symbols[i],lemp,&lineno);
  //    out.write(String.format("      break;\n"); lineno++;
  //  }
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //
  //  /* Generate code which executes whenever the parser stack overflows */
  //  tplt_print(out,lemp,lemp.overflow,lemp.overflowln,&lineno);
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //
  //  /* Generate the table of rule information
  //  **
  //  ** Note: This code depends on the fact that rules are number
  //  ** sequentually beginning with 0.
  //  */
  //  for(rp=lemp.rule; rp; rp=rp.next){
  //    out.write(String.format("  { %d, %d },\n",rp.lhs.index,rp.nrhs); lineno++;
  //  }
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //
  //  /* Generate code which execution during each REDUCE action */
  //  for(rp=lemp.rule; rp; rp=rp.next){
  //    out.write(String.format("      case %d:\n",rp.index); lineno++;
  //    out.write(String.format("        YYTRACE(\"%s ::=",rp.lhs.name);
  //    for(i=0; i<rp.nrhs; i++) out.write(String.format(" %s",rp.rhs[i].name);
  //    out.write(String.format("\")\n"); lineno++;
  //    emit_code(out,rp,lemp,&lineno);
  //    out.write(String.format("        break;\n"); lineno++;
  //  }
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //
  //  /* Generate code which executes if a parse fails */
  //  tplt_print(out,lemp,lemp.failure,lemp.failureln,&lineno);
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //
  //  /* Generate code which executes when a syntax error occurs */
  //  tplt_print(out,lemp,lemp.error,lemp.errorln,&lineno);
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //
  //  /* Generate code which executes when the parser accepts its input */
  //  tplt_print(out,lemp,lemp.accept,lemp.acceptln,&lineno);
  //  tplt_xfer(lemp.name,in,out,&lineno);
  //
  //  /* Append any addition code the user desires */
  //  tplt_print(out,lemp,lemp.extracode,lemp.extracodeln,&lineno);
  //
  //  fclose(in);
  //  fclose(out);
  //  return;
  //}
  //
  ///* Generate a header file for the parser */
  //void ReportHeader(lemp)
  //struct lemon *lemp;
  //{
  //  FILE *out, *in;
  //  char *prefix;
  //  char line[LINESIZE];
  //  char pattern[LINESIZE];
  //  int i;
  //
  //  if( lemp.tokenprefix ) prefix = lemp.tokenprefix;
  //  else                    prefix = "";
  //  in = file_open(lemp,".h","r");
  //  if( in ){
  //    for(i=1; i<lemp.nterminal && fgets(line,LINESIZE,in); i++){
  //      sprintf(pattern,"#define %s%-30s %2d\n",prefix,lemp.symbols[i].name,i);
  //      if( strcmp(line,pattern) ) break;
  //    }
  //    fclose(in);
  //    if( i==lemp.nterminal ){
  //      /* No change in the file.  Don't rewrite it. */
  //      return;
  //    }
  //  }
  //  out = file_open(lemp,".h","w");
  //  if( out ){
  //    for(i=1; i<lemp.nterminal; i++){
  //      out.write(String.format("#define %s%-30s %2d\n",prefix,lemp.symbols[i].name,i);
  //    }
  //    fclose(out);
  //  }
  //  return;
  //}
  //
  ///* Reduce the size of the action tables, if possible, by making use
  //** of defaults.
  //**
  //** In this version, if all REDUCE actions use the same rule, make
  //** them the default.  Only default them if there are more than one.
  //*/
  //void CompressTables(lemp)
  //struct lemon *lemp;
  //{
  //  struct state *stp;
  //  struct action *ap;
  //  struct rule *rp;
  //  int i;
  //  int cnt;
  //
  //  for(i=0; i<lemp.nstate; i++){
  //    stp = lemp.sorted[i];
  //
  //    /* Find the first REDUCE action */
  //    for(ap=stp.ap; ap && ap.type!=REDUCE; ap=ap.next);
  //    if( ap==0 ) continue;
  //
  //    /* Remember the rule used */
  //    rp = ap.x.rp;
  //
  //    /* See if all other REDUCE acitons use the same rule */
  //    cnt = 1;
  //    for(ap=ap.next; ap; ap=ap.next){
  //      if( ap.type==REDUCE ){
  //        if( ap.x.rp!=rp ) break;
  //        cnt++;
  //      }
  //    }
  //    if( ap || cnt==1 ) continue;
  //
  //    /* Combine all REDUCE actions into a single default */
  //    for(ap=stp.ap; ap && ap.type!=REDUCE; ap=ap.next);
  //    assert( ap );
  //    ap.sp = Symbol_new("{default}");
  //    for(ap=ap.next; ap; ap=ap.next){
  //      if( ap.type==REDUCE ) ap.type = NOT_USED;
  //    }
  //    stp.ap = Action_sort(stp.ap);
  //  }
  //}
}
