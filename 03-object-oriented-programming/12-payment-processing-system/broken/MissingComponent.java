/** Composition only works once every part has been plugged in. */
public class MissingComponent {

    public static void main(String[] args) {
        PaymentProcessor processor = new PaymentProcessor();
        processor.pay(500);
    }
}

interface PaymentMethod {
    boolean charge(double amount);
}

class PaymentProcessor {
    private PaymentMethod method;   // nobody ever sets this

    void pay(double amount) {
        boolean ok = method.charge(amount);
        System.out.println(ok ? "Paid" : "Declined");
    }
}
