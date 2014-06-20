/*
 * Created on 2013-12-04 14:58:00
 *
 */
package com.liyiwei.cfw.system.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/common/*")
public class FileViewController {
	@RequestMapping("fileView")
	public String fileView(HttpServletRequest request, ModelMap map) throws Exception {
		String url = StringUtil.toString(request.getParameter("url"));
		if(StringUtil.isEmpty(url)){
			url = request.getSession().getServletContext().getRealPath(File.separator);
		}

		File file = new File(url);
		File parentFile = file.getParentFile();
		if (parentFile != null) {
			map.put("parentPath", parentFile.getPath().replace("\\","\\\\"));
		}
		map.put("path", file.getPath());
		if (file.isDirectory()) {
			List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			map.put("isDirectory", "1");
			File[] files = file.listFiles();
			map.put("count", files.length);
			for (File fileItem : files) {
				Map<String, String> fileMap = new HashMap<String, String>();
				fileMap.put("path", fileItem.getPath().replace("\\","\\\\"));
				if (fileItem.isDirectory()) {
					fileMap.put("name", "[" + fileItem.getName() + "]");
					fileMap.put("type", "目录");
					fileMap.put("size", "");
				} else {
					fileMap.put("name", fileItem.getName());
					fileMap.put("type", "文件");
					fileMap.put("size", fileItem.length() / 1024 + " KB");
				}
				fileMap.put("modifyTime", StringUtil.formatDateTime1(new Date(fileItem.lastModified())));
				list.add(fileMap);
			}
			map.put("fileList", list);
		} else {
			map.put("isDirectory", "0");
			StringBuffer sb = new StringBuffer();
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(new String(line.getBytes(), "utf-8").replace("<", "&lt;") + "<br/>");
			}
			br.close();
			map.put("content", sb.toString());
		}

		return "/common/fileView";
	}
}