<#--
 ****************************************************
 * Created on 2013-01-09 17:45:42
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title><#if !user.id?exists>新建<#else>修改</#if>用户</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link href="../widget/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css"/>
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/zTree/js/jquery.ztree.core-3.5.js"></script>
</head>

<body>
<form name="form1" id="form1" action="userSave.htm" method="post">
<input type="hidden" id="id" name="id" value="${user.id?if_exists}"/>
<input type="hidden" id="departmentId" name="departmentId" value="${user.departmentId?if_exists}"/>
<div class="titlebar"><i class="icon-edit"></i> <a href="userList.htm">用户管理</a> > <a href="javascript:;" onclick="window.location.reload();"><#if !user.id?exists>新建<#else>修改</#if>用户</a></div>
<div class="container-fluid">
<table class="table table-condensed table-bordered">
  <thead>
  <tr>
    <th height="25" align="center" colspan="2"><strong>用户信息</strong></td>
  </tr>
  </thead>
  <tbody>
  <tr>
    <td class="td-left"><span class="red"> * </span>所属部门：&nbsp;</td>
    <td class="td-right">
      <div class="selectBox">
        <span class="selectTxt">${user.departmentName?if_exists}</span>
        <div class="option">
          <div id="tree" class="ztree"></div>
        </div>
      </div>
    </td>
  </tr>
  <tr>
    <td class="td-left"><label class="Validform_label"><#if !user.id?exists><span class="red"> * </span></#if>名称：&nbsp;</label></td>
    <td class="td-right"><#if !user.id?exists><input type="text" name="username" id="username" class="span3" maxlength="20" value="${user.username?if_exists}"><#else><input type="hidden" name="username" id="username" value="${user.username?if_exists}">${user.username?if_exists}</#if></td>
  </tr>
  <#if !user.id?exists>
  <tr>
    <td class="td-left"><span class="red"> * </span>密码：&nbsp;</td>
    <td class="td-right"><input type="password" name="password" id="password" class="span3" maxlength="255" value="${user.password?if_exists}">&nbsp;6-12位密码</td>
  </tr>
  </#if>
  <tr>
    <td class="td-left">真实名称：&nbsp;</td>
    <td class="td-right"><input type="text" name="realname" id="realname" class="span3" maxlength="40" value="${user.realname?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left">职位：&nbsp;</td>
    <td class="td-right"><input type="text" name="title" id="title" class="span3" maxlength="40" value="${user.title?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left">Email：&nbsp;</td>
    <td class="td-right"><input type="text" name="email" id="email" class="span3" maxlength="60" value="${user.email?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left">手机号：&nbsp;</td>
    <td class="td-right"><input type="text" name="mobile" id="mobile" class="span3" maxlength="60" value="${user.mobile?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left">分机号：&nbsp;</td>
    <td class="td-right"><input type="text" name="ext" id="ext" class="span3" maxlength="20" value="${user.ext?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left">备注：&nbsp;</td>
    <td class="td-right"><textarea name="note" id="note" class="w300 h100">${user.note?if_exists}</textarea></td>
  </tr>
  <tr>
    <td class="td-left">身份：&nbsp;</td>
    <td class="td-right">${user.rankSelectBox?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left">角色：&nbsp;</td>
    <td class="td-right">${user.rolesCheckBox?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left">&nbsp;</td>
    <td class="td-right">
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

var zNodes =${departmentTree?if_exists};

function onClick(e, treeId, treeNode) {
    $(".selectTxt").text(treeNode.name);
    $("#departmentId").val(treeNode.id);
}

$(document).ready(function() {
    $.fn.zTree.init($("#tree"), setting, zNodes);
    
    var treeObj = $.fn.zTree.getZTreeObj("tree");
    var node = treeObj.getNodeByParam("id", ${user.departmentId?default(0)}, null);
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
		    <#if !user.id?exists>
		    isExist();
		    <#else>
		    $("#form1").submit();
		    </#if>
		}
	});
});

function checkData() {
    if (!checkNull("#departmentId", "部门"))
		return false;
    <#if !user.id?exists>
    if (!checkNull("#username", "名称"))
		return false;
				
    if (!checkNull("#password", "密码"))
		return false;

	if (!checkMin("#password", 6, "密码"))
		return false;	
	</#if>
	if (!checkEmail("#email"))
		return false;	
	
	if (!checkMax("#note", 500, "描述"))
		return false;	
				
	return true;
}

function isExist() {
	var url = "isExist.htm";
  	$.post(url, {username:$("#username").val()},function(data) {
  	    if(data.isExist){
  		    alert(data.msg);
  		    return false;
  		} else {
  		    $("#form1").submit();
  		}
    }, "json");
}
</script>
</body>
</html>