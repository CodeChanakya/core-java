package com.codechanakya.hr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** The in-memory Derby database and its two tables. */
public final class Schema {

    static {
        System.setProperty("derby.stream.error.method", "java.io.OutputStream.nullOutputStream");
    }

    private Schema() {
    }

    public static ConnectionProvider inMemory(String name) {
        return () -> DriverManager.getConnection("jdbc:derby:memory:" + name + ";create=true");
    }

    public static void create(ConnectionProvider provider) {
        try (Connection c = provider.open(); Statement s = c.createStatement()) {
            s.executeUpdate("""
                    CREATE TABLE employee (
                        id             INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        name           VARCHAR(80)   NOT NULL,
                        email          VARCHAR(120)  NOT NULL,
                        department     VARCHAR(12)   NOT NULL,
                        birth_date     DATE          NOT NULL,
                        join_date      DATE          NOT NULL,
                        monthly_salary DECIMAL(12,2) NOT NULL,
                        active         BOOLEAN       NOT NULL,
                        CONSTRAINT unique_email UNIQUE (email),
                        CONSTRAINT salary_positive CHECK (monthly_salary > 0)
                    )""");
            s.executeUpdate("""
                    CREATE TABLE leave_request (
                        id          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        employee_id INTEGER     NOT NULL REFERENCES employee(id),
                        type        VARCHAR(8)  NOT NULL,
                        from_date   DATE        NOT NULL,
                        to_date     DATE        NOT NULL,
                        days        INTEGER     NOT NULL,
                        status      VARCHAR(8)  NOT NULL
                    )""");
        } catch (SQLException e) {
            throw new HrException("Could not create the tables: " + e.getMessage(), e);
        }
    }

    public static void drop(String name) {
        try {
            DriverManager.getConnection("jdbc:derby:memory:" + name + ";drop=true");
        } catch (SQLException e) {
            if (!"08006".equals(e.getSQLState())) {
                throw new HrException("Could not drop " + name + ": " + e.getMessage(), e);
            }
        }
    }
}
