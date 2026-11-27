package com.codechanakya.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("CouponService")
class CouponServiceTest {

    private final CouponService service = new CouponService();

    private BigDecimal discount(String code, String subtotal) {
        return service.discountFor(code, new BigDecimal(subtotal));
    }

    @Nested
    @DisplayName("WELCOME10")
    class Welcome {

        @ParameterizedTest
        @CsvSource({
                "100.00,  10.00",
                "1999.99, 200.00",   // 10 percent would be 199.999, which rounds to 200.00
                "2000.00, 200.00",
                "9999.00, 200.00"    // capped at 200
        })
        @DisplayName("is ten percent, capped at 200")
        void tenPercentWithCap(String subtotal, String expected) {
            assertEquals(new BigDecimal(expected), discount("WELCOME10", subtotal));
        }

        @Test
        @DisplayName("ignores upper and lower case and spaces")
        void caseInsensitive() {
            assertEquals(new BigDecimal("10.00"), discount("  welcome10 ", "100"));
        }
    }

    @Nested
    @DisplayName("FLAT50")
    class Flat {

        @ParameterizedTest
        @CsvSource({
                "299.99, 0.00",     // just under the minimum
                "300.00, 50.00",    // exactly on the minimum
                "1000.00, 50.00"
        })
        @DisplayName("gives 50 off only from a subtotal of 300")
        void minimumSpend(String subtotal, String expected) {
            assertEquals(new BigDecimal(expected), discount("FLAT50", subtotal));
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "   "})
    @DisplayName("a blank code means no coupon")
    void blankCode(String code) {
        assertEquals(new BigDecimal("0.00"), discount(code, "500"));
    }

    @Test
    @DisplayName("a missing code means no coupon")
    void nullCode() {
        assertEquals(new BigDecimal("0.00"), discount(null, "500"));
    }

    @Test
    @DisplayName("an unknown code is an error that names the code")
    void unknownCode() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class, () -> discount("FREEBIE", "500"));
        assertEquals("Unknown coupon: FREEBIE", error.getMessage());
    }
}
