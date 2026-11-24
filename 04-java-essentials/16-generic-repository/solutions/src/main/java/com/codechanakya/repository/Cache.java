package com.codechanakya.repository;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * C1: a small generic cache that keeps only the most recent entries.
 *
 * K is the key type and V the value type. When it is full, adding one more
 * pushes out the entry that was added first.
 */
public class Cache<K, V> {

    private final int capacity;
    private final Map<K, V> entries = new LinkedHashMap<>();

    public Cache(int capacity) {
        this.capacity = capacity;
    }

    public void put(K key, V value) {
        entries.remove(key);   // re-adding a key makes it the newest
        if (entries.size() == capacity) {
            K oldest = entries.keySet().iterator().next();
            entries.remove(oldest);
        }
        entries.put(key, value);
    }

    /** Returns the cached value, or null when it was never stored or has been pushed out. */
    public V get(K key) {
        return entries.get(key);
    }

    public int size() {
        return entries.size();
    }
}
