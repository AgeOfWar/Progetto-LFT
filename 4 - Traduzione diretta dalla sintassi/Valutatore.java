import java.io.*;

public class Valutatore {
  private Lexer lex;
  private BufferedReader pbr;
  private Token look;
  
  public Valutatore(Lexer l, BufferedReader br) {
    lex = l;
    pbr = br;
    move();
  }
  
  void move() {
    look = lex.lexical_scan(pbr);
    System.out.println("token = " + look);
  }
  
  void error(String s) {
    throw new Error("near line " + lex.line + ": " + s);
  }
  
  void match(int t) {
    if (look.tag == t) {
      if (look.tag != Tag.EOF) move();
    } else error("syntax error");
  }
  
  public void start() {
    //if (look.tag != Tag.NUM && look.tag != '(') {
    //  error("start");
    //}

    int val = expr();
    match(Tag.EOF);
    System.out.println(val);
  }
  
  private int expr() {
    //if (look.tag != Tag.NUM && look.tag != '(') {
    //  error("start");
    //}

    return exprp(term());
  }
  
  private int exprp(int val) {
    //if (look.tag != '+' && look.tag != '-' && look.tag != '(' && look.tag != Tag.EOF) {
    //  error("start");
    //}

    switch (look.tag) {
      case '+':
        match('+');
        return val + exprp(term());
      case '-':
        match('-');
        return val - exprp(term());
      default:
        return val;
    }
  }
  
  private int term() {
    //if (look.tag != Tag.NUM && look.tag != '(') {
    //  error("start");
    //}

    return termp(fact());
  }
  
  private int termp(int val) {
    //if (look.tag != '+' && look.tag != '-' && look.tag != '*' && look.tag != '/' && look.tag != '(' && look.tag != Tag.EOF) {
    //  error("start");
    //}

    switch (look.tag) {
      case '*':
        match('*');
        return val * termp(fact());
      case '/':
        match('/');
        return val / termp(fact());
      default:
        return val;
    }
  }
  
  private int fact() {
    //if (look.tag != Tag.NUM && look.tag != '(') {
    //  error("start");
    //}

    int val = 0;
    switch (look.tag) {
      case Tag.NUM:
        val = ((NumberTok) look).value;
        match(Tag.NUM);
        break;
      case '(':
        match('(');
        val = expr();
        match(')');
        break;
      default:
        error("syntax error");
    }
    return val;
  }
  
  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Usage: <file path>");
      return;
    }
    Lexer lex = new Lexer();
    String path = args[0];
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      Valutatore valutatore = new Valutatore(lex, br);
      valutatore.start();
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
