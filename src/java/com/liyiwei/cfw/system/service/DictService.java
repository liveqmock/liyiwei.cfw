/*
 * Created on 2013-02-06 16:45:33
 *
 */
package com.liyiwei.cfw.system.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.common.cache.Cache;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Html;
import com.liyiwei.cfw.system.entity.Dict;

/**
 * @author Liyiwei
 * 
 */
@Service
public class DictService extends BaseDictService {
	public boolean isExist(String tableName, String fieldName, String value) {
		int count = super.getDictCount(" where dict.tableName='" + tableName + "' and dict.fieldName='" + fieldName + "' and dict.value='" + value + "'");
		return count > 0 ? true : false;
	}

	@SuppressWarnings("unchecked")
	public List<String> getTableList() {
		String hql = "select distinct tableName from Dict dict";
		return (List<String>) dictDao.createQuery(hql).list();
	}

	public String tableSelectBox(String selectBoxName, String selectData, String message) {
		return tableSelectBox(selectBoxName, selectBoxName, selectData, message, "");
	}

	public String tableSelectBox(String selectBoxName, String selectBoxId, String selectData, String message, String other) {
		List<String> list = getTableListByCache();

		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size() + 1][2];
			paras[0][0] = "";
			paras[0][1] = message;

			int i = 1;
			for (String tableName : list) {
				paras[i][0] = tableName;
				paras[i][1] = tableName;
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

	@SuppressWarnings("unchecked")
	public List<String> getFieldListByTable(String tableName) {
		String hql = "select distinct fieldName from Dict dict where dict.tableName='" + tableName + "'";
		return (List<String>) dictDao.createQuery(hql).list();
	}

	public String fieldSelectBox(String tableName, String selectBoxName, String selectData, String message) {
		return fieldSelectBox(tableName, selectBoxName, selectBoxName, selectData, message, "");
	}

	public String fieldSelectBox(String tableName, String selectBoxName, String selectBoxId, String selectData, String message, String other) {
		List<String> list = getFieldListByTableCache(tableName);

		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size() + 1][2];
			paras[0][0] = "";
			paras[0][1] = message;

			int i = 1;
			for (String fieldName : list) {
				paras[i][0] = fieldName;
				paras[i][1] = fieldName;
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

	public String fieldSelectBoxByTable(String tableName, String message) {
		List<String> list = getFieldListByTable(tableName);

		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size() + 1][2];
			paras[0][0] = "";
			paras[0][1] = message;

			int i = 1;
			for (String fieldName : list) {
				paras[i][0] = fieldName;
				paras[i][1] = fieldName;
				i++;
			}
			return Html.selectBox(paras);
		} else {
			String[][] paras = new String[1][2];
			paras[0][0] = "";
			paras[0][1] = message;
			return Html.selectBox(paras);
		}
	}

	public String getDictMeaning(String tableName, String fieldName, int value, String defaultValue) {
		String meaning = getDictMeaningByCache(tableName, fieldName, StringUtil.toString(value));
		return meaning == null ? defaultValue : meaning;
	}

	public String getDictMeaning(String tableName, String fieldName, String value, String defaultValue) {
		String meaning = getDictMeaningByCache(tableName, fieldName, value);
		return meaning == null ? defaultValue : meaning;
	}

	public String getDictMeanings(String tableName, String fieldName, String values) {
		if (StringUtil.isEmpty(values)) {
			return "";
		}

		String[] value = values.split(",");
		String meaning[] = new String[value.length];
		for (int i = 0; i < value.length; i++) {
			meaning[i] = getDictMeaningByCache(tableName, fieldName, value[i]);
		}

		return StringUtil.join(meaning, ",");
	}

	public Integer getDictValue(String tableName, String fieldName, String meaning) {
		return StringUtil.toInteger(getDictStringValue(tableName, fieldName, meaning, null));
	}

	public Integer getDictValue(String tableName, String fieldName, String meaning, Integer defaultValue) {
		return StringUtil.toInteger(getDictStringValue(tableName, fieldName, meaning, String.valueOf(defaultValue)));
	}

	public String getDictStringValue(String tableName, String fieldName, String meaning, String defaultValue) {
		if (meaning == null) {
			return defaultValue;
		}
		
		String value = getDictValueByCache(tableName, fieldName, meaning);
		if (value == null) {
			return defaultValue;
		} else {
			return value;
		}
	}

	public String dictSelectBox(String selectBoxName, String selectData, String message, String tableName, String fieldName) {
		return dictSelectBox(selectBoxName, selectBoxName, selectData, message, tableName, fieldName, "");
	}

	public String dictSelectBox(String selectBoxName, String selectBoxId, String selectData, String message, String tableName, String fieldName, String other) {
		List<Dict> list = getDictListByTableFieldByCache(tableName, fieldName);
		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size() + 1][2];
			paras[0][0] = "";
			paras[0][1] = message;

			int i = 1;
			for (Dict dict : list) {
				paras[i][0] = dict.getValue();
				paras[i][1] = dict.getMeaning();
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

	public String dictRadio(String radioName, String selectData, String tableName, String fieldName) {
		List<Dict> list = getDictListByTableFieldByCache(tableName, fieldName);
		if (list != null && list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Dict dict : list) {
				sb.append(Html.radio(radioName, String.valueOf(dict.getValue()), dict.getMeaning(), selectData));
			}
			return sb.toString();
		}
		return "";
	}

