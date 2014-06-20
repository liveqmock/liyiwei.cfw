/*
 * Created on 2012-10-18 15:49:00
 *
 */
package com.liyiwei.cfw.util;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.liyiwei.cfw.system.service.ActionLogService;
import com.liyiwei.cfw.user.entity.User;
import com.liyiwei.cfw.user.service.UserService;
import com.liyiwei.common.enypt.Md5;
import com.liyiwei.common.log.Log;
import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Component
public class LoginFilter extends HttpServlet implements Filter {
	@Autowired
	private UserService userService;
	@Autowired
	private ActionLogService actionLogService;
	private static final long serialVersionUID = 1L;
	private String exclude = "";

	public void init(FilterConfig filterConfig) throws ServletException {
		Log.info("LoginFilter inited...");
		this.exclude = filterConfig.getInitParameter("exclude");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		HttpSession httpSession = httpServletRequest.getSession(true);

		String path = httpServletRequest.getContextPath() + httpServletRequest.getServletPath();
		String query = httpServletRequest.getQueryString();
		if (query != null) {
			path += "?" + query;
		}
		String uri = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length());
		System.out.println("Access Uri:" + uri);

		if (exclude != null && !exclude.equals("")) {
			for (String excludeDir : exclude.split(",")) {
				if (httpServletRequest.getRequestURL().indexOf(excludeDir) > -1) {
					chain.doFilter(request, response);
					return;
				}
			}
		}
		if (uri.equals("/index.htm") || httpServletRequest.getRequestURL().indexOf("login.htm") > -1 || httpServletRequest.getRequestURL().indexOf("loginAction") > -1
				|| httpServletRequest.getRequestURL().indexOf("validateCode.htm") > -1 || httpServletRequest.getRequestURL().indexOf("/register") > -1
				|| httpServletRequest.getRequestURL().indexOf("/common") > -1 || httpServletRequest.getRequestURL().indexOf("license") > -1 || httpServletRequest.getRequestURL().indexOf("init") > -1) {
			chain.doFilter(request, response);
			return;
		}

		if (httpSession == null || httpSession.getAttribute("_user") == null) {
			String username = CookieUtil.getCookie(httpServletRequest, "username");
			String userToken = CookieUtil.getCookie(httpServletRequest, "userToken");
			String remember = CookieUtil.getCookie(httpServletRequest, "remember");
			if (StringUtil.isNotEmpty(username) && StringUtil.isNotEmpty(userToken) && StringUtil.isNotEmpty(remember)) {
				if (remember.equals("1")) {
					if (userToken.equals(Md5.encrypt(username + Constants.SECRET_KEY))) {
						User user = userService.getUserByName(username);
						httpSession.removeAttribute("_lastVisitTime");
						httpSession.setAttribute("_lastVisitTime", user.getLastVisitTimeName());
						httpSession.removeAttribute("_lastIp");
						httpSession.setAttribute("_lastIp", user.getLastIp());
						String ip = "";
						if (httpServletRequest.getHeader("x-forwarded-for") == null) {
							ip = httpServletRequest.getRemoteAddr();
						} else {
							ip = httpServletRequest.getHeader("x-forwarded-for");
						}
						user.setLastIp(ip);
						user.setLastVisitTime(new Date());
						userService.modifyUser(user);

						List<Integer> privilegeList = userService.getUserAllPrivilege(user.getId());
						List<Integer> menuPrivilegeList = userService.getUserAllMenuPrivilege(user.getId());
						httpSession.removeAttribute("_user");
						httpSession.setAttribute("_user", user);
						httpSession.removeAttribute("_username");
						httpSession.setAttribute("_username", user.getUsername());
						httpSession.removeAttribute("_userId");
						httpSession.setAttribute("_userId", user.getId());
						httpSession.removeAttribute("_rank");
						httpSession.setAttribute("_rank", user.getRank());
						httpSession.removeAttribute("_privilegeList");
						httpSession.setAttribute("_privilegeList", privilegeList);
						httpSession.removeAttribute("_menuPrivilegeList");
						httpSession.setAttribute("_menuPrivilegeList", menuPrivilegeList);
						httpSession.removeAttribute("_userDepartmentIds");
						httpSession.setAttribute("_userDepartmentIds", userService.getUserIdsByDepartment(user.getDepartmentId()));
						httpSession.removeAttribute("_rownum");
						httpSession.setAttribute("_rownum", user.getRowNum());
						actionLogService.writeLoginLog("通用框架子系统", username + "自动登录成功", ip, user.getId());
						CookieUtil.setCookie(httpServletResponse, "backUrl", path);
						
						httpServletResponse.sendRedirect(path);
						return;
					}
				} else {
					CookieUtil.deleteCookie(httpServletRequest, httpServletResponse, username);
					CookieUtil.deleteCookie(httpServletRequest, httpServletResponse, userToken);
					CookieUtil.deleteCookie(httpServletRequest, httpServletResponse, remember);
				}
			}

			// httpServletResponse.sendRedirect("../main/login.htm");
			httpServletResponse.sendRedirect("../common/redirect.html");
			return;
		}

		chain.doFilter(request, response);
	}

	public void destroy() {
	}
}
