package com.codechanakya.customerdata;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        List<Customer> customers = CustomerData.sample();

        System.out.println("--- The same question, a loop and a stream ---");
        System.out.println("Names of active customers in Pune:");
        List<String> byLoop = new ArrayList<>();
        for (Customer customer : customers) {
            if (customer.isActive() && customer.getCity().equals("Pune")) {
                byLoop.add(customer.getName());
            }
        }
        System.out.println("  loop  : " + byLoop);
        List<String> byStream = customers.stream()
                .filter(Customer::isActive)
                .filter(customer -> customer.getCity().equals("Pune"))
                .map(Customer::getName)
                .toList();
        System.out.println("  stream: " + byStream);

        System.out.println();
        System.out.println("--- Predicate: a yes/no question, as a value ---");
        Predicate<Customer> active = Customer::isActive;
        Predicate<Customer> bigSpender = customer -> customer.getTotalSpent().compareTo(new BigDecimal("10000")) >= 0;
        Predicate<Customer> inKochi = customer -> customer.getCity().equals("Kochi");
        System.out.println("  Active big spenders : " + names(customers, active.and(bigSpender)));
        System.out.println("  Inactive or in Kochi: " + names(customers, active.negate().or(inKochi)));
        System.out.println("  Test one customer   : " + bigSpender.test(customers.get(2)));

        System.out.println();
        System.out.println("--- Function: turn one thing into another ---");
        Function<Customer, String> label = customer -> customer.getName() + " <" + customer.getEmail() + ">";
        Function<String, String> shout = String::toUpperCase;
        System.out.println("  " + label.apply(customers.get(0)));
        System.out.println("  " + label.andThen(shout).apply(customers.get(1)));

        System.out.println();
        System.out.println("--- Consumer and Supplier ---");
        Consumer<Customer> greet = customer -> System.out.println("  Hello, " + customer.getName().split(" ")[0] + "!");
        greet.accept(customers.get(4));
        Supplier<List<String>> newList = ArrayList::new;       // a recipe for making an empty list
        List<String> fresh = newList.get();
        fresh.add("empty list made on demand");
        System.out.println("  " + fresh);

        System.out.println();
        System.out.println("--- Sorting with a Comparator ---");
        System.out.println("  Top 3 spenders:");
        customers.stream()
                .sorted(Comparator.comparing(Customer::getTotalSpent).reversed())
                .limit(3)
                .forEach(customer -> System.out.println("    " + customer));
        System.out.println("  Youngest first, then by name: " + customers.stream()
                .sorted(Comparator.comparingInt(Customer::getAge).thenComparing(Customer::getName))
                .limit(4).map(Customer::getName).toList());

        System.out.println();
        System.out.println("--- Pipelines: filter, map, distinct, sorted ---");
        System.out.println("  Cities, sorted, no repeats: " + customers.stream()
                .map(Customer::getCity).distinct().sorted().toList());
        System.out.println("  Email domains            : " + customers.stream()
                .map(Customer::getEmailDomain).distinct().sorted().toList());
        System.out.println("  How many are active      : " + customers.stream().filter(Customer::isActive).count());

        System.out.println();
        System.out.println("--- Questions with a yes or no answer ---");
        System.out.println("  Anyone over 55?           " + customers.stream().anyMatch(c -> c.getAge() > 55));
        System.out.println("  Everyone spent something? " + customers.stream()
                .allMatch(c -> c.getTotalSpent().signum() > 0));
        System.out.println("  Nobody under 18?          " + customers.stream().noneMatch(c -> c.getAge() < 18));

        System.out.println();
        System.out.println("--- Adding things up ---");
        BigDecimal total = customers.stream()
                .map(Customer::getTotalSpent)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("  Total spent: " + total);
        System.out.println("  Average per customer: " + total.divide(BigDecimal.valueOf(customers.size()), 2, RoundingMode.HALF_UP));
        IntSummaryStatistics ages = customers.stream().mapToInt(Customer::getAge).summaryStatistics();
        System.out.println("  Ages: min " + ages.getMin() + ", max " + ages.getMax()
                + ", average " + ages.getAverage() + ", count " + ages.getCount());

        System.out.println();
        System.out.println("--- Your own functional interface ---");
        Discount tenPercent = amount -> amount.multiply(new BigDecimal("0.90")).setScale(2, RoundingMode.HALF_UP);
        BigDecimal fiftyOff = new BigDecimal("50.00");
        Discount voucher = amount -> amount.subtract(fiftyOff).max(BigDecimal.ZERO);
        BigDecimal basket = new BigDecimal("400.00");
        System.out.println("  Basket " + basket + ": 10 percent -> " + tenPercent.apply(basket)
                + ", voucher -> " + voucher.apply(basket)
                + ", both -> " + tenPercent.then(voucher).apply(basket));

        System.out.println();
        System.out.println("--- Changing a list in place ---");
        List<String> tags = new ArrayList<>(List.of("vip", "new", "pune", "new"));
        tags.replaceAll(String::toUpperCase);
        tags.removeIf(tag -> tag.equals("NEW"));
        System.out.println("  " + tags);

        System.out.println();
        System.out.println("--- Streams are lazy, and used once ---");
        Stream<String> lazy = customers.stream()
                .filter(c -> {
                    System.out.println("    checking " + c.getName());
                    return c.getAge() > 30;
                })
                .map(Customer::getName);
        System.out.println("  Nothing has been checked yet. Asking for the first match:");
        System.out.println("  First over 30: " + lazy.findFirst().orElse("nobody"));
        try {
            lazy.count();
        } catch (IllegalStateException e) {
            System.out.println("  Using the same stream again: " + e.getClass().getSimpleName());
        }
    }

    /** Names of the customers that pass a test. The test arrives as a parameter, which is what lambdas are for. */
    private static List<String> names(List<Customer> customers, Predicate<Customer> test) {
        return customers.stream().filter(test).map(Customer::getName).toList();
    }
}
