/*
 * Created on 2013-02-19 17:14:46
 *
 */
package com.liyiwei.cfw.privilege.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.privilege.entity.RolePrivilege;
import com.liyiwei.common.cache.Cache;

/**
 * @author Liyiwei
 * 
 */
@Service
public class RolePrivilegeService extends BaseRolePrivilegeService {
	public List<RolePrivilege> getRoleAllPrivilege(int roleId) {
		return getRolePrivilegeList(" where rolePrivilege.roleId=" + roleId);
	}

	@Override
	public RolePrivilege getRolePrivilege(int id) {
		return getRolePrivilegeByCache(id);
	}

	@Override
	public List<RolePrivilege> getRolePrivilegeList() {
		return getRolePrivilegeListByCache();
	}

	@SuppressWarnings("unchecked")
	private RolePrivilege getRolePrivilegeByCache(int id) {
		HashMap<Integer, RolePrivilege> hashMap = (HashMap<Integer, RolePrivilege>) Cache.get("RolePrivilege");
		if (hashMap == null) {
			hashMap = new HashMap<Integer, RolePrivilege>();
			List<RolePrivilege> list = super.getRolePrivilegeList();
			for (RolePrivilege rolePrivilege : list) {
				hashMap.put(rolePrivilege.getId(), rolePrivilege);
			}
			Cache.put("RolePrivilege", hashMap);
			Cache.put("RolePrivilegeList", list);
		}

		return (RolePrivilege) hashMap.get(id);
	}

	@SuppressWarnings("unchecked")
	private List<RolePrivilege> getRolePrivilegeListByCache() {
		List<RolePrivilege> list = (List<RolePrivilege>) Cache.get("RolePrivilegeList");
		if (list == null) {
			HashMap<Integer, RolePrivilege> hashMap = new HashMap<Integer, RolePrivilege>();
			list = super.getRolePrivilegeList();
			for (RolePrivilege rolePrivilege : list) {
				hashMap.put(rolePrivilege.getId(), rolePrivilege);
			}
			Cache.put("RolePrivilege", hashMap);
			Cache.put("RolePrivilegeList", list);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public void addRolePrivilege(RolePrivilege rolePrivilege) {
		super.addRolePrivilege(rolePrivilege);
		Cache.removeStartsWith("RolePrivilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyRolePrivilege(RolePrivilege rolePrivilege) {
		super.modifyRolePrivilege(rolePrivilege);
		Cache.removeStartsWith("RolePrivilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyRolePrivilege(String condition) {
		super.modifyRolePrivilege(condition);
		Cache.removeStartsWith("RolePrivilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyRolePrivilege(int id, String field, String value) {
		super.modifyRolePrivilege(id, field, value);
		Cache.removeStartsWith("RolePrivilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteRolePrivilege(int id) {
		super.deleteRolePrivilege(id);
		Cache.removeStartsWith("RolePrivilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteRolePrivilege(String condition) {
		super.deleteRolePrivilege(condition);
		Cache.removeStartsWith("RolePrivilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteRolePrivilegeByIds(String ids) {
		super.deleteRolePrivilege(ids);
		Cache.removeStartsWith("RolePrivilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void execute(String hql) {
		super.execute(hql);
		Cache.removeStartsWith("RolePrivilege");
	}

	@Transactional(readOnly = false)
	public void deleteRoleAllPrivilege(int roleId) {
		deleteRolePrivilege(" where rolePrivilege.roleId=" + roleId);
	}
}