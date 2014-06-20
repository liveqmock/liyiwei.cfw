/*
 * Created on 2013-03-15 16:04:00
 *
 */
package com.liyiwei.cfw.system.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.liyiwei.cfw.system.entity.Attachment;
import com.liyiwei.common.util.FileUtil;
import com.liyiwei.common.util.JsonUtil;
import com.liyiwei.common.util.StringUtil;

/**
 * @author Liyiwei
 * 
 */
@Service
public class AttachmentService extends BaseAttachmentService {
	@Autowired
	private SystemSettingService systemSettingService;

	public Attachment getAttachmentByCodeFilename(String code, String filename) {
		return getAttachment(" where attachment.code='" + code + "' and attachment.filename='" + filename + "'");
	}

	public Attachment getAttachmentByCode(String code) {
		return getAttachment(" where attachment.code='" + code + "' order by attachment.uploadTime");
	}

	public List<Attachment> getAttachmentListByCode(String code) {
		return getAttachmentList(" where attachment.code='" + code + "' order by attachment.uploadTime");
	}

	public int getAttachmentCountByCode(String code) {
		return getAttachmentCount(" where attachment.code='" + code + "'");
	}

	@Transactional(readOnly = false)
	public void updateAttachmentCode(String oldCode, String newCode) {
		String hql = "update Attachment attachment set attachment.code='" + newCode + "' where attachment.code='" + oldCode + "'";
		this.attachmentDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteAttachmentByCode(String code) {
		String hql = "delete from Attachment attachment where attachment.code='" + code + "'";
		this.attachmentDao.batchExecute(hql);
	}

	@Transactional(readOnly = false)
	public void deleteAttachmentByCode(String code, String rootPath) {
		List<Attachment> list = getAttachmentListByCode(code);
		for (Attachment attachment : list) {
			deleteAttachment(attachment, rootPath);
		}
	}

	@Transactional(readOnly = false)
	public void deleteAttachment(int id, String rootPath) {
		Attachment attachment = getAttachment(id);
		deleteAttachmentFile(attachment, rootPath);
		deleteAttachment(attachment.getId());
	}

	@Transactional(readOnly = false)
	public void deleteAttachment(Attachment attachment, String rootPath) {
		if (attachment != null) {
			deleteAttachmentFile(attachment, rootPath);
			deleteAttachment(attachment.getId());
		}
	}

	@Transactional(readOnly = false)
	public void deleteAttachmentFile(Attachment attachment, String rootPath) {
		if (attachment != null) {
			String filename = attachment.getFilename();
			System.out.println("filename:" + filename);
			File file = new File(rootPath + attachment.getPath() + filename);
			if (file.exists()) {
				file.delete();
			}
			File fileThumbnails = new File(rootPath + attachment.getPath() + FileUtil.setFileName(filename, "_100_100"));
			if (fileThumbnails.exists()) {
				fileThumbnails.delete();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public String upload(HttpServletRequest request, String realPath,String exts) throws Exception {
		String rootPath = systemSettingService.getSystemSetting("uploadRootPath", "upload");
		int maxFileSize = StringUtil.toInt(systemSettingService.getSystemSetting("maxFileSize", "20"));
		rootPath = realPath + File.separator + rootPath;

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart == true) {
			try {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
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
							String ext = FileUtil.getFileExt(fileName);
							if (ext.equals("") || !exts.contains((ext))) {
								return JsonUtil.toJson("err", "文件名后缀必须是" + exts, "msg", "");
							}

							String path = rootPath + "/import/".replace("/", File.separator);
							File dir = new File(path);
							if (!dir.exists()) {
								dir.mkdirs();
							}
							File newFilename = new File(path, FileUtil.getMd5Filename(fileName, StringUtil.toString(System.currentTimeMillis())));
							item.write(newFilename);

							return JsonUtil.toJson("err", "", "msg", path + newFilename.getName());
						}
					}
				}
			} catch (FileUploadException e) {
				return JsonUtil.toJson("err", "上传失败!最大上传文件大小为" + maxFileSize + "M", "msg", "");
			}
		} else {
			return JsonUtil.toJson("err", "上传失败!the enctype must be multipart/form-data!", "msg", "");
		}
		return JsonUtil.toJson("err", "上传失败!", "msg", "");
	}
	
	@SuppressWarnings("unchecked")
	public String importXls(HttpServletRequest request, String realPath) throws Exception {
		String rootPath = systemSettingService.getSystemSetting("uploadRootPath", "upload");
		int maxFileSize = StringUtil.toInt(systemSettingService.getSystemSetting("maxFileSize", "20"));
		rootPath = realPath + File.separator + rootPath;

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart == true) {
			try {
				DiskFileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
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
							String ext = FileUtil.getFileExt(fileName);
							if (ext.equals("") || !("xls,xlsx").contains((ext))) {
								return JsonUtil.toJson("err", "文件名后缀必须是xls或者xlsx", "msg", "");
							}

							String path = rootPath + "/import/".replace("/", File.separator);
							File dir = new File(path);
							if (!dir.exists()) {
								dir.mkdirs();
							}
							File newFilename = new File(path, FileUtil.getMd5Filename(fileName, StringUtil.toString(System.currentTimeMillis())));
							item.write(newFilename);

							return JsonUtil.toJson("err", "", "msg", path + newFilename.getName());
						}
					}
				}
			} catch (FileUploadException e) {
				return JsonUtil.toJson("err", "上传失败!最大上传文件大小为" + maxFileSize + "M", "msg", "");
			}
		} else {
			return JsonUtil.toJson("err", "上传失败!the enctype must be multipart/form-data!", "msg", "");
		}
		return JsonUtil.toJson("err", "上传失败!", "msg", "");
	}

	public void download(String filename, String remote, HttpServletResponse response) throws Exception {
		OutputStream out = null;
		FileInputStream reader = null;
		filename = StringUtil.notNull(new String(filename.getBytes(), "UTF-8"));

		try {
			response.reset();
			response.setHeader("Content-type", "application/octet-stream;charset=ISO8859-1");
			response.setHeader("Accept-Ranges", "bytes");
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1"));

			out = response.getOutputStream();
			int readnum = 0;
			byte[] buffer = new byte[8192];
			reader = new FileInputStream(remote);
			while ((readnum = reader.read(buffer)) != -1) {
				out.write(buffer, 0, readnum);
			}
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (out != null)
					out.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}