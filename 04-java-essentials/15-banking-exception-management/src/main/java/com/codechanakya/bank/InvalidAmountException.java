package com.codechanakya.bank;

import java.math.BigDecimal;

/**
 * Thrown when someone asks to move zero or a negative amount.
 *
 * This is an unchecked exception (it extends RuntimeException through
 * IllegalArgumentException). It means the caller made a mistake that a
 * correct program would not make, so Java does not force every method to
 * declare or catch it.
 */
public class InvalidAmountException extends IllegalArgumentException {

    public InvalidAmountException(BigDecimal amount) {
        super("Amount must be greater than zero, but was " + amount.toPlainString());
    }
}
