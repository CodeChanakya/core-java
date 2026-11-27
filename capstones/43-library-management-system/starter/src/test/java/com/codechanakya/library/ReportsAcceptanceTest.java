package com.codechanakya.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReportsAcceptanceTest extends AcceptanceBase {

    @Test
    @DisplayName("nothing is overdue on the day a loan is due")
    void dueTodayIsNotOverdue() {
        book("A", 1);
        library.borrow(student("Asha").id(), "A");
        passDays(14);
        assertTrue(library.overdueLoans().isEmpty());
    }

    @Test
    @DisplayName("overdue loans are listed earliest due date first, then by loan id; later ones are left out")
    void overdueOrder() {
        book("A", 1);
        book("B", 1);
        book("C", 1);
        book("D", 1);
        Member asha = student("Asha");
        Member bilal = student("Bilal");
        Member chitra = faculty("Chitra");
        Member dev = student("Dev");
        library.borrow(asha.id(), "A");            // loan 1, day 0, due day 14
        passDays(2);
        library.borrow(bilal.id(), "B");           // loan 2, day 2, due day 16
        library.borrow(chitra.id(), "C");          // loan 3, day 2, faculty, due day 32
        library.borrow(dev.id(), "D");             // loan 4, day 2, due day 16
        passDays(15);                              // day 17
        List<Integer> ids = library.overdueLoans().stream().map(Loan::id).toList();
        assertEquals(List.of(1, 2, 4), ids);
    }

    @Test
    @DisplayName("a returned loan is no longer overdue")
    void returnedIsNotOverdue() {
        book("A", 1);
        Loan loan = library.borrow(student("Asha").id(), "A");
        passDays(20);
        assertEquals(1, library.overdueLoans().size());
        library.returnBook(loan.id());
        assertTrue(library.overdueLoans().isEmpty());
    }

    @Test
    @DisplayName("the order is by due date, not by loan id: a later loan can be due earlier")
    void overdueSortedByDueDateNotId() {
        book("A", 1);
        book("B", 1);
        Member bilal = faculty("Bilal");
        Member asha = student("Asha");
        library.borrow(bilal.id(), "A");           // loan 1, faculty, due day 30
        passDays(10);
        library.borrow(asha.id(), "B");            // loan 2, student, due day 24
        passDays(25);                              // day 35: both overdue
        assertEquals(List.of(2, 1), library.overdueLoans().stream().map(Loan::id).toList());
    }
}
