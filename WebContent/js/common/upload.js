/*******************************************************************************
 * 
 * 程序功能：通用文件上传函数库 作者： 李一伟 编写日期：2013-04-11
 * 
 ******************************************************************************/

function getAttachmentList(code, type, rootPath, hiddenFeild, divId) {
    var url = "../system/attachmentList.htm?code="+code;
    $.post(url, function(data) {
		showAttachment(data, type, code, rootPath, hiddenFeild, divId);
	}, "json");
}

function deleteAttachment(id, type, code, rootPath, hiddenFeild, divId) {
    var result = window.confirm("您确认要删除吗？");
    if (!result) {
        return;
    }
    var url = "../system/attachmentDelete.htm?id=" + id + "&code=" + code;
  	$.post(url, function(data) {
        showAttachment(data, type, code, rootPath, hiddenFeild, divId);
    }, "json");
}

function showAttachment(data, type, code, rootPath, hiddenFeild, divId) {
    var attachmentList = eval(data);
	if (attachmentList.length > 0) {
		if(type=="image") {
			var info = "<ul class='img-list'>";
			$.each(attachmentList, function(i, n) {
				var name = n.filename;
				var index = name.lastIndexOf('.');
				var url = name.substring(0, index);
				url += "_100_100" + name.substring(index);
				url = "../" + rootPath + n.path + url;
				info += "<li>";
				info += "<a href='" + url.replace("_100_100","") + "' target='_blank'><img src='" + url + "'></a>";
				info += "<p class='tc'><a title='删除' href='javascript:deleteAttachment(\"" + n.id + "\", \""+type+"\", \"" + code+ "\", \"" + rootPath + "\", \"" + hiddenFeild + "\", \"" + divId + "\");'>删除</a></p>";
				info += "</li>";
				$("#" + hiddenFeild).val(n.path + name);
			});
			info += "</ul>";
		} else {
			var info = "<table>";
			$.each(attachmentList, function(i, n) {
				var name = n.filename;
				var icon = getFileIcon(getFileExt(name));
				info += "<tr><td><img src='../images/" + icon + "' height='16' width='16'>&nbsp;<a href='../system/download.htm?code=" + n.code + "&filename=" + n.filename + "'>" + n.initFilename + "</a>";
				info += "&nbsp;&nbsp;<a title='删除' href='javascript:deleteAttachment(\"" + n.id + "\", \""+type+"\", \"" + code+ "\", \"" + rootPath + "\", \"" + hiddenFeild + "\", \"" + divId + "\");'>删除</a></td>";
				info += "</tr>";
				$("#" + hiddenFeild).val(n.path + name);
			});
			info += "</table>";
		}	
		$("#" + divId).html(info);
	}else{
		$("#" + divId).html("");
		$("#" + hiddenFeild).val("");
	}
}

function uploadFile(exts, num, fileId, type, code, rootPath, hiddenFeild, divId, button, path, rename) {
    if($("#" + fileId).val() == "") {
        alert("请选择上传文件！");
        return false;
    }
    
    if(num != "" && num != "0"){
    	var url = "../system/attachmentListCount.htm?code=" + code;
      	$.post(url, function(data) {
      		if(data.msg >= num){
            	alert("你最多上传" + num + "个文件");
            } else {
            	uploadFileAction(exts, fileId, type, code, rootPath, hiddenFeild, divId, button, path, rename);
            }
        }, "json");
    }
}

function uploadFileAction(exts, fileId, type, code, rootPath, hiddenFeild, divId, button, path, rename) {
    var thumbnails = 0;
    if(type == "image"){
        thumbnails = 1;
    }
	if(type == "image" && exts == ""){
       exts = "jpg,jpeg,gif,png";
    }
	
    button.disabled = true;
    $.ajaxFileUpload({
		url: "../system/upload.htm?exts=" + exts + "&code=" + code + "&thumbnails=" + thumbnails + "&path=" + path + "&rename=" + rename,
		secureuri: false,
		fileElementId: fileId,
		dataType: "json",
		success: function (data, status) {
			if(typeof(data.err) != "undefined") {
				if(data.err != "") {
					alert(data.err);
				}else {
				    showAttachment(data.msg, type, code, rootPath, hiddenFeild, divId);
				}
			}
			button.disabled = false;
		},
		error: function (data, status, e) {
			button.disabled = false;
			alert(e);
		}
	});
	return false;
}

function getFileExt(str) { 
   var d=/\.[^\.]+$/.exec(str); 
   return d; 
}

function getFileIcon(ext) { 
	if(ext =='.jpg' || ext == '.jpeg' || ext == '.png' || ext == '.gif') {
		return 'pic.png';
	} else if(ext =='.doc' || ext == '.docx') {
			return 'word.png';	
	} else if(ext =='.xls' || ext == '.xlsx') {
		return 'excel.png';
	} else if(ext =='.ppt' || ext == '.pptx') {	
		return 'ppt.png';
	} else if(ext =='.pdf') {	
		return 'pdf.png';	
	} else if(ext =='.rar' || ext =='.zip') {	
		return 'rar.png';
	} else {
		return 'file.png';
	}   
}