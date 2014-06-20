/*
 * Created on 2013-01-15 17:32:33
 *
 */
package com.liyiwei.cfw.privilege.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.privilege.entity.Role;
import com.liyiwei.common.cache.Cache;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Html;

/**
 * @author Liyiwei
 * 
 */
@Service
public class RoleService extends BaseRoleService {
	public String getRoleName(Integer id) {
		if (id == null) {
			return "";
		}

		Role role = getRole(id.intValue());
		return role == null ? "" : role.getName();
	}

	public String getRoleName(String ids) {
		if (StringUtil.isEmpty(ids)) {
			return "";
		}

		String[] value = ids.split(",");
		String name[] = new String[value.length];
		for (int i = 0; i < value.length; i++) {
			name[i] = getRoleName(StringUtil.toInt(value[i]));
		}

		return StringUtil.join(name, ",");
	}

	public String roleSelectBox(String selectBoxName, String selectData) {
		return roleSelectBox(selectBoxName, selectBoxName, selectData, "", "");
	}

	public String roleSelectBox(String selectBoxName, String selectData, String message) {
		return roleSelectBox(selectBoxName, selectBoxName, selectData, message, "");
	}

	public String roleSelectBox(String selectBoxName, String selectBoxId, String selectData, String message, String other) {
		List<Role> list = getRoleList();
		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size() + 1][2];
			paras[0][0] = "";
			paras[0][1] = message;

			int i = 1;
			for (Role role : list) {
				paras[i][0] = String.valueOf(role.getId());
				paras[i][1] = role.getName();
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

	public String roleCheckBox(String checkBoxName, String selectData) {
		List<Role> list = getRoleList();
		if (list != null && list.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (Role role : list) {
				sb.append(Html.checkBox(checkBoxName, String.valueOf(role.getId()), role.getName(), selectData));
			}
			return sb.toString();
		}
		return "";
	}

	@Override
	public Role getRole(int id) {
		return getRoleByCache(id);
	}

	@Override
	public List<Role> getRoleList() {
		return getRoleListByCache();
	}

	@SuppressWarnings("unchecked")
	private Role getRoleByCache(int id) {
		HashMap<Integer, Role> hashMap = (HashMap<Integer, Role>) Cache.get("Role");
		if (hashMap == null) {
			hashMap = new HashMap<Integer, Role>();
			List<Role> list = super.getRoleList();
			for (Role role : list) {
				hashMap.put(role.getId(), role);
			}
			Cache.put("Role", hashMap);
			Cache.put("RoleList", list);
		}

		return (Role) hashMap.get(id);
	}

	@SuppressWarnings("unchecked")
	private List<Role> getRoleListByCache() {
		List<Role> list = (List<Role>) Cache.get("RoleList");
		if (list == null) {
			HashMap<Integer, Role> hashMap = new HashMap<Integer, Role>();
			list = super.getRoleList();
			for (Role role : list) {
				hashMap.put(role.getId(), role);
			}
			Cache.put("Role", hashMap);
			Cache.put("RoleList", list);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public void addRole(Role role) {
		super.addRole(role);
		Cache.removeStartsWith("Role");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyRole(Role role) {
		super.modifyRole(role);
		Cache.removeStartsWith("Role");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteRole(int id) {
		super.deleteRole(id);
		Cache.removeStartsWith("Role");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteRole(String ids) {
		super.deleteRole(ids);
		Cache.removeStartsWith("Role");
	}
}