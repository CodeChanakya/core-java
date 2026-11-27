package com.codechanakya.library;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Where the library keeps its data. Given to you, with an in-memory version.
 * Your {@link LibraryService} talks only to this interface, never to a map or a database directly.
 */
public interface LibraryStore {

    void saveBook(Book book);

    Optional<Book> findBook(String isbn);

    /** Every book, in no particular order. */
    List<Book> allBooks();

    /** Saves a new member and returns it with the next id (1, 2, 3, ...). */
    Member addMember(String name, MemberType type);

    Optional<Member> findMember(int id);

    /** Saves a new loan, not yet returned, and returns it with the next id (1, 2, 3, ...). */
    Loan addLoan(String isbn, int memberId, LocalDate borrowedOn, LocalDate dueOn);

    Optional<Loan> findLoan(int id);

    /** Replaces the stored loan that has the same id. */
    void updateLoan(Loan loan);

    /** Every loan ever made, returned or not, ordered by id. */
    List<Loan> allLoans();

    /** Saves a new reservation and returns it with the next id (1, 2, 3, ...). */
    Reservation addReservation(String isbn, int memberId, LocalDate reservedOn);

    /** The waiting list for one book, first come first. */
    List<Reservation> reservationsFor(String isbn);

    void removeReservation(int reservationId);

    /** What the member owes in fines. Zero (scale 2) for a member who owes nothing. */
    BigDecimal fines(int memberId);

    void setFines(int memberId, BigDecimal amount);
}
