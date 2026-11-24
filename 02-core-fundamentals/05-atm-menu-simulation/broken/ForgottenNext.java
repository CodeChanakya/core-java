// This file repeats itself on bad input, on purpose. A counter stops it after
// 5 tries so it cannot hang. See README.md in this folder.
// Run it with: echo abc | java ForgottenNext
import java.util.Scanner;

public class ForgottenNext {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int tries = 0;
        while (!scanner.hasNextInt() && tries < 5) {
            System.out.println("Not a number, try again");
            tries++;
        }
    }
}
