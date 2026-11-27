package com.codechanakya.hr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/** All the date arithmetic, with no database. */
class WorkCalendarTest {

    private final WorkCalendar calendar = new WorkCalendar(Set.of(LocalDate.of(2025, 8, 15), LocalDate.of(2025, 12, 25)));

    @Test
    @DisplayName("weekends and listed holidays are not working days")
    void workingDays() {
        assertTrue(calendar.isWorkingDay(LocalDate.of(2025, 6, 30)));
        assertFalse(calendar.isWorkingDay(LocalDate.of(2025, 7, 5)));
        assertFalse(calendar.isWorkingDay(LocalDate.of(2025, 7, 6)));
        assertFalse(calendar.isWorkingDay(LocalDate.of(2025, 8, 15)));
    }

    @ParameterizedTest(name = "{0} to {1} has {2} working days")
    @CsvSource({
            "2025-08-11, 2025-08-15, 4",
            "2025-08-11, 2025-08-11, 1",
            "2025-07-05, 2025-07-06, 0",
            "2025-07-04, 2025-07-07, 2",
            "2025-07-07, 2025-07-04, 0"})
    void workingDaysBetween(String from, String to, int expected) {
        assertEquals(expected, calendar.workingDaysBetween(LocalDate.parse(from), LocalDate.parse(to)));
    }

    @ParameterizedTest(name = "{0} has {1} working days")
    @CsvSource({"2025-07, 23", "2025-08, 20", "2025-02, 20", "2025-12, 22"})
    void workingDaysInMonth(String month, int expected) {
        assertEquals(expected, calendar.workingDaysIn(YearMonth.parse(month)));
    }

    @ParameterizedTest(name = "payday for {0} is {1}")
    @CsvSource({"2025-06, 2025-06-30", "2025-08, 2025-08-29", "2025-11, 2025-11-28", "2025-12, 2025-12-31"})
    void lastWorkingDay(String month, String expected) {
        assertEquals(LocalDate.parse(expected), calendar.lastWorkingDay(YearMonth.parse(month)));
    }

    @Test
    @DisplayName("when the last day is a holiday too, payday moves back past it")
    void paydayBackPastHoliday() {
        WorkCalendar c = new WorkCalendar(Set.of(LocalDate.of(2025, 9, 30)));
        assertEquals(LocalDate.of(2025, 9, 29), c.lastWorkingDay(YearMonth.of(2025, 9)));
    }

    @ParameterizedTest(name = "born {0}, today {1}: next birthday {2}")
    @CsvSource({
            "1992-07-04, 2025-06-30, 2025-07-04",
            "1992-07-04, 2025-07-04, 2025-07-04",
            "1992-07-04, 2025-07-05, 2026-07-04",
            "1988-02-29, 2025-06-30, 2026-02-28",
            "1988-02-29, 2027-12-31, 2028-02-29",
            "1999-12-31, 2025-12-31, 2025-12-31"})
    void nextBirthday(String born, String today, String expected) {
        assertEquals(LocalDate.parse(expected), WorkCalendar.nextBirthday(LocalDate.parse(born), LocalDate.parse(today)));
    }

    @ParameterizedTest(name = "joined {0}: probation ends {1}")
    @CsvSource({
            "2025-03-03, 2025-09-03",
            "2020-02-29, 2020-08-31",
            "2024-08-31, 2025-02-28",
            "2025-06-30, 2025-12-30"})
    void probationEnds(String joined, String expected) {
        assertEquals(LocalDate.parse(expected), calendar.probationEnds(LocalDate.parse(joined)));
    }

    @Test
    void probationMovesPastAHoliday() {
        // 15 August 2025 is a Friday and a holiday; joining on 15 February gives 15 August, so it moves to Monday the 18th.
        assertEquals(LocalDate.of(2025, 8, 18), calendar.probationEnds(LocalDate.of(2025, 2, 15)));
    }

    @Test
    void leapDay() {
        assertTrue(WorkCalendar.isLeapDay(LocalDate.of(2024, 2, 29)));
        assertFalse(WorkCalendar.isLeapDay(LocalDate.of(2024, 2, 28)));
    }
}
