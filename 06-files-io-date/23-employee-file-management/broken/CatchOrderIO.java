import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/** A general catch above a specific one. */
public class CatchOrderIO {

    public static void main(String[] args) {
        try {
            new FileReader("employees.csv").close();
        } catch (IOException e) {
            System.out.println("Some I/O problem");
        } catch (FileNotFoundException e) {
            System.out.println("The file is missing");
        }
    }
}
