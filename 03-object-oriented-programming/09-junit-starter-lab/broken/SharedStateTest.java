package com.codechanakya.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/**
 * Both tests share one static account and never reset it. Whether they pass
 * depends on which runs first, and JUnit does not promise an order. A test
 * that passes alone but fails in the suite almost always has a problem like this.
 */
class SharedStateTest {

    private static final Account SHARED = new Account("ACC-1", "Asha Menon", new BigDecimal("100"));

    @Test
    void depositIncreasesBalance() {
        SHARED.deposit(new BigDecimal("50"));
        assertEquals(new BigDecimal("150.00"), SHARED.getBalance());
    }

    @Test
    void withdrawDecreasesBalance() {
        SHARED.withdraw(new BigDecimal("30"));
        assertEquals(new BigDecimal("70.00"), SHARED.getBalance());
    }
}
