/*
 * Created on 2014-04-10 15:25:29
 *
 */
package com.liyiwei.cfw.system.web;

import java.security.PrivateKey;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.liyiwei.cfw.system.entity.License;
import com.liyiwei.cfw.system.service.LicenseService;
import com.liyiwei.cfw.util.BaseController;
import com.liyiwei.common.enypt.RSA;
import com.liyiwei.common.util.CommonUtil;
import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/system/*")
public class AuthController extends BaseController {
	@Autowired
	private LicenseService licenseService;

	@RequestMapping("licenseRegister")
	public String licenseRegister(HttpServletRequest request, ModelMap map) throws Exception {
		return "/system/licenseRegister";
	}

	@RequestMapping("licenseRegisterAction")
	public String licenseRegisterAction(License license, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
		String priKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAxx07jlV4T+c7I61m6ChXomyow5Wa8aGZ99sKjcqJPRk7Ho4JUnP0PtIxgwAFvf5hqpGz2SzNJf0desBPNOh1UwIDAQABAkAXc6r7FbgI+Twu9JKFT5Maz1+FQc6xHQ6MceEpWCyX5HaoC2cn7Gy24wAC5j6rjo95Khy3itDt83V/j31He+apAiEA6pCjRelr6YemUL6ON6tZHT1OjMzHprmlhx3TbWJxHxcCIQDZT0Q8UZkEiVjR4TCmcoSIUxNmI7m892xEcetLpvshJQIhAN/oB2MyCUeXrEQBUkKDtwI5RmUTCTX2mrKa3vrYcIErAiEAn3OiRgA5nzKIkbgC0p9E0CLlIRUlvJpwPui4FhK4QmECIB0ctUxcDDi1KzVN71Uj7vWME7U0jRC7R/kzT+d6mp7v";
		String msg = "";
		try {
			PrivateKey privateKey = RSA.getPrivateKey(priKey);
			String source = RSA.decryptByPrivateKey(license.getLicenseCode(), privateKey);
			if (source != null) {
				// System.out.println(source);
				// System.out.println(CommonUtil.getMACAddress());
				String[] licenseInfo = source.split(";");
				if (licenseInfo.length != 4 || !licenseInfo[0].equals(license.getName()) || !licenseInfo[1].equals(license.getRegisterDateName())
						|| StringUtil.toInt(licenseInfo[2]) != license.getPermitDays() || !licenseInfo[3].equals(CommonUtil.getMACAddress())) {
					msg = "授权码信息错误！";
				} else {
					license.setMac(CommonUtil.getMACAddress());
					licenseService.deleteLicense("");
					licenseService.addLicense(license);
					msg = "注册成功！";
				}
			} else {
				msg = "授权码错误！";
			}
		} catch (Exception e) {
			msg = "授权码错误！";
		}

		redirectAttributes.addFlashAttribute("msg", msg);
		redirectAttributes.addFlashAttribute("title", "数据初始化结果");
		return "redirect:../common/message.htm";
	}
	
	@RequestMapping("licenseInfo")
	public String licenseInfo(HttpServletRequest request, ModelMap map) throws Exception {
		License license = licenseService.licenseCheck();
		map.put("license", license);
		return "/system/licenseInfo";
	}
}