import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * One possible answer to the exercises in ../exercises/README.md.
 * Try them yourself first. Your version does not need to look like this one.
 */
public class MyBill {

    // I2: slab limits have names, so no number appears twice.
    private static final int SLAB_1_LIMIT = 100;
    private static final int SLAB_2_LIMIT = 200;
    private static final int SLAB_3_LIMIT = 300;   // B1: a fourth slab above 300

    private static final BigDecimal FIXED_CHARGE = new BigDecimal("50.00");   // I1
    private static final BigDecimal TAX_RATE = new BigDecimal("0.05");        // I1

    /**
     * C1: the rates are parameters, so household and commercial customers share
     * one method instead of two copies of it.
     */
    static BigDecimal energyCharge(int units, BigDecimal rate1, BigDecimal rate2,
                                   BigDecimal rate3, BigDecimal rate4) {
        BigDecimal total = BigDecimal.ZERO;
        int remaining = Math.max(units, 0);

        if (remaining > SLAB_3_LIMIT) {
            total = total.add(rate4.multiply(BigDecimal.valueOf(remaining - SLAB_3_LIMIT)));
            remaining = SLAB_3_LIMIT;
        }
        if (remaining > SLAB_2_LIMIT) {
            total = total.add(rate3.multiply(BigDecimal.valueOf(remaining - SLAB_2_LIMIT)));
            remaining = SLAB_2_LIMIT;
        }
        if (remaining > SLAB_1_LIMIT) {
            total = total.add(rate2.multiply(BigDecimal.valueOf(remaining - SLAB_1_LIMIT)));
            remaining = SLAB_1_LIMIT;
        }
        return total.add(rate1.multiply(BigDecimal.valueOf(remaining)));
    }

    static void printBill(String customerType, int units, BigDecimal... rates) {
        BigDecimal energy = energyCharge(units, rates[0], rates[1], rates[2], rates[3]);
        BigDecimal subtotal = energy.add(FIXED_CHARGE);
        BigDecimal tax = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        System.out.println(customerType + ", " + units + " units");
        System.out.println("  Energy charge : " + energy);
        System.out.println("  Fixed charge  : " + FIXED_CHARGE);
        System.out.println("  Tax (5%)      : " + tax);
        System.out.println("  Total         : " + subtotal.add(tax));
    }

    public static void main(String[] args) {
        BigDecimal[] household = {new BigDecimal("3.50"), new BigDecimal("5.25"),
                                  new BigDecimal("7.80"), new BigDecimal("9.10")};
        BigDecimal[] commercial = {new BigDecimal("6.00"), new BigDecimal("8.00"),
                                   new BigDecimal("10.50"), new BigDecimal("12.00")};

        printBill("Household", 250, household);
        printBill("Household", 350, household);
        printBill("Commercial", 250, commercial);
    }
}
