# Break it on purpose

Each file here is a classic mistake with annotations and reflection. Each is self-contained.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `DefaultRetention.java`: an annotation that vanishes

```bash
javac DefaultRetention.java
java DefaultRetention
```

```text
First  sees: null
Second sees: @DefaultRetention.Kept(name="demo")
```

Both classes carry an annotation, and both are read the same way. Only the one declared with `RUNTIME` retention survives into the running program. The default keeps annotations in the source and drops them before run time, so reflection sees `null` and nothing complains.

## 2. `MissingAnnotationValue.java`: a required element left out

```bash
javac MissingAnnotationValue.java
```

```text
MissingAnnotationValue.java:9: error: annotation @PluginInfo is missing a default value for the element 'name'
    @PluginInfo(order = 5)
    ^
1 error
```

`name()` has no default, so it must be supplied wherever the annotation is used. Give the element a default if it is truly optional.

## 3. `ClassNameTypo.java`: a typo the compiler cannot see

```bash
javac ClassNameTypo.java
java ClassNameTypo
```

```text
Exception in thread "main" java.lang.ClassNotFoundException: com.example.plugins.ReportPlgin
	at java.base/jdk.internal.loader.BuiltinClassLoader.loadClass(BuiltinClassLoader.java:641)
	at java.base/jdk.internal.loader.ClassLoaders$AppClassLoader.loadClass(ClassLoaders.java:188)
	at java.base/java.lang.ClassLoader.loadClass(ClassLoader.java:526)
	at java.base/java.lang.Class.forName0(Native Method)
	at java.base/java.lang.Class.forName(Class.java:423)
```

The class name is just text, so the compiler has nothing to check. The mistake appears when the program runs. `PluginLoader` catches `ClassNotFoundException` for exactly this reason and records the entry as skipped.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*
