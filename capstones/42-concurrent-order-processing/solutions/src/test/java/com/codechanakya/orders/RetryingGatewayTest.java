package com.codechanakya.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RetryingGatewayTest {

    /** Fails the first {@code failures} times, then accepts. Counts its calls. */
    private static class Flaky implements PaymentGateway {
        final AtomicInteger calls = new AtomicInteger();
        final int failures;

        Flaky(int failures) {
            this.failures = failures;
        }

        @Override
        public CompletableFuture<Boolean> charge(String card) {
            return calls.incrementAndGet() <= failures
                    ? CompletableFuture.failedFuture(new IllegalStateException("flaky"))
                    : CompletableFuture.completedFuture(true);
        }
    }

    @Test
    @DisplayName("one failure is retried and then succeeds")
    void retriesOnce() throws Exception {
        Flaky flaky = new Flaky(1);
        assertEquals(true, new RetryingGateway(flaky).charge("4111").get(5, TimeUnit.SECONDS));
        assertEquals(2, flaky.calls.get());
    }

    @Test
    @DisplayName("two failures in a row still fail, and there are only two attempts")
    void givesUpAfterOneRetry() {
        Flaky flaky = new Flaky(5);
        assertThrows(ExecutionException.class, () -> new RetryingGateway(flaky).charge("4111").get(5, TimeUnit.SECONDS));
        assertEquals(2, flaky.calls.get());
    }

    @Test
    @DisplayName("a success is not retried")
    void successNotRetried() throws Exception {
        Flaky flaky = new Flaky(0);
        new RetryingGateway(flaky).charge("4111").get(5, TimeUnit.SECONDS);
        assertEquals(1, flaky.calls.get());
    }

    @Test
    @DisplayName("a decline is an answer, so it is not retried")
    void declineNotRetried() throws Exception {
        AtomicInteger calls = new AtomicInteger();
        PaymentGateway declining = card -> {
            calls.incrementAndGet();
            return CompletableFuture.completedFuture(false);
        };
        assertEquals(false, new RetryingGateway(declining).charge("0000").get(5, TimeUnit.SECONDS));
        assertEquals(1, calls.get());
    }

    @Test
    @DisplayName("exercise 3: the summary counts every status, zeros included")
    void summary() {
        List<OrderResult> results = List.of(new OrderResult(1, OrderStatus.COMPLETED, ""),
                new OrderResult(2, OrderStatus.COMPLETED, ""), new OrderResult(3, OrderStatus.DECLINED, ""));
        assertEquals("{COMPLETED=2, OUT_OF_STOCK=0, DECLINED=1, TIMED_OUT=0, FAILED=0}", Summary.countByStatus(results).toString());
    }
}
