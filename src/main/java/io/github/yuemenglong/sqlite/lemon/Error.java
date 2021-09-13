package io.github.yuemenglong.sqlite.lemon;

public class Error {
  public static void msg(String filename, int lineNo, String fmt, Object... args) {
    fmt = String.format("[%s:%d]: %s\n", filename, lineNo, fmt);
    System.err.printf(fmt, args);
  }
}
