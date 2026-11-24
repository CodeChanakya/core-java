package com.codechanakya.employees;

import java.math.BigDecimal;

/**
 * C1: paid by the hour and outside the raise cycle. Payroll is untouched,
 * which is the whole point: a new kind of employee needs a new class, not
 * changes to the code that already works.
 */
public class Contractor extends Employee {

    private final BigDecimal hourlyRate;
    private final int hoursThisMonth;

    public Contractor(String id, String name, BigDecimal hourlyRate, int hoursThisMonth) {
        super(id, name, BigDecimal.ZERO);
        this.hourlyRate = hourlyRate;
        this.hoursThisMonth = hoursThisMonth;
    }

    @Override
    public BigDecimal monthlyPay() {
        return hourlyRate.multiply(BigDecimal.valueOf(hoursThisMonth)).setScale(2);
    }

    @Override
    public void applyRaise(BigDecimal percent) {
        // Contractors renegotiate their rate; they do not get a staff raise.
    }

    @Override
    public String role() {
        return "Contractor";
    }
}
