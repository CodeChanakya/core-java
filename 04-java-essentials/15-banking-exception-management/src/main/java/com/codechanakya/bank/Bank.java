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
    public void transfer(String fromNumber, String toNumber, BigDecimal amount) throws TransferFailedException {
        try {
            Account from = find(fromNumber);
            Account to = find(toNumber);
            from.withdraw(amount);
            to.deposit(amount);
        } catch (AccountNotFoundException | InsufficientFundsException e) {
            throw new TransferFailedException("Transfer of " + amount.toPlainString() + " failed", e);
        }
    }
}
