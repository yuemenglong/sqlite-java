/**************** From the file "main.c" ************************************/
/*
** Main program file for the LEMON parser generator.
*/

/* Report an out-of-memory condition and abort.  This function
** is used mostly by the "MemoryCheck" macro in struct.h
*/
void memory_error(){
  fprintf(stderr,"Out of memory.  Aborting...\n");
  exit(1);
}


/* The main program.  Parse the command line and do it... */
int main(argc,argv)
int argc;
char **argv;
{
  static int version = 0;
  static int rpflag = 0;
  static int basisflag = 0;
  static int compress = 0;
  static int quiet = 0;
  static int statistics = 0;
  static int mhflag = 0;
  static struct s_options options[] = {
    {OPT_FLAG, "b", (char*)&basisflag, "Print only the basis in report."},
    {OPT_FLAG, "c", (char*)&compress, "Don't compress the action table."},
    {OPT_FLAG, "g", (char*)&rpflag, "Print grammar without actions."},
    {OPT_FLAG, "m", (char*)&mhflag, "Output a makeheaders compatible file"},
    {OPT_FLAG, "q", (char*)&quiet, "(Quiet) Don't print the report file."},
    {OPT_FLAG, "s", (char*)&statistics, "Print parser stats to standard output."},
    {OPT_FLAG, "x", (char*)&version, "Print the version number."},
    {OPT_FLAG,0,0,0}
  };
  int i;
  struct lemon lem;

  OptInit(argv,options,stderr);
  if( version ){
     printf("Lemon version 1.0\n"
       "Copyright 1991-1997 by D. Richard Hipp\n"
       "Freely distributable under the GNU Public License.\n"
     );
     exit(0); 
  }
  if( OptNArgs()!=1 ){
    fprintf(stderr,"Exactly one filename argument is required.\n");
    exit(1);
  }
  lem.errorcnt = 0;

  /* Initialize the machine */
  Strsafe_init();
  Symbol_init();
  State_init();
  lem.argv0 = argv[0];
  lem.filename = OptArg(0);
  lem.basisflag = basisflag;
  lem.nconflict = 0;
  lem.name = lem.include = lem.arg = lem.tokentype = lem.start = 0;
  lem.stacksize = 0;
  lem.error = lem.overflow = lem.failure = lem.accept = lem.tokendest =
     lem.tokenprefix = lem.outname = lem.extracode = 0;
  lem.tablesize = 0;
  Symbol_new("$");
  lem.errsym = Symbol_new("error");

  /* Parse the input file */
  Parse(&lem);
  if( lem.errorcnt ) exit(lem.errorcnt);
  if( lem.rule==0 ){
    fprintf(stderr,"Empty grammar.\n");
    exit(1);
  }

  /* Count and index the symbols of the grammar */
  lem.nsymbol = Symbol_count();
  Symbol_new("{default}");
  lem.symbols = Symbol_arrayof();
  qsort(lem.symbols,lem.nsymbol+1,sizeof(struct symbol*),
        (int(*)())Symbolcmpp);
  for(i=0; i<=lem.nsymbol; i++) lem.symbols[i]->index = i;
  for(i=1; isupper(lem.symbols[i]->name[0]); i++);
  lem.nterminal = i;

  /* Generate a reprint of the grammar, if requested on the command line */
  if( rpflag ){
    Reprint(&lem);
  }else{
    /* Initialize the size for all follow and first sets */
    SetSize(lem.nterminal);

    /* Find the precedence for every production rule (that has one) */
    FindRulePrecedences(&lem);

    /* Compute the lambda-nonterminals and the first-sets for every
    ** nonterminal */
    FindFirstSets(&lem);

    /* Compute all LR(0) states.  Also record follow-set propagation
    ** links so that the follow-set can be computed later */
    lem.nstate = 0;
    FindStates(&lem);
    lem.sorted = State_arrayof();

    /* Tie up loose ends on the propagation links */
    FindLinks(&lem);

    /* Compute the follow set of every reducible configuration */
    FindFollowSets(&lem);

    /* Compute the action tables */
    FindActions(&lem);

    /* Compress the action tables */
    if( compress==0 ) CompressTables(&lem);

    /* Generate a report of the parser generated.  (the "y.output" file) */
    if( !quiet ) ReportOutput(&lem);

    /* Generate the source code for the parser */
    ReportTable(&lem, mhflag);

    /* Produce a header file for use by the scanner.  (This step is
    ** omitted if the "-m" option is used because makeheaders will
    ** generate the file for us.) */
    if( !mhflag ) ReportHeader(&lem);
  }
  if( statistics ){
    printf("Parser statistics: %d terminals, %d nonterminals, %d rules\n",
      lem.nterminal, lem.nsymbol - lem.nterminal, lem.nrule);
    printf("                   %d states, %d parser table entries, %d conflicts\n",
      lem.nstate, lem.tablesize, lem.nconflict);
  }
  if( lem.nconflict ){
    fprintf(stderr,"%d parsing conflicts.\n",lem.nconflict);
  }
  exit(lem.errorcnt + lem.nconflict);
}
