import java.math.BigDecimal;

/** Why assertEquals on two BigDecimal values can fail even when both are "ninety". */
public class BigDecimalScale {

    public static void main(String[] args) {
        BigDecimal expected = new BigDecimal("90.0");
        BigDecimal actual = new BigDecimal("90.00");

        System.out.println("expected = " + expected + ", actual = " + actual);
        System.out.println("equals()    : " + expected.equals(actual) + "   <- what assertEquals uses");
        System.out.println("compareTo() : " + expected.compareTo(actual) + "       <- 0 means same value");
    }
}
