import java.util.ArrayDeque;
import java.util.Queue;

/** push() belongs to Deque (the stack end), not to the Queue interface. */
public class QueuePushMissing {

    public static void main(String[] args) {
        Queue<String> line = new ArrayDeque<>();
        line.push("first");
        System.out.println(line);
    }
}
