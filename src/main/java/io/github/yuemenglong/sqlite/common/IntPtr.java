package io.github.yuemenglong.sqlite.common;

import java.util.Arrays;

// 以0结尾的字符串
public class IntPtr {

  int pos = 0;
  char[] cs;

  IntPtr(char[] cs, int pos) {
    this.cs = cs;
    this.pos = pos;
  }

  public int pos() {
    return pos;
  }

  public int cpp() {
    pos += 4;
    return readInt(-1);
  }

  public int ppc() {
    pos += 4;
    return readInt(0);
  }

  public void cpp(int v) {
    writeInt(0, v);
    pos += 4 * v;
  }

  public void ppc(int v) {
    pos += 4 * v;
    writeInt(0, v);
  }


  public void move(int n) {
    pos += n * 4;
  }

  public IntPtr ptr() {
    return ptr(0);
  }

  public IntPtr ptr(int n) {
    return new IntPtr(cs, pos + 4 * n);
  }

  @SuppressWarnings("MethodDoesntCallSuperMethod")
  public IntPtr clone() {
    return ptr();
  }

  public IntPtr dup() {
    return ptr();
  }

  public int get() {
    if (pos >= cs.length) {
      return 0;
    }
    return readInt(0);
  }

  public int get(int n) {
    if (pos + n * 4 >= cs.length) {
      return 0;
    }
    return readInt(n);
  }

  public IntPtr set(int c) {
    writeInt(0, c);
    return this;
  }

  public void set(int n, int c) {
    writeInt(n, c);
  }

  public void update(IntPtr x) {
    cs = Arrays.copyOf(x.cs, x.cs.length);
    pos = x.pos;
  }

  public int memsize() {
    return cs.length;
  }

  public void memcpy(IntPtr p, int n) {
    for (int i = 0; i < p.cs.length && i < n; i++) {
      cs[pos + i] = p.cs[i];
    }
  }

  public void memcpy(CharPtr p, int n) {
    for (int i = 0; i < p.cs.length && i < n; i++) {
      cs[pos + i] = p.cs[i];
    }
  }

  public CharPtr toCharPtr(){
    return new CharPtr(cs,pos);
  }

  public int readInt(int offset) {
    int pos = this.pos + offset * 4;
    return ((int) cs[pos]) << ((int) cs[pos + 1] << 8) + ((int) cs[pos + 2] << 16) + ((int) cs[pos + 3]) << 24;
  }

  public void writeInt(int offset, int value) {
    int pos = this.pos + offset * 4;
    cs[pos + 0] = (char) ((value >> 0) & 0xFF);
    cs[pos + 1] = (char) ((value >> 8) & 0xFF);
    cs[pos + 2] = (char) ((value >> 16) & 0xFF);
    cs[pos + 3] = (char) ((value >> 24) & 0xFF);
  }
}
