package com.codechanakya.library;

import java.time.LocalDate;

/**
 * One copy of one book lent to one member. Given to you.
 *
 * <p>A loan is immutable. {@link #returnedOn} and {@link #renewedTo} give a changed copy, which you hand back
 * to the store with {@code updateLoan}. A loan with no {@code returnedOn} date is still out.
 */
public record Loan(int id, String isbn, int memberId, LocalDate borrowedOn, LocalDate dueOn, LocalDate returnedOn,
                   int renewals) {

    public boolean isReturned() {
        return returnedOn != null;
    }

    /** True when the book is still out and its due date has passed. A loan due today is not overdue yet. */
    public boolean isOverdueOn(LocalDate today) {
        return !isReturned() && dueOn.isBefore(today);
    }

    public Loan returnedOn(LocalDate date) {
        return new Loan(id, isbn, memberId, borrowedOn, dueOn, date, renewals);
    }

    public Loan renewedTo(LocalDate newDueDate) {
        return new Loan(id, isbn, memberId, borrowedOn, newDueDate, returnedOn, renewals + 1);
    }
}
