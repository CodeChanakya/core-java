/** Statuses kept as plain Strings: one wrong letter and the rule silently never fires. */
public class StatusTypo {

    public static void main(String[] args) {
        String status = "PAID";
        if (status.equals("PAYED")) {
            System.out.println("Shipping the order");
        } else {
            System.out.println("Cannot ship: order is " + status);
        }
    }
}
