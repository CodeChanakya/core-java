package com.codechanakya.quotes;

import java.math.BigDecimal;
import java.util.List;

/**
 * A finished quote.
 *
 * @param lines        the priced rows
 * @param subtotal     the sum of price times quantity, before any discount
 * @param discountRate the discount as a fraction (0.10 is ten percent)
 * @param discount     the discount in money
 */
public record Quote(List<QuoteLine> lines, BigDecimal subtotal, BigDecimal discountRate, BigDecimal discount) {

    public BigDecimal tax() {
        return lines.stream().map(QuoteLine::tax).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal total() {
        return lines.stream().map(QuoteLine::gross).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
