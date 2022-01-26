import static java.lang.Character.isJavaIdentifierPart;
import static java.lang.Character.isLetter;

/*
 * Progettare e implementare un DFA che riconosca il linguaggio degli identificatori
 * in un linguaggio in stile Java: un identificatore e una sequenza non vuota di lettere, numeri, ed il
 * simbolo di "underscore" _ che non comincia con un numero e che non puo essere composto solo
 * dal simbolo _. Compilare e testare il suo funzionamento su un insieme significativo di esempi.
 * Esempi di stringhe accettate: "x", "flag1", "x2y2", "x_1", "lft_lab", "_temp", "x_1_y_2",
 * "x_", "_5"
 * Esempi di stringhe non accettate: "5", "221B", "123", "9_to_5", "___"
 */
public class DFA2 extends DeterministicFiniteAutomaton {
  @Override
  public boolean isInAlphabet(char c) {
    return isJavaIdentifierPart(c);
  }
  
  @Override
  public int initialState() {
    return 0;
  }
  
  @Override
  public boolean isFinalState(int state) {
    return state == 2;
  }
  
  @Override
  public int transit(int state, char c) {
    switch (state) {
      case 0:
        return c == '_' ? 1 : isLetter(c) ? 2 : -1;
      case 1:
        return c == '_' ? 1 : 2;
      case 2:
        return 2;
      case -1:
        return -1;
      default:
        throw new IllegalStateException("Illegal state '" + state + "'");
    }
  }
  
  public static void main(String[] args) {
    main(new DFA2(), args);
  }
}
