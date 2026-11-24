import java.util.Scanner;

/**
 * One possible answer to the exercises in ../exercises/README.md.
 * Try them yourself first. Your version does not need to look like this one.
 *
 * Try it with:  java MyAtm < my-input.txt   (see the end of the exercises README)
 */
public class MyAtm {

    private static final int MAX_PIN_ATTEMPTS = 3;
    private static final int NOTE_VALUE = 100;
    private static final int DAILY_LIMIT = 5000;   // I1

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int pin = 4321;          // B1
        int balance = 15000;     // B1
        int withdrawnToday = 0;

        // C1: no static variables. main compares the balance before and after
        // each action to find out whether anything actually happened.
        int deposits = 0;
        int withdrawals = 0;
        int moneyIn = 0;
        int moneyOut = 0;

        if (!pinAccepted(scanner, pin)) {
            System.out.println("Card blocked. Please contact your bank.");
            return;
        }

        boolean running = true;
        while (running && scanner.hasNext()) {
            printMenu();
            int choice = readInt(scanner);
            switch (choice) {
                case 1 -> System.out.println("Balance: " + balance);
                case 2 -> {
                    int before = balance;
                    balance = deposit(scanner, balance);
                    if (balance > before) {
                        deposits++;
                        moneyIn += balance - before;
                    }
                }
                case 3 -> {
                    int before = balance;
                    balance = withdraw(scanner, balance, DAILY_LIMIT - withdrawnToday);
                    if (balance < before) {
                        withdrawals++;
                        moneyOut += before - balance;
                        withdrawnToday += before - balance;
                    }
                }
                case 4 -> pin = changePin(scanner, pin);
                case 5 -> System.out.println("Daily limit: " + DAILY_LIMIT
                        + ", left today: " + (DAILY_LIMIT - withdrawnToday));
                case 6 -> running = false;
                default -> System.out.println("Invalid choice. Enter a number from 1 to 6.");
            }
        }

        System.out.println();
        System.out.println("Session summary: " + deposits + " deposits (" + moneyIn + "), "
                + withdrawals + " withdrawals (" + moneyOut + ")");
    }

    private static boolean pinAccepted(Scanner scanner, int correctPin) {
        for (int attempt = 1; attempt <= MAX_PIN_ATTEMPTS; attempt++) {
            System.out.println("Enter PIN:");
            if (readInt(scanner) == correctPin) {
                System.out.println("PIN accepted.");
                return true;
            }
            System.out.println("Wrong PIN. Attempts left: " + (MAX_PIN_ATTEMPTS - attempt));
        }
        return false;
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Check balance  2. Deposit  3. Withdraw");
        System.out.println("4. Change PIN     5. Daily limit  6. Exit");
        System.out.println("Choose an option:");
    }

    private static int deposit(Scanner scanner, int balance) {
        System.out.println("Amount to deposit:");
        int amount = readInt(scanner);
        if (amount <= 0) {
            System.out.println("Enter a positive whole number.");
            return balance;
        }
        System.out.println("Deposited " + amount + ". New balance: " + (balance + amount));
        return balance + amount;
    }

    private static int withdraw(Scanner scanner, int balance, int limitLeft) {
        System.out.println("Amount to withdraw:");
        int amount = readInt(scanner);
        if (amount <= 0) {
            System.out.println("Enter a positive whole number.");
        } else if (amount % NOTE_VALUE != 0) {
            System.out.println("Amount must be a multiple of " + NOTE_VALUE + ".");
        } else if (amount > balance) {
            System.out.println("Insufficient funds. Balance: " + balance);
        } else if (amount > limitLeft) {
            System.out.println("Daily limit exceeded. You can still withdraw " + limitLeft + " today.");
        } else {
            balance = balance - amount;
            System.out.println("Please collect " + (amount / NOTE_VALUE) + " notes of "   // B3
                    + NOTE_VALUE + ". New balance: " + balance);
        }
        return balance;
    }

    /** I2: the new PIN must be four digits, so between 1000 and 9999. */
    private static int changePin(Scanner scanner, int currentPin) {
        System.out.println("Enter new 4-digit PIN:");
        int newPin = readInt(scanner);
        if (newPin < 1000 || newPin > 9999) {
            System.out.println("PIN must be exactly four digits. PIN unchanged.");
            return currentPin;
        }
        System.out.println("PIN changed.");
        return newPin;
    }

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
