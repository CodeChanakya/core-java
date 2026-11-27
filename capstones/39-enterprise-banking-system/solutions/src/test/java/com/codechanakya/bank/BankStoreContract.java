package com.codechanakya.bank;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The rules every BankStore must obey. The in-memory and the JDBC store each run this same class,
 * so the two can never quietly disagree.
 */
abstract class BankStoreContract {

    static final LocalDate DAY = LocalDate.of(2025, 6, 30);

    abstract BankStore store();

    static BigDecimal money(String text) {
        return new BigDecimal(text).setScale(2);
    }

    @Test
    @DisplayName("opening an account records the opening balance in the history")
    void openRecordsHistory() {
        Account a = store().open("Asha", AccountType.SAVINGS, money("1000"), DAY);
        List<Transaction> history = store().history(a.id());
        assertAll(
                () -> assertEquals(1, history.size()),
                () -> assertEquals(TransactionKind.OPENING, history.get(0).kind()),
                () -> assertEquals(money("1000"), history.get(0).balanceAfter()));
    }

    @Test
    @DisplayName("ids are handed out in order and findAll is ordered by id")
    void idsAreOrdered() {
        Account a = store().open("A", AccountType.CURRENT, money("10"), DAY);
        Account b = store().open("B", AccountType.CURRENT, money("20"), DAY);
        assertEquals(List.of(a.id(), b.id()), store().findAll().stream().map(Account::id).toList());
        assertTrue(b.id() > a.id());
    }

    @Test
    @DisplayName("find returns empty for an unknown id")
    void findUnknown() {
        assertTrue(store().find(999).isEmpty());
    }

    @Test
    @DisplayName("a savings account cannot be opened below its floor")
    void openBelowFloor() {
        assertThrows(InsufficientFundsException.class,
                () -> store().open("A", AccountType.SAVINGS, money("100"), DAY));
    }

    @Test
    @DisplayName("post changes the balance and adds a history line with the new balance")
    void postChangesBalance() {
        Account a = store().open("A", AccountType.CURRENT, money("100"), DAY);
        store().post(DAY, List.of(new Entry(a.id(), TransactionKind.DEPOSIT, money("50"), "Deposit")));
        assertEquals(money("150"), store().find(a.id()).orElseThrow().balance());
        assertEquals(money("150"), store().history(a.id()).get(1).balanceAfter());
    }

    @Test
    @DisplayName("a current account may go into overdraft, but not past its limit")
    void overdraft() {
        Account a = store().open("A", AccountType.CURRENT, money("100"), DAY);
        store().post(DAY, List.of(new Entry(a.id(), TransactionKind.WITHDRAWAL, money("-10100"), "x")));
        assertEquals(money("-10000"), store().find(a.id()).orElseThrow().balance());
        assertThrows(InsufficientFundsException.class,
                () -> store().post(DAY, List.of(new Entry(a.id(), TransactionKind.WITHDRAWAL, money("-0.01"), "x"))));
    }

    @Test
    @DisplayName("a savings account cannot go below its minimum balance")
    void savingsFloor() {
        Account a = store().open("A", AccountType.SAVINGS, money("600"), DAY);
        assertThrows(InsufficientFundsException.class,
                () -> store().post(DAY, List.of(new Entry(a.id(), TransactionKind.WITHDRAWAL, money("-100.01"), "x"))));
        assertEquals(money("600"), store().find(a.id()).orElseThrow().balance());
    }

    @Test
    @DisplayName("posting to an unknown account is refused")
    void unknownAccount() {
        assertThrows(AccountNotFoundException.class,
                () -> store().post(DAY, List.of(new Entry(42, TransactionKind.DEPOSIT, money("1"), "x"))));
    }

    @Test
    @DisplayName("when the second entry fails, the first is undone too (all or nothing)")
    void allOrNothing() {
        Account a = store().open("A", AccountType.CURRENT, money("500"), DAY);
        assertThrows(AccountNotFoundException.class, () -> store().post(DAY, List.of(
                new Entry(a.id(), TransactionKind.TRANSFER_OUT, money("-200"), "out"),
                new Entry(77, TransactionKind.TRANSFER_IN, money("200"), "in"))));
        assertAll(
                () -> assertEquals(money("500"), store().find(a.id()).orElseThrow().balance()),
                () -> assertEquals(1, store().history(a.id()).size()));
    }

    @Test
    @DisplayName("a transfer that overdraws the sender leaves the receiver untouched")
    void transferOverdrawLeavesReceiver() {
        Account savings = store().open("S", AccountType.SAVINGS, money("600"), DAY);
        Account other = store().open("O", AccountType.CURRENT, money("0"), DAY);
        assertThrows(InsufficientFundsException.class, () -> store().post(DAY, List.of(
                new Entry(savings.id(), TransactionKind.TRANSFER_OUT, money("-500"), "out"),
                new Entry(other.id(), TransactionKind.TRANSFER_IN, money("500"), "in"))));
        assertEquals(money("0"), store().find(other.id()).orElseThrow().balance());
    }

    @Test
    @DisplayName("withdrawnOn sums only withdrawals, only that day, as a positive number")
    void withdrawnOn() {
        Account a = store().open("A", AccountType.CURRENT, money("1000"), DAY);
        store().post(DAY, List.of(new Entry(a.id(), TransactionKind.WITHDRAWAL, money("-100"), "w")));
        store().post(DAY, List.of(new Entry(a.id(), TransactionKind.WITHDRAWAL, money("-50.50"), "w")));
        store().post(DAY, List.of(new Entry(a.id(), TransactionKind.DEPOSIT, money("999"), "d")));
        store().post(DAY.plusDays(1), List.of(new Entry(a.id(), TransactionKind.WITHDRAWAL, money("-7"), "w")));
        assertEquals(money("150.50"), store().withdrawnOn(a.id(), DAY));
        assertEquals(money("0"), store().withdrawnOn(a.id(), DAY.minusDays(1)));
    }
}
