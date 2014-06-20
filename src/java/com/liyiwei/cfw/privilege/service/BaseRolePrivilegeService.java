/*
 * Created on 2013-02-19 17:14:46
 *
 */
package com.liyiwei.cfw.privilege.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.util.BaseDao;
import com.liyiwei.cfw.privilege.entity.RolePrivilege;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseRolePrivilegeService {
	protected BaseDao<RolePrivilege> rolePrivilegeDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		rolePrivilegeDao = new BaseDao<RolePrivilege>(sessionFactory, RolePrivilege.class);
	}

	public List<RolePrivilege> getRolePrivilegeList() {
		return rolePrivilegeDao.findAll();
	}

	public List<RolePrivilege> getRolePrivilegeList(int currentPage, int pageSize) {
		String hql = "from RolePrivilege rolePrivilege order by rolePrivilege.id";
		return rolePrivilegeDao.find(hql, currentPage, pageSize);
	}

	public List<RolePrivilege> getRolePrivilegeList(String condition) {
		String hql = "from RolePrivilege rolePrivilege " + condition;
		return rolePrivilegeDao.find(hql);
	}

	public List<RolePrivilege> getRolePrivilegeList(String condition, int currentPage, int pageSize) {
		String hql = "from RolePrivilege rolePrivilege " + condition;
		return rolePrivilegeDao.find(hql, currentPage, pageSize);
	}

	public int getRolePrivilegeCount() {
		String hql = "select count(rolePrivilege.id) from RolePrivilege rolePrivilege";
		return rolePrivilegeDao.findLong(hql).intValue();
	}

	public int getRolePrivilegeCount(String condition) {
		String hql = "select count(rolePrivilege.id) from RolePrivilege rolePrivilege " + condition;
		return rolePrivilegeDao.findLong(hql).intValue();
	}

	public RolePrivilege getRolePrivilege(int id) {
		return rolePrivilegeDao.get(id);
	}

	public RolePrivilege getRolePrivilege(String condition) {
		String hql = "from RolePrivilege rolePrivilege " + condition;
		List<RolePrivilege> list = rolePrivilegeDao.find(hql);
		if (list != null && list.size() > 0) {
			return (RolePrivilege) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addRolePrivilege(RolePrivilege rolePrivilege) {
		rolePrivilegeDao.save(rolePrivilege);
	}

	@Transactional(readOnly = false)
	public void modifyRolePrivilege(RolePrivilege rolePrivilege) {
		rolePrivilegeDao.saveOrUpdate(rolePrivilege);
	}

	@Transactional(readOnly = false)
	public void modifyRolePrivilege(String condition) {
		String hql = "update RolePrivilege rolePrivilege set " + condition;
		rolePrivilegeDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void modifyRolePrivilege(int id, String field, String value) {
		this.modifyRolePrivilege(" rolePrivilege." + field + "='" + value + "' where rolePrivilege.id=" + id);
	}

	@Transactional(readOnly = false)
	public void deleteRolePrivilege(int id) {
		rolePrivilegeDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteRolePrivilege(String condition) {
		String hql = "delete from RolePrivilege rolePrivilege " + condition;
		rolePrivilegeDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteRolePrivilegeByIds(String ids) {
		String hql = "delete from RolePrivilege rolePrivilege where rolePrivilege.id in(" + ids + ")";
		rolePrivilegeDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		rolePrivilegeDao.batchExecute(hql);
	}
}