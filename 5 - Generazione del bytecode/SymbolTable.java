import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
  Map<String, Integer> offsetMap = new HashMap<>();
  
  public void insert(String name, int address) {
    if (!offsetMap.containsValue(address)) {
      offsetMap.put(name, address);
    } else {
      throw new IllegalArgumentException("Riferimento ad una locazione di memoria gia‘ occupata da un’altra variabile");
    }
  }
  
  public int lookupAddress(String s) {
    if (offsetMap.containsKey(s)) {
      return offsetMap.get(s);
    } else {
      return -1;
    }
  }
}
