package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.util.Table;

public class Symbol {
    public enum SymbolType {
        TERMINAL,
        NONTERMINAL
    }

    public enum SymbolAssoc {
        LEFT,
        RIGHT,
        NONE,
        UNK
    }

    public String name;
    public int index;
    public SymbolType type;
    public Rule rule;
    public int prec;
    public SymbolAssoc assoc;
    public byte[] firstset;          /* First-set for all rules of this symbol */
    public boolean lambda;          /* True if NT and can generate an empty string */
    public String destructor;        /* Code which executes whenever this symbol is
     ** popped from the stack during error processing */
    public int destructorln;        /* Line number of destructor code */
    public String datatype;          /* The data type of information held by this
     ** object. Only used if type==NONTERMINAL */
    public int dtnum;               /* The data type number.  In the parser, the value
     ** stack is a union.  The .yy%d element of this
     ** union is the correct data type for this object */

    private static Table<String, Symbol> x2a;

    public static void init() {
        if (x2a != null) return;
        x2a = new Table<>();
        x2a.init(128);
    }

    public static int insert(Symbol data, String key) {
        if (x2a == null) return 0;
        return x2a.insert(key, data);
    }

    public static Symbol find(String key) {
        if (x2a == null) return null;
        return x2a.find(key);
    }

    public static Symbol nth(int n) {
        if (x2a == null) return null;
        return x2a.nth(n);
    }

    public static int count() {
        if (x2a == null) return 0;
        return x2a.count();
    }

    public static Symbol[] arrayOf() {
        if (x2a == null) return null;
        return x2a.arrayOf(Symbol.class);
    }

}
