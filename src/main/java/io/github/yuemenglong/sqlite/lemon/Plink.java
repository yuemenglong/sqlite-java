package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.lemon.common.IList;
import io.github.yuemenglong.sqlite.lemon.util.Addr;

public class Plink {
  public Config cfp;      /* The configuration to which linked */
  public Plink next;      /* The next propagate link */

  @Override
  public String toString() {
    String ns = "null";
    if (next != null) {
      ns = next.toString();
    }
    return String.format("%s -> %s", cfp, ns);
  }

  private static Plink freelist = null;

  public static Plink new_() {
    Plink new_;
    if (freelist == null) {
      freelist = IList.malloc(100, Plink::new, (prev, next) -> prev.next = next);
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
