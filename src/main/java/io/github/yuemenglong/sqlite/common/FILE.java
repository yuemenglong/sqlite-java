package io.github.yuemenglong.sqlite.common;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class FILE {
  public InputStream is;
  public OutputStream os;
  public final String path;

  private FILE(String path, InputStream is, OutputStream os) {
    this.path = path;
    this.is = is;
    this.os = os;
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
}
