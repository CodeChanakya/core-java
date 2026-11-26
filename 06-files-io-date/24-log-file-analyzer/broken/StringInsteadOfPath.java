import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

/** The NIO.2 methods take a Path, not a String. */
public class StringInsteadOfPath {

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines("data/logs/app-2026-03-01.log");
        System.out.println(lines.size());
    }
}
