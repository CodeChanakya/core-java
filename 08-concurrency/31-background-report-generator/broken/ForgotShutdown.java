import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Worker threads of a pool keep the JVM alive until the pool is shut down. */
public class ForgotShutdown {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        pool.submit(() -> 21 * 2);                    // a task that finishes at once

        // BUG: no pool.shutdown(). The two worker threads are still alive, waiting for more work.
        System.out.println("main() has finished, but the program has not ended.");

        // So this demonstration can end by itself, a watchdog stops the JVM after one second.
        // A real program would simply hang here until you pressed Ctrl+C.
        Thread watchdog = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
                return;
            }
            System.out.println("One second later the JVM is still running. Stopping it by force.");
            System.exit(0);
        });
        watchdog.setDaemon(true);
        watchdog.start();
    }
}
