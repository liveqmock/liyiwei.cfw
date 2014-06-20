/*
 * Created on 2013-03-13 16:32:35
 *
 */
package com.liyiwei.cms.article.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.util.BaseDao;
import com.liyiwei.cms.article.entity.Article;

/**
 * @author Liyiwei
 * 
 */
@Transactional(readOnly = true)
public class BaseArticleService {
	protected BaseDao<Article> articleDao;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		articleDao = new BaseDao<Article>(sessionFactory, Article.class);
	}

	public List<Article> getArticleList() {
		return articleDao.findAll();
	}

	public List<Article> getArticlePageList(int currentPage, int pageSize) {
		String hql = "from Article article order by article.id";
		return articleDao.find(hql, currentPage, pageSize);
	}

	public List<Article> getArticleList(String condition, Object... values) {
		String hql = "from Article article " + condition;
		return articleDao.find(hql, values);
	}

	public List<Article> getArticlePageList(String condition, int currentPage, int pageSize, Object... values) {
		String hql = "from Article article " + condition;
		return articleDao.find(hql, currentPage, pageSize, values);
	}
	
	public int getArticleQueryCount(String condition, List<Object> list) {
		String hql = "select count(article.id) from Article article " + condition;
		return articleDao.findQueryInt(hql, list);
	}
	
	public List<Article> getArticleQueryList(String condition, int currentPage, int pageSize, List<Object> list) {
		String hql = "from Article article " + condition;
		return articleDao.query(hql, currentPage, pageSize, list);
	}

	public int getArticleCount() {
		String hql = "select count(article.id) from Article article";
		return articleDao.findLong(hql).intValue();
	}

	public int getArticleCount(String condition, Object... values) {
		String hql = "select count(article.id) from Article article " + condition;
		return articleDao.findLong(hql, values).intValue();
	}

	public Article getArticle(int id) {
		return articleDao.get(id);
	}

	public Article getArticle(String condition, Object... values) {
		String hql = "from Article article " + condition;
		List<Article> list = articleDao.find(hql, values);
		if (list != null && list.size() > 0) {
			return (Article) list.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void addArticle(Article article) {
		articleDao.save(article);
	}

	@Transactional(readOnly = false)
	public void modifyArticle(Article article) {
		articleDao.saveOrUpdate(article);
	}

	@Transactional(readOnly = false)
	public void modifyArticle(String condition, Object... values) {
		String hql = "update Article article set " + condition;
		articleDao.batchExecute(hql, values);
	}

	@Transactional(readOnly = false)
	public void modifyArticle(int id, String field, String value) {
		this.modifyArticle(" article." + field + "=? where article.id=?", value, id);
	}

	@Transactional(readOnly = false)
	public void deleteArticle(int id) {
		articleDao.delete(id);
	}

	@Transactional(readOnly = false)
	public void deleteArticle(String condition, Object... values) {
		String hql = "delete from Article article " + condition;
		articleDao.batchExecute(hql, values);
	}

	@Transactional(readOnly = false)
	public void deleteArticleByIds(String ids) {
		String hql = "delete from Article article where article.id in(" + ids + ")";
		articleDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void execute(String hql, Object... values) {
		articleDao.batchExecute(hql, values);
	}
}