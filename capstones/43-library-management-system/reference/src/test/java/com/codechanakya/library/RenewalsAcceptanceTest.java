package com.codechanakya.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RenewalsAcceptanceTest extends AcceptanceBase {

    @Test
    @DisplayName("renewing adds 7 days to the due date and counts one renewal")
    void renew() {
        book("A", 1);
        Member asha = student("Asha");
        Loan loan = library.borrow(asha.id(), "A");
        Loan renewed = library.renew(loan.id());
        assertEquals(DAY_0.plusDays(14 + 7), renewed.dueOn());
        assertEquals(1, renewed.renewals());
        assertEquals(DAY_0.plusDays(21), library.loansOf(asha.id()).get(0).dueOn());
    }

    @Test
    @DisplayName("a loan can be renewed only once")
    void onlyOnce() {
        book("A", 1);
        Member asha = student("Asha");
        Loan loan = library.borrow(asha.id(), "A");
        library.renew(loan.id());
        assertThrows(LibraryException.class, () -> library.renew(loan.id()));
    }

    @Test
    @DisplayName("an overdue loan cannot be renewed, but one due today can")
    void overdueRenewal() {
        book("A", 1);
        book("B", 1);
        Member asha = student("Asha");
        Member bilal = student("Bilal");
        Loan a = library.borrow(asha.id(), "A");
        Loan b = library.borrow(bilal.id(), "B");
        passDays(14);
        library.renew(a.id());                                      // due today: allowed
        passDays(1);
        assertThrows(LibraryException.class, () -> library.renew(b.id()));   // a day overdue: refused
    }

    @Test
    @DisplayName("a loan cannot be renewed while someone is waiting for the book")
    void reservedBook() {
        book("A", 1);
        Member asha = student("Asha");
        Member bilal = student("Bilal");
        Loan loan = library.borrow(asha.id(), "A");
        library.reserve(bilal.id(), "A");
        assertThrows(LibraryException.class, () -> library.renew(loan.id()));
    }

    @Test
    @DisplayName("a returned loan cannot be renewed, and an unknown loan is an error")
    void returnedOrUnknown() {
        book("A", 1);
        Member asha = student("Asha");
        Loan loan = library.borrow(asha.id(), "A");
        library.returnBook(loan.id());
        assertThrows(LibraryException.class, () -> library.renew(loan.id()));
        assertThrows(LibraryException.class, () -> library.renew(99));
    }
}
