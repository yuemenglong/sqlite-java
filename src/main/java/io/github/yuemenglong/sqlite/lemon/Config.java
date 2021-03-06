package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.lemon.util.Table;

import java.util.function.Consumer;

public class Config {

  public enum Status {
    COMPLETE,              /* The status is used during followset and */
    INCOMPLETE             /*    shift computations */
  }

  public Rule rp;         /* The rule upon which the configuration is based */
  public int dot;                 /* The parse point */
  public byte[] fws;               /* Follow-set for this configuration only */
  public Plink fplp;      /* Follow-set forward propagation links */
  public Plink bplp;      /* Follow-set backwards propagation links */
  public State stp;       /* Pointer to state which contains this */
  public Status status;
  public Config next;     /* Next configuration in the state */
  public Config bp;       /* The next basis configuration */

  @Override
  public String toString() {
    return String.format("(%d)%s", dot, rp);
  }

  private static Table<Config, Config> x4a;

  // Configcmp
  public static int cmp(Config a, Config b) {
    int x;
    x = a.rp.index - b.rp.index;
    if (x == 0) x = a.dot - b.dot;
    return x;
  }

  public static int hash(Config a) {
    int h = 0;
    h = a.rp.index * 37 + a.dot;
    return h;
  }

  public static void init() {
    if (x4a != null) return;
    x4a = new Table<>();
    x4a.setHasher(Config::hash);
    x4a.setComparator(Config::cmp);
    x4a.init(64);
  }

  public static int insert(Config data) {
    return x4a.insert(data, data);
  }

  public static Config find(Config key) {
    return x4a.find(key);
  }

  public static void clear(Consumer<Config> fn) {
    x4a.clear(fn);
  }

}
