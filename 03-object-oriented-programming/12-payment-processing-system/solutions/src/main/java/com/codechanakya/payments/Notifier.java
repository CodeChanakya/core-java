package com.codechanakya.payments;

/** C1: a third part the processor can be given. */
public interface Notifier {

    void paymentApproved(String reference, String methodName);
}
