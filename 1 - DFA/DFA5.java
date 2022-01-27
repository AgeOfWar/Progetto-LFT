import static java.lang.Character.*;

public class DFA5 {
  public static boolean scan(String s) {
    int state = 0;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      if (!isLetter(c) && !isDigit(c)) {
        throw new IllegalArgumentException("character '" + c + "' is not in the alphabet");
      }

      switch (state) {
        case 0:
          state = c >= 'A' && c <= 'K' ? 1 : c >= 'L' && c <= 'Z' ? 2 : -1;
          break;
        case 1:
          if (isLowerCase(c)) state = 1;
          switch (c) {
            case '0':
            case '2':
            case '4':
            case '6':
            case '8':
              state = 5;
              break;
            case '1':
            case '3':
            case '5':
            case '7':
            case '9':
              state = 3;
              break;
            default:
              state = -1;
              break;
          }
          break;
        case 2:
          if (isLowerCase(c)) state = 2;
          switch (c) {
            case '0':
            case '2':
            case '4':
            case '6':
            case '8':
              state = 6;
              break;
            case '1':
            case '3':
            case '5':
            case '7':
            case '9':
              state = 4;
              break;
            default:
              state = -1;
              break;
          }
          break;
        case 3:
        case 5:
          switch (c) {
            case '0':
            case '2':
            case '4':
            case '6':
            case '8':
              state = 5;
              break;
            case '1':
            case '3':
            case '5':
            case '7':
            case '9':
              state = 3;
              break;
            default:
              state = -1;
              break;
          }
          break;
        case 4:
        case 6:
          switch (c) {
            case '0':
            case '2':
            case '4':
            case '6':
            case '8':
              state = 6;
              break;
            case '1':
            case '3':
            case '5':
            case '7':
            case '9':
              state = 4;
              break;
            default:
              state = -1;
              break;
          }
          break;
        case -1:
          state = -1;
          break;
      }
    }
    return state >= 5;
  }
  
  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.println("Usage: <string> [strings...]");
      return;
    }

    for (int i = 0; i < args.length; i++) {
      try {
        System.out.println(args[i] + " -> " + scan(args[i]));
      } catch (IllegalArgumentException e) {
        System.out.println(args[i] + " -> false (" + e.getMessage() + ")");
      }
    }
  }
}

