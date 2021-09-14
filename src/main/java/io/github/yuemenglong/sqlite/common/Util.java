package io.github.yuemenglong.sqlite.common;

import java.io.File;

public class Util {
  public static boolean islower(int c) {
    return 'a' <= c && c <= 'z';
  }

  public static boolean isupper(int c) {
    return 'A' <= c && c <= 'Z';
  }

  public static char toupper(int c) {
    return (char) (c + 'A' - 'a');
  }

  public static char tolower(int c) {
    return (char) (c + 'a' - 'A');
  }

  public static boolean isalpha(int c) {
    return isupper(c) || islower(c);
  }

  public static boolean isnum(int c) {
    return '0' <= c && c <= '9';
  }

  public static boolean isalnum(int c) {
    return isalpha(c) || isnum(c);
  }

  public static boolean isspace(int c) {
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

  public static boolean isdigit(int c) {
    return '0' <= c && c <= '9';
  }

  public static int access(String path, int mode) {
    return new File(path).exists() ? 0 : -1;
  }

  public static int strrchr(String str, int c) {
    return str.lastIndexOf(c);
  }

  public static int strchr(String str, int c) {
    return str.indexOf(c);
  }

  public static String getenv(String key) {
    return System.getenv(key);
  }

  public static int strlen(String s) {
    return s.length();
  }

  //  strncmp(&line[i],"Parse",5)==0
  public static int strncmp(String a, String b, int n) {
    return a.startsWith(b, n) ? 0 : -1;
  }

  public static int strcmp(String a, String b) {
    return a.compareTo(b);
  }

  public static int atoi(String s) {
    return Integer.parseInt(s);
  }

  public static double strtod(String s) {
    return Double.parseDouble(s);
  }

  public static long strtol(String s, int n) {
    return Long.parseLong(s, n);
  }

  public static String FILE() {
    return Thread.currentThread().getStackTrace()[1].getFileName();
  }

  public static int LINE() {
    return Thread.currentThread().getStackTrace()[1].getLineNumber();
  }

  public static void unlink(String path) {
    new File(path).delete();
  }
}
