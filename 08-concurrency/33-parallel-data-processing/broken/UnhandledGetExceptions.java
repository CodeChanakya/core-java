import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Future.get() can throw two checked exceptions, and Java makes you decide what to do about both. */
public class UnhandledGetExceptions {

    public static void main(String[] args) {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        Future<Integer> answer = pool.submit(() -> 6 * 7);

        System.out.println("The answer is " + answer.get());   // BUG: InterruptedException and ExecutionException
        pool.shutdown();
    }
}
