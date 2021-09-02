package io.github.yuemenglong.sqlite.common;

import java.util.function.Supplier;

public interface IList {
  interface GetNext<T> {
    T call(T prev);
  }

  interface SetNext<T> {
    void call(T prev, T next);
  }

  @SuppressWarnings({"unchecked"})
  static <T> T malloc(int size, Supplier<T> fn, SetNext<T> setNext) {
    Object[] arr = new Object[size];
    for (int i = 0; i < size; i++) {
      arr[i] = fn.get();
    }
    for (int i = 0; i < size - 1; i++) {
      T prev = (T) arr[i];
      T next = (T) arr[i + 1];
      setNext.call(prev, next);
    }

    setNext.call((T) arr[size - 1], null);
    return (T) arr[0];
  }
}
