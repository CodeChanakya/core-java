package com.codechanakya.payments;

/** The default: says nothing. It lets the processor always call a notifier without checking for null. */
public class SilentNotifier implements Notifier {

    @Override
    public void paymentApproved(String reference, String methodName) {
        // intentionally empty
    }
}
