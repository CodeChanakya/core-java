import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/** Text sits in the writer's buffer until the writer is flushed or closed. */
public class UnclosedWriter {

    public static void main(String[] args) throws IOException {
        File file = File.createTempFile("report", ".txt");
        file.deleteOnExit();

        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("Total payroll: 466500.50\n");
        System.out.println("File size with the writer still open: " + file.length() + " bytes");

        writer.close();
        System.out.println("File size after close()             : " + file.length() + " bytes");
    }
}
