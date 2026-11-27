import java.util.concurrent.CompletableFuture;

/**
 * thenApply wraps whatever the function returns. If the function already returns a
 * CompletableFuture, you get a future inside a future. thenCompose flattens it.
 */
public class ThenApplyNesting {

    static CompletableFuture<String> lookUpName(int id) {
        return CompletableFuture.supplyAsync(() -> "customer-" + id);
    }

    public static void main(String[] args) {
        CompletableFuture<Integer> id = CompletableFuture.supplyAsync(() -> 42);

        CompletableFuture<String> name = id.thenApply(n -> lookUpName(n));   // BUG: should be thenCompose

        System.out.println(name.join());
    }
}
