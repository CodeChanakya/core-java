# Break it on purpose

Each file here is a classic mistake from menus and input handling. Compile it, run it, and work out why it behaves that way.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `FallThrough.java`: a missing `break`

```bash
javac FallThrough.java
java FallThrough
```

```text
Balance
Deposit
Withdraw
```

`choice` is 1, yet three lines print. An old-style `case` does not stop at the end of its block. It keeps running into the next case until it meets `break`. The arrow form used in `AtmSimulator` cannot do this.

## 2. `ForgottenNext.java`: the bad word is never consumed

```bash
javac ForgottenNext.java
echo abc | java ForgottenNext
```

```text
Not a number, try again
Not a number, try again
Not a number, try again
Not a number, try again
Not a number, try again
```

The input `abc` is not a number, so `hasNextInt()` is false. The loop prints a message but never reads `abc`, so the very same word is checked again. This copy stops after 5 tries so it cannot hang. Without that counter it would repeat forever. The fix is `scanner.next();` inside the loop.

## 3. `WrongStringCompare.java`: `==` on text

```bash
javac WrongStringCompare.java
echo yes | java WrongStringCompare
```

```text
Not confirmed
```

You typed `yes`, but the program says "Not confirmed". `==` on Strings asks whether two names point to the very same object, not whether the text matches. Use `answer.equals("yes")`. Program 7 explains this in depth.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*
