import java.util.ArrayList;
import java.util.List;

/**
 * count++ is three steps (read, add, write). Four threads doing it at once lose updates.
 * The exact number changes from run to run, which is what makes races so hard to debug.
 */
public class UnsafeCounter {

    private static int count = 0;                 // shared, and nothing protects it

    public static void main(String[] args) throws InterruptedException {
        for (int attempt = 1; attempt <= 10; attempt++) {
            count = 0;
            List<Thread> threads = new ArrayList<>();
            for (int t = 0; t < 4; t++) {
                Thread thread = new Thread(() -> {
                    for (int i = 0; i < 100_000; i++) {
                        count++;                  // BUG: not atomic
                    }
                });
                threads.add(thread);
                thread.start();
            }
            for (Thread thread : threads) {
                thread.join();
            }
            if (count != 400_000) {
                System.out.println("Attempt " + attempt + ": expected 400000 but got " + count);
                System.out.println("Updates lost: " + (400_000 - count) + " (your number will differ)");
                return;
            }
        }
        System.out.println("No update was lost this time. Run it again; a race is a matter of luck.");
    }
}
