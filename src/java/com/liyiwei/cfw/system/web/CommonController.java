/*
 * Created on 2014-04-11 14:42:00
 *
 */
package com.liyiwei.cfw.system.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/common/*")
public class CommonController {
	@RequestMapping("message")
	public String message(HttpServletRequest request) throws Exception {
		return "/common/message";
	}
}