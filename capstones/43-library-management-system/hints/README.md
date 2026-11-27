# Hints

Read one hint at a time, and only after you have been stuck for twenty minutes. None of them contains code.

**Hint 1: where to start.** Run `bash run.sh --grade` and read the first failing test name. Make that one pass,
then run again. Do not try to write the whole service at once.

**Hint 2: the order.** Catalogue first (add, find, search), then borrowing, then returns, then renewals, then
reservations, then reports. Each group of tests only needs the one before it.

**Hint 3: reading the rules.** The rules live in the Javadoc of each method in `LibraryService`. When a test
fails, look for the sentence in the Javadoc it checks. The test names also describe the rule.

**Hint 4: dates.** Always use the injected `Clock`, never `LocalDate.now()`. The tests move time forward, so a
direct call to the system clock makes late-return tests fail.

**Hint 5: money.** Use `BigDecimal` with scale 2. Compare with `compareTo`, not `equals`, because `2.0` and
`2.00` are not equal under `equals`.

**Hint 6: the 50 limit.** More than 50.00 in unpaid fines blocks borrowing. Exactly 50.00 does not. A test pins
down this boundary.

**Hint 7: the queue.** When a book has reservations, only the person at the head may borrow it. Renewing is also
refused while anyone is waiting.

**Hint 8: errors.** Throw `LibraryException` with a message a person could read. Tests check the type, and the
rubric checks that the message helps.

**Hint 9: your own tests.** Write at least ten, in classes whose names do not end in `AcceptanceTest`. Test
things the given tests do not: unknown ids, empty strings, a member with no loans.
