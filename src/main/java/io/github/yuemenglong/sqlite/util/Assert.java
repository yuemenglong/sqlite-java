package io.github.yuemenglong.sqlite.util;

public class Assert {
  public static void assertTrue(boolean v) {
    if (!v) {
      throw new RuntimeException("Assert Fail");
    }
  }

  public void myassert() {
    System.err.println("Assert Fail");
    for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
      System.err.println(e.toString());
    }
    System.exit(1);
  }
}
