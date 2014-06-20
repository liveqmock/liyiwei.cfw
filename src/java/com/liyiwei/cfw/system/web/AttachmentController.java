/*
 * Created on 2013-03-15 14:46:03
 *
 */
package com.liyiwei.cfw.system.web;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.liyiwei.cfw.system.entity.Attachment;
import com.liyiwei.cfw.system.service.AttachmentService;
import com.liyiwei.cfw.system.service.SystemSettingService;
import com.liyiwei.cfw.util.BaseController;
import com.liyiwei.common.util.DateUtil;
import com.liyiwei.common.util.FileUtil;
import com.liyiwei.common.util.JsonUtil;
import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Controller
@RequestMapping("/system/*")
public class AttachmentController extends BaseController {
	@Autowired
	private AttachmentService attachmentService;
	@Autowired
	private SystemSettingService systemSettingService;
	private String rootPath;
	private String pathRule;
	private int maxFileSize;

	@ModelAttribute("getSystemSetting")
	public void getSystemSetting() {
		rootPath = systemSettingService.getSystemSetting("uploadRootPath", "upload");
		maxFileSize = StringUtil.toInt(systemSettingService.getSystemSetting("maxFileSize", "20"));
		pathRule = systemSettingService.getSystemSetting("pathRule", "");
		realPath = httpServletRequest.getSession().getServletContext().getRealPath(File.separator);
		System.out.println(realPath);
		rootPath = realPath + File.separator + rootPath;
	}

	@RequestMapping("attachmentList")
	public void attachmentList(String code, HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<Attachment> attachmentList = attachmentService.getAttachmentListByCode(code);
		outputJson(response, JsonUtil.beanToJson(attachmentList));
	}

	@RequestMapping("attachmentListCount")
	public void attachmentListCount(String code, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int count = attachmentService.getAttachmentCountByCode(code);
		outputJson(response, JsonUtil.toJson("success", true, "msg", StringUtil.toString(count)));
	}

	@RequestMapping("attachmentDelete")
	public void attachmentDelete(int id, String code, HttpServletResponse response) throws Exception {
		attachmentService.deleteAttachment(id, rootPath);
		String queryString = " where attachment.code='" + code + "' order by attachment.uploadTime";
		List<Attachment> attachmentList = attachmentService.getAttachmentList(queryString);
		outputJson(response, JsonUtil.beanToJson(attachmentList));
	}

