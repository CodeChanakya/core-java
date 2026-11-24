# Exercises: Plugin Discovery Demonstration

Work in a copy so the original stays intact:

```bash
cp -r 17-plugin-discovery-demonstration ../my-plugin-discovery-demonstration
cd ../my-plugin-discovery-demonstration
./run.sh
```

Edit the files under `src/main/java` in your copy and run `./run.sh` after each change. The compiler is your first reviewer; read its messages.

Try each task before opening a hint. The hints get stronger one at a time.

## Beginner

**B1.** Add a new plugin class `GreetingPlugin` in `builtin`, and list it in `plugins.txt`. Do you change any other file?

<details><summary>Hint</summary>Copy `AuditPlugin`, rename it and change the message. Add its full name to the text file.</details>

**B2.** Set `enabled = false` to `true` on `ExperimentalPlugin` and run again.

<details><summary>Hint</summary>It moves from the skipped list into the running order.</details>

**B3.** Change some order numbers and predict the new running order before you run.

<details><summary>Hint</summary>Within a phase, the smaller number runs first. A phase always beats the order number.</details>

## Intermediate

**I1.** Add `String description() default ""` to `PluginInfo` and print it next to each plugin's name.

<details><summary>Hint</summary>New elements need a default, or every existing plugin stops compiling.</details>

**I2.** A plugin with no no-argument constructor currently stops loading with an exception. Make the loader skip it with a reason instead.

<details><summary>Hint</summary>Call `type.getDeclaredConstructor()` early and catch `NoSuchMethodException`.</details>

## Challenge

**C1.** Add a second annotation `@Command(label = "...")` for **methods**, and make `Main` call every annotated method of each plugin after `run()`.

<details><summary>Hint 1</summary>Give it `@Target(ElementType.METHOD)` and `RUNTIME` retention.</details>
<details><summary>Hint 2</summary>Loop over `getDeclaredMethods()`, check `method.getAnnotation(Command.class)`, then call `method.invoke(plugin)`.</details>

When you are done, compare with the matching files under [`../solutions/src`](../solutions/src).
