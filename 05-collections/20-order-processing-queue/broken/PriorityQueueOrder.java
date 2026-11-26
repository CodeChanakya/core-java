import java.util.PriorityQueue;
import java.util.Queue;

/** A PriorityQueue only promises the next item out is the smallest, not that printing is sorted. */
public class PriorityQueueOrder {

    public static void main(String[] args) {
        Queue<Integer> queue = new PriorityQueue<>();
        for (int n : new int[] {5, 1, 4, 2, 3}) {
            queue.add(n);
        }
        System.out.println("Printed whole: " + queue);

        StringBuilder order = new StringBuilder();
        while (!queue.isEmpty()) {
            order.append(queue.poll()).append(' ');
        }
        System.out.println("Taken out    : " + order.toString().trim());
    }
}
