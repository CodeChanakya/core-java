package com.codechanakya.bank;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * A bank is a collection of accounts plus the rules that involve more than one
 * of them, such as a transfer.
 *
 * ArrayList is a list that grows as you add to it. Phase 5 covers collections
 * properly; here, think of it as an array that never runs out of room.
 */
public class Bank {

    private final String name;
    private final ArrayList<Account> accounts = new ArrayList<>();
    private int nextNumber = 1001;

    public Bank(String name) {
        this.name = name;
    }

    /** Opens an account and gives it the next free number, such as ACC-1001. */
    public Account openAccount(String holderName, BigDecimal openingBalance) {
        Account account = new Account("ACC-" + nextNumber, holderName, openingBalance);
        nextNumber++;
        accounts.add(account);
        return account;
    }

    /** Returns the account with this number, or null when there is none. */
    public Account find(String accountNumber) {
        for (Account account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }

    /**
     * Moves money from one account to another.
     *
     * The withdrawal comes first. If it fails, nothing has changed and we stop.
     * The deposit cannot fail after that, because the amount has already been
     * accepted as positive, so no money can vanish halfway.
     */
    public boolean transfer(String fromNumber, String toNumber, BigDecimal amount) {
        Account from = find(fromNumber);
        Account to = find(toNumber);
        if (from == null || to == null || from == to) {
            return false;
        }
        if (!from.withdraw(amount)) {
            return false;
        }
        return to.deposit(amount);
    }

    /** The sum of every balance in the bank. */
    public BigDecimal totalHoldings() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (Account account : accounts) {
            total = total.add(account.getBalance());
        }
        return total;
    }

    public String getName() {
        return name;
    }

    public int accountCount() {
        return accounts.size();
    }

    /** Prints one line per account and the total. */
    public void printStatement() {
        System.out.println("--- " + name + " ---");
        for (Account account : accounts) {
            System.out.println(account);
        }
        System.out.println("Total holdings: " + totalHoldings().toPlainString());
    }
}
