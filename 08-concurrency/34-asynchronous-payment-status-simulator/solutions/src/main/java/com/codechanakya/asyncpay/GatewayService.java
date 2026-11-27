package com.codechanakya.asyncpay;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * The outside company that really moves the money.
 *
 * <p>Two payment ids are rigged so we can show the bad days: {@code P-GATE} finds the gateway
 * down, and {@code P-SLOW} gets an answer only after one and a half seconds.
 */
public final class GatewayService {

    private final Executor executor;

    public GatewayService(Executor executor) {
        this.executor = executor;
    }

    /**
     * @return a future holding the authorisation code. It completes <em>exceptionally</em> with
     * {@link GatewayUnavailableException} when the gateway is down.
     */
    public CompletableFuture<String> authorize(PaymentRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            if (request.id().equals("P-SLOW")) {
                Latency.pause(1500);
            } else {
                Latency.pause(40);
            }
            if (request.id().equals("P-GATE")) {
                throw new GatewayUnavailableException("Gateway refused the connection");
            }
            return "AUTH-" + request.id();
        }, executor);
    }
}
