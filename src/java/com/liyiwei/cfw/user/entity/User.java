/*
 * Created on 2013-01-09 17:45:42
 *
 */
package com.liyiwei.cfw.user.entity;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import com.liyiwei.cfw.util.ApplicationUtil;
import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Entity
@Table(name = "sysUser")
public class User implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String username;
	private String password;
	private Integer departmentId;
	private String realname;
	private String title;
	private String email;
	private String mobile;
	private String ext;
	private String note;
	private String rawPassword;
	private Integer rank;
	private String roles;
	private Integer status;
	private String salt;
	private Integer screenHeight;
	private Integer screenWidth;
	private Integer rowNum;
	private Date registerTime;
	private Date lastVisitTime;
	private String lastIp;

	public User() {

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

	@Column(name = "username", nullable = false, length = 20)
	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column(name = "password", nullable = false, length = 255)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "departmentId")
	public Integer getDepartmentId() {
		return this.departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	@Column(name = "realname", length = 40)
	public String getRealname() {
		return this.realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	@Column(name = "title", length = 40)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "email", length = 60)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "mobile", length = 60)
	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "ext", length = 20)
	public String getExt() {
		return this.ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	@Column(name = "note", length = 255)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "rawPassword", length = 12)
	public String getRawPassword() {
		return this.rawPassword;
	}

	public void setRawPassword(String rawPassword) {
		this.rawPassword = rawPassword;
	}

	@Column(name = "rank")
	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	@Column(name = "roles")
	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "salt")
	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	@Column(name = "screenHeight")
	public Integer getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(Integer screenHeight) {
		this.screenHeight = screenHeight;
	}

	@Column(name = "screenWidth")
	public Integer getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(Integer screenWidth) {
		this.screenWidth = screenWidth;
	}

	@Column(name = "rowNum")
	public Integer getRowNum() {
		return rowNum;
	}

	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "registerTime", nullable = false)
	public Date getRegisterTime() {
		return this.registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastVisitTime")
	public Date getLastVisitTime() {
		return this.lastVisitTime;
	}

	public void setLastVisitTime(Date lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	@Column(name = "lastIp", length = 20)
	public String getLastIp() {
		return this.lastIp;
	}

	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	@Transient
	public String getDepartmentName() {
		return ApplicationUtil.getDepartmentService().getDepartmentName(this.departmentId);
	}

	@Transient
	public String getStatusName() {
		return ApplicationUtil.getDictMeaning("user", "status", StringUtil.toInt(this.status), "");
	}

	@Transient
	public String getRankName() {
		return ApplicationUtil.getDictMeaning("user", "rank", StringUtil.toInt(this.rank), "");
	}

	@Transient
	public String getRolesName() {
		return ApplicationUtil.getRoleService().getRoleName(this.roles);
	}

	@Transient
	public String getRegisterTimeName() {
		return StringUtil.formatDateTime(registerTime);
	}

	@Transient
	public String getLastVisitTimeName() {
		return StringUtil.formatDateTime(lastVisitTime);
	}

	@Transient
	public String getDepartmentSelectBox() {
		return ApplicationUtil.getDepartmentService().departmentSelectBox("departmentId", String.valueOf(this.departmentId), " -请选择- ");
	}

	@Transient
	public String getRankSelectBox() {
		return ApplicationUtil.dictSelectBox("rank", "rank", String.valueOf(this.rank), " -请选择- ", "user", "rank", "");
	}

	@Transient
	public String getStatusSelectBox() {
		return ApplicationUtil.dictSelectBox("status", "status", String.valueOf(this.status), " -请选择- ", "user", "status", "");
	}

	@Transient
	public String getRolesCheckBox() {
		return ApplicationUtil.getRoleService().roleCheckBox("roles", this.roles);
	}
}