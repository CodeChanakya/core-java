package com.codechanakya.orders;

import java.util.concurrent.CompletableFuture;

/**
 * Exercise 2: wraps another gateway and tries once more when the first attempt fails with an exception.
 * A decline (false) is an answer, not a failure, so it is never retried.
 */
public class RetryingGateway implements PaymentGateway {

    private final PaymentGateway inner;

    public RetryingGateway(PaymentGateway inner) {
        this.inner = inner;
    }

    @Override
    public CompletableFuture<Boolean> charge(String card) {
        return inner.charge(card)
                .handle((accepted, error) -> error == null
                        ? CompletableFuture.completedFuture(accepted)
                        : inner.charge(card))
                .thenCompose(next -> next);
    }
}
