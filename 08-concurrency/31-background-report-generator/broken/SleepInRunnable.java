/** A Runnable cannot throw a checked exception. */
public class SleepInRunnable {

    public static void main(String[] args) {
        Thread worker = new Thread(() -> {
            Thread.sleep(100);             // BUG: sleep() throws InterruptedException, run() cannot
            System.out.println("Woke up.");
        });
        worker.start();
    }
}
