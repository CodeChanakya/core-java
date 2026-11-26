package com.codechanakya.directory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Looks employees up by id and groups them by department.
 *
 * A Map answers "given this key, what is the value?" in near constant time,
 * which a List cannot do without scanning from the start.
 */
public class Directory {

    // LinkedHashMap so listings come out in the order people joined.
    private final Map<String, Employee> byId = new LinkedHashMap<>();

    /** Adds a person. Returns the previous holder of that id, or null when the id was new. */
    public Employee add(Employee employee) {
        return byId.put(employee.getId(), employee);
    }

    /** Adds only when the id is free, leaving an existing record untouched. */
    public boolean addIfAbsent(Employee employee) {
        return byId.putIfAbsent(employee.getId(), employee) == null;
    }

    /** The person with this id, or null. */
    public Employee find(String id) {
        return byId.get(id);
    }

    public Employee remove(String id) {
        return byId.remove(id);
    }

    public boolean has(String id) {
        return byId.containsKey(id);
    }

    public int size() {
        return byId.size();
    }

    /** Names for an id, or a friendly fallback so the caller never sees null. */
    public String nameOrUnknown(String id) {
        Employee found = byId.get(id);
        return found == null ? "(unknown)" : found.getName();
    }

    /** Department to people, with departments in alphabetical order. */
    public Map<String, List<Employee>> byDepartment() {
        Map<String, List<Employee>> groups = new TreeMap<>();
        for (Employee employee : byId.values()) {
            // computeIfAbsent: create the list the first time a department appears.
            groups.computeIfAbsent(employee.getDepartment(), key -> new ArrayList<>()).add(employee);
        }
        return groups;
    }

    /** Department to head count. merge() adds one each time, starting from 1. */
    public Map<String, Integer> headCount() {
        Map<String, Integer> counts = new TreeMap<>();
        for (Employee employee : byId.values()) {
            counts.merge(employee.getDepartment(), 1, Integer::sum);
        }
        return counts;
    }

    public void printAll() {
        for (Map.Entry<String, Employee> entry : byId.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue().getName());
        }
    }

    /** B3: just the keys, in joining order. */
    public List<String> ids() {
        return new ArrayList<>(byId.keySet());
    }

    /** B2: how many people are in one department. */
    public int countIn(String department) {
        return headCount().getOrDefault(department, 0);
    }

    /**
     * I1: moves everyone from one department to another.
     * Employee is immutable, so each person is replaced by a new object under the same id.
     */
    public int renameDepartment(String from, String to) {
        int moved = 0;
        for (Employee employee : new ArrayList<>(byId.values())) {      // a copy, so the loop is safe
            if (employee.getDepartment().equals(from)) {
                byId.put(employee.getId(), new Employee(employee.getId(), employee.getName(), to));
                moved++;
            }
        }
        return moved;
    }

    /** I2: the department with the most people. Ties go to the alphabetically first. */
    public String largestDepartment() {
        String best = null;
        int bestCount = 0;
        for (Map.Entry<String, Integer> entry : headCount().entrySet()) {
            if (entry.getValue() > bestCount) {
                best = entry.getKey();
                bestCount = entry.getValue();
            }
        }
        return best;
    }
}
