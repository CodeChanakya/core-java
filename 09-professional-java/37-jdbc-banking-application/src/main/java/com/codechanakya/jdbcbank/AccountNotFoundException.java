package com.codechanakya.jdbcbank;

/** No account has the given id. */
public class AccountNotFoundException extends BankException {

    public AccountNotFoundException(int accountId) {
        super("No account with id " + accountId);
    }
}
