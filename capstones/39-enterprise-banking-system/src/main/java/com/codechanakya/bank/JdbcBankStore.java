package com.codechanakya.bank;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The database version of {@link BankStore}.
 *
 * <p>{@link #post} runs inside one transaction. Each entry changes the balance with
 * {@code UPDATE ... SET balance = balance + ?}, so two customers can never overwrite each other's
 * change, and the table's CHECK rule refuses a balance below the floor (SQLState 23513).
 */
public class JdbcBankStore implements BankStore {

    private static final String ACCOUNT_COLUMNS = "id, customer, type, balance";

    private final ConnectionProvider provider;

    public JdbcBankStore(ConnectionProvider provider) {
        this.provider = provider;
    }

    @Override
    public Account open(String customer, AccountType type, BigDecimal opening, LocalDate date) {
        try (Connection connection = provider.open()) {
            connection.setAutoCommit(false);
            try {
                int id;
                try (PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO account (customer, type, balance, floor) VALUES (?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS)) {
                    statement.setString(1, customer);
                    statement.setString(2, type.name());
                    statement.setBigDecimal(3, opening);
                    statement.setBigDecimal(4, type.floor());
                    statement.executeUpdate();
                    try (ResultSet keys = statement.getGeneratedKeys()) {
                        keys.next();
                        id = keys.getInt(1);
                    }
                }
                Account account = new Account(id, customer, type, opening);
                insertLedger(connection, id, TransactionKind.OPENING, account.balance(), account.balance(), "Account opened", date);
                connection.commit();
                return account;
            } catch (SQLException e) {
                connection.rollback();
                throw translate("Opening an account", e, 0);
            }
        } catch (SQLException e) {
            throw translate("Opening an account", e, 0);
        }
    }

    @Override
    public Optional<Account> find(int id) {
        return queryAccounts("SELECT " + ACCOUNT_COLUMNS + " FROM account WHERE id = ?", id).stream().findFirst();
    }

    @Override
    public List<Account> findAll() {
        return queryAccounts("SELECT " + ACCOUNT_COLUMNS + " FROM account ORDER BY id");
    }

    @Override
    public void post(LocalDate date, List<Entry> entries) {
        try (Connection connection = provider.open()) {
            connection.setAutoCommit(false);
            int current = 0;
            try {
                for (Entry entry : entries) {
                    current = entry.accountId();
                    try (PreparedStatement statement = connection.prepareStatement(
                            "UPDATE account SET balance = balance + ? WHERE id = ?")) {
                        statement.setBigDecimal(1, entry.amount());
                        statement.setInt(2, entry.accountId());
                        if (statement.executeUpdate() == 0) {
                            throw new AccountNotFoundException(entry.accountId());
                        }
                    }
                    BigDecimal after = balanceOf(connection, entry.accountId());
                    insertLedger(connection, entry.accountId(), entry.kind(), entry.amount(), after, entry.note(), date);
                }
                connection.commit();
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                if (e instanceof SQLException sql) {
                    throw translate("Posting entries", sql, current);
                }
                throw (RuntimeException) e;
            }
        } catch (SQLException e) {
            throw translate("Posting entries", e, 0);
        }
    }

    @Override
    public List<Transaction> history(int accountId) {
        String sql = "SELECT id, account_id, kind, amount, balance_after, note, on_date FROM ledger WHERE account_id = ? ORDER BY id";
        try (Connection connection = provider.open(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            try (ResultSet rows = statement.executeQuery()) {
                List<Transaction> list = new ArrayList<>();
                while (rows.next()) {
                    list.add(new Transaction(rows.getInt(1), rows.getInt(2), TransactionKind.valueOf(rows.getString(3)),
                            rows.getBigDecimal(4), rows.getBigDecimal(5), rows.getString(6), rows.getDate(7).toLocalDate()));
                }
                return list;
            }
        } catch (SQLException e) {
            throw translate("Reading history", e, accountId);
        }
    }

    @Override
    public BigDecimal withdrawnOn(int accountId, LocalDate date) {
        String sql = "SELECT COALESCE(SUM(-amount), 0) FROM ledger WHERE account_id = ? AND on_date = ? AND kind = 'WITHDRAWAL'";
        try (Connection connection = provider.open(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            statement.setDate(2, Date.valueOf(date));
            try (ResultSet rows = statement.executeQuery()) {
                rows.next();
                return rows.getBigDecimal(1).setScale(2);
            }
        } catch (SQLException e) {
            throw translate("Summing withdrawals", e, accountId);
        }
    }

    private BigDecimal balanceOf(Connection connection, int id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT balance FROM account WHERE id = ?")) {
            statement.setInt(1, id);
            try (ResultSet rows = statement.executeQuery()) {
                rows.next();
                return rows.getBigDecimal(1);
            }
        }
    }

    private void insertLedger(Connection connection, int accountId, TransactionKind kind, BigDecimal amount,
                              BigDecimal after, String note, LocalDate date) throws SQLException {
        String sql = "INSERT INTO ledger (account_id, kind, amount, balance_after, note, on_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, accountId);
            statement.setString(2, kind.name());
            statement.setBigDecimal(3, amount);
            statement.setBigDecimal(4, after);
            statement.setString(5, note);
            statement.setDate(6, Date.valueOf(date));
            statement.executeUpdate();
        }
    }

    private List<Account> queryAccounts(String sql, Object... parameters) {
        try (Connection connection = provider.open(); PreparedStatement statement = connection.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            try (ResultSet rows = statement.executeQuery()) {
                List<Account> list = new ArrayList<>();
                while (rows.next()) {
                    list.add(new Account(rows.getInt(1), rows.getString(2), AccountType.valueOf(rows.getString(3)), rows.getBigDecimal(4)));
                }
                return list;
            }
        } catch (SQLException e) {
            throw translate("Querying accounts", e, 0);
        }
    }

    /** SQLState 23513 is a broken CHECK rule: the balance would go below its floor. */
    private static BankException translate(String action, SQLException e, int accountId) {
        if ("23513".equals(e.getSQLState())) {
            return new InsufficientFundsException(accountId);
        }
        return new BankException(action + " failed (SQLState " + e.getSQLState() + "): " + e.getMessage(), e);
    }
}
