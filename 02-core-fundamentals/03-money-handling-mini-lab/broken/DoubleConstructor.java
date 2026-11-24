// This file compiles and runs, but the result is surprising. See README.md in this folder.
import java.math.BigDecimal;

public class DoubleConstructor {

    public static void main(String[] args) {
        BigDecimal price = new BigDecimal(19.99);
        System.out.println("Price: " + price);
    }
}
