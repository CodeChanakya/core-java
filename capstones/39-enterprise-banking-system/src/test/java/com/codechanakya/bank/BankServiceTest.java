package com.codechanakya.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/** The business rules, tested on the fast in-memory store and a fixed clock. */
class BankServiceTest {

    private BankService bank;

    private static BigDecimal d(String text) {
        return new BigDecimal(text);
    }

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2025-06-30T00:00:00Z"), ZoneOffset.UTC);
        bank = new BankService(new InMemoryBankStore(), clock);
    }

    @Test
    @DisplayName("deposit and withdraw change the balance")
    void depositAndWithdraw() {
        Account a = bank.open("A", AccountType.CURRENT, d("100"));
        bank.deposit(a.id(), d("50.25"));
        bank.withdraw(a.id(), d("20"));
        assertEquals(d("130.25"), bank.account(a.id()).balance());
    }

    @ParameterizedTest(name = "amount {0} is refused")
    @ValueSource(strings = {"0", "-1", "-0.01"})
    void nonPositiveAmounts(String amount) {
        Account a = bank.open("A", AccountType.CURRENT, d("100"));
        assertThrows(BankException.class, () -> bank.deposit(a.id(), d(amount)));
        assertThrows(BankException.class, () -> bank.withdraw(a.id(), d(amount)));
        assertThrows(BankException.class, () -> bank.transfer(a.id(), a.id() + 1, d(amount)));
    }

    @Test
    @DisplayName("the daily withdrawal limit counts earlier withdrawals the same day")
    void dailyLimit() {
        Account a = bank.open("A", AccountType.CURRENT, d("90000"));
        bank.withdraw(a.id(), d("30000"));
        bank.withdraw(a.id(), d("20000"));
        BankException e = assertThrows(BankException.class, () -> bank.withdraw(a.id(), d("0.01")));
        assertEquals("Daily withdrawal limit of 50000.00 exceeded (already withdrawn 50000.00)", e.getMessage());
    }

    @Test
    @DisplayName("a transfer moves money from one account to the other and keeps the total the same")
    void transferKeepsTotal() {
        Account a = bank.open("A", AccountType.CURRENT, d("100"));
        Account b = bank.open("B", AccountType.CURRENT, d("100"));
        bank.transfer(a.id(), b.id(), d("40"));
        assertEquals(d("60.00"), bank.account(a.id()).balance());
        assertEquals(d("140.00"), bank.account(b.id()).balance());
        assertEquals(d("200.00"), bank.totalDeposits());
    }

    @Test
    @DisplayName("a transfer to the same account is refused")
    void transferToSelf() {
        Account a = bank.open("A", AccountType.CURRENT, d("100"));
        assertThrows(BankException.class, () -> bank.transfer(a.id(), a.id(), d("1")));
    }

    @Test
    @DisplayName("opening needs a name and the type's minimum")
    void openingRules() {
        assertThrows(BankException.class, () -> bank.open("  ", AccountType.CURRENT, d("1")));
        assertThrows(BankException.class, () -> bank.open("A", AccountType.SAVINGS, d("499.99")));
        bank.open("A", AccountType.SAVINGS, d("500"));
    }

    @Test
    @DisplayName("monthly interest is 4% a year divided by 12, rounded to the cent, savings only")
    void interest() {
        Account savings = bank.open("S", AccountType.SAVINGS, d("12000"));
        Account current = bank.open("C", AccountType.CURRENT, d("12000"));
        assertEquals(1, bank.payMonthlyInterest());
        assertEquals(d("12040.00"), bank.account(savings.id()).balance());
        assertEquals(d("12000.00"), bank.account(current.id()).balance());
    }

    @Test
    @DisplayName("interest rounds half up")
    void interestRounding() {
        Account savings = bank.open("S", AccountType.SAVINGS, d("502.50"));
        bank.payMonthlyInterest();
        // 502.50 * 0.04 / 12 = 1.675, which rounds half up to 1.68
        assertEquals(d("504.18"), bank.account(savings.id()).balance());
    }

    @Test
    @DisplayName("an unknown account is reported with its id")
    void unknownAccount() {
        AccountNotFoundException e = assertThrows(AccountNotFoundException.class, () -> bank.account(5));
        assertEquals("No account with id 5", e.getMessage());
    }
}
