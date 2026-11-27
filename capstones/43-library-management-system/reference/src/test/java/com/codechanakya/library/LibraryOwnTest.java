package com.codechanakya.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * An example of the tests YOU write. The acceptance tests came with the project and check the rules in the brief.
 * These check the things the brief does not spell out: combinations of rules, and the edges between them.
 * Note that this class does not end in "AcceptanceTest", so ./run.sh --grade does not count it.
 */
class LibraryOwnTest extends AcceptanceBase {

    @Test
    @DisplayName("a renewed loan returned on its new due date has no fine")
    void renewedThenReturnedOnTime() {
        book("A", 1);
        Member asha = student("Asha");
        Loan loan = library.borrow(asha.id(), "A");
        library.renew(loan.id());
        passDays(14 + 7);
        assertEquals(money("0.00"), library.returnBook(loan.id()));
    }

    @Test
    @DisplayName("a renewed loan that is returned a day after its new due date is fined for one day only")
    void renewedThenOneDayLate() {
        book("A", 1);
        Member asha = student("Asha");
        Loan loan = library.borrow(asha.id(), "A");
        library.renew(loan.id());
        passDays(14 + 7 + 1);
        assertEquals(money("2.00"), library.returnBook(loan.id()));
    }

    @Test
    @DisplayName("paying the fines in full lets a blocked member borrow again")
    void payingUnblocks() {
        book("A", 1);
        book("B", 1);
        Member asha = student("Asha");
        Loan loan = library.borrow(asha.id(), "A");
        passDays(14 + 30);                                         // 30 days late: 60.00, over the limit
        library.returnBook(loan.id());
        assertThrows(LibraryException.class, () -> library.borrow(asha.id(), "B"));
        library.payFine(asha.id(), new BigDecimal("10.00"));       // 50.00 left: exactly the limit
        library.borrow(asha.id(), "B");
    }

    @Test
    @DisplayName("a student with two books who has paid all fines can still take a third")
    void thirdBook() {
        book("A", 1);
        book("B", 1);
        book("C", 1);
        Member asha = student("Asha");
        library.borrow(asha.id(), "A");
        library.borrow(asha.id(), "B");
        library.borrow(asha.id(), "C");
        assertEquals(3, library.loansOf(asha.id()).size());
    }

    @Test
    @DisplayName("the search text is trimmed")
    void searchTrims() {
        library.addBook("1", "Clean Code", "Robert Martin", 1);
        assertEquals(1, library.search("  clean  ").size());
    }

    @Test
    @DisplayName("blank and padded details are cleaned when a book is added")
    void addBookTrims() {
        Book book = library.addBook("  111  ", "  Clean Code ", " Robert Martin ", 1);
        assertEquals("111", book.isbn());
        assertEquals("Clean Code", book.title());
        assertEquals("Robert Martin", book.author());
        assertTrue(library.search("clean").stream().map(Book::isbn).toList().contains("111"));
        assertEquals(List.of("111"), library.search("martin").stream().map(Book::isbn).toList());
    }
}
