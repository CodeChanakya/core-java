package com.codechanakya.reporting;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Main {

    /** A text block: everything between the triple quotes, with the common indentation removed. */
    private static final String DATA = """
            id,account,kind,amount,date
            T-1001,ACC-1,DEPOSIT,5000.00,2026-03-02
            T-1002,ACC-2,WITHDRAWAL,1200.5,2026-03-02
            T-1003,ACC-1,WITHDRAWAL,300,2026-03-03
            T-1004,ACC-3,DEPOSIT,800.00,2026-03-03
            T-1005,ACC-2,DEPOSIT,4000.00,2026-03-04
            T-1006,ACC-1,TRANSFER,100.00,2026-03-04
            T-1007,ACC-3,WITHDRAWAL,-50.00,2026-03-05
            T-1008,ACC-1,DEPOSIT,1000.00,not-a-date
            T-1009,ACC-2,DEPOSIT,250.00,2026-03-06
            T-1010,ACC-1
            """;

    public static void main(String[] args) {
        System.out.println("--- What a record gives you for free ---");
        Transaction a = new Transaction("T-1", "ACC-1", Kind.DEPOSIT, new BigDecimal("100"), LocalDate.of(2026, 3, 2));
        Transaction b = new Transaction("T-1", "ACC-1", Kind.DEPOSIT, new BigDecimal("100.00"), LocalDate.of(2026, 3, 2));
        System.out.println("  toString : " + a);
        System.out.println("  accessor : id() = " + a.id() + ", amount() = " + a.amount());
        System.out.println("  equals   : " + a.equals(b) + " (same values, so equal; different objects: " + (a != b) + ")");
        System.out.println("  hashCode : " + (a.hashCode() == b.hashCode()));
        Set<Transaction> unique = new HashSet<>();
        unique.add(a);
        unique.add(b);
        System.out.println("  In a HashSet twice: " + unique.size() + " item");
        Transaction bigger = a.withAmount(new BigDecimal("250"));
        System.out.println("  withAmount made a new one: " + bigger.amount() + ", the old one is still " + a.amount());

        System.out.println();
        System.out.println("--- Validation in the compact constructor ---");
        try {
            new Transaction("T-2", "ACC-1", Kind.DEPOSIT, BigDecimal.ZERO, LocalDate.of(2026, 3, 2));
        } catch (IllegalArgumentException e) {
            System.out.println("  Refused: " + e.getMessage());
        }

        System.out.println();
        System.out.println("--- Loading a text block ---");
        Ledger ledger = new Ledger();
        ledger.load(DATA);
        System.out.println("  Loaded " + ledger.all().size() + " transactions, rejected " + ledger.rejected().size());
        for (String line : ledger.rejected()) {
            System.out.println("    " + line);
        }

        System.out.println();
        System.out.println("--- Optional: an answer that may not exist ---");
        Optional<Transaction> found = ledger.findById("T-1004");
        System.out.println("  isPresent        : " + found.isPresent());
        System.out.println("  map the amount   : " + found.map(Transaction::amount).orElse(BigDecimal.ZERO));
        System.out.println("  missing id, orElse: " + ledger.findById("T-9999").map(Transaction::account).orElse("(none)"));
        found.ifPresent(t -> System.out.println("  ifPresent        : " + t.id() + " belongs to " + t.account()));
        ledger.findById("T-9999").ifPresentOrElse(
                t -> System.out.println("  never printed"),
                () -> System.out.println("  ifPresentOrElse  : T-9999 is not in the ledger"));
        System.out.println("  Largest deposit  : " + ledger.largestDeposit().map(Transaction::id).orElse("none"));
        System.out.println("  Latest for ACC-3 : " + ledger.latestFor("ACC-3").map(Transaction::id).orElse("(no transactions)"));
        System.out.println("  Latest for ACC-9 : " + ledger.latestFor("ACC-9").map(Transaction::id).orElse("(no transactions)"));

        System.out.println();
        System.out.println("--- orElseThrow when \"no\" is a real error ---");
        try {
            ledger.findById("T-9999").orElseThrow(() -> new IllegalStateException("Transaction T-9999 does not exist"));
        } catch (IllegalStateException e) {
            System.out.println("  " + e.getMessage());
        }

        System.out.println();
        System.out.println("--- A report from records ---");
        for (AccountSummary row : ledger.summaries()) {
            System.out.printf("  %-6s %d tx  in %9s  out %9s  net %9s%n",
                    row.account(), row.count(), row.deposits(), row.withdrawals(), row.net());
        }
        System.out.println("  " + ledger.summaries().get(0));

        System.out.println();
        System.out.println("--- A record can live inside a method ---");
        record Range(BigDecimal low, BigDecimal high) {
        }
        BigDecimal low = null;
        BigDecimal high = null;
        for (Transaction t : ledger.all()) {
            low = (low == null || t.amount().compareTo(low) < 0) ? t.amount() : low;
            high = (high == null || t.amount().compareTo(high) > 0) ? t.amount() : high;
        }
        System.out.println("  " + new Range(low, high));
    }
}
