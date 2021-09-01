package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.IList;

import java.util.function.BiFunction;

public class Msort {

  private static final int LISTSIZE = 30;

  public static <T> T merge(
          T a,
          T b,
          IList.GetNext<T> getNext,
          IList.SetNext<T> setNext,
          BiFunction<T, T, Integer> comp) {
    T ptr, head;
    if (a == null) {
      head = b;
    } else if (b == null) {
      head = a;
    } else {
      if (comp.apply(a, b) < 0) {
        ptr = a;
        a = getNext.call(a);
      } else {
        ptr = b;
        b = getNext.call(b);
      }
      head = ptr;
      while (a != null && b != null) {
        if (comp.apply(a, b) < 0) {
          setNext.call(ptr, a);
          ptr = a;
          a = getNext.call(a);
        } else {
          setNext.call(ptr, b);
          ptr = b;
          b = getNext.call(b);
        }
      }
      if (a != null) {
        setNext.call(ptr, a);
      } else {
        setNext.call(ptr, b);
      }
    }
    return head;
  }

  @SuppressWarnings({"unchecked"})
  public static <T> T msort(
          T list,
          IList.GetNext<T> getNext,
          IList.SetNext<T> setNext,
          BiFunction<T, T, Integer> comp) {
    T ep;
    Object[] set = new Object[LISTSIZE];
    int i;
    while (list != null) {
      ep = list;
      list = getNext.call(list);
      setNext.call(ep, null);
      for (i = 0; i < LISTSIZE - 1 && set[i] != null; i++) {
        ep = merge(ep, (T) set[i], getNext, setNext, comp);
        set[i] = null;
      }
      set[i] = ep;
    }
    ep = null;
    for (i = 0; i < LISTSIZE; i++) {
      if (set[i] != null) {
        ep = merge(ep, (T) set[i], getNext, setNext, comp);
      }
    }
    return ep;
  }
}
