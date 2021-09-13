package work;

public class Test {
  public static void main(String[] args) {
    lemon.yyParser parse = lemon.ParseAlloc();
    Parse p = new Parse();
    lemon.Parse(parse, lemon.TK_N, 0, p);
    lemon.Parse(parse, lemon.TK_PLUS, 0, p);
    lemon.Parse(parse, lemon.TK_N, 1, p);
    lemon.Parse(parse, 0, 0, p);
  }
}
