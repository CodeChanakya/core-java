package com.codechanakya.library;

import java.time.LocalDate;

/** A member waiting for a book that has no free copy. Given to you. */
public record Reservation(int id, String isbn, int memberId, LocalDate reservedOn) {
}
