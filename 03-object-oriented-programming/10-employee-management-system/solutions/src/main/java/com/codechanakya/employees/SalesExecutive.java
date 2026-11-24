package com.codechanakya.employees;

import java.math.BigDecimal;
import java.math.RoundingMode;

/** I1: base salary plus 2 percent commission on this month's sales. */
public class SalesExecutive extends Employee {

    private static final BigDecimal COMMISSION_RATE = new BigDecimal("0.02");

    private final BigDecimal monthlySales;

    public SalesExecutive(String id, String name, BigDecimal baseSalary, BigDecimal monthlySales) {
        super(id, name, baseSalary);
        this.monthlySales = monthlySales;
    }

    @Override
    public BigDecimal monthlyPay() {
        BigDecimal commission = monthlySales.multiply(COMMISSION_RATE).setScale(2, RoundingMode.HALF_UP);
        return super.monthlyPay().add(commission);
    }

    @Override
    public String role() {
        return "Sales";
    }
}
