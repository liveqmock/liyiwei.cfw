/*
 * Created on 2013-02-06 15:55:50
 *
 */
package com.liyiwei.cfw.system.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.util.BaseDao;
import com.liyiwei.cfw.system.entity.SystemSetting;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseSystemSettingService {
	protected BaseDao<SystemSetting> systemSettingDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		systemSettingDao = new BaseDao<SystemSetting>(sessionFactory, SystemSetting.class);
	}

	public List<SystemSetting> getSystemSettingList() {
		return systemSettingDao.findAll();
	}

	public List<SystemSetting> getSystemSettingList(int currentPage, int pageSize) {
		String hql = "from SystemSetting systemSetting order by systemSetting.id";
		return systemSettingDao.find(hql, currentPage, pageSize);
	}

	public List<SystemSetting> getSystemSettingList(String condition) {
		String hql = "from SystemSetting systemSetting " + condition;
		return systemSettingDao.find(hql);
	}

	public List<SystemSetting> getSystemSettingList(String condition, int currentPage, int pageSize) {
		String hql = "from SystemSetting systemSetting " + condition;
		return systemSettingDao.find(hql, currentPage, pageSize);
	}

	public int getSystemSettingCount() {
		String hql = "select count(systemSetting.id) from SystemSetting systemSetting";
		return systemSettingDao.findLong(hql).intValue();
	}

	public int getSystemSettingCount(String condition) {
		String hql = "select count(systemSetting.id) from SystemSetting systemSetting " + condition;
		return systemSettingDao.findLong(hql).intValue();
	}

	public SystemSetting getSystemSetting(int id) {
		return systemSettingDao.get(id);
	}

	public SystemSetting getSystemSetting(String condition) {
		String hql = "from SystemSetting systemSetting " + condition;
		List<SystemSetting> list = systemSettingDao.find(hql);
		if (list != null && list.size() > 0) {
			return (SystemSetting) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addSystemSetting(SystemSetting systemSetting) {
		systemSettingDao.save(systemSetting);
	}

	@Transactional(readOnly = false)
	public void modifySystemSetting(SystemSetting systemSetting) {
		systemSettingDao.saveOrUpdate(systemSetting);
	}

	@Transactional(readOnly = false)
	public void modifySystemSetting(String condition) {
		String hql = "update SystemSetting systemSetting set " + condition;
		systemSettingDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void modifySystemSetting(int id, String field, String value) {
		this.modifySystemSetting(" systemSetting." + field + "='" + value + "' where systemSetting.id=" + id);
	}

	@Transactional(readOnly = false)
	public void deleteSystemSetting(int id) {
		systemSettingDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteSystemSetting(String condition) {
		String hql = "delete from SystemSetting systemSetting " + condition;
		systemSettingDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteSystemSettingByIds(String ids) {
		String hql = "delete from SystemSetting systemSetting where systemSetting.id in(" + ids + ")";
		systemSettingDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		systemSettingDao.batchExecute(hql);
	}
}