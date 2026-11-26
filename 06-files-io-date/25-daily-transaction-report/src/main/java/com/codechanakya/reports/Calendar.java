package com.codechanakya.reports;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Small date rules used by the report. */
public final class Calendar {

    /**
     * Locale.ENGLISH is passed on purpose. Without it, "EEE" and "MMM" print in the language of the
     * computer running the program, so the same code would produce different text on different machines.
     */
    public static final DateTimeFormatter LONG_DAY = DateTimeFormatter.ofPattern("EEE dd MMM yyyy", Locale.ENGLISH);

    /** The day-month-year layout many branches use in handwritten forms. */
    public static final DateTimeFormatter BRANCH_FORM = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Calendar() {
    }

    public static boolean isWeekend(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    /** The next day banks are open, which is the earliest a transaction made today can settle (T+1). */
    public static LocalDate nextBusinessDay(LocalDate date) {
        LocalDate next = date.plusDays(1);
        while (isWeekend(next)) {
            next = next.plusDays(1);
        }
        return next;
    }
}
