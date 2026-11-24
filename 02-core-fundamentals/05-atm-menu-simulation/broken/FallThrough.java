// This file compiles and runs, but prints too much. See README.md in this folder.
public class FallThrough {

    public static void main(String[] args) {
        int choice = 1;
        switch (choice) {
            case 1:
                System.out.println("Balance");
            case 2:
                System.out.println("Deposit");
            case 3:
                System.out.println("Withdraw");
                break;
            default:
                System.out.println("Invalid choice");
        }
    }
}
