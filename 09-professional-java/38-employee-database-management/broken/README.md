# Break it on purpose

Each file here shows what happens at the edges of the program: the database rules and Java's own. They run against the Derby jars that `../run.sh` downloads into `../lib`. On Windows (cmd), write the class path as `".;..\lib\*"`.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `DuplicateEmailInDatabase.java`: a second copy in a UNIQUE column

```bash
CP=".:../lib/*"
javac -cp "$CP" DuplicateEmailInDatabase.java
java -cp "$CP" DuplicateEmailInDatabase
```

```text
SQLState 23505
The statement was aborted because it would have caused a duplicate key value in a unique or primary key constraint or unique index identified by 'EMAIL_UNIQUE' defined on 'EMPLOYEE'.
```

`EmployeeService` checks `existsByEmail` first, so a duplicate normally gets a clear `DuplicateEmailException`. The `UNIQUE` rule in the table is the second line of defence: two people hiring at the same moment could both pass the check. `JdbcEmployeeRepository.translate` turns SQLState `23505` into the same `DuplicateEmailException`, so the caller sees one kind of error whichever layer caught it.

## 2. `SalaryCheckConstraint.java`: a salary the table refuses

```bash
CP=".:../lib/*"
javac -cp "$CP" SalaryCheckConstraint.java
java -cp "$CP" SalaryCheckConstraint
```

```text
SQLState 23513
The check constraint 'SALARY_POSITIVE' was violated while performing an INSERT or UPDATE on table '"APP"."EMPLOYEE"'.
```

The service refuses a salary below 15000 long before SQL is sent. If another program, or a future bug, skips the service, the `CHECK` rule still stops a zero or negative salary. Rules that must never be broken belong in both places.

## 3. `RecordIsImmutable.java`: assigning to a record field

```bash
javac RecordIsImmutable.java
```

```text
RecordIsImmutable.java:9: error: cannot assign a value to final variable salary
        asha.salary = 95000;
            ^
1 error
```

The fields of a record are final. To change a salary you build a new `Employee` with `withSalary(...)` and pass it to `repository.update`. That is why a raise can never half happen on an object that other code is still holding.

*The output above was captured from a real run with OpenJDK 21.0.13 and Apache Derby 10.16.1.1. Other versions may word some messages a little differently.*
