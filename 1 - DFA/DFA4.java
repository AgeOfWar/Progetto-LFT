import static java.lang.Character.*;
import static java.lang.Character.isLowerCase;

/*
 * Modificare l’automa dell’esercizio precedente in modo che riconosca le combinazioni di
 * matricola e cognome degli studenti del corso A che hanno un numero di matricola
 * pari oppure a studenti del corso B che hanno un numero di matricola dispari, dove il numero
 * di matricola e il cognome possono essere separati da una sequenza di spazi, e possono essere
 * precedute e/o seguite da sequenze eventualmente vuote di spazi. Per esempio, l’automa deve
 * accettare la stringa "654321 Rossi" e " 123456 Bianchi " (dove, nel secondo esempio, ci
 * sono spazi prima del primo carattere e dopo l’ultimo carattere), ma non "1234 56Bianchi" e
 * "123456Bia nchi". Per questo esercizio, i cognomi composti (con un numero arbitrario di parti)
 * possono essere accettati: per esempio, la stringa "123456De Gasperi" deve essere accettato.
 * Modificare l’implementazione Java dell’automa di conseguenza.
 */
public class DFA4 {
  public static boolean scan(String s) {
    int state = 0;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      if (!isLetter(c) && !isDigit(c) && c != ' ') {
        throw new IllegalArgumentException("character '" + c + "' is not in the alphabet");
      }

      switch (state) {
        case 0:
          switch (c) {
            case ' ':
              state = 0;
              break;
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
          if (c >= 'A' && c <= 'K') state = 5; else
          switch (c) {
            case ' ':
              state = 3;
              break;
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
          if (c >= 'L' && c <= 'Z') state = 5; else
          switch (c) {
            case ' ':
              state = 4;
              break;
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
          state = c >= 'A' && c <= 'K' ? 5 : c == ' ' ? 3 : -1;
          break;
        case 4:
          state = c >= 'L' && c <= 'Z' ? 5 : c == ' ' ? 4 : -1;
          break;
        case 5:
          state = c == ' ' ? 6 : isLowerCase(c) ? 5 : -1;
          break;
        case 6:
          state = c == ' ' ? 6 : isUpperCase(c) ? 5 : -1;
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