/*
 * Created on 2013-01-09 17:34:17
 *
 */
package com.liyiwei.cfw.user.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.user.entity.Department;
import com.liyiwei.cfw.util.BaseDao;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseDepartmentService {
	protected BaseDao<Department> departmentDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		departmentDao = new BaseDao<Department>(sessionFactory, Department.class);
	}

	public List<Department> getDepartmentList() {
		return departmentDao.findAll();
	}

	public List<Department> getDepartmentList(int currentPage, int pageSize) {
		String hql = "from Department department order by department.id";
		return departmentDao.find(hql, currentPage, pageSize);
	}

	public List<Department> getDepartmentList(String condition) {
		String hql = "from Department department " + condition;
		return departmentDao.find(hql);
	}

	public List<Department> getDepartmentList(String condition, int currentPage, int pageSize) {
		String hql = "from Department department " + condition;
		return departmentDao.find(hql, currentPage, pageSize);
	}

	public int getDepartmentCount() {
		String hql = "select count(department.id) from Department department";
		return departmentDao.findLong(hql).intValue();
	}

	public int getDepartmentCount(String condition) {
		String hql = "select count(department.id) from Department department " + condition;
		return departmentDao.findLong(hql).intValue();
	}

	public Department getDepartment(int id) {
		return departmentDao.get(id);
	}

	public Department getDepartment(String condition) {
		String hql = "from Department department " + condition;
		List<Department> list = departmentDao.find(hql);
		if (list != null && list.size() > 0) {
			return (Department) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addDepartment(Department department) {
		departmentDao.save(department);
	}

	@Transactional(readOnly = false)
	public void modifyDepartment(Department department) {
		departmentDao.saveOrUpdate(department);
	}

	@Transactional(readOnly = false)
	public void deleteDepartment(int id) {
		departmentDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteDepartment(String ids) {
		String hql = "delete from Department department where department.id in(" + ids + ")";
		departmentDao.batchExecute(hql);
	}
}