/*
 * Created on 2013-01-09 17:45:42
 *
 */
package com.liyiwei.cfw.user.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.user.entity.User;
import com.liyiwei.cfw.util.BaseDao;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseUserService {
	protected BaseDao<User> userDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		userDao = new BaseDao<User>(sessionFactory, User.class);
	}

	public List<User> getUserList() {
		return userDao.findAll();
	}

	public List<User> getUserList(int currentPage, int pageSize) {
		String hql = "from User user order by user.id";
		return userDao.find(hql, currentPage, pageSize);
	}

	public List<User> getUserList(String condition) {
		String hql = "from User user " + condition;
		return userDao.find(hql);
	}

	public List<User> getUserList(String condition, int currentPage, int pageSize) {
		String hql = "from User user " + condition;
		return userDao.find(hql, currentPage, pageSize);
	}

	public int getUserCount() {
		String hql = "select count(user.id) from User user";
		return userDao.findLong(hql).intValue();
	}

	public int getUserCount(String condition) {
		String hql = "select count(user.id) from User user " + condition;
		return userDao.findLong(hql).intValue();
	}

	public User getUser(int id) {
		return userDao.get(id);
	}

	public User getUser(String condition) {
		String hql = "from User user " + condition;
		List<User> list = userDao.find(hql);
		if (list != null && list.size() > 0) {
			return (User) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addUser(User user) {
		userDao.save(user);
	}

	@Transactional(readOnly = false)
	public void modifyUser(User user) {
		userDao.saveOrUpdate(user);
	}

	@Transactional(readOnly = false)
	public void deleteUser(int id) {
		userDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteUser(String ids) {
		String hql = "delete from User user where user.id in(" + ids + ")";
		userDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		userDao.batchExecute(hql);
	}
}