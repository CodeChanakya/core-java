package com.codechanakya.logs;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Running totals across one or more log files. */
public class LogReport {

    private final Map<Level, Integer> byLevel = new EnumMap<>(Level.class);
    private final Map<String, Integer> errorsByComponent = new TreeMap<>();
    private final List<String> firstErrors = new ArrayList<>();
    private int unreadable = 0;
    private int lines = 0;

    public LogReport() {
        for (Level level : Level.values()) {
            byLevel.put(level, 0);          // so a level with no lines still shows up as 0
        }
    }

    /** Adds every line of one file. The file name is only used to label the first error found. */
    public void addFile(String fileName, List<String> fileLines) {
        boolean firstErrorSeen = false;
        int lineNumber = 0;
        for (String line : fileLines) {
            lineNumber++;
            lines++;
            LogEntry entry = LogParser.parse(line);
            if (entry == null) {
                unreadable++;
                continue;
            }
            byLevel.merge(entry.getLevel(), 1, Integer::sum);
            if (entry.getLevel() == Level.ERROR) {
                errorsByComponent.merge(entry.getComponent(), 1, Integer::sum);
                if (!firstErrorSeen) {
                    firstErrors.add(fileName + " line " + lineNumber + " at " + entry.getTime()
                            + ": " + entry.getMessage());
                    firstErrorSeen = true;
                }
            }
        }
    }

    public Map<Level, Integer> getByLevel() {
        return byLevel;
    }

    public Map<String, Integer> getErrorsByComponent() {
        return errorsByComponent;
    }

    public List<String> getFirstErrors() {
        return firstErrors;
    }

    public int getUnreadable() {
        return unreadable;
    }

    public int getLines() {
        return lines;
    }

    /** The summary as text lines, ready to be written to a file. */
    public List<String> summaryLines() {
        List<String> out = new ArrayList<>();
        out.add("Lines read: " + lines + " (unreadable: " + unreadable + ")");
        for (Map.Entry<Level, Integer> entry : byLevel.entrySet()) {
            out.add(String.format("%-5s %d", entry.getKey(), entry.getValue()));
        }
        out.add("Errors by component: " + errorsByComponent);
        return out;
    }
}
