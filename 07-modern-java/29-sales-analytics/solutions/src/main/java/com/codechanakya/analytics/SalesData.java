package com.codechanakya.analytics;

import java.util.List;

import static com.codechanakya.analytics.Category.ELECTRONICS;
import static com.codechanakya.analytics.Category.HOME;
import static com.codechanakya.analytics.Category.STATIONERY;

/** Sixteen sample orders across four regions. */
public final class SalesData {

    private SalesData() {
    }

    public static List<Sale> sample() {
        return List.of(
                new Sale("S-01", "North", ELECTRONICS, "Asha", 3, "1999.00"),
                new Sale("S-02", "North", STATIONERY, "Asha", 40, "85.50"),
                new Sale("S-03", "South", ELECTRONICS, "Rahul", 2, "1999.00"),
                new Sale("S-04", "South", HOME, "Meera", 5, "1250.00"),
                new Sale("S-05", "East", STATIONERY, "Kiran", 100, "40.00"),
                new Sale("S-06", "East", ELECTRONICS, "Dev", 6, "299.00"),
                new Sale("S-07", "West", HOME, "Zoya", 2, "1250.00"),
                new Sale("S-08", "West", ELECTRONICS, "Zoya", 4, "1999.00"),
                new Sale("S-09", "North", HOME, "Neha", 1, "1250.00"),
                new Sale("S-10", "South", STATIONERY, "Rahul", 25, "85.50"),
                new Sale("S-11", "East", HOME, "Dev", 3, "1250.00"),
                new Sale("S-12", "West", STATIONERY, "Farah", 60, "40.00"),
                new Sale("S-13", "North", ELECTRONICS, "Neha", 10, "299.00"),
                new Sale("S-14", "South", ELECTRONICS, "Meera", 1, "1999.00"),
                new Sale("S-15", "East", ELECTRONICS, "Kiran", 2, "1999.00"),
                new Sale("S-16", "West", HOME, "Farah", 4, "1250.00"));
    }
}
