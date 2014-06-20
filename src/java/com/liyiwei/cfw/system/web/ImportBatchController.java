/*
 * Created on 2013-04-27 15:09:15
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

import com.liyiwei.cfw.system.entity.ImportBatch;
import com.liyiwei.cfw.system.service.ImportBatchService;
import com.liyiwei.cfw.util.BaseController;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Page;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/system/*")
public class ImportBatchController extends BaseController {
	@Autowired
	private ImportBatchService importBatchService;

	@RequestMapping("importBatchList")
	public String importBatchList(@RequestParam Map<String, String> queryMap, HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(908, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		String queryString = "";
		String queryModule = StringUtil.toString(queryMap.get("queryModule"));
		if (StringUtil.isNotEmpty(queryModule)) {
			queryString += " and importBatch.module like '%" + queryModule + "%'";
		}
		String orderby = StringUtil.toString(queryMap.get("orderby"), "importBatch.id");
		String descflag = StringUtil.toString(queryMap.get("descflag"));
		queryString += " order by " + orderby + " " + descflag;
		if (queryString.startsWith(" and")) {
			queryString = queryString.replaceFirst(" and", " where ");
		}

		Page page = new Page();
		page.setDefaultPageSize(_rownum > 0 ? _rownum : 1);
		page.setPageSize(StringUtil.toInt(request.getParameter("pageSize")));
		page.setCurrentPage(StringUtil.toInt(request.getParameter("pn"), 1));
		page.setRecordCount(importBatchService.getImportBatchCount(queryString));
		List<ImportBatch> importBatchList = importBatchService.getImportBatchList(queryString, page.getCurrentPage(), page.getPageSize());

		map.put("queryMap", queryMap);
		map.put("importBatchList", importBatchList);
		map.put("page", page);

		return "/system/importBatchList";
	}
}