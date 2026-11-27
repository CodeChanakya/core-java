package demo;

import java.io.InputStream;

/**
 * Looks up the same file with and without a leading slash.
 * Without the slash, Java searches in the class's own package folder (demo/), not at the root.
 */
public class ResourceLookup {

    public static void main(String[] args) throws Exception {
        report("/catalog.csv");
        report("catalog.csv");
    }

    private static void report(String name) throws Exception {
        try (InputStream in = ResourceLookup.class.getResourceAsStream(name)) {
            System.out.println(String.format("%-13s -> %s", name, in == null ? "null (not found)" : "found"));
        }
    }
}
