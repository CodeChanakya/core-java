# Break it on purpose

Each file needs only the JDK:

```
javac --release 21 -d /tmp/out InvalidDate.java
java -cp /tmp/out InvalidDate
```

## InvalidDate

```
LocalDate.of(2025, 2, 29) failed: Invalid date 'February 29' as '2025' is not a leap year
parse("2025-13-01") failed: Text '2025-13-01' could not be parsed: Invalid value for MonthOfYear (valid values 1 - 12): 13
A leap-day birthday in 2025 becomes: 2025-02-28
```

Java refuses dates that do not exist. Catch `DateTimeException` where dates come from people, and show the
message. Note the last line: adding a year to a leap day does not fail, it moves to 28 February. That is why
`WorkCalendar.nextBirthday` is tested on 29 February.

## DurationVersusPeriod

```
Start:             2025-03-08T12:00
Plus 24 hours:     2025-03-09T13:00
Plus 1 day:        2025-03-09T12:00
```

New York set its clocks forward that night, so the day after was only 23 hours long. A `Duration` is an exact
amount of time and a `Period` is a calendar amount. For "the same time tomorrow", use a `Period`.

## MonthDrift

```
31 Jan, one month, one month: 2025-03-28
31 Jan, two months at once:   2025-03-31
```

One month after 31 January is 28 February, and the lost days never come back. When you mean "N months from
the start", add them in one step, as `probationEnds` does with `plusMonths(6)`.
