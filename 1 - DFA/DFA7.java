/*
 * Progettare e implementare un DFA che riconosca il linguaggio di stringhe che
 * contengono il tuo nome e tutte le stringhe ottenute dopo la sostituzione di un carattere del nome
 * con un altro qualsiasi. Ad esempio, nel caso di uno studente che si chiama Paolo, il DFA deve
 * accettare la stringa "Paolo" (cioe il nome scritto correttamente), ma anche le stringhe "Pjolo",
 * "caolo", "Pa%lo", "Paola" e "Parlo" (il nome dopo la sostituzione di un carattere), ma non
 * "Eva", "Perro", "Pietro" oppure "P*o*o".
 */
public class DFA7 extends DeterministicFiniteAutomaton<Integer> {
  private final String name;
  
  public DFA7(String name) {
    this.name = name;
  }
  
  @Override
  public boolean isInAlphabet(char c) {
    return true;
  }
  
  @Override
  public Integer initialState() {
    return 0;
  }
  
  @Override
  public boolean isFinalState(Integer state) {
    return state == name.length() * 2;
  }
  
  @Override
  public Integer transit(Integer state, char c) {
    if (state < -1 || state > name.length() * 2) throw new IllegalStateException("Illegal state '" + state + "'");
    if (state == -1 || state == name.length() * 2) return -1;
    if (c == name.charAt(state % name.length())) {
      return state == name.length() -1 ? state + 1 + name.length() : state + 1;
    } else if (state < name.length()) {
      return state + 1 + name.length();
    } else {
      return -1;
    }
  }
  
  public static void main(String[] args) {
    main(new DFA7("Michi"), args);
  }
}
