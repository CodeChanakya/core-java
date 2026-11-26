package com.codechanakya.logs;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        try {
            run();
        } catch (IOException e) {
            System.out.println("Could not finish: " + e.getClass().getSimpleName());
        }
    }

    /** Paths print with the operating system's separator, so show them with / to keep output identical everywhere. */
    private static String unix(Path path) {
        return path.toString().replace('\\', '/');
    }

    private static void run() throws IOException {
        System.out.println("--- A short tour of Path ---");
        Path logs = Path.of("data", "logs");
        Path first = logs.resolve("app-2026-03-01.log");
        System.out.println("  resolve        : " + unix(first));
        System.out.println("  getFileName    : " + first.getFileName());
        System.out.println("  getParent      : " + unix(first.getParent()));
        System.out.println("  relativize     : " + unix(logs.relativize(first)));
        System.out.println("  normalize      : " + unix(Path.of("data", "extra", "..", "logs").normalize()));
        System.out.println("  isAbsolute     : " + first.isAbsolute());
        System.out.println("  exists / isDir : " + Files.exists(first) + " / " + Files.isDirectory(logs));

        System.out.println();
        System.out.println("--- Finding the log files ---");
        List<Path> files = LogFolder.listLogs(logs);
        for (Path file : files) {
            System.out.println("  " + file.getFileName());
        }
        System.out.println("  (notes.txt is ignored by the *.log pattern)");

        System.out.println();
        System.out.println("--- One file at a time ---");
        LogReport total = new LogReport();
        for (Path file : files) {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            LogReport single = new LogReport();
            single.addFile(file.getFileName().toString(), lines);
            total.addFile(file.getFileName().toString(), lines);
            System.out.println("  " + file.getFileName() + ": " + single.getLines() + " lines, "
                    + single.getByLevel().get(Level.ERROR) + " errors, " + single.getUnreadable() + " unreadable");
        }

        System.out.println();
        System.out.println("--- All files together ---");
        for (String line : total.summaryLines()) {
            System.out.println("  " + line);
        }
        System.out.println("  First error in each file:");
        for (String firstError : total.getFirstErrors()) {
            System.out.println("    " + firstError);
        }

        System.out.println();
        System.out.println("--- Writing, moving and deleting ---");
        Path work = Path.of("out", "work");
        Path archive = work.resolve("archive");
        Files.createDirectories(archive);                       // creates every missing folder; silent if they exist
        Path summary = work.resolve("summary.txt");
        Files.write(summary, total.summaryLines(), StandardCharsets.UTF_8);
        System.out.println("  Summary written: " + Files.readAllLines(summary).size() + " lines");

        Path moved = archive.resolve("summary-2026-03.txt");
        Files.move(summary, moved, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("  After move, old path exists: " + Files.exists(summary)
                + ", new path exists: " + Files.exists(moved));

        Path copy = archive.resolve("summary-copy.txt");
        Files.copy(moved, copy, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("  Copy has the same lines: "
                + Files.readAllLines(moved).equals(Files.readAllLines(copy)));
        System.out.println("  Deleted the copy: " + Files.deleteIfExists(copy));
        System.out.println("  Deleting it again: " + Files.deleteIfExists(copy));

        System.out.println();
        System.out.println("--- A missing file ---");
        try {
            Files.readAllLines(Path.of("data", "logs", "app-2026-03-03.log"));
        } catch (NoSuchFileException e) {
            System.out.println("  Caught " + e.getClass().getSimpleName() + " for " + unix(Path.of(e.getFile())));
        }

        Map<Level, Integer> levels = total.getByLevel();
        System.out.println();
        System.out.println("Errors were about " + (levels.get(Level.ERROR) * 100 / (total.getLines() - total.getUnreadable()))
                + " percent of readable lines.");

        System.out.println();
        System.out.println("--- Exercise answers ---");
        for (Path file : files) {
            String name = file.getFileName().toString();
            String bare = name.substring(0, name.lastIndexOf('.'));       // B1
            System.out.println("  " + file.getParent().getFileName() + " / " + bare
                    + ", regular file: " + Files.isRegularFile(file)       // B3
                    + ", busiest: " + LogReport.busiestComponent(Files.readAllLines(file)));    // C1
        }
        System.out.println("  INFO is " + total.percentOf(Level.INFO) + " percent of readable lines (B2)");
        System.out.println("  TRACE lines counted now (I1): " + total.getByLevel().get(Level.TRACE)
                + ", unreadable: " + total.getUnreadable());
        System.out.println("  Text files in the folder (I2): " + LogFolder.list(logs, "*.txt").size());
    }
}
