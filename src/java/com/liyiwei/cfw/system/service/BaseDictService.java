/*
 * Created on 2013-02-06 16:45:33
 *
 */
package com.liyiwei.cfw.system.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.util.BaseDao;
import com.liyiwei.cfw.system.entity.Dict;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseDictService {
	protected BaseDao<Dict> dictDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		dictDao = new BaseDao<Dict>(sessionFactory, Dict.class);
	}

	public List<Dict> getDictList() {
		return dictDao.findAll();
	}

	public List<Dict> getDictList(int currentPage, int pageSize) {
		String hql = "from Dict dict order by dict.id";
		return dictDao.find(hql, currentPage, pageSize);
	}

	public List<Dict> getDictList(String condition) {
		String hql = "from Dict dict " + condition;
		return dictDao.find(hql);
	}

	public List<Dict> getDictList(String condition, int currentPage, int pageSize) {
		String hql = "from Dict dict " + condition;
		return dictDao.find(hql, currentPage, pageSize);
	}

	public int getDictCount() {
		String hql = "select count(dict.id) from Dict dict";
		return dictDao.findLong(hql).intValue();
	}

	public int getDictCount(String condition) {
		String hql = "select count(dict.id) from Dict dict " + condition;
		return dictDao.findLong(hql).intValue();
	}

	public Dict getDict(int id) {
		return dictDao.get(id);
	}

	public Dict getDict(String condition) {
		String hql = "from Dict dict " + condition;
		List<Dict> list = dictDao.find(hql);
		if (list != null && list.size() > 0) {
			return (Dict) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addDict(Dict dict) {
		dictDao.save(dict);
	}

	@Transactional(readOnly = false)
	public void modifyDict(Dict dict) {
		dictDao.saveOrUpdate(dict);
	}

	@Transactional(readOnly = false)
	public void modifyDict(String condition) {
		String hql = "update Dict dict set " + condition;
		dictDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void modifyDict(int id, String field, String value) {
		this.modifyDict(" dict." + field + "='" + value + "' where dict.id=" + id);
	}

	@Transactional(readOnly = false)
	public void deleteDict(int id) {
		dictDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteDict(String condition) {
		String hql = "delete from Dict dict " + condition;
		dictDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteDictByIds(String ids) {
		String hql = "delete from Dict dict where dict.id in(" + ids + ")";
		dictDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		dictDao.batchExecute(hql);
	}
}