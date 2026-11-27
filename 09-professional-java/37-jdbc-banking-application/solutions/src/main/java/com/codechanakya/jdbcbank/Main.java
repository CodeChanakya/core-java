package com.codechanakya.jdbcbank;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/** Runs the whole bank against an in-memory Derby database and prints what happens. */
public class Main {

    public static void main(String[] args) throws SQLException {
        Database database = new Database("bank");
        AccountRepository accounts = new AccountRepository();
        AuditLog log = new AuditLog();
        TransferService transfers = new TransferService(database, accounts, log);

        try {
            database.createSchema();
            try (Connection connection = database.connect()) {
                System.out.println("== Opening accounts");
                accounts.open(connection, "Asha", new BigDecimal("1000.00"));
                accounts.open(connection, "Bilal", new BigDecimal("250.00"));
                accounts.openMany(connection, List.of("Chitra", "Dev", "Anita"), new BigDecimal("100.00"));
                show(accounts.findAll(connection));

                System.out.println();
                System.out.println("== Search by owner prefix \"A\" (placeholder)");
                show(accounts.findByOwnerPrefix(connection, "A"));
            }

            System.out.println();
            System.out.println("== Transfers");
            attempt(transfers, 1, 2, "300.00");
            attempt(transfers, 2, 3, "5000.00");
            attempt(transfers, 1, 99, "50.00");
            attempt(transfers, 4, 4, "10.00");
            attempt(transfers, 3, 4, "-5.00");

            try (Connection connection = database.connect()) {
                System.out.println();
                System.out.println("== Balances afterwards (the failed transfers changed nothing)");
                show(accounts.findAll(connection));
                BigDecimal total = accounts.findAll(connection).stream()
                        .map(Account::balance).reduce(BigDecimal.ZERO, BigDecimal::add);
                System.out.println("Total money in the bank: " + total.toPlainString());

                System.out.println();
                System.out.println("== Audit log");
                for (AuditLog.Entry entry : log.all(connection)) {
                    System.out.println("  #" + entry.id() + " " + entry.from() + " -> " + entry.to() + "  "
                            + entry.amount().toPlainString() + "  " + entry.status()
                            + (entry.note() == null ? "" : "  (" + entry.note() + ")"));
                }

                System.out.println();
                System.out.println("== Exercise answers");
                System.out.println("A1: accounts holding at least 500.00");
                show(accounts.findByMinBalance(connection, new BigDecimal("500.00")));
                System.out.println("B1: closing accounts");
                close(transfers, 3);
                transfers.transfer(3, 4, new BigDecimal("100.00"));
                close(transfers, 3);
                System.out.println("C1: a batch of two transfers where the second one fails");
                try {
                    transfers.transferAll(List.of(new TransferService.Move(1, 5, new BigDecimal("100.00")),
                            new TransferService.Move(2, 5, new BigDecimal("9999.00"))));
                } catch (BankException e) {
                    System.out.println("  refused: " + e.getMessage());
                }
                System.out.println("  balances of 1 and 5 are unchanged:");
                show(List.of(accounts.find(connection, 1).orElseThrow(), accounts.find(connection, 5).orElseThrow()));
                System.out.println("  now a batch that works:");
                transfers.transferAll(List.of(new TransferService.Move(1, 5, new BigDecimal("100.00")),
                        new TransferService.Move(2, 5, new BigDecimal("50.00"))));
                show(List.of(accounts.find(connection, 1).orElseThrow(), accounts.find(connection, 2).orElseThrow(),
                        accounts.find(connection, 5).orElseThrow()));

                System.out.println();
                System.out.println("== SQL injection");
                String attack = "nobody' OR '1'='1";
                System.out.println("Input text           : " + attack);
                System.out.println("Glued into the SQL   : " + InjectionDemo.unsafeFind(connection, attack).size() + " rows leaked");
                System.out.println("Through a placeholder: " + accounts.findByOwnerPrefix(connection, attack).size() + " rows");
            }
        } finally {
            database.drop();
        }
    }

    private static void close(TransferService transfers, int id) {
        try {
            transfers.closeAccount(id);
            System.out.println("  closed account " + id);
        } catch (BankException e) {
            System.out.println("  refused: " + e.getMessage());
        }
    }

    private static void attempt(TransferService transfers, int from, int to, String amount) {
        String label = "  " + from + " -> " + to + "  " + amount + "  ";
        try {
            transfers.transfer(from, to, new BigDecimal(amount));
            System.out.println(label + "done");
        } catch (BankException e) {
            System.out.println(label + "refused: " + e.getMessage());
        }
    }

    private static void show(List<Account> list) {
        for (Account account : list) {
            System.out.printf("  %d  %-7s %9s%n", account.id(), account.owner(), account.balance().toPlainString());
        }
    }
}
