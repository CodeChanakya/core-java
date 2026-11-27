package com.codechanakya.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Runs the shared contract against a real (in-memory) Derby database, plus checks only a database can do. */
class JdbcBankStoreTest extends BankStoreContract {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    private String name;
    private ConnectionProvider provider;
    private BankStore store;

    @BeforeEach
    void setUp() {
        name = "bank-test-" + COUNTER.incrementAndGet();
        provider = Schema.inMemory(name);
        Schema.create(provider);
        store = new JdbcBankStore(provider);
    }

    @AfterEach
    void tearDown() {
        Schema.drop(name);
    }

    @Override
    BankStore store() {
        return store;
    }

    @Test
    @DisplayName("the database itself refuses a balance below the floor, even from raw SQL")
    void databaseCheckRule() throws SQLException {
        Account a = store.open("A", AccountType.SAVINGS, money("600"), DAY);
        try (Connection c = provider.open();
             PreparedStatement s = c.prepareStatement("UPDATE account SET balance = ? WHERE id = ?")) {
            s.setBigDecimal(1, new BigDecimal("1"));
            s.setInt(2, a.id());
            SQLException e = assertThrows(SQLException.class, s::executeUpdate);
            assertEquals("23513", e.getSQLState());
        }
    }

    @Test
    @DisplayName("a customer name with SQL in it is stored as plain text")
    void injectionIsHarmless() {
        Account a = store.open("x'); DROP TABLE account; --", AccountType.CURRENT, money("1"), DAY);
        assertEquals("x'); DROP TABLE account; --", store.find(a.id()).orElseThrow().customer());
        assertEquals(1, store.findAll().size());
    }
}
