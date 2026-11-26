package com.codechanakya.customers;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

public class Main {

    public static void main(String[] args) {
        CustomerDirectory directory = new CustomerDirectory();
        directory.signUp(new Customer("Asha Menon", "asha@example.com", "Kochi"));
        directory.signUp(new Customer("Rahul Verma", "rahul@example.com", "Pune"));
        directory.signUp(new Customer("Meera Iyer", "meera@example.com", "Chennai"));
        directory.signUp(new Customer("Asha Menon", "ASHA@example.com", "Kochi"));    // same person again
        directory.signUp(new Customer("Kiran Rao", "kiran@example.com", "Pune"));
        directory.signUp(new Customer("Anil Kumar", "anil@example.com", "Kochi"));
        directory.signUp(new Customer("Asha Menon", "asha.m@example.com", "Chennai"));  // a different Asha

        System.out.println("Sign-ups received: " + directory.signUpCount());
        System.out.println();
        System.out.println("--- First sign-up per email (LinkedHashSet keeps arrival order) ---");
        for (Customer customer : directory.firstSignUps()) {
            System.out.println("  " + customer);
        }

        System.out.println();
        System.out.println("--- Cities served (TreeSet keeps them sorted) ---");
        System.out.println("  " + directory.cities());

        System.out.println();
        System.out.println("--- Search: names starting with \"as\" ---");
        for (Customer customer : directory.searchByName("as")) {
            System.out.println("  " + customer);
        }
        System.out.println("Two sign-ups from asha@example.com collapsed into one result,");
        System.out.println("because compareTo() said they are the same customer.");

        System.out.println();
        System.out.println("--- Same data, three kinds of Set ---");
        String[] tags = {"vip", "new", "vip", "pune", "new", "kochi"};
        Set<String> hash = new HashSet<>();
        Set<String> linked = new LinkedHashSet<>();
        Set<String> tree = new TreeSet<>();
        for (String tag : tags) {
            hash.add(tag);
            linked.add(tag);
            tree.add(tag);
        }
        System.out.println("  LinkedHashSet (insertion order): " + linked);
        System.out.println("  TreeSet (sorted order)         : " + tree);
        System.out.println("  HashSet size                   : " + hash.size() + " (its order is not promised)");

        System.out.println();
        System.out.println("--- Set maths: newsletter list against buyers ---");
        Set<String> newsletter = new TreeSet<>(directory.namesIn("Pune"));
        newsletter.add("Asha Menon");
        newsletter.add("Meera Iyer");
        Set<String> buyers = new TreeSet<>(directory.namesIn("Kochi"));
        buyers.add("Meera Iyer");

        Set<String> both = new TreeSet<>(newsletter);
        both.retainAll(buyers);
        Set<String> either = new TreeSet<>(newsletter);
        either.addAll(buyers);
        Set<String> onlyNewsletter = new TreeSet<>(newsletter);
        onlyNewsletter.removeAll(buyers);

        System.out.println("  Newsletter : " + newsletter);
        System.out.println("  Buyers     : " + buyers);
        System.out.println("  Both       : " + both);
        System.out.println("  Either     : " + either);
        System.out.println("  Newsletter only: " + onlyNewsletter);

        System.out.println();
        System.out.println("--- Set facts ---");
        Set<String> demo = new TreeSet<>();
        System.out.println("  First add(\"a\") returns : " + demo.add("a"));
        System.out.println("  Second add(\"a\") returns: " + demo.add("a") + " (already there, nothing changes)");
        System.out.println("  Is Asha a buyer? " + buyers.contains("Asha Menon"));

        System.out.println();
        System.out.println("--- Exercise answers ---");
        // B1
        directory.signUp(new Customer("Zoya Khan", "zoya@mail.example.org", "Mumbai"));
        System.out.println("  Cities: " + directory.cities() + " (" + directory.cityCount() + ")");   // B2
        // B3
        TreeSet<String> allNames = new TreeSet<>();
        for (Customer customer : directory.firstSignUps()) {
            allNames.add(customer.getName());
        }
        System.out.println("  Names, Z to A: " + allNames.descendingSet());
        // I1
        for (Customer customer : directory.customersSortedByCity()) {
            System.out.println("  " + customer.getCity() + " / " + customer.getName());
        }
        // I2
        System.out.println("  Email domains: " + directory.emailDomains());
        // C1
        System.out.println("  Between Anil and Meera: " + directory.namesBetween("anil", "MEERA IYER"));
    }
}
