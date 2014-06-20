/*
 * Created on 2013-04-27 15:09:47
 *
 */
package com.liyiwei.cfw.system.entity;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Liyiwei
 * 
 */
@Entity
@Table(name = "importLog")
public class ImportLog implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer importBatchId;
	private String module;
	private Integer objectId;

	public ImportLog() {

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

	@Column(name = "importBatchId", nullable = false)
	public Integer getImportBatchId() {
		return this.importBatchId;
	}

	public void setImportBatchId(Integer importBatchId) {
		this.importBatchId = importBatchId;
	}

	@Column(name = "module", nullable = false, length = 40)
	public String getModule() {
		return this.module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Column(name = "objectId", nullable = false)
	public Integer getObjectId() {
		return this.objectId;
	}

	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}
}