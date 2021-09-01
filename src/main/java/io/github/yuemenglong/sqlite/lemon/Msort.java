package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.INext;

import java.util.function.BiFunction;

public class Msort {

  private static final int LISTSIZE = 30;

  public static <T extends INext<T>> T merge(T a, T b, BiFunction<T, T, Integer> comp) {
    T ptr, head;
    if (a == null) {
      head = b;
    } else if (b == null) {
      head = a;
    } else {
      if (comp.apply(a, b) < 0) {
        ptr = a;
        a = a.getNext();
      } else {
        ptr = b;
        b = b.getNext();
      }
      head = ptr;
      while (a != null && b != null) {
        if (comp.apply(a, b) < 0) {
          ptr.setNext(a);
          ptr = a;
          a = a.getNext();
        } else {
          ptr.setNext(b);
          ptr = b;
          b = b.getNext();
        }
      }
      if (a != null) {
        ptr.setNext(a);
      } else {
        ptr.setNext(b);
      }
    }
    return head;
  }

  @SuppressWarnings({"unchecked"})
  public static <T extends INext<T>> T msort(T list, BiFunction<T, T, Integer> comp) {
    T ep;
    Object[] set = new Object[LISTSIZE];
    int i;
    while (list != null) {
      ep = list;
      list = list.getNext();
      ep.setNext(null);
      for (i = 0; i < LISTSIZE - 1 && set[i] != null; i++) {
        ep = merge(ep, (T) set[i], comp);
        set[i] = null;
      }
      set[i] = ep;
    }
    ep = null;
    for (i = 0; i < LISTSIZE; i++) {
      if (set[i] != null) {
        ep = merge(ep, (T) set[i], comp);
      }
    }
    return ep;
  }
}
