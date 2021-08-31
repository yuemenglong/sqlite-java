package io.github.yuemenglong.sqlite.lemon;

public class Symbol {
    public enum SymbolType {
        TERMINAL,
        NONTERMINAL
    }

    public enum SymbleEAssoc {
        LEFT,
        RIGHT,
        NONE,
        UNK
    }

    public String name;
    public int index;
    public SymbolType type;
    public Rule rule;
    int prec;
    SymbleEAssoc assoc;
    String firstset;          /* First-set for all rules of this symbol */
    boolean lambda;          /* True if NT and can generate an empty string */
    String destructor;        /* Code which executes whenever this symbol is
     ** popped from the stack during error processing */
    int destructorln;        /* Line number of destructor code */
    String datatype;          /* The data type of information held by this
     ** object. Only used if type==NONTERMINAL */
    int dtnum;               /* The data type number.  In the parser, the value
     ** stack is a union.  The .yy%d element of this
     ** union is the correct data type for this object */
}
