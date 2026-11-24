/** An empty catch block hides the problem. The program carries on with bad data and nobody knows why. */
public class SwallowedException {

    public static void main(String[] args) {
        int balance = 0;
        try {
            balance = Integer.parseInt("1,000");
        } catch (NumberFormatException e) {
            // ignored
        }
        System.out.println("Balance is " + balance);
        System.out.println("Fee is " + (balance * 2 / 100));
    }
}
