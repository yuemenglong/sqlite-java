prog ::= expr. {System.out.println("1");}
expr ::= N.    {System.out.println("2");}
expr ::= expr PLUS N.   {System.out.println("3");}
