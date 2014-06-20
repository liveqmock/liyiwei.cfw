/*
 * Created on 2014-06-12 18:04:00
 *
 */
package com.liyiwei.cfw.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
public class CookieUtil {
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie cookies[] = request.getCookies();
		if (cookies != null && StringUtil.isNotEmpty(name)) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}

	public static void setCookie(HttpServletResponse response, String name, String value) {
		setCookie(response, name, value, 60 * 60 * 24 * 30);
	}

	public static void setCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, StringUtil.toString(value));
		cookie.setMaxAge(maxAge);
		cookie.setPath("/");
		response.addCookie(cookie);
	}

	public static void setCookie(HttpServletResponse response, String name, String value, int maxAge, String path, String domain) {
		Cookie cookie = new Cookie(name, StringUtil.toString(value));
		cookie.setMaxAge(maxAge);
		cookie.setPath(path);
		if (StringUtil.isNotEmpty(domain)) {
			cookie.setDomain(domain);
		}
		response.addCookie(cookie);
	}

	public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
		Cookie cookies[] = request.getCookies();
		if (cookies != null && StringUtil.isNotEmpty(name)) {
			for (Cookie cookie : cookies) {
				if (name.equals(cookie.getName())) {
					cookie.setPath("/");
					cookie.setValue("");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
					return;
				}
			}
		}
	}
}