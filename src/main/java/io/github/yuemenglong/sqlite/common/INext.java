package io.github.yuemenglong.sqlite.common;

import java.util.function.Supplier;

public interface INext<T> {
  T getNext();

  void setNext(T v);

  @SuppressWarnings({"rawtypes", "unchecked"})
  static <T> T malloc(int size, Supplier<INext<T>> fn) {
    Object[] arr = new Object[size];
    for (int i = 0; i < size; i++) {
      arr[i] = fn.get();
    }
    for (int i = 0; i < size - 1; i++) {
      INext prev = (INext) arr[i];
      INext next = (INext) arr[i + 1];
      prev.setNext(next);
    }
    ((INext) arr[size - 1]).setNext(null);
    return (T) arr[0];
  }
}
