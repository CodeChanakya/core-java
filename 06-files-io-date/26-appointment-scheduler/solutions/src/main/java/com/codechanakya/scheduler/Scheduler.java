package com.codechanakya.scheduler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/** Keeps appointments in time order, refuses overlaps, and saves them to a text file. */
public class Scheduler {

    /** Weekday, date, 24-hour time, the offset from UTC and the zone name. */
    private static final DateTimeFormatter SHOWN =
            DateTimeFormatter.ofPattern("EEE dd MMM HH:mm xxx '['VV']'", Locale.ENGLISH);

    private final List<Appointment> appointments = new ArrayList<>();
    private final List<String> loadProblems = new ArrayList<>();

    public void add(Appointment wanted) throws ScheduleConflictException {
        for (Appointment existing : appointments) {
            if (existing.overlaps(wanted)) {
                throw new ScheduleConflictException(wanted, existing);
            }
        }
        appointments.add(wanted);
        Collections.sort(appointments);                 // Appointment is Comparable by moment in time
    }

    public List<Appointment> all() {
        return Collections.unmodifiableList(appointments);
    }

    /**
     * The agenda as text, with every start shown on the clocks of one zone.
     * withZoneSameInstant keeps the moment and changes only how it is displayed.
     */
    public List<String> agendaIn(ZoneId zone) {
        List<String> lines = new ArrayList<>();
        for (Appointment appointment : appointments) {
            ZonedDateTime local = appointment.getStart().withZoneSameInstant(zone);
            lines.add(local.format(SHOWN) + "  " + appointment.getTitle()
                    + " (" + appointment.getLength().toMinutes() + " min)");
        }
        return lines;
    }

    public void save(Path file) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Appointment appointment : appointments) {
            lines.add(appointment.toLine());
        }
        Files.write(file, lines, StandardCharsets.UTF_8);
    }

    /** Loads a file. Lines that cannot be read are skipped and listed in getLoadProblems(). */
    public static Scheduler load(Path file) throws IOException {
        Scheduler scheduler = new Scheduler();
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            try {
                scheduler.add(Appointment.fromLine(lines.get(i)));
            } catch (ScheduleConflictException e) {
                scheduler.loadProblems.add("line " + (i + 1) + ": " + e.getMessage());
            } catch (RuntimeException e) {
                // DateTimeParseException, NumberFormatException and IllegalArgumentException all end up here.
                scheduler.loadProblems.add("line " + (i + 1) + ": could not read (" + e.getClass().getSimpleName() + ")");
            }
        }
        return scheduler;
    }

    public List<String> getLoadProblems() {
        return loadProblems;
    }

    /** I1: appointments that start on a given calendar date as seen on the clocks of one zone. */
    public List<Appointment> on(LocalDate date, ZoneId zone) {
        List<Appointment> found = new ArrayList<>();
        for (Appointment appointment : appointments) {
            if (appointment.getStart().withZoneSameInstant(zone).toLocalDate().equals(date)) {
                found.add(appointment);
            }
        }
        return found;
    }

    /** I2: removes the appointment with this title. Returns false when there was none. */
    public boolean cancel(String title) {
        Iterator<Appointment> it = appointments.iterator();
        while (it.hasNext()) {
            if (it.next().getTitle().equalsIgnoreCase(title)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * C1: the earliest start at or after "from" where an appointment of this length fits.
     * Appointments are sorted by start, so one pass is enough: each clash pushes the candidate
     * forward to the end of the appointment it clashed with.
     */
    public ZonedDateTime firstFreeSlot(ZonedDateTime from, Duration length) {
        ZonedDateTime candidate = from;
        for (Appointment existing : appointments) {
            Appointment trial = new Appointment("trial", candidate, length);
            if (existing.overlaps(trial)) {
                candidate = existing.getEnd();
            }
        }
        return candidate;
    }
}
