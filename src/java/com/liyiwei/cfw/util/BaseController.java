/*
 * Created on 2012-04-18 15:37:00
 *
 */
package com.liyiwei.cfw.util;

import java.beans.PropertyEditorSupport;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.liyiwei.cfw.system.service.ActionLogService;
import com.liyiwei.cfw.user.entity.User;
import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
public class BaseController {
	@Autowired
	protected HttpServletRequest httpServletRequest;
	@Autowired
	protected ActionLogService actionLogService;

	protected HttpSession session;
	protected String context;
	protected String realPath;
	protected String ip;
	protected User _user;
	protected int _userId;
	protected String _username;
	protected int _rank;
	protected int _rownum = 8;
	protected String _userDepartmentIds;
	protected List<Integer> _privilegeList;

	@SuppressWarnings("unchecked")
	@ModelAttribute("init")
	public void init() {
		realPath = httpServletRequest.getSession().getServletContext().getRealPath(File.separator);
		session = httpServletRequest.getSession();
		if (session != null) {
			_user = (User) session.getAttribute("_user");
			_userId = StringUtil.toInt((Integer) session.getAttribute("_userId"));
			_username = (String) session.getAttribute("_username");
			_rank = StringUtil.toInt((Integer) session.getAttribute("_rank"), 1);
			_rownum = StringUtil.toInt((Integer) session.getAttribute("_rownum"), 8);
			_privilegeList = (List<Integer>) session.getAttribute("_privilegeList");
			_userDepartmentIds = (String) session.getAttribute("_userDepartmentIds");
		}
		ip = getIpAddress(httpServletRequest);
	}

	@ModelAttribute("context")
	public String getContext() {
		if (context == null) {
			context = httpServletRequest.getContextPath();
		}
		return context;
	}

	protected void outputJson(HttpServletResponse response, String result) throws Exception {
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.println(result);
		out.flush();
		out.close();
	}

	protected void outputHtml(HttpServletResponse response, String result) throws Exception {
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.println(result);
		out.flush();
		out.close();
	}

	protected void outputHtml(HttpServletResponse response, String result, String charset) throws Exception {
		response.setCharacterEncoding(charset);
		PrintWriter out = response.getWriter();
		out.println(result);
		out.flush();
		out.close();
	}

	@InitBinder
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(StringUtil.escape(text.trim()));
			}
		});
	}

	public boolean hasPrivilege(int privilegeId, List<Integer> privilegeList) {
		if (privilegeList == null || privilegeList.size() <= 0) {
			return false;
		}

		for (Integer id : privilegeList) {
			if (privilegeId == id) {
				return true;
			}
		}
		return false;
	}

	public boolean hasPrivilege(String privilegeName, List<String> privilegeList) {
		if (privilegeList == null || privilegeList.size() <= 0) {
			return false;
		}

		for (String name : privilegeList) {
			if (privilegeName.equals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public String getPrivilegeCondition(String field,int type) {
		StringBuilder sb = new StringBuilder();
		String condition = (type == 1 ? " where" : " and");

		if (_rank == 2) {
			if (_user.getDepartmentId() != null) {
				sb.append(condition).append(field).append(" in(").append(_userDepartmentIds).append(")");
			} else {
				sb.append(condition).append(field).append(" =").append(_userId);
			}
		} else if (_rank == 3) {
		} else {
			sb.append(condition).append(field).append(" =").append(_userId);
		}
		return sb.toString();
	}

	public String getIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}

			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} else {
			if (ip.contains(",")) {
				String[] ips = ip.split(",");
				return ips[0];
			}
		}

		return ip;
	}
}