import java.util.*;
 public class card {
    private int value;
    private char kind;

    public card(int v, char k) {
        this.value = v;
        this.kind = k;
    }
    public int getCardVal() {

        return(this.value);
    }
    public char getCardKind() {
        return(this.kind);
    }
}

