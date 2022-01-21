import static java.lang.Character.isDigit;

/*
 * Progettare e implementare un DFA che riconosca il linguaggio delle costanti numeriche in virgola mobile
 * utilizzando la notazione scientifica dove il simbolo e indica la funzione esponenziale con base 10.
 * L’alfabeto del DFA contiene i seguenti elementi: le cifre numeriche
 * 0, 1, . . . , 9, il segno . (punto) che precede una eventuale parte decimale, i segni + (piu) e - (meno)
 * per indicare positivita o negatività, e il simbolo e.
 * Le stringhe accettate devono seguire le solite regole per la scrittura delle costanti numeriche.
 * In particolare, una costante numerica consiste di due segmenti, la seconda di quale e opzionale:
 * il primo segmento e una sequenza di cifre numeriche che (1) può essere preceduta da un segno
 * + o meno -, (2) puo essere seguita da un segno punto., che a sua volta deve essere seguito da
 * una sequenza non vuota di cifre numeriche; il secondo segmento inizia con il simbolo e, che a
 * sua volta e seguito da una sequenza di cifre numeriche che soddisfa i punti (1) e (2) scritti per il
 * primo segmento. Si nota che, sia nel primo segmento, sia in un eventuale secondo segmento, un
 * segno punto . non deve essere preceduto per forza da una cifra numerica.
 * Esempi di stringhe accettate: "123", "123.5", ".567", "+7.5", "-.7", "67e10", "1e-2",
 * "-.7e2", "1e2.3"
 * Esempi di stringhe non accettate: ".", "e3", "123.", "+e6", "1.2.3", "4e5e6", "++3"
 */
public class DFA8 extends DeterministicFiniteAutomaton<Integer> {
  @Override
  public boolean isInAlphabet(char c) {
    return isDigit(c) || isSign(c) || c == '.' || c == 'e';
  }
  
  @Override
  public Integer initialState() {
    return 0;
  }
  
  @Override
  public boolean isFinalState(Integer state) {
    return state == 2 || state == 4 || state == 7 || state == 9;
  }
  
  @Override
  public Integer transit(Integer state, char c) {
    switch (state) {
      case 0:
        return isSign(c) ? 1 : isDigit(c) ? 2 : c == '.' ? 3 : -1;
      case 1:
        return isDigit(c) ? 2 : c == '.' ? 3 : -1;
      case 2:
        return isDigit(c) ? 2 : c == '.' ? 3 : c == 'e' ? 5 : -1;
      case 3:
        return isDigit(c) ? 4 : -1;
      case 4:
        return isDigit(c) ? 4 : c == 'e' ? 5 : -1;
      case 5:
        return isSign(c) ? 6 : isDigit(c) ? 7 : c == '.' ? 8 : -1;
      case 6:
      case 7:
        return isDigit(c) ? 7 : c == '.' ? 8 : -1;
      case 8:
      case 9:
        return isDigit(c) ? 9 : -1;
      case -1:
        return -1;
      default:
        throw new IllegalStateException("Illegal state '" + state + "'");
    }
  }
  
  private boolean isSign(char c) {
    return c == '+' || c == '-';
  }
  
  public static void main(String[] args) {
    main(new DFA8(), args);
  }
}
