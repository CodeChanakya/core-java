package com.codechanakya.jdbcbank;

import java.math.BigDecimal;

/** One row of the ACCOUNT table, as a Java object. */
public record Account(int id, String owner, BigDecimal balance) {
}
