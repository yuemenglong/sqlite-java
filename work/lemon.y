%token_prefix TK_
%extra_argument {Parse pParse}

s(A) ::= e(B).{System.out.println("s ::= e");System.out.println(B);}
e(A) ::= e(B) PLUS t(C).{System.out.println("e ::= e PLUS t");A=(int)(B)+(int)(C);}
e(A) ::= t(B).{System.out.println("e ::= t");A=B;}
t(A) ::= t(B) MULTI f(C).{System.out.println("t ::= t MULTI f");A=(int)(B)*(int)(C);}
t(A) ::= f(B).{System.out.println("t ::= f");A=B;}
f(A) ::= LB f(B) RB.{System.out.println("f ::= LB f RB");A=(B);}
f(A) ::= N(B).{System.out.println("f ::= N");A=B;}