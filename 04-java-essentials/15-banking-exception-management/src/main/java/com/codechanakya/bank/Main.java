package com.codechanakya.bank;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        Bank bank = new Bank();
        Account asha = bank.openAccount("Asha Menon", money("1000.00"));
        Account rahul = bank.openAccount("Rahul Verma", money("200.00"));

        System.out.println("1. Catching a checked exception");
        try {
            asha.withdraw(money("250"));
            System.out.println("   Withdrew 250, balance " + asha.getBalance().toPlainString());
            asha.withdraw(money("5000"));
            System.out.println("   This line is skipped: the exception jumps past it");
        } catch (InsufficientFundsException e) {
            System.out.println("   Refused: " + e.getMessage());
            System.out.println("   Short by " + e.getShortfall().toPlainString());
        }

        System.out.println();
        System.out.println("2. An unchecked exception from bad input");
        try {
            rahul.deposit(money("-5"));
        } catch (InvalidAmountException e) {
            System.out.println("   Refused: " + e.getMessage());
        }

        System.out.println();
        System.out.println("3. Looking up an account that does not exist");
        try {
            bank.find("ACC-9999");
        } catch (AccountNotFoundException e) {
            System.out.println("   " + e.getMessage());
        }

        System.out.println();
        System.out.println("4. A chained exception: the wrapper and its cause");
        try {
            bank.transfer("ACC-1001", "ACC-1002", money("100"));
            System.out.println("   Transfer of 100 done");
            bank.transfer("ACC-1002", "ACC-1001", money("9000"));
        } catch (TransferFailedException e) {
            System.out.println("   " + e.getMessage());
            System.out.println("   Cause: " + e.getCause().getMessage());
        }
        try {
            bank.transfer("ACC-1001", "ACC-7777", money("10"));
        } catch (TransferFailedException e) {
            System.out.println("   " + e.getMessage());
            System.out.println("   Cause: " + e.getCause().getMessage());
        }

        System.out.println();
        System.out.println("5. try-with-resources and finally");
        try (AuditLog log = new AuditLog("end-of-day")) {
            log.record("Asha balance " + asha.getBalance().toPlainString());
            log.record("Rahul balance " + rahul.getBalance().toPlainString());
            rahul.withdraw(money("1000"));
            log.record("This entry is never written");
        } catch (InsufficientFundsException e) {
            System.out.println("   Stopped part-way: " + e.getMessage());
        } finally {
            System.out.println("   finally runs every time, with or without an error");
        }
    }

    private static BigDecimal money(String text) {
        return new BigDecimal(text);
    }
}
