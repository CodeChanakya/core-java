# Exercises: Employee Directory

Work in a copy so the original stays intact:

```bash
cp -r 21-employee-directory ../my-employee-directory
cd ../my-employee-directory
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add a sixth employee in the Support department.

<details><summary>Hint</summary>`directory.add(new Employee(...))`.</details>

**B2.** Add `countIn(String department)` returning the head count of one department, 0 for an unknown one.

<details><summary>Hint</summary>`headCount().getOrDefault(department, 0)`.</details>

**B3.** Add `ids()` returning just the ids, in joining order.

<details><summary>Hint</summary>`new ArrayList<>(byId.keySet())`.</details>

## Intermediate

**I1.** Add `renameDepartment(String from, String to)` that moves everyone and returns how many moved. `Employee` has no setters; what do you do?

<details><summary>Hint 1</summary>Replace each person with a new `Employee` under the same id using `put`.</details>
<details><summary>Hint 2</summary>Loop over a **copy** of the values, so the loop is safe.</details>

**I2.** Add `largestDepartment()`.

<details><summary>Hint</summary>Loop over `headCount().entrySet()` and remember the biggest.</details>

## Challenge

**C1.** Write a `Badge` class (site and number) that works as a `HashMap` key, and prove two equal badges find the same value.

<details><summary>Hint 1</summary>Both fields `final`.</details>
<details><summary>Hint 2</summary>`equals` compares both fields; `hashCode` is `Objects.hash(site, number)`.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).
