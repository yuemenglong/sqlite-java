package io.github.yuemenglong.sqlite.common;

import java.util.Arrays;

// 以0结尾的字符串
public class Ptr<T> {

  private int pos = 0;
  private final T[] list;

  public Ptr(T[] list) {
    this(list, 0);
  }

  private Ptr(T[] list, int pos) {
    this.list = list;
    this.pos = pos;
  }

  public T cpp() {
    return list[pos++];
  }

  public T ppc() {
    return list[++pos];
  }

  public void move(int n) {
    pos += n;
  }

  public Ptr<T> ptr() {
    return ptr(0);
  }

  public Ptr<T> ptr(int n) {
    return new Ptr<T>(list, pos + n);
  }

  @SuppressWarnings("MethodDoesntCallSuperMethod")
  public Ptr<T> clone() {
    return ptr();
  }

  public Ptr<T> dup() {
    return ptr();
  }

  public T get() {
    return get(0);
  }

  public T get(int n) {
    if (pos + n >= list.length) {
      return null;
    }
    return list[pos + n];
  }

  public void set(T c) {
    set(0, c);
  }

  public void set(int n, T c) {
    list[pos + n] = c;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Arrays.stream(list).forEach(x -> sb.append(x).append("."));
    return sb.toString();
  }
}
