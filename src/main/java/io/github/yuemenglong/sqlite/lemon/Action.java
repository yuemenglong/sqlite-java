package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.INext;
import io.github.yuemenglong.sqlite.common.INode;
import io.github.yuemenglong.sqlite.util.Assert;

import java.util.function.Supplier;

import static io.github.yuemenglong.sqlite.lemon.Action.ActionType.REDUCE;
import static io.github.yuemenglong.sqlite.lemon.Action.ActionType.SHIFT;

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

    public static class StateOrRule {
        public State stp;
        public Rule rp;
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
    public StateOrRule x = new StateOrRule();
    public Action next;     /* Next action for this state */
    public Action collide;  /* Next action with the same hash */

    private static Action freelist;

    // Action_new
    public static Action actionNew() {
        Action new_;
        if (freelist == null) {
            int i;
            int amt = 100;
            freelist = INext.malloc(amt, Action::new);
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
            rc = ap1.x.rp.index - ap2.x.rp.index;
        }
        return rc;
    }

    // Action_sort
    public static Action actionSort(Action ap) {
        return Msort.msort(ap);
    }

    // Action_add
    public static void actionAdd(Action[] app, ActionType type, Symbol sp, Object arg) {
        Action new_ = actionNew();
        new_.next = app[0];
        app[0] = new_;
        new_.type = type;
        new_.sp = sp;
        if (type == SHIFT) {
            new_.x.stp = (State) arg;
        } else {
            new_.x.rp = (Rule) arg;
        }
    }

    public static void main(String[] args) {
        actionNew();
    }
}
