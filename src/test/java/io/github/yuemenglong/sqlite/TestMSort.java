package io.github.yuemenglong.sqlite;

import io.github.yuemenglong.sqlite.lemon.Msort;
import org.junit.Test;

public class TestMSort {

  public static class IntNode implements io.github.yuemenglong.sqlite.common.INext<IntNode>, Comparable<IntNode> {
    public int value;
    public IntNode next;

    public IntNode(Integer value) {
      this.value = value;
    }

    public IntNode getNext() {
      return next;
    }

    public void setNext(IntNode v) {
      next = v;
    }

    public int compareTo(IntNode o) {
      return Integer.compare(value, o.value);
    }

    @Override
    public String toString() {
      String n = "null";
      if (next != null) {
        n = next.toString();
      }
      return String.format("%d -> %s", value, n);
    }

    public static IntNode create(Integer... values) {
      IntNode head = null;
      IntNode prev = null;
      for (Integer value : values) {
        if (prev == null) {
          head = new IntNode(value);
          prev = head;
        } else {
          prev.next = new IntNode(value);
          prev = prev.next;
        }
      }
      return head;
    }
  }

  @Test
  public void testMSort() {
    IntNode list = IntNode.create(3, 5, 4, 2);
    System.out.println(list);
    list = Msort.msort(list, (a, b) -> Integer.compare(a.value, b.value));
    System.out.println(list);
  }
}
