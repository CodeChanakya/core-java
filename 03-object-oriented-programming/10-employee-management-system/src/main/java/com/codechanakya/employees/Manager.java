package com.codechanakya.employees;

import java.math.BigDecimal;

/** A manager earns the base salary plus a fixed bonus for every person in the team. */
public class Manager extends Employee {

    private static final BigDecimal BONUS_PER_REPORT = new BigDecimal("1500.00");

    private final int teamSize;

    public Manager(String id, String name, BigDecimal baseSalary, int teamSize) {
        super(id, name, baseSalary);
        this.teamSize = teamSize;
    }

    @Override
    public BigDecimal monthlyPay() {
        return super.monthlyPay().add(BONUS_PER_REPORT.multiply(BigDecimal.valueOf(teamSize)));
    }

    @Override
    public String role() {
        return "Manager";
    }

    /** Only managers have a team, so only the Manager class offers this method. */
    public int getTeamSize() {
        return teamSize;
    }
}
