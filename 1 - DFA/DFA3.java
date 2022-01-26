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
public class DFA3 extends DeterministicFiniteAutomaton {
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
    return state == 3;
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
        if (c >= 'A' && c <= 'K') return 3;
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
      case 2:
        if (c >= 'L' && c <= 'Z') return 3;
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
      case 3:
        return isLowerCase(c) ? 3 : -1;
      case -1:
        return -1;
      default:
        throw new IllegalStateException("Illegal state '" + state + "'");
    }
  }
  
  public static void main(String[] args) {
    main(new DFA3(), args);
  }
}
