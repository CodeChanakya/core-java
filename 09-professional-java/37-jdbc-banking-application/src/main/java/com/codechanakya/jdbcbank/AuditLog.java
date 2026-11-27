package com.codechanakya.jdbcbank;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** The TRANSFER_LOG table: one row for every transfer that was tried, whatever happened to it. */
public class AuditLog {

    public record Entry(int id, int from, int to, BigDecimal amount, String status, String note) {
    }

    public void record(Connection connection, int from, int to, BigDecimal amount, String status, String note) {
        String sql = "INSERT INTO transfer_log (from_id, to_id, amount, status, note) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, from);
            statement.setInt(2, to);
            statement.setBigDecimal(3, amount);
            statement.setString(4, status);
            statement.setString(5, note);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Writing the audit log", e);
        }
    }

    public List<Entry> all(Connection connection) {
        String sql = "SELECT id, from_id, to_id, amount, status, note FROM transfer_log ORDER BY id";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet rows = statement.executeQuery()) {
            List<Entry> entries = new ArrayList<>();
            while (rows.next()) {
                entries.add(new Entry(rows.getInt(1), rows.getInt(2), rows.getInt(3), rows.getBigDecimal(4),
                        rows.getString(5), rows.getString(6)));
            }
            return entries;
        } catch (SQLException e) {
            throw new DataAccessException("Reading the audit log", e);
        }
    }
}
