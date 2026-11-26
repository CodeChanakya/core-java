package com.codechanakya.customerdata;

import java.util.List;

/** The sample customers used by the program. In a real system these would come from a database or a file. */
public final class CustomerData {

    private CustomerData() {
    }

    public static List<Customer> sample() {
        return List.of(
                new Customer("Asha Menon", "asha@example.com", "Kochi", 34, "12500.00", true),
                new Customer("Rahul Verma", "rahul@mail.org", "Pune", 28, "8200.50", true),
                new Customer("Meera Iyer", "meera@example.com", "Chennai", 41, "30500.00", true),
                new Customer("Kiran Rao", "kiran@mail.org", "Pune", 52, "150.00", false),
                new Customer("Dev Patel", "dev@example.com", "Mumbai", 23, "4300.75", true),
                new Customer("Zoya Khan", "zoya@corp.example.net", "Pune", 36, "19800.00", true),
                new Customer("Imran Sheikh", "imran@mail.org", "Mumbai", 45, "0.00", false),
                new Customer("Neha Joshi", "neha@example.com", "Kochi", 31, "7600.00", true),
                new Customer("Farah Siddiqui", "farah@corp.example.net", "Chennai", 29, "2100.00", true),
                new Customer("Leela Nair", "leela@example.com", "Kochi", 60, "9900.00", false));
    }
}
