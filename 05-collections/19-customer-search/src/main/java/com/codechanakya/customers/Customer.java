package com.codechanakya.customers;

/**
 * A customer, ordered naturally by name and then by email.
 *
 * Implementing Comparable gives the class one "default" order, which
 * TreeSet and Collections.sort use when nobody passes a Comparator.
 * Email is the tie-break because two people can share a name, but not a mailbox.
 */
public class Customer implements Comparable<Customer> {

    private final String name;
    private final String email;
    private final String city;

    public Customer(String name, String email, String city) {
        this.name = name;
        this.email = email.toLowerCase();    // mailboxes are not case-sensitive in practice
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    @Override
    public int compareTo(Customer other) {
        int byName = name.compareToIgnoreCase(other.name);
        if (byName != 0) {
            return byName;
        }
        return email.compareTo(other.email);
    }

    @Override
    public String toString() {
        return name + " <" + email + "> " + city;
    }
}
