# Solutions

One worked answer to every exercise that changes code, kept in the same folder layout as the program.

| Exercise | Where |
|---|---|
| A1 | `OrderPricerTest.flatCouponAfterGoldDiscount` |
| A2 | `CouponServiceTest.nearMisses` |
| B1 | `ShippingCalculatorTest.tooHeavy` and the new rule in `ShippingCalculator.java` |
| B2 | `CouponServiceTest.welcomeFromMethodSource` |
| C2 | `OrderPricerTest.Express`, a `@Nested` class with its own `@BeforeEach` |

A3 and C1 have no code. For A3, `mvn -Dtest=CouponServiceTest test` runs one class. For C1, the first change fails the `999.00` shipping case, and the second changes the totals in both `OrderPricerTest` tests that check a total.

To run the solution version, copy it over a spare copy of the program and run the tests:

```bash
cp -r 36-automated-business-logic-tests ../try-solution
cp -r ../try-solution/solutions/src/. ../try-solution/src/
cd ../try-solution
./run.sh --check
```

Each change is marked with the exercise number in a comment, for example `// B1`. Your own answer can look different and still be right.
