package com.codechanakya.employees;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        Employee[] staff = {
            new Developer("E101", "Asha Menon", new BigDecimal("70000"), "Java", true),
            new Developer("E102", "Dev Patel", new BigDecimal("65000"), "Python", false),
            new Manager("E201", "Rahul Verma", new BigDecimal("90000"), 6),
            new Intern("E301", "Kiran Rao", new BigDecimal("15000")),
            new Developer("E103", "Meera Iyer", new BigDecimal("72000"), "Kotlin", false),   // B1
            new SalesExecutive("E401", "Dev Sharma", new BigDecimal("40000"), new BigDecimal("500000")),
            new Contractor("E501", "Isha Nair", new BigDecimal("1200"), 80),
        };
        Payroll payroll = new Payroll(staff);

        payroll.printReport("Payroll before the raise");

        System.out.println();
        System.out.println("Applying a 10 percent raise to everyone...");
        for (Employee employee : staff) {
            employee.applyRaise(new BigDecimal("10"));
        }
        System.out.println();
        payroll.printReport("Payroll after the raise");

        System.out.println("Developers: " + payroll.countByRole("Developer"));
        System.out.println("Asha annual pay: " + staff[0].annualPay().toPlainString());

        // A variable of type Employee only offers Employee's methods. To use a
        // Manager-only method we first ask whether this object really is one.
        System.out.println();
        for (Employee employee : staff) {
            if (employee instanceof Manager manager) {
                System.out.println(manager.getName() + " manages a team of " + manager.getTeamSize());
            }
        }
    }
}
