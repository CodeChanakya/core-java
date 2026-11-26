package com.codechanakya.logs;

/**
 * Turns one line of text into a LogEntry.
 *
 * Expected layout: date, time, level, component, then the rest of the line as the message.
 * Columns are separated by one or more spaces, which is why the split uses a pattern.
 */
public final class LogParser {

    private LogParser() {
    }

    /** Returns the entry, or null when the line is not a log line (a stack trace, a blank line, a note). */
    public static LogEntry parse(String line) {
        String[] parts = line.trim().split("\\s+", 5);     // 5 = at most five pieces; the message keeps its spaces
        if (parts.length < 5) {
            return null;
        }
        try {
            Level level = Level.valueOf(parts[2]);
            return new LogEntry(parts[0], parts[1], level, parts[3], parts[4]);
        } catch (IllegalArgumentException e) {
            return null;                                    // third word was not a known level, such as TRACE
        }
    }
}
