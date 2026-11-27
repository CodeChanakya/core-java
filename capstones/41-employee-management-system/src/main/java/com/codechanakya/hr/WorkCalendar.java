package com.codechanakya.hr;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.MonthDay;
import java.time.YearMonth;
import java.util.Set;
import java.util.TreeSet;

/**
 * Which days count as working days: Monday to Friday, except the listed public holidays.
 * All the date arithmetic of the HR system lives here, so it can be tested without any database.
 */
public final class WorkCalendar {

    private final Set<LocalDate> holidays;

    public WorkCalendar(Set<LocalDate> holidays) {
        this.holidays = new TreeSet<>(holidays);
    }

    public boolean isWorkingDay(LocalDate date) {
        DayOfWeek day = date.getDayOfWeek();
        return day != DayOfWeek.SATURDAY && day != DayOfWeek.SUNDAY && !holidays.contains(date);
    }

    /** Working days from {@code from} to {@code to}, both included. Zero when {@code to} is before {@code from}. */
    public int workingDaysBetween(LocalDate from, LocalDate to) {
        if (to.isBefore(from)) {
            return 0;
        }
        return (int) from.datesUntil(to.plusDays(1)).filter(this::isWorkingDay).count();
    }

    public int workingDaysIn(YearMonth month) {
        return workingDaysBetween(month.atDay(1), month.atEndOfMonth());
    }

    /** The last working day of the month: payday. */
    public LocalDate lastWorkingDay(YearMonth month) {
        LocalDate day = month.atEndOfMonth();
        while (!isWorkingDay(day)) {
            day = day.minusDays(1);
        }
        return day;
    }

    /** The given date, or the next working day if it is a weekend or holiday. */
    public LocalDate onOrAfter(LocalDate date) {
        LocalDate day = date;
        while (!isWorkingDay(day)) {
            day = day.plusDays(1);
        }
        return day;
    }

    /**
     * The next birthday on or after {@code today}. A 29 February birthday is celebrated on 28 February in
     * years that have no 29 February.
     */
    public static LocalDate nextBirthday(LocalDate birthDate, LocalDate today) {
        MonthDay birthday = MonthDay.from(birthDate);
        LocalDate thisYear = birthday.atYear(today.getYear());
        return thisYear.isBefore(today) ? birthday.atYear(today.getYear() + 1) : thisYear;
    }

    /** The last day of the probation period: six months after joining, moved to the next working day. */
    public LocalDate probationEnds(LocalDate joinDate) {
        return onOrAfter(joinDate.plusMonths(6));
    }

    public static boolean isLeapDay(LocalDate date) {
        return date.getMonth() == Month.FEBRUARY && date.getDayOfMonth() == 29;
    }
}
