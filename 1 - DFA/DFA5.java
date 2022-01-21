import static java.lang.Character.*;

public class DFA5 extends DeterministicFiniteAutomaton<Integer> {
  @Override
  public boolean isInAlphabet(char c) {
    return isLetter(c) || isDigit(c);
  }
  
  @Override
  public Integer initialState() {
    return 0;
  }
  
  @Override
  public boolean isFinalState(Integer state) {
    return state >= 5;
  }
  
  @Override
  public Integer transit(Integer state, char c) {
    switch (state) {
      case 0:
        return c >= 'A' && c <= 'K' ? 1 : c >= 'L' && c <= 'Z' ? 2 : -1;
      case 1:
        if (isLowerCase(c)) return 1;
        switch (c) {
          case '0':
          case '2':
          case '4':
          case '6':
          case '8':
            return 5;
          case '1':
          case '3':
          case '5':
          case '7':
          case '9':
            return 3;
          default:
            return -1;
        }
      case 2:
        if (isLowerCase(c)) return 2;
        switch (c) {
          case '0':
          case '2':
          case '4':
          case '6':
          case '8':
            return 6;
          case '1':
          case '3':
          case '5':
          case '7':
          case '9':
            return 4;
          default:
            return -1;
        }
      case 3:
      case 5:
        switch (c) {
          case '0':
          case '2':
          case '4':
          case '6':
          case '8':
            return 5;
          case '1':
          case '3':
          case '5':
          case '7':
          case '9':
            return 3;
          default:
            return -1;
        }
      case 4:
      case 6:
        switch (c) {
          case '0':
          case '2':
          case '4':
          case '6':
          case '8':
            return 6;
          case '1':
          case '3':
          case '5':
          case '7':
          case '9':
            return 4;
          default:
            return -1;
        }
      case -1:
        return -1;
      default:
        throw new IllegalStateException("Illegal state '" + state + "'");
    }
  }
  
  public static void main(String[] args) {
    main(new DFA5(), args);
  }
}

