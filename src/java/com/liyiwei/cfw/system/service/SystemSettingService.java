/*
 * Created on 2013-02-06 15:55:50
 *
 */
package com.liyiwei.cfw.system.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.system.entity.SystemSetting;
import com.liyiwei.common.cache.Cache;
import com.liyiwei.common.web.Html;

/**
 * @author Liyiwei
 * 
 */
@Service
public class SystemSettingService extends BaseSystemSettingService {
	@SuppressWarnings("unchecked")
	public List<String> getCategoryList() {
		String hql = "select distinct category from SystemSetting systemSetting";
		return (List<String>) systemSettingDao.createQuery(hql).list();
	}

	public String categorySelectBox(String selectBoxName, String selectData, String message) {
		return categorySelectBox(selectBoxName, selectBoxName, selectData, message, "");
	}

	public String categorySelectBox(String selectBoxName, String selectBoxId, String selectData, String message, String other) {
		List<String> list = getCategoryListByCache();

		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size() + 1][2];
			paras[0][0] = "";
			paras[0][1] = message;

			int i = 1;
			for (String category : list) {
				paras[i][0] = category;
				paras[i][1] = category;
				i++;
			}
			return Html.selectBox(selectBoxName, selectBoxId, paras, selectData, other);
		} else {
			String[][] paras = new String[1][2];
			paras[0][0] = "";
			paras[0][1] = message;
			return Html.selectBox(selectBoxName, selectBoxId, paras, selectData, other);
		}
	}

	public String getSystemSetting(String name, String defaultValue) {
		String value = getSystemSettingByCache(name);
		return value == null ? defaultValue : value;
	}

	@Transactional(readOnly = false)
	public void setSystemSetting(String name,String category, String value) {
		SystemSetting systemSetting = super.getSystemSetting(" where systemSetting.name='" + name + "' and systemSetting.category='" + category + "'");
		if (systemSetting == null) {
			systemSetting = new SystemSetting();
			systemSetting.setName(name);
			systemSetting.setCategory(category);
			systemSetting.setValue(value);
			addSystemSetting(systemSetting);
		} else {
			systemSetting.setValue(value);
			modifySystemSetting(systemSetting);
		}
	}

	@Override
	public SystemSetting getSystemSetting(int id) {
		return getSystemSettingByCache(id);
	}

	@Override
	public List<SystemSetting> getSystemSettingList() {
		return getSystemSettingListByCache();
	}

	@SuppressWarnings("unchecked")
	private SystemSetting getSystemSettingByCache(int id) {
		HashMap<Integer, SystemSetting> hashMap = (HashMap<Integer, SystemSetting>) Cache.get("SystemSetting");
		if (hashMap == null) {
			hashMap = new HashMap<Integer, SystemSetting>();
			List<SystemSetting> list = super.getSystemSettingList();
			for (SystemSetting systemSetting : list) {
				hashMap.put(systemSetting.getId(), systemSetting);
			}
			Cache.put("SystemSetting", hashMap);
			Cache.put("SystemSettingList", list);
		}

		return (SystemSetting) hashMap.get(id);
	}

	@SuppressWarnings("unchecked")
	private String getSystemSettingByCache(String name) {
		HashMap<String, String> hashMap = (HashMap<String, String>) Cache.get("SystemSettingByName");
		if (hashMap == null) {
			hashMap = new HashMap<String, String>();
			List<SystemSetting> list = getSystemSettingList();
			for (SystemSetting systemSetting : list) {
				hashMap.put(systemSetting.getName(), systemSetting.getValue());
			}
			Cache.put("SystemSettingByName", hashMap);
		}

		return (String) hashMap.get(name);
	}

	@SuppressWarnings("unchecked")
	private List<SystemSetting> getSystemSettingListByCache() {
		List<SystemSetting> list = (List<SystemSetting>) Cache.get("SystemSettingList");
		if (list == null) {
			HashMap<Integer, SystemSetting> hashMap = new HashMap<Integer, SystemSetting>();
			list = super.getSystemSettingList();
			for (SystemSetting systemSetting : list) {
				hashMap.put(systemSetting.getId(), systemSetting);
			}
			Cache.put("SystemSetting", hashMap);
			Cache.put("SystemSettingList", list);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<String> getCategoryListByCache() {
		List<String> list = (List<String>) Cache.get("SystemSettingCategoryList");
		if (list == null) {
			list = this.getCategoryList();
			Cache.put("SystemSettingCategoryList", list);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public void addSystemSetting(SystemSetting systemSetting) {
		super.addSystemSetting(systemSetting);
		Cache.removeStartsWith("SystemSetting");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifySystemSetting(SystemSetting systemSetting) {
		super.modifySystemSetting(systemSetting);
		Cache.removeStartsWith("SystemSetting");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifySystemSetting(String condition) {
		super.modifySystemSetting(condition);
		Cache.removeStartsWith("SystemSetting");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifySystemSetting(int id, String field, String value) {
		super.modifySystemSetting(id, field, value);
		Cache.removeStartsWith("SystemSetting");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteSystemSetting(int id) {
		super.deleteSystemSetting(id);
		Cache.removeStartsWith("SystemSetting");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteSystemSetting(String condition) {
		super.deleteSystemSetting(condition);
		Cache.removeStartsWith("SystemSetting");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteSystemSettingByIds(String ids) {
		super.deleteSystemSetting(ids);
		Cache.removeStartsWith("SystemSetting");
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		super.execute(hql);
		Cache.removeStartsWith("SystemSetting");
	}
}