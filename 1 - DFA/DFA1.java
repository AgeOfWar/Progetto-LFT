/*
 * Linguaggio delle stringhe di 0 e 1 che contengono 3 zeri consecutivi.
 */
public class DFA1 extends DeterministicFiniteAutomaton<Integer> {
  @Override
  public boolean isInAlphabet(char c) {
    return c == '0' || c == '1';
  }
  
  @Override
  public Integer initialState() {
    return 0;
  }
  
  @Override
  public boolean isFinalState(Integer state) {
    return state == 3;
  }
  
  @Override
  public Integer transit(Integer state, char c) {
    switch (state) {
      case 0:
        return c == '0' ? 1 : 0;
      case 1:
        return c == '0' ? 2 : 0;
      case 2:
        return c == '0' ? 3 : 0;
      case 3:
        return 3;
      default:
        throw new IllegalStateException("Illegal state '" + state + "'");
    }
  }
  
  public static void main(String[] args) {
    main(new DFA1(), args);
  }
}
