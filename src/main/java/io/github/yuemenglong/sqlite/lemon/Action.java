package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.INode;
import io.github.yuemenglong.sqlite.util.Assert;

import static io.github.yuemenglong.sqlite.lemon.Action.ActionType.REDUCE;

public class Action implements INode<Action> {
    public int compareTo(Action o) {
        return actionCmp(this, o);
    }

    public Action getNext() {
        return next;
    }

    public void setNext(Action v) {
        next = v;
    }

    public static class ActionUnionX {
        private final Object value;

        public ActionUnionX(Object v) {
            this.value = v;
        }

        public State stp() {
            return (State) value;
        }

        public Rule rp() {
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

    // Action_new
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

    // actioncmp
    public static int actionCmp(Action ap1, Action ap2) {
        int rc;
        rc = ap1.sp.index - ap2.sp.index;
        if (rc == 0) rc = ap1.type.ordinal() - ap2.type.ordinal();
        if (rc == 0) {
            Assert.assertTrue(ap1.type == REDUCE && ap2.type == REDUCE);
            rc = ap1.x.rp().index - ap2.x.rp().index;
        }
        return rc;
    }

    // Action_sort
    public static Action actionSort(Action ap) {
        return Msort.msort(ap);
    }

    public static void main(String[] args) {
        actionNew();
    }
}
