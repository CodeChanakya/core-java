package com.codechanakya.jdbcbank;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * All the SQL about accounts lives here.
 *
 * <p>Every method receives the {@link Connection} instead of opening its own. That is deliberate:
 * the caller decides when a transaction starts and ends, so several repository calls can succeed
 * or fail together. Every value reaches the database through a {@code ?} placeholder.
 */
public class AccountRepository {

    /** Inserts a new account and returns it with the id the database chose. */
    public Account open(Connection connection, String owner, BigDecimal opening) {
        String sql = "INSERT INTO account (owner, balance) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, owner);
            statement.setBigDecimal(2, opening);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return new Account(keys.getInt(1), owner, opening.setScale(2));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Opening an account for " + owner, e);
        }
    }

    /** Inserts many accounts with one round trip to the database (a JDBC batch). */
    public int openMany(Connection connection, List<String> owners, BigDecimal opening) {
        String sql = "INSERT INTO account (owner, balance) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (String owner : owners) {
                statement.setString(1, owner);
                statement.setBigDecimal(2, opening);
                statement.addBatch();
            }
            int inserted = 0;
            for (int count : statement.executeBatch()) {
                inserted += count;
            }
            return inserted;
        } catch (SQLException e) {
            throw new DataAccessException("Opening accounts in a batch", e);
        }
    }

    public Optional<Account> find(Connection connection, int id) {
        String sql = "SELECT id, owner, balance FROM account WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet rows = statement.executeQuery()) {
                return rows.next() ? Optional.of(map(rows)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Finding account " + id, e);
        }
    }

    public List<Account> findAll(Connection connection) {
        return query(connection, "SELECT id, owner, balance FROM account ORDER BY id", null);
    }

    /** Owners whose name starts with the given text. The text is a parameter, so it can never become SQL. */
    public List<Account> findByOwnerPrefix(Connection connection, String prefix) {
        return query(connection, "SELECT id, owner, balance FROM account WHERE owner LIKE ? ORDER BY id", prefix + "%");
    }

    /**
     * Takes money out only if there is enough, in one statement. Checking and changing together means
     * two customers cannot both pass the check and then both spend the same money.
     *
     * @return true when the money was taken, false when the account is missing or too poor
     */
    public boolean withdraw(Connection connection, int id, BigDecimal amount) {
        return update(connection, "UPDATE account SET balance = balance - ? WHERE id = ? AND balance >= ?", amount, id, amount);
    }

    /** @return true when the account exists and the money was added */
    public boolean deposit(Connection connection, int id, BigDecimal amount) {
        return update(connection, "UPDATE account SET balance = balance + ? WHERE id = ?", amount, id, null);
    }

    public boolean delete(Connection connection, int id) {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM account WHERE id = ?")) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataAccessException("Deleting account " + id, e);
        }
    }

    private boolean update(Connection connection, String sql, BigDecimal amount, int id, BigDecimal minimum) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBigDecimal(1, amount);
            statement.setInt(2, id);
            if (minimum != null) {
                statement.setBigDecimal(3, minimum);
            }
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            throw new DataAccessException("Updating account " + id, e);
        }
    }

    private List<Account> query(Connection connection, String sql, String parameter) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            if (parameter != null) {
                statement.setString(1, parameter);
            }
            try (ResultSet rows = statement.executeQuery()) {
                List<Account> accounts = new ArrayList<>();
                while (rows.next()) {
                    accounts.add(map(rows));
                }
                return accounts;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Querying accounts", e);
        }
    }

    private static Account map(ResultSet row) throws SQLException {
        return new Account(row.getInt("id"), row.getString("owner"), row.getBigDecimal("balance"));
    }
}
