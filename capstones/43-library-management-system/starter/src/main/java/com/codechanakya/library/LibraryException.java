package com.codechanakya.library;

/**
 * Thrown whenever the library refuses to do something: a rule is broken, or something does not exist.
 * Given to you. The acceptance tests check the type. Your message should say what is wrong, in words a librarian
 * would understand.
 */
public class LibraryException extends RuntimeException {

    public LibraryException(String message) {
        super(message);
    }
}
