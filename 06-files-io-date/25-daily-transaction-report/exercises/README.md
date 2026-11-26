# Exercises: Daily Transaction Report

Work in a copy so the original stays intact:

```bash
cp -r 25-daily-transaction-report ../my-daily-transaction-report
cd ../my-daily-transaction-report
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Print the weekday of the first day of the report date's month.

<details><summary>Hint</summary>`reportDate.withDayOfMonth(1).getDayOfWeek()`.</details>

**B2.** Count how many transactions happened on a weekend.

<details><summary>Hint</summary>`Calendar.isWeekend` already exists.</details>

**B3.** Print the daily dates in the `dd/MM/yyyy` layout.

<details><summary>Hint</summary>`Calendar.BRANCH_FORM` is ready to use.</details>

## Intermediate

**I1.** Add `lastBusinessDay(YearMonth)` to `Calendar`. Try February, March and May 2026.

<details><summary>Hint</summary>Start at `atEndOfMonth()` and step back with `minusDays(1)` while it is a weekend.</details>

**I2.** Add `busiestDay()` to `DailyReport`.

<details><summary>Hint</summary>Count with `merge` into a `TreeMap` and keep the date with the highest count.</details>

## Challenge

**C1.** Add `settlementDate(LocalDateTime)`: before 17:00 on a business day it settles on the next business day. After 17:00, or on a weekend, it settles one business day later than that.

<details><summary>Hint 1</summary>Work out the day the bank treats the transaction as **received** first.</details>
<details><summary>Hint 2</summary>Settlement is the business day after the received day.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

Run the solution version with `./solutions/run.sh` from this program's folder. It needs the `data` folder, so it runs from here.
