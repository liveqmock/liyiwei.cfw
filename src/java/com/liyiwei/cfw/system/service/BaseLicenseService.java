/*
 * Created on 2014-04-10 15:25:29
 *
 */
package com.liyiwei.cfw.system.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.util.BaseDao;
import com.liyiwei.cfw.system.entity.License;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseLicenseService {
	protected BaseDao<License> licenseDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		licenseDao = new BaseDao<License>(sessionFactory, License.class);
	}

	public List<License> getLicenseList() {
		return licenseDao.findAll();
	}

	public List<License> getLicenseList(int currentPage, int pageSize) {
		String hql = "from License license order by license.id";
		return licenseDao.find(hql, currentPage, pageSize);
	}

	public List<License> getLicenseList(String condition) {
		String hql = "from License license " + condition;
		return licenseDao.find(hql);
	}

	public List<License> getLicenseList(String condition, int currentPage, int pageSize) {
		String hql = "from License license " + condition;
		return licenseDao.find(hql, currentPage, pageSize);
	}

	public int getLicenseCount() {
		String hql = "select count(license.id) from License license";
		return licenseDao.findLong(hql).intValue();
	}

	public int getLicenseCount(String condition) {
		String hql = "select count(license.id) from License license " + condition;
		return licenseDao.findLong(hql).intValue();
	}

	public License getLicense(int id) {
		return licenseDao.get(id);
	}

	public License getLicense(String condition) {
		String hql = "from License license " + condition;
		List<License> list = licenseDao.find(hql);
		if (list != null && list.size() > 0) {
			return (License) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addLicense(License license) {
		licenseDao.save(license);
	}

	@Transactional(readOnly = false)
	public void modifyLicense(License license) {
		licenseDao.saveOrUpdate(license);
	}

	@Transactional(readOnly = false)
	public void modifyLicense(String condition) {
		String hql = "update License license set " + condition;
		licenseDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void modifyLicense(int id, String field, String value) {
		this.modifyLicense(" license." + field + "='" + value + "' where license.id=" + id);
	}

	@Transactional(readOnly = false)
	public void deleteLicense(int id) {
		licenseDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteLicense(String condition) {
		String hql = "delete from License license " + condition;
		licenseDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteLicenseByIds(String ids) {
		String hql = "delete from License license where license.id in(" + ids + ")";
		licenseDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		licenseDao.batchExecute(hql);
	}
}