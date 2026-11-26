# 24 · Log File Analyzer

**Phase 06 · Files, I/O and Date** &nbsp;|&nbsp; **Level:** Intermediate &nbsp;|&nbsp; **Java:** 21 or newer

Concepts: `java.nio.file`: `Path`, `Files`, `DirectoryStream`, glob patterns, `StandardCopyOption`.

---

## The problem

A service writes a log file per day. Someone has to answer, every morning: how many errors, which component is complaining most, and where did the first error of each file happen? The files are plain text, one event per line, and some lines are not events at all.

This is the same file work as Program 23, with the modern toolkit: `Path` for locations and `Files` for actions.

<!--flow-->
## The flow at a glance

<p align="center"><a href="../../docs/flows/program-24.svg"><img alt="Flow of Program 24, Log File Analyzer, in 5 steps. The steps are listed in text below the picture." src="../../docs/flows/program-24.svg" width="760"></a></p>

| If you are | Read it like this |
|---|---|
| New to coding | A detective reading yesterday's diary: scan every .log file and tell me where it hurt. |
| Learning | Follow the numbered cards from top to bottom. Open the file named on each card, run the program, and match its output to the last card. |
| Experienced | Path and Files throw precise exceptions. Sorting directory results removes the dependence on file-system order. |

<details>
<summary>Text version of the flow</summary>

1. **Find the .log files** (input): DirectoryStream with a glob, sorted. (`LogFolder.java`)
2. **Read each file** (work): Files.readAllLines gives the lines. (`Files`)
3. **Is the line an event?** (check): split with a limit; valueOf for a safe level. (`LogParser.java`)
4. **Count by level and component** (work): EnumMap and TreeMap with merge. (`LogReport.java`)
5. **Errors, noisy component, first error** (result): Printed with / in file names, the same everywhere. (`expected-output.txt`)

</details>
<!--/flow-->

## Requirements

**Functional**
1. Find every `*.log` file in `data/logs`, ignoring other files.
2. Parse each line into date, time, level, component and message. Count lines that cannot be parsed.
3. Count lines per level and errors per component, and note the first error in each file.
4. Write a summary file, move it to an archive folder, copy it, and delete the copy.
5. Report a missing file clearly.

**Non-functional**
- Output must not depend on the order the operating system lists a folder in.
- Output must be the same on Windows and Linux.

## Read in this order

| Step | File | What to look for |
|---|---|---|
| 1 | [`data/logs/`](data/logs) | Two log files, one note file. |
| 2 | [`Level.java`](src/main/java/com/codechanakya/logs/Level.java), [`LogEntry.java`](src/main/java/com/codechanakya/logs/LogEntry.java) | An enum and a plain data class. |
| 3 | [`LogParser.java`](src/main/java/com/codechanakya/logs/LogParser.java) | `split` with a limit, and `valueOf` for a safe conversion. |
| 4 | [`LogReport.java`](src/main/java/com/codechanakya/logs/LogReport.java) | `EnumMap`, `TreeMap`, `merge`. |
| 5 | [`LogFolder.java`](src/main/java/com/codechanakya/logs/LogFolder.java) | `DirectoryStream` with a glob. |
| 6 | [`Main.java`](src/main/java/com/codechanakya/logs/Main.java) | `Path` and `Files` in use. |

## Run it

```bash
./run.sh            # compile and run
./run.sh --check    # run, then compare with expected-output.txt
```

Run it from this folder (`run.sh` does that for you). The program reads its sample data from the `data` folder next to it, and writes anything it creates under `out/work`, which `run.sh` clears on every run, so the repository stays clean.

Expected output:

```text
--- A short tour of Path ---
  resolve        : data/logs/app-2026-03-01.log
  getFileName    : app-2026-03-01.log
  getParent      : data/logs
  relativize     : app-2026-03-01.log
  normalize      : data/logs
  isAbsolute     : false
  exists / isDir : true / true

--- Finding the log files ---
  app-2026-03-01.log
  app-2026-03-02.log
  (notes.txt is ignored by the *.log pattern)

--- One file at a time ---
  app-2026-03-01.log: 9 lines, 2 errors, 1 unreadable
  app-2026-03-02.log: 7 lines, 2 errors, 1 unreadable

--- All files together ---
  Lines read: 16 (unreadable: 2)
  DEBUG 1
  INFO  7
  WARN  2
  ERROR 4
  Errors by component: {AuthService=1, OrderService=2, PaymentService=1}
  First error in each file:
    app-2026-03-01.log line 4 at 09:17:03: Payment declined ref=P-1002 reason=INSUFFICIENT_FUNDS
    app-2026-03-02.log line 3 at 08:06:01: Stock check failed id=O-78 sku=H-300

--- Writing, moving and deleting ---
  Summary written: 6 lines
  After move, old path exists: false, new path exists: true
  Copy has the same lines: true
  Deleted the copy: true
  Deleting it again: false

--- A missing file ---
  Caught NoSuchFileException for data/logs/app-2026-03-03.log

Errors were about 28 percent of readable lines.
```

