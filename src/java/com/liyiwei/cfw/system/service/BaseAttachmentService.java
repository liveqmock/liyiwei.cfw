/*
 * Created on 2013-03-15 14:46:03
 *
 */
package com.liyiwei.cfw.system.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.util.BaseDao;
import com.liyiwei.cfw.system.entity.Attachment;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseAttachmentService {
	protected BaseDao<Attachment> attachmentDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		attachmentDao = new BaseDao<Attachment>(sessionFactory, Attachment.class);
	}

	public List<Attachment> getAttachmentList() {
		return attachmentDao.findAll();
	}

	public List<Attachment> getAttachmentList(int currentPage, int pageSize) {
		String hql = "from Attachment attachment order by attachment.id";
		return attachmentDao.find(hql, currentPage, pageSize);
	}

	public List<Attachment> getAttachmentList(String condition) {
		String hql = "from Attachment attachment " + condition;
		return attachmentDao.find(hql);
	}

	public List<Attachment> getAttachmentList(String condition, int currentPage, int pageSize) {
		String hql = "from Attachment attachment " + condition;
		return attachmentDao.find(hql, currentPage, pageSize);
	}

	public int getAttachmentCount() {
		String hql = "select count(attachment.id) from Attachment attachment";
		return attachmentDao.findLong(hql).intValue();
	}

	public int getAttachmentCount(String condition) {
		String hql = "select count(attachment.id) from Attachment attachment " + condition;
		return attachmentDao.findLong(hql).intValue();
	}

	public Attachment getAttachment(int id) {
		return attachmentDao.get(id);
	}

	public Attachment getAttachment(String condition) {
		String hql = "from Attachment attachment " + condition;
		List<Attachment> list = attachmentDao.find(hql);
		if (list != null && list.size() > 0) {
			return (Attachment) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addAttachment(Attachment attachment) {
		attachmentDao.save(attachment);
	}

	@Transactional(readOnly = false)
	public void modifyAttachment(Attachment attachment) {
		attachmentDao.saveOrUpdate(attachment);
	}

	@Transactional(readOnly = false)
	public void modifyAttachment(String condition) {
		String hql = "update Attachment attachment set " + condition;
		attachmentDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void modifyAttachment(int id, String field, String value) {
		this.modifyAttachment(" attachment." + field + "='" + value + "' where attachment.id=" + id);
	}

	@Transactional(readOnly = false)
	public void deleteAttachment(int id) {
		attachmentDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteAttachment(String condition) {
		String hql = "delete from Attachment attachment " + condition;
		attachmentDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteAttachmentByIds(String ids) {
		String hql = "delete from Attachment attachment where attachment.id in(" + ids + ")";
		attachmentDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		attachmentDao.batchExecute(hql);
	}
}