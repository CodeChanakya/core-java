# Exercises: Enterprise Banking System

Do these in order. Each one touches a different layer. Run `./run.sh --test` after every change.

1. **Per-type daily limit.** Savings accounts may withdraw at most 25,000 a day, current accounts 50,000.
   Hint: put the limit on `AccountType`, next to `floor()`. Update the message and the test that checks it.
2. **A monthly fee.** Charge 100.00 to every current account holding less than 5,000. Add `FEE` to
   `TransactionKind`. Decide what happens if the fee would pass the overdraft limit. Hint: skip that account.
3. **Statement between two dates.** Add `history(accountId, from, to)` to `BankService`. Both ends are
   included. Hint: one stream with a filter. Test the day before, the first day, the last day and the day after.
4. **Close an account.** Add `close(id)`. An account can only be closed when its balance is exactly zero.
   Decide whether the history stays. Hint: a new `closed` column, and `find` must not return closed accounts.
5. **A third store.** Write `CsvBankStore` that keeps everything in a file. Make `BankStoreContract` pass
   on it by adding a third subclass. You will find out which rules you had forgotten.
6. **Break the database rule.** Remove the `balance_above_floor` CHECK from `Schema`. Which tests fail?
   Now remove the check in `InMemoryBankStore` instead. What does that tell you about where a rule should live?

Stuck? Solutions for tasks 2 and 3 are in [`../solutions`](../solutions/README.md). Try first.
