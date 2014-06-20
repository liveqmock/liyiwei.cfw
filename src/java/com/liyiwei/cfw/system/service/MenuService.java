/*
 * Created on 2013-02-18 15:22:08
 *
 */
package com.liyiwei.cfw.system.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.privilege.entity.Privilege;
import com.liyiwei.cfw.privilege.service.PrivilegeService;
import com.liyiwei.cfw.privilege.service.RolePrivilegeService;
import com.liyiwei.cfw.system.entity.Menu;
import com.liyiwei.common.cache.Cache;
import com.liyiwei.common.web.Html;

/**
 * @author Liyiwei
 * 
 */
@Service
public class MenuService extends BaseMenuService {
	@Autowired
	PrivilegeService privilegeService;
	@Autowired
	RolePrivilegeService rolePrivilegeService;

	public List<Menu> getMenuRootList() {
		return getMenuRootListByCache();
	}

	public List<Menu> getMenuListByParentId(int parentId) {
		return getMenuListByParentIdCache(parentId);
	}

	public boolean existChildMenu(int parentId) {
		int count = super.getMenuCount(" where menu.parentId =" + parentId);
		return count > 0 ? true : false;
	}

	public boolean existChildMenu(String parentIds) {
		int count = super.getMenuCount(" where menu.parentId in(" + parentIds + ")");
		return count > 0 ? true : false;
	}

	@Transactional(readOnly = false)
	public void upMenuSeq(int parentId, int sno) {
		List<Menu> list = getMenuList("where  menu.parentId=" + parentId + " order by menu.seq,menu.id");
		int i = 1;
		for (Menu menu : list) {
			if (i == sno - 1) {
				menu.setSeq(parentId * 100 + i + 1);
			} else if (i == sno) {
				menu.setSeq(parentId * 100 + i - 1);
			} else {
				menu.setSeq(parentId * 100 + i);
			}
			super.modifyMenu(menu);
			i++;
		}
		Cache.removeStartsWith("Menu");
	}

	@Transactional(readOnly = false)
	public void downMenuSeq(int parentId, int sno) {
		List<Menu> list = getMenuList("where  menu.parentId=" + parentId + " order by  menu.seq,menu.id");
		int i = 1;
		for (Menu menu : list) {
			if (i == sno) {
				menu.setSeq(parentId * 100 + i + 1);
			} else if (i == sno + 1) {
				menu.setSeq(parentId * 100 + i - 1);
			} else {
				menu.setSeq(parentId * 100 + i);
			}
			super.modifyMenu(menu);
			i++;
		}
		Cache.removeStartsWith("Menu");
	}

	public String getMenuName(Integer id) {
		if (id == null) {
			return "";
		}

		Menu menu = getMenu(id.intValue());
		return menu == null ? "" : menu.getName();
	}

	public String menuSelectBox(String selectBoxName, String selectData) {
		return menuSelectBox(selectBoxName, selectBoxName, selectData, "", "");
	}

	public String menuSelectBox(String selectBoxName, String selectData, String message) {
		return menuSelectBox(selectBoxName, selectBoxName, selectData, message, "");
	}

	public String menuSelectBox(String selectBoxName, String selectBoxId, String selectData, String message, String other) {
		List<Menu> list = getMenuList();
		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size() + 1][2];
			paras[0][0] = "";
			paras[0][1] = message;

