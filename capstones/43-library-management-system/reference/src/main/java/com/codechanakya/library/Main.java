package com.codechanakya.library;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.function.Supplier;

/** A short story that uses every rule of the library. It is the reference solution's demonstration. */
public class Main {

    /** A clock the story can move forward. Day 0 is Monday 30 June 2025. */
    private static final class StoryClock extends Clock {
        private LocalDate today = LocalDate.of(2025, 6, 30);

        void advance(int days) {
            today = today.plusDays(days);
        }

        @Override
        public ZoneId getZone() {
            return ZoneOffset.UTC;
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return this;
        }

        @Override
        public Instant instant() {
            return today.atStartOfDay(ZoneOffset.UTC).toInstant();
        }
    }

    public static void main(String[] args) {
        StoryClock clock = new StoryClock();
        LibraryService library = new LibraryService(new InMemoryLibraryStore(), clock);

        System.out.println("== Setting up");
        library.addBook("978-0132350884", "Clean Code", "Robert C. Martin", 2);
        library.addBook("978-0134685991", "Effective Java", "Joshua Bloch", 1);
        library.addBook("978-0201633610", "Design Patterns", "Erich Gamma", 1);
        library.addBook("978-0321125217", "Domain-Driven Design", "Eric Evans", 1);
        Member asha = library.join("Asha Rao", MemberType.STUDENT);
        Member bilal = library.join("Bilal Khan", MemberType.FACULTY);
        Member chitra = library.join("Chitra Nair", MemberType.STUDENT);
        System.out.println("  4 books, 3 members");
        System.out.println("  Search \"java\": " + library.search("java").stream().map(Book::title).toList());
        System.out.println("  Search \"eric\": " + library.search("eric").stream().map(Book::title).toList());

        System.out.println();
        System.out.println("== Day 0: borrowing");
        Loan cleanCode = show(library.borrow(asha.id(), "978-0132350884"), "Asha borrows Clean Code");
        show(library.borrow(asha.id(), "978-0134685991"), "Asha borrows Effective Java");
        show(library.borrow(bilal.id(), "978-0201633610"), "Bilal borrows Design Patterns");
        refused("Chitra borrows Effective Java (no copy left)", () -> library.borrow(chitra.id(), "978-0134685991"));
        refused("Asha borrows Clean Code again", () -> library.borrow(asha.id(), "978-0132350884"));
        System.out.println("  Chitra reserves Effective Java: reservation " + library.reserve(chitra.id(), "978-0134685991").id());
        refused("Chitra reserves Clean Code (a copy is free)", () -> library.reserve(chitra.id(), "978-0132350884"));

        System.out.println();
        System.out.println("== Day 10: renewing");
        clock.advance(10);
        show(library.renew(cleanCode.id()), "Asha renews Clean Code");
        refused("Asha renews Clean Code again", () -> library.renew(cleanCode.id()));
        refused("Asha renews Effective Java (Chitra is waiting)", () -> library.renew(2));

        System.out.println();
        System.out.println("== Day 17: Effective Java is 3 days late");
        clock.advance(7);
        for (Loan late : library.overdueLoans()) {
            System.out.println("  Overdue: loan " + late.id() + ", due " + late.dueOn());
        }
        refused("Asha borrows Domain-Driven Design (she has an overdue book)", () -> library.borrow(asha.id(), "978-0321125217"));
        System.out.println("  Asha returns Effective Java, fine " + library.returnBook(2).toPlainString());
        System.out.println("  Asha owes " + library.outstandingFines(asha.id()).toPlainString());
        refused("Asha borrows Effective Java (Chitra is first in the queue)", () -> library.borrow(asha.id(), "978-0134685991"));
        show(library.borrow(chitra.id(), "978-0134685991"), "Chitra borrows Effective Java");

        System.out.println();
        System.out.println("== Day 26: Clean Code is 5 days late, Design Patterns is 4 days early");
        clock.advance(9);
        System.out.println("  Asha returns Clean Code, fine " + library.returnBook(cleanCode.id()).toPlainString());
        System.out.println("  Bilal returns Design Patterns, fine " + library.returnBook(3).toPlainString());
        System.out.println("  Asha owes " + library.outstandingFines(asha.id()).toPlainString());
        System.out.println("  Asha pays 4.00, still owes " + library.payFine(asha.id(), new BigDecimal("4.00")).toPlainString());
        refused("Asha pays 100.00", () -> library.payFine(asha.id(), new BigDecimal("100.00")));

        System.out.println();
        System.out.println("== Shelf at the end");
        for (String title : new String[]{"Clean Code", "Effective Java", "Design Patterns", "Domain-Driven Design"}) {
            Book book = library.search(title).get(0);
            System.out.printf("  %-22s %d of %d on the shelf%n", book.title(), library.availableCopies(book.isbn()), book.copies());
        }
    }

    private static Loan show(Loan loan, String what) {
        System.out.println("  " + what + ": loan " + loan.id() + ", due " + loan.dueOn());
        return loan;
    }

    private static void refused(String what, Supplier<?> action) {
        try {
            action.get();
            System.out.println("  " + what + ": done");
        } catch (LibraryException e) {
            System.out.println("  " + what + ": refused, " + e.getMessage());
        }
    }
}
