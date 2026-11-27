package com.codechanakya.bank;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;


/** Runs the whole bank on an in-memory Derby database and prints what happens. */
public class Main {

    public static void main(String[] args) {
        Clock clock = Clock.fixed(Instant.parse("2025-06-30T00:00:00Z"), ZoneOffset.UTC);
        ConnectionProvider database = Schema.inMemory("enterprise-bank");
        try {
            Schema.create(database);
            BankService bank = new BankService(new JdbcBankStore(database), clock);

            System.out.println("== Opening accounts");
            Account asha = bank.open("Asha Rao", AccountType.SAVINGS, new BigDecimal("12000"));
            Account bilal = bank.open("Bilal Khan", AccountType.CURRENT, new BigDecimal("3000"));
            Account chitra = bank.open("Chitra Nair", AccountType.SAVINGS, new BigDecimal("75000"));
            show(bank);

            System.out.println();
            System.out.println("== Everyday banking");
            attempt("Deposit 2500.50 to Asha", () -> bank.deposit(asha.id(), new BigDecimal("2500.50")));
            attempt("Withdraw 4000 from Bilal", () -> bank.withdraw(bilal.id(), new BigDecimal("4000")));
            attempt("Withdraw 9000 more from Bilal (overdraft)", () -> bank.withdraw(bilal.id(), new BigDecimal("9000")));
            attempt("Withdraw 5000 more from Bilal (past the overdraft)", () -> bank.withdraw(bilal.id(), new BigDecimal("5000")));
            attempt("Withdraw 14100 from Asha (below the minimum)", () -> bank.withdraw(asha.id(), new BigDecimal("14100")));
            attempt("Withdraw 40000 from Chitra", () -> bank.withdraw(chitra.id(), new BigDecimal("40000")));
            attempt("Withdraw 20000 more from Chitra (daily limit)", () -> bank.withdraw(chitra.id(), new BigDecimal("20000")));
            attempt("Deposit -5 to Asha", () -> bank.deposit(asha.id(), new BigDecimal("-5")));
            attempt("Deposit to account 99", () -> bank.deposit(99, new BigDecimal("10")));
            attempt("Open a savings account with 100", () -> bank.open("Dev Mehta", AccountType.SAVINGS, new BigDecimal("100")));

            System.out.println();
            System.out.println("== Transfers");
            attempt("Transfer 5000 from Chitra to Bilal", () -> bank.transfer(chitra.id(), bilal.id(), new BigDecimal("5000")));
            attempt("Transfer 50000 from Asha to Bilal (Asha is too poor)", () -> bank.transfer(asha.id(), bilal.id(), new BigDecimal("50000")));
            attempt("Transfer 100 from Chitra to account 99", () -> bank.transfer(chitra.id(), 99, new BigDecimal("100")));
            attempt("Transfer 100 from Asha to Asha", () -> bank.transfer(asha.id(), asha.id(), new BigDecimal("100")));
            System.out.println("  Total deposits in the bank: " + bank.totalDeposits().toPlainString());

            System.out.println();
            System.out.println("== Month end");
            System.out.println("  Interest paid to " + bank.payMonthlyInterest() + " savings accounts");
            show(bank);

            System.out.println();
            System.out.println("== Statement");
            for (String line : StatementPrinter.format(bank.account(bilal.id()), bank.history(bilal.id()))) {
                System.out.println(line);
            }
            System.out.println();
            for (String line : StatementPrinter.format(bank.account(asha.id()), bank.history(asha.id()))) {
                System.out.println(line);
            }
        } finally {
            Schema.drop("enterprise-bank");
        }
    }

    private static void attempt(String label, Runnable action) {
        try {
            action.run();
            System.out.println("  " + label + ": done");
        } catch (BankException e) {
            System.out.println("  " + label + ": refused, " + e.getMessage());
        }
    }

    private static void show(BankService bank) {
        for (Account a : bank.accounts()) {
            System.out.printf("  %d  %-12s %-8s %12s%n", a.id(), a.customer(), a.type(), a.balance().toPlainString());
        }
    }
}
