/*
 * Created on 2013-02-18 15:20:24
 *
 */
package com.liyiwei.cfw.system.web;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.liyiwei.cfw.system.entity.Menu;
import com.liyiwei.cfw.system.service.MenuService;
import com.liyiwei.cfw.util.BaseController;
import com.liyiwei.common.util.JsonUtil;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Page;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/system/*")
public class MenuController extends BaseController {
	@Autowired
	private MenuService menuService;

	@RequestMapping("menuMain")
	public String menuMain(ModelMap map) throws Exception {
		return "/system/menuMain";
	}

	@RequestMapping("menuTree")
	public String menuTree(HttpServletRequest request, ModelMap map) throws Exception {
		List<Menu> list = menuService.getMenuList();
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		for (Menu menu : list) {
			Map<String, Object> menuMap = new HashMap<String, Object>();
			menuMap.put("id", menu.getId());
			menuMap.put("name", menu.getName());
			menuMap.put("pId", StringUtil.toInt(menu.getParentId()));
			menuMap.put("target", "menuContent");
			menuMap.put("url", "menuList.htm?parentId=" + menu.getId());
			if (StringUtil.toInt(menu.getParentId()) == 0) {
				menuMap.put("open", "true");
			}
			treeList.add(menuMap);
		}
		map.put("menuTree", JsonUtil.beanToJson(treeList));
		return "/system/menuTree";
	}

	@RequestMapping("menuList")
	public String menuList(@RequestParam Map<String, String> queryMap, HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(904, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		String queryString = "";
		String parentId = StringUtil.toString(queryMap.get("parentId"));
		if (StringUtil.isNotEmpty(parentId)) {
			queryString += " and menu.parentId =" + parentId;
		}
		String queryName = StringUtil.toString(queryMap.get("queryName"));
		if (StringUtil.isNotEmpty(queryName)) {
			queryString += " and menu.name like '%" + queryName + "%'";
		}

		String orderby = StringUtil.toString(queryMap.get("orderby"), "menu.parentId,menu.seq,menu.id");
		String descflag = StringUtil.toString(queryMap.get("descflag"));
		queryString += " order by " + orderby + " " + descflag;
		if (queryString.startsWith(" and")) {
			queryString = queryString.replaceFirst(" and", " where ");
		}

		Page page = new Page();
		page.setDefaultPageSize(_rownum > 0 ? _rownum : 1);
		page.setPageSize(StringUtil.toInt(request.getParameter("pageSize")));
		page.setCurrentPage(StringUtil.toInt(request.getParameter("pn"), 1));
		page.setRecordCount(menuService.getMenuCount(queryString));
		List<Menu> menuList = menuService.getMenuList(queryString, page.getCurrentPage(), page.getPageSize());

		map.put("queryMap", queryMap);
		map.put("menuList", menuList);
		map.put("page", page);
		map.put("menu", menuService.getMenu(StringUtil.toInt(parentId)));

		return "/system/menuList";
	}

	@RequestMapping("menuSave")
	public String menuSave(Menu menu, HttpServletRequest request) throws Exception {
		if (!hasPrivilege(904, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		if (menu.getId() == null) {
			menuService.addMenu(menu);
			actionLogService.writeActionLog("通用框架子系统", "菜单管理", "新增菜单", StringUtil.toString(menu.getId()), "新增菜单" + menu.getName(), ip, _userId);
		} else {
			menuService.modifyMenu(menu);
			actionLogService.writeActionLog("通用框架子系统", "菜单管理", "修改菜单", StringUtil.toString(menu.getId()), "修改菜单" + menu.getName(), ip, _userId);
		}
		return "redirect:menuList.htm?parentId=" + menu.getParentId();
	}

	@RequestMapping("menuEditDialog")
	public String menuEditDialog(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(904, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		int id = StringUtil.toInt(request.getParameter("id"), 0);
		if (id == 0) {
			Menu menu = new Menu();
			if (StringUtil.isNotEmpty(request.getParameter("parentId"))) {
				int parentId = StringUtil.toInt(request.getParameter("parentId"));
				menu.setParentId(parentId);
			}
			map.put("menu", menu);
		} else {
			Menu menu = menuService.getMenu(id);
			map.put("menu", menu);
		}
		List<Menu> list = menuService.getMenuList();
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		for (Menu menu : list) {
			Map<String, Object> menuMap = new HashMap<String, Object>();
			menuMap.put("id", menu.getId());
			menuMap.put("name", menu.getName());
			menuMap.put("pId", StringUtil.toInt(menu.getParentId()));
			menuMap.put("open", "true");
			treeList.add(menuMap);
		}
		map.put("menuTree", JsonUtil.beanToJson(treeList));

		return "/system/menuEditDialog";
	}

	@RequestMapping("menuDelete")
	public void menuDelete(int id, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(904, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		if (menuService.existChildMenu(id)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "请先删除该菜单下的所有子菜单后再删除该菜单!"));
			return;
		}
		menuService.deleteMenu(id);
		actionLogService.writeActionLog("通用框架子系统", "菜单管理", "删除菜单", StringUtil.toString(id), "删除菜单", ip, _userId);
		outputJson(response, JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@RequestMapping("menuDeleteSelect")
	public void menuDeleteSelect(String ids, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(904, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		if (menuService.existChildMenu(ids)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "请先删除该菜单下的所有子菜单后再删除该菜单!"));
			return;
		}
		menuService.deleteMenuByIds(ids);
		actionLogService.writeActionLog("通用框架子系统", "菜单管理", "批量删除菜单", StringUtil.toString(ids), "删除菜单", ip, _userId);
		outputJson(response, JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@RequestMapping("menuUp")
	public String menuUp(int parentId, int sno, HttpServletResponse response) throws Exception {
		menuService.upMenuSeq(parentId, sno);
		return "redirect:menuList.htm?parentId=" + parentId;
	}

	@RequestMapping("menuDown")
	public String menuDown(int parentId, int sno, HttpServletResponse response) throws Exception {
		menuService.downMenuSeq(parentId, sno);
		return "redirect:menuList.htm?parentId=" + parentId;
	}
}