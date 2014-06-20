/*
 * Created on 2012-04-18 15:37:00
 *
 */
package com.liyiwei.cfw.util;

import java.util.List;

import com.liyiwei.common.util.StringUtil;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * @author Liyiwei
 * 
 */
public class DictMeaning implements TemplateMethodModel {
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		String tableName = (String) args.get(0);
		String fieldName = (String) args.get(1);
		int value = StringUtil.toInt((String) args.get(2));
		String defaultValue = (String) args.get(3);

		return ApplicationUtil.getDictMeaning(tableName, fieldName, value, defaultValue);
	}
}