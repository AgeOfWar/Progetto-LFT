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
public class DFA4 extends DeterministicFiniteAutomaton<Integer> {
  @Override
  public boolean isInAlphabet(char c) {
    return isLetter(c) || isDigit(c) || c == ' ';
  }
  
  @Override
  public Integer initialState() {
    return 0;
  }
  
  @Override
  public boolean isFinalState(Integer state) {
    return state >= 5;
  }
  
  @Override
  public Integer transit(Integer state, char c) {
    switch (state) {
      case 0:
        switch (c) {
          case ' ':
            return 0;
          case '0':
          case '2':
          case '4':
          case '6':
          case '8':
            return 1;
          case '1':
          case '3':
          case '5':
          case '7':
          case '9':
            return 2;
          default:
            return -1;
        }
      case 1:
        if (c >= 'A' && c <= 'K') return 5;
        switch (c) {
          case ' ':
            return 3;
          case '0':
          case '2':
          case '4':
          case '6':
          case '8':
            return 1;
          case '1':
          case '3':
          case '5':
          case '7':
          case '9':
            return 2;
          default:
            return -1;
        }
      case 2:
        if (c >= 'L' && c <= 'Z') return 5;
        switch (c) {
          case ' ':
            return 4;
          case '0':
          case '2':
          case '4':
          case '6':
          case '8':
            return 1;
          case '1':
          case '3':
          case '5':
          case '7':
          case '9':
            return 2;
          default:
            return -1;
        }
      case 3:
        return c >= 'A' && c <= 'K' ? 5 : c == ' ' ? 3 : -1;
      case 4:
        return c >= 'L' && c <= 'Z' ? 5 : c == ' ' ? 4 : -1;
      case 5:
        return c == ' ' ? 6 : isLowerCase(c) ? 5 : -1;
      case 6:
        return c == ' ' ? 6 : isUpperCase(c) ? 5 : -1;
      case -1:
        return -1;
      default:
        throw new IllegalStateException("Illegal state '" + state + "'");
    }
  }
  
  public static void main(String[] args) {
    main(new DFA4(), args);
  }
}