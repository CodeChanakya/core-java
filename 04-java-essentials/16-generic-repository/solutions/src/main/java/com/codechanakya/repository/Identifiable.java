package com.codechanakya.repository;

/**
 * Anything that has an id, where the type of the id is up to the implementer.
 *
 * The &lt;ID&gt; after the name is a type parameter: a placeholder filled in when
 * the interface is used. A Customer is Identifiable&lt;String&gt; (its id is text)
 * and a Product is Identifiable&lt;Integer&gt; (its id is a number).
 */
public interface Identifiable<ID> {

    ID getId();
}
