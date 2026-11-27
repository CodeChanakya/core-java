# Solutions

`src/` is a full copy of the project with exercises 2 and 3 solved, and their tests added.

- **Exercise 2:** `RetryingGateway` uses `handle` to look at the first attempt. On an exception it returns a second
  `charge`, and `thenCompose` flattens the future of a future. A decline is a `false` answer with no exception, so it passes through.
- **Exercise 3:** `Summary.countByStatus` starts every status at zero in an `EnumMap`, then counts.

```
./run.sh --test      # 22 tests: the original 17 and five new ones
```

Exercises 1, 4, 5 and 6 are left for you.
