package com.codechanakya.repository;

import java.util.List;

/** Small generic helpers, one for each flavour of generic method. */
public final class Util {

    private Util() {
    }

    /**
     * The largest item in a non-empty list.
     *
     * "T extends Comparable&lt;T&gt;" is the bound: only types that can be compared
     * with themselves are allowed, because the method calls compareTo.
     * Strings, Integers and our Product qualify. A type with no ordering, such
     * as Customer, is rejected by the compiler.
     */
    public static <T extends Comparable<T>> T maxOf(List<T> items) {
        T best = items.get(0);
        for (T item : items) {
            if (item.compareTo(best) > 0) {
                best = item;
            }
        }
        return best;
    }

    /** Swaps two positions in an array of any reference type. The compiler works out T from the argument. */
    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Adds up any list of numbers as doubles. Fine for counts and measurements,
     * never for money; use BigDecimal for that.
     *
     * "? extends Number" is a wildcard: a list of some unknown type that is
     * a Number. We can read Numbers out of it, but could not safely add to it.
     */
    public static double sum(List<? extends Number> numbers) {
        double total = 0;
        for (Number number : numbers) {
            total += number.doubleValue();
        }
        return total;
    }

    /** Prints a list of anything. "?" means "I do not care what the elements are". */
    public static void printAll(String title, List<?> items) {
        System.out.println(title + " (" + items.size() + ")");
        for (Object item : items) {
            System.out.println("  " + item);
        }
    }
}
