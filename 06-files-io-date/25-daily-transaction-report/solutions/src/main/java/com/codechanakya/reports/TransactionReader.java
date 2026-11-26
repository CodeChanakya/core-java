package com.codechanakya.reports;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/** Reads transactions from a CSV file, keeping a note of every row it could not use. */
public class TransactionReader {

    private final List<String> problems = new ArrayList<>();

    public List<Transaction> read(Path file) throws IOException {
        List<Transaction> result = new ArrayList<>();
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        for (int i = 1; i < lines.size(); i++) {            // index 0 is the header
            String line = lines.get(i);
            if (line.isBlank()) {
                continue;
            }
            String[] p = line.split(",");
            try {
                // parse() understands the ISO layouts 2026-03-02 and 09:10 with no pattern needed.
                result.add(new Transaction(LocalDate.parse(p[0]), LocalTime.parse(p[1]), p[2],
                        TxType.valueOf(p[3]), new BigDecimal(p[4])));
            } catch (DateTimeParseException e) {
                problems.add("line " + (i + 1) + ": bad date or time \"" + p[0] + " " + p[1] + "\"");
            } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
                problems.add("line " + (i + 1) + ": " + e.getClass().getSimpleName());
            }
        }
        return result;
    }

    public List<String> getProblems() {
        return problems;
    }
}
