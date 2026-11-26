package com.codechanakya.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes the employee CSV using the classic java.io classes.
 *
 * Two families of class appear here, and the split is worth learning early:
 *  - Reader and Writer handle TEXT (characters). They need a charset.
 *  - InputStream and OutputStream handle BYTES. They know nothing about text.
 * Every file is opened with try-with-resources, so it is closed even when an exception is thrown.
 */
public class EmployeeFile {

    private static final String HEADER = "id,name,department,salary";

    /**
     * Reads every employee from the file. Rows that cannot be used are skipped and described
     * in the result. A file that cannot be opened at all throws IOException.
     */
    public LoadResult load(File file) throws IOException {
        LoadResult result = new LoadResult();
        // FileReader reads characters one at a time from disk; BufferedReader wraps it so lines
        // come from a memory buffer instead of a disk read each time.
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 && line.equals(HEADER)) {
                    continue;                       // the header names the columns; it is not data
                }
                if (line.isBlank()) {
                    continue;                       // blank lines are harmless, so no complaint
                }
                parseRow(line, lineNumber, result);
            }
        }
        return result;
    }

    private void parseRow(String line, int lineNumber, LoadResult result) {
        String[] parts = line.split(",", -1);       // -1 keeps empty trailing fields instead of dropping them
        if (parts.length != 4) {
            result.addProblem("line " + lineNumber + ": expected 4 fields but found " + parts.length);
            return;
        }
        try {
            BigDecimal salary = new BigDecimal(parts[3].trim());
            result.addEmployee(new Employee(parts[0].trim(), parts[1].trim(), parts[2].trim(), salary));
        } catch (NumberFormatException e) {
            result.addProblem("line " + lineNumber + ": salary is not a number: \"" + parts[3].trim() + "\"");
        }
    }

    /** Writes a fresh file, replacing anything already there. */
    public void save(File file, List<Employee> employees) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write(HEADER + "\n");            // "\n" rather than newLine(), so the file is the same on every OS
            for (Employee employee : employees) {
                writer.write(employee.toCsv() + "\n");
            }
        }
    }

    /** Adds one line to the end of an existing file. The third argument, true, means append. */
    public void append(File file, Employee employee) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, true))) {
            writer.write(employee.toCsv() + "\n");
        }
    }

    /** Writes a readable, aligned text report. PrintWriter adds printf and println to a Writer. */
    public void writeReport(File file, List<Employee> employees) throws IOException {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8)))) {
            out.printf("%-8s %-14s %-12s %11s\n", "ID", "Name", "Department", "Salary");
            out.print("-".repeat(48) + "\n");
            for (Employee employee : employees) {
                out.printf("%-8s %-14s %-12s %11s\n", employee.getId(), employee.getName(),
                        employee.getDepartment(), employee.getSalary());
            }
            out.printf("%d employees\n", employees.size());
        }
    }

    /** Reads every line of a text file. Fine for small files; for huge ones, read line by line instead. */
    public List<String> readAllLines(File file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        }
        return lines;
    }

    /**
     * Copies any file, text or not, byte for byte. Returns how many bytes were copied.
     * A 4 KB buffer means one disk read per 4096 bytes rather than one per byte.
     */
    public long copyBytes(File from, File to) throws IOException {
        long total = 0;
        try (InputStream in = new FileInputStream(from);
             OutputStream out = new FileOutputStream(to)) {
            byte[] buffer = new byte[4096];
            int count;
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);        // write only what was actually read this time
                total += count;
            }
        }
        return total;
    }
}
