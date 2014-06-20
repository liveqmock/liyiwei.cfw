/*
 * Created on 2012-04-18 15:37:00
 *
 */
package com.liyiwei.cfw.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.liyiwei.cfw.base.service.AreaService;
import com.liyiwei.cfw.base.service.CategoryService;
import com.liyiwei.cfw.privilege.service.PrivilegeService;
import com.liyiwei.cfw.privilege.service.RoleService;
import com.liyiwei.cfw.system.service.DictService;
import com.liyiwei.cfw.system.service.MenuService;
import com.liyiwei.cfw.system.service.SystemSettingService;
import com.liyiwei.cfw.user.service.DepartmentService;
import com.liyiwei.cfw.user.service.UserService;

/**
 * @author Liyiwei
 * 
 */
public class ApplicationUtil {
	protected static ApplicationContext applicationContext;
	protected static DictService dictService;
	protected static SystemSettingService systemSettingService;
	protected static MenuService menuService;
	protected static DepartmentService departmentService;
	protected static UserService userService;
	protected static RoleService roleService;
	protected static PrivilegeService privilegeService;

	protected static CategoryService categoryService;
	protected static AreaService areaService;

	static {
		applicationContext = new ClassPathXmlApplicationContext("/applicationContext.xml");
		dictService = (DictService) applicationContext.getBean("dictService");
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static DictService getDictService() {
		return dictService;
	}

	public static MenuService getMenuService() {
		if (menuService == null) {
			menuService = (MenuService) applicationContext.getBean("menuService");
		}
		return menuService;
	}

	public static DepartmentService getDepartmentService() {
		if (departmentService == null) {
			departmentService = (DepartmentService) applicationContext.getBean("departmentService");
		}
		return departmentService;
	}

	public static UserService getUserService() {
		if (userService == null) {
			userService = (UserService) applicationContext.getBean("userService");
		}
		return userService;
	}

	public static RoleService getRoleService() {
		if (roleService == null) {
			roleService = (RoleService) applicationContext.getBean("roleService");
		}
		return roleService;
	}

	public static PrivilegeService getPrivilegeService() {
		if (privilegeService == null) {
			privilegeService = (PrivilegeService) applicationContext.getBean("privilegeService");
		}
		return privilegeService;
	}

	public static SystemSettingService getSystemSettingService() {
		if (systemSettingService == null) {
			systemSettingService = (SystemSettingService) applicationContext.getBean("systemSettingService");
		}
		return systemSettingService;
	}

	public static CategoryService getCategoryService() {
		if (categoryService == null) {
			categoryService = (CategoryService) applicationContext.getBean("categoryService");
		}
		return categoryService;
	}

	public static AreaService getAreaService() {
		if (areaService == null) {
			areaService = (AreaService) applicationContext.getBean("areaService");
		}
		return areaService;
	}

	public static String getDictMeaning(String tableName, String fieldName, int value, String defaultValue) {
		return dictService.getDictMeaning(tableName, fieldName, value, defaultValue);
	}

	public static String getDictMeaning(String tableName, String fieldName, String value) {
		return dictService.getDictMeanings(tableName, fieldName, value);
	}

	public static String dictSelectBox(String selectBoxName, String selectData, String message, String tableName, String fieldName) {
		return dictService.dictSelectBox(selectBoxName, selectData, message, tableName, fieldName);
	}

	public static String dictSelectBox(String selectBoxName, String id, String selectData, String message, String tableName, String fieldName, String other) {
		return dictService.dictSelectBox(selectBoxName, id, selectData, message, tableName, fieldName, other);
	}

	public static String dictRadio(String radioName, String selectData, String tableName, String fieldName) {
		return dictService.dictRadio(radioName, selectData, tableName, fieldName);
	}

	public static String dictRadio(String radioName, String selectData, String tableName, String fieldName, String other) {
		return dictService.dictRadio(radioName, selectData, tableName, fieldName, other);
	}

	public static String dictCheckBox(String checkBoxName, String selectData, String tableName, String fieldName) {
		return dictService.dictCheckBox(checkBoxName, selectData, tableName, fieldName);
	}

	public static String dictCheckBox(String checkBoxName, String selectData, String tableName, String fieldName, String other) {
		return dictService.dictCheckBox(checkBoxName, selectData, tableName, fieldName, other);
	}

	public static String dictCheckBox(String checkBoxName, String selectData, String tableName, String fieldName, int rownum, boolean flag) {
		return dictService.dictCheckBox(checkBoxName, selectData, tableName, fieldName, rownum, flag);
	}

	public static String dictCheckBox(String checkBoxName, String selectData, String tableName, String fieldName, int rownum, int width) {
		return dictService.dictCheckBox(checkBoxName, selectData, tableName, fieldName, rownum, width);
	}
}