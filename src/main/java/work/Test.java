package work;

public class Test {
  public static void main(String[] args) {
    lemon.yyParser parse = lemon.ParseAlloc();
    lemon.Parse(parse, lemon.N, 0);
    lemon.Parse(parse, lemon.PLUS, 0);
    lemon.Parse(parse, lemon.N, 1);
  }
}
