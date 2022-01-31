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
    throw new Error("near line " + lex.line + ": " + s + ", found " + look);
  }
  
  void match(int t) {
    if (look.tag == t) {
      if (look.tag != Tag.EOF) move();
    } else error("expected " + t);
  }
  
  public void start() {
    // 3.1
    // if (look.tag != Tag.NUM && look.tag != '(') {
    //   error("expected number, '('");
    // }

    // expr();
    // match(Tag.EOF);
    // 3.1

    // 3.2
    prog();
    // 3.2
  }
  
  private void expr() {
    // 3.1
    // if (look.tag != Tag.NUM && look.tag != '(') {
    //   error("expected number, '('");
    // }

    // term();
    // exprp();
    // 3.1

    // 3.2
    if (look.tag != '+' && look.tag != '-' && look.tag != '*' && look.tag != '/' && look.tag != Tag.NUM && look.tag != Tag.ID) {
      error("expected '+', '-', '*', '/', number, identifier");
    }

    switch (look.tag) {
      case Tag.NUM:
        match(Tag.NUM);
        break;
      case Tag.ID:
        match(Tag.ID);
        break;
      case '+':
        match('+');
        match('(');
        exprlist();
        match(')');
        break;
      case '*':
        match('*');
        match('(');
        exprlist();
        match(')');
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
    }
    // 3.2
  }
  
  private void exprp() {
    // 3.1
    //if (look.tag != '+' && look.tag != '-' && look.tag != '(' && look.tag != Tag.EOF) {
    //  error("expected '+', '-', '(', end of file");
    //}

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
    if (look.tag != Tag.NUM && look.tag != '(') {
      error("expected number, '('");
    }

    fact();
    termp();
    // 3.1
  }
  
  private void termp() {
    // 3.1
    //if (look.tag != '+' && look.tag != '-' && look.tag != '*' && look.tag != '/' && look.tag != '(' && look.tag != Tag.EOF) {
    //  error("expected '+', '-', '*', '/', '(', end of file");
    //}

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
    if (look.tag != Tag.NUM && look.tag != '(') {
      error("expected number, '('");
    }

    switch (look.tag) {
      case Tag.NUM:
        match(Tag.NUM);
        break;
      case '(':
        match('(');
        expr();
        match(')');
        break;
    }
    // 3.1
  }
  
  // 3.2
  private void prog() {
    if (look.tag != Tag.ASSIGN && look.tag != Tag.PRINT && look.tag != Tag.READ && look.tag != Tag.WHILE && look.tag != Tag.IF && look.tag == '{') {
      error("expected 'assign', 'print', 'read', 'if', 'while', '{'");
    }

    statlist();
    match(Tag.EOF);
  }
  
  private void statlist() {
    if (look.tag != Tag.ASSIGN && look.tag != Tag.PRINT && look.tag != Tag.READ && look.tag != Tag.WHILE && look.tag != Tag.IF && look.tag == '{') {
      error("expected 'assign', 'print', 'read', 'if', 'while', '{'");
    }

    stat();
    statlistp();
  }
  
  private void statlistp() {
    //if (look.tag != ';' && look.tag == '}' && look.tag != Tag.EOF) {
    //  error("expected ';', '}', end of file");
    //}

    if (look.tag == ';') {
      match(';');
      stat();
      statlistp();
    }
  }
  
  private void stat() {
    if (look.tag != Tag.ASSIGN && look.tag != Tag.PRINT && look.tag != Tag.READ && look.tag != Tag.WHILE && look.tag != Tag.IF && look.tag != '{') {
      error("expected 'assign', 'print', 'read', 'if', 'while', '{'");
    }

    switch (look.tag) {
      case Tag.ASSIGN:
        match(Tag.ASSIGN);
        expr();
        match(Tag.TO);
        idlist();
        break;
      case Tag.PRINT:
        match(Tag.PRINT);
        match('(');
        exprlist();
        match(')');
        break;
      case Tag.READ:
        match(Tag.READ);
        match('(');
        idlist();
        match(')');
        break;
      case Tag.WHILE:
        match(Tag.WHILE);
        match('(');
        bexpr();
        match(')');
        stat();
        break;
      case Tag.IF:
        match(Tag.IF);
        match('(');
        bexpr();
        match(')');
        stat();
        statp();
        break;
      case '{':
        match('{');
        statlist();
        match('}');
        break;
    }
  }

  private void statp() {
    if (look.tag != Tag.END && look.tag != Tag.ELSE) {
      error("expected 'end', 'else'");
    }

    switch (look.tag) {
      case Tag.END:
        match(Tag.END);
        break;
      case Tag.ELSE:
        match(Tag.ELSE);
        stat();
        match(Tag.END);
        break;
    }
  }
  
  private void idlist() {
    if (look.tag != Tag.ID) {
      error("expected identifier");
    }

    match(Tag.ID);
    idlistp();
  }
  
  private void idlistp() {
    //if (look.tag != ',' && look.tag != ';' && look.tag != Tag.END && look.tag != Tag.ELSE && look.tag != Tag.EOF && look.tag != '}' && look.tag != ')') {
    //  error("expected ',', ';', 'end', 'else', '}', ')', end of file");
    //}

    if (look.tag == ',') {
      match(',');
      match(Tag.ID);
      idlistp();
    }
  }
  
  private void bexpr() {
    if (look.tag != Tag.RELOP && look.tag != '!' && look.tag != Tag.AND && look.tag != Tag.OR) {
      error("expected '==', '<>', '<', '>', '<=', '>='");
    }

    match(Tag.RELOP);
    expr();
    expr();
  }
  
  private void exprlist() {
    if (look.tag != '+' && look.tag != '-' && look.tag != '*' && look.tag != '/' && look.tag != Tag.NUM && look.tag != Tag.ID) {
      error("expected '+', '-', '*', '/', number, identifier");
    }

    expr();
    exprlistp();
  }
  
  private void exprlistp() {
    //if (look.tag != ',' && look.tag != ')') {
    //  error("expected ',', ')'");
    //}

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
