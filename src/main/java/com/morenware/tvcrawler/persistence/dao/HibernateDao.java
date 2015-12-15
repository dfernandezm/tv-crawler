package com.morenware.tvcrawler.persistence.dao;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

public interface HibernateDao<E extends Identifiable<K>, K extends Serializable> extends GenericDao<E, K> {

	public static final String ROOT_OBJECT_ALIAS = "root";

	/**
	 * Evict the given entity from the hibernate session.
	 */
	public void evict(E entity);

	/**
	 * Create a Hibernate query.
	 * 
	 * @param query
	 *            the query to create.
	 * @return the created query.
	 */
	public Query createNamedQuery(String query);

	/**
	 * Create a Hibernate named query.
	 * 
	 * @param query
	 *            the query to create.
	 * @return the created query.
	 */
	public Query createQuery(String query);

	/**
	 * Create an SQL query.
	 * 
	 * @param query
	 *            the query to create.
	 * @return the created query.
	 */
	public SQLQuery createSQLQuery(String query);


	/**
	 * Get the Hibernate Session.
	 * 
	 * @return the Hibernate Session.
	 */
	public Session getSession();

	/**
	 * Get the Hibernate SessionFactory.
	 * 
	 * @return the Hibernate Session.
	 */
	public SessionFactory getSessionFactory();

	/**
	 * Set the Hibernate SessionFactory.
	 */
	public void setSessionFactory(SessionFactory sessionFactory);

	/**
	 * Returns the entity class this DAO is used for.
	 */
	public Class<E> getEntityClass();
	
	/**
	 * Returns a java.util.List of entities with the specified fieldName equals to the fieldValue
	 * 
	 * @param fieldName
	 * @param fieldValue
	 * @return
	 */
	public List<E> findByField(String fieldName, Object fieldValue);
	
	public E findByUniqueField(String fieldName, Object fieldValue);
	
}