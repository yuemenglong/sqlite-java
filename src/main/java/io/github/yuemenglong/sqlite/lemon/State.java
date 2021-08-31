package io.github.yuemenglong.sqlite.lemon;

public class State {
    public Config bp;       /* The basis configurations for this state */
    public Config cfp;      /* All configurations in this set */
    public int index;               /* Sequencial number for this state */
    public Action ap;       /* Array of actions for this state */
    public int naction;             /* Number of actions for this state */
    public int tabstart;            /* First index of the action table */
    public int tabdfltact;          /* Default action */
}
