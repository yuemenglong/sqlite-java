package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.lemon.util.Table;

public class State {
  public Config bp;       /* The basis configurations for this state */
  public Config cfp;      /* All configurations in this set */
  public int index;               /* Sequencial number for this state */
  public Action ap;       /* Array of actions for this state */
  public int naction;             /* Number of actions for this state */
  public int tabstart;            /* First index of the action table */
  public int tabdfltact;          /* Default action */

  private static Table<Config, State> x3a;

  public static int comp(Config a, Config b) {
    int rc;
    for (rc = 0; rc == 0 && a != null && b != null; a = a.bp, b = b.bp) {
      rc = a.rp.index - b.rp.index;
      if (rc == 0) rc = a.dot - b.dot;
    }
    if (rc == 0) {
      if (a != null) rc = 1;
      if (b != null) rc = -1;
    }
    return rc;
  }

  public static int hash(Config a) {
    int h = 0;
    while (a != null) {
      h = h * 571 + a.rp.index * 37 + a.dot;
      a = a.bp;
    }
    return h;
  }

  public static State new_() {
    return new State();
  }

  public static void init() {
    if (x3a != null) return;
    x3a = new Table<>();
    x3a.setHasher(State::hash);
    x3a.setComparator(State::comp);
    x3a.init(128);
  }

  public static int insert(State data, Config key) {
    if (x3a == null) return 0;
    return x3a.insert(key, data);
  }

  public static State find(Config key) {
    if (x3a == null) return null;
    return x3a.find(key);
  }

  public static State[] arrayOf() {
    if (x3a == null) return null;
    return x3a.arrayOf(State.class);
  }
}
