/*
 * Created on 2012-10-15 16:39:00
 *
 */
package com.liyiwei.cfw.main.web;

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

import com.liyiwei.cfw.privilege.service.PrivilegeService;
import com.liyiwei.cfw.system.entity.License;
import com.liyiwei.cfw.system.entity.Menu;
import com.liyiwei.cfw.system.service.ActionLogService;
import com.liyiwei.cfw.system.service.LicenseService;
import com.liyiwei.cfw.system.service.MenuService;
import com.liyiwei.cfw.user.entity.User;
import com.liyiwei.cfw.user.service.UserService;
import com.liyiwei.cfw.util.Constants;
import com.liyiwei.cfw.util.CookieUtil;
import com.liyiwei.common.cache.Cache;
import com.liyiwei.common.enypt.Md5;
import com.liyiwei.common.util.DateUtil;
import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/main/*")
public class MainController {
	@Autowired
	private UserService userService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private ActionLogService actionLogService;
	@Autowired
	PrivilegeService privilegeService;
	@Autowired
	LicenseService licenseService;

	@RequestMapping("index")
	public String index(ModelMap map) throws Exception {
		return "/main/login";
	}

	@RequestMapping("login")
	public String login(ModelMap map) throws Exception {
		return "/main/login";
	}

	@RequestMapping("logout")
	public String logout(ModelMap map, HttpSession session, HttpServletRequest request) throws Exception {
		if (session != null) {
			int userId = (Integer) session.getAttribute("_userId");
			actionLogService.writeLogoutLog("通用框架子系统", request.getRemoteAddr(), userId);
			session.invalidate();
		}

		return "/main/login";
	}

	@RequestMapping("main")
	public String main(ModelMap map) throws Exception {
		return "/main/main";
	}

	@RequestMapping("top")
	public String top(ModelMap map) throws Exception {
		return "/main/top";
	}

	@RequestMapping("left")
	public String left(ModelMap map) throws Exception {
		return "/main/left";
	}

	@RequestMapping("toolbar")
	public String toolbar(ModelMap map, HttpSession session) throws Exception {
		User user = (User) session.getAttribute("_user");
		String username = (String) session.getAttribute("_username");
		if (StringUtil.isNotEmpty(user.getRealname())) {
			username = user.getRealname();
		}

		map.put("username", username);
		map.put("departmentName", user.getDepartmentName());
		map.put("rolesName", user.getRolesName());
		return "/main/toolbar";
	}

	@RequestMapping("loginAction")
	public String loginAction(HttpSession session, HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		if(Constants.LOAD_LICENSE_MODULE == 1){
			License license = licenseService.licenseCheck();
			if (license != null) {
				if (DateUtil.before(DateUtil.add(license.getRegisterDate(), license.getPermitDays()), new Date())) {
					return "redirect:../system/licenseInfo.htm";
				}
			} else {
				return "redirect:../system/licenseInfo.htm";
			}
			session.setAttribute("_licenseName", license.getName());
			session.setAttribute("_licenseRegisterDate", license.getRegisterDate());
			session.setAttribute("_licensePermitDays", license.getPermitDays());
		}

		String validateCode = StringUtil.toString(request.getParameter("validateCode"));
		String username = StringUtil.toString(request.getParameter("username"));
		String code = (String) request.getSession().getAttribute("validateCode");
		if (StringUtil.isNotEmpty(validateCode) && !validateCode.toUpperCase().equals(code)) {
			int failCount = addFailCount(username);
			if (failCount >= 3) {
				map.put("validateCodeFlag", "1");
			}
			map.put("msg", "验证码错误!");
			return "/main/login";
		}

		String password = StringUtil.toString(request.getParameter("password"));
		int height = StringUtil.toInt(request.getParameter("height"), 577);
		int width = StringUtil.toInt(request.getParameter("width"), 1366);
		String ip = "";
		if (request.getHeader("x-forwarded-for") == null) {
			ip = request.getRemoteAddr();
		} else {
			ip = request.getHeader("x-forwarded-for");
		}
		if (userService.validate(username, password)) {
			User user = userService.getUserByName(username);
			session.removeAttribute("_lastVisitTime");
			session.setAttribute("_lastVisitTime", user.getLastVisitTimeName());
			session.removeAttribute("_lastIp");
			session.setAttribute("_lastIp", user.getLastIp());
			int rowNum = (height - 277) / 37;
			if (height != StringUtil.toInt(user.getScreenHeight())) {
				user.setScreenHeight(height);
				user.setRowNum(rowNum);
			}
			if (width != StringUtil.toInt(user.getScreenWidth())) {
				user.setScreenWidth(width);
			}
			
			user.setLastIp(ip);
			user.setLastVisitTime(new Date());
			userService.modifyUser(user);

			List<Integer> privilegeList = userService.getUserAllPrivilege(user.getId());
			List<Integer> menuPrivilegeList = userService.getUserAllMenuPrivilege(user.getId());
			session.removeAttribute("_user");
			session.setAttribute("_user", user);
			session.removeAttribute("_username");
			session.setAttribute("_username", user.getUsername());
			session.removeAttribute("_userId");
			session.setAttribute("_userId", user.getId());
			session.removeAttribute("_rank");
			session.setAttribute("_rank", user.getRank());
			session.removeAttribute("_privilegeList");
			session.setAttribute("_privilegeList", privilegeList);
			session.removeAttribute("_menuPrivilegeList");
			session.setAttribute("_menuPrivilegeList", menuPrivilegeList);
			session.removeAttribute("_userDepartmentIds");
			session.setAttribute("_userDepartmentIds", userService.getUserIdsByDepartment(user.getDepartmentId()));
			session.removeAttribute("_rownum");
			session.setAttribute("_rownum", rowNum);
			actionLogService.writeLoginLog("通用框架子系统", username + "登录成功", ip, user.getId());
			failCountReset(username);
			
			if(Constants.LOAD_LICENSE_MODULE == 1){
				String remember = CookieUtil.getCookie(request, "remember");
				if(remember != null){
					if(remember.equals("1")){
						String userToken = Md5.encrypt(username + Constants.SECRET_KEY);
						CookieUtil.setCookie(response, "username", username);
						CookieUtil.setCookie(response, "userToken", userToken);
						CookieUtil.setCookie(response, "remember", remember);
					} else {
						CookieUtil.deleteCookie(request, response, "username");
						CookieUtil.deleteCookie(request, response, "userToken");
						CookieUtil.deleteCookie(request, response, "remember");
					}
				}
			}	
			
			return "redirect:/main/main.htm";
		} else {
			actionLogService.writeLoginLog("通用框架子系统", username + "登录失败", ip, 0);
			int failCount = addFailCount(username);
			if (failCount >= 3) {
				map.put("validateCodeFlag", "1");
			}
			map.put("msg", "用户名不存在或者密码错误，请重新输入!");
			return "/main/login";
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("menu")
	public String menu(HttpServletRequest request, HttpSession session, ModelMap map) throws Exception {
		int parentId = StringUtil.toInt(request.getParameter("parentId"));
		List<Menu> list = menuService.getMenuList();
		List<Integer> menuPrivilegeList = (List<Integer>) session.getAttribute("_menuPrivilegeList");

		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		List<Menu> menuPermitList = new ArrayList<Menu>();
		Map<String, Object> menuMap = new HashMap<String, Object>();
		int i = 0;
		int menuId = parentId;
		for (Menu menu : list) {
			if (menuPrivilegeList.contains(menu.getId())) {
				if (StringUtil.toInt(menu.getParentId()) == parentId) {
					if (i != 0) {
						menuMap.put("menuList", menuPermitList);
						treeList.add(menuMap);
					}
					menuMap = new HashMap<String, Object>();
					menuPermitList = new ArrayList<Menu>();
					menuMap.put("menu", menu);
					menuId = menu.getId();
					i++;
				} else if (StringUtil.toInt(menu.getParentId()) == menuId) {
					menuPermitList.add(menu);
				}
			}
		}
		if (i != 0) {
			menuMap.put("menuList", menuPermitList);
			treeList.add(menuMap);
		}

		map.put("treeList", treeList);
		return "/main/menu";
	}
	
	@RequestMapping("desktop")
	public String desktop(ModelMap map, HttpSession session) throws Exception {
		User user = (User) session.getAttribute("_user");
		String username = (String) session.getAttribute("_username");
		if (StringUtil.isNotEmpty(user.getRealname())) {
			username = user.getRealname();
		}

		map.put("username", username);
		map.put("today", DateUtil.formatChineseDateWeek(new Date()));
		return "/main/desktop";
	}

	@SuppressWarnings("unchecked")
	private int addFailCount(String username) {
		HashMap<String, Integer> hashMap = (HashMap<String, Integer>) Cache.get("failCount");
		int failCount = 1;
		if (hashMap == null) {
			hashMap = new HashMap<String, Integer>();
		} else {
			failCount = StringUtil.toInt((Integer) hashMap.get(username)) + 1;
		}
		hashMap.put(username, failCount);
		Cache.put("failCount", hashMap);
		return failCount;
	}

	@SuppressWarnings("unchecked")
	private void failCountReset(String username) {
		HashMap<String, Integer> hashMap = (HashMap<String, Integer>) Cache.get("failCount");
		if (hashMap == null) {
			hashMap = new HashMap<String, Integer>();
		}
		hashMap.put(username, 0);
		Cache.put("failCount", hashMap);
	}
}