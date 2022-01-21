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
    prog();
    try {
      code.toJasmin();
    }
    catch(java.io.IOException e) {
      System.out.println("IO error\n");
    }
  }
  
  private void expr() {
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
        match('(', "'(' expected after '+'");
        exprlistBinary(new Instruction(OpCode.iadd));
        match(')', "unclosed sum");
        break;
      case '*':
        match('*');
        match('(', "'(' expected after '*'");
        exprlistBinary(new Instruction(OpCode.imul));
        match(')', "unclosed product");
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
      default:
        error("expression expected (constant, variable, '+ expr expr', '* expr expr', '- expr expr', '/ expr expr')");
    }
  }
  
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
        List<String> to = idlist();
        for (int i = 0; i < to.size(); i++) {
          if (i < to.size() - 1) {
            code.emit(OpCode.dup);
          }
          int id_addr = st.lookupAddress(to.get(i));
          if (id_addr == -1) {
            id_addr = count;
            st.insert(to.get(i), count++);
          }
          code.emit(OpCode.istore, id_addr);
        }
        break;
      case Tag.PRINT:
        match(Tag.PRINT);
        match('(', "expected '(' after 'print'");
        exprlistUnary(new Instruction(OpCode.invokestatic, 1));
        match(')', "unclosed print '(");
        break;
      case Tag.READ:
        match(Tag.READ);
        match('(', "expected '(' after 'read'");
        List<String> ids = idlist();
        match(')', "unclosed read '(");
  
        for (String id : ids) {
          int id_addr = st.lookupAddress(id);
          if (id_addr == -1) {
            id_addr = count;
            st.insert(id, count++);
          }
          code.emit(OpCode.invokestatic, 0);
          code.emit(OpCode.istore, id_addr);
        }
        break;
      case Tag.WHILE:
        int whileLabel = code.newLabel();
        int whileEndLabel = code.newLabel();
        
        match(Tag.WHILE);
        match('(', "expected '(' after 'while'");
        code.emitLabel(whileLabel);
        bexpr(whileEndLabel, false);
        match(')', "unclosed while condition '(");
        stat();
        code.emit(OpCode.GOto, whileLabel);
        code.emitLabel(whileEndLabel);
        break;
      case Tag.IF:
        int elseLabel = code.newLabel();
        
        match(Tag.IF);
        match('(', "expected '(' after 'if'");
        bexpr(elseLabel, false);
        match(')', "unclosed if condition '(");
        stat();
        switch (look.tag) {
          case Tag.END:
            code.emitLabel(elseLabel);
            match(Tag.END);
            break;
          case Tag.ELSE:
            int ifEndLabel = code.newLabel();
            code.emit(OpCode.GOto, ifEndLabel);
            code.emitLabel(elseLabel);
            match(Tag.ELSE);
            stat();
            match(Tag.END, "expected 'end' after 'else'");
            code.emitLabel(ifEndLabel);
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
  
  private List<String> idlist() {
    List<String> ids = new LinkedList<>();
    if (look.tag == Tag.ID) {
      ids.add(((Word) look).lexeme);
    }
    match(Tag.ID, "expected identifier");
    idlistp(ids);
    return ids;
  }
  
  private void idlistp(List<String> ids) {
    if (look.tag == ',') {
      match(',');
      if (look.tag == Tag.ID) {
        ids.add(((Word) look).lexeme);
      }
      match(Tag.ID, "expected identifier");
      idlistp(ids);
    }
  }
  
  private void bexpr(int label, boolean expected) {
    Token t = look;
    switch (t.tag) {
      case '!':
        match('!');
        bexpr(label, !expected);
        break;
      case Tag.AND:
        match(Tag.AND);
        if (expected) {
          int andEndLabel = code.newLabel();
          bexpr(andEndLabel, false);
          bexpr(label, true);
          code.emitLabel(andEndLabel);
        } else {
          bexpr(label, false);
          bexpr(label, false);
        }
        break;
      case Tag.OR:
        match(Tag.OR);
        if (expected) {
          bexpr(label, true);
          bexpr(label, true);
        } else {
          int orEndLabel = code.newLabel();
          bexpr(orEndLabel, true);
          bexpr(label, false);
          code.emitLabel(orEndLabel);
        }
        break;
      default:
        match(Tag.RELOP, "expected boolean expression ('[<,<=,>,>=,==,<>] expr expr', '[&&,||] expr expr', '! expr')");
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
        break;
    }
  }
  
  private void exprlistUnary(Instruction instruction) {
    expr();
    code.emit(instruction);
    exprlistp(instruction);
  }
  
  private void exprlistBinary(Instruction instruction) {
    expr();
    exprlistp(instruction);
  }
  
  private void exprlistp(Instruction instruction) {
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