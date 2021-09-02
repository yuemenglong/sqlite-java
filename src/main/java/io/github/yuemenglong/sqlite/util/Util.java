package io.github.yuemenglong.sqlite.util;

public class Util {
  public static boolean islower(char c) {
    return 'a' <= c && c <= 'z';
  }

  public static boolean isupper(char c) {
    return 'A' <= c && c <= 'Z';
  }

  public static boolean isalpha(char c) {
    return isupper(c) || islower(c);
  }

  public static boolean isnum(char c) {
    return '0' <= c && c <= '9';
  }

  public static boolean isalnum(char c) {
    return isalpha(c) || isnum(c);
  }

  public static boolean isspace(char c) {
    switch (c) {
      case ' ':
      case '\t':
      case '\n':
      case '\r':
        return true;
      default:
        return false;
    }
  }

}
