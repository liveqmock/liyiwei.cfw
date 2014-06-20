/*
 * Created on 2013-03-13 16:32:35
 *
 */
package com.liyiwei.cms.article.entity;

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

import com.liyiwei.cms.util.ApplicationUtil;
import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Entity
@Table(name = "article")
public class Article implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer channelId;
	private String title;
	private String subTitle;
	private String brief;
	private String smallPic;
	private String content;
	private String author;
	private String tag;
	private String source;
	private Integer status;
	private Integer viewCount;
	private Integer approveStatus;
	private Integer approveUserId;
	private Date approveTime;
	private String approveNote;
	private Integer createUserId;
	private Date createTime;
	private Integer modifyUserId;
	private Date modifyTime;

	public Article() {

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

	@Column(name = "channelId", nullable = false)
	public Integer getChannelId() {
		return this.channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	@Column(name = "title", nullable = false, length = 120)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "subTitle", length = 120)
	public String getSubTitle() {
		return this.subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	@Column(name = "brief", length = 1000)
	public String getBrief() {
		return this.brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	@Column(name = "smallPic", length = 80)
	public String getSmallPic() {
		return this.smallPic;
	}

	public void setSmallPic(String smallPic) {
		this.smallPic = smallPic;
	}

	@Column(name = "content", length = 6000)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "author", length = 40)
	public String getAuthor() {
		return this.author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	@Column(name = "tag", length = 60)
	public String getTag() {
		return this.tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Column(name = "source", length = 60)
	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Column(name = "status", nullable = false)
	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Column(name = "viewCount", nullable = false)
	public Integer getViewCount() {
		return viewCount;
	}

	public void setViewCount(Integer viewCount) {
		this.viewCount = viewCount;
	}

	@Column(name = "approveStatus", nullable = false)
	public Integer getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(Integer approveStatus) {
		this.approveStatus = approveStatus;
	}

	@Column(name = "approveUserId")
	public Integer getApproveUserId() {
		return approveUserId;
	}

	public void setApproveUserId(Integer approveUserId) {
		this.approveUserId = approveUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "approveTime")
	public Date getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(Date approveTime) {
		this.approveTime = approveTime;
	}

	@Column(name = "approveNote", length = 400)
	public String getApproveNote() {
		return approveNote;
	}

	public void setApproveNote(String approveNote) {
		this.approveNote = approveNote;
	}

	@Column(name = "createUserId", nullable = false)
	public Integer getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createTime", nullable = false)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "modifyUserId", nullable = false)
	public Integer getModifyUserId() {
		return this.modifyUserId;
	}

	public void setModifyUserId(Integer modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modifyTime", nullable = false)
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Transient
	public String getChannelName() {
		return ApplicationUtil.getChannelService().getChannelName(this.channelId);
	}

	@Transient
	public String getTitleBrief() {
		return StringUtil.brief(title, 18);
	}

	@Transient
	public String getBriefBrief() {
		return StringUtil.brief(brief, 100);
	}

	@Transient
	public String getContentBrief() {
		return StringUtil.brief(content, 100);
	}

	@Transient
	public String getStatusName() {
		return ApplicationUtil.getDictMeaning("article", "status", StringUtil.toInt(this.status), "");
	}

	@Transient
	public String getApproveStatusName() {
		return ApplicationUtil.getDictMeaning("article", "approveStatus", StringUtil.toInt(this.approveStatus), "");
	}

	@Transient
	public String getApproveUserName() {
		return ApplicationUtil.getUserService().getUserName(this.approveUserId);
	}

	@Transient
	public String getCreateUserName() {
		return ApplicationUtil.getUserService().getUserName(this.createUserId);
	}

	@Transient
	public String getApproveTimeName() {
		return StringUtil.formatDateTime(approveTime);
	}

	@Transient
	public String getCreateTimeName() {
		return StringUtil.formatDateTime(createTime);
	}

	@Transient
	public String getModifyUserName() {
		return ApplicationUtil.getUserService().getUserName(this.modifyUserId);
	}

	@Transient
	public String getModifyTimeName() {
		return StringUtil.formatDateTime(this.modifyTime);
	}

	@Transient
	public String getModifyTimeDateName() {
		return StringUtil.formatDate(this.modifyTime);
	}

	@Transient
	public String getChannelSelectBox() {
		return ApplicationUtil.getChannelService().channelSelectBox("channelId", String.valueOf(this.channelId), " -请选择- ");
	}

	@Transient
	public String getStatusSelectBox() {
		return ApplicationUtil.dictSelectBox("status", "status", String.valueOf(this.status), " -请选择- ", "article", "status", "");
	}

	@Transient
	public String getApproveUserSelectBox() {
		return ApplicationUtil.getUserService().userSelectBox("approveUserId", String.valueOf(this.approveUserId), " -请选择- ");
	}

	@Transient
	public String getCreateUserSelectBox() {
		return ApplicationUtil.getUserService().userSelectBox("createUserId", String.valueOf(this.createUserId), " -请选择- ");
	}

	@Transient
	public String getModifyUserSelectBox() {
		return ApplicationUtil.getUserService().userSelectBox("modifyUserId", String.valueOf(this.modifyUserId), " -请选择- ");
	}
}