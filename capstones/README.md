# Phase 10 · Capstones

Five systems built from everything before them. Each is a small, complete application with a database, business
rules, tests, and the documentation a teammate would need.

| No. | Program | What it adds |
|---|---|---|
| 39 | [Enterprise Banking System](39-enterprise-banking-system) | Transactions, a contract test run on two stores, a rule held in both code and database. |
| 40 | [E-commerce Order Management](40-ecommerce-order-management) | Streams for reports, strategy by interface, an enum state machine, an atomic stock check. |
| 41 | [Employee Management System](41-employee-management-system) | The date and time API doing real work, a soft delete, payslips. |
| 42 | [Concurrent Order Processing System](42-concurrent-order-processing) | `CompletableFuture`, atomic stock, timeouts, and output that stays the same however the threads run. |
| 43 | [Library Management System](43-library-management-system) | You build it alone from a brief and 48 acceptance tests, and a rubric marks it. |

## How each capstone is laid out

```
NN-name/
  README.md             the problem, the design, how to run it, what was checked
  run.sh                --check, --test, --maven
  pom.xml               the same project for Maven
  expected-output.txt   the exact output the demonstration must print
  src/main/java/...     the program
  src/test/java/...     the JUnit 5 tests
  exercises/            six tasks, easiest first
  solutions/            a full copy of the project with two of them solved
  broken/               three small programs that fail on purpose, with their real output
```

## Working through a capstone

1. Read the README's **problem** and **design** first. Do not open the code yet.
2. Run `./run.sh --check`, then read `expected-output.txt` next to `Main.java` until each line makes sense.
3. Read the tests before the code they test. They are the specification.
4. Do the exercises. Run `./run.sh --test` after every change.
5. Run the programs in `broken/` and say what is wrong before you read the explanation.

Program 43 is laid out differently, because you write it: `starter/` is the project with the answer left out, `reference/` is a finished solution, and `RUBRIC.md`, `GRADING-SHEET.md` and `hints/` replace the solutions. Program 42 needs no database.

Every capstone runs the same way on every machine: the clock is fixed, the database is in memory, and nothing is written to disk.
