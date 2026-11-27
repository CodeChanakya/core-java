package com.codechanakya.empdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

/** The business rules, tested with an in-memory repository and a clock that always says 30 June 2025. */
@DisplayName("EmployeeService")
class EmployeeServiceTest {

    private static final LocalDate TODAY = LocalDate.of(2025, 6, 30);
    private static final LocalDate EARLIER = LocalDate.of(2024, 1, 15);

    private EmployeeService service;

    @BeforeEach
    void setUp() {
        Clock clock = Clock.fixed(Instant.parse("2025-06-30T00:00:00Z"), ZoneOffset.UTC);
        service = new EmployeeService(new InMemoryEmployeeRepository(), clock);
    }

    private Employee hireAsha() {
        return service.hire("Asha", "asha@example.com", Department.ENGINEERING, new BigDecimal("90000"), EARLIER);
    }

    @Test
    @DisplayName("hiring stores the person and trims the name")
    void hire() {
        Employee asha = service.hire("  Asha  ", "asha@example.com", Department.QA, new BigDecimal("50000"), EARLIER);
        assertEquals(1, asha.id());
        assertEquals("Asha", asha.name());
        assertEquals(asha, service.get(1));
    }

    @Test
    @DisplayName("a hire date of today is allowed, tomorrow is not")
    void hireDateBoundary() {
        service.hire("Today", "today@example.com", Department.QA, new BigDecimal("50000"), TODAY);
        ValidationException error = assertThrows(ValidationException.class,
                () -> service.hire("Soon", "soon@example.com", Department.QA, new BigDecimal("50000"), TODAY.plusDays(1)));
        assertEquals("Hire date 2025-07-01 is in the future.", error.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   ", "plain", "a@b", "@example.com", "a b@example.com"})
    @DisplayName("an invalid e-mail is refused")
    void invalidEmails(String email) {
        assertThrows(ValidationException.class,
                () -> service.hire("Asha", email, Department.QA, new BigDecimal("50000"), EARLIER));
    }

    @ParameterizedTest
    @CsvSource({"14999.99, true", "15000.00, false", "15000, false", "0, true", "-5, true"})
    @DisplayName("the minimum salary is 15000")
    void minimumSalary(String salary, boolean refused) {
        if (refused) {
            assertThrows(ValidationException.class,
                    () -> service.hire("Asha", "asha@example.com", Department.QA, new BigDecimal(salary), EARLIER));
        } else {
            assertEquals(1, service.hire("Asha", "asha@example.com", Department.QA, new BigDecimal(salary), EARLIER).id());
        }
    }

    @Test
    @DisplayName("a blank name is refused")
    void blankName() {
        assertThrows(ValidationException.class,
                () -> service.hire(" ", "asha@example.com", Department.QA, new BigDecimal("50000"), EARLIER));
    }

    @Test
    @DisplayName("the same e-mail cannot be used twice, whatever its case")
    void duplicateEmail() {
        hireAsha();
        assertThrows(DuplicateEmailException.class,
                () -> service.hire("Another", "ASHA@EXAMPLE.COM", Department.HR, new BigDecimal("50000"), EARLIER));
        assertEquals(1, service.everyone().size());
    }

    @ParameterizedTest
    @CsvSource({"10, 99000.00", "20, 108000.00", "0.5, 90450.00", "3.333, 92999.70"})
    @DisplayName("a raise adds a percentage and rounds to the cent")
    void raise(String percent, String expectedSalary) {
        hireAsha();
        assertEquals(new BigDecimal(expectedSalary), service.giveRaise(1, new BigDecimal(percent)).salary());
        assertEquals(new BigDecimal(expectedSalary), service.get(1).salary());
    }

    @ParameterizedTest
    @ValueSource(strings = {"0", "-1", "20.01", "100"})
    @DisplayName("a raise must be above 0 and at most 20 percent")
    void raiseOutOfRange(String percent) {
        hireAsha();
        assertThrows(ValidationException.class, () -> service.giveRaise(1, new BigDecimal(percent)));
        assertEquals(new BigDecimal("90000.00"), service.get(1).salary());
    }

    @Test
    @DisplayName("moving a person changes only the department")
    void move() {
        Employee before = hireAsha();
        Employee after = service.moveTo(1, Department.SALES);
        assertEquals(Department.SALES, after.department());
        assertEquals(before.withDepartment(Department.SALES), after);
    }

    @Test
    @DisplayName("asking for someone who does not exist is an error, not null")
    void missingEmployee() {
        assertThrows(EmployeeNotFoundException.class, () -> service.get(5));
        assertThrows(EmployeeNotFoundException.class, () -> service.moveTo(5, Department.HR));
        assertThrows(EmployeeNotFoundException.class, () -> service.giveRaise(5, BigDecimal.TEN));
        assertThrows(EmployeeNotFoundException.class, () -> service.terminate(5));
    }

    @Test
    @DisplayName("terminating removes the person")
    void terminate() {
        hireAsha();
        service.terminate(1);
        assertTrue(service.everyone().isEmpty());
    }

    @Test
    @DisplayName("total payroll adds up every department")
    void payroll() {
        hireAsha();
        service.hire("Bilal", "bilal@example.com", Department.QA, new BigDecimal("60000.25"), EARLIER);
        assertEquals(new BigDecimal("150000.25"), service.totalPayroll());
        assertEquals(new BigDecimal("0.00"), new EmployeeService(new InMemoryEmployeeRepository(), Clock.systemUTC()).totalPayroll());
    }
}
