package com.codechanakya.library;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * The library's rules. This is the reference solution: one way to do it, not the only way.
 * Money has two decimal places (scale 2) everywhere. Today's date always comes from the clock.
 */
public class LibraryService {

    static final BigDecimal FINE_LIMIT = new BigDecimal("50.00");
    static final int RENEWAL_DAYS = 7;

    private final LibraryStore store;
    private final Clock clock;

    public LibraryService(LibraryStore store, Clock clock) {
        this.store = store;
        this.clock = clock;
    }

    // ----- catalogue and members

    public Book addBook(String isbn, String title, String author, int copies) {
        if (isbn == null || isbn.isBlank()) {
            throw new LibraryException("A book needs an ISBN.");
        }
        if (title == null || title.isBlank()) {
            throw new LibraryException("A book needs a title.");
        }
        if (copies < 1) {
            throw new LibraryException("A book needs at least one copy, but " + copies + " was given.");
        }
        String key = isbn.trim();
        if (store.findBook(key).isPresent()) {
            throw new LibraryException("A book with ISBN " + key + " already exists.");
        }
        Book book = new Book(key, title.trim(), author == null ? "" : author.trim(), copies);
        store.saveBook(book);
        return book;
    }

    public Member join(String name, MemberType type) {
        if (name == null || name.isBlank()) {
            throw new LibraryException("A member needs a name.");
        }
        return store.addMember(name.trim(), type);
    }

    /** Copies on the shelf: the copies owned, minus the loans still out. */
    public int availableCopies(String isbn) {
        Book book = requireBook(isbn);
        long out = store.allLoans().stream().filter(l -> l.isbn().equals(isbn) && !l.isReturned()).count();
        return book.copies() - (int) out;
    }

