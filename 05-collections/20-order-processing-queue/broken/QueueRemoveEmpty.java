import java.util.ArrayDeque;
import java.util.Queue;

/** remove() on an empty queue throws; poll() would have returned null. */
public class QueueRemoveEmpty {

    public static void main(String[] args) {
        Queue<String> line = new ArrayDeque<>();
        System.out.println("poll gives: " + line.poll());
        System.out.println("remove gives: " + line.remove());
    }
}
