import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A task that waits for another task in the same pool can wait forever, if the pool has no free
 * thread left to run the second one.
 */
public class PoolStarvation {

    public static void main(String[] args) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(1, task -> {
            Thread thread = new Thread(task);
            thread.setDaemon(true);                          // so this demo can still exit
            return thread;
        });

        Future<String> outer = pool.submit(() -> {
            // BUG: the only worker thread is busy running this task, so the inner task can never start.
            Future<String> inner = pool.submit(() -> "inner result");
            return "outer got " + inner.get();
        });

        try {
            System.out.println(outer.get(1, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            System.out.println("Nothing happened for one second: " + e.getClass().getSimpleName());
            System.out.println("The outer task is waiting for the inner task, which is waiting for a thread.");
        }
    }
}
