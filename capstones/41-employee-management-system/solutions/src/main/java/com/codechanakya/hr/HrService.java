package com.codechanakya.hr;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * The rules of the HR system: who may be hired, who may take leave, what is paid.
 * Today's date always comes from the injected {@link Clock}.
 */
public class HrService {

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    static final BigDecimal TAX_THRESHOLD = new BigDecimal("50000.00");
    static final BigDecimal LOW_TAX_RATE = new BigDecimal("0.05");
    static final BigDecimal HIGH_TAX_RATE = new BigDecimal("0.10");

    private final EmployeeStore store;
    private final WorkCalendar calendar;
    private final Clock clock;

    public HrService(EmployeeStore store, WorkCalendar calendar, Clock clock) {
        this.store = store;
        this.calendar = calendar;
        this.clock = clock;
    }

    public Employee hire(String name, String email, Department department, LocalDate birthDate, LocalDate joinDate,
                         BigDecimal monthlySalary) {
        if (name == null || name.isBlank()) {
            throw new HrException("A name is required.");
        }
        if (email == null || !EMAIL.matcher(email.trim()).matches()) {
            throw new HrException("The email address '" + email + "' is not valid.");
        }
        if (monthlySalary.signum() <= 0) {
            throw new HrException("The salary must be positive.");
        }
        if (joinDate.isAfter(today())) {
            throw new HrException("The joining date " + joinDate + " is in the future.");
        }
        int ageAtJoining = Period.between(birthDate, joinDate).getYears();
        if (ageAtJoining < 18) {
            throw new HrException("An employee must be at least 18 on the joining date, but would be " + ageAtJoining + ".");
        }
        return store.add(new Employee(0, name.trim(), email.trim().toLowerCase(), department, birthDate, joinDate,
                monthlySalary, true));
    }

    public Employee employee(int id) {
        return store.find(id).orElseThrow(() -> new HrException("No employee with id " + id));
    }

    public List<Employee> staff() {
        return store.findAll();
    }

    public List<Employee> activeIn(Department department) {
        return store.findByDepartment(department).stream().filter(Employee::active).toList();
    }

    public Employee changeSalary(int id, BigDecimal newSalary) {
        if (newSalary.signum() <= 0) {
            throw new HrException("The salary must be positive.");
        }
        Employee updated = requireActive(id).withSalary(newSalary);
        store.update(updated);
        return updated;
    }

    public Employee transfer(int id, Department department) {
        Employee updated = requireActive(id).withDepartment(department);
        store.update(updated);
        return updated;
    }

    /** An employee who leaves is kept in the records but marked inactive; their pending leave is rejected. */
    public void terminate(int id) {
        Employee employee = requireActive(id);
        store.update(employee.deactivated());
        for (LeaveRequest leave : store.leavesOf(id)) {
            if (leave.status() == LeaveStatus.PENDING) {
                store.updateLeaveStatus(leave.id(), LeaveStatus.REJECTED);
            }
        }
    }

    /** How long someone has worked here, as years, months and days. For someone who left, it is still measured to today. */
    public Period tenure(int id) {
        return Period.between(employee(id).joinDate(), today());
    }

    public LocalDate probationEnds(int id) {
        return calendar.probationEnds(employee(id).joinDate());
    }

    /** Active employees whose birthday falls in the next {@code days} days (today included), soonest first. */
    public List<Employee> birthdaysWithin(int days) {
        LocalDate today = today();
        LocalDate until = today.plusDays(days);
        return store.findAll().stream()
                .filter(Employee::active)
                .filter(e -> !WorkCalendar.nextBirthday(e.birthDate(), today).isAfter(until))
                .sorted(Comparator.comparing((Employee e) -> WorkCalendar.nextBirthday(e.birthDate(), today))
                        .thenComparing(Employee::id))
                .toList();
    }

    /** Exercise 2: active employees whose probation ends within the next {@code days} days, today included, soonest first. */
    public List<Employee> probationEndingWithin(int days) {
        LocalDate today = today();
        LocalDate until = today.plusDays(days);
        return store.findAll().stream()
                .filter(Employee::active)
                .filter(e -> {
                    LocalDate ends = calendar.probationEnds(e.joinDate());
                    return !ends.isBefore(today) && !ends.isAfter(until);
                })
                .sorted(Comparator.comparing((Employee e) -> calendar.probationEnds(e.joinDate())).thenComparing(Employee::id))
                .toList();
    }

