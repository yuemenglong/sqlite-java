package io.github.yuemenglong.sqlite.lemon;

public class Action {
    public static class ActionUnionX {
        private final Object value;

        public ActionUnionX(Object v) {
            this.value = v;
        }

        public State getState() {
            return (State) value;
        }

        public Rule getRule() {
            return (Rule) value;
        }
    }

    public enum ActionType {
        SHIFT,
        ACCEPT,
        REDUCE,
        ERROR,
        CONFLICT,                /* Was a reduce, but part of a conflict */
        SH_RESOLVED,             /* Was a shift.  Precedence resolved conflict */
        RD_RESOLVED,             /* Was reduce.  Precedence resolved conflict */
        NOT_USED                 /* Deleted by compression */
    }

    public Symbol sp;       /* The look-ahead symbol */
    public ActionType type;
    public ActionUnionX x;
    public Action next;     /* Next action for this state */
    public Action collide;  /* Next action with the same hash */

    private static Action[] freelists;
    private static Action freelist;

    public static Action actionNew() {
        Action new_;
        if (freelists == null) {
            int i;
            int amt = 100;
            freelists = new Action[amt];
            for (i = 0; i < amt; i++) {
                freelists[i] = new Action();
            }
            for (i = 0; i < amt - 1; i++) {
                freelists[i].next = freelists[i + 1];
            }
            freelists[amt - 1].next = null;
            freelist = freelists[0];
        }
        new_ = freelist;
        freelist = freelist.next;
        return new_;
    }

    public static void main(String[] args) {
        actionNew();
    }
}
