/*
 * Created on 2013-12-24 13:58:15
 *
 */
package com.liyiwei.cfw.base.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.util.BaseDao;
import com.liyiwei.cfw.base.entity.Area;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseAreaService {
	protected BaseDao<Area> areaDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		areaDao = new BaseDao<Area>(sessionFactory, Area.class);
	}

	public List<Area> getAreaList() {
		return areaDao.findAll();
	}

	public List<Area> getAreaList(int currentPage, int pageSize) {
		String hql = "from Area area order by area.id";
		return areaDao.find(hql, currentPage, pageSize);
	}

	public List<Area> getAreaList(String condition) {
		String hql = "from Area area " + condition;
		return areaDao.find(hql);
	}

	public List<Area> getAreaList(String condition, int currentPage, int pageSize) {
		String hql = "from Area area " + condition;
		return areaDao.find(hql, currentPage, pageSize);
	}

	public int getAreaCount() {
		String hql = "select count(area.id) from Area area";
		return areaDao.findLong(hql).intValue();
	}

	public int getAreaCount(String condition) {
		String hql = "select count(area.id) from Area area " + condition;
		return areaDao.findLong(hql).intValue();
	}

	public Area getArea(int id) {
		return areaDao.get(id);
	}

	public Area getArea(String condition) {
		String hql = "from Area area " + condition;
		List<Area> list = areaDao.find(hql);
		if (list != null && list.size() > 0) {
			return (Area) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addArea(Area area) {
		areaDao.save(area);
	}

	@Transactional(readOnly = false)
	public void modifyArea(Area area) {
		areaDao.saveOrUpdate(area);
	}

	@Transactional(readOnly = false)
	public void modifyArea(String condition) {
		String hql = "update Area area set " + condition;
		areaDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void modifyArea(int id, String field, String value) {
		this.modifyArea(" area." + field + "='" + value + "' where area.id=" + id);
	}

	@Transactional(readOnly = false)
	public void deleteArea(int id) {
		areaDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteArea(String condition) {
		String hql = "delete from Area area " + condition;
		areaDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteAreaByIds(String ids) {
		String hql = "delete from Area area where area.id in(" + ids + ")";
		areaDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		areaDao.batchExecute(hql);
	}
}