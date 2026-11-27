# Solutions

One worked answer to every exercise that changes code, kept in the same folder layout as the program so you can compare file by file.

| Exercise | Where |
|---|---|
| A1 | `AccountRepository.findByMinBalance`; `Main` prints accounts with 500.00 or more. |
| B1 | `TransferService.closeAccount`; `Main` tries to close account 3 before and after it is emptied. |
| C1 | `TransferService.transferAll` and the `Move` record; `Main` shows a failed batch and a working one. |

A2 and A3 need no new code, and B2 is the same pattern as `findByOwnerPrefix` with `%` on both sides of the value.

Run the solution version (run `../run.sh` once first so the driver is downloaded):

```bash
./run.sh
```

With the 100 sent to Dev, account 3 is empty and can be closed. In the failed batch, the first leg had already moved 100 out of account 1 when the second leg failed; the output shows account 1 and account 5 back at their old balances.

Each change is marked with the exercise number in a comment, for example `// C1`. Your own answer can look different and still be right.
