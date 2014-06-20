/*
 * Created on 2013-01-09 17:45:42
 *
 */
package com.liyiwei.cfw.user.web;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.liyiwei.cfw.privilege.service.PrivilegeService;
import com.liyiwei.cfw.user.entity.Department;
import com.liyiwei.cfw.user.entity.User;
import com.liyiwei.cfw.user.service.DepartmentService;
import com.liyiwei.cfw.user.service.UserService;
import com.liyiwei.cfw.util.ApplicationUtil;
import com.liyiwei.cfw.util.BaseController;
import com.liyiwei.cfw.util.Constants;
import com.liyiwei.common.enypt.Md5;
import com.liyiwei.common.util.JsonUtil;
import com.liyiwei.common.util.RandomUtil;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Page;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/user/*")
public class UserController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private PrivilegeService privilegeService;
	@Autowired
	private DepartmentService departmentService;

	@RequestMapping("userMain")
	public String userMain(ModelMap map) throws Exception {
		return "/user/userMain";
	}

	@RequestMapping("userList")
	public String userList(@RequestParam Map<String, String> queryMap, HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(902, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		String queryString = "";
		String departmentId = StringUtil.toString(queryMap.get("departmentId"));
		if (StringUtil.isNotEmpty(departmentId)) {
			// queryString += " and user.departmentId =" + departmentId;
			queryString += " and user.departmentId in(" + departmentService.getDepartmentIds(StringUtil.toInt(departmentId)) + ")";
		}
		String queryUsername = StringUtil.toString(queryMap.get("queryUsername"));
		if (StringUtil.isNotEmpty(queryUsername)) {
			queryString += " and user.username like '%" + queryUsername + "%'";
		}
		String queryRealname = StringUtil.toString(queryMap.get("queryRealname"));
		if (StringUtil.isNotEmpty(queryRealname)) {
			queryString += " and user.realname like '%" + queryRealname + "%'";
		}
		String queryStatus = StringUtil.toString(queryMap.get("queryStatus"));
		if (StringUtil.isNotEmpty(queryStatus)) {
			queryString += " and user.status =" + queryStatus;
		}

		String orderby = StringUtil.toString(queryMap.get("orderby"), "user.id");
		String descflag = StringUtil.notNull(queryMap.get("descflag"));
		queryString += " order by " + orderby + " " + descflag;
		if (queryString.startsWith(" and")) {
			queryString = queryString.replaceFirst(" and", " where ");
		}

		Page page = new Page();
		page.setDefaultPageSize(_rownum > 0 ? _rownum : 1);
		page.setPageSize(StringUtil.toInt(request.getParameter("pageSize")));
		page.setCurrentPage(StringUtil.toInt(request.getParameter("pn"), 1));
		page.setRecordCount(userService.getUserCount(queryString));
		List<User> userList = userService.getUserList(queryString, page.getCurrentPage(), page.getPageSize());

		queryMap.put("queryStatus", ApplicationUtil.getDictService().dictSelectBox("queryStatus", StringUtil.toString(queryMap.get("queryStatus")), " -请选择- ", "user", "status"));
		map.put("queryMap", queryMap);
		map.put("userList", userList);
		map.put("page", page);

		return "/user/userList";
	}

	@RequestMapping("userEdit")
	public String userEdit(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(902, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		int id = StringUtil.toInt(request.getParameter("id"), 0);
		if (id == 0) {
			User user = new User();
			if (StringUtil.isNotEmpty(request.getParameter("departmentId"))) {
				int departmentId = StringUtil.toInt(request.getParameter("departmentId"));
				user.setDepartmentId(departmentId);
			}
			map.put("user", user);
		} else {
			User user = userService.getUser(id);
			map.put("user", user);
		}

		List<Department> list = departmentService.getDepartmentList();
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		for (Department department : list) {
			Map<String, Object> departmentMap = new HashMap<String, Object>();
			departmentMap.put("id", department.getId());
			departmentMap.put("name", department.getName());
			departmentMap.put("pId", StringUtil.toInt(department.getParentId()));
			departmentMap.put("open", "true");
			departmentMap.put("isParent", "true");
			treeList.add(departmentMap);
		}
		map.put("departmentTree", JsonUtil.beanToJson(treeList));

		return "/user/userEdit";
	}

	@RequestMapping("userSave")
	public String userSave(User user, HttpServletRequest request) throws Exception {
		if (!hasPrivilege(902, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		user.setRegisterTime(StringUtil.toDate(request.getParameter("registerTime"), "yyyy-MM-dd HH:mm"));
		if (user.getId() == null) {
			user.setRawPassword(user.getPassword());
			String salt = Md5.encrypt(RandomUtil.createRandomString(6));
			String password = Md5.encrypt(user.getPassword() + salt + Constants.SECRET_KEY);
			user.setPassword(password);
			user.setSalt(salt);
			user.setStatus(1);
			user.setRegisterTime(new Date());
			userService.addUser(user);
			actionLogService.writeActionLog("通用框架子系统", "用户管理", "新增用户", StringUtil.toString(user.getId()), "新增用户" + user.getUsername(), ip, _userId);
		} else {
			userService.modifyUser(user.getId(), user);
			actionLogService.writeActionLog("通用框架子系统", "用户管理", "修改用户", StringUtil.toString(user.getId()), "修改用户" + user.getUsername(), ip, _userId);
		}
		return "redirect:userList.htm?departmentId=" + user.getDepartmentId();
	}

	@RequestMapping("userView")
	public String userView(int id, HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(902, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		User user = userService.getUser(id);
		map.put("user", user);

		List<Integer> list = userService.getUserAllPrivilege(id);
		List<String> privilegeList = new ArrayList<String>();
		for (Integer privilegeId : list) {
			privilegeList.add(privilegeService.getPrivilegeName(privilegeId));
		}
		map.put("privilegeList", privilegeList);

		return "/user/userView";
	}

	@RequestMapping("userInfo")
	public String userInfo(HttpServletRequest request, ModelMap map) throws Exception {
		return "/user/userInfo";
	}

	@RequestMapping("userInfoModify")
	public String userInfoModify(HttpServletRequest request, ModelMap map) throws Exception {
		return "/user/userInfoModify";
	}

	@RequestMapping("userInfoSave")
	public String userInfoSave(User user, HttpSession session, HttpServletRequest request) throws Exception {
		user.setRegisterTime(StringUtil.toDate(request.getParameter("registerTime"), "yyyy-MM-dd HH:mm"));
		user.setId(_userId);
		userService.modifyUserInfo(user);
		user = userService.getUser(_userId);
		session.removeAttribute("_user");
		session.setAttribute("_user", user);
		session.removeAttribute("_username");
		session.setAttribute("_username", user.getUsername());
		session.removeAttribute("_userId");
		session.setAttribute("_userId", user.getId());
		return "redirect:userInfo.htm";
	}

	@RequestMapping("isExist")
	public void isExist(String username, HttpServletResponse response) throws Exception {
		if (userService.isExist(username)) {
			outputJson(response, JsonUtil.toJson("isExist", true, "msg", "用户名已存在!"));
		} else {
			outputJson(response, JsonUtil.toJson("isExist", false, "msg", ""));
		}
	}

	@RequestMapping("userDelete")
	public void userDelete(int id, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(902, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		userService.deleteUser(id);
		actionLogService.writeActionLog("通用框架子系统", "用户管理", "删除用户", StringUtil.toString(id), "删除用户", ip, _userId);
		out.print(JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@RequestMapping("userDeleteSelect")
	public void userDeleteSelect(String ids, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(902, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		userService.deleteUser(ids);
		actionLogService.writeActionLog("通用框架子系统", "用户管理", "批量删除用户", StringUtil.toString(ids), "删除用户", ip, _userId);
		out.print(JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@RequestMapping("userStatusSet")
	public void userStatusSet(int id, int status, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(902, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		userService.modifyUserStatus(id, status);
		if (status == 1) {
			actionLogService.writeActionLog("通用框架子系统", "用户管理", "激活用户", StringUtil.toString(id), "激活用户", ip, _userId);
		} else {
			actionLogService.writeActionLog("通用框架子系统", "用户管理", "冻结用户", StringUtil.toString(id), "冻结用户", ip, _userId);
		}
		String msg = status == 1 ? "激活成功!" : "冻结成功!";
		out.print(JsonUtil.toJson("success", true, "msg", msg));
	}

	@RequestMapping("modifyPassword")
	public void modifyPassword(String oldPassword, String password, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		if (!userService.validate(_userId, oldPassword)) {
			out.print(JsonUtil.toJson("success", false, "msg", "旧密码不正确!"));
			return;
		}
		userService.modifyPassword(_userId, password);
		actionLogService.writeActionLog("通用框架子系统", "用户", "修改密码", "", "修改密码成功!", ip, _userId);
		out.print(JsonUtil.toJson("success", true, "msg", "修改密码成功!"));
	}

	@RequestMapping("modifyUserPassword")
	public void modifyUserPassword(int id, String password, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(902, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		userService.modifyPassword(id, password);
		actionLogService.writeActionLog("通用框架子系统", "用户管理", "重置密码", StringUtil.toString(id), "重置密码成功!", ip, _userId);
		out.print(JsonUtil.toJson("success", true, "msg", "修改密码成功!"));
	}
	
	@RequestMapping("getUserSelectBoxByDepartment")
	public void getUserSelectBoxByDepartment(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int departmentId = StringUtil.toInt(request.getParameter("departmentId"));
		outputHtml(response, userService.userSelectBox("-请选择-", departmentId));
	}
}