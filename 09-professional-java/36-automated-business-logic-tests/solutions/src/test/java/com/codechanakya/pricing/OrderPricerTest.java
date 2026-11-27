package com.codechanakya.pricing;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("OrderPricer")
class OrderPricerTest {

    private OrderPricer pricer;

    @BeforeEach
    void setUp() {
        pricer = new OrderPricer(new CouponService(), new ShippingCalculator());
    }

    private static BigDecimal money(String text) {
        return new BigDecimal(text);
    }

    @Test
    @DisplayName("a plain order: no tier, no coupon")
    void plainOrder() {
        PriceBreakdown price = pricer.price(money("400"), CustomerTier.STANDARD, null, 1500, false);

        // assertAll runs every check and reports every failure, not just the first.
        assertAll("breakdown of a plain order",
                () -> assertEquals(money("400.00"), price.subtotal()),
                () -> assertEquals(money("0.00"), price.tierDiscount()),
                () -> assertEquals(money("0.00"), price.couponDiscount()),
                () -> assertEquals(money("72.00"), price.tax()),
                () -> assertEquals(money("80.00"), price.shipping()),
                () -> assertEquals(money("552.00"), price.total()));
    }

    @Test
    @DisplayName("gold tier, coupon and express shipping together")
    void everythingTogether() {
        PriceBreakdown price = pricer.price(money("1200"), CustomerTier.GOLD, "welcome10", 800, true);

        assertAll(
                () -> assertEquals(money("120.00"), price.tierDiscount()),
                () -> assertEquals(money("108.00"), price.couponDiscount()),   // 10% of 1080
                () -> assertEquals(money("174.96"), price.tax()),              // 18% of 972
                () -> assertEquals(money("160.00"), price.shipping()),
                () -> assertEquals(money("1306.96"), price.total()));
    }

    static Stream<Arguments> tiersAndTotals() {
        // tier, subtotal, expected tier discount
        return Stream.of(
                Arguments.of(CustomerTier.STANDARD, "1000", "0.00"),
                Arguments.of(CustomerTier.SILVER, "1000", "50.00"),
                Arguments.of(CustomerTier.GOLD, "1000", "100.00"),
                Arguments.of(CustomerTier.GOLD, "33.33", "3.33"));   // 3.333 rounds down
    }

    @ParameterizedTest(name = "{0} on {1} gets {2} off")
    @MethodSource("tiersAndTotals")
    @DisplayName("each tier gets its own discount")
    void tierDiscounts(CustomerTier tier, String subtotal, String expectedDiscount) {
        PriceBreakdown price = pricer.price(money(subtotal), tier, null, 500, false);
        assertEquals(money(expectedDiscount), price.tierDiscount());
    }

    @ParameterizedTest
    @EnumSource(CustomerTier.class)
    @DisplayName("a better tier never pays more")
    void higherTiersNeverPayMore(CustomerTier tier) {
        BigDecimal standard = pricer.price(money("800"), CustomerTier.STANDARD, null, 500, false).total();
        BigDecimal actual = pricer.price(money("800"), tier, null, 500, false).total();
        assertTrue(actual.compareTo(standard) <= 0, tier + " paid " + actual + " but standard pays " + standard);
    }

    @Test
    @DisplayName("a coupon minimum is checked after the tier discount")
    void couponMinimumUsesDiscountedAmount() {
        // FLAT50 needs 300. An order of 300 with a gold discount of 30 leaves 270, so no coupon applies.
        PriceBreakdown price = pricer.price(money("300"), CustomerTier.GOLD, "FLAT50", 500, false);
        assertEquals(money("0.00"), price.couponDiscount());
    }

    // A1
    @Test
    @DisplayName("FLAT50 works for a gold customer whose discounted amount is still 300 or more")
    void flatCouponAfterGoldDiscount() {
        PriceBreakdown price = pricer.price(money("400"), CustomerTier.GOLD, "FLAT50", 500, false);
        assertEquals(money("50.00"), price.couponDiscount());   // 400 - 40 = 360, which is above 300
    }

    // C2: a group of tests that share a setup of their own
    @Nested
    @DisplayName("with express delivery")
    class Express {

        private PriceBreakdown price;

        @BeforeEach
        void priceAnExpressOrder() {
            price = pricer.price(money("2000"), CustomerTier.STANDARD, null, 500, true);
        }

        @Test
        @DisplayName("is never free, even over the free-shipping line")
        void notFree() {
            assertEquals(money("160.00"), price.shipping());
        }

        @Test
        @DisplayName("is added to the total after tax")
        void addedAfterTax() {
            assertEquals(money("2520.00"), price.total());   // 2000 + 360 tax + 160 shipping
        }
    }

    @Test
    @DisplayName("a coupon that is not recognised stops the order")
    void badCouponStopsTheOrder() {
        assertThrows(IllegalArgumentException.class,
                () -> pricer.price(money("400"), CustomerTier.STANDARD, "NOPE", 500, false));
    }

    @Test
    @DisplayName("a subtotal of zero is rejected")
    void zeroSubtotal() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> pricer.price(money("0"), CustomerTier.STANDARD, null, 500, false));
        assertTrue(error.getMessage().startsWith("Subtotal must be positive"));
    }
}
