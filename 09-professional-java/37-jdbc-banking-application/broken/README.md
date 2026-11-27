# Break it on purpose

Each file here is a classic JDBC mistake. They run against the same Derby jars, which `../run.sh` downloads into `../lib`. On Windows (cmd), write the class path as `".;..\lib\*"`.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `AutoCommitSurprise.java`: a transfer with auto-commit left on

```bash
CP=".:../lib/*"
javac -cp "$CP" AutoCommitSurprise.java
java -cp "$CP" AutoCommitSurprise
```

```text
auto-commit is true
a later step failed: The check constraint 'NOT_NEGATIVE' was violated while performing an INSERT or UPDATE on table '"APP"."ACCOUNT"'.
money in the bank: 150 (it was 200)
```

The first update was permanent the moment it ran. When the third statement failed, nothing undid the first, so 50 disappeared from the bank. `TransferService` turns auto-commit off and rolls back, which is exactly what stops this.

## 2. `ResultSetBeforeNext.java`: reading a row before calling next()

```bash
CP=".:../lib/*"
javac -cp "$CP" ResultSetBeforeNext.java
java -cp "$CP" ResultSetBeforeNext
```

```text
SQLState 24000: Invalid cursor state - no current row.
```

A `ResultSet` points before the first row when it is returned. Call `next()` first, and use its `true` or `false` to know whether a row exists. `AccountRepository.find` does `rows.next() ? ... : Optional.empty()`.

## 3. `ParameterIndexZero.java`: parameters count from 1, not 0

```bash
CP=".:../lib/*"
javac -cp "$CP" ParameterIndexZero.java
java -cp "$CP" ParameterIndexZero
```

```text
SQLState XCL13: The parameter position '0' is out of range.  The number of parameters for this prepared  statement is '1'.
```

Arrays start at 0 and JDBC starts at 1, for parameters and for columns. The error names the position and tells you how many parameters the statement has, which is the quickest way to find a miscount.

*The output above was captured from a real run with OpenJDK 21.0.13 and Apache Derby 10.16.1.1. Other versions may word some messages a little differently.*
