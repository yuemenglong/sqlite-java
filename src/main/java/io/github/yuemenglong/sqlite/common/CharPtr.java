package io.github.yuemenglong.sqlite.common;

import java.util.Arrays;

// 以0结尾的字符串
public class CharPtr {

  private int pos = 0;
  private char[] cs;

  public CharPtr(String s) {
    this.cs = Arrays.copyOf(s.toCharArray(), s.length() + 1);
    this.cs[cs.length - 1] = 0;
  }

  public CharPtr() {
    this(1);
  }

  public CharPtr(int len) {
    this.cs = new char[len];
  }

  private CharPtr(char[] cs, int pos) {
    this.cs = cs;
    this.pos = pos;
  }

  public char cpp() {
    return cs[pos++];
  }

  public char ppc() {
    return cs[++pos];
  }

  public void cpp(char v) {
    cs[pos++] = v;
  }

  public void ppc(char v) {
    cs[++pos] = v;
  }


  public void move(int n) {
    pos += n;
  }

  public CharPtr ptr() {
    return ptr(0);
  }

  public CharPtr ptr(int n) {
    return new CharPtr(cs, pos + n);
  }

  @SuppressWarnings("MethodDoesntCallSuperMethod")
  public CharPtr clone() {
    return ptr();
  }

  public CharPtr dup() {
    return ptr();
  }

  public char get() {
    if (pos >= cs.length) {
      return 0;
    }
    return cs[pos];
  }

  public char get(int n) {
    if (pos + n >= cs.length) {
      return 0;
    }
    return cs[pos + n];
  }

  public void set(int c) {
    cs[pos] = (char) c;
  }

  public void set(int n, int c) {
    cs[pos + n] = (char) c;
  }

  public int strcmp(CharPtr x) {
    return toZeroString().compareTo(x.toZeroString());
  }

  public void update(CharPtr x) {
    cs = Arrays.copyOf(x.cs, x.cs.length);
    pos = x.pos;
  }

  public int strlen() {
    for (int i = pos; i < cs.length; i++) {
      if (cs[i] == 0) return i;
    }
    return cs.length;
  }

  public int memsize() {
    return cs.length;
  }

  public void strcpy(CharPtr p) {
    for (int i = 0; i < p.strlen(); i++) {
      cs[pos + i] = p.cs[i];
    }
  }

  public void memcpy(CharPtr p, int n) {
    for (int i = 0; i < p.strlen() && i < n; i++) {
      cs[pos + i] = p.cs[i];
    }
  }

  public void strncpy(CharPtr p, int n) {
    for (int i = 0; i < p.strlen() && i < n; i++) {
      cs[pos + i] = p.cs[i];
    }
  }

  public void sprintf(String fmt, Object args) {
    String s = String.format(fmt, args);
    strcpy(new CharPtr(s));
  }

  public int atoi() {
    return Integer.parseInt(toZeroString());
  }

  public double atof() {
    return Double.parseDouble(toZeroString());
  }


  public String toZeroString() {
    int i = pos;
    for (; i < cs.length; i++) {
      if (cs[i] == 0) break;
    }
    return new String(cs, pos, i - pos);
  }

  @Override
  public String toString() {
    char[] cs2 = Arrays.copyOf(cs, cs.length);
    for (int i = pos; i < cs2.length; i++) {
      if (cs2[i] == 0) {
        cs2[i] = '.';
      }
    }
    return new String(cs2, pos, cs2.length - pos);
  }

  public static void main(String[] args) {
    CharPtr p = new CharPtr("asdf");
    System.out.println(p);
  }
}
