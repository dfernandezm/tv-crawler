package com.morenware.tvcrawler.persistence.dao;

import java.io.Serializable;

/**
 * Entities which wish to be managed using the GenericDao framework must implement
 * this interface so that their primary key can be interrogated and set.
 * 
 *
 * @param <K> primary key type.
 */
public interface Identifiable<K extends Serializable> extends Serializable
{
    public K getId();
    
    public void setId(K id);
}
