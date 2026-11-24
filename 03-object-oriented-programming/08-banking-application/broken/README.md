# Break it on purpose

Each file here is a classic first mistake with classes. Each defines its own small `Account`, so it compiles on its own.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `PrivateAccess.java`: reaching into a private field

```bash
javac PrivateAccess.java
```

```text
PrivateAccess.java:8: error: balance has private access in Account
        account.balance = new BigDecimal("1000000.00");
               ^
PrivateAccess.java:9: error: balance has private access in Account
        System.out.println("Balance: " + account.balance);
                                                ^
2 errors
```

The error is the whole point of `private`: code outside the class is not allowed to touch the field. The fix is not to make the field public. Add a method such as `deposit` that checks the amount, and call that.

## 2. `ForgottenThis.java`: a parameter hides the field

```bash
javac ForgottenThis.java
java ForgottenThis
```

```text
Holder : null
Balance: null
```

The program compiles and runs without complaint, and prints `null` twice. Inside the constructor, `holder` means the parameter, so `holder = holder;` assigns the parameter to itself and the field is never set. Write `this.holder = holder;`.

## 3. `NullReference.java`: a variable with no object

```bash
javac NullReference.java
java NullReference
```

```text
Exception in thread "main" java.lang.NullPointerException: Cannot invoke "Account.deposit(java.math.BigDecimal)" because "<local1>" is null
	at NullReference.main(NullReference.java:9)
```

`findAccount` found nothing and returned `null`. Calling a method on `null` throws a `NullPointerException`. The message names the line. Check for `null` before using a result that can be missing, as `Bank.find` warns in its comment.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*
