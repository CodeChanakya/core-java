/** A static method belongs to the class, so it has no "this" and cannot see instance fields. */
public class StaticFromInstance {

    private final String customer = "Asha";

    static void printCustomer() {
        System.out.println("Customer: " + customer);
    }

    public static void main(String[] args) {
        printCustomer();
    }
}
