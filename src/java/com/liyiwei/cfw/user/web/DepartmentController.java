/*
 * Created on 2013-01-09 17:34:17
 *
 */
package com.liyiwei.cfw.user.web;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.liyiwei.cfw.user.entity.Department;
import com.liyiwei.cfw.user.service.DepartmentService;
import com.liyiwei.cfw.user.service.UserService;
import com.liyiwei.cfw.util.BaseController;
import com.liyiwei.common.util.JsonUtil;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Page;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/user/*")
public class DepartmentController extends BaseController {
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private UserService userService;

	@RequestMapping("departmentMain")
	public String departmentMain(ModelMap map) throws Exception {
		return "/user/departmentMain";
	}

	@RequestMapping("departmentTree")
	public String departmentTree(HttpServletRequest request, ModelMap map) throws Exception {
		List<Department> list = departmentService.getDepartmentList();
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		int type = StringUtil.toInt(request.getParameter("type"));
		for (Department department : list) {
			Map<String, Object> departmentMap = new HashMap<String, Object>();
			departmentMap.put("id", department.getId());
			departmentMap.put("name", department.getName());
			departmentMap.put("pId", StringUtil.toInt(department.getParentId()));
			if(StringUtil.isNotEmpty(request.getParameter("url")) && StringUtil.isNotEmpty(request.getParameter("target"))){
				departmentMap.put("target", request.getParameter("target"));
				departmentMap.put("url", request.getParameter("url") + department.getId());
			} else {
				if (type != 2) {
					departmentMap.put("target", "departmentContent");
					departmentMap.put("url", "departmentList.htm?pId=" + department.getId());
				} else {
					departmentMap.put("target", "userContent");
					departmentMap.put("url", "userList.htm?departmentId=" + department.getId());
				}
			}
			
			if (StringUtil.toInt(department.getParentId()) == 0) {
				departmentMap.put("open", "true");
			}
			treeList.add(departmentMap);
		}
		map.put("departmentTree", JsonUtil.beanToJson(treeList));
		map.put("type", type);
		return "/user/departmentTree";
	}

	@RequestMapping("departmentList")
	public String departmentList(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(901, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		String queryString = "";
		String pId = "";
		if (StringUtil.isNotEmpty(request.getParameter("pId"))) {
			pId = request.getParameter("pId");
			// queryString += " and department.parentId =" + pId;
			queryString += " and department.parentId in(" + departmentService.getDepartmentIds(StringUtil.toInt(pId)) + ")";
		}

		String queryName = "";
		if (StringUtil.isNotEmpty(request.getParameter("queryName"))) {
			queryName = request.getParameter("queryName");
			queryString += " and department.name like '%" + queryName + "%'";
		}
		String queryCode = "";
		if (StringUtil.isNotEmpty(request.getParameter("queryCode"))) {
			queryCode = request.getParameter("queryCode");
			queryString += " and department.code like '%" + queryCode + "%'";
		}

		String orderby = StringUtil.toString(request.getParameter("orderby"), "department.parentId,department.seq,department.id");
		String descflag = StringUtil.notNull(request.getParameter("descflag"));
		queryString += " order by " + orderby + " " + descflag;
		if (queryString.startsWith(" and")) {
			queryString = queryString.replaceFirst(" and", " where ");
		}

		int defaultPageSize = _rownum > 0 ? _rownum : 1;
		int pn = StringUtil.toInt(request.getParameter("pn"), 1);
		int pageSize = StringUtil.toInt(request.getParameter("pageSize"), defaultPageSize);
		Page page = new Page();
		page.setDefaultPageSize(defaultPageSize);
		page.setPageSize(pageSize);
		page.setCurrentPage(pn);
		page.setRecordCount(departmentService.getDepartmentCount(queryString));
		List<Department> departmentList = departmentService.getDepartmentList(queryString, page.getCurrentPage(), page.getPageSize());

		map.put("queryName", queryName);
		map.put("queryCode", queryCode);
		map.put("orderby", orderby);
		map.put("descflag", descflag);
		map.put("departmentList", departmentList);
		map.put("page", page);
		map.put("pId", pId);
		map.put("department", departmentService.getDepartment(StringUtil.toInt(pId)));

		return "/user/departmentList";
	}

	@RequestMapping("departmentSave")
	public String departmentSave(Department department, HttpServletRequest request) throws Exception {
		if (!hasPrivilege(901, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		if (department.getId() == null) {
			departmentService.addDepartment(department);
			actionLogService.writeActionLog("通用框架子系统", "组织机构管理", "新增部门", StringUtil.toString(department.getId()), "新增部门" + department.getName(), ip, _userId);
		} else {
			departmentService.modifyDepartment(department);
			actionLogService.writeActionLog("通用框架子系统", "组织机构管理", "修改部门", StringUtil.toString(department.getId()), "修改部门" + department.getName(), ip, _userId);
		}
		return "redirect:departmentList.htm?pId=" + department.getParentId();
	}

	@RequestMapping("departmentEditDialog")
	public String departmentEditDialog(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(901, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		int id = StringUtil.toInt(request.getParameter("id"), 0);
		if (id == 0) {
			Department department = new Department();
			if (StringUtil.isNotEmpty(request.getParameter("parentId"))) {
				int parentId = StringUtil.toInt(request.getParameter("parentId"));
				department.setParentId(parentId);
			}
			map.put("department", department);
		} else {
			Department department = departmentService.getDepartment(id);
			map.put("department", department);
		}

		List<Department> list = departmentService.getDepartmentList();
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		for (Department department : list) {
			Map<String, Object> departmentMap = new HashMap<String, Object>();
			departmentMap.put("id", department.getId());
			departmentMap.put("name", department.getName());
			departmentMap.put("pId", StringUtil.toInt(department.getParentId()));
			departmentMap.put("open", "true");
			treeList.add(departmentMap);
		}
		map.put("departmentTree", JsonUtil.beanToJson(treeList));

		return "/user/departmentEditDialog";
	}

	@RequestMapping("departmentDelete")
	public void departmentDelete(int id, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(901, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String ids = departmentService.getDepartmentIds(id);
		if (userService.existUserByDepartmentIds(ids)) {
			out.print(JsonUtil.toJson("success", false, "msg", "请先删除该部门下的所有用户后再删除该部门!"));
			return;
		}

		departmentService.deleteDepartment(ids);
		actionLogService.writeActionLog("通用框架子系统", "组织机构管理", "删除部门", StringUtil.toString(id), "删除部门", ip, _userId);
		out.print(JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@RequestMapping("departmentUp")
	public String departmentUp(int pId, int sno, HttpServletResponse response) throws Exception {
		departmentService.upDepartmentSeq(pId, sno);
		return "redirect:departmentList.htm?pId=" + pId;
	}

	@RequestMapping("departmentDown")
	public String departmentDown(int pId, int sno, HttpServletResponse response) throws Exception {
		departmentService.downDepartmentSeq(pId, sno);
		return "redirect:departmentList.htm?pId=" + pId;
	}
}