## java.io against java.nio.file

| | `java.io.File` (Program 23) | `java.nio.file` (this program) |
|---|---|---|
| Location | `File` | `Path` |
| Actions | methods on `File`, many return `false` on failure | `Files.*` methods that **throw** with a clear reason |
| Read a whole text file | a loop with `BufferedReader` | `Files.readAllLines(path)` |
| Copy, move, delete | copy by hand; `renameTo` and `delete` return `boolean` | `Files.copy`, `Files.move`, `Files.delete` |
| Error detail | "something went wrong" | `NoSuchFileException`, `DirectoryNotEmptyException`, `AccessDeniedException` |

New code should use `Path` and `Files`. You still meet `File` in older code, and `Path.toFile()` and `File.toPath()` convert between the two.

## Path is a value, not a file

`Path.of("data", "logs")` builds a location. It does not check that the place exists. The tour at the top of the output shows the common operations:

| Call | Meaning |
|---|---|
| `resolve("a.log")` | join: this path, then the name |
| `getFileName()`, `getParent()` | the last part, and everything before it |
| `relativize(other)` | the route from this path to the other |
| `normalize()` | remove `.` and `..` |
| `isAbsolute()` | starts from the root of the disk? |

`resolve` has a trap. If the argument is already absolute, it **replaces** the path instead of joining: [`broken/ResolveAbsolute.java`](broken/README.md). That matters when the second part comes from user input.

## Reading and writing with Files

```java
List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
Files.write(summary, lines, StandardCharsets.UTF_8);
```

- `readAllLines` loads the whole file into memory. Fine for kilobytes, a bad idea for a log of several gigabytes. For big files, read line by line with `Files.newBufferedReader`, or use `Files.lines` (a lazy stream, covered with Phase 7).
- `Files.write` **replaces** the file by default. To add to it, pass `StandardOpenOption.APPEND`. Forgetting this loses data silently: [`broken/OverwriteByDefault.java`](broken/README.md).
- Writing with `Files.write` ends each line with the local line separator. The program prints line counts and not byte sizes, so the output is identical on every system.

## Finding files

```java
try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.log")) {
    for (Path path : stream) { found.add(path); }
}
Collections.sort(found);
```

- The second argument is a **glob**: `*.log` means "anything ending in .log". `notes.txt` is not returned.
- A `DirectoryStream` keeps the folder open, so it is closed with try-with-resources.
- The operating system lists files in whatever order it likes. The explicit sort makes the program predictable. Without it, "first error in each file" could come out in a different order on another machine.

## Moving, copying, deleting

```java
Files.createDirectories(archive);                                   // every missing folder; silent if present
Files.move(summary, moved, StandardCopyOption.REPLACE_EXISTING);
Files.copy(moved, copy, StandardCopyOption.REPLACE_EXISTING);
Files.deleteIfExists(copy);                                          // true if it deleted, false if nothing was there
```

Without `REPLACE_EXISTING`, moving onto an existing file throws `FileAlreadyExistsException`: the safe default.

## Parsing a line

`line.trim().split("\\s+", 5)` splits on runs of spaces, **at most five pieces**. The fifth piece is the whole rest of the line, so a message with spaces stays in one piece. A line like `this line is not a log entry` has too few pieces to be an entry and is counted as unreadable, not as a crash. A level the enum does not know (the `TRACE` line) makes `Level.valueOf` throw `IllegalArgumentException`, which `LogParser` turns into `null`. Exercise I1 adds `TRACE` and the unreadable count drops by one.

## Common mistakes

| Mistake | What you see |
|---|---|
| `Files.write` when you meant to append | earlier content gone |
| Passing a `String` where a `Path` is expected | compile error |
| `resolve` with an absolute second path | the first part silently ignored |
| Trusting the order of a directory listing | output differs between machines |
| `readAllLines` on a huge file | `OutOfMemoryError` |
| Printing a `Path` and comparing with `/` | `\` on Windows |

## Practice

- [`exercises/`](exercises/README.md): six tasks.
- [`solutions/`](solutions/README.md): one answer to each.

## Verification

Compiled and run with OpenJDK 21.0.13 on Linux. `./run.sh --check` passes. The solution file also compiles and runs. Not yet run on Java 25, Windows or macOS.

## Next

Program 25 turns to dates and times: the `java.time` classes that every report and every appointment depends on.
