package io.github.yuemenglong.sqlite.common;

import java.io.*;

public class FILE {
  public InputStream is;
  public OutputStream os;
  public final String path;

  private FILE(String path, InputStream is, OutputStream os) {
    this.path = path;
    this.is = is;
    this.os = os;
  }

  public int fread(CharPtr v, int n) {
    byte[] bs = new byte[n];
    try {
      int ret = is.read(bs);
      v.memcpy(new CharPtr(bs, 0), n);
      return ret;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public void fwrite(CharPtr v, int n) {
    try {
      os.write(v.toByteArray(), 0, n);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public CharPtr fgets(CharPtr s, int n) {
    try {
      for (int i = 0; i < n - 1; i++) {
        int c = is.read();
        if (c < 0) {
          return s;
        } else if (c == '\n') {
          s.set(i, c);
          return s;
        } else {
          s.set(i, c);
        }
      }
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
    return s;
  }

  public static FILE openRead(String path) {
    try {
      return new FILE(path, new FileInputStream(path), null);
    } catch (Throwable e) {
      return null;
    }
  }

  public static FILE openWrite(String path) {
    try {
      return new FILE(path, null, new FileOutputStream(path));
    } catch (Throwable e) {
      return null;
    }
  }

  public static FILE openWriteAppend(String path) {
    try {
      return new FILE(path, null, new FileOutputStream(path, true));
    } catch (Throwable e) {
      return null;
    }
  }

  public boolean isStdin() {
    return is != null && is == System.in;
  }

  public boolean isStdout() {
    return os != null && os == System.out;
  }

  public boolean isStderr() {
    return os != null && os == System.err;
  }

  public static FILE stdin() {
    return new FILE(null, System.in, null);
  }

  public static FILE stdout() {
    return new FILE(null, null, System.out);
  }

  public static FILE stderr() {
    return new FILE(null, null, System.err);
  }

  public void write(byte[] bytes) {
    try {
      os.write(bytes);
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public void fprintf(String fmt, Object... args) {
    String s = String.format(fmt, args);
    write(s.getBytes());
  }

  public void close() {
    try {
      if (is != null) {
        is.close();
      }
      if (os != null) {
        os.close();
      }
      is = null;
      os = null;
    } catch (Throwable e) {
      throw new RuntimeException(e);
    }
  }

  public void rewind() {
    try {
      is.reset();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
