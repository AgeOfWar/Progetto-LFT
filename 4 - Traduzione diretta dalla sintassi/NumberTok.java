public class NumberTok extends Token {
  public int value;
  
  public NumberTok(int value) {
    super(Tag.NUM);
    this.value = value;
  }
  
  public String toString() { return "<" + tag + ", " + value + ">"; }
}