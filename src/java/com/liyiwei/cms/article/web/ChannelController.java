/*
 * Created on 2013-03-13 19:01:00
 *
 */
package com.liyiwei.cms.article.web;

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

import com.liyiwei.cfw.util.BaseController;
import com.liyiwei.cms.article.entity.Channel;
import com.liyiwei.cms.article.service.ArticleService;
import com.liyiwei.cms.article.service.ChannelService;
import com.liyiwei.common.util.JsonUtil;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Page;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/article/*")
public class ChannelController extends BaseController {
	@Autowired
	private ChannelService channelService;
	@Autowired
	private ArticleService articleService;

	@RequestMapping("channelMain")
	public String channelMain(ModelMap map) throws Exception {
		return "/article/channelMain";
	}

	@RequestMapping("channelTree")
	public String channelTree(HttpServletRequest request, ModelMap map) throws Exception {
		List<Channel> list = channelService.getChannelList();
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		int type = StringUtil.toInt(request.getParameter("type"));
		for (Channel channel : list) {
			Map<String, Object> channelMap = new HashMap<String, Object>();
			channelMap.put("id", channel.getId());
			channelMap.put("name", channel.getName());
			channelMap.put("pId", StringUtil.toInt(channel.getParentId()));
			if (type == 2) {
				channelMap.put("target", "articleContent");
				channelMap.put("url", "articleList.htm?channelId=" + channel.getId());
			} else if (type == 3) {
				channelMap.put("target", "articleContent");
				channelMap.put("url", "articleApproveList.htm?queryApproveStatus=0&channelId=" + channel.getId());
			} else {
				channelMap.put("target", "channelContent");
				channelMap.put("url", "channelList.htm?pId=" + channel.getId());
			}
			if (StringUtil.toInt(channel.getParentId()) == 0) {
				channelMap.put("open", "true");

			}
			treeList.add(channelMap);
		}
		map.put("channelTree", JsonUtil.beanToJson(treeList));
		map.put("type", type);
		return "/article/channelTree";
	}

	@RequestMapping("channelList")
	public String channelList(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(801, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		String queryString = "";
		String pId = "";
		if (StringUtil.isNotEmpty(request.getParameter("pId"))) {
			pId = request.getParameter("pId");
			queryString += " and channel.parentId in(" + channelService.getChannelIds(StringUtil.toInt(pId)) + ")";
		}

		String queryName = "";
		if (StringUtil.isNotEmpty(request.getParameter("queryName"))) {
			queryName = request.getParameter("queryName");
			queryString += " and channel.name like '%" + queryName + "%'";
		}

		String orderby = StringUtil.toString(request.getParameter("orderby"), "channel.parentId,channel.seq,channel.id");
		String descflag = StringUtil.notNull(request.getParameter("descflag"));
		queryString += " order by " + orderby + " " + descflag;
		if (queryString.startsWith(" and")) {
			queryString = queryString.replaceFirst(" and", " where ");
		}

		int defaultPageSize = _rownum > 0 ? _rownum : 1;
		int pn = StringUtil.toInt(request.getParameter("pn"), 1);
		int pageSize = StringUtil.toInt(request.getParameter("pageSize"), defaultPageSize);
		Page page = new Page();
		page.setDefaultPageSize(defaultPageSize);
		page.setPageSize(pageSize);
		page.setCurrentPage(pn);
		page.setRecordCount(channelService.getChannelCount(queryString));
		List<Channel> channelList = channelService.getChannelList(queryString, page.getCurrentPage(), page.getPageSize());

		map.put("queryName", queryName);
		map.put("orderby", orderby);
		map.put("descflag", descflag);
		map.put("channelList", channelList);
		map.put("page", page);
		map.put("pId", pId);
		map.put("channel", channelService.getChannel(StringUtil.toInt(pId)));

		return "/article/channelList";
	}

	@RequestMapping("channelSave")
	public String channelSave(Channel channel, HttpServletRequest request) throws Exception {
		if (!hasPrivilege(801, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		if (channel.getId() == null) {
			channelService.addChannel(channel);
			actionLogService.writeActionLog("信息发布子系统", "频道管理", "新增频道", StringUtil.toString(channel.getId()), "新增频道" + channel.getName(), ip, _userId);
		} else {
			channelService.modifyChannel(channel);
			actionLogService.writeActionLog("信息发布子系统", "频道管理", "修改频道", StringUtil.toString(channel.getId()), "修改频道" + channel.getName(), ip, _userId);
		}
		return "redirect:channelList.htm?pId=" + channel.getParentId();
	}

	@RequestMapping("channelEditDialog")
	public String channelEditDialog(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(801, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		int id = StringUtil.toInt(request.getParameter("id"), 0);
		if (id == 0) {
			Channel channel = new Channel();
			if (StringUtil.isNotEmpty(request.getParameter("parentId"))) {
				int parentId = StringUtil.toInt(request.getParameter("parentId"));
				channel.setParentId(parentId);
			}
			map.put("channel", channel);
		} else {
			Channel channel = channelService.getChannel(id);
			map.put("channel", channel);
		}

		List<Channel> list = channelService.getChannelList();
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		for (Channel channel : list) {
			Map<String, Object> channelMap = new HashMap<String, Object>();
			channelMap.put("id", channel.getId());
			channelMap.put("name", channel.getName());
			channelMap.put("pId", StringUtil.toInt(channel.getParentId()));
			channelMap.put("open", "true");
			treeList.add(channelMap);
		}
		map.put("channelTree", JsonUtil.beanToJson(treeList));

		return "/article/channelEditDialog";
	}

	@RequestMapping("channelDelete")
	public void channelDelete(int id, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(801, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		String ids = channelService.getChannelIds(id);
		if (articleService.existArticleByChannelIds(ids)) {
			out.print(JsonUtil.toJson("success", false, "msg", "请先删除该频道下的所有文章后再删除该频道!"));
			return;
		}

		channelService.deleteChannelByIds(ids);
		actionLogService.writeActionLog("信息发布子系统", "频道管理", "删除频道", StringUtil.toString(id), "删除频道", ip, _userId);
		out.print(JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@RequestMapping("channelUp")
	public String channelUp(int pId, int sno, HttpServletResponse response) throws Exception {
		channelService.upChannelSeq(pId, sno);
		return "redirect:channelList.htm?pId=" + pId;
	}

	@RequestMapping("channelDown")
	public String channelDown(int pId, int sno, HttpServletResponse response) throws Exception {
		channelService.downChannelSeq(pId, sno);
		return "redirect:channelList.htm?pId=" + pId;
	}
}