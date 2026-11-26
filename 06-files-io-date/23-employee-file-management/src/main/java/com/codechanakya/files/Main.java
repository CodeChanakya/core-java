package com.codechanakya.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {
        try {
            run();
        } catch (IOException e) {
            System.out.println("Could not finish: " + e.getMessage());
        }
    }

    private static void run() throws IOException {
        EmployeeFile files = new EmployeeFile();

        // Everything this program writes goes under out/work, which run.sh clears on every run.
        File work = new File("out", "work");
        if (!work.isDirectory() && !work.mkdirs()) {
            throw new IOException("Could not create the work folder");
        }

        System.out.println("--- Reading data/employees.csv ---");
        LoadResult loaded = files.load(new File("data", "employees.csv"));
        List<Employee> employees = loaded.getEmployees();
        System.out.println("Loaded " + employees.size() + " employees, skipped " + loaded.getProblems().size() + " lines");
        for (String problem : loaded.getProblems()) {
            System.out.println("  Skipped " + problem);
        }

        System.out.println();
        System.out.println("--- Payroll by department ---");
        Map<String, BigDecimal> payroll = new TreeMap<>();
        for (Employee employee : employees) {
            payroll.merge(employee.getDepartment(), employee.getSalary(), BigDecimal::add);
        }
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> entry : payroll.entrySet()) {
            System.out.printf("  %-12s %11s%n", entry.getKey(), entry.getValue());
            total = total.add(entry.getValue());
        }
        System.out.printf("  %-12s %11s%n", "Total", total);

        System.out.println();
        System.out.println("--- Writing a report, then reading it back ---");
        File report = new File(work, "report.txt");
        files.writeReport(report, employees);
        for (String line : files.readAllLines(report)) {
            System.out.println("  | " + line);
        }

        System.out.println();
        System.out.println("--- Saving a clean copy, then appending ---");
        File staff = new File(work, "staff.csv");
        files.save(staff, employees);
        System.out.println("  Rows after save  : " + files.load(staff).getEmployees().size());
        files.append(staff, new Employee("E-309", "Farah Siddiqui", "Support", new BigDecimal("39500.00")));
        LoadResult again = files.load(staff);
        System.out.println("  Rows after append: " + again.getEmployees().size()
                + " (problems: " + again.getProblems().size() + ")");

        System.out.println();
        System.out.println("--- Copying as raw bytes ---");
        File copy = new File(work, "staff-copy.csv");
        long copied = files.copyBytes(staff, copy);
        System.out.println("  Copied " + copied + " bytes; the original is " + staff.length()
                + " bytes, the copy is " + copy.length() + " bytes");

        System.out.println();
        System.out.println("--- When the file is not there ---");
        File missing = new File(work, "nope.csv");
        System.out.println("  exists(): " + missing.exists());
        try {
            files.load(missing);
        } catch (FileNotFoundException e) {
            System.out.println("  Caught " + e.getClass().getSimpleName());
        }
        System.out.println("  FileNotFoundException is an IOException: " + (new FileNotFoundException() instanceof IOException));
    }
}
