package com.codechanakya.hr;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.Set;


/** Runs the HR system on an in-memory Derby database. "Today" is Monday 30 June 2025. */
public class Main {

    public static void main(String[] args) {
        Clock clock = Clock.fixed(Instant.parse("2025-06-30T00:00:00Z"), ZoneOffset.UTC);
        WorkCalendar calendar = new WorkCalendar(Set.of(
                LocalDate.of(2025, 8, 15), LocalDate.of(2025, 10, 2), LocalDate.of(2025, 12, 25)));
        ConnectionProvider database = Schema.inMemory("hr");
        try {
            Schema.create(database);
            HrService hr = new HrService(new JdbcEmployeeStore(database), calendar, clock);

            System.out.println("== Hiring");
            Employee asha = hire(hr, "Asha Rao", "asha@example.com", Department.ENGINEERING, "1992-07-04", "2020-02-29", "85000");
            Employee bilal = hire(hr, "Bilal Khan", "bilal@example.com", Department.FINANCE, "1988-02-29", "2023-11-15", "45000");
            Employee chitra = hire(hr, "Chitra Nair", "chitra@example.com", Department.SALES, "1999-07-20", "2025-03-03", "38000");
            Employee dev = hire(hr, "Dev Mehta", "dev@example.com", Department.HR, "2001-07-10", "2025-06-30", "52000");
            hire(hr, "Eva Joseph", "eva-at-example.com", Department.SALES, "1995-01-01", "2024-01-01", "30000");
            hire(hr, "Farid Ali", "farid@example.com", Department.SALES, "2010-05-05", "2025-06-01", "30000");
            hire(hr, "Gita Rao", "gita@example.com", Department.SALES, "1995-01-01", "2025-07-15", "30000");
            hire(hr, "Asha Again", "ASHA@example.com", Department.SALES, "1995-01-01", "2024-01-01", "30000");

            System.out.println();
            System.out.println("== Dates");
            for (Employee e : hr.staff()) {
                Period tenure = hr.tenure(e.id());
                System.out.printf("  %-12s joined %s  tenure %dy %dm %dd  probation ends %s (%s)%n",
                        e.name(), e.joinDate(), tenure.getYears(), tenure.getMonths(), tenure.getDays(),
                        hr.probationEnds(e.id()), hr.probationEnds(e.id()).getDayOfWeek());
            }
            System.out.println("  Birthdays in the next 30 days: " + hr.birthdaysWithin(30).stream()
                    .map(e -> e.name() + " (" + WorkCalendar.nextBirthday(e.birthDate(), LocalDate.of(2025, 6, 30)) + ")").toList());
            System.out.println("  Bilal was born on 29 February; his next birthday is celebrated on "
                    + WorkCalendar.nextBirthday(bilal.birthDate(), LocalDate.of(2025, 6, 30)));
            for (YearMonth month : new YearMonth[]{YearMonth.of(2025, 6), YearMonth.of(2025, 8), YearMonth.of(2025, 12)}) {
                LocalDate payday = hr.payday(month);
                System.out.println("  Payday for " + month + ": " + payday + " (" + payday.getDayOfWeek() + ")");
            }

            System.out.println();
            System.out.println("== Leave");
            Integer ashaLeave = leave(hr, "Asha annual 11 to 15 Aug (15 Aug is a holiday)", asha, LeaveType.ANNUAL, "2025-08-11", "2025-08-15");
            leave(hr, "Asha annual 13 to 14 Aug", asha, LeaveType.ANNUAL, "2025-08-13", "2025-08-14");
            leave(hr, "Asha annual all of September", asha, LeaveType.ANNUAL, "2025-09-01", "2025-09-30");
            leave(hr, "Asha annual Saturday and Sunday", asha, LeaveType.ANNUAL, "2025-07-05", "2025-07-06");
            leave(hr, "Bilal sick from 20 June (past)", bilal, LeaveType.SICK, "2025-06-20", "2025-06-20");
            leave(hr, "Bilal annual 5 July back to 1 July", bilal, LeaveType.ANNUAL, "2025-07-05", "2025-07-01");
            leave(hr, "Bilal annual 30 Dec to 2 Jan", bilal, LeaveType.ANNUAL, "2025-12-30", "2026-01-02");
            Integer chitraLeave = leave(hr, "Chitra unpaid 14 to 18 July", chitra, LeaveType.UNPAID, "2025-07-14", "2025-07-18");
            leave(hr, "Dev annual 21 to 22 July", dev, LeaveType.ANNUAL, "2025-07-21", "2025-07-22");
            System.out.println("  Asha annual days left in 2025 (before approval): " + hr.balance(asha.id(), LeaveType.ANNUAL, 2025));
            run("Approve leave " + ashaLeave, () -> hr.approve(ashaLeave));
            run("Approve leave " + ashaLeave + " again", () -> hr.approve(ashaLeave));
            run("Approve leave " + chitraLeave, () -> hr.approve(chitraLeave));

            System.out.println();
            System.out.println("== Changes");
            run("Raise Bilal to 52000", () -> hr.changeSalary(bilal.id(), new BigDecimal("52000")));
            run("Set Bilal's salary to -1", () -> hr.changeSalary(bilal.id(), new BigDecimal("-1")));
            run("Move Chitra to ENGINEERING", () -> hr.transfer(chitra.id(), Department.ENGINEERING));
            run("Terminate Dev", () -> hr.terminate(dev.id()));
            run("Terminate Dev again", () -> hr.terminate(dev.id()));
            run("Raise Dev after leaving", () -> hr.changeSalary(dev.id(), new BigDecimal("60000")));
            System.out.println("  Dev's leave after leaving: " + hr.leaves(dev.id()).stream().map(LeaveRequest::status).toList());
            System.out.println("  Active in ENGINEERING: " + hr.activeIn(Department.ENGINEERING).stream().map(Employee::name).toList());
            System.out.println("  Active in HR: " + hr.activeIn(Department.HR).stream().map(Employee::name).toList());

            System.out.println();
            System.out.println("== Payslips for July 2025 (23 working days)");
            for (Employee e : hr.staff()) {
                Payslip p = hr.payslip(e.id(), YearMonth.of(2025, 7));
                System.out.printf("  %-12s gross %9s  unpaid days %d  deduction %8s  tax %8s  net %9s%n", e.name(),
                        p.gross().toPlainString(), p.unpaidDays(), p.unpaidDeduction().toPlainString(),
                        p.tax().toPlainString(), p.net().toPlainString());
            }
        } finally {
            Schema.drop("hr");
        }
    }

    private static Employee hire(HrService hr, String name, String email, Department department, String born, String joined, String salary) {
        try {
            Employee e = hr.hire(name, email, department, LocalDate.parse(born), LocalDate.parse(joined), new BigDecimal(salary));
            System.out.println("  Hired " + e.name() + " as employee " + e.id());
            return e;
        } catch (HrException e) {
            System.out.println("  Hiring " + name + " refused: " + e.getMessage());
            return null;
        }
    }

    private static Integer leave(HrService hr, String label, Employee employee, LeaveType type, String from, String to) {
        try {
            LeaveRequest request = hr.requestLeave(employee.id(), type, LocalDate.parse(from), LocalDate.parse(to));
            System.out.println("  " + label + ": request " + request.id() + ", " + request.days() + " working days");
            return request.id();
        } catch (HrException e) {
            System.out.println("  " + label + ": refused, " + e.getMessage());
            return null;
        }
    }

    private static void run(String label, Runnable action) {
        try {
            action.run();
            System.out.println("  " + label + ": done");
        } catch (HrException e) {
            System.out.println("  " + label + ": refused, " + e.getMessage());
        }
    }
}
