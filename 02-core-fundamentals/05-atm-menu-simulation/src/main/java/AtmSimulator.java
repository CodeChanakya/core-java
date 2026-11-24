import java.util.Scanner;

/**
 * A console ATM: check the PIN, then offer a menu until the customer exits.
 *
 * Balances are whole rupees to keep the focus on the menu logic. Program 3
 * explains what real money handling needs.
 */
public class AtmSimulator {

    private static final int CORRECT_PIN = 1234;
    private static final int MAX_PIN_ATTEMPTS = 3;
    private static final int NOTE_VALUE = 100;   // the machine only holds 100-rupee notes

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int balance = 10000;

        if (!pinAccepted(scanner)) {
            System.out.println("Card blocked. Please contact your bank.");
            return;
        }

        boolean running = true;
        while (running) {
            if (!scanner.hasNext()) {
                System.out.println("Input ended. Session closed.");
                break;
            }
            printMenu();
            int choice = readInt(scanner);

            // The arrow form of switch runs only the matching case, so there is
            // no break to forget. The older "case 1: ... break;" form is shown
            // in broken/FallThrough.java.
            switch (choice) {
                case 1 -> System.out.println("Balance: " + balance);
                case 2 -> balance = deposit(scanner, balance);
                case 3 -> balance = withdraw(scanner, balance);
                case 4 -> {
                    System.out.println("Thank you. Please take your card.");
                    running = false;
                }
                default -> System.out.println("Invalid choice. Enter a number from 1 to 4.");
            }
        }
    }

    private static boolean pinAccepted(Scanner scanner) {
        for (int attempt = 1; attempt <= MAX_PIN_ATTEMPTS; attempt++) {
            System.out.println("Enter PIN:");
            int pin = readInt(scanner);
            if (pin == CORRECT_PIN) {
                System.out.println("PIN accepted.");
                return true;
            }
            System.out.println("Wrong PIN. Attempts left: " + (MAX_PIN_ATTEMPTS - attempt));
        }
        return false;
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Check balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. Exit");
        System.out.println("Choose an option:");
    }

    private static int deposit(Scanner scanner, int balance) {
        System.out.println("Amount to deposit:");
        int amount = readInt(scanner);
        if (amount <= 0) {
            System.out.println("Enter a positive whole number.");
            return balance;
        }
        int updated = balance + amount;
        System.out.println("Deposited " + amount + ". New balance: " + updated);
        return updated;
    }

    private static int withdraw(Scanner scanner, int balance) {
        System.out.println("Amount to withdraw:");
        int amount = readInt(scanner);
        if (amount <= 0) {
            System.out.println("Enter a positive whole number.");
        } else if (amount % NOTE_VALUE != 0) {
            System.out.println("Amount must be a multiple of " + NOTE_VALUE + ".");
        } else if (amount > balance) {
            System.out.println("Insufficient funds. Balance: " + balance);
        } else {
            balance = balance - amount;
            System.out.println("Please collect " + amount + ". New balance: " + balance);
        }
        return balance;
    }

    /**
     * Reads one whole number. If the next word is not a number, it is thrown
     * away (otherwise the program would read it again forever) and -1 is
     * returned. -1 is also returned when the input has run out.
     */
    private static int readInt(Scanner scanner) {
        if (!scanner.hasNext()) {
            return -1;
        }
        if (scanner.hasNextInt()) {
            return scanner.nextInt();
        }
        scanner.next();
        return -1;
    }
}
