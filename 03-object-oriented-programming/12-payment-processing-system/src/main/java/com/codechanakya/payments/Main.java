package com.codechanakya.payments;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        CardPayment card = new CardPayment("4242", money("50000"));
        UpiPayment upi = new UpiPayment("asha@examplebank");
        WalletPayment wallet = new WalletPayment("Rahul", money("300"));

        // One processor, started with the card and a 2 percent fee.
        PaymentProcessor processor = new PaymentProcessor(card, new PercentageFee(money("2")));
        show("Card, 12000", processor.pay(money("12000")));
        show("Card, 45000", processor.pay(money("45000")));
        show("Card, -50", processor.pay(money("-50")));

        // Same processor, new parts. Nothing inside PaymentProcessor changed.
        processor.useMethod(upi);
        processor.useFeePolicy(new FlatFee(money("0")));
        show("UPI, 2500", processor.pay(money("2500")));
        show("UPI, 150000", processor.pay(money("150000")));

        processor.useMethod(wallet);
        processor.useFeePolicy(new FlatFee(money("5")));
        show("Wallet, 250", processor.pay(money("250")));
        show("Wallet, 100", processor.pay(money("100")));

        System.out.println();
        processor.printLedger();

        System.out.println();
        System.out.println("Card limit left : " + card.getRemainingLimit().toPlainString());
        System.out.println("Wallet balance  : " + wallet.getBalance().toPlainString());
    }

    private static void show(String label, PaymentResult result) {
        System.out.println(label + " -> " + (result.isApproved() ? "approved " : "declined ")
                + result.getReference() + " | " + result.getMessage());
    }

    private static BigDecimal money(String text) {
        return new BigDecimal(text);
    }
}
