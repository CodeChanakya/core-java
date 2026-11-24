package com.codechanakya.bank;

/**
 * Thrown when a transfer cannot be completed, whatever the underlying reason.
 *
 * The original problem is kept as the "cause". Callers of Bank.transfer get one
 * exception type to handle, and can still ask getCause() what really went
 * wrong. Throwing a new exception that wraps the old one is called chaining.
 */
public class TransferFailedException extends Exception {

    public TransferFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
