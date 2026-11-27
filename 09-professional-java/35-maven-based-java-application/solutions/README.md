# Solutions

One worked answer to the exercises that change code, kept in the same folder layout as the program so you can compare file by file.

| Exercise | Where |
|---|---|
| A1 | `src/main/resources/catalog.csv` has a new last line. `Main` quotes four staplers. |
| A2 | `QuoteCalculator.java` has the 15 percent tier. `Main` prices three keyboards (4497, so 15 percent). |
| B1 | `Catalog.java` has `byPrice()`. `Main` prints the list. |
| B2 | `QuoteItem.java` refuses more than 100. `Main` shows the message. |

A3 has no code: the answer is `quote-calculator-1.1.0.jar`.

**C1.** The jar lists `catalog.csv` at its root, next to `com/`. Resources are copied to the root of the classpath, so `getResourceAsStream("/catalog.csv")` finds it inside the jar exactly as it does on disk.

**C2.** With filtering on, Maven replaces `${project.version}` while it copies the file, so the program prints `1.1.0` (or whatever the version is). `run.sh` copies without replacing, so the program prints the text `${project.version}` unchanged. That difference is the main reason to let Maven build the project.

Run the solution version:

```bash
./run.sh
```

Each change is marked with the exercise number in a comment, for example `// B2`. Your own answer can look different and still be right.
