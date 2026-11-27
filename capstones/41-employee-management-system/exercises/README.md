# Exercises: Employee Management System

1. **Half-day leave.** Allow a request for half a working day (0.5). Think about the type: `int days` no longer
   fits. What should `balance` return?
2. **Probation reminders.** Add `probationEndingWithin(days)`: active employees whose probation ends in the next
   N days, soonest first. Test today, the last day, and one day too far.
3. **Work anniversaries.** Add `anniversariesIn(YearMonth)`. Decide what happens to someone who joined on 29 February.
4. **A different week.** Some countries work Sunday to Thursday. Make `WorkCalendar` take the set of weekend days
   as a constructor argument. Which tests must you change, and which should pass untouched?
5. **Time zones.** The company has an office in Pune and one in New York. A leave request says "starting today".
   Which `today`? Add a `ZoneId` to the employee and use it.
6. **Carry over.** Let up to five unused annual days carry into the next year. Where does the rule belong?
   Write the failing test first.

Solutions for tasks 2 and 3 are in [`../solutions`](../solutions/README.md). Try first.
