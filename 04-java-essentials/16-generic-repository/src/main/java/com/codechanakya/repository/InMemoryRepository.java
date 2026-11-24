package com.codechanakya.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A repository that keeps everything in memory.
 *
 * A Map stores values under keys. LinkedHashMap remembers the order things
 * were added, so listings come out in a predictable order. Phase 5 covers maps.
 * A database-backed repository in Phase 9 will implement the same interface,
 * and code that uses Repository will not need to change.
 */
public class InMemoryRepository<T extends Identifiable<ID>, ID> implements Repository<T, ID> {

    private final Map<ID, T> items = new LinkedHashMap<>();

    @Override
    public void save(T item) {
        items.put(item.getId(), item);
    }

    @Override
    public T findById(ID id) {
        return items.get(id);
    }

    @Override
    public List<T> findAll() {
        // A copy, so callers cannot change our storage by editing the list they get.
        return new ArrayList<>(items.values());
    }

    @Override
    public boolean delete(ID id) {
        return items.remove(id) != null;
    }

    @Override
    public int count() {
        return items.size();
    }
}
