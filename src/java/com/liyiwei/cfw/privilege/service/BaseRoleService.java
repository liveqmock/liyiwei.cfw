/*
 * Created on 2013-01-15 17:32:33
 *
 */
package com.liyiwei.cfw.privilege.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.privilege.entity.Role;
import com.liyiwei.cfw.util.BaseDao;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseRoleService {
	protected BaseDao<Role> roleDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		roleDao = new BaseDao<Role>(sessionFactory, Role.class);
	}

	public List<Role> getRoleList() {
		return roleDao.findAll();
	}

	public List<Role> getRoleList(int currentPage, int pageSize) {
		String hql = "from Role role order by role.id";
		return roleDao.find(hql, currentPage, pageSize);
	}

	public List<Role> getRoleList(String condition) {
		String hql = "from Role role " + condition;
		return roleDao.find(hql);
	}

	public List<Role> getRoleList(String condition, int currentPage, int pageSize) {
		String hql = "from Role role " + condition;
		return roleDao.find(hql, currentPage, pageSize);
	}

	public int getRoleCount() {
		String hql = "select count(role.id) from Role role";
		return roleDao.findLong(hql).intValue();
	}

	public int getRoleCount(String condition) {
		String hql = "select count(role.id) from Role role " + condition;
		return roleDao.findLong(hql).intValue();
	}

	public Role getRole(int id) {
		return roleDao.get(id);
	}

	public Role getRole(String condition) {
		String hql = "from Role role " + condition;
		List<Role> list = roleDao.find(hql);
		if (list != null && list.size() > 0) {
			return (Role) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addRole(Role role) {
		roleDao.save(role);
	}

	@Transactional(readOnly = false)
	public void modifyRole(Role role) {
		roleDao.saveOrUpdate(role);
	}

	@Transactional(readOnly = false)
	public void deleteRole(int id) {
		roleDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteRole(String ids) {
		String hql = "delete from Role role where role.id in(" + ids + ")";
		roleDao.batchExecute(hql);
	}
}