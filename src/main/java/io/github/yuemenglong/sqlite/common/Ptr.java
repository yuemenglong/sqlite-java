package io.github.yuemenglong.sqlite.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 以0结尾的字符串
public class Ptr<T> {

  private int pos = 0;
  private int limit = 0;
  private final List<T> list;

  public Ptr(int len) {
    this.limit = len;
    this.list = new ArrayList<>(len);
  }

  private Ptr(List<T> list, int pos, int limit) {
    this.list = list;
    this.pos = pos;
  }

  public T cpp() {
    return list.get(pos);
  }

  public T ppc() {
    return list.get(++pos);
  }

  public void move(int n) {
    pos += n;
  }

  public Ptr<T> ptr() {
    return ptr(0);
  }

  public Ptr<T> ptr(int n) {
    return new Ptr<T>(list, pos + n, limit);
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
    if (pos + n >= limit) {
      return null;
    }
    return list.get(pos + n);
  }

  public void set(T c) {
    set(0, c);
  }

  public void set(int n, T c) {
    list.set(pos + n, c);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    list.forEach(x -> sb.append(x).append("."));
    return sb.toString();
  }
}
