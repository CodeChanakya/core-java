package com.codechanakya.empdb;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Headcount and pay figures for one department.
 *
 * @param headcount how many people work there
 * @param payroll   the sum of their salaries
 * @param highest   the largest single salary
 */
public record DepartmentStats(int headcount, BigDecimal payroll, BigDecimal highest) {

    public BigDecimal average() {
        return payroll.divide(BigDecimal.valueOf(headcount), 2, RoundingMode.HALF_UP);
    }
}
