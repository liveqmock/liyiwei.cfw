/*
 * Created on 2013-03-15 14:46:03
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

import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Entity
@Table(name = "attachment")
public class Attachment implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String filename;
	private String initFilename;
	private String code;
	private Integer size;
	private String path;
	private Integer uploadUserId;
	private Date uploadTime;

	public Attachment() {

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

	@Column(name = "filename", nullable = false, length = 100)
	public String getFilename() {
		return this.filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	@Column(name = "initFilename", nullable = false, length = 100)
	public String getInitFilename() {
		return this.initFilename;
	}

	public void setInitFilename(String initFilename) {
		this.initFilename = initFilename;
	}

	@Column(name = "code", nullable = false, length = 60)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "size", nullable = false)
	public Integer getSize() {
		return this.size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@Column(name = "path", nullable = false, length = 100)
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name = "uploadUserId", nullable = false)
	public Integer getUploadUserId() {
		return this.uploadUserId;
	}

	public void setUploadUserId(Integer uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "uploadTime", nullable = false)
	public Date getUploadTime() {
		return this.uploadTime;
	}

	public void setUploadTime(Date uploadTime) {
		this.uploadTime = uploadTime;
	}

	@Transient
	public String getUploadTimeName() {
		return StringUtil.formatDateTime(uploadTime);
	}
}