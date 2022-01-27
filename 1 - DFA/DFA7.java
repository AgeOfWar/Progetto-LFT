/*
 * Progettare e implementare un DFA che riconosca il linguaggio di stringhe che
 * contengono il tuo nome e tutte le stringhe ottenute dopo la sostituzione di un carattere del nome
 * con un altro qualsiasi. Ad esempio, nel caso di uno studente che si chiama Paolo, il DFA deve
 * accettare la stringa "Paolo" (cioe il nome scritto correttamente), ma anche le stringhe "Pjolo",
 * "caolo", "Pa%lo", "Paola" e "Parlo" (il nome dopo la sostituzione di un carattere), ma non
 * "Eva", "Perro", "Pietro" oppure "P*o*o".
 */
public class DFA7 {
  public static boolean scan(String s) {
    int state = 0;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      switch (state) {
        case 0:
          state = c == 'M' ? 1 : 5;
          break;
        case 1:
          state = c == 'i' ? 2 : 6;
          break;
        case 2:
          state = c == 'c' ? 3 : 7;
          break;
        case 3:
          state = c == 'h' ? 4 : 8;
          break;
        case 4:
          state = 9;
          break;
        case 5:
          state = c == 'i' ? 6 : -1;
          break;
        case 6:
          state = c == 'c' ? 7 : -1;
          break;
        case 7:
          state = c == 'h' ? 8 : -1;
          break;
        case 8:
          state = c == 'i' ? 9 : -1;
          break;
        case 9:
        case -1:
          state = -1;
          break;
      }
    }
    return state == 9;
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
