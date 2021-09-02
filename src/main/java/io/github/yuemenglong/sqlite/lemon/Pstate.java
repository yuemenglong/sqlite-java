package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.util.Addr;

import static io.github.yuemenglong.sqlite.lemon.Common.MAXRHS;

public class Pstate {
  //  enum e_state {
  //    INITIALIZE,
  //    WAITING_FOR_DECL_OR_RULE,
  //    WAITING_FOR_DECL_KEYWORD,
  //    WAITING_FOR_DECL_ARG,
  //    WAITING_FOR_PRECEDENCE_SYMBOL,
  //    WAITING_FOR_ARROW,
  //    IN_RHS,
  //    LHS_ALIAS_1,
  //    LHS_ALIAS_2,
  //    LHS_ALIAS_3,
  //    RHS_ALIAS_1,
  //    RHS_ALIAS_2,
  //    PRECEDENCE_MARK_1,
  //    PRECEDENCE_MARK_2,
  //    RESYNC_AFTER_RULE_ERROR,
  //    RESYNC_AFTER_DECL_ERROR,
  //    WAITING_FOR_DESTRUCTOR_SYMBOL,
  //    WAITING_FOR_DATATYPE_SYMBOL
  //  } state;                   /* The state of the parser */

  public enum State {
    INITIALIZE,
    WAITING_FOR_DECL_OR_RULE,
    WAITING_FOR_DECL_KEYWORD,
    WAITING_FOR_DECL_ARG,
    WAITING_FOR_PRECEDENCE_SYMBOL,
    WAITING_FOR_ARROW,
    IN_RHS,
    LHS_ALIAS_1,
    LHS_ALIAS_2,
    LHS_ALIAS_3,
    RHS_ALIAS_1,
    RHS_ALIAS_2,
    PRECEDENCE_MARK_1,
    PRECEDENCE_MARK_2,
    RESYNC_AFTER_RULE_ERROR,
    RESYNC_AFTER_DECL_ERROR,
    WAITING_FOR_DESTRUCTOR_SYMBOL,
    WAITING_FOR_DATATYPE_SYMBOL
  }

  public byte[] buf;
  public String filename;       /* Name of the input file */
  public int tokenlineno;      /* Linenumber at which current token starts */
  public int errorcnt;         /* Number of errors so far */
  public int tokenstart;     /* Text of current token */
  public int tokenend;     /* Text of current token */
  public Lemon gp;     /* Global state vector */
  public State state;
  public Symbol lhs;        /* Left-hand side of current rule */
  public String lhsalias;            /* Alias for the LHS */
  public int nrhs;                  /* Number of right-hand side symbols seen */
  public Symbol[] rhs = new Symbol[MAXRHS];  /* RHS symbols */
  public String[] alias = new String[MAXRHS];       /* Aliases for each RHS symbol (or NULL) */
  public Rule prevrule;     /* Previous rule parsed */
  public String declkeyword;         /* Keyword of a declaration */
  public Addr<String> declargslot;        /* Where the declaration argument should be put */
  public Addr<Integer> decllnslot;           /* Where the declaration linenumber is put */
  public Symbol.Assoc declassoc;    /* Assign this association to decl arguments */
  public int preccounter;           /* Assign this precedence to decl arguments */
  public Rule firstrule;    /* Pointer to first rule in the grammar */
  public Rule lastrule;     /* Pointer to the most recently parsed rule */
}
