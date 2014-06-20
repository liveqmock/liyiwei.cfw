/*
 * Created on 2012-04-18 15:11:00
 *
 */
package com.liyiwei.cfw.util;

import java.util.List;

import com.liyiwei.common.web.Html;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

/**
 * @author Liyiwei
 * 
 */
public class SortCol implements TemplateMethodModel {
	@SuppressWarnings("rawtypes")
	public Object exec(List args) throws TemplateModelException {
		String col = (String) args.get(0);
		String colName = (String) args.get(1);
		String orderby = (String) args.get(2);
		String descflag = (String) args.get(3);

		return Html.colSort(col, colName, orderby, descflag);
	}
}