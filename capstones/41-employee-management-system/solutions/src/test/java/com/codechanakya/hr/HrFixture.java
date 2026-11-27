package com.codechanakya.hr;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/** A fresh in-memory HR system for one test. "Today" is Monday 30 June 2025; 15 August 2025 is a holiday. */
final class HrFixture {

    private static final AtomicInteger COUNTER = new AtomicInteger();

    static final LocalDate TODAY = LocalDate.of(2025, 6, 30);

    final String name = "hr-test-" + COUNTER.incrementAndGet();
    final ConnectionProvider provider = Schema.inMemory(name);
    final EmployeeStore store = new JdbcEmployeeStore(provider);
    final WorkCalendar calendar = new WorkCalendar(Set.of(LocalDate.of(2025, 8, 15)));
    final Clock clock = Clock.fixed(Instant.parse("2025-06-30T00:00:00Z"), ZoneOffset.UTC);
    final HrService hr = new HrService(store, calendar, clock);

    HrFixture() {
        Schema.create(provider);
    }

    Employee hire(String name, String salary) {
        return hr.hire(name, name.toLowerCase().replace(' ', '.') + "@example.com", Department.ENGINEERING,
                LocalDate.of(1990, 5, 20), LocalDate.of(2024, 1, 15), new BigDecimal(salary));
    }

    void close() {
        Schema.drop(name);
    }
}
