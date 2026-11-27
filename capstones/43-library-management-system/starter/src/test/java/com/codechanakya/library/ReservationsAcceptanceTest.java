package com.codechanakya.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationsAcceptanceTest extends AcceptanceBase {

    @Test
    @DisplayName("a book with no free copy can be reserved")
    void reserve() {
        book("A", 1);
        Member asha = student("Asha");
        Member bilal = student("Bilal");
        library.borrow(asha.id(), "A");
        Reservation reservation = library.reserve(bilal.id(), "A");
        assertEquals(1, reservation.id());
        assertEquals("A", reservation.isbn());
        assertEquals(bilal.id(), reservation.memberId());
        assertEquals(DAY_0, reservation.reservedOn());
    }

    @Test
    @DisplayName("a book that is on the shelf cannot be reserved")
    void availableBook() {
        book("A", 1);
        Member asha = student("Asha");
        assertThrows(LibraryException.class, () -> library.reserve(asha.id(), "A"));
    }

    @Test
    @DisplayName("the member who has the book cannot reserve it, and nobody can reserve twice")
    void holderAndDuplicates() {
        book("A", 1);
        Member asha = student("Asha");
        Member bilal = student("Bilal");
        library.borrow(asha.id(), "A");
        assertThrows(LibraryException.class, () -> library.reserve(asha.id(), "A"));
        library.reserve(bilal.id(), "A");
        assertThrows(LibraryException.class, () -> library.reserve(bilal.id(), "A"));
    }

    @Test
    @DisplayName("reserving an unknown book, or as an unknown member, is an error")
    void unknowns() {
        book("A", 1);
        Member asha = student("Asha");
        assertThrows(LibraryException.class, () -> library.reserve(asha.id(), "nope"));
        assertThrows(LibraryException.class, () -> library.reserve(99, "A"));
    }

    @Test
    @DisplayName("when a copy comes back, only the first in the queue may borrow it")
    void firstComeFirstServed() {
        book("A", 1);
        Member asha = student("Asha");
        Member bilal = student("Bilal");
        Member chitra = student("Chitra");
        Loan loan = library.borrow(asha.id(), "A");
        library.reserve(bilal.id(), "A");
        library.reserve(chitra.id(), "A");
        library.returnBook(loan.id());
        assertThrows(LibraryException.class, () -> library.borrow(chitra.id(), "A"));   // second in the queue: must wait
        assertThrows(LibraryException.class, () -> library.borrow(asha.id(), "A"));     // not in the queue at all
        library.borrow(bilal.id(), "A");                                                // first in the queue: allowed
    }

    @Test
    @DisplayName("once the first in the queue has borrowed, the next one is first")
    void queueAdvances() {
        book("A", 1);
        Member asha = student("Asha");
        Member bilal = student("Bilal");
        Member chitra = student("Chitra");
        Member dev = student("Dev");
        Loan first = library.borrow(asha.id(), "A");
        library.reserve(bilal.id(), "A");
        library.reserve(chitra.id(), "A");
        library.returnBook(first.id());
        Loan second = library.borrow(bilal.id(), "A");        // Bilal leaves the queue
        library.returnBook(second.id());
        assertThrows(LibraryException.class, () -> library.borrow(dev.id(), "A"));   // Chitra is still waiting
        library.borrow(chitra.id(), "A");
    }

    @Test
    @DisplayName("with nobody waiting, anyone may borrow a returned copy")
    void noQueue() {
        book("A", 1);
        Member asha = student("Asha");
        Member bilal = student("Bilal");
        library.returnBook(library.borrow(asha.id(), "A").id());
        library.borrow(bilal.id(), "A");
    }
}
