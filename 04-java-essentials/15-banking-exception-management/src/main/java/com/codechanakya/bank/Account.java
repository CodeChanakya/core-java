package com.codechanakya.bank;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Program 8's account, rebuilt to report problems with exceptions instead of
 * returning true or false.
 *
 * A boolean says "no" but not why, and a caller can ignore it. An exception
 * carries the reason and cannot be ignored by accident: unhandled, it stops
 * the program.
 */
public class Account {

    private final String accountNumber;
    private final String holderName;
    private BigDecimal balance;

    public Account(String accountNumber, String holderName, BigDecimal openingBalance) {
        this.accountNumber = accountNumber;
        this.holderName = holderName;
        this.balance = openingBalance.setScale(2, RoundingMode.HALF_UP);
    }

    /** Adds money. Throws InvalidAmountException for zero or less. */
    public void deposit(BigDecimal amount) {
        requirePositive(amount);
        balance = balance.add(amount.setScale(2, RoundingMode.HALF_UP));
    }

    /**
     * Takes money out.
     *
     * The "throws" on the first line is a promise to the reader: this method
     * can end with an InsufficientFundsException. Callers must handle it.
     * InvalidAmountException needs no such promise because it is unchecked.
     */
    public void withdraw(BigDecimal amount) throws InsufficientFundsException {
        requirePositive(amount);
        BigDecimal exact = amount.setScale(2, RoundingMode.HALF_UP);
        if (exact.compareTo(balance) > 0) {
            throw new InsufficientFundsException(exact, balance);
        }
        balance = balance.subtract(exact);
    }

    private void requirePositive(BigDecimal amount) {
        if (amount.signum() <= 0) {
            throw new InvalidAmountException(amount);
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
}
