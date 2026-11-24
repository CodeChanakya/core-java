package com.codechanakya.bank;

/** Thrown when an account number does not exist. Checked, like InsufficientFundsException. */
public class AccountNotFoundException extends Exception {

    public AccountNotFoundException(String accountNumber) {
        super("No account with number " + accountNumber);
    }
}
