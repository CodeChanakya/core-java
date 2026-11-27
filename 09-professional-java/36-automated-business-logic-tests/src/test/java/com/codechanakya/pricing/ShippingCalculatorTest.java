package com.codechanakya.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ShippingCalculator")
class ShippingCalculatorTest {

    private ShippingCalculator calculator;

    @BeforeEach
    void setUp() {
        // JUnit creates a new test object for every test, so each test starts clean.
        calculator = new ShippingCalculator();
    }

    @ParameterizedTest(name = "standard, {1} g, subtotal {0} costs {2}")
    @CsvSource({
            // subtotal, grams, expected cost
            "100.00,   1,    60.00",   // 1 g still counts as a started kilogram
            "100.00,   1000, 60.00",   // exactly 1 kg is still one kilogram
            "100.00,   1001, 80.00",   // one gram over starts the second kilogram
            "100.00,   2500, 100.00",
            "998.99,   500,  60.00",   // just under the free-shipping line
            "999.00,   500,  0.00",    // exactly on the line is free
            "5000.00,  9000, 0.00"
    })
    @DisplayName("standard delivery")
    void standardDelivery(String subtotal, int grams, String expected) {
        assertEquals(new BigDecimal(expected), calculator.cost(new BigDecimal(subtotal), grams, false));
    }

    @ParameterizedTest
    @CsvSource({
            "100.00, 500,  160.00",
            "5000.00, 500, 160.00",    // express is never free
            "100.00, 2500, 200.00"
    })
    @DisplayName("express delivery adds a flat 100 and is never free")
    void expressDelivery(String subtotal, int grams, String expected) {
        assertEquals(new BigDecimal(expected), calculator.cost(new BigDecimal(subtotal), grams, true));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -500})
    @DisplayName("a weight that is not positive is rejected")
    void badWeights(int grams) {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> calculator.cost(new BigDecimal("100"), grams, false));
        assertEquals("Weight must be positive, but was " + grams, error.getMessage());
    }

    @Test
    @DisplayName("the answer always has two decimals")
    void twoDecimals() {
        assertEquals(2, calculator.cost(new BigDecimal("100"), 500, false).scale());
    }
}
