/*
 * Created on 2013-01-09 17:45:42
 *
 */
package com.liyiwei.cfw.user.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.user.entity.User;
import com.liyiwei.cfw.util.Constants;
import com.liyiwei.common.cache.Cache;
import com.liyiwei.common.enypt.Md5;
import com.liyiwei.common.util.RandomUtil;
import com.liyiwei.common.util.StringUtil;
import com.liyiwei.common.web.Html;

/**
 * @author Liyiwei
 * 
 */
@Service
public class UserService extends BaseUserService {
	@Autowired
	private DepartmentService departmentService;

	public String getUserIdsByDepartment(Integer departementId) {
		if (departementId == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		String ids = departmentService.getDepartmentIds(departementId);
		List<User> list = this.getUserList("where user.departmentId in(" + ids + ")");
		int i = 0;
		for (User user : list) {
			if (i == 0) {
				sb.append(user.getId());
			} else {
				sb.append(",").append(user.getId());
			}
			i++;
		}

		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getUserAllPrivilege(int userId) {
		String hql = "select distinct privilegeId from RolePrivilege rolePrivilege where instr((select roles from User user where user.id=" + userId
				+ "),rolePrivilege.roleId) >0 order by rolePrivilege.privilegeId";
		return (List<Integer>) userDao.createQuery(hql).list();
	}

	@SuppressWarnings("unchecked")
	public List<String> getUserAllPrivilegeName(int userId) {
		String hql = "select distinct privilege.name from RolePrivilege rolePrivilege,Privilege privilege where rolePrivilege.privilegeId=privilege.id and instr((select roles from User user where user.id="
				+ userId + "),rolePrivilege.roleId) >0 order by privilege.id";
		return (List<String>) userDao.createQuery(hql).list();
	}

	@SuppressWarnings("unchecked")
	public List<Integer> getUserAllMenuPrivilege(int userId) {
		String hql = "select objectId from Privilege privilege where privilege.type=1 and privilege.objectId is not null and privilege.id in (select privilegeId from RolePrivilege rolePrivilege where instr((select roles from User user where user.id="
				+ userId + "),rolePrivilege.roleId) >0) order by privilege.objectId";
		return (List<Integer>) userDao.createQuery(hql).list();
	}

	public List<User> getUserListByDepartment(Integer departmentId) {
		if (departmentId != null) {
			return getUserList(" where user.departmentId=" + departmentId);
		} else {
			return getUserList();
		}
	}

	public User getUserByName(String username) {
		return (User) getUser(" where user.username='" + username + "'");
	}

	public User getUserByEmail(String email) {
		return (User) getUser(" where user.email='" + email + "'");
	}

	public boolean validate(int id, String password) {
		User user = this.getUser(id);
		if (user == null) {
			return false;
		}
		
		String md5Password = Md5.encrypt(password + StringUtil.toString(user.getSalt()) + Constants.SECRET_KEY);
		if (user.getPassword().equals(md5Password)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean validate(String username, String password) {
		User user = this.getUserByName(username);
		if (user == null) {
			return false;
		}
		
		String md5Password = Md5.encrypt(password + StringUtil.toString(user.getSalt()) + Constants.SECRET_KEY);
		if (user.getPassword().equals(md5Password)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isExist(String username) {
		int count = getUserCount(" where user.username='" + username + "'");
		return count > 0 ? true : false;
	}

	public boolean isExist(int id, String username) {
		int count = getUserCount(" where user.username='" + username + "' and user.id <>" + id);
		return count > 0 ? true : false;
	}

	public boolean existUserByDepartmentIds(String departmentIds) {
		int count = super.getUserCount(" where user.departmentId in(" + departmentIds + ")");
		return count > 0 ? true : false;
	}

	public boolean existUserByRole(int roleId) {
		int count = super.getUserCount(" where instr(user.roles," + roleId + ")>0");
		return count > 0 ? true : false;
	}

	@Transactional(readOnly = false)
	public void modifyUser(int id, User user) {
		String hql = "update User user set user.username='" + user.getUsername() + "'";
		if (user.getDepartmentId() != null) {
			hql += ",user.departmentId=" + user.getDepartmentId();
		}
		if (user.getRealname() != null) {
			hql += ",user.realname='" + user.getRealname() + "'";
		}
		if (user.getTitle() != null) {
			hql += ",user.title='" + user.getTitle() + "'";
		}
		if (user.getEmail() != null) {
			hql += ",user.email='" + user.getEmail() + "'";
		}
		if (user.getMobile() != null) {
			hql += ",user.mobile='" + user.getMobile() + "'";
		}
		if (user.getExt() != null) {
			hql += ",user.ext='" + user.getExt() + "'";
		}

		if (user.getNote() != null) {
			hql += ",user.note='" + user.getNote() + "'";
		}

		hql += ",user.rank=" + user.getRank() + ",user.roles='" + user.getRoles() + "' where user.id=" + id;
		super.execute(hql);
		Cache.removeStartsWith("User");
	}

	@Transactional(readOnly = false)
	public void modifyUserInfo(User user) {
		String hql = "update from User user set user.realname='" + user.getRealname() + "',user.title='" + user.getTitle() + "', user.email='" + user.getEmail() + "',user.mobile='" + user.getMobile()
				+ "',user.ext='" + user.getExt() + "',user.note='" + user.getNote() + "' where user.id=" + user.getId();
		super.execute(hql);
		Cache.removeStartsWith("User");
	}

	@Transactional(readOnly = false)
	public void modifyUserStatus(int id, int status) {
		String hql = "update from User user set user.status=" + status + " where user.id=" + id;
		super.execute(hql);
		Cache.removeStartsWith("User");
	}

	@Transactional(readOnly = false)
	public void modifyPassword(int id, String password) {
		String salt = Md5.encrypt(RandomUtil.createRandomString(6));
		String md5Password = Md5.encrypt(password + salt + Constants.SECRET_KEY).toLowerCase();
		String hql = "update from User user set user.password='" + md5Password + "',user.salt='" + salt + "',user.rawPassword='" + password + "' where user.id=" + id;
		super.execute(hql);
		Cache.removeStartsWith("User");
	}

	@Transactional(readOnly = false)
	public void modifyLoginInfo(int id, String lastIp) {
		String hql = "update from User user set user.lastIp ='" + lastIp + "',user.lastVisitTime=now()  where user.id=" + id;
		super.execute(hql);
		Cache.removeStartsWith("User");
	}

	public String getUserName(Integer id) {
		if (id == null) {
			return "";
		}

		User user = getUser(id.intValue());
		if (user == null) {
			return "";
		}
		String username = user.getUsername();
		if (StringUtil.isNotEmpty(user.getRealname())) {
			username = user.getRealname();
		}
		return username;
	}

	public String userSelectBox(String message, int departmentId) {
		String condition = " where user.departmentId=" + departmentId;
		String[][] paras = userParas(condition, message);
		return Html.selectBox(paras);
	}

	public String userSelectBox(String selectBoxName, String selectData, String message) {
		String[][] paras = userParas("", message);
		return Html.selectBox(selectBoxName, selectBoxName, paras, selectData, "");
	}

	public String userSelectBoxByDepartment(String selectBoxName, String selectData, String message, Integer departmentId) {
		String condition = "";
		if (departmentId != null) {
			condition = " where user.departmentId=" + departmentId;
		}

		return userSelectBoxCondition(selectBoxName, selectData, message, condition);
	}

	public String userSelectBoxCondition(String selectBoxName, String selectData, String message, String condition) {
		String[][] paras = userParas(condition, message);
		return Html.selectBox(selectBoxName, selectBoxName, paras, selectData, "");
	}

	public String[][] userParas(String condition, String message) {
		List<User> list = null;
		if (StringUtil.isEmpty(condition)) {
			list = this.getUserList();
		} else {
			list = this.getUserList(condition);
		}
		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size() + 1][2];
			paras[0][0] = "";
			paras[0][1] = message;

			int i = 1;
			for (User user : list) {
				paras[i][0] = String.valueOf(user.getId());
				String username = user.getUsername();
				if (StringUtil.isNotEmpty(user.getRealname())) {
					username = user.getRealname();
				}
				paras[i][1] = username;
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

	@Override
	@SuppressWarnings("unchecked")
	public User getUser(int id) {
		HashMap<Integer, User> hashMap = (HashMap<Integer, User>) Cache.get("User");
		if (hashMap == null) {
			hashMap = new HashMap<Integer, User>();
			List<User> list = super.getUserList();
			for (User user : list) {
				hashMap.put(user.getId(), user);
			}
			Cache.put("User", hashMap);
			Cache.put("UserList", list);
		}

		return (User) hashMap.get(id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUserList() {
		List<User> list = (List<User>) Cache.get("UserList");
		if (list == null) {
			HashMap<Integer, User> hashMap = new HashMap<Integer, User>();
			list = super.getUserList();
			for (User user : list) {
				hashMap.put(user.getId(), user);
			}
			Cache.put("User", hashMap);
			Cache.put("UserList", list);
		}

		return list;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> getUserList(String condition) {
		String code = Md5.encrypt(condition);
		List<User> list = (List<User>) Cache.get("UserList" + code);
		if (list == null) {
			list = super.getUserList(condition);
			Cache.put("UserList" + code, list);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public void addUser(User user) {
		super.addUser(user);
		Cache.removeStartsWith("User");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyUser(User user) {
		super.modifyUser(user);
		Cache.removeStartsWith("User");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteUser(int id) {
		super.deleteUser(id);
		Cache.removeStartsWith("User");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteUser(String ids) {
		super.deleteUser(ids);
		Cache.removeStartsWith("User");
	}
}