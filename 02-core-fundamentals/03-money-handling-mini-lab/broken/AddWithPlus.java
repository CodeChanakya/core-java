// This file does not compile, on purpose. See README.md in this folder.
import java.math.BigDecimal;

public class AddWithPlus {

    public static void main(String[] args) {
        BigDecimal first = new BigDecimal("1.50");
        BigDecimal second = new BigDecimal("2.25");
        BigDecimal total = first + second;
        System.out.println(total);
    }
}
