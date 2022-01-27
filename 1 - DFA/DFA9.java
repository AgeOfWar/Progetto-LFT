/**
 * Progettare e implementare un DFA con alfabeto {/, *, a} che riconosca il linguaggio di "commenti" delimitati da
 * /* (all’inizio) e *&#47; (alla fine):cioe l’automa deve accettare le
 * stringhe che contengono almeno 4 caratteri che iniziano con /*, che finiscono con *&#47;,
 * e che contengono una sola occorrenza della sequenza*&#47;,quella finale(dove l’asterisco della sequenza*&#47;
 * non deve essere in comune con quello della sequenza /* all’inizio).
 * Esempi di stringhe accettate: "/****&#47;", "/*a*a*&#47;", "/*a/**&#47;", "/**a///a/a**&#47;", "/**&#47;", "/*&#47;*&#47;"
 * Esempi di stringhe non accettate: "/*&#47;", "/**&#47;***&#47;"
 */
public class DFA9 {
  public static boolean scan(String s) {
    int state = 0;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      if (c != '/' && c != '*' && c != 'a') {
        throw new IllegalArgumentException("character '" + c + "' is not in the alphabet");
      }

      switch (state) {
        case 0:
          state = c == '/' ? 1 : -1;
          break;
        case 1:
          state = c == '*' ? 2 : -1;
          break;
        case 2:
          state = c == '*' ? 3 : 2;
          break;
        case 3:
          switch (c) {
            case '/':
              state = 4;
              break;
            case '*':
              state = 3;
              break;
            case 'a':
              state = 2;
              break;
          }
          break;
        case -1:
        case 4:
          state = -1;
          break;
      }
    }
    return state == 4;
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
