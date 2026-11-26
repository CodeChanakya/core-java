package com.codechanakya.reports;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
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

    /** I1: the last day of the month on which banks are open. */
    public static LocalDate lastBusinessDay(YearMonth month) {
        LocalDate day = month.atEndOfMonth();
        while (isWeekend(day)) {
            day = day.minusDays(1);
        }
        return day;
    }

    /**
     * C1: when a transaction settles. Before 17:00 on a business day it settles the next business day.
     * After the cut-off, or on a weekend, it is treated as received on the next business day,
     * and settles one business day after that.
     */
    public static LocalDate settlementDate(LocalDateTime made) {
        boolean afterCutOff = made.toLocalTime().isAfter(LocalTime.of(17, 0));
        LocalDate received = (afterCutOff || isWeekend(made.toLocalDate()))
                ? nextBusinessDay(made.toLocalDate())
                : made.toLocalDate();
        return nextBusinessDay(received);
    }
}
