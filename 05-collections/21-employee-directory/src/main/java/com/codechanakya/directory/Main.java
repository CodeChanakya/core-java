package com.codechanakya.directory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        Directory directory = new Directory();
        directory.add(new Employee("E-104", "Asha Menon", "Engineering"));
        directory.add(new Employee("E-211", "Rahul Verma", "Finance"));
        directory.add(new Employee("E-007", "Meera Iyer", "Engineering"));
        directory.add(new Employee("E-350", "Kiran Rao", "Support"));
        directory.add(new Employee("E-412", "Dev Patel", "Finance"));

        System.out.println("--- Lookup by key ---");
        System.out.println("  find(E-007)       : " + directory.find("E-007"));
        System.out.println("  find(E-999)       : " + directory.find("E-999"));
        System.out.println("  nameOrUnknown(E-999): " + directory.nameOrUnknown("E-999"));
        System.out.println("  has(E-350)        : " + directory.has("E-350"));

        System.out.println();
        System.out.println("--- put() replaces, putIfAbsent() does not ---");
        Employee replaced = directory.add(new Employee("E-350", "Kiran Rao", "Engineering"));
        System.out.println("  put returned the old value: " + replaced);
        boolean added = directory.addIfAbsent(new Employee("E-350", "Someone Else", "Support"));
        System.out.println("  addIfAbsent on a taken id : " + added);
        System.out.println("  E-350 is still            : " + directory.find("E-350"));

        System.out.println();
        System.out.println("--- Everyone, in joining order ---");
        directory.printAll();

        System.out.println();
        System.out.println("--- Grouped by department ---");
        for (Map.Entry<String, List<Employee>> group : directory.byDepartment().entrySet()) {
            System.out.println("  " + group.getKey() + ": " + names(group.getValue()));
        }
        System.out.println("  Head count: " + directory.headCount());

        System.out.println();
        System.out.println("--- Why equals() and hashCode() matter ---");
        Employee first = new Employee("E-104", "Asha Menon", "Engineering");
        Employee sameId = new Employee("E-104", "A. Menon", "Engineering");
        Set<Employee> team = new HashSet<>();
        team.add(first);
        team.add(sameId);
        System.out.println("  Added the same id twice, set holds: " + team.size());
        System.out.println("  first.equals(sameId): " + first.equals(sameId));
        System.out.println("  Same hash code: " + (first.hashCode() == sameId.hashCode()));

        System.out.println();
        System.out.println("--- Counting words with a HashMap ---");
        Map<String, Integer> words = new HashMap<>();
        for (String word : "to be or not to be".split(" ")) {
            words.put(word, words.getOrDefault(word, 0) + 1);
        }
        System.out.println("  to=" + words.get("to") + ", be=" + words.get("be")
                + ", or=" + words.get("or") + ", not=" + words.get("not")
                + ", missing=" + words.get("never"));
        System.out.println("  (HashMap makes no promise about iteration order, so we look the keys up one by one.)");
    }

    private static String names(List<Employee> people) {
        StringBuilder text = new StringBuilder();
        for (Employee person : people) {
            if (text.length() > 0) {
                text.append(", ");
            }
            text.append(person.getName());
        }
        return text.toString();
    }
}
