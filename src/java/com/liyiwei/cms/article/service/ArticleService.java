/*
 * Created on 2013-03-13 19:01:00
 *
 */
package com.liyiwei.cms.article.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cms.article.entity.Article;
import com.liyiwei.common.util.RandomUtil;

/**
 * @author Liyiwei
 * 
 */
@Service
public class ArticleService extends BaseArticleService {
	public List<Article> getArticleListByChannel(Integer channelId, int num) {
		if (channelId != null) {
			return getArticlePageList(" where article.channelId=? and article.status=1 and article.approveStatus=1 order by article.modifyTime desc", 1, num, channelId);
		} else {
			return getArticlePageList(" order by article.modifyTime desc", 1, num);
		}
	}

	public List<Article> getArticleListByChannel(Integer channelId, int currentPage, int pageSize) {
		if (channelId != null) {
			return getArticlePageList(" where article.channelId=? and article.status=1 and article.approveStatus=1 order by article.modifyTime desc", currentPage, pageSize, channelId);
		} else {
			return getArticlePageList(currentPage, pageSize);
		}
	}

	public int getArticleCountByChannel(int channelId) {
		return getArticleCount(" where article.channelId=? and article.status=1 and article.approveStatus=1", channelId);
	}

	public List<Article> getHotArticleList(int channelId, int num) {
		return getArticlePageList(" where article.channelId=? and article.status=1 and article.approveStatus=1 order by article.viewCount desc", 1, num, channelId);
	}

	public List<Article> getRelationArticleList(int id, int channelId, int num) {
		List<Article> list = super.getArticleList(" where article.channelId=? and article.status=1 and article.approveStatus=1 and article.id<>?", channelId, id);
		if (list == null || list.size() <= num) {
			return list;
		} else {
			List<Article> relationList = new ArrayList<Article>();
			int[] result = RandomUtil.getRandomArray(list.size(), num);
			for (int i = 0; i < result.length; i++) {
				relationList.add(list.get(result[i]));
			}
			return relationList;
		}
	}

	public boolean existArticleByChannelIds(String channelIds) {
		int count = super.getArticleCount(" where article.channelId in(" + channelIds + ")");
		return count > 0 ? true : false;
	}

	@Transactional(readOnly = false)
	public void modifyArticleStatus(int id, int status) {
		String hql = "update from Article article set article.status=? where article.id=?";
		super.execute(hql, status, id);
	}

	@Transactional(readOnly = false)
	public void addViewCount(int id) {
		String hql = "update from Article article set article.viewCount=article.viewCount+1 where article.id=?";
		super.execute(hql, id);
	}

	@Transactional(readOnly = false)
	public void approveArticleByIds(String ids, int approveStatus, String approveNote, int approveUserId) {
		String hql = "update from Article article set article.approveStatus=?,article.approveNote=?,article.approveUserId=?,article.approveTime=now() where article.id in(" + ids + ")";
		articleDao.batchExecute(hql, approveStatus, approveNote, approveUserId);
	}
}