/*
 * Created on 2013-12-10 14:42:00
 *
 */
package com.liyiwei.cfw.system.web;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.liyiwei.common.db.Db;
import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/common/*")
public class SqlToolsController {
	@SuppressWarnings("unchecked")
	@RequestMapping("sqlTools")
	public String sqlTools(HttpServletRequest request, HttpServletResponse response, HttpSession session, ModelMap map) throws Exception {
		String database = "mysql";
		String url = "jdbc:mysql://localhost:3306/cfw?autoReconnect=true&amp;useUnicode=true&amp;characterEncoding=utf-8";
		String user = "root";
		String password = "root";
		String sql = "";

		String opt = StringUtil.toString(request.getParameter("opt"));
		if (StringUtil.isNotEmpty(opt)) {
			database = StringUtil.toString(request.getParameter("database"));
			url = StringUtil.toString(request.getParameter("url"));
			user = StringUtil.toString(request.getParameter("user"));
			password = StringUtil.toString(request.getParameter("password"));
			sql = StringUtil.toString(request.getParameter("sql"));
			String tableName = StringUtil.toString(request.getParameter("tableName"));

			List<String> v = null;
			if (session.getAttribute("v") != null) {
				v = (List<String>) session.getAttribute("v");
			} else {
				v = new ArrayList<String>();
			}
			StringBuffer sb1 = new StringBuffer();
			for (int i = v.size() - 1; i >= 0; i--) {
				String str = (String) v.get(i);
				if (StringUtil.isNotEmpty(str)) {
					String newStr = str.replace('\'', '~');
					newStr = newStr.replace('\"', '^');
					newStr = newStr.replace('\n', '$');
					newStr = newStr.replace('\r', '#');
					sb1.append("<tr><td class='cp' onClick=\"replaceSql('" + newStr + "')\">");
					sb1.append(StringUtil.brief(str, 80));
					sb1.append("</td></tr>");
				}
			}
			map.put("history", sb1.toString());
			
			try {
				StringBuffer sb = new StringBuffer();
				String driver = "com.mysql.jdbc.Driver";
				if (database.equals("oracle")) {
					driver = "oracle.jdbc.driver.OracleDriver";
				}
				Db db = new Db(driver, url, user, password);
				if (opt.equals("exesql") && StringUtil.isNotEmpty(sql)) {
					if (v.contains(sql)) {
						v.remove(sql);
					}
					v.add(sql);
					session.setAttribute("v", v);
					sb.append(exeSql(db, sql));
				} else if (request.getParameter("opt").equals("allTable")) {
					String owner = "";
					if(database.equals("oracle")){
						owner = user.toUpperCase();
					}
					sb.append(getAllTable(db,owner));
				} else if (request.getParameter("opt").equals("struct")) {
					sb.append(getTableStruct(db, tableName));
				} else if (request.getParameter("opt").equals("tableData")) {
					sb.append("<textarea cols=262 rows=17>");
					sb.append(getTableData(db, tableName));
					sb.append("</textarea>");
				} else if (request.getParameter("opt").equals("tableDesc")) {
					sb.append("<textarea cols=262 rows=17>");
					sb.append(getTableDesc(db, tableName,database));
					sb.append("</textarea>");
				} else if (request.getParameter("opt").equals("allTableData")) {
					if (request.getParameterValues("chkId") != null) {
						sb.append("<textarea cols=262 rows=17>");
						String[] tableNames = request.getParameterValues("chkId");
						for (String tablename : tableNames) {
							sb.append(getTableData(db, tablename));
							sb.append("\r\n\r\n");
						}
						sb.append("</textarea>");
					}
				} else if (request.getParameter("opt").equals("allTableDesc")) {
					if (request.getParameterValues("chkId") != null) {
						sb.append("<textarea cols=262 rows=17>");
						String[] tableNames = request.getParameterValues("chkId");
						for (String tablename : tableNames) {
							sb.append(getTableDesc(db, tablename,database));
							sb.append("\r\n\r\n");
						}
						sb.append("</textarea>");
					}
				}
				db.close();
				map.put("content", sb.toString());
			} catch (Exception e) {
				map.put("content", "执行失败！\r\n" + e);
			}
		}

		map.put("database", database);
		map.put("url", url);
		map.put("user", user);
		map.put("password", password);
		map.put("sql", sql);
		return "/common/sqlTools";
	}

	private String exeSql(Db db, String sql) throws Exception {
		StringBuffer sb = new StringBuffer();
		if (sql.toLowerCase().substring(0, 6).equals("select")) {
			int rowCount = db.count(sql);
			ResultSet rs = db.query(sql);

			sb.append("查询结果：共 <span class='red'>").append(rowCount).append("</span> 条记录！\r\n");
			sb.append("<table class=\"table table-bordered table-condensed\">\r\n");
			sb.append("  <tr>\r\n");
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			for (int i = 1; i < colCount + 1; i++) {
				sb.append("    <th>").append(rsmd.getColumnName(i)).append("</th>\r\n");
			}
			sb.append("  </tr>\r\n");

			while (rs.next()) {
				sb.append("  <tr>\r\n");
				for (int i = 1; i < colCount + 1; i++) {
					if (rs.getString(i) != null) {
						sb.append("    <td");
						if (rs.getString(i).length() < 40) {
							sb.append(" nowrap");
						}
						sb.append(">");
						sb.append(rs.getString(i)).append("</td>\r\n");
					} else {
						sb.append("    <td>&nbsp;</td>\r\n");
					}
				}
				sb.append("  </tr>\r\n");
			}
			sb.append("</table>");
			rs.close();
		} else {
			int rowNum = db.execute(sql);
			if (rowNum >= 0) {
				sb.append("共 <span class=\"red\">").append(rowNum).append("</span> 条记录执行完毕！");
			}
		}

		return sb.toString();
	}

	private String getAllTable(Db db,String owner) throws Exception {
		StringBuffer sb = new StringBuffer();
		List<String> list = db.getTableList(owner);
		sb.append("数据库共 <span class='red'>").append(list.size()).append("</span> 个表！\r\n");
		sb.append("<table class=\"table table-bordered table-condensed\">\r\n");
		sb.append("  <tr>\r\n");
		sb.append("    <th class='tc'><input type='checkbox' name='chkAll' onclick='checkBoxSelectAll(\"chkAll\",\"chkId\");'></th>\r\n");
		sb.append("    <th>表名</th>\r\n");
		sb.append("    <th>&nbsp;</th>\r\n");
		sb.append("    <th>&nbsp;</th>\r\n");
		sb.append("    <th>&nbsp;</td>\r\n");
		sb.append("  </tr>\r\n");
		for (String tableName : list) {
			int count = db.count("select count(*) from " + tableName);
			sb.append("  <tr>\r\n");
			sb.append("    <td width='10' class='tc'><input type=checkbox name=chkId value=\"").append(tableName).append("\"></td>\r\n");
			sb.append("    <td><a href='#' onClick=queryIt('exesql','").append(tableName).append("')>").append(tableName).append("<span class='red'>(").append(count).append(")</span></a>")
					.append("</td>\r\n");
			sb.append("    <td class='tc' width='100'><a href='#' onClick=queryIt('struct','").append(tableName).append("')>表结构</a></td>\r\n");
			sb.append("    <td class='tc' width='100'><a href='#' onClick=queryIt('tableDesc','").append(tableName).append("')>表描述</a></td>\r\n");
			sb.append("    <td class='tc' width='100'><a href='#' onClick=queryIt('tableData','").append(tableName).append("')>表数据</a></td>\r\n");
			sb.append("  </tr>\r\n");
		}
		sb.append("</table>\r\n");
		sb.append("<p class='tc'><input type='button' name='allTableDesc' value='生成表描述' onClick=submitIt('allTableDesc')>&nbsp;<input type='button' name='allTableData' value='生成表数据' onClick=submitIt('allTableData')></p>");

		return sb.toString();
	}

	private String getTableStruct(Db db, String tableName) throws Exception {
		StringBuffer sb = new StringBuffer();
		List<Map<String, String>> list = db.getTable(tableName);
		sb.append(tableName).append("表共 <span class='red'>").append(list.size()).append("</span> 个字段！ <a href='#' onClick=queryIt('exesql','").append(tableName).append("')>查看表记录</a>\r\n");
		sb.append("<table class=\"table table-bordered table-condensed\">\r\n");
		sb.append("  <tr>\r\n");
		sb.append("    <th>字段名</th>\r\n");
		sb.append("    <th>类型</th>\r\n");
		sb.append("    <th>长度</th>\r\n");
		sb.append("    <th>精度</th>\r\n");
		sb.append("    <th>允许为空</th>\r\n");
		sb.append("    <th>备注</th>\r\n");
		sb.append("  </tr>\r\n");
		for (Map<String, String> tableMap : list) {
			sb.append("  <tr>\r\n");
			sb.append("    <td>").append(tableMap.get("name")).append("</td>\r\n");
			sb.append("    <td class='tc'>").append(StringUtil.toString(tableMap.get("type").toLowerCase())).append("</td>\r\n");
			sb.append("    <td class='tc'>").append(tableMap.get("precision")).append("</td>\r\n");
			sb.append("    <td class='tc'>").append(StringUtil.toString(tableMap.get("scale"))).append("</td>\r\n");
			sb.append("    <td class='tc'>").append(tableMap.get("null").toLowerCase()).append("</td>\r\n");
			sb.append("    <td>").append(StringUtil.toString(tableMap.get("remarks"))).append("</td>\r\n");
			sb.append("  </tr>\r\n");
		}
		sb.append("</table>\r\n");
		return sb.toString();
	}

	private String getTableData(Db db, String tableName) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append("truncate table ").append(tableName).append(";\r\n");
		String sql = "insert into " + tableName + "(";
		ResultSet rs = db.query("select * from " + tableName);
		ResultSetMetaData rsmd = rs.getMetaData();
		int colCount = rsmd.getColumnCount();
		String[] col = new String[colCount];
		for (int i = 1; i < colCount + 1; i++) {
			col[i - 1] = rsmd.getColumnName(i);
		}
		sql += StringUtil.join(col, ",") + ") values(";

		while (rs.next()) {
			sb.append(sql);
			String[] data = new String[colCount];
			for (int i = 1; i < colCount + 1; i++) {
				if (rs.getString(i) == null) {
					data[i - 1] = "null";
				} else if (rsmd.getColumnTypeName(i).equals("INT") || rsmd.getColumnTypeName(i).equals("DECIMAL")|| rsmd.getColumnTypeName(i).equals("NUMBER")) {
					data[i - 1] = rs.getString(i);
				} else {
					data[i - 1] = "'" + rs.getString(i).replace("'", "''") + "'";
				}
			}
			sb.append(StringUtil.join(data, ","));
			sb.append(");\r\n");
		}
		rs.close();
		return sb.toString();
	}

	private String getTableDesc(Db db, String tableName,String database) throws Exception {
		StringBuffer sb = new StringBuffer();
		if(database.equals("mysql")){
			sb.append("DROP TABLE IF EXISTS ").append(tableName).append(";\r\n");
			sb.append("CREATE TABLE ").append(tableName).append("(\r\n");
		} else if(database.equals("oracle")){
			sb.append("drop table ").append(tableName).append(";\r\n");
			sb.append("create table ").append(tableName).append("(\r\n");
		}
		List<Map<String, String>> list = db.getTable(tableName);
		for (Map<String, String> tableMap : list) {
			sb.append("  ").append(tableMap.get("name"));
			String type = StringUtil.toString(tableMap.get("type")).toLowerCase();
			sb.append(StringUtil.repeat(" ", 20 - tableMap.get("name").length()));
			int len = type.length();
			if (type.equals("varchar")) {
				sb.append("Varchar");
			} else if (type.equals("int")) {
				sb.append("Int");
			} else if (type.equals("datetime")) {
				sb.append("Datetime");
			} else if (type.equals("date")) {
				if(database.equals("mysql")){
					sb.append("Date");
				} else {
					sb.append("DATE");
				}
			} else if (type.equals("decimal")) {
				sb.append("Decimal");
			} else if (type.equals("varchar2")) {
				sb.append("VARCHAR2");
			} else if (type.equals("number")) {
				sb.append("NUMBER");
			}

			if (!type.equals("datetime") && !type.equals("date")) {
				sb.append("(").append(tableMap.get("precision"));
				len += 2 + tableMap.get("precision").length();
				if (type.equals("decimal")) {
					sb.append(",").append(tableMap.get("scale"));
					len += 1 + tableMap.get("scale").length();
				}
				sb.append(")");
			}
			if (tableMap.get("null").equals("NO")) {
				sb.append(StringUtil.repeat(" ", 16 - len));
				if (tableMap.get("name").equals("id")) {
					sb.append("NOT NULL").append(" auto_increment,\r\n");
				} else {
					sb.append("NOT NULL").append(",\r\n");
				}
			} else {
				sb.append(",\r\n");
			}
		}
		if(database.equals("mysql")){
			sb.append("  PRIMARY KEY (id)\r\n");
			sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
		} else if(database.equals("oracle")){
			sb.append("  constraint PK_").append(tableName).append(" primary key (\"ID\")\r\n");
			sb.append(");\r\n");
		}

		return sb.toString();
	}
}