package com.codechanakya.quotes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads the product catalog from a file on the classpath.
 *
 * <p>The file lives in {@code src/main/resources}. Maven copies everything in that folder
 * next to the compiled classes, so the code finds it with {@code getResourceAsStream}
 * and never needs to know where the project sits on disk. That is why the program works
 * the same from an IDE, from {@code mvn}, and from a packaged jar.
 */
public final class CatalogLoader {

    private CatalogLoader() {
    }

    /** Loads a catalog from a classpath resource such as {@code /catalog.csv}. */
    public static Catalog fromResource(String resourceName) {
        try (InputStream in = CatalogLoader.class.getResourceAsStream(resourceName)) {
            if (in == null) {
                throw new IllegalStateException("Resource not found on the classpath: " + resourceName
                        + " (is src/main/resources on the classpath?)");
            }
            return read(new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)));
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + resourceName, e);
        }
    }

    static Catalog read(BufferedReader reader) throws IOException {
        List<Product> products = new ArrayList<>();
        String line;
        int lineNumber = 0;
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            if (lineNumber == 1 || line.isBlank()) {
                continue; // header and empty lines
            }
            String[] cells = line.split(",", -1); // -1 keeps empty cells at the end of a line
            if (cells.length != 4) {
                throw new IllegalStateException("Line " + lineNumber + ": expected 4 columns but found " + cells.length);
            }
            try {
                products.add(new Product(cells[0].trim(), cells[1].trim(),
                        new BigDecimal(cells[2].trim()), new BigDecimal(cells[3].trim())));
            } catch (NumberFormatException e) {
                throw new IllegalStateException("Line " + lineNumber + ": price or tax is not a number", e);
            }
        }
        return new Catalog(products);
    }
}
