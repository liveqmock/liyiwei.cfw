/*
 * Created on 2013-02-17 19:33:51
 *
 */
package com.liyiwei.cfw.system.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.util.BaseDao;
import com.liyiwei.cfw.system.entity.ActionLog;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseActionLogService {
	protected BaseDao<ActionLog> actionLogDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		actionLogDao = new BaseDao<ActionLog>(sessionFactory, ActionLog.class);
	}

	public List<ActionLog> getActionLogList() {
		return actionLogDao.findAll();
	}

	public List<ActionLog> getActionLogList(int currentPage, int pageSize) {
		String hql = "from ActionLog actionLog order by actionLog.id";
		return actionLogDao.find(hql, currentPage, pageSize);
	}

	public List<ActionLog> getActionLogList(String condition) {
		String hql = "from ActionLog actionLog " + condition;
		return actionLogDao.find(hql);
	}

	public List<ActionLog> getActionLogList(String condition, int currentPage, int pageSize) {
		String hql = "from ActionLog actionLog " + condition;
		return actionLogDao.find(hql, currentPage, pageSize);
	}

	public int getActionLogCount() {
		String hql = "select count(actionLog.id) from ActionLog actionLog";
		return actionLogDao.findLong(hql).intValue();
	}

	public int getActionLogCount(String condition) {
		String hql = "select count(actionLog.id) from ActionLog actionLog " + condition;
		return actionLogDao.findLong(hql).intValue();
	}

	public ActionLog getActionLog(int id) {
		return actionLogDao.get(id);
	}

	public ActionLog getActionLog(String condition) {
		String hql = "from ActionLog actionLog " + condition;
		List<ActionLog> list = actionLogDao.find(hql);
		if (list != null && list.size() > 0) {
			return (ActionLog) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addActionLog(ActionLog actionLog) {
		actionLogDao.save(actionLog);
	}

	@Transactional(readOnly = false)
	public void modifyActionLog(ActionLog actionLog) {
		actionLogDao.saveOrUpdate(actionLog);
	}

	@Transactional(readOnly = false)
	public void modifyActionLog(String condition) {
		String hql = "update ActionLog actionLog set " + condition;
		actionLogDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void modifyActionLog(int id, String field, String value) {
		this.modifyActionLog(" actionLog." + field + "='" + value + "' where actionLog.id=" + id);
	}

	@Transactional(readOnly = false)
	public void deleteActionLog(int id) {
		actionLogDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteActionLog(String condition) {
		String hql = "delete from ActionLog actionLog " + condition;
		actionLogDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteActionLogByIds(String ids) {
		String hql = "delete from ActionLog actionLog where actionLog.id in(" + ids + ")";
		actionLogDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		actionLogDao.batchExecute(hql);
	}
}