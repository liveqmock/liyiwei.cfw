/*
 * Created on 2013-02-19 17:14:46
 *
 */
package com.liyiwei.cfw.privilege.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.liyiwei.cfw.util.ApplicationUtil;

/**
 * @author Liyiwei
 * 
 */
@Entity
@Table(name = "rolePrivilege")
public class RolePrivilege implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer roleId;
	private Integer privilegeId;

	public RolePrivilege() {

	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "roleId", nullable = false)
	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Column(name = "privilegeId", nullable = false)
	public Integer getPrivilegeId() {
		return this.privilegeId;
	}

	public void setPrivilegeId(Integer privilegeId) {
		this.privilegeId = privilegeId;
	}

	@Transient
	public String getPrivilegeName() {
		return ApplicationUtil.getPrivilegeService().getPrivilegeName(this.privilegeId);
	}
}