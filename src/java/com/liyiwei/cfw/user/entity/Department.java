/*
 * Created on 2013-01-09 17:34:17
 *
 */
package com.liyiwei.cfw.user.entity;

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
@Table(name = "department")
public class Department implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String code;
	private String note;
	private Integer seq;
	private Integer parentId;

	public Department() {

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

	@Column(name = "name", nullable = false, length = 60)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "note", length = 100)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "seq")
	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	@Column(name = "parentId")
	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Transient
	public String getParentName() {
		return ApplicationUtil.getDepartmentService().getDepartmentName(this.parentId);
	}

	@Transient
	public String getParentSelectBox() {
		return ApplicationUtil.getDepartmentService().departmentSelectBox("parentId", String.valueOf(this.parentId), " -请选择- ");
	}
}