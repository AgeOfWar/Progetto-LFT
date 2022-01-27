import static java.lang.Character.*;

/**
 * Progettare e implementare un DFA che riconosca il linguaggio di stringhe che contengono un numero di matricola
 * seguito (subito) da un cognome, dove la combinazione di matricola e cognome corrisponde a studenti del turno
 * T2 o del turno T3 del laboratorio di Linguaggi Formali e Traduttori.
 * Si ricorda le regole per suddivisione di studenti in turni:
 * - Turno T2: cognomi la cui iniziale e compresa tra A e K, e la penultima cifra del numero di matricola e pari;
 * - Turno T3: cognomi la cui iniziale e compresa tra L e Z, e la penultima cifra del numero di matricola e dispari;
 * Un numero di matricola deve essere composto di almeno due cifre, ma (come in Esercizio 1.3)
 * non ha un numero massimo prestabilito di cifre. Assicurarsi che il DFA sia minimo.
 * Esempi di stringhe accettate: "654321Bianchi", "123456Rossi", "221B"
 * Esempi di stringhe non accettate: "123456Bianchi", "654321Rossi", "5", "654322", "Rossi",
 * "2Bianchi"
 */
public class DFA6 {
  public static boolean scan(String s) {
    int state = 0;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      if (!isLetter(c) && !isDigit(c)) {
        throw new IllegalArgumentException("character '" + c + "' is not in the alphabet");
      }

      switch (state) {
        case 0:
          switch (c) {
            case '0':
            case '2':
            case '4':
            case '6':
            case '8':
              state = 1;
              break;
            case '1':
            case '3':
            case '5':
            case '7':
            case '9':
              state = 2;
              break;
            default:
              state = -1;
              break;
          }
          break;
        case 1:
          switch (c) {
            case '0':
            case '2':
            case '4':
            case '6':
            case '8':
              state = 3;
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
        case 2:
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
              state = 6;
              break;
            default:
              state = -1;
              break;
          }
          break;
        case 3:
          if (c >= 'A' && c <= 'K') state = 7;
          switch (c) {
            case '0':
            case '2':
            case '4':
            case '6':
            case '8':
              state = 3;
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
        case 4:
          if (c >= 'A' && c <= 'K') state = 7;
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
              state = 6;
              break;
            default:
              state = -1;
              break;
          }
          break;
        case 5:
          if (c >= 'L' && c <= 'Z') state = 7;
          switch (c) {
            case '0':
            case '2':
            case '4':
            case '6':
            case '8':
              state = 3;
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
        case 6:
          if (c >= 'L' && c <= 'Z') state = 7;
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
              state = 6;
              break;
            default:
              state = -1;
              break;
          }
          break;
        case 7:
          state = isLowerCase(c) ? 7 : -1;
          break;
        case -1:
          state = -1;
          break;
      }
    }
    return state == 7;
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
