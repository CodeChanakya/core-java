package com.codechanakya.employees;

import java.math.BigDecimal;

/**
 * Prepares the monthly payroll for a group of employees.
 *
 * The array is declared as Employee[], yet it holds developers, managers and
 * interns. A subclass object can always be used where its parent is expected.
 * Payroll never checks which kind it is dealing with; each object knows how to
 * work out its own pay.
 */
public class Payroll {

    private final Employee[] staff;

    public Payroll(Employee[] staff) {
        this.staff = staff;
    }

    public BigDecimal total() {
        BigDecimal total = BigDecimal.ZERO.setScale(2);
        for (Employee employee : staff) {
            total = total.add(employee.monthlyPay());
        }
        return total;
    }

    public Employee highestPaid() {
        Employee best = staff[0];
        for (Employee employee : staff) {
            if (employee.monthlyPay().compareTo(best.monthlyPay()) > 0) {
                best = employee;
            }
        }
        return best;
    }

    /** I2: how many employees have this exact role. */
    public int countByRole(String role) {
        int count = 0;
        for (Employee employee : staff) {
            if (employee.role().equals(role)) {
                count++;
            }
        }
        return count;
    }

    public void printReport(String title) {
        System.out.println("=== " + title + " ===");
        System.out.println(String.format("%-6s %-13s %-10s %11s", "ID", "Name", "Role", "Monthly pay"));
        for (Employee employee : staff) {
            System.out.println(employee);
        }
        System.out.println("Total payroll: " + total().toPlainString());
        System.out.println("Highest paid : " + highestPaid().getName());
    }
}
