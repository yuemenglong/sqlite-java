package io.github.yuemenglong.sqlite.lemon;

import io.github.yuemenglong.sqlite.common.INode;

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
    public int compareTo(Config o) {
        return 0;
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
}
