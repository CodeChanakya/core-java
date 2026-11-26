# Exercises: Appointment Scheduler

Work in a copy so the original stays intact:

```bash
cp -r 26-appointment-scheduler ../my-appointment-scheduler
cd ../my-appointment-scheduler
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Book a 30-minute appointment on 11 March at 09:00 London time, then look at the New York agenda.

<details><summary>Hint</summary>Use `ZonedDateTime.of(2026, 3, 11, 9, 0, 0, 0, LONDON)`.</details>

**B2.** Print the end of every appointment.

<details><summary>Hint</summary>`getEnd()` is already there.</details>

**B3.** Print how long it is from the stand-up to the retro, in days and hours.

<details><summary>Hint</summary>`Duration.between(a.getStart(), b.getStart())`, then `toDays()` and `toHoursPart()`.</details>

## Intermediate

**I1.** Add `on(LocalDate, ZoneId)` returning the appointments that start on that date on that zone's clocks.

<details><summary>Hint</summary>Convert each start with `withZoneSameInstant(zone)` before taking `toLocalDate()`.</details>

**I2.** Add `cancel(String title)` that returns whether something was removed.

<details><summary>Hint</summary>Use an `Iterator`, as in Program 18.</details>

## Challenge

**C1.** Add `firstFreeSlot(ZonedDateTime from, Duration length)`.

<details><summary>Hint 1</summary>Start with `from` as the candidate time.</details>
<details><summary>Hint 2</summary>Walk the sorted appointments; whenever one overlaps the candidate, move the candidate to that appointment's end.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).

Run the solution version with `./solutions/run.sh` from this program's folder. It needs the `data` folder, so it runs from here.
