package com.codechanakya.payments;

/**
 * The outcome of one charge: approved or declined, a reference and a message.
 *
 * All fields are final and there are no setters, so a result cannot be edited
 * after it is created. The two static methods are named for what they mean,
 * which reads better than a constructor with a mysterious true or false.
 */
public class PaymentResult {

    private final boolean approved;
    private final String reference;
    private final String message;

    private PaymentResult(boolean approved, String reference, String message) {
        this.approved = approved;
        this.reference = reference;
        this.message = message;
    }

    public static PaymentResult approved(String reference) {
        return new PaymentResult(true, reference, "Approved");
    }

    public static PaymentResult declined(String reason) {
        return new PaymentResult(false, "-", reason);
    }

    public boolean isApproved() {
        return approved;
    }

    public String getReference() {
        return reference;
    }

    public String getMessage() {
        return message;
    }
}
