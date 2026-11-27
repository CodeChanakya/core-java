package com.codechanakya.bank;

/** Base class for everything the bank refuses on purpose. */
public class BankException extends RuntimeException {

    public BankException(String message) {
        super(message);
    }

    public BankException(String message, Throwable cause) {
        super(message, cause);
    }
}
