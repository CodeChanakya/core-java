import java.math.BigDecimal;

/** Why prices in the quote calculator are BigDecimal. */
public class MoneyAsDouble {

    public static void main(String[] args) {
        double unitPrice = 0.10;
        System.out.println("double : 3 x 0.10 = " + (unitPrice * 3));
        System.out.println("double : equals 0.30? " + (unitPrice * 3 == 0.30));

        BigDecimal exact = new BigDecimal("0.10").multiply(BigDecimal.valueOf(3));
        System.out.println("decimal: 3 x 0.10 = " + exact);
        System.out.println("decimal: equals 0.30? " + exact.equals(new BigDecimal("0.30")));
    }
}
