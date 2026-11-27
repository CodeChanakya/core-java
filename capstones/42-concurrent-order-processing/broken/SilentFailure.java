import java.util.concurrent.*;

/** BROKEN: an exception inside an async task is not thrown anywhere. It waits inside the future. */
public class SilentFailure {
    public static void main(String[] args) throws Exception {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            throw new IllegalStateException("payment gateway exploded");
        });
        Thread.sleep(300);
        System.out.println("The task failed 300 ms ago, and nothing was printed.");
        System.out.println("isCompletedExceptionally: " + future.isCompletedExceptionally());
        try {
            future.join();
        } catch (CompletionException e) {
            System.out.println("join() threw: " + e.getClass().getSimpleName());
            System.out.println("The real cause is inside it: " + e.getCause());
        }
    }
}
