package com.codechanakya.hr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Create, read, update on the real database. Delete is a soft delete, so it is an update. */
class JdbcEmployeeStoreTest {

    private HrFixture fixture;
    private EmployeeStore store;

    @BeforeEach
    void setUp() {
        fixture = new HrFixture();
        store = fixture.store;
    }

    @AfterEach
    void tearDown() {
        fixture.close();
    }

    private Employee sample(String email) {
        return new Employee(0, "Test", email, Department.SALES, LocalDate.of(1990, 2, 28), LocalDate.of(2020, 2, 29),
                new BigDecimal("1000"), true);
    }

    @Test
    @DisplayName("an employee is saved and read back with every field, dates included")
    void roundTrip() {
        Employee saved = store.add(sample("a@example.com"));
        Employee read = store.find(saved.id()).orElseThrow();
        assertEquals(saved, read);
        assertEquals(LocalDate.of(2020, 2, 29), read.joinDate());
        assertEquals(new BigDecimal("1000.00"), read.monthlySalary());
    }

    @Test
    @DisplayName("ids are generated in order and findAll is ordered by id")
    void ids() {
        Employee a = store.add(sample("a@example.com"));
        Employee b = store.add(sample("b@example.com"));
        assertTrue(b.id() > a.id());
        assertEquals(java.util.List.of(a.id(), b.id()), store.findAll().stream().map(Employee::id).toList());
    }

    @Test
    @DisplayName("the same email twice is a DuplicateEmailException, on add and on update")
    void duplicateEmail() {
        store.add(sample("a@example.com"));
        Employee b = store.add(sample("b@example.com"));
        assertThrows(DuplicateEmailException.class, () -> store.add(sample("a@example.com")));
        Employee clash = new Employee(b.id(), b.name(), "a@example.com", b.department(), b.birthDate(), b.joinDate(),
                b.monthlySalary(), true);
        assertThrows(DuplicateEmailException.class, () -> store.update(clash));
    }

    @Test
    void updateChangesTheRow() {
        Employee a = store.add(sample("a@example.com"));
        store.update(a.withSalary(new BigDecimal("2500.5")).withDepartment(Department.HR));
        Employee read = store.find(a.id()).orElseThrow();
        assertEquals(new BigDecimal("2500.50"), read.monthlySalary());
        assertEquals(Department.HR, read.department());
    }

    @Test
    void updateUnknownIdIsReported() {
        HrException e = assertThrows(HrException.class, () -> store.update(new Employee(77, "X", "x@example.com",
                Department.HR, LocalDate.of(1990, 1, 1), LocalDate.of(2020, 1, 1), BigDecimal.ONE, true)));
        assertEquals("No employee with id 77", e.getMessage());
    }

    @Test
    void findByDepartment() {
        store.add(sample("a@example.com"));
        Employee hr = store.add(sample("b@example.com").withDepartment(Department.HR));
        assertEquals(java.util.List.of(hr.id()), store.findByDepartment(Department.HR).stream().map(Employee::id).toList());
    }

    @Test
    @DisplayName("the database refuses a zero salary even from the store")
    void salaryRule() {
        HrException e = assertThrows(HrException.class, () -> store.add(sample("z@example.com").withSalary(BigDecimal.ZERO)));
        assertTrue(e.getMessage().contains("23513"));
    }

    @Test
    @DisplayName("leave round trips, starts PENDING, and is listed oldest first")
    void leave() {
        Employee a = store.add(sample("a@example.com"));
        LeaveRequest later = store.addLeave(a.id(), LeaveType.ANNUAL, LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 2), 2);
        LeaveRequest earlier = store.addLeave(a.id(), LeaveType.SICK, LocalDate.of(2025, 7, 1), LocalDate.of(2025, 7, 1), 1);
        assertEquals(LeaveStatus.PENDING, later.status());
        assertEquals(java.util.List.of(earlier.id(), later.id()), store.leavesOf(a.id()).stream().map(LeaveRequest::id).toList());
        store.updateLeaveStatus(later.id(), LeaveStatus.APPROVED);
        assertEquals(LeaveStatus.APPROVED, store.findLeave(later.id()).orElseThrow().status());
    }

    @Test
    void unknownIds() {
        assertTrue(store.find(5).isEmpty());
        assertTrue(store.findLeave(5).isEmpty());
    }

    @Test
    @DisplayName("an email with SQL in it is stored as plain text")
    void injection() {
        Employee a = store.add(sample("x'); DROP TABLE employee; --@e.com"));
        assertEquals("x'); DROP TABLE employee; --@e.com", store.find(a.id()).orElseThrow().email());
    }
}
