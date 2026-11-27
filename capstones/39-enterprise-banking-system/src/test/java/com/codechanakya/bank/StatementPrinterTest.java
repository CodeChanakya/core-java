package com.codechanakya.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;

class StatementPrinterTest {

    @Test
    void formatsHeaderLinesAndClosingBalance() {
        Account account = new Account(7, "Asha", AccountType.SAVINGS, new BigDecimal("1500"));
        Transaction t = new Transaction(1, 7, TransactionKind.DEPOSIT, new BigDecimal("500"), new BigDecimal("1500"),
                "Deposit", LocalDate.of(2025, 6, 30));
        List<String> lines = StatementPrinter.format(account, List.of(t));
        assertEquals(3, lines.size());
        assertEquals("Statement for account 7 (Asha, SAVINGS)", lines.get(0));
        assertEquals("  2025-06-30  DEPOSIT             500.00  balance      1500.00  Deposit", lines.get(1));
        assertEquals("  Closing balance: 1500.00", lines.get(2));
    }
}
