/*
 * Created on 2013-02-17 19:44:24
 *
 */
package com.liyiwei.cfw.system.web;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.liyiwei.cfw.util.BaseController;
import com.liyiwei.common.cache.Cache;
import com.liyiwei.common.util.JsonUtil;
import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/system/*")
public class CacheController extends BaseController {
	@RequestMapping("cacheList")
	public String cacheList(HttpServletRequest request, ModelMap map) throws Exception {
		if (!hasPrivilege(909, _privilegeList)) {
			return "redirect:/common/noprivilege.html";
		}

		String queryKey = StringUtil.toString(request.getParameter("queryKey"));
		List<Map<String, String>> cacheList = new ArrayList<Map<String, String>>();
		Enumeration<String> e = Cache.getKeys();
		while (e.hasMoreElements()) {
			Map<String, String> cacheMap = new HashMap<String, String>();
			String key = (String) e.nextElement();
			if (StringUtil.isNotEmpty(queryKey)) {
				if (key.startsWith(queryKey)) {
					Object object = Cache.get(key);
					cacheMap.put("key", key);
					cacheMap.put("object", object.toString());
					cacheList.add(cacheMap);
				}
			} else {
				Object object = Cache.get(key);
				cacheMap.put("key", key);
				cacheMap.put("object", object.toString());
				cacheList.add(cacheMap);
			}
		}
		map.put("cacheList", cacheList);
		map.put("queryKey", queryKey);

		return "/system/cacheList";
	}

	@RequestMapping("cacheDelete")
	public void cacheDelete(String key, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(909, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		Cache.remove(key);
		outputJson(response, JsonUtil.toJson("success", true, "msg", "清除成功!"));
	}

	@RequestMapping("cacheDeleteSelect")
	public void cacheDeleteSelect(String keys, HttpServletResponse response) throws Exception {
		if (!hasPrivilege(909, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		String[] keyArray = keys.split(",");
		for (String key : keyArray) {
			Cache.remove(key);
		}

		outputJson(response, JsonUtil.toJson("success", true, "msg", "清除成功!"));
	}

	@RequestMapping("cacheDeleteAll")
	public void cacheDeleteAll(HttpServletResponse response) throws Exception {
		if (!hasPrivilege(909, _privilegeList)) {
			outputJson(response, JsonUtil.toJson("success", false, "msg", "权限不足!"));
			return;
		}

		Cache.destroy();
		outputJson(response, JsonUtil.toJson("success", true, "msg", "清除成功!"));
	}
}