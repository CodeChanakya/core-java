package com.codechanakya.bank;

import java.sql.Connection;
import java.sql.SQLException;

/** Anything that can open a database connection. */
@FunctionalInterface
public interface ConnectionProvider {

    Connection open() throws SQLException;
}
