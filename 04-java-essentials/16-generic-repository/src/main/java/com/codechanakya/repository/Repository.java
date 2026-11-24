package com.codechanakya.repository;

import java.util.List;

/**
 * A place to store and find things of one type.
 *
 * Two type parameters: T is what is stored, ID is how it is looked up.
 * "T extends Identifiable&lt;ID&gt;" is a bound. It restricts T to types that have
 * a getId() of the matching kind, which is what lets the repository use
 * item.getId() without knowing anything else about T.
 *
 * Write this once and you have a repository for customers, products, orders
 * and everything you add later.
 */
public interface Repository<T extends Identifiable<ID>, ID> {

    /** Stores the item. An item with the same id replaces the old one. */
    void save(T item);

    /** Returns the item with this id, or null when there is none. */
    T findById(ID id);

    List<T> findAll();

    /** Removes the item. Returns false when nothing had that id. */
    boolean delete(ID id);

    int count();
}
