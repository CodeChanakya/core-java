package com.codechanakya.repository;

/**
 * Two values held together, each with its own type.
 *
 * A generic class, not just a generic interface. Pair&lt;String, Integer&gt; and
 * Pair&lt;Customer, Product&gt; are different types made from the same code, and
 * the compiler checks you never mix them up.
 */
public class Pair<A, B> {

    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
