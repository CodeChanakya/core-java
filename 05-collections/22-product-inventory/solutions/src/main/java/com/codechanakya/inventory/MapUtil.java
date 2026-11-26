package com.codechanakya.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Small generic helpers for maps whose values are lists. */
public final class MapUtil {

    private MapUtil() {
        // Only static methods; nobody should create one of these.
    }

    /**
     * Adds a value to the list stored under a key, creating the list on first use.
     * Works for any key type K and any value type V, which is the point of the two type parameters.
     */
    public static <K, V> void addToGroup(Map<K, List<V>> groups, K key, V value) {
        groups.computeIfAbsent(key, ignored -> new ArrayList<>()).add(value);
    }
}
