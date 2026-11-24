import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

/**
 * Prices a small shopping cart and prints a receipt.
 *
 * This is the Phase 2 project: it uses variables, BigDecimal for money, if
 * statements, loops, methods, arrays and Strings together. The three arrays
 * are matched by position, which is fine for now. Phase 3 replaces them with
 * a CartItem class.
 */
public class CartCalculator {

    private static final BigDecimal GST_RATE = new BigDecimal("0.18");
    private static final BigDecimal COUPON_RATE = new BigDecimal("0.10");
    private static final String COUPON_CODE = "SAVE10";
    private static final int RECEIPT_WIDTH = 46;

    public static void main(String[] args) {
        String[] itemNames = {"USB-C Cable", "Wireless Mouse", "Notebook A5", "Desk Lamp"};
        int[] quantities = {2, 1, 5, 1};
        BigDecimal[] unitPrices = {
            new BigDecimal("349.00"),
            new BigDecimal("799.50"),
            new BigDecimal("85.00"),
            new BigDecimal("1249.00")
        };

        // Coupon codes come from people, so expect stray spaces and any capitalisation.
        String enteredCoupon = "  save10 ";

        System.out.print(buildReceipt(itemNames, quantities, unitPrices, enteredCoupon));
    }

    static String buildReceipt(String[] names, int[] quantities, BigDecimal[] prices, String coupon) {
        BigDecimal subtotal = subtotal(quantities, prices);
        BigDecimal discount = discountFor(coupon, subtotal);
        BigDecimal afterDiscount = subtotal.subtract(discount);
        BigDecimal gst = afterDiscount.multiply(GST_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = afterDiscount.add(gst);

        // A StringBuilder collects text without creating a new String for every
        // append, which is why it is used for building longer output.
        StringBuilder receipt = new StringBuilder();
        receipt.append("LAKEVIEW STATIONERS\n");
        receipt.append("=".repeat(RECEIPT_WIDTH)).append('\n');

        for (int i = 0; i < names.length; i++) {
            receipt.append(String.format(Locale.US, "%-16s %3d x %9s = %9s%n",
                    names[i], quantities[i], prices[i], lineTotal(quantities[i], prices[i])));
        }

        receipt.append("-".repeat(RECEIPT_WIDTH)).append('\n');
        receipt.append(moneyLine("Subtotal", subtotal));
        receipt.append(moneyLine("Discount (" + coupon.trim().toUpperCase() + ")", discount.negate()));
        receipt.append(moneyLine("GST 18%", gst));
        receipt.append("=".repeat(RECEIPT_WIDTH)).append('\n');
        receipt.append(moneyLine("TOTAL", total));
        return receipt.toString();
    }

    static BigDecimal lineTotal(int quantity, BigDecimal unitPrice) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    static BigDecimal subtotal(int[] quantities, BigDecimal[] prices) {
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < quantities.length; i++) {
            sum = sum.add(lineTotal(quantities[i], prices[i]));
        }
        return sum;
    }

    /**
     * Returns the coupon discount, or zero when the code is not recognised.
     * trim() removes stray spaces, toUpperCase() ignores capitalisation, and
     * equals() compares the text. Never use == to compare Strings.
     */
    static BigDecimal discountFor(String coupon, BigDecimal subtotal) {
        String cleaned = coupon.trim().toUpperCase();
        if (cleaned.equals(COUPON_CODE)) {
            return subtotal.multiply(COUPON_RATE).setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO.setScale(2);
    }

    private static String moneyLine(String label, BigDecimal amount) {
        return String.format(Locale.US, "%-30s %15s%n", label, amount);
    }
}
