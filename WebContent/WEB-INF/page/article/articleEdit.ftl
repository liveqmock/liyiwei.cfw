<#--
 ****************************************************
 * Created on 2013-03-13 16:32:35
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title><#if !article.id?exists>新建<#else>修改</#if>文章</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link href="../widget/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css"/>
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/jquery/ajaxfileupload.js"></script>
<script src="../js/common/upload.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/zTree/js/jquery.ztree.core-3.5.js"></script>
<script src="../widget/xheditor/xheditor-1.2.1.min.js"></script>
<script src="../widget/xheditor/xheditor_lang/zh-cn.js"></script>
<script>

</script>
</head>

<body>
<form name="form1" id="form1" action="articleSave.htm" method="post">
<input type="hidden" name="id" value="${article.id?if_exists}"/>
<input type="hidden" name="status" value="${article.status?if_exists}"/>
<input type="hidden" name="viewCount" value="${article.viewCount?if_exists}"/>
<!--
<input type="hidden" name="approveStatus" value="${article.approveStatus?if_exists}"/>
<input type="hidden" name="approveUserId" value="${article.approveUserId?if_exists}"/>
<input type="hidden" name="approveTime" value="${article.approveTimeName?if_exists}"/>
<input type="hidden" name="approveNote" value="${article.approveNote?if_exists}"/>
-->
<input type="hidden" name="createUserId" value="${article.createUserId?if_exists}"/>
<input type="hidden" name="createTime" value="${article.createTimeName?if_exists}"/>
<input type="hidden" name="modifyUserId" value="${article.modifyUserId?if_exists}"/>
<input type="hidden" name="modifyTime" value="${article.modifyTimeName?if_exists}"/>
<input type="hidden" id="channelId" name="channelId" value="${article.channelId?if_exists}"/>

<div class="titlebar"><i class="icon-edit"></i> <a href="articleList.htm">文章管理</a> > <a href="javascript:;" onclick="window.location.reload();"><#if !article.id?exists>新建<#else>修改</#if>文章</a></div>
<div class="container-fluid">
<table class="table table-condensed table-bordered">
  <thead>
  <tr>
    <th height="25" align="center" colspan="2"><strong>文章信息</strong></td>
  </tr>
  </thead>
  <tbody>
  <tr>
    <td class="td-left3"><label class="Validform_label"><span class="red"> * </span>频道：&nbsp;</label></td>
    <td class="td-right3">
      <div class="selectBox">
        <span class="selectTxt">${article.channelName?if_exists}</span>
        <div class="option">
          <div id="tree" class="ztree"></div>
        </div>
      </div>
    </td>
  </tr>
  <tr>
    <td class="td-left3"><label class="Validform_label"><span class="red"> * </span>标题：&nbsp;</label></td>
    <td class="td-right3"><input type="text" name="title" id="title" class="span6" maxlength="120" value="${article.title?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left3"><label class="Validform_label">子标题：&nbsp;</label></td>
    <td class="td-right3"><input type="text" name="subTitle" id="subTitle" class="span6" maxlength="120" value="${article.subTitle?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left3"><label class="Validform_label">标签：&nbsp;</label></td>
    <td class="td-right3"><input type="text" name="tag" id="tag" class="span3" maxlength="60" value="${article.tag?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left3"><label class="Validform_label">作者：&nbsp;</label></td>
    <td class="td-right3"><input type="text" name="author" id="author" class="span3" maxlength="40" value="${article.author?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left3"><label class="Validform_label">来源：&nbsp;</label></td>
    <td class="td-right3"><input type="text" name="source" id="source" class="span3" maxlength="60" value="${article.source?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left3"><label class="Validform_label">摘要：&nbsp;</label></td>
    <td class="td-right3"><textarea name="brief" id="brief" rows="4" cols="100">${article.brief?if_exists}</textarea></td>
  </tr>
  <tr>
    <td class="td-left3"><label class="Validform_label">缩略图：&nbsp;</label></td>
    <td class="td-right3">
      <input type="file" id="uploadFile" name="uploadFile"/>
      <button type="button" class="btn btn-primary" onclick="uploadIt(this);"><i class="icon-ok icon-white"></i>上传</button>
      <div id="attachmentList"></div>
      <input type="hidden" id="code" name="code"/>
      <input type="hidden" id="smallPic" name="smallPic" value="${article.smallPic?if_exists}"/>
    </td>
  </tr>
  <tr>
    <td class="td-left3"><label class="Validform_label">内容：&nbsp;</label></td>
    <td class="td-right3">
    <textarea name="content" id="content" class="xheditor {upImgUrl:'../system/uploadImage.htm',upImgExt:'jpg,jpeg,gif,png'}" rows="20" cols="137">${article.content?if_exists}</textarea></td>
  </tr>
  
  <tr>
    <td class="td-left3">&nbsp;</td>
    <td class="td-right3">
      <button type="button" id="submitBtn" class="btn btn-primary" onclick="return submitIt();"><i class="icon-ok icon-white"></i>　提　交　</button>&nbsp;
      <button type="button" class="btn" onclick="history.go(-1);"><i class="icon-share-alt"></i>　返　回　</button>
    </td>
  </tr>
  </tbody>
</table>
</div>
</form>
<script>
var setting = {
    view: {	
        dblClickExpand: false
	},
    data: {
        simpleData: {enable: true}
    },
    callback: {
		onClick: onClick
	}
};

var zNodes =${channelTree?if_exists};

function onClick(e, treeId, treeNode) {
    $(".selectTxt").text(treeNode.name);
    $("#channelId").val(treeNode.id);
}

$(document).ready(function() {
    $.fn.zTree.init($("#tree"), setting, zNodes);
    
    var treeObj = $.fn.zTree.getZTreeObj("tree");
    var node = treeObj.getNodeByParam("id", ${article.channelId?default(0)}, null);
    treeObj.selectNode(node,false);
    
	$(".selectBox").click(function(event) {
		event.stopPropagation();
		$(this).find(".option").toggle();
		$(this).parent().siblings().find(".option").hide();
	});
	
	$(document).click(function(event) {
		var e = $(event.target);
		if ($(".selectBox").is(":visible") && e.attr("class") != "option"	&& !e.parent(".option").length) {
			$('.option').hide();
		}	
	});
});

$(document).ready(function() {
    $("#submitBtn").click(function() {
        if (checkData()) {
            $("#form1").submit();
        }
    });
});

<#if !article.id?exists>
var code = new Date().getTime();
$("#code").val(code);
<#else>
var code = "article${article.id?if_exists}";
getAttachmentList("article${article.id?if_exists}","image",'${rootPath?if_exists}','smallPic',"attachmentList");
</#if>

function uploadIt(button) {
    uploadFile('',1,'uploadFile','image',code,'${rootPath?if_exists}','smallPic','attachmentList', button, '', 1);
}

function checkData() {
    if (!checkNull("#channelId", "频道"))
        return false;

    if (!checkNull("#title", "标题"))
        return false;
		
    if (!checkMax("#brief", 1000, "摘要"))
        return false;

    return true;
}
</script>
</body>
</html>