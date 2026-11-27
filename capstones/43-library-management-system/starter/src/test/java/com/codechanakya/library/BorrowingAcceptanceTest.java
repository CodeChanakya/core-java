package com.codechanakya.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BorrowingAcceptanceTest extends AcceptanceBase {

    @Test
    @DisplayName("a student's loan lasts 14 days")
    void studentDueDate() {
        book("A", 1);
        Member asha = student("Asha");
        Loan loan = library.borrow(asha.id(), "A");
        assertEquals(1, loan.id());
        assertEquals(DAY_0, loan.borrowedOn());
        assertEquals(DAY_0.plusDays(14), loan.dueOn());
        assertNull(loan.returnedOn());
        assertEquals(0, loan.renewals());
    }

    @Test
    @DisplayName("a faculty member's loan lasts 30 days")
    void facultyDueDate() {
        book("A", 1);
        Member bilal = faculty("Bilal");
        assertEquals(DAY_0.plusDays(30), library.borrow(bilal.id(), "A").dueOn());
    }

    @Test
    @DisplayName("borrowing takes one copy off the shelf")
    void copiesGoDown() {
        book("A", 3);
        Member asha = student("Asha");
        library.borrow(asha.id(), "A");
        assertEquals(2, library.availableCopies("A"));
    }

    @Test
    @DisplayName("when every copy is out, nobody else can borrow")
    void noCopiesLeft() {
        book("A", 1);
        Member asha = student("Asha");
        Member bilal = student("Bilal");
        library.borrow(asha.id(), "A");
        assertThrows(LibraryException.class, () -> library.borrow(bilal.id(), "A"));
    }

    @Test
    @DisplayName("a student may hold three books, not four")
    void studentLimit() {
        book("A", 1);
        book("B", 1);
        book("C", 1);
        book("D", 1);
        Member asha = student("Asha");
        library.borrow(asha.id(), "A");
        library.borrow(asha.id(), "B");
        library.borrow(asha.id(), "C");
        assertThrows(LibraryException.class, () -> library.borrow(asha.id(), "D"));
    }

    @Test
    @DisplayName("a faculty member may hold ten books, not eleven")
    void facultyLimit() {
        for (int i = 1; i <= 11; i++) {
            book("B" + i, 1);
        }
        Member bilal = faculty("Bilal");
        for (int i = 1; i <= 10; i++) {
            library.borrow(bilal.id(), "B" + i);
        }
        assertThrows(LibraryException.class, () -> library.borrow(bilal.id(), "B11"));
    }

    @Test
    @DisplayName("a member cannot hold two copies of the same book")
    void sameBookTwice() {
        book("A", 2);
        Member asha = student("Asha");
        library.borrow(asha.id(), "A");
        assertThrows(LibraryException.class, () -> library.borrow(asha.id(), "A"));
    }

    @Test
    @DisplayName("an unknown member or an unknown book is an error")
    void unknowns() {
        book("A", 1);
        Member asha = student("Asha");
        assertThrows(LibraryException.class, () -> library.borrow(99, "A"));
        assertThrows(LibraryException.class, () -> library.borrow(asha.id(), "nope"));
    }

    @Test
    @DisplayName("a member with an overdue book cannot borrow, but one whose book is due today can")
    void overdueBlocksBorrowing() {
        book("A", 1);
        book("B", 1);
        book("C", 1);
        Member asha = student("Asha");
        library.borrow(asha.id(), "A");
        passDays(14);                                              // due today: not overdue yet
        library.borrow(asha.id(), "B");
        passDays(1);                                               // now A is a day overdue
        assertThrows(LibraryException.class, () -> library.borrow(asha.id(), "C"));
    }

    @Test
    @DisplayName("fines of exactly 50.00 still allow borrowing; more than 50.00 does not")
    void fineLimit() {
        book("A", 1);
        book("B", 1);
        book("C", 1);
        Member asha = student("Asha");
        Loan first = library.borrow(asha.id(), "A");
        passDays(14 + 25);                                         // 25 days late at 2.00 a day
        assertEquals(money("50.00"), library.returnBook(first.id()));
        Loan second = library.borrow(asha.id(), "B");              // allowed: the fines are exactly 50.00
        passDays(14 + 1);                                          // one more day late
        assertEquals(money("2.00"), library.returnBook(second.id()));
        assertEquals(money("52.00"), library.outstandingFines(asha.id()));
        assertThrows(LibraryException.class, () -> library.borrow(asha.id(), "C"));
    }

    @Test
    @DisplayName("loansOf lists the books a member has now, oldest first, without returned ones")
    void loansOf() {
        book("A", 1);
        book("B", 1);
        book("C", 1);
        Member asha = student("Asha");
        Loan a = library.borrow(asha.id(), "A");
        Loan b = library.borrow(asha.id(), "B");
        library.borrow(asha.id(), "C");
        library.returnBook(b.id());
        assertEquals(List.of("A", "C"), library.loansOf(asha.id()).stream().map(Loan::isbn).toList());
        assertEquals(a.id(), library.loansOf(asha.id()).get(0).id());
    }

    @Test
    @DisplayName("loansOf for an unknown member is an error")
    void loansOfUnknown() {
        assertThrows(LibraryException.class, () -> library.loansOf(5));
    }
}
