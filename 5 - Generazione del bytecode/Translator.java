import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class Translator {
  private Lexer lex;
  private BufferedReader pbr;
  private Token look;
  
  SymbolTable st = new SymbolTable();
  CodeGenerator code = new CodeGenerator();
  int count = 0;
  
  public Translator(Lexer l, BufferedReader br) {
    lex = l;
    pbr = br;
    move();
  }
  
  void move() {
    look = lex.lexical_scan(pbr);
    System.out.println("token = " + look);
  }
  
  void error(String s) {
    throw new Error("near line " + lex.line + ": " + s + ", found '" + look + "'");
  }
  
  void match(int t) {
    if (look.tag == t) {
      if (look.tag != Tag.EOF) move();
    } else error("expected " + Tag.toString(t));
  }
  
  public void start() {
    prog();
    try {
      code.toJasmin();
    }
    catch(IOException e) {
      System.out.println("IO error\n");
    }
  }
  
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
    //if (look.tag != ';' && loSok.tag == '}' && look.tag != Tag.EOF) {
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
        List<Integer> to = idlist();
        for (int i = 0; i < to.size(); i++) {
          if (i < to.size() - 1) {
            code.emit(OpCode.dup);
          }
          code.emit(OpCode.istore, to.get(i));
        }
        break;
      case Tag.PRINT:
        match(Tag.PRINT);
        match('(');
        exprlist(new Instruction(OpCode.invokestatic, 1), false);
        match(')');
        break;
      case Tag.READ:
        match(Tag.READ);
        match('(');
        List<Integer> ids = idlist();
        match(')');
        for (int i = 0; i < ids.size(); i++) {
          code.emit(OpCode.invokestatic, 0);
          code.emit(OpCode.istore, ids.get(i));
        }
        break;
      case Tag.WHILE:
        int whileLabel = code.newLabel();
        int whileEndLabel = code.newLabel();
        
        match(Tag.WHILE);
        match('(');
        code.emitLabel(whileLabel);
        bexpr(whileEndLabel, false);
        match(')');
        stat();
        code.emit(OpCode.GOto, whileLabel);
        code.emitLabel(whileEndLabel);
        break;
      case Tag.IF:
        int thenEndLabel = code.newLabel();
        
        match(Tag.IF);
        match('(');
        bexpr(thenEndLabel, false);
        match(')');
        stat();
        statp(thenEndLabel);
        break;
      case '{':
        match('{');
        statlist();
        match('}');
        break;
    }
  }

  private void statp(int thenEndLabel) {
    if (look.tag != Tag.END && look.tag != Tag.ELSE) {
      error("expected 'end', 'else'");
    }

    switch (look.tag) {
      case Tag.END:
        code.emitLabel(thenEndLabel);
        match(Tag.END);
        break;
      case Tag.ELSE:
        int ifEndLabel = code.newLabel();
        code.emit(OpCode.GOto, ifEndLabel);
        code.emitLabel(thenEndLabel);
        match(Tag.ELSE);
        stat();
        match(Tag.END);
        code.emitLabel(ifEndLabel);
        break;
    }
  }
  
  private List<Integer> idlist() {
    if (look.tag != Tag.ID) {
      error("expected identifier");
    }

    List<Integer> ids = new LinkedList<>();
    if (look.tag == Tag.ID) {
      int id_addr = st.lookupAddress(((Word) look).lexeme);
      if (id_addr == -1) {
        id_addr = count;
        st.insert(((Word) look).lexeme, count++);
      }
      ids.add(id_addr);
    }
    match(Tag.ID);
    idlistp(ids);
    return ids;
  }
  
  private void idlistp(List<Integer> ids) {
    //if (look.tag != ',' && look.tag != ';' && look.tag != Tag.END && look.tag != Tag.ELSE && look.tag != Tag.EOF && look.tag != '}' && look.tag != ')') {
    //  error("expected ',', ';', 'end', 'else', '}', ')', end of file");
    //}

    if (look.tag == ',') {
      match(',');
      if (look.tag == Tag.ID) {
        int id_addr = st.lookupAddress(((Word) look).lexeme);
        if (id_addr == -1) {
          id_addr = count;
          st.insert(((Word) look).lexeme, count++);
        }
        ids.add(id_addr);
      }
      match(Tag.ID);
      idlistp(ids);
    }
  }
  
  private void bexpr(int label, boolean expected) {
    if (look.tag != Tag.RELOP && look.tag != '!' && look.tag != Tag.AND && look.tag != Tag.OR) {
      error("expected '==', '<>', '<', '>', '<=', '>=', '!', '&&', '||'");
    }

    Token t = look;
    // 5.2 & 5.3
    switch (t.tag) {
      case '!':
        match('!');
        bexpr(label, !expected);
        break;
      case Tag.AND:
        match(Tag.AND);
        if (expected) { // AND
          int andEndLabel = code.newLabel();
          bexpr(andEndLabel, false);
          bexpr(label, true);
          code.emitLabel(andEndLabel);
        } else { // NAND
          bexpr(label, false);
          bexpr(label, false);
        }
        break;
      case Tag.OR:
        match(Tag.OR);
        if (expected) { // OR
          bexpr(label, true);
          bexpr(label, true);
        } else { // NOR
          int orEndLabel = code.newLabel();
          bexpr(orEndLabel, true);
          bexpr(label, false);
          code.emitLabel(orEndLabel);
        }
        break;
      case Tag.RELOP:
    // 5.2 & 5.3
        match(Tag.RELOP);
        expr();
        expr();
        switch (((Word) t).lexeme) {
          case "==":
            code.emit(expected ? OpCode.if_icmpeq : OpCode.if_icmpne, label);
            break;
          case "<>":
            code.emit(expected ? OpCode.if_icmpne : OpCode.if_icmpeq, label);
            break;
          case "<":
            code.emit(expected ? OpCode.if_icmplt : OpCode.if_icmpge, label);
            break;
          case "<=":
            code.emit(expected ? OpCode.if_icmple : OpCode.if_icmpgt, label);
            break;
          case ">":
            code.emit(expected ? OpCode.if_icmpgt : OpCode.if_icmple, label);
            break;
          case ">=":
            code.emit(expected ? OpCode.if_icmpge : OpCode.if_icmplt, label);
            break;
        }
    // 5.2 & 5.3
        break;
    }
    // 5.2 & 5.3
  }

  private void expr() {
    if (look.tag != '+' && look.tag != '-' && look.tag != '*' && look.tag != '/' && look.tag != Tag.NUM && look.tag != Tag.ID) {
      error("expected '+', '-', '*', '/', number, identifier");
    }

    switch (look.tag) {
      case Tag.NUM:
        code.emit(OpCode.ldc, ((NumberTok) look).value);
        match(Tag.NUM);
        break;
      case Tag.ID:
        code.emit(OpCode.iload, st.lookupAddress(((Word) look).lexeme));
        match(Tag.ID);
        break;
      case '+':
        match('+');
        match('(');
        exprlist(new Instruction(OpCode.iadd), true);
        match(')');
        break;
      case '*':
        match('*');
        match('(');
        exprlist(new Instruction(OpCode.imul), true);
        match(')');
        break;
      case '-':
        match('-');
        expr();
        expr();
        code.emit(OpCode.isub);
        break;
      case '/':
        match('/');
        expr();
        expr();
        code.emit(OpCode.idiv);
        break;
    }
  }
  
  private void exprlist(Instruction instruction, boolean binary) {
    if (look.tag != '+' && look.tag != '-' && look.tag != '*' && look.tag != '/' && look.tag != Tag.NUM && look.tag != Tag.ID) {
      error("expected '+', '-', '*', '/', number, identifier");
    }

    expr();
    if (!binary) code.emit(instruction);
    exprlistp(instruction);
  }
  
  private void exprlistp(Instruction instruction) {
    //if (look.tag != ',' && look.tag != ')') {
    //  error("expected ',', ')'");
    //}

    if (look.tag == ',') {
      match(',');
      expr();
      code.emit(instruction);
      exprlistp(instruction);
    }
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
      Translator parser = new Translator(lex, br);
      parser.start();
      System.out.println("Input OK");
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
