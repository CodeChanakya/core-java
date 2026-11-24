package com.codechanakya.repository;

/** A customer, identified by a text id such as C-101. */
public class Customer implements Identifiable<String> {

    private final String id;
    private final String name;
    private final String city;

    public Customer(String id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    @Override
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return id + " " + name + " (" + city + ")";
    }
}
