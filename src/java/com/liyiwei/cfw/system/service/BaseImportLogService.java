/*
 * Created on 2013-04-27 15:09:47
 *
 */
package com.liyiwei.cfw.system.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.util.BaseDao;
import com.liyiwei.cfw.system.entity.ImportLog;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseImportLogService {
	protected BaseDao<ImportLog> importLogDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		importLogDao = new BaseDao<ImportLog>(sessionFactory, ImportLog.class);
	}

	public List<ImportLog> getImportLogList() {
		return importLogDao.findAll();
	}

	public List<ImportLog> getImportLogList(int currentPage, int pageSize) {
		String hql = "from ImportLog importLog order by importLog.id";
		return importLogDao.find(hql, currentPage, pageSize);
	}

	public List<ImportLog> getImportLogList(String condition) {
		String hql = "from ImportLog importLog " + condition;
		return importLogDao.find(hql);
	}

	public List<ImportLog> getImportLogList(String condition, int currentPage, int pageSize) {
		String hql = "from ImportLog importLog " + condition;
		return importLogDao.find(hql, currentPage, pageSize);
	}

	public int getImportLogCount() {
		String hql = "select count(importLog.id) from ImportLog importLog";
		return importLogDao.findLong(hql).intValue();
	}

	public int getImportLogCount(String condition) {
		String hql = "select count(importLog.id) from ImportLog importLog " + condition;
		return importLogDao.findLong(hql).intValue();
	}

	public ImportLog getImportLog(int id) {
		return importLogDao.get(id);
	}

	public ImportLog getImportLog(String condition) {
		String hql = "from ImportLog importLog " + condition;
		List<ImportLog> list = importLogDao.find(hql);
		if (list != null && list.size() > 0) {
			return (ImportLog) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addImportLog(ImportLog importLog) {
		importLogDao.save(importLog);
	}

	@Transactional(readOnly = false)
	public void modifyImportLog(ImportLog importLog) {
		importLogDao.saveOrUpdate(importLog);
	}

	@Transactional(readOnly = false)
	public void modifyImportLog(String condition) {
		String hql = "update ImportLog importLog set " + condition;
		importLogDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void modifyImportLog(int id, String field, String value) {
		this.modifyImportLog(" importLog." + field + "='" + value + "' where importLog.id=" + id);
	}

	@Transactional(readOnly = false)
	public void deleteImportLog(int id) {
		importLogDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteImportLog(String condition) {
		String hql = "delete from ImportLog importLog " + condition;
		importLogDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteImportLogByIds(String ids) {
		String hql = "delete from ImportLog importLog where importLog.id in(" + ids + ")";
		importLogDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		importLogDao.batchExecute(hql);
	}
}