	@RequestMapping("attachmentDeleteByCode")
	public void attachmentDeleteByCode(String code, HttpServletResponse response) throws Exception {
		attachmentService.deleteAttachmentByCode(code, rootPath);
		outputJson(response, JsonUtil.toJson("success", true, "msg", "删除成功!"));
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("upload")
	public void upload(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String code = StringUtil.notNull(request.getParameter("code"));
		String exts = StringUtil.notNull(request.getParameter("exts"));
		String thumbnails = StringUtil.notNull(request.getParameter("thumbnails"));
		int rename = StringUtil.toInt(request.getParameter("rename"), 1);
		if (StringUtil.isNotEmpty(request.getParameter("path"))) {
			pathRule = StringUtil.notNull(request.getParameter("path"));
		}

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart == true) {
			try {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("utf-8");
				upload.setFileSizeMax(1024 * 1024 * maxFileSize);

				List<FileItem> fileItems = upload.parseRequest(request);
				for (FileItem item : fileItems) {
					if (!item.isFormField()) {
						String fileName = item.getName();
						long size = item.getSize();
						if (fileName != null) {
							File file = new File(item.getName());
							if (file.exists()) {
								fileName = file.getName();
							}
							if (StringUtil.isNotEmpty(exts)) {
								String ext = FileUtil.getFileExt(fileName);
								if (ext.equals("") || !("," + exts + ",").contains(("," + ext + ","))) {
									outputJson(response, JsonUtil.toJson("err", "文件名后缀必须是" + exts + "!", "msg", ""));
									return;
								}
							}
							
							
							String uploadPath = getUploadPath();
							String path = rootPath + uploadPath.replace("/", File.separator);
							File dir = new File(path);
							if (!dir.exists()) {
								dir.mkdirs();
							}
							String newFileName = fileName;
							if (rename == 1) {
								newFileName = FileUtil.getMd5Filename(fileName, StringUtil.toString(System.currentTimeMillis()));
							}
							File newFile = new File(path, newFileName);
							item.write(newFile);

							if (thumbnails.equals("1")) {
								Thumbnails.of(newFile).size(100, 100).keepAspectRatio(false).toFile(path + FileUtil.setFileName(newFile.getName(), "_100_100"));
							}
							Attachment attachment = new Attachment();
							attachment.setFilename(newFile.getName());
							attachment.setCode(code);
							attachment.setInitFilename(fileName);
							attachment.setPath(uploadPath);
							attachment.setSize((int) size);
							attachment.setUploadUserId(_userId);
							attachment.setUploadTime(new Date());
							attachmentService.addAttachment(attachment);

							String queryString = " where attachment.code='" + code + "' order by attachment.uploadTime";
							List<Attachment> attachmentList = attachmentService.getAttachmentList(queryString);
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("err", "");
							map.put("msg", attachmentList);
							outputJson(response, JsonUtil.beanToJson(map));
							return;
						}
					}
				}
			} catch (FileUploadException e) {
				outputJson(response, JsonUtil.toJson("err", "上传失败!最大上传文件大小为" + maxFileSize + "M", "msg", ""));
				return;
			}
		} else {
			System.out.println("the enctype must be multipart/form-data");
			outputJson(response, JsonUtil.toJson("err", "上传失败!the enctype must be multipart/form-data!", "msg", ""));
			return;
		}
		outputJson(response, JsonUtil.toJson("err", "上传失败!", "msg", ""));
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("uploadImage")
	public void uploadImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (request.getContentType().equals("application/octet-stream")) {
			try {
				String dispoString = request.getHeader("Content-Disposition");
				int iFindStart = dispoString.indexOf("name=\"") + 6;
				int iFindEnd = dispoString.indexOf("\"", iFindStart);
				iFindStart = dispoString.indexOf("filename=\"") + 10;
				iFindEnd = dispoString.indexOf("\"", iFindStart);
				String sFileName = dispoString.substring(iFindStart, iFindEnd);

				int i = request.getContentLength();
				byte buffer[] = new byte[i];
				int j = 0;
				while (j < i) {
					int k = request.getInputStream().read(buffer, j, i - j);
					j += k;
				}

				if (buffer.length == 0) {
					outputJson(response, JsonUtil.toJson("err", "上传文件不能为空!", "msg", ""));
					return;
				}

				String uploadPath = getUploadPath();
				String path = rootPath + uploadPath.replace("/", File.separator);
				File dir = new File(path);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				String filename = FileUtil.getMd5Filename(sFileName, StringUtil.toString(System.currentTimeMillis()));
				OutputStream out = new BufferedOutputStream(new FileOutputStream(path + filename, true));
				out.write(buffer);
				out.close();
				outputJson(response, JsonUtil.toJson("err", "", "msg", "../upload" + uploadPath + filename));
			} catch (Exception e) {
				e.printStackTrace();
				outputJson(response, JsonUtil.toJson("err", "上传失败!", "msg", ""));
			}
		} else {
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List<FileItem> fileItems = upload.parseRequest(request);
			for (FileItem item : fileItems) {
				if (!item.isFormField()) {
					String fileName = item.getName();
					if (fileName != null) {
						File file = new File(item.getName());
						if (file.exists()) {
							fileName = file.getName();
						}

						String uploadPath = getUploadPath();
						String path = rootPath + uploadPath.replace("/", File.separator);
						File dir = new File(path);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						File newFilename = new File(path, FileUtil.getMd5Filename(fileName, StringUtil.toString(System.currentTimeMillis())));
						item.write(newFilename);

						outputJson(response, JsonUtil.toJson("err", "", "msg", "../upload" + uploadPath + newFilename.getName()));
						return;
					}
				}
			}
		}
		outputJson(response, JsonUtil.toJson("err", "上传失败!", "msg", ""));
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping("uploadFile")
	public void uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String exts = StringUtil.notNull(request.getParameter("exts"));
		String path = StringUtil.notNull(request.getParameter("path"));

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart == true) {
			try {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("utf-8");
				upload.setFileSizeMax(1024 * 1024 * maxFileSize);

				List<FileItem> fileItems = upload.parseRequest(request);
				for (FileItem item : fileItems) {
					if (!item.isFormField()) {
						String fileName = item.getName();
						if (fileName != null) {
							File file = new File(item.getName());
							if (file.exists()) {
								fileName = file.getName();
							}
							if (StringUtil.isNotEmpty(exts)) {
								String ext = FileUtil.getFileExt(fileName);
								if (ext.equals("") || !("," + exts + ",").contains(("," + ext + ","))) {
									outputJson(response, JsonUtil.toJson("err", "文件名后缀必须是" + exts + "!", "msg", ""));
									return;
								}
							}
							
							path = rootPath + File.separator +  path + File.separator;
							File dir = new File(path);
							if (!dir.exists()) {
								dir.mkdirs();
							}
							File newFile = new File(path, fileName);
							item.write(newFile);
							outputJson(response, JsonUtil.toJson("err", "", "msg", fileName));
							
							return;
						}
					}
				}
			} catch (FileUploadException e) {
				e.printStackTrace();
				outputJson(response, JsonUtil.toJson("err", "上传失败!最大上传文件大小为" + maxFileSize + "M", "msg", ""));
				return;
			}
		} else {
			System.out.println("the enctype must be multipart/form-data");
			outputJson(response, JsonUtil.toJson("err", "上传失败!the enctype must be multipart/form-data!", "msg", ""));
			return;
		}
		outputJson(response, JsonUtil.toJson("err", "上传失败!", "msg", ""));
	}

	public String getUploadPath() {
		String uploadPath = "";
		if (StringUtil.isNotEmpty(pathRule)) {
			uploadPath = "/"
					+ pathRule.replace("yyyy", StringUtil.toString(DateUtil.getYear())).replace("mm", StringUtil.toString(DateUtil.getMonth())).replace("dd", StringUtil.toString(DateUtil.getDay()))
							.replace("dd", StringUtil.toString(DateUtil.getDay())) + "/";
		}
		return uploadPath;
	}

	@RequestMapping("download")
	public void download(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String filename = request.getParameter("filename");
		String code = StringUtil.notNull(request.getParameter("code"));
		Attachment attachment = attachmentService.getAttachmentByCodeFilename(code, filename);
		if (attachment == null) {
			return;
		}
		String remote = rootPath + attachment.getPath() + attachment.getFilename();
		attachmentService.download(attachment.getInitFilename(), remote, response);
	}

	@RequestMapping("downloadFile")
	public void downloadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("utf-8");
		String filename = request.getParameter("filename");
		String path = StringUtil.notNull(request.getParameter("path"));
		String remote = rootPath + path + filename;
		attachmentService.download(filename, remote, response);
	}
}