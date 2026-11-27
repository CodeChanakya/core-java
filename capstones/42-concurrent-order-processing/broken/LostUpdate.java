/** BROKEN: two threads add one to a shared counter. Each reads, thinks, then writes. */
public class LostUpdate {
    static int counter = 0;

    static void addOne() throws InterruptedException {
        int seen = counter;        // read
        Thread.sleep(100);         // "think": the other thread reads in the meantime
        counter = seen + 1;        // write, based on what was read a moment ago
    }

    public static void main(String[] args) throws Exception {
        Thread a = new Thread(() -> { try { addOne(); } catch (InterruptedException e) { } });
        Thread b = new Thread(() -> { try { addOne(); } catch (InterruptedException e) { } });
        a.start(); b.start();
        a.join(); b.join();
        System.out.println("Two threads each added 1 to 0. Expected 2, got " + counter);
    }
}
