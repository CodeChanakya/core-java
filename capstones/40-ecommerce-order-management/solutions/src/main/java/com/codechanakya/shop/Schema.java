package com.codechanakya.shop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** The in-memory Derby database and its four tables. */
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
                    CREATE TABLE product (
                        sku      VARCHAR(20) PRIMARY KEY,
                        name     VARCHAR(60)   NOT NULL,
                        category VARCHAR(12)   NOT NULL,
                        price    DECIMAL(12,2) NOT NULL,
                        stock    INTEGER       NOT NULL,
                        CONSTRAINT stock_not_negative CHECK (stock >= 0)
                    )""");
            s.executeUpdate("""
                    CREATE TABLE customer (
                        id   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        name VARCHAR(60) NOT NULL,
                        city VARCHAR(40) NOT NULL
                    )""");
            s.executeUpdate("""
                    CREATE TABLE orders (
                        id          INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        customer_id INTEGER       NOT NULL REFERENCES customer(id),
                        status      VARCHAR(10)   NOT NULL,
                        discount    DECIMAL(12,2) NOT NULL,
                        total       DECIMAL(12,2) NOT NULL,
                        placed_on   DATE          NOT NULL
                    )""");
            s.executeUpdate("""
                    CREATE TABLE order_line (
                        order_id   INTEGER       NOT NULL REFERENCES orders(id),
                        sku        VARCHAR(20)   NOT NULL REFERENCES product(sku),
                        quantity   INTEGER       NOT NULL,
                        unit_price DECIMAL(12,2) NOT NULL
                    )""");
        } catch (SQLException e) {
            throw new ShopException("Could not create the tables: " + e.getMessage(), e);
        }
    }

    public static void drop(String name) {
        try {
            DriverManager.getConnection("jdbc:derby:memory:" + name + ";drop=true");
        } catch (SQLException e) {
            if (!"08006".equals(e.getSQLState())) {
                throw new ShopException("Could not drop " + name + ": " + e.getMessage(), e);
            }
        }
    }
}
