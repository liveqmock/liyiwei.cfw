/*
 * Created on 2013-02-19 17:12:29
 *
 */
package com.liyiwei.cfw.privilege.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.privilege.entity.Privilege;
import com.liyiwei.common.cache.Cache;

/**
 * @author Liyiwei
 * 
 */
@Service
public class PrivilegeService extends BasePrivilegeService {
	public Integer getMenuPrivilege(int menuId) {
		Privilege privilege = getPrivilegeByMenuCache(menuId);
		return privilege == null ? null : privilege.getId();
	}

	public String getPrivilegeName(Integer id) {
		if (id == null) {
			return "";
		}

		Privilege privilege = getPrivilege(id.intValue());
		return privilege == null ? "" : privilege.getName();
	}

	@Override
	public Privilege getPrivilege(int id) {
		return getPrivilegeByCache(id);
	}

	@Override
	public List<Privilege> getPrivilegeList() {
		return getPrivilegeListByCache();
	}

	@SuppressWarnings("unchecked")
	private Privilege getPrivilegeByCache(int id) {
		HashMap<Integer, Privilege> hashMap = (HashMap<Integer, Privilege>) Cache.get("Privilege");
		if (hashMap == null) {
			hashMap = new HashMap<Integer, Privilege>();
			List<Privilege> list = super.getPrivilegeList();
			for (Privilege privilege : list) {
				hashMap.put(privilege.getId(), privilege);
			}
			Cache.put("Privilege", hashMap);
			Cache.put("PrivilegeList", list);
		}

		return (Privilege) hashMap.get(id);
	}

	@SuppressWarnings("unchecked")
	private List<Privilege> getPrivilegeListByCache() {
		List<Privilege> list = (List<Privilege>) Cache.get("PrivilegeList");
		if (list == null) {
			HashMap<Integer, Privilege> hashMap = new HashMap<Integer, Privilege>();
			list = super.getPrivilegeList();
			for (Privilege privilege : list) {
				hashMap.put(privilege.getId(), privilege);
			}
			Cache.put("Privilege", hashMap);
			Cache.put("PrivilegeList", list);
		}

		return list;
	}

	private Privilege getPrivilegeByMenuCache(int menuId) {
		Privilege privilege = (Privilege) Cache.get("Privilege_Menu" + "_" + menuId);
		if (privilege == null) {
			privilege = super.getPrivilege(" where privilege.objectId=" + menuId + " and privilege.type=1");
			Cache.put("Privilege_Menu" + "_" + menuId, privilege);
		}

		return privilege;
	}

	@Override
	@Transactional(readOnly = false)
	public void addPrivilege(Privilege privilege) {
		super.addPrivilege(privilege);
		Cache.removeStartsWith("Privilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyPrivilege(Privilege privilege) {
		super.modifyPrivilege(privilege);
		Cache.removeStartsWith("Privilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyPrivilege(String condition) {
		super.modifyPrivilege(condition);
		Cache.removeStartsWith("Privilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyPrivilege(int id, String field, String value) {
		super.modifyPrivilege(id, field, value);
		Cache.removeStartsWith("Privilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePrivilege(int id) {
		super.deletePrivilege(id);
		Cache.removeStartsWith("Privilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePrivilege(String condition) {
		super.deletePrivilege(condition);
		Cache.removeStartsWith("Privilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void deletePrivilegeByIds(String ids) {
		super.deletePrivilege(ids);
		Cache.removeStartsWith("Privilege");
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		super.execute(hql);
		Cache.removeStartsWith("Privilege");
	}
}