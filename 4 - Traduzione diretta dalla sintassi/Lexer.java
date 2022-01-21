import java.io.*;

public class Lexer {
  
  public int line = 1;
  private char peek = ' ';
  
  private void readch(BufferedReader br) {
    try {
      peek = (char) br.read();
    } catch (IOException exc) {
      peek = (char) -1; // ERROR
    }
  }
  
  public Token lexical_scan(BufferedReader br) {
    while (peek == ' ' || peek == '\t' || peek == '\n'  || peek == '\r') {
      if (peek == '\n') line++;
      readch(br);
    }
    
    switch (peek) {
      case '!':
        peek = ' ';
        return Token.not;
        
      // ... gestire i casi di ( ) { } + - * / ; , ... //
      case '(':
        peek = ' ';
        return Token.lpt;
  
      case ')':
        peek = ' ';
        return Token.rpt;
  
      case '{':
        peek = ' ';
        return Token.lpg;
  
      case '}':
        peek = ' ';
        return Token.rpg;
  
      case '+':
        peek = ' ';
        return Token.plus;
        
      case '-':
        peek = ' ';
        return Token.minus;
        
      case '*':
        peek = ' ';
        return Token.mult;
        
      // 2.3
      case '/':
        readch(br);
        switch (peek) {
          case '/':
            do {
              readch(br);
            } while (peek != '\n' && peek != (char) -1);
            return lexical_scan(br);
          case '*':
            while (true) {
              switch (peek) {
                case '\n':
                  line++;
                  readch(br);
                  break;
                case (char) -1:
                  System.err.println("Unclosed comment at EOF");
                  return null;
                case '*':
                  readch(br);
                  if (peek == '/') {
                    peek = ' ';
                    return lexical_scan(br);
                  }
                default:
                  readch(br);
              }
            }
          default:
            return Token.div;
        }
      // 2.3
        
      case ';':
        peek = ' ';
        return Token.semicolon;
        
      case ',':
        peek = ' ';
        return Token.comma;
      // ... gestire i casi di ( ) { } + - * / ; , ... //
  
      case '&':
        readch(br);
        if (peek == '&') {
          peek = ' ';
          return Word.and;
        } else {
          System.err.println("Erroneous character"
              + " after & : "  + peek );
          return null;
        }
  
      // ... gestire i casi di || < > <= >= == <> ... //
      case '|':
        readch(br);
        if (peek == '|') {
          peek = ' ';
          return Word.or;
        } else {
          System.err.println("Erroneous character"
              + " after | : "  + peek );
          return null;
        }
  
      case '<':
        readch(br);
        if (peek == '=') {
          peek = ' ';
          return Word.le;
        } else if (peek == '>') {
          peek = ' ';
          return Word.ne;
        } else {
          return Word.lt;
        }
  
      case '>':
        readch(br);
        if (peek == '=') {
          peek = ' ';
          return Word.ge;
        } else {
          return Word.gt;
        }
  
      case '=':
        readch(br);
        if (peek == '=') {
          peek = ' ';
          return Word.eq;
        } else {
          System.err.println("Erroneous character"
              + " after = : "  + peek );
          return null;
        }
      // ... gestire i casi di || < > <= >= == <> ... //
      
      case (char)-1:
        return new Token(Tag.EOF);
      
      default:
        if (Character.isLetter(peek) || peek == '_') {
  
          // ... gestire il caso degli identificatori e delle parole chiave //
          // 2.2
          String word = Character.toString(peek);
          boolean onlyUnderscores = peek == '_';
          
          readch(br);
          while (Character.isLetterOrDigit(peek) || peek == '_') {
            word += peek;
            onlyUnderscores &= peek == '_';
            readch(br);
          }
          // 2.2
  
          switch (word) {
            case "assign":
              return Word.assign;
            case "to":
              return Word.to;
            case "if":
              return Word.iftok;
            case "else":
              return Word.elsetok;
            case "while":
              return Word.whiletok;
            case "begin":
              return Word.begin;
            case "end":
              return Word.end;
            case "print":
              return Word.print;
            case "read":
              return Word.read;
            default:
              // 2.2
              if (onlyUnderscores) {
                System.err.println("Invalid identifier: " + word);
                return null;
              } else {
                return new Word(Tag.ID, word);
              }
              // 2.2
          }
          // ... gestire il caso degli identificatori e delle parole chiave //
          
        } else if (Character.isDigit(peek)) {
  
          // ... gestire il caso dei numeri ... //
          if (peek == '0') {
            readch(br);
            if (Character.isDigit(peek)) {
              System.err.println("Erroneous character after 0: "
                  + peek );
              return null;
            } else {
              return new NumberTok(0);
            }
          } else {
            String number = Character.toString(peek);
            readch(br);
            while (Character.isDigit(peek)) {
              number += Character.toString(peek);
              readch(br);
            }
            return new NumberTok(Integer.parseInt(number));
          }
          // ... gestire il caso dei numeri ... //
          
        } else {
          System.err.println("Erroneous character: "
              + peek );
          return null;
        }
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
      Token tok;
      do {
        tok = lex.lexical_scan(br);
        System.out.println("Scan: " + tok);
      } while (tok.tag != Tag.EOF);
      br.close();
    } catch (IOException e) {e.printStackTrace();}
  }
  
}
