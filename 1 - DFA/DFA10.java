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
public class DFA10 {
  public static boolean scan(String s) {
    int state = 0;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      if (c != '/' && c != '*' && c != 'a') {
        throw new IllegalArgumentException("character '" + c + "' is not in the alphabet");
      }

      switch (state) {
        case 0:
          state = c == '/' ? 1 : 0;
          break;
        case 1:
          state = c == '*' ? 2 : c == '/' ? 1 : 0;
          break;
        case 2:
          state = c == '*' ? 3 : 2;
          break;
        case 3:
          switch (c) {
            case '/':
              state = 0;
              break;
            case '*':
              state = 3;
              break;
            case 'a':
              state = 2;
              break;
          }
          break;
      }
    }
    return state <= 1;
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
