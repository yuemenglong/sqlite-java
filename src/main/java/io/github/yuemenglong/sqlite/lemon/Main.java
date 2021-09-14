package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.Addr;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.yuemenglong.sqlite.lemon.Options.Type.OPT_FLAG;
import static io.github.yuemenglong.sqlite.common.Util.isupper;
import static java.lang.System.exit;

public class Main {


  /* The main program.  Parse the command line and do it... */
  public static void main(String[] argv) throws IOException {
    AtomicInteger version = new AtomicInteger();
    AtomicInteger rpflag = new AtomicInteger();
    AtomicInteger basisflag = new AtomicInteger();
    AtomicInteger compress = new AtomicInteger();
    AtomicInteger quiet = new AtomicInteger();
    AtomicInteger statistics = new AtomicInteger();
    AtomicInteger mhflag = new AtomicInteger();
    Options[] options = new Options[]{
            new Options(OPT_FLAG, "b", new Addr<>(basisflag::get, v -> basisflag.set((int) v)), "Print only the basis in report."),
            new Options(OPT_FLAG, "c", new Addr<>(compress::get, v -> compress.set((int) v)), "Don't compress the action table."),
            new Options(OPT_FLAG, "g", new Addr<>(rpflag::get, v -> rpflag.set((int) v)), "Print grammar without actions."),
            new Options(OPT_FLAG, "m", new Addr<>(mhflag::get, v -> mhflag.set((int) v)), "Output a makeheaders compatible file"),
            new Options(OPT_FLAG, "q", new Addr<>(quiet::get, v -> quiet.set((int) v)), "(Quiet) Don't print the report file."),
            new Options(OPT_FLAG, "s", new Addr<>(statistics::get, v -> statistics.set((int) v)), "Print parser stats to standard output."),
            new Options(OPT_FLAG, "x", new Addr<>(version::get, v -> version.set((int) v)), "Print the version number."),
            new Options(OPT_FLAG, null, null, null)
    };
    int i;
    Lemon lem = new Lemon();

    Options.optInit(argv, options, System.err);
    if (version.get() != 0) {
      System.out.print("Lemon version 1.0\n" +
              "Copyright 1991-1997 by D. Richard Hipp\n" +
              "Freely distributable under the GNU Public License.\n"
      );
      exit(0);
    }
    if (Options.optNArgs() != 1) {
      System.err.println("Exactly one filename argument is required.\n");
      exit(1);
    }
    lem.errorcnt = 0;

    /* Initialize the machine */
    Strsafe.init();
    Symbol.init();
    State.init();
    lem.argv0 = argv[0];
    lem.filename = Options.optArg(0);
    lem.basisflag = basisflag.get();
    lem.nconflict = 0;
    lem.name = null;
    lem.include = null;
    lem.arg = null;
    lem.tokentype = null;
    lem.start = null;
    lem.stacksize = null;
    lem.error = null;
    lem.overflow = null;
    lem.failure = null;
    lem.accept = null;
    lem.tokendest = null;
    lem.tokenprefix = null;
    lem.outname = null;
    lem.extracode = null;
    lem.tablesize = 0;
    Symbol.new_("$");
    lem.errsym = Symbol.new_("error");

    /* Parse the input file */
    Parse.parse(lem);
    if (lem.errorcnt != 0) exit(lem.errorcnt);
    if (lem.rule == null) {
      System.err.println("Empty grammar.\n");
      exit(1);
    }

    /* Count and index the symbols of the grammar */
    lem.nsymbol = Symbol.count();
    Symbol.new_("{default}");
    lem.symbols = Symbol.arrayOf();
    assert lem.symbols != null;
    Arrays.sort(lem.symbols, Comparator.comparing(o -> o.name));
    for (i = 0; i <= lem.nsymbol; i++) lem.symbols[i].index = i;
    for (i = 1; isupper((byte) lem.symbols[i].name.charAt(0)); i++) ;
    lem.nterminal = i;

    /* Generate a reprint of the grammar, if requested on the command line */
    if (rpflag.get() != 0) {
      Report.Reprint(lem);
    } else {
      /* Initialize the size for all follow and first sets */
      Set.size(lem.nterminal);

      /* Find the precedence for every production rule (that has one) */
      Build.findRulePrecedences(lem);

      /* Compute the lambda-nonterminals and the first-sets for every
       ** nonterminal */
      Build.findFirstSets(lem);

      /* Compute all LR(0) states.  Also record follow-set propagation
       ** links so that the follow-set can be computed later */
      lem.nstate = 0;
      Build.findStates(lem);
      lem.sorted = State.arrayOf();

      /* Tie up loose ends on the propagation links */
      Build.findLinks(lem);

      /* Compute the follow set of every reducible configuration */
      Build.findFollowSets(lem);

      /* Compute the action tables */
      Build.findActions(lem);

      /* Compress the action tables */
      if (compress.get() == 0) Report.compressTables(lem);

      /* Generate a report of the parser generated.  (the "y.output" file) */
      if (quiet.get() == 0) Report.reportOutput(lem);

      /* Generate the source code for the parser */
      Report.reportTable(lem, mhflag.get());

      /* Produce a header file for use by the scanner.  (This step is
       ** omitted if the "-m" option is used because makeheaders will
       ** generate the file for us.) */
      if (mhflag.get() == 0) Report.reportHeader(lem);
    }
    if (statistics.get() != 0) {
      System.out.printf("Parser statistics: %d terminals, %d nonterminals, %d rules\n",
              lem.nterminal, lem.nsymbol - lem.nterminal, lem.nrule);
      System.out.printf("                   %d states, %d parser table entries, %d conflicts\n",
              lem.nstate, lem.tablesize, lem.nconflict);
    }
    if (lem.nconflict != 0) {
      System.err.printf("%d parsing conflicts.\n", lem.nconflict);
    }
//    exit(lem.errorcnt + lem.nconflict);
  }
}