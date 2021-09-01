package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.INext;
import io.github.yuemenglong.sqlite.util.Addr;

public class Plink implements INext<Plink> {
  public Config cfp;      /* The configuration to which linked */
  public Plink next;      /* The next propagate link */

  private static Plink freelist = null;

  @Override
  public Plink getNext() {
    return next;
  }

  @Override
  public void setNext(Plink v) {
    next = v;
  }

  public static Plink new_() {
    Plink new_;
    if (freelist == null) {
      freelist = INext.malloc(100, Plink::new);
    }
    new_ = freelist;
    freelist = freelist.next;
    return new_;
  }

  public static void add(Addr<Plink> plpp, Config cfp) {
    Plink new_;
    new_ = new_();
    new_.next = plpp.get();
    plpp.set(new_);
    new_.cfp = cfp;
  }

  public static void copy(Addr<Plink> to, Plink from) {
    Plink nextpl;
    while (from != null) {
      nextpl = from.next;
      from.next = to.get();
      to.set(from);
      from = nextpl;
    }
  }

  public static void delete(Plink plp) {
    Plink nextpl;
    while (plp != null) {
      nextpl = plp.next;
      plp.next = freelist;
      freelist = plp;
      plp = nextpl;
    }
  }

}
