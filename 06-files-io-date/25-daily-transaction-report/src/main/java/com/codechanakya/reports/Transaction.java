package com.codechanakya.reports;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * One bank transaction.
 *
 * The date and the time of day are kept as two separate values, as they arrive in the file.
 * A LocalDate has no time and no time zone; a LocalTime has no date. That is exactly right
 * for a bank statement, which records what the branch clock said, not an exact instant.
 */
public class Transaction {

    private final LocalDate date;
    private final LocalTime time;
    private final String account;
    private final TxType type;
    private final BigDecimal amount;

    public Transaction(LocalDate date, LocalTime time, String account, TxType type, BigDecimal amount) {
        this.date = date;
        this.time = time;
        this.account = account;
        this.type = type;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getAccount() {
        return account;
    }

    public TxType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime dateTime() {
        return LocalDateTime.of(date, time);
    }
}
