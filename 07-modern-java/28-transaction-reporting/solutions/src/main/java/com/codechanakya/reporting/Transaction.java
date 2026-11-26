package com.codechanakya.reporting;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

/**
 * One transaction, written as a record.
 *
 * The one line in the header gives you: private final fields, a constructor, accessors named
 * after the fields (id(), not getId()), and equals, hashCode and toString that use every field.
 * Everything below is extra: validation, a parser and a helper method.
 *
 * @param id      unique id such as T-1001
 * @param account the account it belongs to
 * @param kind    deposit or withdrawal
 * @param amount  always positive; the kind says which way the money moved
 * @param date    the day it happened
 */
public record Transaction(String id, String account, Kind kind, BigDecimal amount, LocalDate date)
        implements Comparable<Transaction> {

    /**
     * The compact constructor. It has no parameter list, runs before the fields are assigned, and
     * is the place to check values. The fields are assigned for you at the end, so do not write
     * this.amount = ... here (the compiler rejects it).
     */
    public Transaction {
        if (!id.startsWith("T-")) {                                          // I1
            throw new IllegalArgumentException("Ids must start with T-, got " + id);
        }
        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("Amount must be positive for " + id);
        }
        // Reassigning the PARAMETER is allowed, and is what gets stored. setScale(2) with no rounding
        // mode throws for an amount like 10.005, which is what we want: money has two decimals.
        amount = amount.setScale(2);
    }

    /** Positive for money in, negative for money out. */
    public BigDecimal signedAmount() {
        return kind == Kind.DEPOSIT ? amount : amount.negate();
    }

    /** A copy with a different amount. Records cannot change, so "changing" one means making another. */
    public Transaction withAmount(BigDecimal newAmount) {
        return new Transaction(id, account, kind, newAmount, date);
    }

    @Override
    public int compareTo(Transaction other) {
        return date.compareTo(other.date);
    }

    /**
     * Reads one CSV line: id,account,kind,amount,date.
     * Returns an empty Optional when the line cannot be used, instead of null or an exception.
     */
    public static Optional<Transaction> parse(String line) {
        String[] p = line.split(",");
        if (p.length != 5) {
            return Optional.empty();
        }
        try {
            return Optional.of(new Transaction(p[0].trim(), p[1].trim(), Kind.valueOf(p[2].trim()),
                    new BigDecimal(p[3].trim()), LocalDate.parse(p[4].trim())));
        } catch (RuntimeException e) {
            // bad kind, bad number, bad date or a non-positive amount: all mean "not a usable line"
            return Optional.empty();
        }
    }
}
