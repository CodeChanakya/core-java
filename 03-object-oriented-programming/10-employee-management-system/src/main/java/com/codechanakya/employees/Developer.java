package com.codechanakya.employees;

import java.math.BigDecimal;

/** A developer earns the base salary, plus an allowance in months they are on call. */
public class Developer extends Employee {

    private static final BigDecimal ON_CALL_ALLOWANCE = new BigDecimal("4000.00");

    private final String primaryLanguage;
    private final boolean onCall;

    public Developer(String id, String name, BigDecimal baseSalary, String primaryLanguage, boolean onCall) {
        super(id, name, baseSalary);   // the parent builds id, name and salary
        this.primaryLanguage = primaryLanguage;
        this.onCall = onCall;
    }

    @Override
    public BigDecimal monthlyPay() {
        BigDecimal pay = super.monthlyPay();   // reuse the parent's rule, then add to it
        return onCall ? pay.add(ON_CALL_ALLOWANCE) : pay;
    }

    @Override
    public String role() {
        return "Developer";
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }
}
