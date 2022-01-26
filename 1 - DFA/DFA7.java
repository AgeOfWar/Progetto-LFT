/*
 * Progettare e implementare un DFA che riconosca il linguaggio di stringhe che
 * contengono il tuo nome e tutte le stringhe ottenute dopo la sostituzione di un carattere del nome
 * con un altro qualsiasi. Ad esempio, nel caso di uno studente che si chiama Paolo, il DFA deve
 * accettare la stringa "Paolo" (cioe il nome scritto correttamente), ma anche le stringhe "Pjolo",
 * "caolo", "Pa%lo", "Paola" e "Parlo" (il nome dopo la sostituzione di un carattere), ma non
 * "Eva", "Perro", "Pietro" oppure "P*o*o".
 */
public class DFA7 extends DeterministicFiniteAutomaton {
  @Override
  public boolean isInAlphabet(char c) {
    return true;
  }
  
  @Override
  public int initialState() {
    return 0;
  }
  
  @Override
  public boolean isFinalState(int state) {
    return state == 9;
  }
  
  @Override
  public int transit(int state, char c) {
    switch (state) {
      case 0:
        return c == 'M' ? 1 : 5;
      case 1:
        return c == 'i' ? 2 : 6;
      case 2:
        return c == 'c' ? 3 : 7;
      case 3:
        return c == 'h' ? 4 : 8;
      case 4:
        return 9;
      case 5:
        return c == 'i' ? 6 : -1;
      case 6:
        return c == 'c' ? 7 : -1;
      case 7:
        return c == 'h' ? 8 : -1;
      case 8:
        return c == 'i' ? 9 : -1;
      case 9:
      case -1:
        return -1;
      default:
        throw new IllegalStateException("Illegal state '" + state + "'");
    }
  }
  
  public static void main(String[] args) {
    main(new DFA7(), args);
  }
}
