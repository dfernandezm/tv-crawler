package com.morenware.tvcrawler.persistence.dao;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of a generic read write data access object.
 */
//@Repository("hibernateDao")
public class HibernateDaoImpl<E extends Identifiable<K>, K extends Serializable> implements HibernateDao<E, K> {

	/*
	 * Type of entities managed by this DAO.
	 */
	private Class<E> entityClass;

	/*
	 * Query to be used to retrieve all managed instances.
	 */
	private String findAllQuery;

	/*
	 * Hibernate SessionFactory.
	 */
	@Resource
	private SessionFactory sessionFactory;

	/**
	 * Evict the given entity from the hibernate session.
	 */
	@Override
	public void evict(E entity) {
		getSession().evict(entity);
	}

	/**
	 * Create a Hibernate named query.
	 *
	 * @param query
	 *            the query to create.
	 * @return the created query.
	 */
	@Override
	public Query createNamedQuery(String query) {
		return getSession().getNamedQuery(query);
	}

	/**
	 * Create a Hibernate query.
	 *
	 * @param query
	 *            the query to create.
	 * @return the created query.
	 */
	@Override
	public Query createQuery(String query) {
		return getSession().createQuery(query);
	}

	/**
	 * Create an SQL query.
	 *
	 * @param query
	 *            the query to create.
	 * @return the created query.
	 */
	@Override
	public SQLQuery createSQLQuery(String query) {
		return getSession().createSQLQuery(query);
	}

	@SuppressWarnings("unchecked")
	@Override
	public E find(K id) {
		return (E) getSession().get(getEntityClass(), id);
	}
	
	

	@SuppressWarnings(value = "unchecked")
	@Override
	public Collection<E> findAll() {
		Query query = getSession().createQuery(getFindAllQuery());
		Collection<E> results = query.list();
		return Collections.unmodifiableCollection(results);
	}

	

	@Override
	public void flush() {
		getSession().flush();
	}

	/**
	 * @return The class of entities managed by this service.
	 */
	@Override
	public Class<E> getEntityClass() {
		return entityClass;
	}

	/**
	 * @return The query used to retrieve all managed instances.
	 */
	public String getFindAllQuery() {
		return findAllQuery;
	}

	@Override
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public E merge(E instance) {
		return (E) getSession().merge(instance);
	}

	@Override
	public E persist(E instance) {

		getSession().persist(instance);
		return instance;
	}

	@Override
	public void remove(E instance) {
		getSession().delete(instance);
	}

	@Override
	public void remove(K id) {
		remove(find(id));
	}

	/**
	 * Set the class of entities managed by this service.
	 *
	 * @param entityClass
	 *            The new class of entities.
	 */
	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
		setFindAllQuery("select e from " + entityClass.getSimpleName() + " e");
	}

	/**
	 * Set the query to be used when retrieving all managed instances.
	 *
	 * @param findAllQuery
	 *            The new query.
	 */
	public void setFindAllQuery(String findAllQuery) {
		this.findAllQuery = findAllQuery;
	}
	

	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findByField(String fieldName, Object fieldValue) {
		
		Class<?> propertyClass = fieldValue.getClass();	
		
		List<E> list = (List<E>) getSession().createCriteria(entityClass)
	     .add(Restrictions.eq(fieldName, propertyClass.cast(fieldValue)))
	     .list();
	
	   return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public E findByUniqueField(String fieldName, Object fieldValue) {
		
		Class<?> propertyClass = fieldValue.getClass();	
		
		E entity = (E) getSession().createCriteria(entityClass)
	     .add(Restrictions.eq(fieldName, propertyClass.cast(fieldValue)))
	     .uniqueResult();
	
	   return entity;
	}

}