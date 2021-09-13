package io.github.yuemenglong.sqlite.lemon.util;

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
