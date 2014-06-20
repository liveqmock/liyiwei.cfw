/*
 * Created on 2013-12-24 13:58:15
 *
 */
package com.liyiwei.cfw.base.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.liyiwei.cfw.base.entity.Area;
import com.liyiwei.common.cache.Cache;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Html;

/**
 * @author Liyiwei
 * 
 */
@Service
public class AreaService extends BaseAreaService {
	@SuppressWarnings("unchecked")
	private String getProvinceByCache(String code) {
		HashMap<String, String> hashMap = (HashMap<String, String>) Cache.get("Province");
		if (hashMap == null) {
			hashMap = new HashMap<String, String>();
			List<Area> list = super.getAreaList(" where area.type=1 order by area.code");
			for (Area area : list) {
				hashMap.put(area.getCode(), area.getName());
			}
			Cache.put("Province", hashMap);
		}

		return (String) hashMap.get(code);
	}

	@SuppressWarnings("unchecked")
	private List<Area> getProvinceListByCache() {
		List<Area> list = (List<Area>) Cache.get("ProvinceList");
		if (list == null) {
			list = super.getAreaList(" where area.type=1 order by area.code");
			Cache.put("ProvinceList", list);
		}

		return list;
	}

	public String getAreaName(String areaCode) {
		if (areaCode == null) {
			return "";
		}
		String province = "";
		String city = "";
		String district = "";
		Area area = super.getArea(" where area.code='" + areaCode + "'");
		if (area == null) {
			return "";
		}
		if (areaCode.length() > 6) {
			district = area.getName();
			String areaCode1 = areaCode.substring(0, 6);
			Area area1 = super.getArea(" where area.code='" + areaCode1 + "'");
			if (area1 != null) {
				city = area1.getName();
			}
			province = StringUtil.toString(getProvinceByCache(areaCode.substring(0, 3)));
			return province + "-" + city + "-" + district;
		} else if (areaCode.length() > 3) {
			city = area.getName();
			province = StringUtil.toString(getProvinceByCache(areaCode.substring(0, 3)));
			return province + "-" + city;
		} else {
			province = area.getName();
			return province;
		}
	}

	public String areaSelectBox(String areaCode) {
		StringBuffer sb = new StringBuffer();
		String province = "";
		String city = "";
		String district = "";

		if (areaCode != null) {
			if (areaCode.length() > 6) {
				district = areaCode;
				city = areaCode.substring(0, 6);
				province = areaCode.substring(0, 3);
			} else if (areaCode.length() > 3) {
				city = areaCode;
				province = areaCode.substring(0, 3);
			} else {
				province = areaCode;
			}
		}
		sb.append(Html.selectBox("province", "province", provinceParas(), province, ""));
		sb.append("&nbsp;");
		sb.append(Html.selectBox("city", "city", cityParas(province), city, ""));
		sb.append("&nbsp;");
		sb.append(Html.selectBox("district", "district", districtParas(city), district, ""));

		return sb.toString();
	}

	public String[][] provinceParas() {
		List<Area> list = getProvinceListByCache();
		return areaParas(list, "省份");
	}

	public String citySelectBox(String province) {
		String[][] paras = cityParas(province);
		return Html.selectBox(paras);
	}

	public String[][] cityParas(String province) {
		List<Area> list = super.getAreaList(" where substring(area.code,1,3)='" + province + "' and area.type=2");
		return areaParas(list, "城市");
	}

	public String districtSelectBox(String city) {
		String[][] paras = districtParas(city);
		return Html.selectBox(paras);
	}

	public String[][] districtParas(String city) {
		List<Area> list = super.getAreaList(" where substring(area.code,1,6)='" + city + "' and area.type=3");
		return areaParas(list, "区县");
	}

	public String[][] areaParas(List<Area> list, String message) {
		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size() + 1][2];
			paras[0][0] = "";
			paras[0][1] = message;

			int i = 1;
			for (Area area : list) {
				paras[i][0] = area.getCode();
				paras[i][1] = area.getName();
				i++;
			}
			return paras;
		} else {
			String[][] paras = new String[1][2];
			paras[0][0] = "";
			paras[0][1] = message;
			return paras;
		}
	}
}