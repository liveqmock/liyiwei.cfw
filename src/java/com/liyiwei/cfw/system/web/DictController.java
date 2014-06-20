/*
 * Created on 2013-02-06 16:45:33
 *
 */
package com.liyiwei.cfw.system.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.liyiwei.cfw.system.entity.Dict;
import com.liyiwei.cfw.system.service.DictService;
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
public class DictController extends BaseController {
	@Autowired
	private DictService dictService;

	@RequestMapping("dictList")
	public String dictList(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(905, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		String queryString = "";
		String queryTable = "";
		if (StringUtil.isNotEmpty(request.getParameter("queryTable"))) {
			queryTable = request.getParameter("queryTable");
			queryString += " and dict.tableName ='" + queryTable + "'";
		}
		String queryField = "";
		if (StringUtil.isNotEmpty(request.getParameter("queryField"))) {
			queryField = request.getParameter("queryField");
			queryString += " and dict.fieldName ='" + queryField + "'";
		}

		String orderby = StringUtil.toString(request.getParameter("orderby"), "dict.id");
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
		page.setRecordCount(dictService.getDictCount(queryString));
		List<Dict> dictList = dictService.getDictList(queryString, page.getCurrentPage(), page.getPageSize());

		map.put("queryTableSelectBox", dictService.tableSelectBox("queryTable", queryTable, "全部"));
		map.put("queryFieldSelectBox", dictService.fieldSelectBox(queryTable, "queryField", queryField, "全部"));
		map.put("orderby", orderby);
		map.put("descflag", descflag);
		map.put("dictList", dictList);
		map.put("page", page);

		return "/system/dictList";
	}

	@RequestMapping("dictSave")
	public String dictSave(Dict dict, HttpServletRequest request) throws Exception {
		if (!hasPrivilege(905, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		if (dict.getId() == null) {
			dictService.addDict(dict);
			actionLogService.writeActionLog("通用框架子系统", "数据字典管理", "新增数据字典", StringUtil.toString(dict.getId()), "新增数据字典", ip, _userId);
		} else {
			dictService.modifyDict(dict);
			actionLogService.writeActionLog("通用框架子系统", "数据字典管理", "修改数据字典", StringUtil.toString(dict.getId()), "修改数据字典", ip, _userId);
		}
		return "redirect:dictList.htm?queryTable=" + dict.getTableName() + "&queryField=" + dict.getFieldName();
	}

	@RequestMapping("dictSet")
	public void dictSet(int id, String field, String value, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(905, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		dictService.modifyDict(id, field, value);
		actionLogService.writeActionLog("通用框架子系统", "数据字典管理", "修改数据字典", StringUtil.toString(id), "修改数据字典", ip, _userId);
		outputJson(response, JsonUtil.toJson("success", true, "msg", "设置成功!"));
	}

	@RequestMapping("isExist")
	public void isExist(String tableName, String fieldName, String value, HttpServletResponse response) throws Exception {
		if (dictService.isExist(tableName, fieldName, value)) {
			outputJson(response, JsonUtil.toJson("isExist", true, "msg", "数据字典值重复!"));
		} else {
			outputJson(response, JsonUtil.toJson("isExist", false, "msg", ""));
		}
	}

	@RequestMapping("dictEditDialog")
	public String dictEditDialog(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(905, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		int id = StringUtil.toInt(request.getParameter("id"), 0);
		if (id == 0) {
			Dict dict = new Dict();
			if (StringUtil.isNotEmpty(request.getParameter("tableName"))) {
				dict.setTableName(request.getParameter("tableName"));
			}
			if (StringUtil.isNotEmpty(request.getParameter("fieldName"))) {
				dict.setFieldName(request.getParameter("fieldName"));
			}
			map.put("dict", dict);
		} else {
			Dict dict = dictService.getDict(id);
			map.put("dict", dict);
		}
		return "/system/dictEditDialog";
	}
}