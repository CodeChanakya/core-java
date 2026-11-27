package com.codechanakya.jdbcbank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Knows where the database is and how its tables look.
 *
 * <p>The bank uses Apache Derby in "memory" mode: a real SQL database that lives inside the JVM
 * and disappears when the program ends. That makes the program repeatable and leaves no files
 * behind, while the JDBC code is exactly what you would write for MySQL, PostgreSQL or Oracle.
 * Only the URL (and a few type names in the SQL) change.
 */
public final class Database {

    private final String url;
    private final String name;

    static {
        // Derby normally writes a derby.log file in the current folder. Send that text nowhere.
        System.setProperty("derby.stream.error.method", "java.io.OutputStream.nullOutputStream");
    }

    /** @param name a name for this in-memory database; two names mean two separate databases */
    public Database(String name) {
        this.name = name;
        this.url = "jdbc:derby:memory:" + name;
    }

    /** Opens a new connection, creating the database the first time. */
    public Connection connect() {
        try {
            return DriverManager.getConnection(url + ";create=true");
        } catch (SQLException e) {
            throw new DataAccessException("Connecting to " + name, e);
        }
    }

    /** Creates the tables. Call once, right after the first connection. */
    public void createSchema() {
        try (Connection connection = connect(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE account (
                        id      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        owner   VARCHAR(60)   NOT NULL,
                        balance DECIMAL(12,2) NOT NULL CHECK (balance >= 0)
                    )""");
            statement.executeUpdate("""
                    CREATE TABLE transfer_log (
                        id      INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        from_id INTEGER       NOT NULL,
                        to_id   INTEGER       NOT NULL,
                        amount  DECIMAL(12,2) NOT NULL,
                        status  VARCHAR(12)   NOT NULL,
                        note    VARCHAR(80)
                    )""");
        } catch (SQLException e) {
            throw new DataAccessException("Creating the tables", e);
        }
    }

    /** Throws the whole in-memory database away. */
    public void drop() {
        try {
            DriverManager.getConnection(url + ";drop=true");
        } catch (SQLException e) {
            if (!"08006".equals(e.getSQLState())) { // 08006 is how Derby says "the database was dropped"
                throw new DataAccessException("Dropping " + name, e);
            }
        }
    }
}
