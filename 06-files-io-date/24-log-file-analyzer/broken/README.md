# Break it on purpose

Each file here is a classic mistake with `Path` and `Files`. The first output was captured on Linux; Windows handles a rooted path a little differently.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `ResolveAbsolute.java`: joining onto an absolute path

```bash
javac ResolveAbsolute.java
java ResolveAbsolute
```

```text
Relative name  : data/logs/app.log
Absolute name  : /var/log/app.log
```

`resolve` is not string concatenation. When the argument is absolute, it wins and the first part disappears. If a name comes from a user or a file, check it is not absolute before resolving it against a trusted folder.

## 2. `OverwriteByDefault.java`: writing replaces the file

```bash
javac OverwriteByDefault.java
java OverwriteByDefault
```

```text
File now holds: [Tuesday: 2 errors]
```

The second `Files.write` did exactly what it is documented to do: create or truncate. To keep the old content, pass `StandardOpenOption.APPEND` (and `CREATE` if the file might not exist yet).

## 3. `StringInsteadOfPath.java`: a String where a Path belongs

```bash
javac StringInsteadOfPath.java
```

```text
StringInsteadOfPath.java:9: error: incompatible types: String cannot be converted to Path
        List<String> lines = Files.readAllLines("data/logs/app-2026-03-01.log");
                                                ^
Note: Some messages have been simplified; recompile with -Xdiags:verbose to get full output
1 error
```

The `Files` methods take a `Path`, so a bare string is a compile error. Build one with `Path.of("data/logs/app-2026-03-01.log")`.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*
