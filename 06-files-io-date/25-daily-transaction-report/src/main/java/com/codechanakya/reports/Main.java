package com.codechanakya.reports;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws IOException {
        // The report date is a fixed value, not LocalDate.now(), so the output is the same every day.
        // In real code you would pass the date in (or a Clock) for exactly this reason.
        LocalDate reportDate = LocalDate.of(2026, 3, 6);

        TransactionReader reader = new TransactionReader();
        List<Transaction> transactions = reader.read(Path.of("data", "transactions.csv"));
        System.out.println("Read " + transactions.size() + " transactions for the report dated "
                + reportDate.format(Calendar.LONG_DAY));
        for (String problem : reader.getProblems()) {
            System.out.println("  Skipped " + problem);
        }

        DailyReport report = new DailyReport(transactions);

        System.out.println();
        System.out.println("--- Net movement per day ---");
        for (Map.Entry<LocalDate, BigDecimal> day : report.netByDay().entrySet()) {
            LocalDate date = day.getKey();
            System.out.printf("  %s  %2d tx  %10s%s%n", date.format(Calendar.LONG_DAY), report.countOn(date),
                    day.getValue(), Calendar.isWeekend(date) ? "  (weekend)" : "");
        }

        System.out.println();
        System.out.println("--- Per week (weeks start on Monday) and per month ---");
        for (Map.Entry<LocalDate, BigDecimal> week : report.netByWeek().entrySet()) {
            System.out.println("  Week of " + week.getKey().format(Calendar.LONG_DAY) + ": " + week.getValue());
        }
        for (Map.Entry<YearMonth, BigDecimal> month : report.netByMonth().entrySet()) {
            System.out.println("  " + month.getKey() + " has " + month.getKey().lengthOfMonth()
                    + " days, net " + month.getValue());
        }

        System.out.println();
        System.out.println("--- Times of day ---");
        for (Transaction tx : report.lateNight()) {
            System.out.println("  After 22:00: " + tx.getAccount() + " at " + tx.getTime() + " on " + tx.getDate());
        }
        LocalDateTime firstOnMonday = LocalDateTime.of(2026, 3, 2, 9, 10);
        LocalDateTime lastOnMonday = LocalDateTime.of(2026, 3, 2, 18, 5);
        Duration busy = Duration.between(firstOnMonday, lastOnMonday);
        System.out.println("  Monday 2 March was busy for " + busy.toHours() + " h " + busy.toMinutesPart() + " min");

        System.out.println();
        System.out.println("--- Distances between dates ---");
        LocalDate first = transactions.get(0).getDate();
        LocalDate last = transactions.get(transactions.size() - 1).getDate();
        System.out.println("  First " + first + ", last " + last);
        System.out.println("  Days between: " + ChronoUnit.DAYS.between(first, last));
        Period gap = Period.between(first, last);
        System.out.println("  As a period : " + gap.getMonths() + " month(s) and " + gap.getDays() + " day(s)");
        System.out.println("  Days from the first transaction to the report date: "
                + ChronoUnit.DAYS.between(first, reportDate));
        System.out.println("  Report date is after the last transaction: " + reportDate.isAfter(last));

        System.out.println();
        System.out.println("--- Settlement: the next business day ---");
        for (LocalDate date : new LocalDate[] {LocalDate.of(2026, 2, 26), LocalDate.of(2026, 2, 27), LocalDate.of(2026, 3, 1)}) {
            System.out.println("  " + date.format(Calendar.LONG_DAY) + " -> "
                    + Calendar.nextBusinessDay(date).format(Calendar.LONG_DAY));
        }

        System.out.println();
        System.out.println("--- Formatting and parsing ---");
        LocalDate fromForm = LocalDate.parse("05/03/2026", Calendar.BRANCH_FORM);
        System.out.println("  \"05/03/2026\" read as day/month/year: " + fromForm + ", " + fromForm.getDayOfWeek());
        System.out.println("  Back to text: " + fromForm.format(Calendar.BRANCH_FORM));

        System.out.println();
        System.out.println("--- Dates that surprise people ---");
        LocalDate jan31 = LocalDate.of(2026, 1, 31);
        System.out.println("  31 Jan 2026 plus one month: " + jan31.plusMonths(1) + " (clamped to the last valid day)");
        System.out.println("  28 Feb 2028 plus one day  : " + LocalDate.of(2028, 2, 28).plusDays(1) + " (2028 is a leap year: "
                + Year.isLeap(2028) + ")");
        System.out.println("  End of March 2026 is a " + YearMonth.of(2026, 3).atEndOfMonth().getDayOfWeek());
        try {
            LocalDate.of(2026, 2, 30);
        } catch (DateTimeException e) {
            System.out.println("  30 Feb 2026 is refused: " + e.getMessage());
        }
        try {
            LocalDate.parse("05/03/2026");              // slashes are not the ISO layout
        } catch (DateTimeParseException e) {
            System.out.println("  Parsing 05/03/2026 with no formatter fails: " + e.getClass().getSimpleName());
        }
    }
}
