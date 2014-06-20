/*
 * Created on 2013-02-18 13:59:31
 *
 */
package com.liyiwei.cfw.system.entity;

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
@Table(name = "actionLog")
public class ActionLog implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String system;
	private String module;
	private String action;
	private String objectId;
	private String note;
	private String ip;
	private Integer userId;
	private Date logTime;

	public ActionLog() {

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

	@Column(name = "system", nullable = false, length = 40)
	public String getSystem() {
		return this.system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	@Column(name = "module", nullable = false, length = 40)
	public String getModule() {
		return this.module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Column(name = "action", nullable = false, length = 40)
	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Column(name = "objectId")
	public String getObjectId() {
		return this.objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	@Column(name = "note", length = 255)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "ip", nullable = false, length = 20)
	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "userId", nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "logTime", nullable = false)
	public Date getLogTime() {
		return this.logTime;
	}

	public void setLogTime(Date logTime) {
		this.logTime = logTime;
	}

	@Transient
	public String getNoteBrief() {
		return StringUtil.brief(note, 20);
	}

	@Transient
	public String getUserName() {
		return ApplicationUtil.getUserService().getUserName(this.userId);
	}

	@Transient
	public String getLogTimeName() {
		return StringUtil.formatDateTime(logTime);
	}
	
	@Transient
	public String getObjectIdBrief() {
		return StringUtil.brief(objectId, 20);
	}

	@Transient
	public String getUserSelectBox() {
		return ApplicationUtil.getUserService().userSelectBox("userId", String.valueOf(this.userId), " -请选择- ");
	}
}