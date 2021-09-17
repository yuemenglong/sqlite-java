package io.github.yuemenglong.sqlite.common;

import java.util.Arrays;

// 以0结尾的字符串
public class CharPtr {

  int pos = 0;
  char[] cs;

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

  CharPtr(byte[] bs, int pos) {
    this.cs = new char[bs.length];
    for (int i = 0; i < bs.length; i++) {
      cs[i] = (char) bs[i];
    }
    this.pos = pos;
  }

  CharPtr(char[] cs, int pos) {
    this.cs = cs;
    this.pos = pos;
  }

  public CharPtr realloc(int n) {
    this.cs = Arrays.copyOf(cs, n);
    return this;
  }

  public IntPtr toIntPtr() {
    return new IntPtr(cs, pos);
  }

  public int pos() {
    return pos;
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

  public int strcmp(String x) {
    return strcmp(new CharPtr(x));
  }

  public int strcmp(CharPtr x) {
    return toZeroString().compareTo(x.toZeroString());
  }

  public int strncmp(CharPtr x, int n) {
    return toZeroString().substring(0, n).compareTo(x.toZeroString().substring(0, n));
  }

  public void assign(CharPtr x) {
    cs = x.cs;
    pos = x.pos;
  }

  public void assign(String x) {
    assign(new CharPtr(x));
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

  public void strcpy(String p) {
    strcpy(new CharPtr(p));
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

  public void sprintf(String fmt, Object... args) {
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

  public byte[] toByteArray() {
    byte[] ret = new byte[cs.length - pos];
    for (int i = 0; i < ret.length; i++) {
      ret[i] = (byte) (cs[pos + i] & 0xFF);
    }
    return ret;
  }

//  public int readInt() {
//    return ((int) cs[pos]) << ((int) cs[pos + 1] << 8) + ((int) cs[pos + 2] << 16) + ((int) cs[pos + 3]) << 24;
//  }
//
//  public CharPtr writeInt(int value) {
//    cs[pos + 0] = (char) ((value >> 0) & 0xFF);
//    cs[pos + 1] = (char) ((value >> 8) & 0xFF);
//    cs[pos + 2] = (char) ((value >> 16) & 0xFF);
//    cs[pos + 3] = (char) ((value >> 24) & 0xFF);
//    return this;
//  }
}
