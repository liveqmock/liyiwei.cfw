/*
 * Created on 2013-04-17 16:59:03
 *
 */
package com.liyiwei.cfw.base.web;

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

import com.liyiwei.cfw.base.entity.Category;
import com.liyiwei.cfw.base.service.CategoryService;
import com.liyiwei.cfw.util.BaseController;
import com.liyiwei.common.util.JsonUtil;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Page;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/base/*")
public class CategoryController extends BaseController {
	@Autowired
	private CategoryService categoryService;

	@RequestMapping("categoryMain")
	public String categoryMain(ModelMap map) throws Exception {
		return "/base/categoryMain";
	}

	@RequestMapping("categoryTree")
	public String categoryTree(HttpServletRequest request, ModelMap map) throws Exception {
		List<Category> list = categoryService.getCategoryList();
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		for (Category category : list) {
			Map<String, Object> categoryMap = new HashMap<String, Object>();
			categoryMap.put("id", category.getId());
			categoryMap.put("name", category.getName());
			categoryMap.put("pId", StringUtil.toInt(category.getParentId()));
			categoryMap.put("target", "categoryContent");
			categoryMap.put("url", "categoryList.htm?parentId=" + category.getId());
			if (StringUtil.toInt(category.getParentId()) == 0) {
				categoryMap.put("open", "true");
			}
			treeList.add(categoryMap);
		}
		map.put("categoryTree", JsonUtil.beanToJson(treeList));
		return "/base/categoryTree";
	}

	@RequestMapping("categoryList")
	public String categoryList(@RequestParam Map<String, String> queryMap, HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(702, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}
		
		String queryString = "";
		String parentId = StringUtil.toString(queryMap.get("parentId"));
		if (StringUtil.isNotEmpty(parentId)) {
			queryString += " and category.parentId =" + parentId;
		}

		String orderby = StringUtil.toString(queryMap.get("orderby"), "category.parentId,category.seq,category.id");
		String descflag = StringUtil.toString(queryMap.get("descflag"));
		queryString += " order by " + orderby + " " + descflag;
		if (queryString.startsWith(" and")) {
			queryString = queryString.replaceFirst(" and", " where ");
		}

		Page page = new Page();
		page.setDefaultPageSize(_rownum + 2);
		page.setPageSize(StringUtil.toInt(request.getParameter("pageSize")));
		page.setCurrentPage(StringUtil.toInt(request.getParameter("pn"), 1));
		page.setRecordCount(categoryService.getCategoryCount(queryString));
		List<Category> categoryList = categoryService.getCategoryList(queryString, page.getCurrentPage(), page.getPageSize());

		map.put("queryMap", queryMap);
		map.put("categoryList", categoryList);
		map.put("page", page);
		map.put("category", categoryService.getCategory(StringUtil.toInt(parentId)));

		return "/base/categoryList";
	}

	@RequestMapping("categoryEditDialog")
	public String categoryEditDialog(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(702, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}
		
		int id = StringUtil.toInt(request.getParameter("id"), 0);
		if (id == 0) {
			Category category = new Category();
			if (StringUtil.isNotEmpty(request.getParameter("parentId"))) {
				int parentId = StringUtil.toInt(request.getParameter("parentId"));
				category.setParentId(parentId);
			}
			map.put("category", category);
		} else {
			Category category = categoryService.getCategory(id);
			map.put("category", category);
		}

		List<Category> list = categoryService.getCategoryList();
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		for (Category category : list) {
			Map<String, Object> categoryMap = new HashMap<String, Object>();
			categoryMap.put("id", category.getId());
			categoryMap.put("name", category.getName());
			categoryMap.put("pId", StringUtil.toInt(category.getParentId()));
			categoryMap.put("open", "true");
			treeList.add(categoryMap);
		}
		map.put("categoryTree", JsonUtil.beanToJson(treeList));
		return "/base/categoryEditDialog";
	}

	@RequestMapping("categorySave")
	public String categorySave(Category category, HttpServletRequest request) throws Exception {
		if (!hasPrivilege(702, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}
		
		if (category.getId() == null) {
			categoryService.addCategory(category);
			actionLogService.writeActionLog("通用框架子系统", "分类管理", "新增分类", StringUtil.toString(category.getId()), "新增分类" + category.getName(), ip, _userId);
		} else {
			categoryService.modifyCategory(category);
			actionLogService.writeActionLog("通用框架子系统", "分类管理", "修改分类", StringUtil.toString(category.getId()), "修改分类" + category.getName(), ip, _userId);
		}
		return "redirect:categoryList.htm?parentId=" + category.getParentId();
	}

	@RequestMapping("categoryDelete")
	public void categoryDelete(int id, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(702, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}
		
		if (categoryService.existChildCategory(id)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "请先删除该分类下的所有子分类后再删除该分类!"));
			return;
		}
		categoryService.deleteCategory(id);
		actionLogService.writeActionLog("通用框架子系统", "分类管理", "删除分类", StringUtil.toString(id), "删除分类", ip, _userId);
		outputJson(response, JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@RequestMapping("categoryDeleteSelect")
	public void categoryDeleteSelect(String ids, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(702, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}
		
		if (categoryService.existChildCategory(ids)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "请先删除该分类下的所有子分类后再删除该分类!"));
			return;
		}
		categoryService.deleteCategoryByIds(ids);
		actionLogService.writeActionLog("通用框架子系统", "分类管理", "批量删除分类", StringUtil.toString(ids), "删除分类", ip, _userId);
		outputJson(response, JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@RequestMapping("categoryUp")
	public String categoryUp(int parentId, int sno, HttpServletResponse response) throws Exception {
		categoryService.upCategorySeq(parentId, sno);
		return "redirect:categoryList.htm?parentId=" + parentId;
	}

	@RequestMapping("categoryDown")
	public String categoryDown(int parentId, int sno, HttpServletResponse response) throws Exception {
		categoryService.downCategorySeq(parentId, sno);
		return "redirect:categoryList.htm?parentId=" + parentId;
	}
}