package io.github.yuemenglong.sqlite.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

public class Replacer {
  public static class Item {
    String from;
    Object to;

    public Item(String from, Object to) {
      this.from = from;
      this.to = to;
    }
  }

  private static final ArrayList<Item> items = new ArrayList<>();

  static {
    //#define ParseARGDECL
    //#define ParseXARGDECL
    //#define ParseANSIARGDECL
    regist("ParseARGDECL", "");
    regist("ParseXARGDECL", "");
    regist("ParseANSIARGDECL", "");
  }

  public static void regist(String from, Object to) {
    items.add(new Item(from, to));
  }

  public static String doReplace(String input) {
    AtomicReference<String> ret = new AtomicReference<>(input);
    items.stream().sorted(Comparator.comparingInt(o -> -o.from.length())).forEach(item -> {
      String from = item.from;
      String to = String.valueOf(item.to);
      System.out.printf("Replace %s => %s\n", from, to);
      ret.set(ret.get().replaceAll(from, to));
    });
    return ret.get();
  }
}
