package com.codechanakya.bank;

public class AccountNotFoundException extends BankException {

    public AccountNotFoundException(int id) {
        super("No account with id " + id);
    }
}
