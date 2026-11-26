import java.io.BufferedReader;
import java.io.FileReader;

/** Opening a file can fail, and Java makes you say what happens then. */
public class UnhandledIOException {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new FileReader("employees.csv"));
        System.out.println("Opened");
    }
}
