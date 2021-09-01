package io.github.yuemenglong.sqlite.lemon;

public class Options {
  public enum Type {
    OPT_FLAG, OPT_INT, OPT_DBL, OPT_STR,
    OPT_FFLAG, OPT_FINT, OPT_FDBL, OPT_FSTR
  }

  public Type type;
  public String label;
  public String arg;
  public String message;
}
