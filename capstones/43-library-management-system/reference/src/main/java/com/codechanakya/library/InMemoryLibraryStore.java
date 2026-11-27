package com.codechanakya.library;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

/** A store that keeps everything in maps and lists. Given to you. */
public class InMemoryLibraryStore implements LibraryStore {

    private final Map<String, Book> books = new HashMap<>();
    private final Map<Integer, Member> members = new TreeMap<>();
    private final Map<Integer, Loan> loans = new TreeMap<>();
    private final List<Reservation> reservations = new ArrayList<>();
    private final Map<Integer, BigDecimal> fines = new HashMap<>();
    private int nextMemberId = 1;
    private int nextLoanId = 1;
    private int nextReservationId = 1;

    @Override
    public void saveBook(Book book) {
        books.put(book.isbn(), book);
    }

    @Override
    public Optional<Book> findBook(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    @Override
    public List<Book> allBooks() {
        return new ArrayList<>(books.values());
    }

    @Override
    public Member addMember(String name, MemberType type) {
        Member member = new Member(nextMemberId++, name, type);
        members.put(member.id(), member);
        return member;
    }

    @Override
    public Optional<Member> findMember(int id) {
        return Optional.ofNullable(members.get(id));
    }

    @Override
    public Loan addLoan(String isbn, int memberId, LocalDate borrowedOn, LocalDate dueOn) {
        Loan loan = new Loan(nextLoanId++, isbn, memberId, borrowedOn, dueOn, null, 0);
        loans.put(loan.id(), loan);
        return loan;
    }

    @Override
    public Optional<Loan> findLoan(int id) {
        return Optional.ofNullable(loans.get(id));
    }

    @Override
    public void updateLoan(Loan loan) {
        loans.put(loan.id(), loan);
    }

    @Override
    public List<Loan> allLoans() {
        return new ArrayList<>(loans.values());
    }

    @Override
    public Reservation addReservation(String isbn, int memberId, LocalDate reservedOn) {
        Reservation reservation = new Reservation(nextReservationId++, isbn, memberId, reservedOn);
        reservations.add(reservation);
        return reservation;
    }

    @Override
    public List<Reservation> reservationsFor(String isbn) {
        return reservations.stream().filter(r -> r.isbn().equals(isbn)).toList();
    }

    @Override
    public void removeReservation(int reservationId) {
        reservations.removeIf(r -> r.id() == reservationId);
    }

    @Override
    public BigDecimal fines(int memberId) {
        return fines.getOrDefault(memberId, BigDecimal.ZERO).setScale(2);
    }

    @Override
    public void setFines(int memberId, BigDecimal amount) {
        fines.put(memberId, amount.setScale(2));
    }
}
