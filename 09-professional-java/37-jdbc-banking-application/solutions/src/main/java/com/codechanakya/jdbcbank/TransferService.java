package com.codechanakya.jdbcbank;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Moves money between two accounts as one all-or-nothing unit: a transaction.
 *
 * <p>A transfer is two updates (take from one account, add to the other) and one log row. If anything
 * goes wrong after the first update, {@code rollback()} puts everything back as if nothing happened.
 */
public class TransferService {

    private final Database database;
    private final AccountRepository accounts;
    private final AuditLog log;

    public TransferService(Database database, AccountRepository accounts, AuditLog log) {
        this.database = database;
        this.accounts = accounts;
        this.log = log;
    }

    /** C1: one leg of a multi-transfer. */
    public record Move(int from, int to, BigDecimal amount) {
    }

    /** B1: closes an account, but only when it holds no money. */
    public void closeAccount(int id) {
        try (Connection connection = database.connect()) {
            connection.setAutoCommit(false);
            try {
                Account account = accounts.find(connection, id).orElseThrow(() -> new AccountNotFoundException(id));
                if (account.balance().signum() != 0) {
                    throw new BankException("Account " + id + " still holds " + account.balance().toPlainString());
                }
                accounts.delete(connection, id);
                connection.commit();
            } catch (RuntimeException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Closing account " + id, e);
        }
    }

    /** C1: several transfers in ONE transaction. If any leg fails, none of them happens. */
    public void transferAll(java.util.List<Move> moves) {
        try (Connection connection = database.connect()) {
            connection.setAutoCommit(false);
            try {
                for (Move move : moves) {
                    if (!accounts.withdraw(connection, move.from(), move.amount())) {
                        throw accounts.find(connection, move.from()).isPresent()
                                ? new InsufficientFundsException(move.from(), move.amount())
                                : new AccountNotFoundException(move.from());
                    }
                    if (!accounts.deposit(connection, move.to(), move.amount())) {
                        throw new AccountNotFoundException(move.to());
                    }
                    log.record(connection, move.from(), move.to(), move.amount(), "SUCCESS", "part of a batch");
                }
                connection.commit();
            } catch (RuntimeException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Batch transfer", e);
        }
    }

    public void transfer(int from, int to, BigDecimal amount) {
        if (amount.signum() <= 0) {
            throw new BankException("A transfer must be for a positive amount, but was " + amount.toPlainString());
        }
        if (from == to) {
            throw new BankException("Cannot transfer to the same account");
        }
        try (Connection connection = database.connect()) {
            connection.setAutoCommit(false); // from here on nothing is permanent until commit()
            try {
                if (!accounts.withdraw(connection, from, amount)) {
                    throw accounts.find(connection, from).isPresent()
                            ? new InsufficientFundsException(from, amount)
                            : new AccountNotFoundException(from);
                }
                if (!accounts.deposit(connection, to, amount)) {
                    throw new AccountNotFoundException(to); // the money is already taken out; rollback returns it
                }
                log.record(connection, from, to, amount, "SUCCESS", null);
                connection.commit();
            } catch (RuntimeException e) {
                connection.rollback();
                recordFailure(from, to, amount, e);
                throw e;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Transfer from " + from + " to " + to, e);
        }
    }

    /** The failed transfer was rolled back, including its log row, so the failure is logged in a fresh transaction. */
    private void recordFailure(int from, int to, BigDecimal amount, RuntimeException reason) {
        try (Connection connection = database.connect()) {
            log.record(connection, from, to, amount, "FAILED", reason.getMessage());
        } catch (SQLException e) {
            throw new DataAccessException("Logging a failed transfer", e);
        }
    }
}
