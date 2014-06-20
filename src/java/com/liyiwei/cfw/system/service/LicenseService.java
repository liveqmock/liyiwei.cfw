/*
 * Created on 2014-04-10 15:25:29
 *
 */
package com.liyiwei.cfw.system.service;

import java.security.PrivateKey;

import org.springframework.stereotype.Service;

import com.liyiwei.cfw.system.entity.License;
import com.liyiwei.common.enypt.RSA;
import com.liyiwei.common.util.CommonUtil;
import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Service
public class LicenseService extends BaseLicenseService {
	public License licenseCheck() {
		String priKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAxx07jlV4T+c7I61m6ChXomyow5Wa8aGZ99sKjcqJPRk7Ho4JUnP0PtIxgwAFvf5hqpGz2SzNJf0desBPNOh1UwIDAQABAkAXc6r7FbgI+Twu9JKFT5Maz1+FQc6xHQ6MceEpWCyX5HaoC2cn7Gy24wAC5j6rjo95Khy3itDt83V/j31He+apAiEA6pCjRelr6YemUL6ON6tZHT1OjMzHprmlhx3TbWJxHxcCIQDZT0Q8UZkEiVjR4TCmcoSIUxNmI7m892xEcetLpvshJQIhAN/oB2MyCUeXrEQBUkKDtwI5RmUTCTX2mrKa3vrYcIErAiEAn3OiRgA5nzKIkbgC0p9E0CLlIRUlvJpwPui4FhK4QmECIB0ctUxcDDi1KzVN71Uj7vWME7U0jRC7R/kzT+d6mp7v";
		License license = super.getLicense(" order by id desc");
		if (license != null) {
			try {
				PrivateKey privateKey = RSA.getPrivateKey(priKey);
				String source = RSA.decryptByPrivateKey(license.getLicenseCode(), privateKey);
				if (source != null) {
					// System.out.println(source);
					// System.out.println(CommonUtil.getMACAddress());
					String[] licenseInfo = source.split(";");
					if (licenseInfo.length == 4 && licenseInfo[0].equals(license.getName()) && licenseInfo[1].equals(license.getRegisterDateName())
							&& StringUtil.toInt(licenseInfo[2]) == license.getPermitDays() && licenseInfo[3].equals(CommonUtil.getMACAddress())) {
						return license;
					}
				}
			} catch (Exception e) {
			}
		}

		return null;
	}
}