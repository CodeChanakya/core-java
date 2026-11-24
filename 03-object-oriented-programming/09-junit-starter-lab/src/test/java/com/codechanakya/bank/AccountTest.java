package com.codechanakya.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for Account.
 *
 * Each test follows the same three beats: arrange (set things up), act (do the
 * one thing being tested), assert (check the result). Keep to one idea per
 * test, so that when a test fails its name tells you what broke.
 */
class AccountTest {

    private Account account;

    /** Runs before every test, so each test starts with a fresh account holding 1000.00. */
    @BeforeEach
    void createAccount() {
        account = new Account("ACC-1", "Asha Menon", new BigDecimal("1000.00"));
    }

    @Test
    @DisplayName("a new account keeps its holder, number and opening balance")
    void newAccountKeepsItsDetails() {
        assertEquals("ACC-1", account.getAccountNumber());
        assertEquals("Asha Menon", account.getHolderName());
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

    @Test
    @DisplayName("deposit adds to the balance")
    void depositAddsToBalance() {
        boolean accepted = account.deposit(new BigDecimal("250.50"));

        assertTrue(accepted);
        assertEquals(new BigDecimal("1250.50"), account.getBalance());
    }

    @Test
    @DisplayName("deposit of zero or less is refused and changes nothing")
    void depositOfZeroOrLessIsRefused() {
        assertFalse(account.deposit(BigDecimal.ZERO));
        assertFalse(account.deposit(new BigDecimal("-5")));
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

    @Test
    @DisplayName("withdraw takes money out")
    void withdrawReducesBalance() {
        assertTrue(account.withdraw(new BigDecimal("400")));
        assertEquals(new BigDecimal("600.00"), account.getBalance());
    }

    @Test
    @DisplayName("withdrawing exactly the balance is allowed")
    void withdrawEntireBalance() {
        assertTrue(account.withdraw(new BigDecimal("1000.00")));
        assertEquals(new BigDecimal("0.00"), account.getBalance());
    }

    @Test
    @DisplayName("withdrawing more than the balance is refused")
    void overdrawIsRefused() {
        assertFalse(account.withdraw(new BigDecimal("1000.01")));
        assertEquals(new BigDecimal("1000.00"), account.getBalance());
    }

    @Test
    @DisplayName("amounts are rounded to two decimal places")
    void amountsAreRoundedToPaise() {
        account.deposit(new BigDecimal("10.005"));

        // BigDecimal equality compares scale too, so 1010.01 and 1010.010 differ.
        // Compare with compareTo when only the numeric value matters.
        assertEquals(0, new BigDecimal("1010.01").compareTo(account.getBalance()));
    }

    @Test
    @DisplayName("a blank holder name is rejected")
    void blankHolderNameIsRejected() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new Account("ACC-2", "   ", new BigDecimal("10")));

        assertEquals("Account holder name is required", error.getMessage());
    }

    @Test
    @DisplayName("a negative opening balance is rejected")
    void negativeOpeningBalanceIsRejected() {
        assertThrows(IllegalArgumentException.class,
                () -> new Account("ACC-3", "Rahul Verma", new BigDecimal("-1")));
    }
}
