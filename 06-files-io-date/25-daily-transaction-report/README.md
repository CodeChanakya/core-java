# 25 · Daily Transaction Report

**Phase 06 · Files, I/O and Date** &nbsp;|&nbsp; **Level:** Intermediate &nbsp;|&nbsp; **Java:** 21 or newer

Concepts: `LocalDate`, `LocalTime`, `LocalDateTime`, `Duration`, `Period`, `ChronoUnit`, `YearMonth`, `DateTimeFormatter`.

---

## The problem

A bank branch exports its transactions as a CSV. The operations team wants a daily statement, a weekly and monthly total, a list of late-night activity, and to know when each payment settles (the next business day, which is not always tomorrow).

Every one of those is a question about the calendar. Java has had date classes since 1996, and the old ones (`Date`, `Calendar`) are confusing and easy to misuse. Since Java 8 the `java.time` package replaces them, and it is the only one you should use now.

## Requirements

**Functional**
1. Read transactions with a date, time, account, type and amount. Skip bad rows with a reason.
2. Net movement per day, per week (starting Monday) and per month.
3. Mark weekend days. List transactions after 22:00.
4. How long between the first and last day, and how many days to the report date.
5. The next business day after any date.
6. Read and write dates in `dd/MM/yyyy`.

**Non-functional**
- The output must not change from one day to the next, so the report date is a fixed value.
- Month and day names must print in English on every machine.

## Read in this order

| Step | File | What to look for |
|---|---|---|
| 1 | [`data/transactions.csv`](data/transactions.csv) | Weekend rows, a month boundary, one impossible date. |
| 2 | [`Transaction.java`](src/main/java/com/codechanakya/reports/Transaction.java) | Date and time kept as `LocalDate` and `LocalTime`. |
| 3 | [`TransactionReader.java`](src/main/java/com/codechanakya/reports/TransactionReader.java) | `parse` and the exception for a bad date. |
| 4 | [`Calendar.java`](src/main/java/com/codechanakya/reports/Calendar.java) | Formatters, weekends, next business day. |
| 5 | [`DailyReport.java`](src/main/java/com/codechanakya/reports/DailyReport.java) | `TemporalAdjusters`, `YearMonth`, `TreeMap` keyed by dates. |
| 6 | [`Main.java`](src/main/java/com/codechanakya/reports/Main.java) | Ten short demonstrations. |

## Run it

```bash
./run.sh            # compile and run
./run.sh --check    # run, then compare with expected-output.txt
```

Run it from this folder (`run.sh` does that for you). The program reads its sample data from the `data` folder next to it, and writes anything it creates under `out/work`, which `run.sh` clears on every run, so the repository stays clean.

Expected output:

```text
Read 11 transactions for the report dated Fri 06 Mar 2026
  Skipped line 12: bad date or time "2026-13-40 10:00"

--- Net movement per day ---
  Fri 27 Feb 2026   1 tx     5000.00
  Sat 28 Feb 2026   1 tx    -1200.00  (weekend)
  Sun 01 Mar 2026   1 tx     -300.00  (weekend)
  Mon 02 Mar 2026   3 tx     3149.25
  Tue 03 Mar 2026   1 tx     -999.99
  Wed 04 Mar 2026   2 tx     3800.00
  Thu 05 Mar 2026   1 tx     1000.00
  Fri 06 Mar 2026   1 tx      600.00

--- Per week (weeks start on Monday) and per month ---
  Week of Mon 23 Feb 2026: 3500.00
  Week of Mon 02 Mar 2026: 7549.26
  2026-02 has 28 days, net 3800.00
  2026-03 has 31 days, net 7249.26

--- Times of day ---
  After 22:00: ACC-1 at 23:50 on 2026-03-01
  After 22:00: ACC-2 at 22:40 on 2026-03-04
  Monday 2 March was busy for 8 h 55 min

--- Distances between dates ---
  First 2026-02-27, last 2026-03-06
  Days between: 7
  As a period : 0 month(s) and 7 day(s)
  Days from the first transaction to the report date: 7
  Report date is after the last transaction: false

--- Settlement: the next business day ---
  Thu 26 Feb 2026 -> Fri 27 Feb 2026
  Fri 27 Feb 2026 -> Mon 02 Mar 2026
  Sun 01 Mar 2026 -> Mon 02 Mar 2026

--- Formatting and parsing ---
  "05/03/2026" read as day/month/year: 2026-03-05, THURSDAY
  Back to text: 05/03/2026

--- Dates that surprise people ---
  31 Jan 2026 plus one month: 2026-02-28 (clamped to the last valid day)
  28 Feb 2028 plus one day  : 2028-02-29 (2028 is a leap year: true)
  End of March 2026 is a TUESDAY
  30 Feb 2026 is refused: Invalid date 'FEBRUARY 30'
  Parsing 05/03/2026 with no formatter fails: DateTimeParseException
```

