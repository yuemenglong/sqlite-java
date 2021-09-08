package io.github.yuemenglong.sqlite.lemon;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Rule {
  public Symbol lhs;      /* Left-hand side of the rule */
  public String lhsalias;          /* Alias for the LHS (NULL if none) */
  public int ruleline;            /* Line number for the rule */
  public int nrhs;                /* Number of RHS symbols */
  public Symbol[] rhs;     /* The RHS symbols */
  public String[] rhsalias;         /* An alias for each RHS symbol (NULL if none) */
  public int line;                /* Line number at which code begins */
  public String code;              /* The code executed when this rule is reduced */
  public Symbol precsym;  /* Precedence symbol for this rule */
  public int index;               /* An index number for this rule */
  public Boolean canReduce;       /* True if this rule is ever reduced */
  public Rule nextlhs;    /* Next rule with the same LHS */
  public Rule next;       /* Next rule in the global list */

  @Override
  public String toString() {
    String right = Arrays.stream(rhs).map(String::valueOf).collect(Collectors.joining(" "));
    return String.format("%s ::= %s.", lhs, right);
  }
}
