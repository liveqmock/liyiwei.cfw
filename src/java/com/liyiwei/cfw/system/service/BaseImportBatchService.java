/*
 * Created on 2013-04-27 15:09:15
 *
 */
package com.liyiwei.cfw.system.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.util.BaseDao;
import com.liyiwei.cfw.system.entity.ImportBatch;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseImportBatchService {
	protected BaseDao<ImportBatch> importBatchDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		importBatchDao = new BaseDao<ImportBatch>(sessionFactory, ImportBatch.class);
	}

	public List<ImportBatch> getImportBatchList() {
		return importBatchDao.findAll();
	}

	public List<ImportBatch> getImportBatchList(int currentPage, int pageSize) {
		String hql = "from ImportBatch importBatch order by importBatch.id";
		return importBatchDao.find(hql, currentPage, pageSize);
	}

	public List<ImportBatch> getImportBatchList(String condition) {
		String hql = "from ImportBatch importBatch " + condition;
		return importBatchDao.find(hql);
	}

	public List<ImportBatch> getImportBatchList(String condition, int currentPage, int pageSize) {
		String hql = "from ImportBatch importBatch " + condition;
		return importBatchDao.find(hql, currentPage, pageSize);
	}

	public int getImportBatchCount() {
		String hql = "select count(importBatch.id) from ImportBatch importBatch";
		return importBatchDao.findLong(hql).intValue();
	}

	public int getImportBatchCount(String condition) {
		String hql = "select count(importBatch.id) from ImportBatch importBatch " + condition;
		return importBatchDao.findLong(hql).intValue();
	}

	public ImportBatch getImportBatch(int id) {
		return importBatchDao.get(id);
	}

	public ImportBatch getImportBatch(String condition) {
		String hql = "from ImportBatch importBatch " + condition;
		List<ImportBatch> list = importBatchDao.find(hql);
		if (list != null && list.size() > 0) {
			return (ImportBatch) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addImportBatch(ImportBatch importBatch) {
		importBatchDao.save(importBatch);
	}

	@Transactional(readOnly = false)
	public void modifyImportBatch(ImportBatch importBatch) {
		importBatchDao.saveOrUpdate(importBatch);
	}

	@Transactional(readOnly = false)
	public void modifyImportBatch(String condition) {
		String hql = "update ImportBatch importBatch set " + condition;
		importBatchDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void modifyImportBatch(int id, String field, String value) {
		this.modifyImportBatch(" importBatch." + field + "='" + value + "' where importBatch.id=" + id);
	}

	@Transactional(readOnly = false)
	public void deleteImportBatch(int id) {
		importBatchDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteImportBatch(String condition) {
		String hql = "delete from ImportBatch importBatch " + condition;
		importBatchDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteImportBatchByIds(String ids) {
		String hql = "delete from ImportBatch importBatch where importBatch.id in(" + ids + ")";
		importBatchDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		importBatchDao.batchExecute(hql);
	}
}