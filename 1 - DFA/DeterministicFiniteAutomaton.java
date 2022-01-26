public abstract class DeterministicFiniteAutomaton {
  public abstract boolean isInAlphabet(char c);
  public abstract int initialState();
  public abstract boolean isFinalState(int state);
  public abstract int transit(int state, char c);
  
  public int transit(int state, String string) {
    if (string.isEmpty()) { // δ^(q, ε) = q
      return state;
    }
    
    String w = string.substring(0, string.length() - 1); // δ^(q, wa) = δ(δ^(q, w), a)
    char a = string.charAt(string.length() - 1);
    if (!isInAlphabet(a)) {
      throw new IllegalArgumentException("character '" + a + "' is not in the alphabet");
    }
    return transit(transit(state, w), a);
  }
  
  public boolean test(String string) {
    return isFinalState(transit(initialState(), string));
  }
  
  public static void main(DeterministicFiniteAutomaton dfa, String[] args) {
    if (args.length == 0) {
      System.err.println("Usage: <string> [strings...]");
      return;
    }
    
    for (int i = 0; i < args.length; i++) {
      try {
        System.out.println(args[i] + " -> " + dfa.test(args[i]));
      } catch (IllegalArgumentException e) {
        System.out.println(args[i] + " -> false (" + e.getMessage() + ")");
      }
    }
  }
}