	public String dictRadio(String radioName, String selectData, String tableName, String fieldName, String other) {
		List<Dict> list = getDictListByTableFieldByCache(tableName, fieldName);
		if (list != null && list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			int i = 0;
			for (Dict dict : list) {
				if (i == 0) {
					sb.append(Html.radio(radioName, "", String.valueOf(dict.getValue()), dict.getMeaning(), selectData, other));
				} else {
					sb.append(Html.radio(radioName, String.valueOf(dict.getValue()), dict.getMeaning(), selectData));
				}
				i++;
			}
			return sb.toString();
		}
		return "";
	}

	public String dictCheckBox(String checkBoxName, String selectData, String tableName, String fieldName) {
		List<Dict> list = getDictListByTableFieldByCache(tableName, fieldName);
		if (list != null && list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Dict dict : list) {
				sb.append(Html.checkBox(checkBoxName, dict.getValue(), dict.getMeaning(), selectData));
			}
			return sb.toString();
		}
		return "";
	}

	public String dictCheckBox(String checkBoxName, String selectData, String tableName, String fieldName, String other) {
		List<Dict> list = getDictListByTableFieldByCache(tableName, fieldName);
		if (list != null && list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			int i = 0;
			for (Dict dict : list) {
				if (i == 0) {
					sb.append(Html.checkBox(checkBoxName, "", dict.getValue(), dict.getMeaning(), selectData, other));
				} else {
					sb.append(Html.checkBox(checkBoxName, dict.getValue(), dict.getMeaning(), selectData));
				}
				i++;
			}
			return sb.toString();
		}
		return "";
	}

	public String dictCheckBox(String checkBoxName, String selectData, String tableName, String fieldName, int rownum, boolean flag) {
		List<Dict> list = getDictListByTableFieldByCache(tableName, fieldName);
		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size()][2];

			int i = 0;
			for (Dict dict : list) {
				paras[i][0] = dict.getValue();
				paras[i][1] = dict.getMeaning();
				i++;
			}
			return Html.checkBox(checkBoxName, paras, selectData, rownum, flag);
		}
		return "";
	}

	public String dictCheckBox(String checkBoxName, String selectData, String tableName, String fieldName, int rownum, int width) {
		List<Dict> list = getDictListByTableFieldByCache(tableName, fieldName);
		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size()][2];

			int i = 0;
			for (Dict dict : list) {
				paras[i][0] = dict.getValue();
				paras[i][1] = dict.getMeaning();
				i++;
			}
			return Html.checkBox(checkBoxName, paras, selectData, rownum, width);
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public List<Dict> getDictListByTableFieldByCache(String tableName, String fieldName) {
		List<Dict> list = (List<Dict>) Cache.get("Dict_" + tableName + "_" + fieldName);
		if (list == null) {
			list = super.getDictList(" where dict.tableName='" + tableName + "' and dict.fieldName='" + fieldName + "' order by dict.id");
			Cache.put("Dict_" + tableName + "_" + fieldName, list);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public String getDictMeaningByCache(String tableName, String fieldName, String value) {
		HashMap<String, String> hashMap = (HashMap<String, String>) Cache.get("DictByValue");
		if (hashMap == null) {
			hashMap = new HashMap<String, String>();
			List<Dict> list = getDictList();
			for (Dict dict : list) {
				hashMap.put(dict.getTableName() + "|" + dict.getFieldName() + "|" + dict.getValue(), dict.getMeaning());
			}
			Cache.put("DictByValue", hashMap);
		}

		return (String) hashMap.get(tableName + "|" + fieldName + "|" + value);
	}
	
	@SuppressWarnings("unchecked")
	public String getDictValueByCache(String tableName, String fieldName, String meaning) {
		HashMap<String, String> hashMap = (HashMap<String, String>) Cache.get("DictByMeaning");
		if (hashMap == null) {
			hashMap = new HashMap<String, String>();
			List<Dict> list = getDictList();
			for (Dict dict : list) {
				hashMap.put(dict.getTableName() + "|" + dict.getFieldName() + "|meaning|" + dict.getMeaning(), dict.getValue());
			}
			Cache.put("DictByMeaning", hashMap);
		}

		return (String) hashMap.get(tableName + "|" + fieldName + "|meaning|" + meaning);
	}

	@SuppressWarnings("unchecked")
	public List<String> getTableListByCache() {
		List<String> list = (List<String>) Cache.get("DictTableList");
		if (list == null) {
			list = this.getTableList();
			Cache.put("DictTableList", list);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	public List<String> getFieldListByTableCache(String tableName) {
		List<String> list = (List<String>) Cache.get("DictFieldList" + "_" + tableName);
		if (list == null) {
			list = this.getFieldListByTable(tableName);
			Cache.put("DictFieldList" + "_" + tableName, list);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public void addDict(Dict dict) {
		super.addDict(dict);
		Cache.removeStartsWith("Dict");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyDict(Dict dict) {
		super.modifyDict(dict);
		Cache.removeStartsWith("Dict");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyDict(String condition) {
		super.modifyDict(condition);
		Cache.removeStartsWith("Dict");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyDict(int id, String field, String value) {
		super.modifyDict(id, field, value);
		Cache.removeStartsWith("Dict");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteDict(int id) {
		super.deleteDict(id);
		Cache.removeStartsWith("Dict");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteDict(String condition) {
		super.deleteDict(condition);
		Cache.removeStartsWith("Dict");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteDictByIds(String ids) {
		super.deleteDict(ids);
		Cache.removeStartsWith("Dict");
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		super.execute(hql);
		Cache.removeStartsWith("Dict");
	}
}