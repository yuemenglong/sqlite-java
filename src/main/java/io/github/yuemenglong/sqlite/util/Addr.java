package io.github.yuemenglong.sqlite.util;

import java.lang.reflect.Field;
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
}
