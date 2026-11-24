package com.codechanakya.payments;

public class ConsoleNotifier implements Notifier {

    @Override
    public void paymentApproved(String reference, String methodName) {
        System.out.println("  [notify] " + reference + " approved on " + methodName);
    }
}
