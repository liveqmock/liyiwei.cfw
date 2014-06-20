/*
 * Created on 2013-04-17 16:59:03
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

/**
 * @author Liyiwei
 * 
 */
@Entity
@Table(name = "category")
public class Category implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Integer seq;
	private Integer parentId;

	public Category() {

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

	@Column(name = "name", nullable = false, length = 40)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "seq")
	public Integer getSeq() {
		return seq;
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
		return ApplicationUtil.getCategoryService().getCategoryName(this.parentId);
	}

	@Transient
	public String getParentSelectBox() {
		return ApplicationUtil.getCategoryService().categorySelectBox("parentId", String.valueOf(this.parentId), " -请选择- ", 1);
	}
}