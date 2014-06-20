/*
 * Created on 2014-04-10 15:25:29
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
@Table(name = "license")
public class License implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private Date registerDate;
	private Integer permitDays;
	private String mac;
	private String licenseCode;

	public License() {

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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "registerDate", nullable = false)
	public Date getRegisterDate() {
		return this.registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	@Column(name = "permitDays", nullable = false)
	public Integer getPermitDays() {
		return this.permitDays;
	}

	public void setPermitDays(Integer permitDays) {
		this.permitDays = permitDays;
	}

	@Column(name = "mac", nullable = false, length = 40)
	public String getMac() {
		return this.mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	@Column(name = "licenseCode", nullable = false, length = 255)
	public String getLicenseCode() {
		return this.licenseCode;
	}

	public void setLicenseCode(String licenseCode) {
		this.licenseCode = licenseCode;
	}

	@Transient
	public String getRegisterDateName() {
		return StringUtil.formatDate(this.registerDate);
	}
}