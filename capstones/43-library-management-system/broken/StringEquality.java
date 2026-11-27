/** BROKEN: comparing text with == instead of equals. */
public class StringEquality {
    public static void main(String[] args) {
        String stored = "978-0132350884";
        String typed = new String("978-0132350884");   // the same text, but a different object, like text read from a file
        System.out.println("stored == typed:      " + (stored == typed));
        System.out.println("stored.equals(typed): " + stored.equals(typed));
    }
}
