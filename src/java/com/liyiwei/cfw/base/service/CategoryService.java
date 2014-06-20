/*
 * Created on 2013-04-17 16:59:03
 *
 */
package com.liyiwei.cfw.base.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.common.cache.Cache;
import com.liyiwei.common.web.Html;
import com.liyiwei.cfw.base.entity.Category;

/**
 * @author Liyiwei
 * 
 */
@Service
public class CategoryService extends BaseCategoryService {
	public int getCategoryIdByName(String name) {
		Category category = getCategory(" where category.name ='" + name + "'");
		return category == null ? 0 : category.getId();
	}

	public boolean existChildCategory(int parentId) {
		int count = super.getCategoryCount(" where category.parentId =" + parentId);
		return count > 0 ? true : false;
	}

	public boolean existChildCategory(String parentIds) {
		int count = super.getCategoryCount(" where category.parentId in(" + parentIds + ")");
		return count > 0 ? true : false;
	}

	@Transactional(readOnly = false)
	public void upCategorySeq(int parentId, int sno) {
		List<Category> list = getCategoryList("where  category.parentId=" + parentId + " order by  category.seq,category.id");
		int i = 1;
		for (Category category : list) {
			if (i == sno - 1) {
				category.setSeq(i + 1);
			} else if (i == sno) {
				category.setSeq(i - 1);
			} else {
				category.setSeq(i);
			}
			super.modifyCategory(category);
			i++;
		}
		Cache.removeStartsWith("Category");
	}

	@Transactional(readOnly = false)
	public void downCategorySeq(int parentId, int sno) {
		List<Category> list = getCategoryList("where  category.parentId=" + parentId + " order by  category.seq,category.id");
		int i = 1;
		for (Category category : list) {
			if (i == sno) {
				category.setSeq(i + 1);
			} else if (i == sno + 1) {
				category.setSeq(i - 1);
			} else {
				category.setSeq(i);
			}
			super.modifyCategory(category);
			i++;
		}
		Cache.removeStartsWith("Category");
	}

	public String getCategoryName(Integer id) {
		if (id == null) {
			return "";
		}

		Category category = getCategory(id.intValue());
		return category == null ? "" : category.getName();
	}

	public String categorySelectBox(String selectBoxName, String selectData, int categoryId) {
		return categorySelectBox(selectBoxName, selectBoxName, selectData, "", "", categoryId);
	}

	public String categorySelectBox(String selectBoxName, String selectData, String message, int categoryId) {
		return categorySelectBox(selectBoxName, selectBoxName, selectData, message, "", categoryId);
	}

	public String categorySelectBox(String selectBoxName, String selectBoxId, String selectData, String message, String other, int categoryId) {
		List<Category> list = getCategoryListByCategoryId(categoryId);
		if (list != null && list.size() > 0) {
			String[][] paras = new String[list.size() + 1][2];
			paras[0][0] = "";
			paras[0][1] = message;

			int i = 1;
			for (Category category : list) {
				paras[i][0] = String.valueOf(category.getId());
				paras[i][1] = category.getName();
				i++;
			}
			return Html.selectBox(selectBoxName, selectBoxId, paras, selectData, other);
		} else {
			String[][] paras = new String[1][2];
			paras[0][0] = "";
			paras[0][1] = message;
			return Html.selectBox(selectBoxName, selectBoxId, paras, selectData, other);
		}
	}

	@Override
	public Category getCategory(int id) {
		return getCategoryByCache(id);
	}

	public List<Category> getCategoryListByCategoryId(int categoryId) {
		return getCategoryListByCache(categoryId);
	}

	@SuppressWarnings("unchecked")
	private Category getCategoryByCache(int id) {
		HashMap<Integer, Category> hashMap = (HashMap<Integer, Category>) Cache.get("Category");
		if (hashMap == null) {
			hashMap = new HashMap<Integer, Category>();
			List<Category> list = super.getCategoryList();
			for (Category category : list) {
				hashMap.put(category.getId(), category);
			}
			Cache.put("Category", hashMap);
			Cache.put("CategoryList", list);
		}

		return (Category) hashMap.get(id);
	}

	@SuppressWarnings("unchecked")
	private List<Category> getCategoryListByCache(int categoryId) {
		List<Category> list = (List<Category>) Cache.get("CategoryList" + categoryId);
		if (list == null) {
			list = super.getCategoryList(" where parentId=" + categoryId + "order by category.seq,category.id");
			Cache.put("CategoryList" + categoryId, list);
		}

		return list;
	}

	@Override
	@Transactional(readOnly = false)
	public void addCategory(Category category) {
		super.addCategory(category);
		Cache.removeStartsWith("Category");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyCategory(Category category) {
		super.modifyCategory(category);
		Cache.removeStartsWith("Category");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyCategory(String condition) {
		super.modifyCategory(condition);
		Cache.removeStartsWith("Category");
	}

	@Override
	@Transactional(readOnly = false)
	public void modifyCategory(int id, String field, String value) {
		super.modifyCategory(id, field, value);
		Cache.removeStartsWith("Category");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteCategory(int id) {
		super.deleteCategory(id);
		Cache.removeStartsWith("Category");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteCategory(String condition) {
		super.deleteCategory(condition);
		Cache.removeStartsWith("Category");
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteCategoryByIds(String ids) {
		super.deleteCategoryByIds(ids);
		Cache.removeStartsWith("Category");
	}

	@Override
	@Transactional(readOnly = false)
	public void execute(String hql) {
		super.execute(hql);
		Cache.removeStartsWith("Category");
	}
}