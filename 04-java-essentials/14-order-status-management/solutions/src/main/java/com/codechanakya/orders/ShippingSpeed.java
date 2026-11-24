package com.codechanakya.orders;

import java.math.BigDecimal;

/**
 * C1: an enum whose constants each carry their own behaviour.
 *
 * cost() is abstract, so every constant must supply a body for it. This is the
 * enum answer to a chain of if statements on the type.
 */
public enum ShippingSpeed {

    STANDARD {
        @Override
        public BigDecimal cost(int kilograms) {
            return new BigDecimal("40.00");
        }
    },
    EXPRESS {
        @Override
        public BigDecimal cost(int kilograms) {
            return new BigDecimal("90.00").add(BigDecimal.valueOf(kilograms * 10L));
        }
    },
    SAME_DAY {
        @Override
        public BigDecimal cost(int kilograms) {
            return new BigDecimal("200.00").add(BigDecimal.valueOf(kilograms * 25L));
        }
    };

    public abstract BigDecimal cost(int kilograms);
}
