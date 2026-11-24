package com.codechanakya.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * One customer's bank account.
 *
 * The balance is private on purpose. Code outside this class cannot set it,
 * so it only ever changes through deposit, withdraw or Bank.transfer, and each
 * of those checks its input first. Keeping the data behind a few checked
 * methods is what "encapsulation" means in practice.
 */
public class Account {

    /** I2: the most one account may withdraw in total. */
    private static final BigDecimal WITHDRAWAL_LIMIT = new BigDecimal("20000.00");

    private final String accountNumber;
    private final String holderName;
    private BigDecimal balance;
    private BigDecimal withdrawnSoFar = BigDecimal.ZERO.setScale(2);
    private final ArrayList<String> history = new ArrayList<>();

    /**
     * Creates an account. Refuses to build one that makes no sense.
     *
     * Throwing an exception is how a constructor says "I cannot create this
     * object". Phase 4 covers exceptions properly; for now, read it as
     * "stop here and tell the caller what was wrong".
     */
    public Account(String accountNumber, String holderName, BigDecimal openingBalance) {
        if (holderName == null || holderName.isBlank()) {
            throw new IllegalArgumentException("Account holder name is required");
        }
        if (openingBalance.signum() < 0) {
            throw new IllegalArgumentException("Opening balance cannot be negative");
        }
        this.accountNumber = accountNumber;
        this.holderName = holderName.trim();
        this.balance = openingBalance.setScale(2, RoundingMode.HALF_UP);
    }

    /** Adds money. Returns false, and changes nothing, if the amount is zero or negative. */
    public boolean deposit(BigDecimal amount) {
        if (amount.signum() <= 0) {
            return false;
        }
        BigDecimal exact = amount.setScale(2, RoundingMode.HALF_UP);
        balance = balance.add(exact);
        history.add("DEPOSIT  " + exact.toPlainString());
        return true;
    }

    /**
     * Takes money out. Returns false, and changes nothing, if the amount is not
     * positive or is more than the balance. There is no overdraft in this bank.
     */
    public boolean withdraw(BigDecimal amount) {
        BigDecimal exact = amount.setScale(2, RoundingMode.HALF_UP);
        if (exact.signum() <= 0 || exact.compareTo(balance) > 0) {
            return false;
        }
        if (withdrawnSoFar.add(exact).compareTo(WITHDRAWAL_LIMIT) > 0) {
            return false;
        }
        balance = balance.subtract(exact);
        withdrawnSoFar = withdrawnSoFar.add(exact);
        history.add("WITHDRAW " + exact.toPlainString());
        return true;
    }

    /** B2: true when there is no money in the account. */
    public boolean isEmpty() {
        return balance.signum() == 0;
    }

    /** C1: prints every deposit and withdrawal, oldest first. */
    public void printHistory() {
        System.out.println("History for " + accountNumber);
        for (String line : history) {
            System.out.println("  " + line);
        }
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getHolderName() {
        return holderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return String.format("%-9s %-14s %12s", accountNumber, holderName, balance.toPlainString());
    }
}
