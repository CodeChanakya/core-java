# Break it on purpose

Compile and run each file from this folder with the Derby jars on the class path
(after one `./run.sh` they are in `../lib`):

```
javac --release 21 -d /tmp/out AutoCommitTransfer.java
java -cp "/tmp/out:../lib/derby-10.16.1.1.jar:../lib/derbyshared-10.16.1.1.jar" AutoCommitTransfer
```

## AutoCommitTransfer

A transfer written as three separate statements, with auto-commit left on.

```
The transfer failed: 23513
Total money in the bank (should be 1000.00): 700.00
```

The first statement took 300 from account 1 and was saved at once. The later ones failed, and nothing
undid the first. 300 has vanished. The fix is `setAutoCommit(false)`, then `commit` at the end and
`rollback` on any failure. That is what `JdbcBankStore.post` does.

## DoubleForMoney

```
Balance after ten deposits of 0.10: 0.9999999999999999
Is it exactly 1.0? false
```

A `double` cannot hold 0.10 exactly. Over thousands of transactions the errors add up and the books stop
balancing. The fix is `BigDecimal` with a fixed scale, as in `Account`.

## ConcatenatedSql

Searching for a customer called `nobody' OR 'a'='a`, by gluing the text into the SQL:

```
Looked up 'nobody' and got: Asha 500.00
Looked up 'nobody' and got: Bilal 900.00
```

Nobody is called that, and yet every row came back. The fix is a `PreparedStatement` with `?`, so the text
is only ever data. `JdbcBankStoreTest.injectionIsHarmless` guards it.
