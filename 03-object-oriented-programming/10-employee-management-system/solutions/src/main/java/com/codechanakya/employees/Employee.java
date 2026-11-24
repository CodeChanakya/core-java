package com.codechanakya.employees;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * The general employee: an id, a name and a base salary.
 *
 * Every other kind of employee in this program extends this class, so
 * everything written here is inherited for free. A subclass only writes what
 * is different about it, usually by overriding monthlyPay() or role().
 */
public class Employee {

    private static final BigDecimal HUNDRED = new BigDecimal("100");

    private final String id;
    private final String name;
    private BigDecimal baseSalary;

    public Employee(String id, String name, BigDecimal baseSalary) {
        this.id = id;
        this.name = name;
        this.baseSalary = baseSalary.setScale(2, RoundingMode.HALF_UP);
    }

    /** What this person is paid for a month. Subclasses add to it or replace it. */
    public BigDecimal monthlyPay() {
        return baseSalary;
    }

    /** A short job title for reports. Subclasses override it. */
    public String role() {
        return "Employee";
    }

    /** Raises the base salary by a percentage, for example 10 for ten percent. */
    public void applyRaise(BigDecimal percent) {
        BigDecimal factor = BigDecimal.ONE.add(percent.divide(HUNDRED, 4, RoundingMode.HALF_UP));
        baseSalary = baseSalary.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    /** B2: twelve months of pay. Contractors and managers get it for free through inheritance. */
    public BigDecimal annualPay() {
        return monthlyPay().multiply(BigDecimal.valueOf(12));
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBaseSalary() {
        return baseSalary;
    }

    /**
     * One report line. Notice that role() and monthlyPay() are called without
     * knowing which subclass this object really is. Java picks the right
     * version when the program runs, which is polymorphism.
     */
    @Override
    public String toString() {
        return String.format("%-6s %-13s %-10s %11s", id, name, role(), monthlyPay().toPlainString());
    }
}
