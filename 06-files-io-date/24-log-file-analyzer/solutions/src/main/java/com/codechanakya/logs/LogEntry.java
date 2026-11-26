package com.codechanakya.logs;

/** One parsed log line. */
public class LogEntry {

    private final String date;
    private final String time;
    private final Level level;
    private final String component;
    private final String message;

    public LogEntry(String date, String time, Level level, String component, String message) {
        this.date = date;
        this.time = time;
        this.level = level;
        this.component = component;
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Level getLevel() {
        return level;
    }

    public String getComponent() {
        return component;
    }

    public String getMessage() {
        return message;
    }
}
