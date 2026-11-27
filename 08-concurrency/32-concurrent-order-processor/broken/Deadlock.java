import java.lang.management.ManagementFactory;
import java.util.concurrent.CountDownLatch;

/**
 * Two threads, two locks, opposite order. A latch makes sure each thread holds its first lock
 * before either asks for its second, so the deadlock happens every time.
 */
public class Deadlock {

    public static void main(String[] args) throws InterruptedException {
        Object accountA = new Object();
        Object accountB = new Object();
        CountDownLatch bothHoldOne = new CountDownLatch(2);

        Thread aToB = new Thread(() -> {
            synchronized (accountA) {                         // holds A ...
                bothHoldOne.countDown();
                await(bothHoldOne);
                synchronized (accountB) {                     // ... and waits for B
                    System.out.println("a-to-b finished");
                }
            }
        }, "a-to-b");

        Thread bToA = new Thread(() -> {
            synchronized (accountB) {                         // holds B ...
                bothHoldOne.countDown();
                await(bothHoldOne);
                synchronized (accountA) {                     // ... and waits for A
                    System.out.println("b-to-a finished");
                }
            }
        }, "b-to-a");

        aToB.setDaemon(true);                                 // so this demo can still exit
        bToA.setDaemon(true);
        aToB.start();
        bToA.start();

        aToB.join(1000);                                      // give them a second; they never finish
        System.out.println("a-to-b is " + aToB.getState());
        System.out.println("b-to-a is " + bToA.getState());

        long[] stuck = ManagementFactory.getThreadMXBean().findDeadlockedThreads();
        System.out.println("Threads the JVM reports as deadlocked: " + (stuck == null ? 0 : stuck.length));
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
