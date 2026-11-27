package com.codechanakya.orders;

import java.util.concurrent.CompletableFuture;

/** The payment company. Charging takes time, so the answer arrives later, as a future. */
public interface PaymentGateway {

    /** A future of true when the payment was accepted, false when it was declined. It may fail with an exception. */
    CompletableFuture<Boolean> charge(String card);
}
