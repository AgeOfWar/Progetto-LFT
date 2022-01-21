import java.io.*;

public class Parser {
  private Lexer lex;
  private BufferedReader pbr;
  private Token look;
  
  public Parser(Lexer l, BufferedReader br) {
    lex = l;
    pbr = br;
    move();
  }
  
  void move() {
    look = lex.lexical_scan(pbr);
    System.out.println("token = " + look);
  }
  
  void error(String s) {
    throw new Error("near line " + lex.line + ": " + s + ", found '" + look.toSimpleString() + "'");
  }
  
  void match(int t, String message) {
    if (look.tag == t) {
      if (look.tag != Tag.EOF) move();
    } else error(message);
  }
  
  void match(int t) {
    match(t, "expected <" + t + ">");
  }
  
  public void start() {
    // 3.1
    // expr();
    // match(Tag.EOF);
    // 3.1
    // 3.2
    prog();
    // 3.2
  }
  
  private void expr() {
    // 3.1
    // term();
    // exprp();
    // 3.1
    // 3.2
    switch (look.tag) {
      case Tag.NUM:
        match(Tag.NUM);
        break;
      case Tag.ID:
        match(Tag.ID);
        break;
      case '+':
        match('+');
        match('(', "'(' expected after '+'");
        exprlist();
        match(')', "unclosed sum");
        break;
      case '*':
        match('*');
        match('(', "'(' expected after '*'");
        exprlist();
        match(')', "unclosed product");
        break;
      case '-':
        match('-');
        expr();
        expr();
        break;
      case '/':
        match('/');
        expr();
        expr();
        break;
      default:
        error("expression expected (constant, variable, '+ expr expr', '* expr expr', '- expr expr', '/ expr expr')");
    }
    // 3.2
  }
  
  private void exprp() {
    // 3.1
    switch (look.tag) {
      case '+':
        match('+');
        term();
        exprp();
        break;
      case '-':
        match('-');
        term();
        exprp();
        break;
    }
    // 3.1
  }
  
  private void term() {
    // 3.1
    fact();
    termp();
    // 3.1
  }
  
  private void termp() {
    // 3.1
    switch (look.tag) {
      case '*':
        match('*');
        fact();
        termp();
        break;
      case '/':
        match('/');
        fact();
        termp();
        break;
    }
    // 3.1
  }
  
  private void fact() {
    // 3.1
    switch (look.tag) {
      case Tag.NUM:
        match(Tag.NUM);
        break;
      case '(':
        match('(');
        expr();
        match(')', "unclosed '('");
        break;
      default:
        error("factor expected (constant, '(expr)')");
    }
    // 3.1
  }
  
  // 3.2
  private void prog() {
    statlist();
    match(Tag.EOF, "expected end of file");
  }
  
  private void statlist() {
    stat();
    statlistp();
  }
  
  private void statlistp() {
    if (look.tag == ';') {
      match(';');
      stat();
      statlistp();
    }
  }
  
  private void stat() {
    switch (look.tag) {
      case Tag.ASSIGN:
        match(Tag.ASSIGN);
        expr();
        match(Tag.TO, "expected 'to' after 'assign'");
        idlist();
        break;
      case Tag.PRINT:
        match(Tag.PRINT);
        match('(', "expected '(' after 'print'");
        exprlist();
        match(')', "unclosed print '(");
        break;
      case Tag.READ:
        match(Tag.READ);
        match('(', "expected '(' after 'read'");
        idlist();
        match(')', "unclosed read '(");
        break;
      case Tag.WHILE:
        match(Tag.WHILE);
        match('(', "expected '(' after 'while'");
        bexpr();
        match(')', "unclosed while condition '(");
        stat();
        break;
      case Tag.IF:
        match(Tag.IF);
        match('(', "expected '(' after 'if'");
        bexpr();
        match(')', "unclosed if condition '(");
        stat();
        switch (look.tag) {
          case Tag.END:
            match(Tag.END);
            break;
          case Tag.ELSE:
            match(Tag.ELSE);
            stat();
            match(Tag.END, "expected 'end' after 'else'");
            break;
          default:
            error("expected 'end' or 'else' after 'if'");
        }
        break;
      case '{':
        match('{');
        statlist();
        match('}', "unclosed block '{");
        break;
      default:
        error("statement expected (assign, print, read, while, if, block)");
    }
  }
  
  private void idlist() {
    match(Tag.ID, "expected identifier");
    idlistp();
  }
  
  private void idlistp() {
    if (look.tag == ',') {
      match(',');
      match(Tag.ID, "expected identifier");
      idlistp();
    }
  }
  
  private void bexpr() {
    match(Tag.RELOP, "expected boolean expression ('< expr expr', '<= expr expr', '> expr expr', '>= expr expr', '== expr expr', '<> expr expr')");
    expr();
    expr();
  }
  
  private void exprlist() {
    expr();
    exprlistp();
  }
  
  private void exprlistp() {
    if (look.tag == ',') {
      match(',');
      expr();
      exprlistp();
    }
  }
  // 3.2
  
  public static void main(String[] args) {
    if (args.length != 1) {
      System.err.println("Usage: <file path>");
      return;
    }
    Lexer lex = new Lexer();
    String path = args[0];
    try {
      BufferedReader br = new BufferedReader(new FileReader(path));
      Parser parser = new Parser(lex, br);
      parser.start();
      System.out.println("Input OK");
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
