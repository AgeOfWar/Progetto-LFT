/**
 * Progettare e implementare un DFA con alfabeto {/, *, a} che riconosca il linguaggio di "commenti" delimitati da
 * /* (all’inizio) e *&#47; (alla fine):cioe l’automa deve accettare le
 * stringhe che contengono almeno 4caratteri che iniziano con /*, che finiscono con *&#47;,
 * e che contengono una sola occorrenza della sequenza*&#47;,quella finale(dove l’asterisco della sequenza*&#47;
 * non deve essere in comune con quello della sequenza /* all’inizio).
 * Esempi di stringhe accettate: "/****&#47;", "/*a*a*&#47;", "/*a/**&#47;", "/**a///a/a**&#47;", "/**&#47;", "/*&#47;*&#47;"
 * Esempi di stringhe non accettate: "/*&#47;", "/**&#47;***&#47;"
 */
public class DFA9 extends DeterministicFiniteAutomaton {
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
    return state == 4;
  }
  
  @Override
  public int transit(int state, char c) {
    switch (state) {
      case 0:
        return c == '/' ? 1 : -1;
      case 1:
        return c == '*' ? 2 : -1;
      case 2:
        return c == '*' ? 3 : 2;
      case 3:
        switch (c) {
          case '/':
            return 4;
          case '*':
            return 3;
          case 'a':
            return 2;
          default:
            throw new AssertionError();
        }
      case -1:
      case 4:
        return -1;
      default:
        throw new IllegalStateException("Illegal state '" + state + "'");
    }
  }
  
  public static void main(String[] args) {
    main(new DFA9(), args);
  }
}