## Pick the right class

| Class | Holds | Example | Use it for |
|---|---|---|---|
| `LocalDate` | a date | 2026-03-02 | birthdays, due dates, statement days |
| `LocalTime` | a time of day | 09:10 | opening hours, cut-offs |
| `LocalDateTime` | both, no zone | 2026-03-02T09:10 | what a wall clock said at a branch |
| `YearMonth` | a month | 2026-03 | monthly totals |
| `Instant` | a point on the timeline | 2026-03-02T03:40:00Z | exact moments (Program 26) |
| `Duration` | exact time | PT8H55M | "8 hours 55 minutes" |
| `Period` | calendar amount | P1M7D | "1 month 7 days" |

"Local" means **no time zone**. It is not a place, it is the absence of one. That is the right choice for a statement date and the wrong choice for an event that people in different countries attend (Program 26).

## Immutable: every change gives a new object

```java
dueDate.plusDays(7);                       // result thrown away; dueDate is unchanged
LocalDate extended = dueDate.plusDays(7);  // keep the result
```

All `java.time` classes are immutable, like `String`. This is the mistake that costs most beginners an hour: [`broken/ImmutableDate.java`](broken/README.md). A bonus is that they are safe to share between threads and as map keys.

There are no constructors either. You create dates with `of`, `parse`, `now` and `from`: [`broken/NewLocalDate.java`](broken/README.md).

## Formatting and parsing

```java
DateTimeFormatter.ofPattern("EEE dd MMM yyyy", Locale.ENGLISH)
```

| Letter | Means | Careful |
|---|---|---|
| `yyyy` | year | |
| `MM`, `MMM` | month as 03, or Mar | **capital** M |
| `mm` | minute | lowercase m is not the month: [`broken/WrongPatternLetters.java`](broken/README.md) |
| `dd` | day of month | |
| `EEE` | day name, Mon | |
| `HH` | hour, 24-hour clock | `hh` is the 12-hour clock |

- `LocalDate.parse("2026-03-02")` needs no pattern, because that is the ISO layout. `LocalDate.parse("05/03/2026")` fails; give it `ofPattern("dd/MM/yyyy")`. Is `05/03/2026` the 5th of March or the 3rd of May? The pattern is how you say.
- **Pass a `Locale`.** Without one, `EEE` and `MMM` print in the computer's language. The same program would say `Mon` on one machine and a different word on another.

## Distances

| You want | Use | Result |
|---|---|---|
| days between two dates | `ChronoUnit.DAYS.between(a, b)` | a number |
| years, months and days | `Period.between(a, b)` | `P1M7D` |
| hours and minutes between two times | `Duration.between(a, b)` | `PT8H55M` |

`Duration` measures exact time, so it does not apply to plain dates (see Program 26's `DurationBetweenDates`). Use `Period` or `ChronoUnit` for dates.

## The calendar has traps

- **Clamping.** 31 January plus one month is 28 February, the last valid day. Java does not throw and does not roll into March.
- **Leap years.** `LocalDate.of(2028, 2, 28).plusDays(1)` is 29 February, and `Year.isLeap` tells you why.
- **Impossible dates** such as 30 February throw `DateTimeException`, so a bad row in a file is caught rather than stored.
- **Weeks.** Which day starts a week is a convention. This report uses Monday with `TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)`.

## Why the report date is fixed

`LocalDate.now()` asks the computer's clock. A program that calls it everywhere gives different answers each day and cannot be tested. Here the date is passed in as a value. Real code often takes a `java.time.Clock` instead, and tests give it a frozen one.

## Common mistakes

| Mistake | What you see |
|---|---|
| Ignoring the result of `plusDays` | the date never changes |
| `mm` for the month | an exception, or the minutes printed |
| `new LocalDate(...)` | compile error: private constructor |
| `Duration.between` on two dates | `UnsupportedTemporalTypeException` |
| Formatting with no `Locale` | text in the machine's language |
| Calling `now()` inside logic | untestable, changing output |

## Practice

- [`exercises/`](exercises/README.md): six tasks.
- [`solutions/`](solutions/README.md): one answer to each.

## Verification

Compiled and run with OpenJDK 21.0.13 on Linux. `./run.sh --check` passes. The solution file also compiles and runs. Not yet run on Java 25, Windows or macOS.

## Next

Program 26 is the Phase 6 project. It adds time zones, daylight saving and saving data to a file.
