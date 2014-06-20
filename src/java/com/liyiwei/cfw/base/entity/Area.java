/*
 * Created on 2013-12-24 13:58:15
 *
 */
package com.liyiwei.cfw.base.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.liyiwei.cfw.util.ApplicationUtil;
import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Entity
@Table(name = "area")
public class Area implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private String code;
	private Integer type;

	public Area() {

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

	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", nullable = false, length = 12)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "type", nullable = false)
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Transient
	public String getTypeName() {
		return ApplicationUtil.getDictMeaning("area", "type", StringUtil.toString(this.type));
	}

	@Transient
	public String getTypeSelectBox() {
		return ApplicationUtil.dictSelectBox("type", "type", String.valueOf(this.type), " -请选择- ", "area", "type", "");
	}
}