    /** Books whose title or author contains the text, ignoring case, sorted by title. Blank text finds nothing. */
    public List<Book> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        String needle = text.trim().toLowerCase(Locale.ROOT);
        return store.allBooks().stream()
                .filter(b -> b.title().toLowerCase(Locale.ROOT).contains(needle) || b.author().toLowerCase(Locale.ROOT).contains(needle))
                .sorted(Comparator.comparing((Book b) -> b.title().toLowerCase(Locale.ROOT)).thenComparing(Book::isbn))
                .toList();
    }

    // ----- borrowing, renewing, returning

    public Loan borrow(int memberId, String isbn) {
        Member member = requireMember(memberId);
        requireBook(isbn);
        LocalDate today = today();
        List<Loan> current = loansOf(memberId);
        if (current.size() >= member.type().maxLoans()) {
            throw new LibraryException(member.name() + " already has " + current.size() + " books, the most a "
                    + member.type() + " may hold.");
        }
        if (current.stream().anyMatch(l -> l.isOverdueOn(today))) {
            throw new LibraryException(member.name() + " has an overdue book and must return it first.");
        }
        if (store.fines(memberId).compareTo(FINE_LIMIT) > 0) {
            throw new LibraryException(member.name() + " owes " + store.fines(memberId).toPlainString()
                    + " in fines and must pay before borrowing.");
        }
        if (current.stream().anyMatch(l -> l.isbn().equals(isbn))) {
            throw new LibraryException(member.name() + " already has a copy of " + isbn + ".");
        }
        if (availableCopies(isbn) == 0) {
            throw new LibraryException("No copy of " + isbn + " is available. It can be reserved.");
        }
        List<Reservation> queue = store.reservationsFor(isbn);
        if (!queue.isEmpty()) {
            Reservation first = queue.get(0);
            if (first.memberId() != memberId) {
                throw new LibraryException(isbn + " is held for another member who reserved it first.");
            }
            store.removeReservation(first.id());
        }
        return store.addLoan(isbn, memberId, today, today.plusDays(member.type().loanDays()));
    }

    /** Adds 7 days to the due date. Allowed once, never when overdue, never when someone is waiting for the book. */
    public Loan renew(int loanId) {
        Loan loan = requireLoan(loanId);
        if (loan.isReturned()) {
            throw new LibraryException("Loan " + loanId + " has already been returned.");
        }
        if (loan.renewals() >= 1) {
            throw new LibraryException("Loan " + loanId + " has already been renewed once.");
        }
        if (loan.isOverdueOn(today())) {
            throw new LibraryException("Loan " + loanId + " is overdue and cannot be renewed.");
        }
        if (!store.reservationsFor(loan.isbn()).isEmpty()) {
            throw new LibraryException("Someone is waiting for " + loan.isbn() + ", so it cannot be renewed.");
        }
        Loan renewed = loan.renewedTo(loan.dueOn().plusDays(RENEWAL_DAYS));
        store.updateLoan(renewed);
        return renewed;
    }

    /** Returns the book and gives back the fine for this loan: the member's daily rate times the days late. */
    public BigDecimal returnBook(int loanId) {
        Loan loan = requireLoan(loanId);
        if (loan.isReturned()) {
            throw new LibraryException("Loan " + loanId + " has already been returned.");
        }
        Member member = requireMember(loan.memberId());
        LocalDate today = today();
        long daysLate = Math.max(0, ChronoUnit.DAYS.between(loan.dueOn(), today));
        BigDecimal fine = member.type().finePerDay().multiply(BigDecimal.valueOf(daysLate)).setScale(2);
        store.updateLoan(loan.returnedOn(today));
        if (fine.signum() > 0) {
            store.setFines(member.id(), store.fines(member.id()).add(fine));
        }
        return fine;
    }

    // ----- reservations

    public Reservation reserve(int memberId, String isbn) {
        Member member = requireMember(memberId);
        requireBook(isbn);
        if (availableCopies(isbn) > 0) {
            throw new LibraryException(isbn + " is on the shelf. Borrow it instead of reserving it.");
        }
        if (loansOf(memberId).stream().anyMatch(l -> l.isbn().equals(isbn))) {
            throw new LibraryException(member.name() + " already has a copy of " + isbn + ".");
        }
        if (store.reservationsFor(isbn).stream().anyMatch(r -> r.memberId() == memberId)) {
            throw new LibraryException(member.name() + " has already reserved " + isbn + ".");
        }
        return store.addReservation(isbn, memberId, today());
    }

    // ----- fines and reports

    public BigDecimal outstandingFines(int memberId) {
        requireMember(memberId);
        return store.fines(memberId);
    }

    /** Takes a payment off the member's fines and returns what is still owed. */
    public BigDecimal payFine(int memberId, BigDecimal amount) {
        requireMember(memberId);
        if (amount.signum() <= 0) {
            throw new LibraryException("A payment must be more than zero.");
        }
        BigDecimal owed = store.fines(memberId);
        if (amount.compareTo(owed) > 0) {
            throw new LibraryException("The payment of " + amount.toPlainString() + " is more than the " + owed.toPlainString() + " owed.");
        }
        BigDecimal left = owed.subtract(amount);
        store.setFines(memberId, left);
        return left.setScale(2);
    }

    /** The books a member has now, oldest loan first. */
    public List<Loan> loansOf(int memberId) {
        requireMember(memberId);
        return store.allLoans().stream().filter(l -> l.memberId() == memberId && !l.isReturned()).toList();
    }

    /** Every loan still out and past its due date, earliest due date first, then lowest loan id. */
    public List<Loan> overdueLoans() {
        LocalDate today = today();
        return store.allLoans().stream()
                .filter(l -> l.isOverdueOn(today))
                .sorted(Comparator.comparing(Loan::dueOn).thenComparing(Loan::id))
                .toList();
    }

    // ----- helpers

    private Book requireBook(String isbn) {
        return store.findBook(isbn).orElseThrow(() -> new LibraryException("No book with ISBN " + isbn + "."));
    }

    private Member requireMember(int id) {
        return store.findMember(id).orElseThrow(() -> new LibraryException("No member with id " + id + "."));
    }

    private Loan requireLoan(int id) {
        return store.findLoan(id).orElseThrow(() -> new LibraryException("No loan with id " + id + "."));
    }

    private LocalDate today() {
        return LocalDate.now(clock);
    }
}
