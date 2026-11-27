package com.codechanakya.jdbcbank;

/** Base class for problems the bank reports to its callers. Unchecked, so it does not clutter signatures. */
public class BankException extends RuntimeException {

    public BankException(String message) {
        super(message);
    }

    public BankException(String message, Throwable cause) {
        super(message, cause);
    }
}
