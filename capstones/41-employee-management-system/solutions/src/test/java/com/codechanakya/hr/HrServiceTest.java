package com.codechanakya.hr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class HrServiceTest {

    private HrFixture fixture;
    private HrService hr;

    @BeforeEach
    void setUp() {
        fixture = new HrFixture();
        hr = fixture.hr;
    }

    @AfterEach
    void tearDown() {
        fixture.close();
    }

    private static LocalDate d(String text) {
        return LocalDate.parse(text);
    }

    private String refusal(Runnable action) {
        return assertThrows(HrException.class, action::run).getMessage();
    }

    @Nested
    @DisplayName("hiring")
    class Hiring {

        @Test
        void emailIsTrimmedAndLowerCased() {
            Employee e = hr.hire("  Asha  ", " ASHA@Example.com ", Department.HR, d("1990-01-01"), d("2024-01-01"), new BigDecimal("100"));
            assertEquals("Asha", e.name());
            assertEquals("asha@example.com", e.email());
        }

        @ParameterizedTest(name = "email \"{0}\" is refused")
        @CsvSource({"plainaddress", "no-at.example.com", "two@@example.com", "a@b", "'a b@example.com'"})
        void badEmails(String email) {
            assertTrue(refusal(() -> hr.hire("A", email, Department.HR, d("1990-01-01"), d("2024-01-01"), BigDecimal.TEN)).contains("not valid"));
        }

        @Test
        void otherRules() {
            assertEquals("A name is required.", refusal(() -> hr.hire(" ", "a@b.com", Department.HR, d("1990-01-01"), d("2024-01-01"), BigDecimal.TEN)));
            assertEquals("The salary must be positive.", refusal(() -> hr.hire("A", "a@b.com", Department.HR, d("1990-01-01"), d("2024-01-01"), BigDecimal.ZERO)));
            assertEquals("The joining date 2025-07-01 is in the future.", refusal(() -> hr.hire("A", "a@b.com", Department.HR, d("1990-01-01"), d("2025-07-01"), BigDecimal.TEN)));
        }

        @Test
        @DisplayName("the 18th birthday on the joining day is allowed, the day before is not")
        void ageBoundary() {
            hr.hire("Old enough", "ok@b.com", Department.HR, d("2007-06-01"), d("2025-06-01"), BigDecimal.TEN);
            assertEquals("An employee must be at least 18 on the joining date, but would be 17.",
                    refusal(() -> hr.hire("Too young", "no@b.com", Department.HR, d("2007-06-02"), d("2025-06-01"), BigDecimal.TEN)));
        }

        @Test
        void duplicateEmailIgnoresCase() {
            fixture.hire("Asha Rao", "100");
            assertThrows(DuplicateEmailException.class, () -> hr.hire("Other", "ASHA.RAO@example.com", Department.HR,
                    d("1990-01-01"), d("2024-01-01"), BigDecimal.TEN));
        }
    }

    @Nested
    @DisplayName("dates")
    class Dates {

        @Test
        @DisplayName("tenure counts years, months and days from the joining date, leap day included")
        void tenure() {
            Employee e = hr.hire("Leap", "leap@b.com", Department.HR, d("1990-01-01"), d("2020-02-29"), BigDecimal.TEN);
            assertEquals(Period.of(5, 4, 1), hr.tenure(e.id()));
        }

        @Test
        void probation() {
            Employee e = hr.hire("P", "p@b.com", Department.HR, d("1990-01-01"), d("2025-03-03"), BigDecimal.TEN);
            assertEquals(d("2025-09-03"), hr.probationEnds(e.id()));
        }

        @Test
        @DisplayName("birthdays within 30 days: soonest first, today included, leavers left out, 29 Feb on 28 Feb")
        void birthdays() {
            Employee later = hr.hire("Later", "later@b.com", Department.HR, d("1990-07-20"), d("2024-01-01"), BigDecimal.TEN);
            Employee today = hr.hire("Today", "today@b.com", Department.HR, d("1990-06-30"), d("2024-01-01"), BigDecimal.TEN);
            Employee tooFar = hr.hire("Far", "far@b.com", Department.HR, d("1990-08-01"), d("2024-01-01"), BigDecimal.TEN);
            Employee left = hr.hire("Left", "left@b.com", Department.HR, d("1990-07-01"), d("2024-01-01"), BigDecimal.TEN);
            Employee leap = hr.hire("Leap", "leap@b.com", Department.HR, d("1992-02-29"), d("2024-01-01"), BigDecimal.TEN);
            hr.terminate(left.id());
            assertEquals(List.of(today.id(), later.id()), hr.birthdaysWithin(30).stream().map(Employee::id).toList());
            assertEquals(List.of(today.id(), later.id(), tooFar.id()), hr.birthdaysWithin(32).stream().map(Employee::id).toList());
            assertTrue(hr.birthdaysWithin(300).stream().anyMatch(e -> e.id() == leap.id()));
        }

        @Test
        void payday() {
            assertEquals(d("2025-08-29"), hr.payday(YearMonth.of(2025, 8)));
        }
    }

    @Nested
    @DisplayName("leave")
    class Leave {

        private Employee asha;

        @BeforeEach
        void hireAsha() {
            asha = fixture.hire("Asha Rao", "1000");
        }

        @Test
        @DisplayName("days count working days only, so a public holiday inside the range is free")
        void countsWorkingDays() {
            LeaveRequest r = hr.requestLeave(asha.id(), LeaveType.ANNUAL, d("2025-08-11"), d("2025-08-15"));
            assertEquals(4, r.days());
            assertEquals(LeaveStatus.PENDING, r.status());
        }

        @Test
        void dateRules() {
            assertEquals("The leave ends (2025-07-01) before it starts (2025-07-05).",
                    refusal(() -> hr.requestLeave(asha.id(), LeaveType.ANNUAL, d("2025-07-05"), d("2025-07-01"))));
            assertEquals("Leave cannot start in the past (2025-06-20).",
                    refusal(() -> hr.requestLeave(asha.id(), LeaveType.SICK, d("2025-06-20"), d("2025-06-20"))));
            assertEquals("Leave cannot run across two years. Please make two requests.",
                    refusal(() -> hr.requestLeave(asha.id(), LeaveType.ANNUAL, d("2025-12-30"), d("2026-01-02"))));
            assertEquals("That range has no working days.",
                    refusal(() -> hr.requestLeave(asha.id(), LeaveType.ANNUAL, d("2025-07-05"), d("2025-07-06"))));
        }

        @Test
        @DisplayName("starting today is allowed")
        void startingToday() {
            assertEquals(1, hr.requestLeave(asha.id(), LeaveType.SICK, HrFixture.TODAY, HrFixture.TODAY).days());
        }

        @Test
        @DisplayName("overlapping requests are refused, but a rejected request frees the days")
        void overlap() {
            LeaveRequest first = hr.requestLeave(asha.id(), LeaveType.ANNUAL, d("2025-07-14"), d("2025-07-16"));
            assertTrue(refusal(() -> hr.requestLeave(asha.id(), LeaveType.SICK, d("2025-07-16"), d("2025-07-17")))
                    .startsWith("That overlaps leave request " + first.id()));
            hr.requestLeave(asha.id(), LeaveType.SICK, d("2025-07-17"), d("2025-07-17"));
            hr.reject(first.id());
            hr.requestLeave(asha.id(), LeaveType.ANNUAL, d("2025-07-14"), d("2025-07-16"));
        }

        @Test
        @DisplayName("annual leave is limited to 18 days a year, and pending requests already count")
        void allowance() {
            hr.requestLeave(asha.id(), LeaveType.ANNUAL, d("2025-07-01"), d("2025-07-14"));   // 10 working days
            assertEquals(8, hr.balance(asha.id(), LeaveType.ANNUAL, 2025));
            assertEquals("Only 8 ANNUAL days left in 2025, but 9 requested.",
                    refusal(() -> hr.requestLeave(asha.id(), LeaveType.ANNUAL, d("2025-09-01"), d("2025-09-11"))));
            hr.requestLeave(asha.id(), LeaveType.ANNUAL, d("2025-09-01"), d("2025-09-10"));   // exactly 8
            assertEquals(0, hr.balance(asha.id(), LeaveType.ANNUAL, 2025));
        }

        @Test
        void unpaidLeaveHasNoLimit() {
            LeaveRequest r = hr.requestLeave(asha.id(), LeaveType.UNPAID, d("2025-09-01"), d("2025-11-28"));
            assertEquals(65, r.days());
        }

        @Test
        void decisionsOnlyOnce() {
            LeaveRequest r = hr.requestLeave(asha.id(), LeaveType.ANNUAL, d("2025-07-14"), d("2025-07-14"));
            hr.approve(r.id());
            assertEquals("Leave request " + r.id() + " is already APPROVED.", refusal(() -> hr.reject(r.id())));
            assertEquals("No leave request with id 99", refusal(() -> hr.approve(99)));
        }
    }

    @Nested
    @DisplayName("changes")
    class Changes {

        @Test
        void terminateKeepsTheRecordAndRejectsPendingLeave() {
            Employee e = fixture.hire("Leaver", "1000");
            LeaveRequest pending = hr.requestLeave(e.id(), LeaveType.ANNUAL, d("2025-07-14"), d("2025-07-15"));
            LeaveRequest approved = hr.requestLeave(e.id(), LeaveType.ANNUAL, d("2025-07-21"), d("2025-07-22"));
            hr.approve(approved.id());
            hr.terminate(e.id());
            assertEquals(false, hr.employee(e.id()).active());
            assertEquals(LeaveStatus.REJECTED, fixture.store.findLeave(pending.id()).orElseThrow().status());
            assertEquals(LeaveStatus.APPROVED, fixture.store.findLeave(approved.id()).orElseThrow().status());
            assertEquals("Leaver no longer works here.", refusal(() -> hr.changeSalary(e.id(), BigDecimal.TEN)));
            assertEquals("Leaver no longer works here.", refusal(() -> hr.requestLeave(e.id(), LeaveType.SICK, d("2025-08-01"), d("2025-08-01"))));
        }

        @Test
        void salaryAndDepartment() {
            Employee e = fixture.hire("Mover", "1000");
            assertEquals(new BigDecimal("2000.00"), hr.changeSalary(e.id(), new BigDecimal("2000")).monthlySalary());
            assertEquals(Department.FINANCE, hr.transfer(e.id(), Department.FINANCE).department());
            assertEquals("The salary must be positive.", refusal(() -> hr.changeSalary(e.id(), new BigDecimal("-5"))));
            assertEquals(List.of(e.id()), hr.activeIn(Department.FINANCE).stream().map(Employee::id).toList());
        }

        @Test
        void unknownEmployee() {
            assertEquals("No employee with id 9", refusal(() -> hr.employee(9)));
        }
    }

    @Nested
    @DisplayName("payslips")
    class Payslips {

        @Test
        @DisplayName("a high salary pays 10 percent tax, a lower one 5 percent; exactly 50,000 is the lower rate")
        void taxRates() {
            Employee low = fixture.hire("Low", "50000");
            Employee high = fixture.hire("High", "50000.01");
            Payslip lowSlip = hr.payslip(low.id(), YearMonth.of(2025, 7));
            Payslip highSlip = hr.payslip(high.id(), YearMonth.of(2025, 7));
            assertEquals(new BigDecimal("2500.00"), lowSlip.tax());
            assertEquals(new BigDecimal("47500.00"), lowSlip.net());
            assertEquals(new BigDecimal("5000.00"), highSlip.tax());
        }

        @Test
        @DisplayName("approved unpaid days reduce the pay by working-day shares; pending ones do not")
        void unpaidDays() {
            Employee e = fixture.hire("Unpaid", "46000");
            LeaveRequest approved = hr.requestLeave(e.id(), LeaveType.UNPAID, d("2025-07-14"), d("2025-07-18"));
            hr.requestLeave(e.id(), LeaveType.UNPAID, d("2025-07-21"), d("2025-07-25"));
            assertEquals(0, hr.payslip(e.id(), YearMonth.of(2025, 7)).unpaidDays());
            hr.approve(approved.id());
            Payslip slip = hr.payslip(e.id(), YearMonth.of(2025, 7));
            assertEquals(5, slip.unpaidDays());
            assertEquals(new BigDecimal("10000.00"), slip.unpaidDeduction());   // 46000 * 5 / 23
            assertEquals(new BigDecimal("1800.00"), slip.tax());                // 36000 * 5 percent
            assertEquals(new BigDecimal("34200.00"), slip.net());
        }

        @Test
        @DisplayName("unpaid leave that crosses a month end is split between the two months")
        void crossingMonths() {
            Employee e = fixture.hire("Cross", "23000");
            LeaveRequest r = hr.requestLeave(e.id(), LeaveType.UNPAID, d("2025-07-30"), d("2025-08-04"));
            hr.approve(r.id());
            assertEquals(2, hr.payslip(e.id(), YearMonth.of(2025, 7)).unpaidDays());   // Wed 30, Thu 31
            assertEquals(2, hr.payslip(e.id(), YearMonth.of(2025, 8)).unpaidDays());   // Mon 4, and Fri 1
            assertEquals(0, hr.payslip(e.id(), YearMonth.of(2025, 9)).unpaidDays());
        }
    }

    @Test
    @DisplayName("exercise 2: probation ending within 30 days, soonest first, leavers left out")
    void probationEndingSoon() {
        Employee soon = hr.hire("Soon", "soon@b.com", Department.HR, d("1990-01-01"), d("2025-01-03"), BigDecimal.TEN);    // ends Thu 3 July
        Employee sooner = hr.hire("Sooner", "sooner@b.com", Department.HR, d("1990-01-01"), d("2024-12-30"), BigDecimal.TEN); // ends Mon 30 June
        Employee done = hr.hire("Done", "done@b.com", Department.HR, d("1990-01-01"), d("2024-01-03"), BigDecimal.TEN);
        Employee far = hr.hire("Far", "far@b.com", Department.HR, d("1990-01-01"), d("2025-03-03"), BigDecimal.TEN);
        Employee left = hr.hire("Left", "left@b.com", Department.HR, d("1990-01-01"), d("2025-01-06"), BigDecimal.TEN);
        hr.terminate(left.id());
        assertEquals(List.of(sooner.id(), soon.id()), hr.probationEndingWithin(30).stream().map(Employee::id).toList());
    }

    @Test
    @DisplayName("exercise 3: anniversaries in a month; leap-day joiners on 28 February; this year's joiners excluded")
    void anniversaries() {
        Employee leap = hr.hire("Leap", "leap@b.com", Department.HR, d("1990-01-01"), d("2020-02-29"), BigDecimal.TEN);
        Employee early = hr.hire("Early", "early@b.com", Department.HR, d("1990-01-01"), d("2022-02-10"), BigDecimal.TEN);
        hr.hire("New", "new@b.com", Department.HR, d("1990-01-01"), d("2025-02-01"), BigDecimal.TEN);
        assertEquals(List.of(early.id(), leap.id()), hr.anniversariesIn(YearMonth.of(2025, 2)).stream().map(Employee::id).toList());
    }
}
