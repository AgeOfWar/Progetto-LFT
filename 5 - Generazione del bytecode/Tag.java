public class Tag {
  public final static int
      EOF = -1, NUM = 256, ID = 257, RELOP = 258,
      ASSIGN = 259, TO = 260, IF = 261, ELSE = 262,
      WHILE = 263, BEGIN = 264, END = 265, PRINT = 266, READ = 267,
      OR = 268, AND = 269;

  public static String toString(int tag) {
    switch (tag) {
      case -1:
        return "EOF";
      case 256:
        return "number";
      case 257:
        return "identifier";
      case 258:
        return "RELOP";
      case 259:
        return "assign";
      case 260:
        return "to";
      case 261:
        return "id";
      case 262:
        return "else";
      case 263:
        return "while";
      case 264:
        return "begin";
      case 265:
        return "end";
      case 266: 
        return "print";
      case 267:
        return "read";
      case 268:
        return "||";
      case 269:
        return "&&";
      default:
        return Character.toString(tag);
    }
  }
}
