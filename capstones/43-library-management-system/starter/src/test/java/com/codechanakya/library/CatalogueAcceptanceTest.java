package com.codechanakya.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CatalogueAcceptanceTest extends AcceptanceBase {

    @Test
    @DisplayName("a new book is stored with its copies, and all of them are available")
    void addBook() {
        Book book = library.addBook("111", "Clean Code", "Robert Martin", 3);
        assertEquals("111", book.isbn());
        assertEquals("Clean Code", book.title());
        assertEquals(3, book.copies());
        assertEquals(3, library.availableCopies("111"));
    }

    @Test
    @DisplayName("an ISBN can be used only once")
    void duplicateIsbn() {
        library.addBook("111", "Clean Code", "Robert Martin", 1);
        assertThrows(LibraryException.class, () -> library.addBook("111", "Another", "Someone", 1));
    }

    @Test
    @DisplayName("a book needs an ISBN, a title and at least one copy")
    void invalidBooks() {
        assertThrows(LibraryException.class, () -> library.addBook("  ", "Title", "Author", 1));
        assertThrows(LibraryException.class, () -> library.addBook("111", "  ", "Author", 1));
        assertThrows(LibraryException.class, () -> library.addBook("111", "Title", "Author", 0));
        assertThrows(LibraryException.class, () -> library.addBook("111", "Title", "Author", -2));
    }

    @Test
    @DisplayName("asking about a book that does not exist is an error")
    void unknownBook() {
        assertThrows(LibraryException.class, () -> library.availableCopies("nope"));
    }

    @Test
    @DisplayName("members get ids 1, 2, 3 and keep their type")
    void join() {
        Member first = student("Asha");
        Member second = faculty("Bilal");
        assertEquals(1, first.id());
        assertEquals(2, second.id());
        assertEquals(MemberType.STUDENT, first.type());
        assertEquals(MemberType.FACULTY, second.type());
        assertEquals("Asha", first.name());
    }

    @Test
    @DisplayName("a member needs a name")
    void joinWithoutName() {
        assertThrows(LibraryException.class, () -> library.join("  ", MemberType.STUDENT));
    }

    @Test
    @DisplayName("search finds part of a title, ignoring case, sorted by title")
    void searchTitle() {
        library.addBook("1", "The Clean Coder", "Robert Martin", 1);
        library.addBook("2", "Clean Code", "Robert Martin", 1);
        library.addBook("3", "Refactoring", "Martin Fowler", 1);
        List<String> titles = library.search("CLEAN").stream().map(Book::title).toList();
        assertEquals(List.of("Clean Code", "The Clean Coder"), titles);
    }

    @Test
    @DisplayName("search also looks at the author")
    void searchAuthor() {
        library.addBook("1", "Clean Code", "Robert Martin", 1);
        library.addBook("2", "Refactoring", "Martin Fowler", 1);
        library.addBook("3", "Effective Java", "Joshua Bloch", 1);
        assertEquals(List.of("Clean Code", "Refactoring"), library.search("martin").stream().map(Book::title).toList());
    }

    @Test
    @DisplayName("sorting by title ignores upper and lower case")
    void searchSortIgnoresCase() {
        library.addBook("1", "banana", "A", 1);
        library.addBook("2", "Apple", "A", 1);
        library.addBook("3", "Cherry", "A", 1);
        assertEquals(List.of("Apple", "banana", "Cherry"), library.search("a").stream().map(Book::title).toList());
    }

    @Test
    @DisplayName("search with no match, or with blank text, finds nothing")
    void searchNothing() {
        library.addBook("1", "Clean Code", "Robert Martin", 1);
        assertTrue(library.search("zebra").isEmpty());
        assertTrue(library.search("   ").isEmpty());
    }
}
