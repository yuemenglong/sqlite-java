package io.github.yuemenglong.sqlite.util;

import java.util.ArrayList;

public class Replacer {
  private static ArrayList<String> froms = new ArrayList<>();
  private static ArrayList<Object> tos = new ArrayList<>();

  public static void regist(String from, Object to) {
    froms.add(from);
    tos.add(to);
  }
}
