package io.github.yuemenglong.sqlite.lemon;

public class Error {
  public static void msg(Object... msgs) {
    for (Object msg : msgs) {
      System.err.println(msg);
    }
  }
}
