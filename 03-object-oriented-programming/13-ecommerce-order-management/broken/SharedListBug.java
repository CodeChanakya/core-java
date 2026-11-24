import java.util.ArrayList;

/** Two orders were handed the same list, so they now share their items. */
public class SharedListBug {

    public static void main(String[] args) {
        ArrayList<String> items = new ArrayList<>();
        Order first = new Order("ORD-1", items);
        Order second = new Order("ORD-2", items);

        first.add("Laptop Stand");
        second.add("Java Ebook");

        System.out.println(first.id + " has " + first.items);
        System.out.println(second.id + " has " + second.items);
    }
}

class Order {
    final String id;
    final ArrayList<String> items;

    Order(String id, ArrayList<String> items) {
        this.id = id;
        this.items = items;
    }

    void add(String item) {
        items.add(item);
    }
}
