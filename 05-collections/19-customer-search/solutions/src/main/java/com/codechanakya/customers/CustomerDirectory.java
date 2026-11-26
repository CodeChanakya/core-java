package com.codechanakya.customers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Holds customers and answers search questions with Sets.
 *
 * A Set never holds the same thing twice, which is the whole reason to use one:
 * "which cities do we serve?" and "who is on the list?" have no duplicates by nature.
 */
public class CustomerDirectory {

    private final List<Customer> signUps = new ArrayList<>();

    public void signUp(Customer customer) {
        signUps.add(customer);
    }

    /**
     * Customers in sign-up order, with repeat emails dropped.
     * LinkedHashSet remembers insertion order, so the first sign-up wins.
     */
    public List<Customer> firstSignUps() {
        Set<String> seenEmails = new LinkedHashSet<>();
        List<Customer> unique = new ArrayList<>();
        for (Customer customer : signUps) {
            if (seenEmails.add(customer.getEmail())) {     // add() says false for a repeat
                unique.add(customer);
            }
        }
        return unique;
    }

    /** Every city we deliver to, alphabetical and without repeats. */
    public Set<String> cities() {
        Set<String> cities = new TreeSet<>();
        for (Customer customer : signUps) {
            cities.add(customer.getCity());
        }
        return cities;
    }

    /** Customers whose name starts with the text, in natural order, repeats collapsed. */
    public Set<Customer> searchByName(String prefix) {
        Set<Customer> found = new TreeSet<>();
        String wanted = prefix.toLowerCase();
        for (Customer customer : signUps) {
            if (customer.getName().toLowerCase().startsWith(wanted)) {
                found.add(customer);
            }
        }
        return found;
    }

    /** Names of everyone in one city, alphabetical. */
    public Set<String> namesIn(String city) {
        Set<String> names = new TreeSet<>();
        for (Customer customer : signUps) {
            if (customer.getCity().equalsIgnoreCase(city)) {
                names.add(customer.getName());
            }
        }
        return names;
    }

    public int signUpCount() {
        return signUps.size();
    }

    /** B2: how many different cities. */
    public int cityCount() {
        return cities().size();
    }

    /** I1: unique customers ordered by city, then by name. */
    public List<Customer> customersSortedByCity() {
        List<Customer> list = firstSignUps();
        list.sort(Comparator.comparing(Customer::getCity).thenComparing(Customer::getName));
        return list;
    }

    /** I2: the part after the @ of every email, alphabetical and without repeats. */
    public Set<String> emailDomains() {
        Set<String> domains = new TreeSet<>();
        for (Customer customer : signUps) {
            String email = customer.getEmail();
            domains.add(email.substring(email.indexOf('@') + 1));
        }
        return domains;
    }

    /** C1: names from one name to another, inclusive, ignoring case. */
    public Set<String> namesBetween(String from, String to) {
        TreeSet<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (Customer customer : signUps) {
            names.add(customer.getName());
        }
        return names.subSet(from, true, to, true);
    }
}
