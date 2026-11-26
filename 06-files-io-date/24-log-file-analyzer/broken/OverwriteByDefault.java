import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** Files.write replaces the whole file unless you ask it to append. */
public class OverwriteByDefault {

    public static void main(String[] args) throws IOException {
        Path file = Files.createTempFile("summary", ".txt");
        file.toFile().deleteOnExit();

        Files.write(file, List.of("Monday: 4 errors"));
        Files.write(file, List.of("Tuesday: 2 errors"));
        System.out.println("File now holds: " + Files.readAllLines(file));
    }
}
