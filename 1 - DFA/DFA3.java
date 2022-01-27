import static java.lang.Character.*;

/*
 * Progettare e implementare un DFA che riconosca il linguaggio di stringhe che
 * contengono un numero di matricola seguito (subito) da un cognome, dove la combinazione di
 * matricola e cognome corrisponde a studenti del corso A che hanno un numero di matricola pari
 * oppure a studenti del corso B che hanno un numero di matricola dispari. Si ricorda che gli
 * studenti del corso A sono quelli con cognomi la cui iniziale e compresa tra A e K, e gli studenti
 * del corso B sono quelli con cognomi la cui iniziale e compresa tra L e Z.
 * Per esempio, "123456Bianchi" e "654321Rossi" sono stringhe del linguaggio, mentre
 * "654321Bianchi" e "123456Rossi" no. Nel contesto di questo esercizio, un numero di matricola non ha un
 * numero prestabilito di cifre (ma deve essere composto di almeno una cifra). Un
 * cognome corrisponde a una sequenza di lettere, e deve essere composto di almeno una lettera.
 * Quindi l’automa deve accettare le stringhe "2Bianchi" e "122B" ma non "654322" e "Rossi".
 * Assicurarsi che il DFA sia minimo.
 */
public class DFA3 {
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
          if (c >= 'A' && c <= 'K') state = 3; else
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
        case 2:
          if (c >= 'L' && c <= 'Z') state = 3; else
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
        case 3:
          state = isLowerCase(c) ? 3 : -1;
          break;
        case -1:
          state = -1;
          break;
      }
    }
    return state == 3;
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
