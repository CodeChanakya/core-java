import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * A short lab on why money must not be stored in double, and what to use
 * instead. Read the output top to bottom: each part answers a question the
 * part before it raised.
 */
public class MoneyLab {

    public static void main(String[] args) {
        // Part 1: the problem. double stores numbers in binary, and a value
        // like 0.1 has no exact binary form, just as 1/3 has no exact decimal form.
        double a = 0.1;
        double b = 0.2;
        double price = 1.10;
        System.out.println("PART 1: double");
        System.out.println("0.1 + 0.2        = " + (a + b));
        System.out.println("0.1 + 0.2 == 0.3 : " + (a + b == 0.3));
        System.out.println("1.10 x 3         = " + (price * 3));
        System.out.println();

        // Part 2: BigDecimal keeps the digits you give it. Always build one
        // from a String. Building it from a double copies the double's error.
        BigDecimal x = new BigDecimal("0.1");
        BigDecimal y = new BigDecimal("0.2");
        System.out.println("PART 2: BigDecimal");
        System.out.println("0.1 + 0.2        = " + x.add(y));
        System.out.println("From a String    : " + new BigDecimal("0.1"));
        System.out.println("From a double    : " + new BigDecimal(0.1));
        System.out.println();

        // Part 3: an invoice. Every amount is rounded to 2 places exactly once,
        // at the moment it becomes a real charge.
        BigDecimal unitPrice = new BigDecimal("499.99");
        int quantity = 3;
        BigDecimal gstRate = new BigDecimal("0.18");

        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal gstExact = subtotal.multiply(gstRate);
        BigDecimal gst = gstExact.setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(gst);

        System.out.println("PART 3: invoice");
        System.out.println("Subtotal         : " + subtotal);
        System.out.println("GST before round : " + gstExact);
        System.out.println("GST (18%)        : " + gst);
        System.out.println("Total            : " + total);
        System.out.println();

        // Part 4: comparing. equals() also compares the scale (the number of
        // decimal places), so 2.0 and 2.00 are not "equal". compareTo() only
        // compares the value.
        BigDecimal twoPointZero = new BigDecimal("2.0");
        BigDecimal twoPointZeroZero = new BigDecimal("2.00");
        System.out.println("PART 4: comparing");
        System.out.println("2.0 equals 2.00    : " + twoPointZero.equals(twoPointZeroZero));
        System.out.println("2.0 compareTo 2.00 : " + twoPointZero.compareTo(twoPointZeroZero));
        System.out.println();

        // Part 5: dividing. 1000 / 3 never ends, so you must say how many
        // places to keep and how to round.
        BigDecimal share = new BigDecimal("1000").divide(new BigDecimal("3"), 2, RoundingMode.HALF_UP);
        System.out.println("PART 5: dividing");
        System.out.println("1000 / 3         = " + share);
        System.out.println("Three shares     = " + share.multiply(BigDecimal.valueOf(3)));
    }
}
