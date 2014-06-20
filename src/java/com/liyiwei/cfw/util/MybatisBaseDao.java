/*/*
 * Created on 2012-04-02 21:50:00
 *
 */
package com.liyiwei.cfw.util;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Component;

/**
 * @author Liyiwei
 * 
 */
@Component
@SuppressWarnings("unchecked")
public class MybatisBaseDao<T> extends SqlSessionDaoSupport {
	public List<T> getList(String statement) {
		return (List<T>) getSqlSession().selectList(statement);
	}

	public List<T> getList(String statement, int currentPage, int pageSize) {
		return (List<T>) getSqlSession().selectList(statement, new RowBounds((currentPage - 1) * pageSize, pageSize));
	}

	public List<T> getList(String statement, String condition) {
		return (List<T>) getSqlSession().selectList(statement, condition);
	}

	public List<T> getList(String statement, Map<String, Object> map) {
		return (List<T>) getSqlSession().selectList(statement, map);
	}

	public List<T> getList(String statement, String condition, int currentPage, int pageSize) {
		return (List<T>) getSqlSession().selectList(statement, condition, new RowBounds((currentPage - 1) * pageSize, pageSize));
	}

	public T getOne(String statement) {
		// return (T) getSqlSession().selectOne(statement);
		List<T> list = (List<T>) getSqlSession().selectList(statement);
		if (list == null) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public T getOne(String statement, String condition) {
		// return (T) getSqlSession().selectOne(statement, condition);
		List<T> list = (List<T>) getSqlSession().selectList(statement, condition);
		if (list == null || list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public int getCount(String statement) {
		return (Integer) getSqlSession().selectOne(statement);
	}

	public int getCount(String statement, String condition) {
		return (Integer) getSqlSession().selectOne(statement, condition);
	}

	public void insert(String statement, T entity) {
		getSqlSession().insert(statement, entity);
	}

	public void update(String statement, T entity) {
		getSqlSession().update(statement, entity);
	}

	public void update(String statement, Map<String, Object> map) {
		getSqlSession().update(statement, map);
	}

	public void delete(String statement, String condition) {
		getSqlSession().delete(statement, condition);
	}
}