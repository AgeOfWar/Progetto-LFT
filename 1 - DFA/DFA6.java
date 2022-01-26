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
public class DFA6 extends DeterministicFiniteAutomaton {
  @Override
  public boolean isInAlphabet(char c) {
    return isLetter(c) || isDigit(c);
  }
  
  @Override
  public int initialState() {
    return 0;
  }
  
  @Override
  public boolean isFinalState(int state) {
    return state == 7;
  }
  
  @Override
  public int transit(int state, char c) {
    switch (state) {
      case 0:
        switch (c) {
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
        switch (c) {
          case '0':
          case '2':
          case '4':
          case '6':
          case '8':
            return 3;
          case '1':
          case '3':
          case '5':
          case '7':
          case '9':
            return 4;
          default:
            return -1;
        }
      case 2:
        switch (c) {
          case '0':
          case '2':
          case '4':
          case '6':
          case '8':
            return 5;
          case '1':
          case '3':
          case '5':
          case '7':
          case '9':
            return 6;
          default:
            return -1;
        }
      case 3:
        if (c >= 'A' && c <= 'K') return 7;
        switch (c) {
          case '0':
          case '2':
          case '4':
          case '6':
          case '8':
            return 3;
          case '1':
          case '3':
          case '5':
          case '7':
          case '9':
            return 4;
          default:
            return -1;
        }
      case 4:
        if (c >= 'A' && c <= 'K') return 7;
        switch (c) {
          case '0':
          case '2':
          case '4':
          case '6':
          case '8':
            return 5;
          case '1':
          case '3':
          case '5':
          case '7':
          case '9':
            return 6;
          default:
            return -1;
        }
      case 5:
        if (c >= 'L' && c <= 'Z') return 7;
        switch (c) {
          case '0':
          case '2':
          case '4':
          case '6':
          case '8':
            return 3;
          case '1':
          case '3':
          case '5':
          case '7':
          case '9':
            return 4;
          default:
            return -1;
        }
      case 6:
        if (c >= 'L' && c <= 'Z') return 7;
        switch (c) {
          case '0':
          case '2':
          case '4':
          case '6':
          case '8':
            return 5;
          case '1':
          case '3':
          case '5':
          case '7':
          case '9':
            return 6;
          default:
            return -1;
        }
      case 7:
        return isLowerCase(c) ? 7 : -1;
      case -1:
        return -1;
      default:
        throw new IllegalStateException("Illegal state '" + state + "'");
    }
  }
  
  public static void main(String[] args) {
    main(new DFA6(), args);
  }
}
