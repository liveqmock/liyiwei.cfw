/*
 * Created on 2013-04-17 16:59:03
 *
 */
package com.liyiwei.cfw.base.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.base.entity.Category;
import com.liyiwei.cfw.util.BaseDao;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseCategoryService {
	protected BaseDao<Category> categoryDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		categoryDao = new BaseDao<Category>(sessionFactory, Category.class);
	}

	public List<Category> getCategoryList() {
		return categoryDao.findAll();
	}

	public List<Category> getCategoryList(int currentPage, int pageSize) {
		String hql = "from Category category order by category.id";
		return categoryDao.find(hql, currentPage, pageSize);
	}

	public List<Category> getCategoryList(String condition) {
		String hql = "from Category category " + condition;
		return categoryDao.find(hql);
	}

	public List<Category> getCategoryList(String condition, int currentPage, int pageSize) {
		String hql = "from Category category " + condition;
		return categoryDao.find(hql, currentPage, pageSize);
	}

	public int getCategoryCount() {
		String hql = "select count(category.id) from Category category";
		return categoryDao.findLong(hql).intValue();
	}

	public int getCategoryCount(String condition) {
		String hql = "select count(category.id) from Category category " + condition;
		return categoryDao.findLong(hql).intValue();
	}

	public Category getCategory(int id) {
		return categoryDao.get(id);
	}

	public Category getCategory(String condition) {
		String hql = "from Category category " + condition;
		List<Category> list = categoryDao.find(hql);
		if (list != null && list.size() > 0) {
			return (Category) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addCategory(Category category) {
		categoryDao.save(category);
	}

	@Transactional(readOnly = false)
	public void modifyCategory(Category category) {
		categoryDao.saveOrUpdate(category);
	}

	@Transactional(readOnly = false)
	public void modifyCategory(String condition) {
		String hql = "update Category category set " + condition;
		categoryDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void modifyCategory(int id, String field, String value) {
		this.modifyCategory(" category." + field + "='" + value + "' where category.id=" + id);
	}

	@Transactional(readOnly = false)
	public void deleteCategory(int id) {
		categoryDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteCategory(String condition) {
		String hql = "delete from Category category " + condition;
		categoryDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteCategoryByIds(String ids) {
		String hql = "delete from Category category where category.id in(" + ids + ")";
		categoryDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		categoryDao.batchExecute(hql);
	}
}