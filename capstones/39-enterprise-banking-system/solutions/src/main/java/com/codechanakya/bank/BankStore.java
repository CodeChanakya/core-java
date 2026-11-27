package com.codechanakya.bank;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Where the bank keeps its accounts and history.
 *
 * <p>The most important method is {@link #post}. It applies a group of entries as one unit: either
 * every entry is recorded, or none is. A transfer is two entries, so a transfer can never half happen.
 * Every implementation must also refuse any entry that would take an account below its floor,
 * so the rule holds even if the code above it has a bug.
 */
public interface BankStore {

    /** Opens an account and records an OPENING entry for the starting balance. */
    Account open(String customer, AccountType type, BigDecimal opening, LocalDate date);

    Optional<Account> find(int id);

    /** All accounts, ordered by id. */
    List<Account> findAll();

    /**
     * Applies all entries or none.
     *
     * @throws AccountNotFoundException   when an entry names an account that does not exist
     * @throws InsufficientFundsException when an entry would take an account below its floor
     */
    void post(LocalDate date, List<Entry> entries);

    /** The history of one account, oldest first. */
    List<Transaction> history(int accountId);

    /** The total withdrawn from an account on one day, as a positive number. */
    BigDecimal withdrawnOn(int accountId, LocalDate date);
}