			int i = 1;
			for (Menu menu : list) {
				paras[i][0] = String.valueOf(menu.getId());
				paras[i][1] = menu.getName();
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

	@Override
	public Menu getMenu(int id) {
		return getMenuByCache(id);
	}

	@Override
	public List<Menu> getMenuList() {
		return getMenuListByCache();
	}

	@SuppressWarnings("unchecked")
	private Menu getMenuByCache(int id) {
		HashMap<Integer, Menu> hashMap = (HashMap<Integer, Menu>) Cache.get("Menu");
		if (hashMap == null) {
			hashMap = new HashMap<Integer, Menu>();
			List<Menu> list = getMenuList(" order by menu.seq,menu.id");
			for (Menu menu : list) {
				hashMap.put(menu.getId(), menu);
			}
			Cache.put("Menu", hashMap);
			Cache.put("MenuList", list);
		}

		return (Menu) hashMap.get(id);
	}

	@SuppressWarnings("unchecked")
	private List<Menu> getMenuListByCache() {
		List<Menu> list = (List<Menu>) Cache.get("MenuList");
		if (list == null) {
			HashMap<Integer, Menu> hashMap = new HashMap<Integer, Menu>();
			list = getMenuList(" order by menu.seq,menu.id");
			for (Menu menu : list) {
				hashMap.put(menu.getId(), menu);
			}
			Cache.put("Menu", hashMap);
			Cache.put("MenuList", list);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	private List<Menu> getMenuRootListByCache() {
		List<Menu> list = (List<Menu>) Cache.get("MenuRootList");
		if (list == null) {
			list = getMenuList(" where menu.parentId is null or menu.parentId=0");
			Cache.put("MenuRootList", list);
		}

		return list;
	}

	@SuppressWarnings("unchecked")
	private List<Menu> getMenuListByParentIdCache(int parentId) {
		List<Menu> list = (List<Menu>) Cache.get("MenuList" + "_" + parentId);
		if (list == null) {
			list = getMenuList(" where menu.parentId =" + parentId + " order by  menu.seq,menu.id");
			Cache.put("MenuList" + "_" + parentId, list);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public void addMenu(Menu menu) {
		super.addMenu(menu);
		if (menu.getParentId() == null || menu.getParentId() == 0) {
			menu.setSeq(menu.getId() * 100);
		} else {
			menu.setSeq(menu.getParentId() * 100 + 99);
		}
		super.modifyMenu(menu);
		Privilege privilege = new Privilege();
		privilege.setName(menu.getName());
		privilege.setObjectId(menu.getId());
		privilege.setType(1);
		if (menu.getParentId() != null && menu.getParentId() != 0) {
			int parentId = privilegeService.getMenuPrivilege(menu.getParentId());
			privilege.setParentId(parentId);
		}
		privilegeService.addPrivilege(privilege);

		Cache.removeStartsWith("Menu");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyMenu(Menu menu) {
		super.modifyMenu(menu);
		Cache.removeStartsWith("Menu");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyMenu(String condition) {
		super.modifyMenu(condition);
		Cache.removeStartsWith("Menu");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyMenu(int id, String field, String value) {
		super.modifyMenu(id, field, value);
		Cache.removeStartsWith("Menu");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteMenu(int id) {
		super.deleteMenu(id);
		rolePrivilegeService.deleteRolePrivilege(" where rolePrivilege.privilegeId in(select id from Privilege privilege where privilege.type=1 and privilege.objectId=" + id + ")");
		privilegeService.deletePrivilege(" where privilege.type=1 and privilege.objectId=" + id);
		Cache.removeStartsWith("Menu");
		Cache.removeStartsWith("Privilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteMenu(String condition) {
		super.deleteMenu(condition);
		rolePrivilegeService.deleteRolePrivilege(" where rolePrivilege.privilegeId in(select id from Privilege privilege where privilege.type=1 and privilege.objectId in (select id from Menu menu "
				+ condition + "))");
		privilegeService.deletePrivilege(" where privilege.type=1 and privilege.objectId in (select id from Menu menu " + condition + ")");
		Cache.removeStartsWith("Menu");
		Cache.removeStartsWith("Privilege");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteMenuByIds(String ids) {
		super.deleteMenuByIds(ids);
		rolePrivilegeService.deleteRolePrivilege(" where rolePrivilege.privilegeId in(select id from Privilege privilege where privilege.type=1 and privilege.objectId in (" + ids + "))");
		privilegeService.deletePrivilege(" where privilege.type=1 and privilege.objectId in (" + ids + ")");
		Cache.removeStartsWith("Menu");
		Cache.removeStartsWith("Privilege");
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		super.execute(hql);
		Cache.removeStartsWith("Menu");
	}
}