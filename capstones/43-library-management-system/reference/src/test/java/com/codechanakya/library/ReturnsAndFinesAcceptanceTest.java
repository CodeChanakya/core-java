package com.codechanakya.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReturnsAndFinesAcceptanceTest extends AcceptanceBase {

    @Test
    @DisplayName("an on-time return costs nothing and puts the copy back")
    void onTime() {
        book("A", 1);
        Member asha = student("Asha");
        Loan loan = library.borrow(asha.id(), "A");
        passDays(5);
        assertEquals(money("0.00"), library.returnBook(loan.id()));
        assertEquals(1, library.availableCopies("A"));
        assertEquals(money("0.00"), library.outstandingFines(asha.id()));
    }

    @Test
    @DisplayName("returning on the due date is not late")
    void onDueDate() {
        book("A", 1);
        Member asha = student("Asha");
        Loan loan = library.borrow(asha.id(), "A");
        passDays(14);
        assertEquals(money("0.00"), library.returnBook(loan.id()));
    }

    @Test
    @DisplayName("a student pays 2.00 for each day late")
    void studentFine() {
        book("A", 1);
        book("B", 1);
        Member asha = student("Asha");
        Loan one = library.borrow(asha.id(), "A");
        passDays(15);
        assertEquals(money("2.00"), library.returnBook(one.id()));
        Loan two = library.borrow(asha.id(), "B");
        passDays(14 + 5);
        assertEquals(money("10.00"), library.returnBook(two.id()));
    }

    @Test
    @DisplayName("a faculty member pays 1.00 for each day late")
    void facultyFine() {
        book("A", 1);
        Member bilal = faculty("Bilal");
        Loan loan = library.borrow(bilal.id(), "A");
        passDays(30 + 5);
        assertEquals(money("5.00"), library.returnBook(loan.id()));
    }

    @Test
    @DisplayName("fines add up across loans")
    void finesAccumulate() {
        book("A", 1);
        book("B", 1);
        Member asha = student("Asha");
        Loan one = library.borrow(asha.id(), "A");
        Loan two = library.borrow(asha.id(), "B");
        passDays(14 + 1);
        library.returnBook(one.id());
        passDays(1);
        library.returnBook(two.id());
        assertEquals(money("6.00"), library.outstandingFines(asha.id()));
    }

    @Test
    @DisplayName("a member who owes nothing owes 0.00")
    void noFines() {
        Member asha = student("Asha");
        assertEquals(money("0.00"), library.outstandingFines(asha.id()));
    }

    @Test
    @DisplayName("paying part of a fine leaves the rest, and paying the rest clears it")
    void payFine() {
        book("A", 1);
        Member asha = student("Asha");
        Loan loan = library.borrow(asha.id(), "A");
        passDays(14 + 5);
        library.returnBook(loan.id());                              // 10.00 owed
        assertEquals(money("6.00"), library.payFine(asha.id(), new BigDecimal("4.00")));
        assertEquals(money("6.00"), library.outstandingFines(asha.id()));
        assertEquals(money("0.00"), library.payFine(asha.id(), new BigDecimal("6.00")));
        assertEquals(money("0.00"), library.outstandingFines(asha.id()));
    }

    @Test
    @DisplayName("a payment cannot be more than what is owed, zero, or negative")
    void badPayments() {
        book("A", 1);
        Member asha = student("Asha");
        Loan loan = library.borrow(asha.id(), "A");
        passDays(14 + 5);
        library.returnBook(loan.id());                              // 10.00 owed
        assertThrows(LibraryException.class, () -> library.payFine(asha.id(), new BigDecimal("10.01")));
        assertThrows(LibraryException.class, () -> library.payFine(asha.id(), BigDecimal.ZERO));
        assertThrows(LibraryException.class, () -> library.payFine(asha.id(), new BigDecimal("-1")));
        assertEquals(money("10.00"), library.outstandingFines(asha.id()));
    }

    @Test
    @DisplayName("a loan can be returned only once, and an unknown loan is an error")
    void returnTwice() {
        book("A", 1);
        Member asha = student("Asha");
        Loan loan = library.borrow(asha.id(), "A");
        library.returnBook(loan.id());
        assertThrows(LibraryException.class, () -> library.returnBook(loan.id()));
        assertThrows(LibraryException.class, () -> library.returnBook(99));
    }

    @Test
    @DisplayName("after returning a book the member may borrow it again")
    void borrowAgain() {
        book("A", 1);
        Member asha = student("Asha");
        library.returnBook(library.borrow(asha.id(), "A").id());
        library.borrow(asha.id(), "A");
        assertEquals(0, library.availableCopies("A"));
    }
}
