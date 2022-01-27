/*
 * Linguaggio delle stringhe di 0 e 1 che non contengono 3 zeri consecutivi.
 */
public class DFA1 {
  public static boolean scan(String s) {
    int state = 0;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      if (c != '0' && c != '1') {
        throw new IllegalArgumentException("character '" + c + "' is not in the alphabet");
      }

      switch (state) {
        case 0:
          state = c == '0' ? 1 : 0;
          break;
        case 1:
          state = c == '0' ? 2 : 0;
          break;
        case 2:
          state = c == '0' ? 3 : 0;
          break;
        case 3:
          state = 3;
          break;
      }
    }
    return state != 3;
  }
  
  public static void main(String[] args) {
    if (args.length == 0) {
      System.err.println("Usage: <string> [strings...]");
      return;
    }

    for (int i = 0; i < args.length; i++) {
      try {
        System.out.println(args[i] + " -> " + scan(args[i]));
      } catch (IllegalArgumentException e) {
        System.out.println(args[i] + " -> false (" + e.getMessage() + ")");
      }
    }
  }
}
