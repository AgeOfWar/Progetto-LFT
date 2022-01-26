/*
 * Modificare l’automa dell’esercizio precedente in modo che riconosca il linguaggio di stringhe (sull’alfabeto {/, *, a})
 * che contengono "commenti" delimitati da &#47;* e *&#47;,ma con
 * la possibilita di avere stringhe prima e dopo come specificato qui di seguito. L’idea è che sia
 * possibile avere eventualmente commenti (anche multipli) immersi in una sequenza di simboli
 * dell’alfabeto. Quindi l’unico vincolo e che l’automa deve accettare le stringhe in cui un’occorrenza
 * della sequenza &#47;* deve essere seguita (anche non immediatamente) da un’occorrenza della
 * sequenza *&#47;.Le stringhe del linguaggio possono non avere nessuna occorrenza della sequenza
 * &#47;* (caso della sequenza di simboli senza commenti). Implementare l’automa seguendo la costruzione vista in Listing 1.
 */
public class DFA10 extends DeterministicFiniteAutomaton {
  @Override
  public boolean isInAlphabet(char c) {
    return c == '/' || c == '*' || c == 'a';
  }
  
  @Override
  public int initialState() {
    return 0;
  }
  
  @Override
  public boolean isFinalState(int state) {
    return state <= 1;
  }
  
  @Override
  public int transit(int state, char c) {
    switch (state) {
      case 0:
        return c == '/' ? 1 : 0;
      case 1:
        return c == '*' ? 2 : c == '/' ? 1 : 0;
      case 2:
        return c == '*' ? 3 : 2;
      case 3:
        switch (c) {
          case '/':
            return 0;
          case '*':
            return 3;
          case 'a':
            return 2;
          default:
            throw new AssertionError();
        }
      default:
        throw new IllegalStateException("Illegal state '" + state + "'");
    }
  }
  
  public static void main(String[] args) {
    main(new DFA10(), args);
  }
}
