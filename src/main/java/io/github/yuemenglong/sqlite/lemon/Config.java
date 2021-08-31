package io.github.yuemenglong.sqlite.lemon;

public class Config {
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
