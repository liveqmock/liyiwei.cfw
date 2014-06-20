/*
 * Created on 2013-02-06 15:55:50
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

import com.liyiwei.cfw.system.entity.SystemSetting;
import com.liyiwei.cfw.system.service.SystemSettingService;
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
public class SystemSettingController extends BaseController {
	@Autowired
	private SystemSettingService systemSettingService;

	@RequestMapping("systemSetting")
	public String systemSetting(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(906, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		String queryString = "";
		String queryCategory = "";
		if (StringUtil.isNotEmpty(request.getParameter("queryCategory"))) {
			queryCategory = request.getParameter("queryCategory");
			queryString += " and systemSetting.category ='" + queryCategory + "'";
		}

		String orderby = StringUtil.toString(request.getParameter("orderby"), "systemSetting.category,systemSetting.id");
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
		page.setRecordCount(systemSettingService.getSystemSettingCount(queryString));
		List<SystemSetting> systemSettingList = systemSettingService.getSystemSettingList(queryString, page.getCurrentPage(), page.getPageSize());

		map.put("orderby", orderby);
		map.put("descflag", descflag);
		map.put("queryCategory", queryCategory);
		map.put("categorySelectBox", systemSettingService.categorySelectBox("queryCategory", queryCategory, "全部"));
		map.put("systemSettingList", systemSettingList);
		map.put("page", page);

		return "/system/systemSetting";
	}

	@RequestMapping("systemSettingSet")
	public void systemSettingSet(int id, String field, String value, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(906, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		systemSettingService.modifySystemSetting(id, field, value);
		actionLogService.writeActionLog("通用框架子系统", "系统设置", "修改系统设置", StringUtil.toString(id), "修改系统设置", ip, _userId);
		outputJson(response, JsonUtil.toJson("success", true, "msg", "设置成功!"));
	}
}