package com.codechanakya.library;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;

/**
 * What every acceptance test starts from: a library with an in-memory store, and a clock set to Monday
 * 30 June 2025. Given to you: do not change it. Day 0 in the tests means 30 June 2025.
 */
abstract class AcceptanceBase {

    static final LocalDate DAY_0 = LocalDate.of(2025, 6, 30);

    TestClock clock;
    LibraryService library;

    @BeforeEach
    void setUpLibrary() {
        clock = new TestClock(DAY_0);
        library = new LibraryService(new InMemoryLibraryStore(), clock);
    }

    static BigDecimal money(String text) {
        return new BigDecimal(text).setScale(2);
    }

    void passDays(int days) {
        clock.advance(days);
    }

    Book book(String isbn, int copies) {
        return library.addBook(isbn, "Title " + isbn, "Author " + isbn, copies);
    }

    Member student(String name) {
        return library.join(name, MemberType.STUDENT);
    }

    Member faculty(String name) {
        return library.join(name, MemberType.FACULTY);
    }
}
