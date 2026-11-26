package com.codechanakya.scheduler;

/** Thrown when a new appointment overlaps one that is already booked. */
public class ScheduleConflictException extends Exception {

    private final Appointment existing;

    public ScheduleConflictException(Appointment wanted, Appointment existing) {
        super("\"" + wanted.getTitle() + "\" overlaps \"" + existing.getTitle() + "\"");
        this.existing = existing;
    }

    public Appointment getExisting() {
        return existing;
    }
}
