/*
 * Created on 2013-03-13 16:21:33
 *
 */
package com.liyiwei.cms.article.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.util.BaseDao;
import com.liyiwei.cms.article.entity.Channel;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseChannelService {
	protected BaseDao<Channel> channelDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		channelDao = new BaseDao<Channel>(sessionFactory, Channel.class);
	}

	public List<Channel> getChannelList() {
		return channelDao.findAll();
	}

	public List<Channel> getChannelList(int currentPage, int pageSize) {
		String hql = "from Channel channel order by channel.id";
		return channelDao.find(hql, currentPage, pageSize);
	}

	public List<Channel> getChannelList(String condition) {
		String hql = "from Channel channel " + condition;
		return channelDao.find(hql);
	}

	public List<Channel> getChannelList(String condition, int currentPage, int pageSize) {
		String hql = "from Channel channel " + condition;
		return channelDao.find(hql, currentPage, pageSize);
	}

	public int getChannelCount() {
		String hql = "select count(channel.id) from Channel channel";
		return channelDao.findLong(hql).intValue();
	}

	public int getChannelCount(String condition) {
		String hql = "select count(channel.id) from Channel channel " + condition;
		return channelDao.findLong(hql).intValue();
	}

	public Channel getChannel(int id) {
		return channelDao.get(id);
	}

	public Channel getChannel(String condition) {
		String hql = "from Channel channel " + condition;
		List<Channel> list = channelDao.find(hql);
		if (list != null && list.size() > 0) {
			return (Channel) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addChannel(Channel channel) {
		channelDao.save(channel);
	}

	@Transactional(readOnly = false)
	public void modifyChannel(Channel channel) {
		channelDao.saveOrUpdate(channel);
	}

	@Transactional(readOnly = false)
	public void modifyChannel(String condition) {
		String hql = "update Channel channel set " + condition;
		channelDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void modifyChannel(int id, String field, String value) {
		this.modifyChannel(" channel." + field + "='" + value + "' where channel.id=" + id);
	}

	@Transactional(readOnly = false)
	public void deleteChannel(int id) {
		channelDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteChannel(String condition) {
		String hql = "delete from Channel channel " + condition;
		channelDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteChannelByIds(String ids) {
		String hql = "delete from Channel channel where channel.id in(" + ids + ")";
		channelDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql) {
		channelDao.batchExecute(hql);
	}
}