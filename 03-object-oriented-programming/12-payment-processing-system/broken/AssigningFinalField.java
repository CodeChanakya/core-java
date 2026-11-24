/** A final field is set once, in the constructor, and never again. That is how a result stays unchangeable. */
public class AssigningFinalField {

    public static void main(String[] args) {
        PaymentResult result = new PaymentResult(false);
        result.markApproved();
        System.out.println("Approved: " + result.approved);
    }
}

class PaymentResult {
    final boolean approved;

    PaymentResult(boolean approved) {
        this.approved = approved;
    }

    void markApproved() {
        approved = true;
    }
}
