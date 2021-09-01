package io.github.yuemenglong.sqlite.lemon;

public class Lemon {
    public State[] sorted;   /* Table of states sorted by state number */
    public Rule rule;       /* List of all rules */
    public int nstate;              /* Number of states */
    public int nrule;               /* Number of rules */
    public int nsymbol;             /* Number of terminal and nonterminal symbols */
    public int nterminal;           /* Number of terminal symbols */
    public Symbol[] symbols; /* Sorted array of pointers to symbols */
    public int errorcnt;            /* Number of errors */
    public Symbol errsym;   /* The error symbol */
    public String name;              /* Name of the generated parser */
    public String arg;               /* Declaration of the 3th argument to parser */
    public String tokentype;         /* Type of terminal symbols in the parser stack */
    public String start;             /* Name of the start symbol for the grammar */
    public String stacksize;         /* Size of the parser stack */
    public String include;           /* Code to put at the start of the C file */
    public int includeln;          /* Line number for start of include code */
    public String error;             /* Code to execute when an error is seen */
    public int errorln;            /* Line number for start of error code */
    public String overflow;          /* Code to execute on a stack overflow */
    public int overflowln;         /* Line number for start of overflow code */
    public String failure;           /* Code to execute on parser failure */
    public int failureln;          /* Line number for start of failure code */
    public String accept;            /* Code to execute when the parser excepts */
    public int acceptln;           /* Line number for the start of accept code */
    public String extracode;         /* Code appended to the generated file */
    public int extracodeln;        /* Line number for the start of the extra code */
    public String tokendest;         /* Code to execute to destroy token data */
    public int tokendestln;        /* Line number for token destroyer code */
    public String filename;          /* Name of the input file */
    public String outname;           /* Name of the current output file */
    public String tokenprefix;       /* A prefix added to token names in the .h file */
    public int nconflict;           /* Number of parsing conflicts */
    public int tablesize;           /* Size of the parse tables */
    public int basisflag;           /* Print only basis configurations */
    public String argv0;             /* Name of the program */
}
