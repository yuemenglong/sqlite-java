s ::= e.{System.out.println("s ::= e");}
e ::= e PLUS t.{System.out.println("e ::= e PLUS t");}
e ::= t.{System.out.println("e ::= t");}
t ::= t MULTI f.{System.out.println("t ::= t MULTI f");}
t ::= f.{System.out.println("t ::= f");}
f ::= LB f RB.{System.out.println("f ::= LB f RB");}
f ::= N.{System.out.println("f ::= N");}