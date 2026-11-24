# 01 · Personal Profile Generator

**Phase 01 · Getting Started** &nbsp;|&nbsp; **Level:** Beginner &nbsp;|&nbsp; **Java:** 21 or newer

Concepts: compiling and running, `main`, `System.out.println`, text and whole-number values.

---

## The problem

You want a program that introduces a person: it knows a few facts about them and prints those facts as a neat card in the terminal.

It is the smallest useful program in Java. Before you can build a bank or an online shop, you need to see one program go from a text file to something running.

## Read in this order

| Step | File | What to look for |
|---|---|---|
| 1 | [`src/main/java/PersonalProfile.java`](src/main/java/PersonalProfile.java) | The whole program. Only about 15 lines do real work. |
| 2 | [`expected-output.txt`](expected-output.txt) | What a correct run prints. |
| 3 | [`run.sh`](run.sh) | The two commands that compile and run it. |

## Run it

You need a JDK (version 21 or newer). Check with `java -version` and `javac -version`.

```bash
./run.sh            # compile and run
./run.sh --check    # run, then compare with expected-output.txt
```

Without the script, those are two commands:

```bash
javac -d out src/main/java/PersonalProfile.java   # source code -> bytecode
java -cp out PersonalProfile                      # the JVM runs the bytecode
```

Expected output:

```text
==============================
  PERSONAL PROFILE
==============================
Name     : Priya Nair
Age      : 29
City     : Pune
Role     : QA engineer
Learning : Java
==============================
```

## What happens when you run it

```text
PersonalProfile.java  --javac-->  PersonalProfile.class  --java-->  text on screen
   (you write this)              (bytecode, made by javac)         (the JVM runs it)
```

1. `javac` reads your source file and checks it. If it finds a mistake, it stops and prints an error. No `.class` file is made.
2. If the code is fine, `javac` writes `PersonalProfile.class` into the `out` folder. That file is bytecode, not machine code for your computer.
3. `java` starts the JVM, loads the class, and calls its `main` method. The JVM turns the bytecode into instructions your computer understands. That is why the same `.class` file runs on Windows, macOS and Linux.

## Every symbol, one by one

The first line of `main` that stores something:

```java
String name = "Priya Nair";
```

| Piece | Meaning |
|---|---|
| `String` | The *type*: this value is text. |
| `name` | The *name* you chose for the value. You will use it later to get the value back. |
| `=` | Store the value on the right under the name on the left. It does not mean "equals" here. |
| `"Priya Nair"` | A text value. The double quotes mark where it starts and ends. They are not part of the text. |
| `;` | Ends the statement, the way a full stop ends a sentence. |

The line that prints:

```java
System.out.println("Name     : " + name);
```

| Piece | Meaning |
|---|---|
| `System.out` | The standard output, which is your terminal. |
| `.println` | "Print this, then start a new line." |
| `( ... )` | What to print goes between the brackets. |
| `+` | With text on either side, joins the pieces into one longer piece of text. |

The line that starts the program:

```java
public static void main(String[] args) {
```

You do not need to understand every word yet. Here is what each one is for, so none of it feels like magic:

| Word | Why it is there |
|---|---|
| `public` | Anyone, including the JVM, is allowed to call this. |
| `static` | The JVM can call it without creating an object first. Phase 3 explains why. |
| `void` | It does its work and gives nothing back. |
| `main` | The name the JVM looks for when it starts your program. |
| `String[] args` | A place for words typed after the program name. This program ignores them. |

## What changes if you change the code

| Change | What happens |
|---|---|
| Remove the `;` at the end of a line | Compile error: `';' expected`. |
| Change `"Pune"` to `"Mumbai"` | The City line prints `Mumbai`. |
| Remove the `+ age` part and write `"Age      : "` | The line prints without the number. |
| Rename `name` to `fullName` on one line only | Compile error: the other line no longer finds `name`. |
| Rename the class but not the file | Compile error. See `broken/WrongClassName.java`. |

## What is in memory

When `main` starts, Java sets aside a small workspace for it. Each variable name labels something in that workspace:

```text
main's workspace
+-----------+-----------------------+
| name      | -> "Priya Nair"       |   text lives elsewhere; name points to it
| age       | 29                    |   a whole number is stored right here
| city      | -> "Pune"             |
+-----------+-----------------------+
```

You will see exactly why text and numbers behave differently in Phase 2.

## Common mistakes

| Mistake | What you see |
|---|---|
| Forgetting a `;` | `error: ';' expected` |
| A missing closing `"` | `error: unclosed string literal` |
| Writing `system` instead of `System` | `error: package system does not exist` |
| Class name that does not match the file name | `error: class X is public, should be declared in a file named X.java` |
| Misspelling `main` | The program compiles, then fails at run time with `Main method not found` |

Try all five yourself in [`broken/`](broken/README.md).

## Practice

- [`exercises/`](exercises/README.md) has six tasks: three beginner, two intermediate, one challenge.
- [`broken/`](broken/README.md) has five files with one mistake each.
- [`solutions/`](solutions/MyProfile.java) has one answer to all six exercises. Try them first.

## Verification

Compiled and run with OpenJDK 21.0.13 on Linux. `./run.sh --check` passes. The solution file also compiles and runs. Not yet run on Java 25, Windows or macOS.

## Next

Program 2, the Employee Salary Calculator, does real arithmetic with the same kind of variables you just used.
