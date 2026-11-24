# Break it on purpose

These three files are tests with a classic flaw. They are kept out of `src/` so they do not break the lab's own run.

To try one, copy it into the test folder and run the lab:

```bash
cp broken/BigDecimalScaleTest.java src/test/java/com/codechanakya/bank/
./run.sh
rm src/test/java/com/codechanakya/bank/BigDecimalScaleTest.java   # tidy up
```

The behaviour below was checked with a stand-in for the JUnit API; the exact wording of real JUnit's report may differ slightly. The failing values come from `BigDecimal` and Java itself, not from JUnit.

## 1. `BigDecimalScaleTest.java`: equal numbers, different scale

The test expects `new BigDecimal("1000")` and the account holds `1000.00`. The check fails with a message of the form:

```text
expected: <1000> but was: <1000.00>
```

`BigDecimal.equals` treats a different scale as a different value. Write the expected value as `new BigDecimal("1000.00")`, or compare with `compareTo`.

## 2. `MissingTestAnnotationTest.java`: the test that never runs

`withdrawWorks` has no `@Test`. It also expects the wrong answer (999.00), yet the suite passes, because JUnit does not see the method at all. Compare the number of tests in the summary with the number you wrote. If a test has been green forever and you never watched it fail, be suspicious.

## 3. `SharedStateTest.java`: tests that depend on each other

Both tests use one `static` account and never reset it. If the deposit test runs first, the withdraw test sees 150 and expects 70: it fails. If the withdraw test runs first, both pass. JUnit does not promise an order, so the result depends on luck and can change between Java versions. The fix is `@BeforeEach` creating a fresh account for every test.
