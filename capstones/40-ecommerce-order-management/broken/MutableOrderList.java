import java.util.ArrayList;
import java.util.List;

/** BROKEN: a record that hands out its own list lets anyone change the order. */
public class MutableOrderList {
    record Order(int id, List<String> lines) { }

    public static void main(String[] args) {
        List<String> lines = new ArrayList<>(List.of("BK-101 x2"));
        Order order = new Order(1, lines);
        System.out.println("Placed: " + order);
        lines.add("EL-202 x5");   // someone else still holds the list
        System.out.println("Later: " + order);
        System.out.println("Nobody called a method on the order, yet it changed.");
    }
}
