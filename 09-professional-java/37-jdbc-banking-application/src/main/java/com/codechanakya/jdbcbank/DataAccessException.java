package com.codechanakya.jdbcbank;

import java.sql.SQLException;

/**
 * Wraps a {@link SQLException}. The checked exception is turned into an unchecked one at the edge
 * of the data layer, and the original stays attached as the cause, with its SQLState.
 */
public class DataAccessException extends BankException {

    public DataAccessException(String action, SQLException cause) {
        super(action + " failed (SQLState " + cause.getSQLState() + "): " + cause.getMessage(), cause);
    }
}
