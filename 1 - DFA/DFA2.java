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
public class DFA2 {
  public static boolean scan(String s) {
    int state = 0;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      if (!isJavaIdentifierPart(c)) {
        throw new IllegalArgumentException("character '" + c + "' is not in the alphabet");
      }

      switch (state) {
        case 0:
          state = c == '_' ? 1 : isLetter(c) ? 2 : -1;
          break;
        case 1:
          state = c == '_' ? 1 : 2;
          break;
        case 2:
          state = 2;
          break;
        case -1:
          state = -1;
          break;
      }
    }
    return state == 2;
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
