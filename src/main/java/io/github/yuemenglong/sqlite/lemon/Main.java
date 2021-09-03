package io.github.yuemenglong.sqlite.lemon;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import static io.github.yuemenglong.sqlite.util.Util.isupper;

public class Main {
  @SuppressWarnings("StatementWithEmptyBody")
  public static void main(String[] args) throws IOException {
    Lemon lem = new Lemon();
    lem.errorcnt = 0;
    Strsafe.init();
    Symbol.init();
    State.init();
    lem.filename = "D:/lemon.y";
    Symbol.new_("$");
    lem.errsym = Symbol.new_("error");
    Parse.parse(lem);
    if (lem.errorcnt != 0) System.exit(lem.errorcnt);
    if (lem.rule == null) {
      System.err.println("Empty grammar");
      System.exit(1);
    }
    lem.nsymbol = Symbol.count();
    Symbol.new_("{default}");
    lem.symbols = Symbol.arrayOf();
    assert lem.symbols != null;
    Arrays.sort(lem.symbols, Comparator.comparing(o -> o.name));
    int i;
    for (i = 0; i < lem.symbols.length; i++) lem.symbols[i].index = i;
    for (i = 1; isupper((byte) lem.symbols[i].name.charAt(0)); i++) ;
    lem.nterminal = i;

    Set.size(lem.nterminal);
    Build.findRulePrecedences(lem);
    Build.findFirstSets(lem);
    lem.nstate = 0;
    Build.findState(lem);
    lem.sorted = State.arrayOf();
    Build.findLinks(lem);
    Build.findFollowSets(lem);
    Build.findActions(lem);
    //    /* Compute the action tables */
    //    FindActions(&lem);
    //
    //    /* Compress the action tables */
    //    if( compress==0 ) CompressTables(&lem);
    //
    //    /* Generate a report of the parser generated.  (the "y.output" file) */
    //    if( !quiet ) ReportOutput(&lem);
    //
    //    /* Generate the source code for the parser */
    //    ReportTable(&lem, mhflag);
    //
    //    /* Produce a header file for use by the scanner.  (This step is
    //    ** omitted if the "-m" option is used because makeheaders will
    //    ** generate the file for us.) */
    //    if( !mhflag ) ReportHeader(&lem);
    //  }
    //  if( statistics ){
    //    printf("Parser statistics: %d terminals, %d nonterminals, %d rules\n",
    //      lem.nterminal, lem.nsymbol - lem.nterminal, lem.nrule);
    //    printf("                   %d states, %d parser table entries, %d conflicts\n",
    //      lem.nstate, lem.tablesize, lem.nconflict);
    //  }
    //  if( lem.nconflict ){
    //    fprintf(stderr,"%d parsing conflicts.\n",lem.nconflict);
    //  }
    //  exit(lem.errorcnt + lem.nconflict);
  }
}
