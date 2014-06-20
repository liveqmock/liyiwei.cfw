/*
 * Created on 2013-01-15 17:32:33
 *
 */
package com.liyiwei.cfw.privilege.web;

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

import com.liyiwei.cfw.privilege.entity.Privilege;
import com.liyiwei.cfw.privilege.entity.Role;
import com.liyiwei.cfw.privilege.entity.RolePrivilege;
import com.liyiwei.cfw.privilege.service.PrivilegeService;
import com.liyiwei.cfw.privilege.service.RolePrivilegeService;
import com.liyiwei.cfw.privilege.service.RoleService;
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
@RequestMapping("/privilege/*")
public class RoleController extends BaseController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private RolePrivilegeService rolePrivilegeService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private UserService userService;

	@RequestMapping("roleList")
	public String roleList(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(903, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		String queryString = "";
		String orderby = StringUtil.toString(request.getParameter("orderby"), "role.id");
		String descflag = StringUtil.notNull(request.getParameter("descflag"));
		queryString += " order by " + orderby + " " + descflag;
		if (queryString.startsWith(" and")) {
			queryString = queryString.replaceFirst(" and", " where ");
		}

		int defaultPageSize = _rownum + 2;
		int pn = StringUtil.toInt(request.getParameter("pn"), 1);
		int pageSize = StringUtil.toInt(request.getParameter("pageSize"), defaultPageSize);
		Page page = new Page();
		page.setDefaultPageSize(defaultPageSize);
		page.setPageSize(pageSize);
		page.setCurrentPage(pn);
		page.setRecordCount(roleService.getRoleCount(queryString));
		List<Role> roleList = roleService.getRoleList(queryString, page.getCurrentPage(), page.getPageSize());

		map.put("orderby", orderby);
		map.put("descflag", descflag);
		map.put("roleList", roleList);
		map.put("page", page);

		return "/privilege/roleList";
	}

	@RequestMapping("roleSave")
	public String roleSave(Role role, HttpServletRequest request) throws Exception {
		if (!hasPrivilege(903, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		if (role.getId() == null) {
			roleService.addRole(role);
			actionLogService.writeActionLog("通用框架子系统", "角色管理", "新增角色", StringUtil.toString(role.getId()), "新增角色" + role.getName(), ip, _userId);
		} else {
			roleService.modifyRole(role);
			actionLogService.writeActionLog("通用框架子系统", "角色管理", "修改角色", StringUtil.toString(role.getId()), "修改角色" + role.getName(), ip, _userId);
		}
		return "redirect:roleList.htm";
	}

	@RequestMapping("roleEditDialog")
	public String roleEditDialog(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(903, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		int id = StringUtil.toInt(request.getParameter("id"), 0);
		if (id == 0) {
			Role role = new Role();
			map.put("role", role);
		} else {
			Role role = roleService.getRole(id);
			map.put("role", role);
		}
		return "/privilege/roleEditDialog";
	}

	@RequestMapping("roleDelete")
	public void roleDelete(int id, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(903, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		if (userService.existUserByRole(id)) {
			out.print(JsonUtil.toJson("success", false, "msg", "请先删除该角色下的所有用户后再删除该角色!"));
			return;
		}
		roleService.deleteRole(id);
		rolePrivilegeService.deleteRoleAllPrivilege(id);
		actionLogService.writeActionLog("通用框架子系统", "角色管理", "删除角色", StringUtil.toString(id), "删除角色", ip, _userId);
		out.print(JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@RequestMapping("roleView")
	public String roleView(int id, HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(903, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		Role role = roleService.getRole(id);
		map.put("rolePrivilegeList", rolePrivilegeService.getRoleAllPrivilege(id));
		map.put("role", role);

		return "/privilege/roleView";
	}

	@RequestMapping("rolePrivilegeSave")
	public String rolePrivilegeSave(int roleId, String privileges, HttpServletRequest request) throws Exception {
		if (!hasPrivilege(903, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		rolePrivilegeService.deleteRoleAllPrivilege(roleId);
		if (StringUtil.isNotEmpty(privileges)) {
			String[] privilegeArray = privileges.split(",");
			for (String privilege : privilegeArray) {
				RolePrivilege rolePrivilege = new RolePrivilege();
				rolePrivilege.setRoleId(roleId);
				rolePrivilege.setPrivilegeId(StringUtil.toInteger(privilege));
				rolePrivilegeService.addRolePrivilege(rolePrivilege);
			}
		}
		actionLogService.writeActionLog("通用框架子系统", "角色管理", "角色权限设置", StringUtil.toString(roleId), "角色权限设置", ip, _userId);
		return "redirect:roleList.htm";
	}

	@RequestMapping("rolePrivilegeSet")
	public String rolePrivilegeSet(int roleId, HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(903, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		Role role = roleService.getRole(roleId);
		List<Privilege> list = privilegeService.getPrivilegeList();
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		for (Privilege privilege : list) {
			Map<String, Object> privilegeMap = new HashMap<String, Object>();
			privilegeMap.put("id", privilege.getId());
			privilegeMap.put("name", privilege.getName());
			privilegeMap.put("pId", StringUtil.toInt(privilege.getParentId()));
			privilegeMap.put("open", "true");
			privilegeMap.put("isParent", "true");
			treeList.add(privilegeMap);
		}
		map.put("privilegeTree", JsonUtil.beanToJson(treeList));
		map.put("rolePrivilegeList", rolePrivilegeService.getRoleAllPrivilege(roleId));
		map.put("role", role);

		return "/privilege/rolePrivilegeSet";
	}
}