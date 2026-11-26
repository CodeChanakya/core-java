package com.codechanakya.customerdata;

import java.math.BigDecimal;

/** A customer record kept as an ordinary class. Program 28 shows how a record shortens this. */
public class Customer {

    private final String name;
    private final String email;
    private final String city;
    private final int age;
    private final BigDecimal totalSpent;
    private final boolean active;

    public Customer(String name, String email, String city, int age, String totalSpent, boolean active) {
        this.name = name;
        this.email = email;
        this.city = city;
        this.age = age;
        this.totalSpent = new BigDecimal(totalSpent);
        this.active = active;
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

    public int getAge() {
        return age;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public boolean isActive() {
        return active;
    }

    /** The part of the email after the @. */
    public String getEmailDomain() {
        return email.substring(email.indexOf('@') + 1);
    }

    @Override
    public String toString() {
        return name + " (" + city + ", " + age + ", " + totalSpent + (active ? "" : ", inactive") + ")";
    }
}
