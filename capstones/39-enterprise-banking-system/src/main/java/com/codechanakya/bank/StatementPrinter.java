package com.codechanakya.bank;

import java.util.ArrayList;
import java.util.List;

/** Formats an account statement as lines of text. Pure: the same input always gives the same lines. */
public final class StatementPrinter {

    private StatementPrinter() {
    }

    public static List<String> format(Account account, List<Transaction> history) {
        List<String> lines = new ArrayList<>();
        lines.add("Statement for account " + account.id() + " (" + account.customer() + ", " + account.type() + ")");
        for (Transaction t : history) {
            lines.add(String.format("  %s  %-13s %12s  balance %12s  %s", t.date(), t.kind(),
                    t.amount().toPlainString(), t.balanceAfter().toPlainString(), t.note()));
        }
        lines.add("  Closing balance: " + account.balance().toPlainString());
        return lines;
    }
}
