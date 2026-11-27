package com.codechanakya.reports;

/**
 * A finished report. Like the request, it is immutable, so the worker thread that built it and
 * the main thread that reads it can never disagree about what it contains.
 *
 * @param name     the report's name
 * @param rows     how many rows were processed
 * @param checksum a number worked out from every row; the same rows always give the same number
 */
public record Report(String name, int rows, long checksum) {

    /** One line for the console. */
    public String summary() {
        return name + ": " + rows + " rows, checksum " + checksum;
    }
}
