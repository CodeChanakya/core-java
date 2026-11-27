package com.codechanakya.asyncpay;

import java.util.List;

/**
 * The final word on a payment, with the steps it went through.
 *
 * @param id     the payment's reference
 * @param status how it ended
 * @param detail a sentence that says why
 * @param trail  the steps, in order
 */
public record PaymentResult(String id, Status status, String detail, List<String> trail) {

    /** The trail as one line, such as {@code PENDING -> VALIDATED -> CHECKED}. */
    public String trailText() {
        return String.join(" -> ", trail);
    }
}
