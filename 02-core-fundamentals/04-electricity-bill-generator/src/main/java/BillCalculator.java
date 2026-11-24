import java.math.BigDecimal;

/**
 * Works out an electricity bill from the units consumed in a month.
 *
 * The tariff is split into slabs. Each rate applies only to the units that
 * fall inside its slab, not to the whole consumption.
 */
public final class BillCalculator {

    private static final BigDecimal RATE_UP_TO_100 = new BigDecimal("3.50");
    private static final BigDecimal RATE_101_TO_200 = new BigDecimal("5.25");
    private static final BigDecimal RATE_ABOVE_200 = new BigDecimal("7.80");

    /**
     * Returns the bill for the given units. Negative units make no sense, so
     * they are treated as zero. Phase 4 shows how to reject them properly.
     */
    public static BigDecimal billFor(int units) {
        BigDecimal total = BigDecimal.ZERO;
        int remaining = Math.max(units, 0);

        // Work from the top slab down: bill what is above 200, then cut the
        // remaining units back to 200 and repeat for the next slab.
        if (remaining > 200) {
            total = total.add(RATE_ABOVE_200.multiply(BigDecimal.valueOf(remaining - 200)));
            remaining = 200;
        }
        if (remaining > 100) {
            total = total.add(RATE_101_TO_200.multiply(BigDecimal.valueOf(remaining - 100)));
            remaining = 100;
        }
        total = total.add(RATE_UP_TO_100.multiply(BigDecimal.valueOf(remaining)));
        return total;
    }

    /** Names the highest slab the given units reach. */
    public static String slabFor(int units) {
        if (units <= 100) {
            return "slab 1";
        } else if (units <= 200) {
            return "slab 2";
        } else {
            return "slab 3";
        }
    }
}
