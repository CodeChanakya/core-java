package com.codechanakya.shop;

import java.math.BigDecimal;

/** A predictable gateway: it declines the card 0000 and any amount over 100,000. Everything else is accepted. */
public class FakeGateway implements PaymentGateway {

    @Override
    public boolean charge(String card, BigDecimal amount) {
        return !"0000".equals(card) && amount.compareTo(new BigDecimal("100000")) <= 0;
    }
}
