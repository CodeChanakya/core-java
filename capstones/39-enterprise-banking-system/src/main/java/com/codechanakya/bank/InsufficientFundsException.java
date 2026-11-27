package com.codechanakya.bank;

public class InsufficientFundsException extends BankException {

    public InsufficientFundsException(int id) {
        super("Account " + id + " would go below its lowest allowed balance");
    }
}
