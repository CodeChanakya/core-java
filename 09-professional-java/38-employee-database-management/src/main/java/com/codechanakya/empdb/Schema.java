package com.codechanakya.empdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Where the in-memory Derby database lives and what its table looks like. */
public final class Schema {

    static {
        // Keep Derby from writing a derby.log file into the current folder.
        System.setProperty("derby.stream.error.method", "java.io.OutputStream.nullOutputStream");
    }

    private Schema() {
    }

    /** A provider for the named in-memory database (created on first use). */
    public static ConnectionProvider inMemory(String name) {
        return () -> DriverManager.getConnection("jdbc:derby:memory:" + name + ";create=true");
    }

    public static void create(ConnectionProvider provider) {
        try (Connection connection = provider.open(); Statement statement = connection.createStatement()) {
            statement.executeUpdate("""
                    CREATE TABLE employee (
                        id         INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        name       VARCHAR(80)   NOT NULL,
                        email      VARCHAR(120)  NOT NULL UNIQUE,
                        department VARCHAR(20)   NOT NULL,
                        salary     DECIMAL(12,2) NOT NULL CHECK (salary > 0),
                        hired      DATE          NOT NULL
                    )""");
        } catch (SQLException e) {
            throw new EmployeeException("Could not create the employee table: " + e.getMessage(), e);
        }
    }

    public static void drop(String name) {
        try {
            DriverManager.getConnection("jdbc:derby:memory:" + name + ";drop=true");
        } catch (SQLException e) {
            if (!"08006".equals(e.getSQLState())) { // 08006 is Derby's way of saying "dropped"
                throw new EmployeeException("Could not drop " + name + ": " + e.getMessage(), e);
            }
        }
    }
}
