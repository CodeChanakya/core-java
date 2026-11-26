package com.codechanakya.customerdata;

import java.math.BigDecimal;

/**
 * A rule that turns an amount into a discounted amount.
 *
 * An interface with exactly one abstract method is a "functional interface", and a lambda can
 * stand in for it. The annotation is optional, but it makes the compiler refuse a second
 * abstract method, so nobody breaks every lambda that uses it by accident.
 */
@FunctionalInterface
public interface Discount {

    BigDecimal apply(BigDecimal amount);

    /** A default method is fine: it does not count as the one abstract method. */
    default Discount then(Discount next) {
        return amount -> next.apply(apply(amount));
    }
}
