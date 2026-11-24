package com.codechanakya.bank;

import java.math.BigDecimal;

/**
 * A short day at the bank. Every line printed here is checked against
 * expected-output.txt, so if you change a rule, the output changes too.
 */
public class Main {

    public static void main(String[] args) {
        Bank bank = new Bank("Chanakya Savings Bank");

        Account asha = bank.openAccount("Asha Menon", money("5000.00"));
        Account rahul = bank.openAccount("Rahul Verma", money("1200.50"));
        Account meera = bank.openAccount("Meera Iyer", money("0"));
        Account kiran = bank.openAccount("Kiran Rao", money("25000"));  // B1
        System.out.println("Opened " + bank.accountCount() + " accounts");
        bank.printStatement();

        System.out.println();
        System.out.println("Deposit 750.25 to Asha: " + outcome(asha.deposit(money("750.25"))));
        System.out.println("Deposit -20 to Rahul: " + outcome(rahul.deposit(money("-20"))));
        System.out.println("Withdraw 300 from Rahul: " + outcome(rahul.withdraw(money("300"))));
        System.out.println("Withdraw 5000 from Meera: " + outcome(meera.withdraw(money("5000"))));

        System.out.println();
        System.out.println("Transfer 1000 Asha to Meera: "
                + outcome(bank.transfer("ACC-1001", "ACC-1003", money("1000"))));
        System.out.println("Transfer 9999 Rahul to Asha: "
                + outcome(bank.transfer("ACC-1002", "ACC-1001", money("9999"))));
        System.out.println("Transfer to unknown ACC-9999: "
                + outcome(bank.transfer("ACC-1001", "ACC-9999", money("10"))));

        System.out.println();
        bank.printStatement();

        System.out.println();
        System.out.println("Withdraw 15000 from Kiran: " + outcome(kiran.withdraw(money("15000"))));
        System.out.println("Withdraw 6000 from Kiran (limit): " + outcome(kiran.withdraw(money("6000"))));
        System.out.println("Richest: " + bank.richestAccount().getHolderName());
        System.out.println("Meera empty? " + meera.isEmpty());
        System.out.println(asha.getHolderName() + " has " + asha.getBalance().toPlainString());  // B3
        asha.printHistory();

        System.out.println();
        try {
            bank.openAccount("   ", money("100"));
        } catch (IllegalArgumentException e) {
            System.out.println("Rejected: " + e.getMessage());
        }
    }

    private static BigDecimal money(String text) {
        return new BigDecimal(text);
    }

    private static String outcome(boolean worked) {
        return worked ? "done" : "refused";
    }
}
