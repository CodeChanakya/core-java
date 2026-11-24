// This file compiles but crashes when run, on purpose. See README.md in this folder.
import java.math.BigDecimal;

public class DivideWithoutScale {

    public static void main(String[] args) {
        BigDecimal amount = new BigDecimal("1000");
        BigDecimal people = new BigDecimal("3");
        System.out.println(amount.divide(people));
    }
}
