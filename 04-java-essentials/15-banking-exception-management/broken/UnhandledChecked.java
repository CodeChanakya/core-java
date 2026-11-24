/** A checked exception must be caught or declared. The compiler checks. */
public class UnhandledChecked {

    static class OutOfMoneyException extends Exception {
        OutOfMoneyException(String message) {
            super(message);
        }
    }

    static void withdraw(int balance, int amount) throws OutOfMoneyException {
        if (amount > balance) {
            throw new OutOfMoneyException("Not enough money");
        }
    }

    public static void main(String[] args) {
        withdraw(100, 500);
        System.out.println("Withdrawn");
    }
}
