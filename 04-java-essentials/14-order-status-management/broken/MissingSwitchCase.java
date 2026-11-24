/** A switch expression over an enum must cover every constant. */
public class MissingSwitchCase {

    enum Status { NEW, PAID, SHIPPED }

    public static void main(String[] args) {
        Status status = Status.SHIPPED;
        String text = switch (status) {
            case NEW -> "waiting for payment";
            case PAID -> "ready to pack";
        };
        System.out.println(text);
    }
}
