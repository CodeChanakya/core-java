package com.codechanakya.logs;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Finds log files in a folder using java.nio.file. */
public final class LogFolder {

    private LogFolder() {
    }

    /**
     * Every *.log file directly inside the folder, sorted by name.
     * The sort matters: the operating system may list a folder in any order, so
     * output that depends on it would differ from one machine to the next.
     */
    public static List<Path> listLogs(Path folder) throws IOException {
        List<Path> found = new ArrayList<>();
        // The second argument is a glob pattern. DirectoryStream holds an open handle, so close it.
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder, "*.log")) {
            for (Path path : stream) {
                found.add(path);
            }
        }
        Collections.sort(found);
        return found;
    }
}
