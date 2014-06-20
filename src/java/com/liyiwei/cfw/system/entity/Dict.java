/*
 * Created on 2013-02-06 16:45:33
 *
 */
package com.liyiwei.cfw.system.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Entity
@Table(name = "dict")
public class Dict implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String tableName;
	private String fieldName;
	private String value;
	private String meaning;
	private String note;

	public Dict() {

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

	@Column(name = "tableName", nullable = false, length = 40)
	public String getTableName() {
		return this.tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "fieldName", nullable = false, length = 40)
	public String getFieldName() {
		return this.fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Column(name = "value", nullable = false, length = 40)
	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "meaning", nullable = false, length = 40)
	public String getMeaning() {
		return this.meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	@Column(name = "note", length = 255)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Transient
	public String getNoteBrief() {
		return StringUtil.brief(note, 20);
	}
}