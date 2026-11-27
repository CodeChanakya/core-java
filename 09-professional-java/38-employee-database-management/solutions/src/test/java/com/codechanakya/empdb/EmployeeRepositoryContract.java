package com.codechanakya.empdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The promises every {@link EmployeeRepository} must keep. Each implementation gets a small subclass
 * that only says how to create one; the tests below are inherited and run against both. If a new
 * implementation breaks a promise, one of these tests says so (the L in SOLID: any implementation
 * can stand in for the interface).
 */
abstract class EmployeeRepositoryContract {

    protected EmployeeRepository repository;

    /** Builds a fresh, empty repository. */
    protected abstract EmployeeRepository create();

    @BeforeEach
    void setUpRepository() {
        repository = create();
    }

    static Employee draft(String name, String email, Department department, String salary) {
        return Employee.draft(name, email, department, new BigDecimal(salary), LocalDate.of(2024, 5, 20));
    }

    @Test
    void insertGivesEachEmployeeAnIdStartingAtOne() {
        Employee first = repository.insert(draft("Asha", "asha@example.com", Department.QA, "50000"));
        Employee second = repository.insert(draft("Bilal", "bilal@example.com", Department.QA, "51000"));
        assertEquals(1, first.id());
        assertEquals(2, second.id());
    }

    @Test
    void whatYouStoreIsWhatYouGetBack() {
        Employee stored = repository.insert(draft("Asha", "asha@example.com", Department.HR, "50000.5"));
        assertEquals(stored, repository.findById(stored.id()).orElseThrow());
        assertEquals(new BigDecimal("50000.50"), stored.salary());
    }

    @Test
    void findByIdOfAMissingEmployeeIsEmpty() {
        assertTrue(repository.findById(42).isEmpty());
    }

    @Test
    void findAllIsOrderedById() {
        repository.insert(draft("Cara", "c@example.com", Department.SALES, "40000"));
        repository.insert(draft("Abe", "a@example.com", Department.SALES, "41000"));
        List<Employee> all = repository.findAll();
        assertEquals(List.of("Cara", "Abe"), all.stream().map(Employee::name).toList());
    }

    @Test
    void findByDepartmentReturnsOnlyThatDepartment() {
        repository.insert(draft("A", "a@example.com", Department.QA, "40000"));
        repository.insert(draft("B", "b@example.com", Department.HR, "41000"));
        repository.insert(draft("C", "c@example.com", Department.QA, "42000"));
        assertEquals(List.of("A", "C"), repository.findByDepartment(Department.QA).stream().map(Employee::name).toList());
        assertTrue(repository.findByDepartment(Department.SALES).isEmpty());
    }

    @Test
    void findByNamePrefixIgnoresCaseAndKeepsIdOrder() { // A2
        repository.insert(draft("Anita", "anita@example.com", Department.QA, "40000"));
        repository.insert(draft("Bilal", "bilal@example.com", Department.QA, "41000"));
        repository.insert(draft("asha", "asha@example.com", Department.HR, "42000"));
        assertEquals(List.of("Anita", "asha"), repository.findByNamePrefix("A").stream().map(Employee::name).toList());
        assertEquals(List.of("Bilal"), repository.findByNamePrefix("bi").stream().map(Employee::name).toList());
        assertTrue(repository.findByNamePrefix("Z").isEmpty());
    }

    @Test
    void updateReplacesTheStoredData() {
        Employee stored = repository.insert(draft("Asha", "asha@example.com", Department.QA, "50000"));
        repository.update(stored.withSalary(new BigDecimal("55000")).withDepartment(Department.HR));
        Employee reloaded = repository.findById(stored.id()).orElseThrow();
        assertEquals(new BigDecimal("55000.00"), reloaded.salary());
        assertEquals(Department.HR, reloaded.department());
    }

    @Test
    void updateOfAMissingEmployeeFails() {
        Employee ghost = new Employee(77, "Ghost", "g@example.com", Department.HR, new BigDecimal("40000"), LocalDate.of(2024, 1, 1));
        assertThrows(EmployeeNotFoundException.class, () -> repository.update(ghost));
    }

    @Test
    void deleteReportsWhetherAnythingWasRemoved() {
        Employee stored = repository.insert(draft("Asha", "asha@example.com", Department.QA, "50000"));
        assertTrue(repository.delete(stored.id()));
        assertFalse(repository.delete(stored.id()));
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    void anEmailCanBeUsedOnlyOnce() {
        repository.insert(draft("Asha", "asha@example.com", Department.QA, "50000"));
        assertThrows(DuplicateEmailException.class,
                () -> repository.insert(draft("Other", "asha@example.com", Department.HR, "50000")));
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void existsByEmailIgnoresCase() {
        repository.insert(draft("Asha", "Asha@Example.com", Department.QA, "50000"));
        assertTrue(repository.existsByEmail("asha@example.com"));
        assertFalse(repository.existsByEmail("nobody@example.com"));
    }

    @Test
    void statsCoverEveryDepartmentThatHasPeople() {
        repository.insert(draft("A", "a@example.com", Department.QA, "40000"));
        repository.insert(draft("B", "b@example.com", Department.QA, "50000.50"));
        repository.insert(draft("C", "c@example.com", Department.HR, "30000"));

        Map<Department, DepartmentStats> stats = repository.statsByDepartment();

        assertEquals(List.of(Department.QA, Department.HR), List.copyOf(stats.keySet()));
        assertEquals(new DepartmentStats(2, new BigDecimal("90000.50"), new BigDecimal("50000.50")), stats.get(Department.QA));
        assertEquals(new DepartmentStats(1, new BigDecimal("30000.00"), new BigDecimal("30000.00")), stats.get(Department.HR));
    }

    @Test
    void statsOfAnEmptyRepositoryAreEmpty() {
        assertTrue(repository.statsByDepartment().isEmpty());
    }
}
