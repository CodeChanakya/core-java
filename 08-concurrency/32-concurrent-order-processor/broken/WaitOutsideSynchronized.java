/** wait() and notify() may only be called by a thread that holds the object's monitor. */
public class WaitOutsideSynchronized {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();

        // BUG: this thread does not own lock's monitor, so wait() is refused.
        lock.wait(10);
    }
}
