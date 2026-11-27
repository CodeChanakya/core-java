# Solutions

`src/` is a full copy of the project with exercises 2 and 3 solved, and their tests added.

- **Exercise 2, monthly fee:** `BankService.chargeMonthlyFees()`, plus `FEE` in `TransactionKind`. An account that
  could not afford the fee without passing its overdraft limit is skipped, so the fee never causes a refusal.
- **Exercise 3, history between dates:** `BankService.history(id, from, to)`, one stream with a filter that
  keeps both ends.

```
./run.sh --test      # 38 tests: the original 36 and two new ones
```

Exercises 1, 4, 5 and 6 are left for you. They are the more interesting ones.
