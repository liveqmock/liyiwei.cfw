/*
 * Created on 2013-01-09 17:34:17
 *
 */
package com.liyiwei.cfw.user.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.user.entity.Department;
import com.liyiwei.common.cache.Cache;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Html;

/**
 * @author Liyiwei
 * 
 */
@Service
public class DepartmentService extends BaseDepartmentService {
	@Transactional(readOnly = false)
	public void upDepartmentSeq(int parentId, int sno) {
		List<Department> list = getDepartmentList("where  department.parentId=" + parentId + " order by  department.seq,department.id");
		int i = 1;
		for (Department department : list) {
			if (i == sno - 1) {
				department.setSeq(i + 1);
			} else if (i == sno) {
				department.setSeq(i - 1);
			} else {
				department.setSeq(i);
			}
			super.modifyDepartment(department);
			i++;
		}
		Cache.removeStartsWith("Department");
	}

	@Transactional(readOnly = false)
	public void downDepartmentSeq(int parentId, int sno) {
		List<Department> list = getDepartmentList("where  department.parentId=" + parentId + " order by  department.seq,department.id");
		int i = 1;
		for (Department department : list) {
			if (i == sno) {
				department.setSeq(i + 1);
			} else if (i == sno + 1) {
				department.setSeq(i - 1);
			} else {
				department.setSeq(i);
			}
			super.modifyDepartment(department);
			i++;
		}
		Cache.removeStartsWith("Department");
	}

	public String getDepartmentName(Integer id) {
		if (id == null) {
			return "";
		}

		Department department = getDepartment(id.intValue());
		return department == null ? "" : department.getName();
	}

	public String departmentSelectBox(String selectBoxName, String selectData) {
		return departmentSelectBox(selectBoxName, selectBoxName, selectData, "", "");
	}

	public String departmentSelectBox(String selectBoxName, String selectData, String message) {
		return departmentSelectBox(selectBoxName, selectBoxName, selectData, message, "");
	}

	public String departmentSelectBox(String selectBoxName, String selectBoxId, String selectData, String message, String other) {
		List<Department> list = getDepartmentList();
		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size() + 1][2];
			paras[0][0] = "";
			paras[0][1] = message;

			int i = 1;
			for (Department department : list) {
				paras[i][0] = String.valueOf(department.getId());
				paras[i][1] = department.getName();
				i++;
			}
			return Html.selectBox(selectBoxName, selectBoxId, paras, selectData, other);
		} else {
			String[][] paras = new String[1][2];
			paras[0][0] = "";
			paras[0][1] = message;
			return Html.selectBox(selectBoxName, selectBoxId, paras, selectData, other);
		}
	}

	@Override
	public Department getDepartment(int id) {
		return getDepartmentByCache(id);
	}

	@Override
	public List<Department> getDepartmentList() {
		return getDepartmentListByCache();
	}

	@SuppressWarnings("unchecked")
	private Department getDepartmentByCache(int id) {
		HashMap<Integer, Department> hashMap = (HashMap<Integer, Department>) Cache.get("Department");
		if (hashMap == null) {
			hashMap = new HashMap<Integer, Department>();
			List<Department> list = getDepartmentList(" order by department.seq,department.id");
			for (Department department : list) {
				hashMap.put(department.getId(), department);
			}
			Cache.put("Department", hashMap);
			Cache.put("DepartmentList", list);
		}

		return (Department) hashMap.get(id);
	}

	@SuppressWarnings("unchecked")
	private List<Department> getDepartmentListByCache() {
		List<Department> list = (List<Department>) Cache.get("DepartmentList");
		if (list == null) {
			HashMap<Integer, Department> hashMap = new HashMap<Integer, Department>();
			list = getDepartmentList(" order by department.seq,department.id");
			for (Department department : list) {
				hashMap.put(department.getId(), department);
			}
			Cache.put("Department", hashMap);
			Cache.put("DepartmentList", list);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public void addDepartment(Department department) {
		super.addDepartment(department);
		Cache.removeStartsWith("Department");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyDepartment(Department department) {
		super.modifyDepartment(department);
		Cache.removeStartsWith("Department");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteDepartment(int id) {
		super.deleteDepartment(id);
		Cache.removeStartsWith("Department");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteDepartment(String ids) {
		super.deleteDepartment(ids);
		Cache.removeStartsWith("Department");
	}

	public String getDepartmentIds(int id) {
		List<Department> list = null;
		if (id == 0) {
			list = this.getDepartmentList(" where department.parentId is null or department.parentId = 0");
		} else {
			list = this.getDepartmentList(" where department.parentId = " + id);
		}

		String departmentIds = StringUtil.toString(id);
		if (list == null || list.size() == 0) {
			return departmentIds;
		} else {
			for (Department department : list) {
				departmentIds += "," + getDepartmentIds(department.getId());
			}
		}
		return departmentIds;
	}
}