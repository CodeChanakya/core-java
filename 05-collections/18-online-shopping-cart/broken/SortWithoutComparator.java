import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Sorting needs to know how to compare two items. */
public class SortWithoutComparator {

    static class Item {
        String name;
        Item(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();
        items.add(new Item("Notebook"));
        items.add(new Item("Cable"));
        Collections.sort(items);
        System.out.println("Sorted");
    }
}
