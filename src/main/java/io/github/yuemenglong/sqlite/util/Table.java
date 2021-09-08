package io.github.yuemenglong.sqlite.util;


import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class Table<K, V> {
  public static class TableNode<K, V> {
    K key;
    V data;
    TableNode<K, V> next;
  }

  private int size;
  private int count;
  private TableNode<K, V>[] tbl;
  private TableNode<K, V>[] ht;
  private Function<K, Integer> hash;
  private BiFunction<K, K, Integer> comp;

  public Table() {
    hash = Object::hashCode;
    comp = (a, b) -> {
      if (a == null) {
        return -1;
      } else if (b == null) {
        return 1;
      } else if (Objects.equals(a, b)) {
        return 0;
      } else {
        return Integer.compare(a.hashCode(), b.hashCode());
      }
    };
  }

  public void init(int n) {
    if (tbl != null) {
      return;
    }
    size = n;
    count = 0;
    tbl = new TableNode[size];
    for (int i = 0; i < size; i++) {
      tbl[i] = new TableNode<>();
    }
    ht = new TableNode[size];
  }

  public void setHasher(Function<K, Integer> hash) {
    this.hash = hash;
  }

  public void setComparator(BiFunction<K, K, Integer> comp) {
    this.comp = comp;
  }

  public int insert(K key, V value) {
    int ph = hash.apply(key);
    int h = ph & (size - 1);
    TableNode<K, V> np = ht[h];
    while (np != null) {
      if (comp.apply(np.key, key) == 0) {
        return 0;
      }
      np = np.next;
    }
    if (count >= size) {
      throw new RuntimeException("Not Enough Space");
    }
    np = tbl[count];
    count += 1;
    np.key = key;
    np.data = value;
    np.next = ht[h];
    ht[h] = np;
    return 1;
  }

  public V find(K key) {
    int ph = hash.apply(key);
    int h = ph & (size - 1);
    TableNode<K, V> np = ht[h];
    while (np != null) {
      if (comp.apply(np.key, key) == 0) {
        return np.data;
      }
      np = np.next;
    }
    return null;
  }

  public V nth(int n) {
    if (tbl != null && n <= count) {
      return tbl[n - 1].data;
    } else {
      return null;
    }
  }

  public int count() {
    return count;
  }

  public V[] arrayOf(Class<V> clazz) {
    if (tbl == null) return null;
    V[] ret = (V[]) Array.newInstance(clazz, count);
    for (int i = 0; i < count; i++) {
      ret[i] = tbl[i].data;
    }
    return ret;
  }

  public void clear(Consumer<V> fn) {
    if (tbl == null || count == 0) {
      return;
    }
    if (fn != null) {
      for (int i = 0; i < count; i++) {
        fn.accept(tbl[i].data);
      }
    }
    for (int i = 0; i < size; i++) {
      ht[i] = null;
    }
    count = 0;
  }
}
