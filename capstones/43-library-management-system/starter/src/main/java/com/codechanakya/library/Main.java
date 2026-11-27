package com.codechanakya.library;

import java.time.Clock;

/**
 * YOUR demonstration. Write a short story that uses your library: add books, join members, borrow, return late,
 * reserve, pay a fine, and print what happens. Use a fixed or adjustable clock, so that the output is the same every time.
 * The reference solution's Main is a model, but do not open it until you have finished.
 */
public class Main {

    public static void main(String[] args) {
        LibraryService library = new LibraryService(new InMemoryLibraryStore(), Clock.systemDefaultZone());
        System.out.println("Library starter: write your demonstration here.");
        System.out.println("Run ./run.sh --grade to see how many acceptance tests pass.");
    }
}
