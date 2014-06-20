/*
 * Created on 2013-02-18 15:20:24
 *
 */
package com.liyiwei.cfw.system.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.util.BaseDao;
import com.liyiwei.cfw.system.entity.Menu;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseMenuService {
	protected BaseDao<Menu> menuDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		menuDao = new BaseDao<Menu>(sessionFactory, Menu.class);
	}

	public List<Menu> getMenuList() {
		return menuDao.findAll();
	}

	public List<Menu> getMenuList(int currentPage, int pageSize) {
		String hql = "from Menu menu order by menu.id";
		return menuDao.find(hql, currentPage, pageSize);
	}

	public List<Menu> getMenuList(String condition) {
		String hql = "from Menu menu " + condition;
		return menuDao.find(hql);
	}

	public List<Menu> getMenuList(String condition, int currentPage, int pageSize) {
		String hql = "from Menu menu " + condition;
		return menuDao.find(hql, currentPage, pageSize);
	}

	public int getMenuCount() {
		String hql = "select count(menu.id) from Menu menu";
		return menuDao.findLong(hql).intValue();
	}

	public int getMenuCount(String condition) {
		String hql = "select count(menu.id) from Menu menu " + condition;
		return menuDao.findLong(hql).intValue();
	}

	public Menu getMenu(int id) {
		return menuDao.get(id);
	}

	public Menu getMenu(String condition) {
		String hql = "from Menu menu " + condition;
		List<Menu> list = menuDao.find(hql);
		if (list != null && list.size() > 0) {
			return (Menu) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addMenu(Menu menu) {
		menuDao.save(menu);
	}

	@Transactional(readOnly = false)
	public void modifyMenu(Menu menu) {
		menuDao.saveOrUpdate(menu);
	}

	@Transactional(readOnly = false)
	public void modifyMenu(String condition) {
		String hql = "update Menu menu set " + condition;
		menuDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void modifyMenu(int id, String field, String value) {
		this.modifyMenu(" menu." + field + "='" + value + "' where menu.id=" + id);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(int id) {
		menuDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteMenu(String condition) {
		String hql = "delete from Menu menu " + condition;
		menuDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteMenuByIds(String ids) {
		String hql = "delete from Menu menu where menu.id in(" + ids + ")";
		menuDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		menuDao.batchExecute(hql);
	}
}