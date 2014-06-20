/*
 * Created on 2013-02-19 17:12:29
 *
 */
package com.liyiwei.cfw.privilege.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.privilege.entity.Privilege;
import com.liyiwei.cfw.util.BaseDao;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BasePrivilegeService {
	protected BaseDao<Privilege> privilegeDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		privilegeDao = new BaseDao<Privilege>(sessionFactory, Privilege.class);
	}

	public List<Privilege> getPrivilegeList() {
		return privilegeDao.findAll();
	}

	public List<Privilege> getPrivilegeList(int currentPage, int pageSize) {
		String hql = "from Privilege privilege order by privilege.id";
		return privilegeDao.find(hql, currentPage, pageSize);
	}

	public List<Privilege> getPrivilegeList(String condition) {
		String hql = "from Privilege privilege " + condition;
		return privilegeDao.find(hql);
	}

	public List<Privilege> getPrivilegeList(String condition, int currentPage, int pageSize) {
		String hql = "from Privilege privilege " + condition;
		return privilegeDao.find(hql, currentPage, pageSize);
	}

	public int getPrivilegeCount() {
		String hql = "select count(privilege.id) from Privilege privilege";
		return privilegeDao.findLong(hql).intValue();
	}

	public int getPrivilegeCount(String condition) {
		String hql = "select count(privilege.id) from Privilege privilege " + condition;
		return privilegeDao.findLong(hql).intValue();
	}

	public Privilege getPrivilege(int id) {
		return privilegeDao.get(id);
	}

	public Privilege getPrivilege(String condition) {
		String hql = "from Privilege privilege " + condition;
		List<Privilege> list = privilegeDao.find(hql);
		if (list != null && list.size() > 0) {
			return (Privilege) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addPrivilege(Privilege privilege) {
		privilegeDao.save(privilege);
	}

	@Transactional(readOnly = false)
	public void modifyPrivilege(Privilege privilege) {
		privilegeDao.saveOrUpdate(privilege);
	}

	@Transactional(readOnly = false)
	public void modifyPrivilege(String condition) {
		String hql = "update Privilege privilege set " + condition;
		privilegeDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void modifyPrivilege(int id, String field, String value) {
		this.modifyPrivilege(" privilege." + field + "='" + value + "' where privilege.id=" + id);
	}

	@Transactional(readOnly = false)
	public void deletePrivilege(int id) {
		privilegeDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deletePrivilege(String condition) {
		String hql = "delete from Privilege privilege " + condition;
		privilegeDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deletePrivilegeByIds(String ids) {
		String hql = "delete from Privilege privilege where privilege.id in(" + ids + ")";
		privilegeDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		privilegeDao.batchExecute(hql);
	}
}