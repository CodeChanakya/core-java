# Break it on purpose

Each file here has exactly one mistake. Compile it, read the message, fix the file, and compile again. Learning to read compiler messages is half of learning Java.

Run these from inside this `broken` folder:

```bash
javac MissingSemicolon.java
```

(This writes `.class` files next to the source. Delete them when you are done.)

## 1. `MissingSemicolon.java`

```text
MissingSemicolon.java:6: error: ';' expected
        System.out.println("Name : " + name)
                                            ^
1 error
```

The caret `^` points to the spot just after the last character of the line. Java expected a `;` there. Add it.

## 2. `WrongClassName.java`

```text
WrongClassName.java:2: error: class ProfilePrinter is public, should be declared in a file named ProfilePrinter.java
public class ProfilePrinter {
       ^
1 error
```

A `public` class must live in a file with the same name. Either rename the class to `WrongClassName` or rename the file.

## 3. `MissingQuote.java`

```text
MissingQuote.java:5: error: unclosed string literal
        System.out.println("Name : Priya Nair);
                           ^
1 error
```

The text starts at the opening quote and never ends. Add the closing `"` before the bracket.

## 4. `LowercaseSystem.java`

```text
LowercaseSystem.java:5: error: package system does not exist
        system.out.println("Java cares about capital letters");
              ^
1 error
```

Java treats `system` and `System` as different names. The message is confusing because Java guesses you meant a package. Capitalise the `S`.

## 5. `MisspelledMain.java`

This one compiles. The mistake only shows up when you run it:

```bash
javac MisspelledMain.java
java MisspelledMain
```

```text
Error: Main method not found in class MisspelledMain, please define the main method as:
   public static void main(String[] args)
or a JavaFX application class must extend javafx.application.Application
```

The compiler cannot know you meant `main`, because `mian` is a perfectly valid method name. The JVM looks for `main` at start-up and cannot find it. Newer Java versions may word this message a little differently.

This is the difference between a **compile-time error** (found by `javac`, before anything runs) and a **run-time error** (found while the program is running).

*The messages above were captured from a real run with OpenJDK 21.0.13.*
