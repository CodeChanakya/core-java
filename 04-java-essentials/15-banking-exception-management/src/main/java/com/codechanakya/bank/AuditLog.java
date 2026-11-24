package com.codechanakya.bank;

import java.util.ArrayList;

/**
 * A log that must be closed when we are done with it.
 *
 * Implementing AutoCloseable lets it be used in a try-with-resources block:
 * Java calls close() for us at the end, even if an exception was thrown inside.
 * Think of closing files, database connections and network sockets.
 */
public class AuditLog implements AutoCloseable {

    private final String name;
    private final ArrayList<String> entries = new ArrayList<>();

    public AuditLog(String name) {
        this.name = name;
        System.out.println("[audit] " + name + " opened");
    }

    public void record(String entry) {
        entries.add(entry);
        System.out.println("[audit] " + entry);
    }

    @Override
    public void close() {
        System.out.println("[audit] " + name + " closed with " + entries.size() + " entries");
    }
}
