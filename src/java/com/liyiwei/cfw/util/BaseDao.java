/*
 * Created on 2012-04-02 21:50:00
 *
 */
package com.liyiwei.cfw.util;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * @author Liyiwei
 * 
 */
@SuppressWarnings("unchecked")
public class BaseDao<T> {
	protected SessionFactory sessionFactory;

	protected Class<T> entityClass;

	public BaseDao(SessionFactory sessionFactory, Class<T> entityClass) {
		this.sessionFactory = sessionFactory;
		this.entityClass = entityClass;
	}

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void flush() {
		getSession().flush();
	}

	public void save(T entity) {
		getSession().save(entity);
	}

	public void saveOrUpdate(T entity) {
		getSession().saveOrUpdate(entity);
	}

	public void merge(T entity) {
		getSession().merge(entity);
	}

	public void delete(T entity) {
		getSession().delete(entity);
	}

	public void delete(int id) {
		delete(get(id));
	}

	public List<T> findAll() {
		return findByCriteria();
	}

	public List<T> findAll(Order defaultOrder) {
		Criteria c = createCriteria();
		c.addOrder(defaultOrder);
		return c.list();
	}

	public List<T> findAll(int currentPage, int pageSize) {
		return findByCriteria(currentPage, pageSize);
	}

	public T get(int id) {
		return (T) getSession().load(entityClass, id);
	}

	public int batchExecute(final String hql, final Object... values) {
		return createQuery(hql, values).executeUpdate();
	}

	public List<T> find(String hql, Object... values) {
		return createQuery(hql, values).list();
	}

	public List<T> find(String hql, int currentPage, int pageSize, Object... values) {
		Query q = createQuery(hql, values);
		q.setFirstResult((currentPage - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q.list();
	}

	public Object findUnique(String hql, Object... values) {
		return createQuery(hql, values).uniqueResult();
	}

	public List<T> findByCriteria(Criterion... criterion) {
		return createCriteria(criterion).list();
	}

	public List<T> findByCriteria(int currentPage, int pageSize, Criterion... criterion) {
		Criteria c = createCriteria(criterion);
		c.setFirstResult((currentPage - 1) * pageSize);
		c.setMaxResults(pageSize);

		return c.list();
	}

	public List<T> findByProperty(String propertyName, Object value) {
		return createCriteria(Restrictions.eq(propertyName, value)).list();
	}

	public T findUniqueByProperty(String propertyName, Object value) {
		return (T) createCriteria(Restrictions.eq(propertyName, value)).uniqueResult();
	}

	public Integer findInt(String hql, Object... values) {
		return (Integer) findUnique(hql, values);
	}

	public Long findLong(String hql, Object... values) {
		return (Long) findUnique(hql, values);
	}

	public Query createQuery(String queryString, Object... values) {
		Query queryObject = getSession().createQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}

	public int findQueryInt(String hql, List<Object> list) {
		return ((Long) createQueryList(hql, list).uniqueResult()).intValue();
	}

	public List<T> query(String hql, int currentPage, int pageSize, List<Object> list) {
		Query q = createQueryList(hql, list);
		q.setFirstResult((currentPage - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q.list();
	}

	public Query createQueryList(String queryString, List<Object> list) {
		Query queryObject = getSession().createQuery(queryString);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				queryObject.setParameter(i, list.get(i));
			}
		}
		return queryObject;
	}

	public Query createQueryList(String queryString, Map<String, Object> map) {
		Query queryObject = getSession().createQuery(queryString);

		if (map != null) {
			Set<String> set = map.keySet();
			for (String key : set) {
				queryObject.setParameter(key, map.get(key));
			}
		}
		return queryObject;
	}

	public Query createSQLQuery(String queryString, Object... values) {
		Query queryObject = getSession().createSQLQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}

	public Criteria createCriteria(Criterion... criterions) {
		Criteria criteria = getSession().createCriteria(entityClass);
		for (Criterion c : criterions) {
			criteria.add(c);
		}
		return criteria;
	}
}