// This file compiles and runs, but surprises you. See README.md in this folder.
import java.util.Scanner;

public class WrongStringCompare {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        if (answer == "yes") {
            System.out.println("Confirmed");
        } else {
            System.out.println("Not confirmed");
        }
    }
}
