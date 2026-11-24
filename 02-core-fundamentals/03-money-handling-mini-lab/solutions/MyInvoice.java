import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * One possible answer to the exercises in ../exercises/README.md.
 * Try them yourself first. Your version does not need to look like this one.
 */
public class MyInvoice {

    public static void main(String[] args) {
        BigDecimal unitPrice = new BigDecimal("1299.50");          // B1
        int quantity = 2;                                          // B1
        BigDecimal discountRate = new BigDecimal("0.10");          // I1
        BigDecimal gstRate = new BigDecimal("0.05");               // B3

        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal discount = subtotal.multiply(discountRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal discounted = subtotal.subtract(discount);
        BigDecimal gst = discounted.multiply(gstRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = discounted.add(gst);

        System.out.println("Subtotal        : " + subtotal);
        System.out.println("Discount (10%)  : " + discount);
        System.out.println("After discount  : " + discounted);
        System.out.println("GST (" + gstRate.multiply(BigDecimal.valueOf(100)).intValue() + "%)        : " + gst);   // B2
        System.out.println("Total           : " + total);
        System.out.println();

        // I2 and C1: split 1000.00 three ways without losing a paisa.
        BigDecimal amount = new BigDecimal("1000.00");
        BigDecimal share = amount.divide(BigDecimal.valueOf(3), 2, RoundingMode.HALF_UP);
        BigDecimal threeShares = share.multiply(BigDecimal.valueOf(3));
        System.out.println("Each share      : " + share);
        System.out.println("Shares add up to: " + threeShares + " (missing " + amount.subtract(threeShares) + ")");

        BigDecimal firstShare = amount.subtract(share.multiply(BigDecimal.valueOf(2)));
        System.out.println("Fixed split     : " + firstShare + " + " + share + " + " + share
                + " = " + firstShare.add(share).add(share));
    }
}
