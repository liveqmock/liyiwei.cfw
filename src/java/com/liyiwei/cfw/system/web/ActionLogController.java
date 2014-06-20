/*
 * Created on 2013-02-17 19:44:24
 *
 */
package com.liyiwei.cfw.system.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.liyiwei.cfw.system.entity.ActionLog;
import com.liyiwei.cfw.system.service.ActionLogService;
import com.liyiwei.cfw.user.service.UserService;
import com.liyiwei.cfw.util.BaseController;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Page;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/system/*")
public class ActionLogController extends BaseController {
	@Autowired
	private ActionLogService actionLogService;
	@Autowired
	private UserService userService;

	@RequestMapping("actionLogList")
	public String actionLogList(@RequestParam Map<String, String> queryMap, HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(907, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		String queryString = "";
		String querySystem = StringUtil.toString(queryMap.get("querySystem"));
		if (StringUtil.isNotEmpty(querySystem)) {
			queryString += " and actionLog.system like '%" + querySystem + "%'";
		}
		String queryModule = StringUtil.toString(queryMap.get("queryModule"));
		if (StringUtil.isNotEmpty(queryModule)) {
			queryString += " and actionLog.module like '%" + queryModule + "%'";
		}
		String queryAction = StringUtil.toString(queryMap.get("queryAction"));
		if (StringUtil.isNotEmpty(queryAction)) {
			queryString += " and actionLog.action like '%" + queryAction + "%'";
		}
		String queryIp = StringUtil.toString(queryMap.get("queryIp"));
		if (StringUtil.isNotEmpty(queryIp)) {
			queryString += " and actionLog.ip like '%" + queryIp + "%'";
		}
		String queryUserId = StringUtil.toString(queryMap.get("queryUserId"));
		if (StringUtil.isNotEmpty(queryUserId)) {
			queryString += " and actionLog.userId =" + queryUserId;
		}
		String queryStartLogTime = StringUtil.toString(queryMap.get("queryStartLogTime"));
		if (StringUtil.isNotEmpty(queryStartLogTime)) {
			queryString += " and date_format(actionLog.logTime,'%Y-%m-%d') >='" + queryStartLogTime + "'";
		}
		String queryEndLogTime = StringUtil.toString(queryMap.get("queryEndLogTime"));
		if (StringUtil.isNotEmpty(queryEndLogTime)) {
			queryString += " and date_format(actionLog.logTime,'%Y-%m-%d') <='" + queryEndLogTime + "'";
		}

		String orderby = StringUtil.toString(queryMap.get("orderby"), "actionLog.id");
		String descflag = StringUtil.toString(queryMap.get("descflag"), "desc");
		queryString += " order by " + orderby + " " + descflag;
		if (queryString.startsWith(" and")) {
			queryString = queryString.replaceFirst(" and", " where ");
		}

		Page page = new Page();
		page.setDefaultPageSize(_rownum > 0 ? _rownum : 1);
		page.setPageSize(StringUtil.toInt(request.getParameter("pageSize")));
		page.setCurrentPage(StringUtil.toInt(request.getParameter("pn"), 1));
		page.setRecordCount(actionLogService.getActionLogCount(queryString));
		List<ActionLog> actionLogList = actionLogService.getActionLogList(queryString, page.getCurrentPage(), page.getPageSize());

		map.put("queryMap", queryMap);
		map.put("userSelectBox", userService.userSelectBox("queryUserId", queryUserId, " -请选择- "));
		map.put("actionLogList", actionLogList);
		map.put("page", page);

		return "/system/actionLogList";
	}
}