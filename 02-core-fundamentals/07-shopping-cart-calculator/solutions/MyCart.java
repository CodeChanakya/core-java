import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/**
 * One possible answer to the exercises in ../exercises/README.md.
 * Try them yourself first. Your version does not need to look like this one.
 */
public class MyCart {

    private static final BigDecimal FREE_SHIPPING_FROM = new BigDecimal("1500.00");   // I1
    private static final BigDecimal SHIPPING_FEE = new BigDecimal("50.00");           // I1
    private static final int NAME_WIDTH = 16;

    public static void main(String[] args) {
        String[] names = {"USB-C Cable", "Wireless Mouse", "Notebook A5", "Ergonomic Laptop Stand"};   // B1, C1
        int[] quantities = {2, 1, 5, 1};
        BigDecimal[] prices = {
            new BigDecimal("349.00"), new BigDecimal("799.50"),
            new BigDecimal("85.00"), new BigDecimal("1899.00")
        };

        BigDecimal subtotal = BigDecimal.ZERO;
        int itemCount = 0;                                                            // B3
        for (int i = 0; i < names.length; i++) {
            BigDecimal line = prices[i].multiply(BigDecimal.valueOf(quantities[i]));
            subtotal = subtotal.add(line);
            itemCount += quantities[i];
            System.out.println(String.format(Locale.US, "%-16s %3d x %9s = %9s",
                    shorten(names[i]), quantities[i], prices[i], line));
        }

        BigDecimal discount = flat200Coupon("flat200 ", subtotal);                    // I2
        BigDecimal shipping = subtotal.compareTo(FREE_SHIPPING_FROM) >= 0
                ? BigDecimal.ZERO.setScale(2) : SHIPPING_FEE;
        System.out.println("Items in cart : " + itemCount);
        System.out.println("Subtotal      : " + subtotal);
        System.out.println("Coupon FLAT200: -" + discount);
        System.out.println("Shipping      : " + shipping);
        System.out.println("Pay           : " + subtotal.subtract(discount).add(shipping));
    }

    /** C1: names longer than the column are cut and end in "...". */
    static String shorten(String name) {
        if (name.length() <= NAME_WIDTH) {
            return name;
        }
        return name.substring(0, NAME_WIDTH - 3) + "...";
    }

    /** I2: 200 off, but only when the subtotal is at least 2000. */
    static BigDecimal flat200Coupon(String code, BigDecimal subtotal) {
        boolean valid = code.trim().equalsIgnoreCase("FLAT200");
        boolean bigEnough = subtotal.compareTo(new BigDecimal("2000.00")) >= 0;
        if (valid && bigEnough) {
            return new BigDecimal("200.00");
        }
        return BigDecimal.ZERO.setScale(2);
    }
}
