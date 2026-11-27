# Exercises: Automated Business Logic Tests

Work in a copy so the original stays intact:

```bash
cp -r 36-automated-business-logic-tests ../my-automated-business-logic-tests
cd ../my-automated-business-logic-tests
./run.sh
```

Edit the files under `src` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**A1.** Add a test to `OrderPricerTest`: a gold customer ordering 400 with `FLAT50` gets a coupon discount of 50.00. Work out first, on paper, why the coupon applies.

<details><summary>Hint</summary>The tier discount is 40, so the coupon sees 360. Is 360 at least 300?</details>

**A2.** Add a `@ValueSource` test to `CouponServiceTest` that proves `FREE`, `welcome`, `WELCOME 10` and `FLAT-50` are all unknown coupons.

<details><summary>Hint</summary>`@ValueSource(strings = {...})` and `assertThrows(IllegalArgumentException.class, ...)`.</details>

**A3.** Run only one test class. With Maven: `mvn -Dtest=CouponServiceTest test`. With the script, add `--select-class com.codechanakya.pricing.CouponServiceTest` instead of `--scan-class-path` in `run.sh`.

<details><summary>Hint</summary>The console launcher help lists every option: `java -jar lib/junit-platform-console-standalone-1.11.4.jar --help`.</details>

## Intermediate

**B1.** Test first. Write a test that says a parcel over 20 kg (20,000 g) is rejected with the message `Parcel is too heavy: 20001 g (maximum 20000)`, and that exactly 20,000 g costs 440.00. Watch it fail. Then change `ShippingCalculator` until it passes.

<details><summary>Hint 1</summary>Add `MAXIMUM_GRAMS` and an `if` before the kilogram calculation.</details>
<details><summary>Hint 2</summary>A failing test that fails for the right reason is progress. Check that the failure message is about the missing rule, not a typo.</details>

**B2.** Rewrite the `WELCOME10` tests with `@MethodSource`: a static method returning `Stream<Arguments>` with three subtotals and their expected discounts.

<details><summary>Hint</summary>`Arguments.of("50", "5.00")`. The method must be `static`.</details>

## Challenge

**C1.** Mutation game. In `ShippingCalculator`, change `>= 0` to `> 0` in the free-shipping check, and run the tests. Which tests fail? Undo it. Then change `TAX_RATE` in `OrderPricer` to `0.19`. Which fail? If a change makes no test fail, write the test that would have caught it.

<details><summary>Hint</summary>The boundary rows (`999.00`) catch the first. The totals in `OrderPricerTest` catch the second.</details>

**C2.** Use `@Nested` in `OrderPricerTest` to group the express-delivery tests. The group has its own `@BeforeEach` that prices a 2000 order with express delivery, and two tests that read the result.

<details><summary>Hint</summary>A nested class is a non-static inner class marked `@Nested`. The outer `@BeforeEach` still runs first.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).
