# Exercises: Vehicle Rental System

Work in a copy so the original stays intact:

```bash
cp -r 11-vehicle-rental-system ../my-vehicle-rental-system
cd ../my-vehicle-rental-system
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add another `Car` to the fleet in `Main` and quote it for 4 days.

<details><summary>Hint</summary>`new Car(registration, model, seats)`, then `agency.addVehicle(...)`.</details>

**B2.** Print how many seats the Innova has. The agency only knows it as a `Vehicle`, so what must you check first?

<details><summary>Hint</summary>`instanceof Car car` gives you a `Car` variable with `getSeats()`.</details>

**B3.** Change the long-rental discount to start from 5 days. Which of the quotes in `Main` change?

<details><summary>Hint</summary>The constant is in `Vehicle`. Only quotes of 5 days or more are affected.</details>

## Intermediate

**I1.** Add a `Bicycle` class at 150.00 a day, with no insurance.

<details><summary>Hint</summary>It needs `dailyRate()` and `kind()`. It does not implement `Insurable`.</details>

**I2.** Add `cheapestFor(int days)` to `RentalAgency`, returning the vehicle with the lowest total for that many days.

<details><summary>Hint</summary>Use `totalFor`, not just `rentalCost`, so insurance counts.</details>

## Challenge

**C1.** Make `Truck` refuse rentals longer than 30 days, without copying the discount rule.

<details><summary>Hint</summary>Override `rentalCost`, check the days, then return `super.rentalCost(days)`.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).
