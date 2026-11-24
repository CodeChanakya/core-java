package com.codechanakya.bank;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Bank {

    private final ArrayList<Account> accounts = new ArrayList<>();
    private int nextNumber = 1001;

    public Account openAccount(String holderName, BigDecimal openingBalance) {
        Account account = new Account("ACC-" + nextNumber, holderName, openingBalance);
        nextNumber++;
        accounts.add(account);
        return account;
    }

    /** Program 8 returned null here and hoped callers would check. Now a missing account cannot be missed. */
    public Account find(String accountNumber) throws AccountNotFoundException {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        throw new AccountNotFoundException(accountNumber);
    }

    /**
     * Moves money between accounts.
     *
     * The two lookups and the withdrawal can each fail in their own way. The
     * multi-catch handles both checked exceptions with one block and wraps them
     * in a single TransferFailedException. Nothing has moved when it is thrown,
     * because the withdrawal comes first and the deposit cannot fail afterwards.
     */
    /** I2: a balance as text, or a friendly message. No exception reaches the caller. */
    public String safeBalance(String accountNumber) {
        try {
            return find(accountNumber).getBalance().toPlainString();
        } catch (AccountNotFoundException e) {
            return "unknown account";
        }
    }

    /**
     * C1: pays the same amount to several accounts. If any payment fails, the
     * ones already made are paid back to the sender, so the batch is all or nothing.
     * Rolling back by hand is what a database transaction automates for you in Phase 9.
     */
    public void batchTransfer(String fromNumber, String[] toNumbers, BigDecimal each) throws TransferFailedException {
        int completed = 0;
        try {
            for (String toNumber : toNumbers) {
                transfer(fromNumber, toNumber, each);
                completed++;
            }
        } catch (TransferFailedException e) {
            for (int i = 0; i < completed; i++) {
                try {
                    find(toNumbers[i]).withdraw(each);
                    find(fromNumber).deposit(each);
                } catch (AccountNotFoundException | InsufficientFundsException | LimitExceededException undoFailure) {
                    // The money was just paid in, so these cannot happen. Say so loudly if they ever do.
                    throw new IllegalStateException("Rollback failed", undoFailure);
                }
            }
            throw e;
        }
    }

    public void transfer(String fromNumber, String toNumber, BigDecimal amount) throws TransferFailedException {
        try {
            Account from = find(fromNumber);
            Account to = find(toNumber);
            from.withdraw(amount);
            to.deposit(amount);
        } catch (AccountNotFoundException | InsufficientFundsException | LimitExceededException e) {
            throw new TransferFailedException("Transfer of " + amount.toPlainString() + " failed", e);
        }
    }
}
