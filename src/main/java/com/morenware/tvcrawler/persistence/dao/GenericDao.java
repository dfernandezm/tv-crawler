package com.morenware.tvcrawler.persistence.dao;

import java.io.Serializable;
import java.util.Collection;

/**
 * Generic CRUD DAO functionality.
 * 
 * 
 * @param <E>
 *            entity type.
 * @param <K>
 *            primary key type of entity.
 */
public interface GenericDao<E extends Identifiable<K>, K extends Serializable> {

	/**
	 * Find an instance identified by the id specified.
	 * 
	 * @param id
	 *            The id of the required instance
	 * @return The instance with the id specified.
	 */
	public E find(K id);

	/**
	 * Find all instances managed by this data access object.
	 * 
	 * @return A List of instances managed by this data access object.
	 */
	public Collection<E> findAll();

	/**
	 * Flush all pending changes to the database.
	 */
	public void flush();

	/**
	 * Update the persisted state of the instance specified to bring it into
	 * line with that specified.
	 * 
	 * @param instance
	 *            The new state of the instance.
	 */
	public E merge(E instance);

	/**
	 * Persist the instance specified in the store.
	 * 
	 * @param instance
	 *            The instance to be persisted.
	 * @return The instance.
	 */
	public E persist(E instance);

	/**
	 * Remove the instance specified from the store.
	 * 
	 * @param instance
	 *            The instance to be removed.
	 */
	public void remove(E instance);

	/**
	 * Remove the instance specified by the id provided.
	 * 
	 * @param id
	 *            The id of the instance to be removed.
	 */
	public void remove(K id);

}
