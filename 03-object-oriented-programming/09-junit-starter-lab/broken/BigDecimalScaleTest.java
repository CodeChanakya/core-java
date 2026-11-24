package com.codechanakya.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/** Looks right, fails anyway: 1000 and 1000.00 are different BigDecimal values to equals. */
class BigDecimalScaleTest {

    @Test
    void openingBalanceIsKept() {
        Account account = new Account("ACC-1", "Asha Menon", new BigDecimal("1000"));
        assertEquals(new BigDecimal("1000"), account.getBalance());
    }
}
