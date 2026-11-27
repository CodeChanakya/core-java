package com.codechanakya.library;

/** A title the library owns, with the number of copies it owns. Given to you. */
public record Book(String isbn, String title, String author, int copies) {
}
