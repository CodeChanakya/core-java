package com.codechanakya.empdb;

import java.sql.Connection;
import java.sql.SQLException;

/** Anything that can open a database connection. A one-method interface, so a lambda or method reference fits. */
@FunctionalInterface
public interface ConnectionProvider {

    Connection open() throws SQLException;
}
