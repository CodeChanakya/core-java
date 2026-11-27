package com.codechanakya.library;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.List;

/**
 * The library's rules. THIS IS THE FILE YOU WRITE.
 *
 * <p>Every method below throws {@link UnsupportedOperationException} until you replace its body. The comment on each
 * method is part of the specification. The tests in {@code src/test} are the rest of it. Read them first.
 *
 * <p>General rules:
 * <ul>
 *   <li>Today's date always comes from {@code clock}: {@code LocalDate.now(clock)}. Never {@code LocalDate.now()}.</li>
 *   <li>Money is a {@link BigDecimal} with two decimal places (scale 2), everywhere it is returned.</li>
 *   <li>Whenever a rule is broken, or something does not exist, throw a {@link LibraryException} whose message says what
 *       is wrong in words a librarian would understand.</li>
 *   <li>Talk to the data only through {@link LibraryStore}.</li>
 * </ul>
 */
public class LibraryService {

    private final LibraryStore store;
    private final Clock clock;

    public LibraryService(LibraryStore store, Clock clock) {
        this.store = store;
        this.clock = clock;
    }

    // ----- catalogue and members

    /**
     * Adds a book to the catalogue and returns it.
     * The ISBN, title and author are trimmed. The ISBN and the title must not be blank, there must be at least one
     * copy, and no other book may have the same ISBN.
     */
    public Book addBook(String isbn, String title, String author, int copies) {
        throw new UnsupportedOperationException("TODO: addBook");
    }

    /** Adds a member and returns them. The name is trimmed and must not be blank. Ids are given by the store. */
    public Member join(String name, MemberType type) {
        throw new UnsupportedOperationException("TODO: join");
    }

    /** The copies on the shelf: the copies the library owns, minus the loans that are still out. Unknown book: error. */
    public int availableCopies(String isbn) {
        throw new UnsupportedOperationException("TODO: availableCopies");
    }

    /**
     * Books whose title or author contains the text, ignoring upper and lower case, sorted by title (also ignoring
     * case), then by ISBN. Blank or null text finds nothing: return an empty list.
     */
    public List<Book> search(String text) {
        throw new UnsupportedOperationException("TODO: search");
    }

    // ----- borrowing, renewing, returning

    /**
     * Lends a copy of a book to a member. The loan is due {@code type.loanDays()} days from today.
     * It is refused when:
     * <ul>
     *   <li>the member or the book does not exist;</li>
     *   <li>the member already holds {@code type.maxLoans()} books;</li>
     *   <li>the member has a loan that is overdue (due before today; a loan due today is not overdue);</li>
     *   <li>the member owes MORE than 50.00 in fines (exactly 50.00 is allowed);</li>
     *   <li>the member already holds a copy of this book;</li>
     *   <li>no copy is available;</li>
     *   <li>somebody has reserved the book and this member is not first in the queue. When the member IS first in
     *       the queue, the borrowing succeeds and their reservation is removed.</li>
     * </ul>
     */
    public Loan borrow(int memberId, String isbn) {
        throw new UnsupportedOperationException("TODO: borrow");
    }

    /**
     * Extends a loan by 7 days and returns the changed loan. It is refused when the loan does not exist, has been
     * returned, has already been renewed once, is overdue (a loan due today may still be renewed), or when anybody is
     * waiting for the book.
     */
    public Loan renew(int loanId) {
        throw new UnsupportedOperationException("TODO: renew");
    }

    /**
     * Takes a book back today and returns the fine for this loan: the member's fine per day multiplied by the days
     * after the due date. Returned on or before the due date: 0.00. The fine is also added to what the member owes
     * (see {@code store.fines} and {@code store.setFines}). A loan that does not exist, or was already returned, is an error.
     */
    public BigDecimal returnBook(int loanId) {
        throw new UnsupportedOperationException("TODO: returnBook");
    }

    // ----- reservations

    /**
     * Puts the member in the queue for a book that has no copy on the shelf. It is refused when the member or the
     * book does not exist, when a copy IS on the shelf (they should borrow it), when the member already holds the
     * book, or when the member is already in the queue for it.
     */
    public Reservation reserve(int memberId, String isbn) {
        throw new UnsupportedOperationException("TODO: reserve");
    }

    // ----- fines and reports

    /** What the member owes in fines. Unknown member: error. */
    public BigDecimal outstandingFines(int memberId) {
        throw new UnsupportedOperationException("TODO: outstandingFines");
    }

    /**
     * Takes a payment off the member's fines and returns what is still owed. The payment must be more than zero and
     * no more than what is owed.
     */
    public BigDecimal payFine(int memberId, BigDecimal amount) {
        throw new UnsupportedOperationException("TODO: payFine");
    }

    /** The books the member has now (not yet returned), lowest loan id first. Unknown member: error. */
    public List<Loan> loansOf(int memberId) {
        throw new UnsupportedOperationException("TODO: loansOf");
    }

    /** Every loan that is overdue today, earliest due date first, then lowest loan id. */
    public List<Loan> overdueLoans() {
        throw new UnsupportedOperationException("TODO: overdueLoans");
    }
}
