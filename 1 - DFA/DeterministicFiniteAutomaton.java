public abstract class DeterministicFiniteAutomaton<S> {
  public abstract boolean isInAlphabet(char c);
  public abstract S initialState();
  public abstract boolean isFinalState(S state);
  public abstract S transit(S state, char c);
  
  public S transit(S state, String string) {
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
  
  public DeterministicFiniteAutomaton<S> negate() {
    return new DeterministicFiniteAutomaton<S>() {
      @Override
      public boolean isInAlphabet(char c) {
        return DeterministicFiniteAutomaton.this.isInAlphabet(c);
      }
  
      @Override
      public S initialState() {
        return DeterministicFiniteAutomaton.this.initialState();
      }
  
      @Override
      public boolean isFinalState(S state) {
        return !DeterministicFiniteAutomaton.this.isFinalState(state);
      }
  
      @Override
      public S transit(S state, char c) {
        return DeterministicFiniteAutomaton.this.transit(state, c);
      }
    };
  }
  
  public static void main(DeterministicFiniteAutomaton<?> dfa, String[] args) {
    if (args.length == 0) {
      System.err.println("Usage: [-negate] <string> [strings...]");
      return;
    }
    
    int stringsIndex = 0;
    if (args[0].equals("-n") || args[0].equals("-negate") || args[0].equals("--negate")) {
      dfa = dfa.negate();
      stringsIndex = 1;
      if (args.length == 1) {
        System.err.println("Usage: [-negate] <string> [strings...]");
        return;
      }
    }
    for (int i = stringsIndex;i < args.length;i++) {
      try {
        System.out.println(args[i] + " -> " + dfa.test(args[i]));
      } catch (IllegalArgumentException e) {
        System.out.println(args[i] + " -> false (" + e.getMessage() + ")");
      }
    }
  }
}