    /**
     * Exercise 3: active employees with a work anniversary in the given month (joined in an earlier year), by day of
     * month then id. Someone who joined on 29 February has their anniversary on 28 February in a year that has none.
     */
    public List<Employee> anniversariesIn(YearMonth month) {
        return store.findAll().stream()
                .filter(Employee::active)
                .filter(e -> e.joinDate().getYear() < month.getYear())
                .filter(e -> e.joinDate().getMonth() == month.getMonth())
                .sorted(Comparator.comparingInt((Employee e) -> e.joinDate().withYear(month.getYear()).getDayOfMonth())
                        .thenComparing(Employee::id))
                .toList();
    }

    public LeaveRequest requestLeave(int employeeId, LeaveType type, LocalDate from, LocalDate to) {
        requireActive(employeeId);
        if (to.isBefore(from)) {
            throw new HrException("The leave ends (" + to + ") before it starts (" + from + ").");
        }
        if (from.getYear() != to.getYear()) {
            throw new HrException("Leave cannot run across two years. Please make two requests.");
        }
        if (from.isBefore(today())) {
            throw new HrException("Leave cannot start in the past (" + from + ").");
        }
        int days = calendar.workingDaysBetween(from, to);
        if (days == 0) {
            throw new HrException("That range has no working days.");
        }
        for (LeaveRequest other : store.leavesOf(employeeId)) {
            if (other.status() != LeaveStatus.REJECTED && other.overlaps(from, to)) {
                throw new HrException("That overlaps leave request " + other.id() + " (" + other.from() + " to " + other.to() + ").");
            }
        }
        if (type.hasLimit()) {
            int left = balance(employeeId, type, from.getYear());
            if (days > left) {
                throw new HrException("Only " + left + " " + type + " days left in " + from.getYear() + ", but " + days + " requested.");
            }
        }
        return store.addLeave(employeeId, type, from, to, days);
    }

    public void approve(int leaveId) {
        decide(leaveId, LeaveStatus.APPROVED);
    }

    public void reject(int leaveId) {
        decide(leaveId, LeaveStatus.REJECTED);
    }

    /** Days still available for a limited leave type in a year. Pending and approved requests both count as used. */
    public int balance(int employeeId, LeaveType type, int year) {
        int used = store.leavesOf(employeeId).stream()
                .filter(l -> l.type() == type && l.status() != LeaveStatus.REJECTED && l.from().getYear() == year)
                .mapToInt(LeaveRequest::days)
                .sum();
        return type.yearlyAllowance() - used;
    }

    public List<LeaveRequest> leaves(int employeeId) {
        return store.leavesOf(employeeId);
    }

    /** The date the month's salary is paid: the last working day. */
    public LocalDate payday(YearMonth month) {
        return calendar.lastWorkingDay(month);
    }

    /**
     * The payslip for one month. Each approved unpaid working day takes off one working-day share of the salary.
     * Tax is 5 percent of what is left when the gross salary is 50,000 or less, and 10 percent when it is more.
     */
    public Payslip payslip(int employeeId, YearMonth month) {
        Employee employee = employee(employeeId);
        int unpaid = 0;
        for (LeaveRequest leave : store.leavesOf(employeeId)) {
            if (leave.type() == LeaveType.UNPAID && leave.status() == LeaveStatus.APPROVED) {
                LocalDate from = leave.from().isBefore(month.atDay(1)) ? month.atDay(1) : leave.from();
                LocalDate to = leave.to().isAfter(month.atEndOfMonth()) ? month.atEndOfMonth() : leave.to();
                unpaid += calendar.workingDaysBetween(from, to);
            }
        }
        BigDecimal gross = employee.monthlySalary();
        BigDecimal deduction = gross.multiply(BigDecimal.valueOf(unpaid))
                .divide(BigDecimal.valueOf(calendar.workingDaysIn(month)), 2, RoundingMode.HALF_UP);
        BigDecimal taxable = gross.subtract(deduction);
        BigDecimal rate = gross.compareTo(TAX_THRESHOLD) <= 0 ? LOW_TAX_RATE : HIGH_TAX_RATE;
        BigDecimal tax = taxable.multiply(rate).setScale(2, RoundingMode.HALF_UP);
        return new Payslip(employeeId, month, gross, unpaid, deduction, tax, taxable.subtract(tax));
    }

    private void decide(int leaveId, LeaveStatus decision) {
        LeaveRequest leave = store.findLeave(leaveId).orElseThrow(() -> new HrException("No leave request with id " + leaveId));
        if (leave.status() != LeaveStatus.PENDING) {
            throw new HrException("Leave request " + leaveId + " is already " + leave.status() + ".");
        }
        store.updateLeaveStatus(leaveId, decision);
    }

    private Employee requireActive(int id) {
        Employee employee = employee(id);
        if (!employee.active()) {
            throw new HrException(employee.name() + " no longer works here.");
        }
        return employee;
    }

    private LocalDate today() {
        return LocalDate.now(clock);
    }
}
