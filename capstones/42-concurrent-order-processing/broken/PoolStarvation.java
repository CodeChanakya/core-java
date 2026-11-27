import java.util.concurrent.*;

/** BROKEN: a task that waits for another task from the same one-thread pool. */
public class PoolStarvation {
    public static void main(String[] args) throws Exception {
        // A daemon thread, so this demonstration can end even though that thread is stuck for good.
        ExecutorService pool = Executors.newSingleThreadExecutor(task -> {
            Thread thread = new Thread(task);
            thread.setDaemon(true);
            return thread;
        });
        CompletableFuture<Integer> outer = CompletableFuture.supplyAsync(() -> {
            CompletableFuture<Integer> inner = CompletableFuture.supplyAsync(() -> 21, pool);
            return inner.join() * 2;     // waits for a thread, but the only thread is the one waiting
        }, pool);
        try {
            System.out.println("Result: " + outer.get(1, TimeUnit.SECONDS));
        } catch (TimeoutException e) {
            System.out.println("Nothing happened for one second. The only thread is waiting for itself.");
        }
        pool.shutdownNow();
    }
}
