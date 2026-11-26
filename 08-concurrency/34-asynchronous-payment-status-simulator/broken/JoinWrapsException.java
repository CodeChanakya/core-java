import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * An exception thrown inside a CompletableFuture stage does not come out as itself.
 * join() wraps it in a CompletionException, and the real problem is the cause.
 */
public class JoinWrapsException {

    public static void main(String[] args) {
        CompletableFuture<String> payment = CompletableFuture.supplyAsync(() -> {
            throw new IllegalStateException("card declined");
        });

        try {
            payment.join();
        } catch (CompletionException e) {
            System.out.println("What you catch : " + e.getClass().getSimpleName());
            System.out.println("Its message    : " + e.getMessage());
            System.out.println("The real cause : " + e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage());
        }
    }
}
