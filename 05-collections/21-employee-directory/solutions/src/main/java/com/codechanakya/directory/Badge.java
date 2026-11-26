package com.codechanakya.directory;

import java.util.Objects;

/**
 * C1: a key made of two parts, a site and a number.
 * Both fields are final, so the hash code can never change while the badge sits in a map.
 */
public class Badge {

    private final String site;
    private final int number;

    public Badge(String site, int number) {
        this.site = site;
        this.number = number;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Badge)) {
            return false;
        }
        Badge that = (Badge) other;
        return number == that.number && site.equals(that.site);
    }

    @Override
    public int hashCode() {
        return Objects.hash(site, number);
    }

    @Override
    public String toString() {
        return site + "-" + number;
    }
}
