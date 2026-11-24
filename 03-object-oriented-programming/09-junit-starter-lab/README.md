# 09 · JUnit Starter Lab

**Phase 03 · Object-Oriented Programming** &nbsp;|&nbsp; **Level:** Intermediate &nbsp;|&nbsp; **Java:** 21 or newer

Concepts: unit tests, JUnit 5, `@Test`, `@BeforeEach`, `@DisplayName`, assertions, `assertThrows`, arrange-act-assert, `BigDecimal` in tests.

---

## The problem

In Program 8 you checked the bank by running `Main` and reading the output. That works for six lines. It does not work for sixty rules, or when a teammate changes `withdraw` and you must find out what they broke.

A **unit test** is a small program that calls one method with known input and checks the result. You run all of them in one go, and they tell you in seconds whether anything that used to work has stopped working.

## What you will build

Nine tests for the `Account` class from Program 8 (the file is copied here so this folder runs on its own). They cover the happy path, the refusals, rounding, and the constructor's exceptions.

## Read in this order

| Step | File | What to look for |
|---|---|---|
| 1 | [`Account.java`](src/main/java/com/codechanakya/bank/Account.java) | The class under test, unchanged from Program 8. |
| 2 | [`AccountTest.java`](src/test/java/com/codechanakya/bank/AccountTest.java) | Read one test top to bottom: set up, act, assert. |
| 3 | [`run.sh`](run.sh) | How the tests are compiled and run without Maven. |

## Run it

```bash
./run.sh            # compile, run every test, show the full report
./run.sh --check    # same, but a short pass or fail (used by CI)
```

The first run downloads one file, the JUnit console launcher, into a `lib/` folder next to the script, so you need an internet connection once. `lib/` is not committed to Git.

A passing run ends with a summary that says every test was successful and none failed. A failing run names the test, the expected value and the actual value. The exact layout and timings come from JUnit and vary between versions, so they are not pasted here.

## Where the files go

```text
09-junit-starter-lab/
├── src/
│   ├── main/java/com/codechanakya/bank/Account.java      the real code
│   └── test/java/com/codechanakya/bank/AccountTest.java  its tests
```

Tests live beside the code in a parallel `test` folder, in the **same package**. That lets a test see anything package-private, and keeps the two easy to find from each other. Test code is never shipped to users.

## Anatomy of a test

```java
@Test
@DisplayName("deposit adds to the balance")
void depositAddsToBalance() {
    boolean accepted = account.deposit(new BigDecimal("250.50"));   // act

    assertTrue(accepted);                                           // assert
    assertEquals(new BigDecimal("1250.50"), account.getBalance());  // assert
}
```

| Part | Meaning |
|---|---|
| `@Test` | Tells JUnit "run this method". Without it nothing runs and nothing complains. |
| `@DisplayName` | A readable sentence for the report. The method name is for the compiler. |
| Arrange | Build what the test needs. Here the shared setup is in `@BeforeEach`. |
| Act | Do **one** thing. |
| Assert | State what must be true. The first failing assertion stops the test. |

## `@BeforeEach`: every test starts clean

```java
@BeforeEach
void createAccount() {
    account = new Account("ACC-1", "Asha Menon", new BigDecimal("1000.00"));
}
```

JUnit creates a new instance of the test class for each test and runs this first. The deposit test cannot leak into the withdraw test. Tests that share state pass or fail depending on the order they happen to run in, which is one of the most frustrating bugs in testing. See [`broken/SharedStateTest.java`](broken/README.md).

## The assertions used here

| Call | Passes when |
|---|---|
| `assertEquals(expected, actual)` | the two are equal. **Expected comes first.** |
| `assertTrue(x)` / `assertFalse(x)` | the boolean is true / false |
| `assertThrows(Type.class, () -> ...)` | the code throws that exception. It returns the exception, so you can check its message. |

Putting `expected` first matters only for the failure message. If you swap them, a failure reads "expected: the real value but was: the value you hoped for", which sends you in the wrong direction.

## `BigDecimal` in tests

```java
assertEquals(new BigDecimal("1000"), account.getBalance());   // FAILS
```

`BigDecimal.equals` compares the **scale** as well as the value, so `1000` and `1000.00` are different. Two fixes:

- Write the expected value with the scale the code promises: `new BigDecimal("1000.00")`. Used throughout this lab.
- When only the number matters, compare with `compareTo`: `assertEquals(0, expected.compareTo(actual))`. The rounding test does this.

## Writing a good test

- **One idea per test**, named for the behaviour, not the method: "withdrawing more than the balance is refused".
- **Test the edges**: exactly the balance, one paisa over, zero, negative.
- **A test is code too.** Keep it short and obvious. If you need a comment to understand it, simplify it.
- **A test that cannot fail is worthless.** Break the code on purpose and watch the matching test go red before you trust it.

## Common mistakes

| Mistake | What happens |
|---|---|
| Forgetting `@Test` | The test is silently skipped; the suite looks greener than it is. |
| Comparing `BigDecimal` values of different scale with `assertEquals` | A failure that looks like "1000 is not 1000.00". |
| Tests that depend on each other or on order | Passes alone, fails in the suite, or the other way round. |
| Asserting nothing | The test always passes. |

The three files in [`broken/`](broken/README.md) cover the first three.

## Practice

- [`exercises/`](exercises/README.md): six tasks, three beginner, two intermediate, one challenge. The last two ask you to write the test first and then the code (test-driven development).
- [`solutions/`](solutions/README.md): one answer to each.

## Verification

The production class compiles and runs on OpenJDK 21.0.13 on Linux, and the nine tests in `AccountTest` were run against a small stand-in for the JUnit 5 API (only the annotations and assertions used here) and all passed. **The tests have not yet been run on the real JUnit 5 jar**: the machine this course was prepared on cannot reach Maven Central to download it. `run.sh` is therefore the least-tested script in this repository. If it fails on your machine, the log will say why; please open an issue.

## Next

Program 10 returns to design, with inheritance and polymorphism. You will meet tests again in Program 36, this time with Maven.
