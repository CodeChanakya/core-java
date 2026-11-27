import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * Asynchronous work usually runs on daemon threads: the common pool's threads are daemons on
 * any machine with two or more cores. A daemon thread never keeps the JVM alive, so when main()
 * ends the JVM exits and the unfinished work is thrown away without a word.
 *
 * <p>To make the demonstration identical on every machine, the executor below creates daemon
 * threads explicitly, which is what the common pool does for you.
 */
public class MainEndsFirst {

    public static void main(String[] args) {
        Executor daemonThreads = task -> {
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        };

        CompletableFuture.supplyAsync(() -> {
            sleep(300);                                        // a slow remote call
            return "payment confirmed";
        }, daemonThreads).thenAccept(result -> System.out.println("Async result: " + result));

        // BUG: nothing waits for the future. main() returns, the JVM exits, the line above never prints.
        System.out.println("main() finished.");
        // Fix: call .join() on the future, or give it a deadline with .get(timeout, unit).
    }

    private static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
