package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.IList;
import io.github.yuemenglong.sqlite.util.Addr;
import io.github.yuemenglong.sqlite.util.Assert;

import static io.github.yuemenglong.sqlite.lemon.Action.Type.REDUCE;
import static io.github.yuemenglong.sqlite.lemon.Action.Type.SHIFT;

public class Action {
  public static class StateOrRule {
    public State stp;
    public Rule rp;
  }

  public enum Type {
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
  public Type type;
  public StateOrRule x = new StateOrRule();
  public Action next;     /* Next action for this state */
  public Action collide;  /* Next action with the same hash */

  private static Action freelist;

  // Action_new
  public static Action new_() {
    Action new_;
    if (freelist == null) {
      int amt = 100;
      freelist = IList.malloc(amt, Action::new, (prev, next) -> prev.next = next);
    }
    new_ = freelist;
    freelist = freelist.next;
    return new_;
  }

  // actioncmp
  public static int cmp(Action ap1, Action ap2) {
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
  public static Action sort(Action ap) {
    return Msort.msort(ap,
            prev -> prev.next,
            (prev, next) -> prev.next = next,
            Action::cmp);
  }

  // Action_add
  public static void add(Addr<Action> app, Type type, Symbol sp, Object arg) {
    Action new_ = new_();
    new_.next = app.get();
    app.set(new_);
    new_.type = type;
    new_.sp = sp;
    if (type == SHIFT) {
      new_.x.stp = (State) arg;
    } else {
      new_.x.rp = (Rule) arg;
    }
  }

}
