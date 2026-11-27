package com.codechanakya.jdbcbank;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows why you must never build SQL by gluing text together. This is the one class in the project
 * that does it, on purpose, so you can see the attack work.
 */
public final class InjectionDemo {

    private InjectionDemo() {
    }

    /** DO NOT COPY: the owner text becomes part of the SQL itself. */
    public static List<String> unsafeFind(Connection connection, String owner) {
        String sql = "SELECT owner FROM account WHERE owner = '" + owner + "'";
        List<String> owners = new ArrayList<>();
        try (Statement statement = connection.createStatement(); ResultSet rows = statement.executeQuery(sql)) {
            while (rows.next()) {
                owners.add(rows.getString(1));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unsafe lookup", e);
        }
        return owners;
    }
}
