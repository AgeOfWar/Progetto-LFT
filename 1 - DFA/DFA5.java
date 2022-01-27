import static java.lang.Character.*;

/*
 * Progettare e implementare un DFA che, come in Esercizio 1.3, riconosca il linguaggio di
 * stringhe che contengono matricola e cognome degli studenti del corso A che hanno un
 * numero di matricola pari oppure a studenti del corso B che hanno un numero di matricola
 * dispari, ma in cui il cognome precede il numero di matricola (in altre parole, le posizioni
 * del cognome e matricola sono scambiate rispetto all’Esercizio 1.3).
 */
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
          if (isLowerCase(c)) state = 1; else
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
          if (isLowerCase(c)) state = 2; else
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

