/** The first matching case wins, so a general case above a specific one hides it. */
public class DominatedPattern {

    record Book(String title, int pages) {
    }

    static String describe(Object thing) {
        return switch (thing) {
            case Book b -> "a book";
            case Book b when b.pages() > 500 -> "a long book";
            default -> "something else";
        };
    }

    public static void main(String[] args) {
        System.out.println(describe(new Book("Dune", 700)));
    }
}
