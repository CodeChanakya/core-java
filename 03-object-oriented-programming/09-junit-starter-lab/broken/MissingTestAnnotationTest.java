package com.codechanakya.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

/** One test has no @Test, so JUnit never runs it. The suite stays green and checks less than you think. */
class MissingTestAnnotationTest {

    @Test
    void depositWorks() {
        Account account = new Account("ACC-1", "Asha Menon", new BigDecimal("100"));
        account.deposit(new BigDecimal("50"));
        assertEquals(new BigDecimal("150.00"), account.getBalance());
    }

    // @Test was forgotten here. This test is wrong on purpose (it expects 999.00),
    // yet nothing fails, because nothing runs it.
    void withdrawWorks() {
        Account account = new Account("ACC-2", "Rahul Verma", new BigDecimal("100"));
        account.withdraw(new BigDecimal("30"));
        assertEquals(new BigDecimal("999.00"), account.getBalance());
    }
}
