package com.codechanakya.reports;

/**
 * What somebody asks for: a report name and how many rows of data it must go through.
 *
 * <p>A record is a good fit here. The request never changes after it is made, so it is safe to
 * hand from one thread to another without any locking.
 */
public record ReportRequest(String name, int rows) {
}
