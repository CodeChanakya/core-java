package com.codechanakya.shop;

import java.math.BigDecimal;

/** The outside world's payment company, hidden behind an interface so tests never call a real one. */
public interface PaymentGateway {

    /** Returns true when the payment was accepted. */
    boolean charge(String card, BigDecimal amount);
}
