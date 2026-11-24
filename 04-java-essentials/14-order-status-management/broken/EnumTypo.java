/** An enum turns a misspelt status into a compile error instead of a silent bug. */
public class EnumTypo {

    enum Status { NEW, PAID, SHIPPED }

    public static void main(String[] args) {
        Status status = Status.PAYED;
        System.out.println("Status: " + status);
    }
}
