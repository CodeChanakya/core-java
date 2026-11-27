/** BROKEN: money held in a double. */
public class DoubleForMoney {
    public static void main(String[] args) {
        double balance = 0;
        for (int i = 0; i < 10; i++) {
            balance += 0.10;   // ten deposits of 10 paise
        }
        System.out.println("Balance after ten deposits of 0.10: " + balance);
        System.out.println("Is it exactly 1.0? " + (balance == 1.0));
    }
}
