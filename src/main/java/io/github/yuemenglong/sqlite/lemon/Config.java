package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.INode;
import io.github.yuemenglong.sqlite.util.Table;

import java.util.function.Consumer;

public class Config implements INode<Config> {
    @Override
    public Config getNext() {
        return next;
    }

    @Override
    public void setNext(Config v) {
        next = v;
    }

    @Override
    public int compareTo(Config b) {
        Config a = this;
        int x = a.rp.index - b.rp.index;
        if (x == 0) x = a.dot - b.dot;
        return x;
    }

    public enum ConfigStatus {
        COMPLETE,              /* The status is used during followset and */
        INCOMPLETE             /*    shift computations */
    }

    public Rule rp;         /* The rule upon which the configuration is based */
    public int dot;                 /* The parse point */
    public String fws;               /* Follow-set for this configuration only */
    public Plink fplp;      /* Follow-set forward propagation links */
    public Plink bplp;      /* Follow-set backwards propagation links */
    public State stp;       /* Pointer to state which contains this */
    public ConfigStatus status;
    public Config next;     /* Next configuration in the state */
    public Config bp;       /* The next basis configuration */

    private static Table<Config, Config> x4a;

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
