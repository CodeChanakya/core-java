import java.util.ArrayList;
import java.util.List;

/**
 * Two "tests" that share one static list. Each passes when run first, and the second fails when run after
 * the first. JUnit makes a NEW test object for every test method so that instance fields cannot leak
 * like this, but static fields can.
 */
public class SharedStateBetweenTests {

    static final List<String> basket = new ArrayList<>();

    static void testAddingOneItem() {
        basket.add("pen");
        check("basket has one item", basket.size() == 1);
    }

    static void testBasketStartsEmpty() {
        check("basket starts empty", basket.isEmpty());
    }

    static void check(String name, boolean ok) {
        System.out.println((ok ? "PASS  " : "FAIL  ") + name);
    }

    public static void main(String[] args) {
        System.out.println("order 1:");
        testBasketStartsEmpty();
        testAddingOneItem();

        basket.clear();
        System.out.println("order 2:");
        testAddingOneItem();
        testBasketStartsEmpty();
    }
}
