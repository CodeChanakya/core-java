/** Calling run() is just a method call. It does not create a thread. */
public class StartVsRun {

    public static void main(String[] args) throws InterruptedException {
        Runnable job = () -> System.out.println("  The job runs on thread: " + Thread.currentThread().getName());

        Thread worker = new Thread(job, "report-worker");

        System.out.println("Calling run():");
        worker.run();                      // BUG: runs the job right here, on the main thread

        System.out.println("Calling start():");
        worker.start();                    // the right call: a new thread runs the job
        worker.join();
    }
}
