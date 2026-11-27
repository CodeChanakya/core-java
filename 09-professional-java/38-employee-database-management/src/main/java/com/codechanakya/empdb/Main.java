package com.codechanakya.empdb;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The composition root: the one place that decides which implementations are used and plugs them together.
 * Everything else in the program asks for an interface and does not care what is behind it.
 */
public class Main {

    public static void main(String[] args) {
        // A clock frozen on one day, so "is this hire date in the future?" gives the same answer on every run.
        Clock clock = Clock.fixed(Instant.parse("2025-06-30T00:00:00Z"), ZoneOffset.UTC);

        ConnectionProvider database = Schema.inMemory("employees");
        try {
            Schema.create(database);
            EmployeeService service = new EmployeeService(new JdbcEmployeeRepository(database), clock);

            System.out.println("== Hiring");
            service.hire("Asha Rao", "asha@example.com", Department.ENGINEERING, new BigDecimal("90000"), LocalDate.of(2021, 4, 12));
            service.hire("Bilal Khan", "bilal@example.com", Department.QA, new BigDecimal("60000"), LocalDate.of(2022, 1, 3));
            service.hire("Chitra Nair", "chitra@example.com", Department.QA, new BigDecimal("72000.50"), LocalDate.of(2020, 9, 21));
            service.hire("Dev Mehta", "dev@example.com", Department.SALES, new BigDecimal("48000"), LocalDate.of(2023, 7, 17));
            service.hire("Esha Iyer", "esha@example.com", Department.HR, new BigDecimal("55000"), LocalDate.of(2019, 11, 4));
            service.hire("Farhan Ali", "farhan@example.com", Department.ENGINEERING, new BigDecimal("84000"), LocalDate.of(2024, 2, 26));
            print(service.everyone());

            System.out.println();
            System.out.println("== Rules that say no");
            refuse("Duplicate e-mail", () -> service.hire("Asha Two", "ASHA@example.com", Department.HR, new BigDecimal("50000"), LocalDate.of(2024, 1, 1)));
            refuse("Low salary", () -> service.hire("Gita", "gita@example.com", Department.HR, new BigDecimal("9000"), LocalDate.of(2024, 1, 1)));
            refuse("Bad e-mail", () -> service.hire("Hari", "hari-at-example", Department.HR, new BigDecimal("50000"), LocalDate.of(2024, 1, 1)));
            refuse("Future hire date", () -> service.hire("Ira", "ira@example.com", Department.HR, new BigDecimal("50000"), LocalDate.of(2026, 1, 1)));
            refuse("Raise too big", () -> service.giveRaise(1, new BigDecimal("35")));
            refuse("Unknown employee", () -> service.get(99));

            System.out.println();
            System.out.println("== Changes");
            System.out.println("  Raise 10% for #2    : " + service.giveRaise(2, new BigDecimal("10")).salary().toPlainString());
            System.out.println("  Move #4 to ENGINEERING: " + service.moveTo(4, Department.ENGINEERING).department());
            service.terminate(5);
            System.out.println("  Terminated #5, headcount now " + service.everyone().size());

            System.out.println();
            System.out.println("== Engineering team");
            print(service.inDepartment(Department.ENGINEERING));

            System.out.println();
            System.out.println("== Department report");
            for (Map.Entry<Department, DepartmentStats> entry : service.departmentReport().entrySet()) {
                DepartmentStats stats = entry.getValue();
                System.out.printf("  %-12s people %d  payroll %10s  average %9s  highest %9s%n", entry.getKey(),
                        stats.headcount(), stats.payroll().toPlainString(), stats.average().toPlainString(), stats.highest().toPlainString());
            }
            System.out.println("  Total payroll: " + service.totalPayroll().toPlainString());
        } finally {
            Schema.drop("employees");
        }
    }

    private static void refuse(String label, Supplier<Employee> action) {
        try {
            action.get();
            System.out.println("  " + label + ": unexpectedly allowed");
        } catch (EmployeeException e) {
            System.out.println("  " + label + ": " + e.getMessage());
        }
    }

    private static void print(List<Employee> people) {
        for (Employee e : people) {
            System.out.printf("  #%d %-12s %-20s %-12s %10s  hired %s%n", e.id(), e.name(), e.email(), e.department(),
                    e.salary().toPlainString(), e.hired());
        }
    }
}
