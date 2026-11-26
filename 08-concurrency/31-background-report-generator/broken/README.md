# Break it on purpose

Each file here breaks one rule of threads and executors. Run them from this folder. The shutdown demonstration stops itself after a second.

Run these from inside this `broken` folder. `javac` writes `.class` files next to the source, so delete them when you are done.

## 1. `StartVsRun.java`: run() instead of start()

```bash
javac StartVsRun.java
java StartVsRun
```

```text
Calling run():
  The job runs on thread: main
Calling start():
  The job runs on thread: report-worker
```

`run()` is an ordinary method call, so the job ran on `main`. Only `start()` creates a thread. The thread name in the output is the proof.

## 2. `ForgotShutdown.java`: a pool that is never shut down

```bash
javac ForgotShutdown.java
java ForgotShutdown
```

```text
main() has finished, but the program has not ended.
One second later the JVM is still running. Stopping it by force.
```

The pool's worker threads are still alive and waiting for work, so the JVM cannot exit. A real program would hang here until you pressed Ctrl+C; this demonstration has a watchdog that stops it after a second. Always shut the pool down, or use `try-with-resources`.

## 3. `SleepInRunnable.java`: a checked exception in a Runnable

```bash
javac SleepInRunnable.java
```

```text
SleepInRunnable.java:6: error: unreported exception InterruptedException; must be caught or declared to be thrown
            Thread.sleep(100);             // BUG: sleep() throws InterruptedException, run() cannot
                        ^
1 error
```

`Runnable.run()` cannot throw `InterruptedException`. Catch it inside the lambda, or use a `Callable`, whose `call()` is allowed to throw.

*The output above was captured from a real run with OpenJDK 21.0.13. Other Java versions may word some messages a little differently.*
