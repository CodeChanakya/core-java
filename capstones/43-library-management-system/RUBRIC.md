# Rubric (100 points)

Marking a solo build fairly means being clear about what earns points. Forty points are awarded by the machine,
sixty by a person reading your code. Both are listed here so there are no surprises.

| Area | Points | How it is marked |
|---|---|---|
| Acceptance tests | 40 | `bash run.sh --grade` in `starter/`: 40 × (tests passed ÷ tests found) |
| Design | 15 | Read by a person |
| Your own tests | 15 | Read by a person |
| Code quality | 10 | Read by a person |
| Error handling | 10 | Read by a person |
| Documentation | 5 | Read by a person |
| Stretch | 5 | Read by a person |

## Design (15)

- **13 to 15:** Rules live in `LibraryService`. Storage is behind `LibraryStore`. Classes have one clear job, and
  nothing in `Main` decides a rule.
- **8 to 12:** Mostly sound, with one rule leaking into the wrong class or one class doing two jobs.
- **0 to 7:** One large class or method, rules repeated in several places, or `Main` holding the logic.

## Your own tests (15)

At least ten tests, in classes not ending in `AcceptanceTest` (those are the given ones and do not count).

- **13 to 15:** Ten or more tests that check things the acceptance tests do not (edge cases, bad input), with names
  that say what they check. Each test fails when the rule it covers is broken.
- **8 to 12:** Ten or more tests, but mostly repeating the given ones, or names like `test1`.
- **0 to 7:** Fewer than ten, or tests that cannot fail.

## Code quality (10)

- **9 to 10:** Clear names, short methods, no copy-pasted blocks, no unused code, no magic numbers (limits and
  rates are named constants).
- **5 to 8:** Readable, with some long methods or repeated blocks.
- **0 to 4:** Hard to follow.

## Error handling (10)

- **9 to 10:** Every refusal throws `LibraryException` with a message that says what went wrong and why. Nothing
  is swallowed. No `null` returned where an exception or an empty list is the honest answer.
- **5 to 8:** Correct exceptions, but vague messages, or one swallowed case.
- **0 to 4:** Generic exceptions, empty catch blocks, or `null` returns.

## Documentation (5)

- **5:** A README that says how to run it, what the design is, and what you would do next. Javadoc on the public
  methods you wrote.
- **3 to 4:** A README or Javadoc, not both.
- **0 to 2:** Neither.

## Stretch (5)

Points for any exercise in [exercises/README.md](exercises/README.md), scaled by how well it is done and tested.

## What does not earn points

Passing tests by special-casing their data, copying the reference solution, or editing the given tests. The
grading run uses the original test files.
