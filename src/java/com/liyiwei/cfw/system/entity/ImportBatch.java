/*
 * Created on 2013-04-27 15:09:15
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
@Table(name = "importBatch")
public class ImportBatch implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String module;
	private String filename;
	private Integer totalCount;
	private Integer finishCount;
	private Integer failCount;
	private Integer importUserId;
	private Date importTime;

	public ImportBatch() {

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

	@Column(name = "module", nullable = false, length = 40)
	public String getModule() {
		return this.module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Column(name = "filename", nullable = false, length = 400)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Column(name = "totalCount")
	public Integer getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	@Column(name = "finishCount")
	public Integer getFinishCount() {
		return this.finishCount;
	}

	public void setFinishCount(Integer finishCount) {
		this.finishCount = finishCount;
	}

	@Column(name = "failCount")
	public Integer getFailCount() {
		return this.failCount;
	}

	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}

	@Column(name = "importUserId", nullable = false)
	public Integer getImportUserId() {
		return this.importUserId;
	}

	public void setImportUserId(Integer importUserId) {
		this.importUserId = importUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "importTime", nullable = false)
	public Date getImportTime() {
		return this.importTime;
	}

	public void setImportTime(Date importTime) {
		this.importTime = importTime;
	}

	@Transient
	public String getImportUserName() {
		return ApplicationUtil.getUserService().getUserName(this.importUserId);
	}

	@Transient
	public String getFilenameBrief() {
		return StringUtil.brief(filename, 30);
	}

	@Transient
	public String getImportTimeName() {
		return StringUtil.formatDateTime(this.importTime);
	}

	@Transient
	public String getImportUserSelectBox() {
		return ApplicationUtil.getUserService().userSelectBox("importUserId", String.valueOf(this.importUserId), " -请选择- ");
	}
}