package com.codechanakya.scheduler;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * A booking: a title, when it starts (with its time zone) and how long it lasts.
 *
 * The start is a ZonedDateTime, so "10:00 in New York" is remembered together with the
 * rules of New York. Everything that compares two appointments goes through toInstant(),
 * the one absolute moment on the timeline, because 10:00 in New York and 14:00 in London
 * can be the same moment and local clock times cannot be compared directly.
 */
public class Appointment implements Comparable<Appointment> {

    private final String title;
    private final ZonedDateTime start;
    private final Duration length;

    public Appointment(String title, ZonedDateTime start, Duration length) {
        if (length.isZero() || length.isNegative()) {
            throw new IllegalArgumentException("An appointment needs a positive length");
        }
        this.title = title;
        this.start = start;
        this.length = length;
    }

    public String getTitle() {
        return title;
    }

    public ZonedDateTime getStart() {
        return start;
    }

    public Duration getLength() {
        return length;
    }

    /** The end, found by adding exact elapsed time, so it stays right across a clock change. */
    public ZonedDateTime getEnd() {
        return start.plus(length);
    }

    /** True when the two appointments share any time. One ending exactly as another starts is fine. */
    public boolean overlaps(Appointment other) {
        return start.toInstant().isBefore(other.getEnd().toInstant())
                && other.start.toInstant().isBefore(getEnd().toInstant());
    }

    @Override
    public int compareTo(Appointment other) {
        return start.toInstant().compareTo(other.start.toInstant());
    }

    /** One line for the data file: title|start|minutes. ZonedDateTime.toString() already includes the zone. */
    public String toLine() {
        return title + "|" + start + "|" + length.toMinutes();
    }

    /** The reverse of toLine(). Throws an unchecked exception when the line is not in that layout. */
    public static Appointment fromLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 3) {
            throw new IllegalArgumentException("expected 3 parts but found " + parts.length);
        }
        return new Appointment(parts[0], ZonedDateTime.parse(parts[1]), Duration.ofMinutes(Long.parseLong(parts[2])));
    }
}
