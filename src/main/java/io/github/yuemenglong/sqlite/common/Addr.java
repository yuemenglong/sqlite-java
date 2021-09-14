package io.github.yuemenglong.sqlite.common;

import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class Addr<T> {

  private final Supplier<T> getter;
  private final Consumer<T> setter;

  public Addr(Supplier<T> getter, Consumer<T> setter) {
    this.getter = getter;
    this.setter = setter;
  }

  public void set(T value) {
    setter.accept(value);
  }

  public T get() {
    return getter.get();
  }

  @Override
  public String toString() {
    return String.valueOf(get());
  }
}
