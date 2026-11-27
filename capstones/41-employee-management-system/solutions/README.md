# Solutions

`src/` is a full copy of the project with exercises 2 and 3 solved, and their tests added.

- **Exercise 2:** `HrService.probationEndingWithin(days)` uses `WorkCalendar.probationEnds` for each active employee
  and keeps the ones whose end date falls from today to today plus N days.
- **Exercise 3:** `HrService.anniversariesIn(month)` keeps employees who joined in an earlier year in the same month,
  and sorts by the day of the month in the target year. `withYear` moves 29 February to 28 February in a year without one.

```
./run.sh --test      # 65 tests: the original 63 and two new ones
```

Exercises 1, 4, 5 and 6 are left for you.
