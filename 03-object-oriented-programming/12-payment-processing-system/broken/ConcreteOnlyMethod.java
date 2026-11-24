/** Through an interface variable you can only call what the interface promises. */
public class ConcreteOnlyMethod {

    public static void main(String[] args) {
        PaymentMethod method = new CardPayment();
        System.out.println("Limit left: " + method.remainingLimit());
    }
}

interface PaymentMethod {
    boolean charge(double amount);
}

class CardPayment implements PaymentMethod {
    public boolean charge(double amount) {
        return true;
    }

    double remainingLimit() {
        return 50000;
    }
}
