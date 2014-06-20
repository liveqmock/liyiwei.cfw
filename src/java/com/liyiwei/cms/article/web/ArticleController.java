/*
 * Created on 2013-03-13 19:01:00
 *
 */
package com.liyiwei.cms.article.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.liyiwei.cfw.system.service.AttachmentService;
import com.liyiwei.cfw.system.service.SystemSettingService;
import com.liyiwei.cfw.util.BaseController;
import com.liyiwei.cms.article.entity.Article;
import com.liyiwei.cms.article.entity.Channel;
import com.liyiwei.cms.article.service.ArticleService;
import com.liyiwei.cms.article.service.ChannelService;
import com.liyiwei.cms.util.ApplicationUtil;
import com.liyiwei.common.util.JsonUtil;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Page;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/article/*")
public class ArticleController extends BaseController {
	@Autowired
	private ArticleService articleService;
	@Autowired
	private ChannelService channelService;
	@Autowired
	private SystemSettingService systemSettingService;
	@Autowired
	private AttachmentService attachmentService;

	private String rootPath;

	@ModelAttribute("rootPath")
	public String getRootPath() {
		rootPath = systemSettingService.getSystemSetting("uploadRootPath", "upload");
		return rootPath;
	}

	@RequestMapping("articleMain")
	public String articleMain(ModelMap map) throws Exception {
		return "/article/articleMain";
	}

	@RequestMapping("articleList")
	public String articleList(@RequestParam Map<String, String> queryMap, HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(802, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		List<Object> list = new ArrayList<Object>();
		StringBuilder sb = new StringBuilder();
		String channelId = StringUtil.toString(queryMap.get("channelId"));
		if (StringUtil.isNotEmpty(channelId)) {
			sb.append(" and article.channelId in(").append(channelService.getChannelIds(StringUtil.toInt(channelId))).append(")");
		}
		String queryTitle = StringUtil.toString(queryMap.get("queryTitle"));
		if (StringUtil.isNotEmpty(queryTitle)) {
			sb.append(" and article.title like ?");
			list.add("%" + queryTitle + "%");
		}
		String queryContent = StringUtil.toString(queryMap.get("queryContent"));
		if (StringUtil.isNotEmpty(queryContent)) {
			sb.append(" and article.content like ?");
			list.add("%" + queryContent + "%");
		}
		String queryStatus = StringUtil.toString(queryMap.get("queryStatus"));
		if (StringUtil.isNotEmpty(queryStatus)) {
			sb.append(" and article.status=?");
			list.add(StringUtil.toInt(queryStatus));
		}
		String queryApproveStatus = StringUtil.toString(queryMap.get("queryApproveStatus"));
		if (StringUtil.isNotEmpty(queryApproveStatus)) {
			sb.append(" and article.approveStatus=?");
			list.add(StringUtil.toInt(queryApproveStatus));
		}

		String queryModifyUserId = StringUtil.toString(queryMap.get("queryModifyUserId"));
		if (StringUtil.isNotEmpty(queryModifyUserId)) {
			sb.append(" and article.modifyUserId=?");
			list.add(StringUtil.toInt(queryModifyUserId));
		}
		String orderby = StringUtil.toString(queryMap.get("orderby"), "article.modifyTime");
		String descflag = StringUtil.toString(queryMap.get("descflag"), "desc");
		sb.append(" order by ").append(orderby).append(" ").append(descflag);

		String queryString = sb.toString();
		if (queryString.startsWith(" and")) {
			queryString = queryString.replaceFirst(" and", " where ");
		}

		Page page = new Page();
		page.setDefaultPageSize(_rownum > 0 ? _rownum : 1);
		page.setPageSize(StringUtil.toInt(request.getParameter("pageSize")));
		page.setCurrentPage(StringUtil.toInt(request.getParameter("pn"), 1));
		page.setRecordCount(articleService.getArticleQueryCount(queryString, list));
		List<Article> articleList = articleService.getArticleQueryList(queryString, page.getCurrentPage(), page.getPageSize(), list);

		map.put("statusSelectBox", ApplicationUtil.getDictService().dictSelectBox("queryStatus", queryStatus, " -请选择- ", "article", "status"));
		map.put("approveStatusSelectBox", ApplicationUtil.getDictService().dictSelectBox("queryApproveStatus", queryApproveStatus, " -请选择- ", "article", "approveStatus"));
		map.put("modifyUserSelectBox", ApplicationUtil.getUserService().userSelectBox("queryModifyUserId", queryModifyUserId, " -请选择- "));
		map.put("queryMap", queryMap);
		map.put("articleList", articleList);
		map.put("page", page);
		map.put("orderby", orderby);
		map.put("descflag", descflag);

		return "/article/articleList";
	}

	@RequestMapping("articleApproveMain")
	public String articleApproveMain(ModelMap map) throws Exception {
		return "/article/articleApproveMain";
	}

	@RequestMapping("articleApproveList")
	public String articleApproveList(@RequestParam Map<String, String> queryMap, HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(803, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		String queryString = "";
		String channelId = StringUtil.toString(queryMap.get("channelId"));
		if (StringUtil.isNotEmpty(channelId)) {
			queryString += " and article.channelId in(" + channelService.getChannelIds(StringUtil.toInt(channelId)) + ")";
		}
		String queryTitle = StringUtil.toString(queryMap.get("queryTitle"));
		if (StringUtil.isNotEmpty(queryTitle)) {
			queryString += " and article.title like '%" + queryTitle + "%'";
		}
		String queryContent = StringUtil.toString(queryMap.get("queryContent"));
		if (StringUtil.isNotEmpty(queryContent)) {
			queryString += " and article.content like '%" + queryContent + "%'";
		}
		String queryApproveStatus = StringUtil.toString(queryMap.get("queryApproveStatus"));
		if (StringUtil.isNotEmpty(queryApproveStatus)) {
			queryString += " and article.approveStatus =" + queryApproveStatus;
		}
		String queryModifyUserId = StringUtil.toString(queryMap.get("queryModifyUserId"));
		if (StringUtil.isNotEmpty(queryModifyUserId)) {
			queryString += " and article.modifyUserId =" + queryModifyUserId;
		}

		queryString += " and article.status = 1";
		String orderby = StringUtil.toString(queryMap.get("orderby"), "article.modifyTime");
		String descflag = StringUtil.toString(queryMap.get("descflag"), "desc");
		queryString += " order by " + orderby + " " + descflag;
		if (queryString.startsWith(" and")) {
			queryString = queryString.replaceFirst(" and", " where ");
		}

		Page page = new Page();
		page.setDefaultPageSize(_rownum > 0 ? _rownum : 1);
		page.setPageSize(StringUtil.toInt(request.getParameter("pageSize")));
		page.setCurrentPage(StringUtil.toInt(request.getParameter("pn"), 1));
		page.setRecordCount(articleService.getArticleCount(queryString));
		List<Article> articleList = articleService.getArticlePageList(queryString, page.getCurrentPage(), page.getPageSize());

		map.put("approveStatusSelectBox", ApplicationUtil.getDictService().dictSelectBox("queryApproveStatus", queryApproveStatus, " -请选择- ", "article", "approveStatus"));
		map.put("modifyUserSelectBox", ApplicationUtil.getUserService().userSelectBox("queryModifyUserId", queryModifyUserId, " -请选择- "));
		map.put("queryMap", queryMap);
		map.put("articleList", articleList);
		map.put("page", page);

		return "/article/articleApproveList";
	}

	@RequestMapping("articleApproveDialog")
	public String articleApproveDialog(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(803, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		String channelId = StringUtil.toString(request.getParameter("channelId"));
		String approveStatus = StringUtil.toString(request.getParameter("approveStatus"));
		String ids = StringUtil.toString(request.getParameter("ids"));
		map.put("ids", ids);
		map.put("approveStatus", approveStatus);
		map.put("channelId", channelId);
		return "/article/articleApproveDialog";
	}

	@RequestMapping("articleApproveSave")
	public String articleApproveSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(803, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		int chId = StringUtil.toInt(request.getParameter("chId"));
		int approveStatus = StringUtil.toInt(request.getParameter("approveStatus"));
		String ids = StringUtil.toString(request.getParameter("ids"));
		String approveNote = StringUtil.toString(request.getParameter("approveNote"));
		articleService.approveArticleByIds(ids, approveStatus, approveNote, _userId);
		if (approveStatus == 1) {
			actionLogService.writeActionLog("信息发布子系统", "文章审批", "文章审批通过", StringUtil.toString(ids), "文章审批通过" + ids, ip, _userId);
		} else {
			actionLogService.writeActionLog("信息发布子系统", "文章审批", "文章审批驳回", StringUtil.toString(ids), "文章审批驳回" + ids, ip, _userId);
		}

		return "redirect:articleApproveList.htm?queryApproveStatus=0&channelId=" + chId;
	}

	@RequestMapping("articleEdit")
	public String articleEdit(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(802, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		int id = StringUtil.toInt(request.getParameter("id"), 0);
		if (id == 0) {
			Article article = new Article();
			if (StringUtil.isNotEmpty(request.getParameter("channelId"))) {
				int channelId = StringUtil.toInt(request.getParameter("channelId"));
				article.setChannelId(channelId);
			}
			map.put("article", article);
		} else {
			Article article = articleService.getArticle(id);
			map.put("article", article);
		}

		List<Channel> list = channelService.getChannelList();
		List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
		for (Channel channel : list) {
			Map<String, Object> channelMap = new HashMap<String, Object>();
			channelMap.put("id", channel.getId());
			channelMap.put("name", channel.getName());
			channelMap.put("pId", StringUtil.toInt(channel.getParentId()));
			channelMap.put("open", "true");
			channelMap.put("isParent", "true");
			treeList.add(channelMap);
		}
		map.put("channelTree", JsonUtil.beanToJson(treeList));

		return "/article/articleEdit";
	}

	@RequestMapping("articleSave")
	public String articleSave(Article article, HttpServletRequest request) throws Exception {
		if (!hasPrivilege(802, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		article.setModifyUserId(_userId);
		article.setModifyTime(new Date());
		if (article.getId() == null) {
			article.setStatus(1);
			article.setViewCount(0);
			article.setApproveStatus(0);
			article.setCreateUserId(_userId);
			article.setCreateTime(new Date());
			articleService.addArticle(article);
			String code = StringUtil.toString(request.getParameter("code"));
			attachmentService.updateAttachmentCode(code, "article" + article.getId());
			actionLogService.writeActionLog("信息发布子系统", "文章管理", "新增文章", StringUtil.toString(article.getId()), "新增文章" + article.getTitle(), ip, _userId);
		} else {
			article.setCreateTime(StringUtil.toDate(request.getParameter("createTime"), "yyyy-MM-dd HH:mm"));
			if (StringUtil.isEmpty(request.getParameter("approveTime"))) {
				article.setApproveTime(StringUtil.toDate(request.getParameter("approveTime"), "yyyy-MM-dd HH:mm"));
			}
			article.setApproveStatus(0);
			articleService.modifyArticle(article);
			actionLogService.writeActionLog("信息发布子系统", "文章管理", "修改文章", StringUtil.toString(article.getId()), "修改文章" + article.getTitle(), ip, _userId);
		}
		return "redirect:articleList.htm?channelId=" + article.getChannelId();
	}

	@RequestMapping("articleView")
	public String articleView(int id, HttpServletRequest request, ModelMap map) throws Exception {
		Article article = articleService.getArticle(id);
		map.put("article", article);
		return "/article/articleView";
	}

	@RequestMapping("articleDelete")
	public void articleDelete(int id, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(802, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		articleService.deleteArticle(id);
		attachmentService.deleteAttachmentByCode("article" + id, rootPath);
		actionLogService.writeActionLog("信息发布子系统", "文章管理", "删除文章", StringUtil.toString(id), "删除文章", ip, _userId);
		outputJson(response, JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@RequestMapping("articleDeleteSelect")
	public void articleDeleteSelect(String ids, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(802, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		articleService.deleteArticleByIds(ids);
		String[] idArray = ids.split(",");
		for (String id : idArray) {
			attachmentService.deleteAttachmentByCode("article" + id, rootPath);
		}
		actionLogService.writeActionLog("信息发布子系统", "文章管理", "批量删除文章", StringUtil.toString(ids), "删除文章", ip, _userId);
		outputJson(response, JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@RequestMapping("articleStatusSet")
	public void articleStatusSet(int id, int status, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(802, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		articleService.modifyArticleStatus(id, status);
		if (status == 1) {
			actionLogService.writeActionLog("信息发布子系统", "文章管理", "生效文章", StringUtil.toString(id), "生效文章", ip, _userId);
		} else {
			actionLogService.writeActionLog("信息发布子系统", "文章管理", "失效文章", StringUtil.toString(id), "失效文章", ip, _userId);
		}
		String msg = status == 1 ? "已经生效!" : "已经失效!";
		outputJson(response, JsonUtil.toJson("success", true, "msg", msg));
	}

	@RequestMapping("articleGonggaoList")
	public void articleGonggaoList(int num, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Article> list = articleService.getArticleListByChannel(1, num);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Article article : list) {
			sb.append("<li class=\"bg\"><a href=\"../article/").append(article.getId()).append(".htm\" class=\"darkblue\" target=\"_blank\">");
			sb.append(article.getTitleBrief()).append("</a>(").append(article.getModifyTimeDateName()).append(")</li>");
			i++;
		}
		for (; i < num; i++) {
			sb.append("<li>&nbsp;</li>");
		}

		outputJson(response, JsonUtil.toJson("success", true, "msg", sb.toString()));
	}

	@RequestMapping("articleInfoList")
	public void articleInfoList(int num, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Article> list = articleService.getArticleListByChannel(2, num);
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (Article article : list) {
			sb.append("<li class=\"bg\"><a href=\"../article/").append(article.getId()).append(".htm\" class=\"darkblue\" target=\"_blank\">");
			sb.append(article.getTitleBrief()).append("</a>(").append(article.getModifyTimeDateName()).append(")</li>");
			i++;
		}
		for (; i < num; i++) {
			sb.append("<li>&nbsp;</li>");
		}

		outputJson(response, JsonUtil.toJson("success", true, "msg", sb.toString()));
	}

	@RequestMapping("list")
	public String list(int channelId, HttpServletRequest request, ModelMap map) throws Exception {
		Page page = new Page();
		page.setDefaultPageSize(_rownum > 0 ? _rownum : 1);
		page.setPageSize(StringUtil.toInt(request.getParameter("pageSize")));
		page.setCurrentPage(StringUtil.toInt(request.getParameter("pn"), 1));
		page.setRecordCount(articleService.getArticleCountByChannel(channelId));
		List<Article> articleList = articleService.getArticleListByChannel(channelId, page.getCurrentPage(), page.getPageSize());
		map.put("channelId", channelId);
		map.put("channelName", channelService.getChannelName(channelId));
		map.put("articleList", articleList);
		map.put("hotArticleList", articleService.getHotArticleList(channelId, 10));
		map.put("page", page);
		return "/cms/list";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String view(@PathVariable int id, HttpServletRequest request, ModelMap map) throws Exception {
		articleService.addViewCount(id);
		Article article = articleService.getArticle(id);
		map.put("article", article);
		map.put("relationArticleList", articleService.getRelationArticleList(id, article.getChannelId(), 5));
		map.put("hotArticleList", articleService.getHotArticleList(article.getChannelId(), 10));
		return "/cms/view";
	}
}