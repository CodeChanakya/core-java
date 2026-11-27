package com.codechanakya.bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** The in-memory Derby database and its tables. */
public final class Schema {

    static {
        // Keep Derby from writing a derby.log file into the current folder.
        System.setProperty("derby.stream.error.method", "java.io.OutputStream.nullOutputStream");
    }

    private Schema() {
    }

    public static ConnectionProvider inMemory(String name) {
        return () -> DriverManager.getConnection("jdbc:derby:memory:" + name + ";create=true");
    }

    public static void create(ConnectionProvider provider) {
        try (Connection connection = provider.open(); Statement statement = connection.createStatement()) {
            // The CHECK rule is the bank's last line of defence: no bug above can push a balance below its floor.
            statement.executeUpdate("""
                    CREATE TABLE account (
                        id       INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        customer VARCHAR(80)   NOT NULL,
                        type     VARCHAR(10)   NOT NULL,
                        balance  DECIMAL(14,2) NOT NULL,
                        floor    DECIMAL(14,2) NOT NULL,
                        CONSTRAINT balance_above_floor CHECK (balance >= floor)
                    )""");
            statement.executeUpdate("""
                    CREATE TABLE ledger (
                        id            INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        account_id    INTEGER       NOT NULL REFERENCES account(id),
                        kind          VARCHAR(14)   NOT NULL,
                        amount        DECIMAL(14,2) NOT NULL,
                        balance_after DECIMAL(14,2) NOT NULL,
                        note          VARCHAR(80),
                        on_date       DATE          NOT NULL
                    )""");
        } catch (SQLException e) {
            throw new BankException("Could not create the tables: " + e.getMessage(), e);
        }
    }

    public static void drop(String name) {
        try {
            DriverManager.getConnection("jdbc:derby:memory:" + name + ";drop=true");
        } catch (SQLException e) {
            if (!"08006".equals(e.getSQLState())) {
                throw new BankException("Could not drop " + name + ": " + e.getMessage(), e);
            }
        }
    }
}
