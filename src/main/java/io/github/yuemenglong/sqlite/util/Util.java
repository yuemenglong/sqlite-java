package io.github.yuemenglong.sqlite.util;

import java.io.File;

public class Util {
  public static boolean islower(byte c) {
    return 'a' <= c && c <= 'z';
  }

  public static boolean isupper(byte c) {
    return 'A' <= c && c <= 'Z';
  }

  public static boolean isalpha(byte c) {
    return isupper(c) || islower(c);
  }

  public static boolean isnum(byte c) {
    return '0' <= c && c <= '9';
  }

  public static boolean isalnum(byte c) {
    return isalpha(c) || isnum(c);
  }

  public static boolean isspace(byte c) {
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

  public static int access(String path, int mode) {
    return new File(path).exists() ? 0 : -1;
  }

  public static int strrchr(String str, int c) {
    return str.lastIndexOf(c);
  }

  public static String getenv(String key) {
    return System.getenv(key);
  }

  //  strncmp(&line[i],"Parse",5)==0
  public static int strncmp(String a, String b, int n) {
    return a.startsWith(b, n) ? 0 : -1;
  }

  public static int strcmp(String a, String b) {
    return a.compareTo(b);
  }
}
