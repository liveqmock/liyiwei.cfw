/*
 * Created on 2013-12-24 13:58:15
 *
 */
package com.liyiwei.cfw.base.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.liyiwei.cfw.base.entity.Area;
import com.liyiwei.cfw.base.service.AreaService;
import com.liyiwei.cfw.util.ApplicationUtil;
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
public class AreaController extends BaseController {
	@Autowired
	private AreaService areaService;

	@RequestMapping("areaList")
	public String areaList(@RequestParam Map<String, String> queryMap, HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(701, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		String queryString = "";
		String queryName = StringUtil.toString(queryMap.get("queryName"));
		if (StringUtil.isNotEmpty(queryName)) {
			queryString += " and area.name like '%" + queryName + "%'";
		}
		String queryCode = StringUtil.toString(queryMap.get("queryCode"));
		if (StringUtil.isNotEmpty(queryCode)) {
			queryString += " and area.code like '%" + queryCode + "%'";
		}
		String queryType = StringUtil.toString(queryMap.get("queryType"));
		if (StringUtil.isNotEmpty(queryType)) {
			queryString += " and area.type =" + queryType;
		}
		String orderby = StringUtil.toString(queryMap.get("orderby"), "area.id");
		String descflag = StringUtil.toString(queryMap.get("descflag"));
		queryString += " order by " + orderby + " " + descflag;
		if (queryString.startsWith(" and")) {
			queryString = queryString.replaceFirst(" and", " where ");
		}

		Page page = new Page();
		page.setDefaultPageSize(_rownum > 0 ? _rownum : 1);
		page.setPageSize(StringUtil.toInt(request.getParameter("pageSize")));
		page.setCurrentPage(StringUtil.toInt(request.getParameter("pn"), 1));
		page.setRecordCount(areaService.getAreaCount(queryString));
		List<Area> areaList = areaService.getAreaList(queryString, page.getCurrentPage(), page.getPageSize());

		queryMap.put("queryType", ApplicationUtil.getDictService().dictSelectBox("queryType", queryType, " -请选择- ", "area", "type"));
		map.put("queryMap", queryMap);
		map.put("areaList", areaList);
		map.put("page", page);

		return "/base/areaList";
	}

	@RequestMapping("areaEditDialog")
	public String areaEditDialog(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(701, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		int id = StringUtil.toInt(request.getParameter("id"), 0);
		if (id == 0) {
			Area area = new Area();
			map.put("area", area);
		} else {
			Area area = areaService.getArea(id);
			map.put("area", area);
		}
		return "/base/areaEditDialog";
	}

	@RequestMapping("areaSave")
	public String areaSave(Area area, HttpServletRequest request) throws Exception {
		if (!hasPrivilege(701, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		if (area.getId() == null) {
			areaService.addArea(area);
			actionLogService.writeActionLog("通用框架子系统", "地区管理", "新增地区", StringUtil.toString(area.getId()), "新增地区" + area.getId(), ip, _userId);
		} else {
			areaService.modifyArea(area);
			actionLogService.writeActionLog("通用框架子系统", "地区管理", "修改地区", StringUtil.toString(area.getId()), "修改地区" + area.getId(), ip, _userId);
		}
		return "redirect:areaList.htm";
	}

	@RequestMapping("areaDelete")
	public void areaDelete(int id, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(701, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		areaService.deleteArea(id);
		actionLogService.writeActionLog("通用框架子系统", "地区管理", "删除地区", StringUtil.toString(id), "删除地区", ip, _userId);
		outputJson(response, JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@RequestMapping("areaDeleteSelect")
	public void areaDeleteSelect(String ids, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(701, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		areaService.deleteAreaByIds(ids);
		actionLogService.writeActionLog("通用框架子系统", "地区管理", "批量删除地区", StringUtil.toString(ids), "删除地区", ip, _userId);
		outputJson(response, JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@RequestMapping("getCitySelectBoxByProvince")
	public void getCitySelectBoxByProvince(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String province = StringUtil.toString(request.getParameter("province"));
		outputHtml(response, areaService.citySelectBox(province));
	}

	@RequestMapping("getDistrictSelectBoxByCity")
	public void getDistrictSelectBoxByCity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String city = StringUtil.toString(request.getParameter("city"));
		outputHtml(response, areaService.districtSelectBox(city));
	}
}