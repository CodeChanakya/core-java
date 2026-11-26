import java.nio.file.Path;

/** resolve() with an absolute path ignores everything before it. */
public class ResolveAbsolute {

    public static void main(String[] args) {
        Path logs = Path.of("data", "logs");
        System.out.println("Relative name  : " + logs.resolve("app.log"));
        System.out.println("Absolute name  : " + logs.resolve("/var/log/app.log"));
    }
}
