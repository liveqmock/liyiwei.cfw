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
<title>用户列表</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
<script src="../widget/My97DatePicker/WdatePicker.js"></script>
</head>

<body>
<form name="form1" id="form1" action="userList.htm" method="post">
<input type="hidden" name="id" id="id"/>
<input type="hidden" name="pn" value="${page.currentPage?if_exists}"/>
<input type="hidden" name="orderby" value="user.id"/>
<input type="hidden" name="descflag"/>
<input type="hidden" name="departmentId" value="${queryMap.departmentId?if_exists}"/>
<div class="titlebar"><i class="icon-th-large"></i> <a href="userList.htm?departmentId=${departmentId?if_exists}" >用户管理</a></div>
<div class="container-fluid">
  <div class="searchbar">
    &nbsp;名称：<input type="text" name="queryUsername" id="queryUsername" class="input-normal" value="${queryMap.queryUsername?if_exists}"/>
    &nbsp;真实名称：<input type="text" name="queryRealname" id="queryRealname" class="input-normal" value="${queryMap.queryRealname?if_exists}"/>
    &nbsp;状态：${queryMap.queryStatus?if_exists}
    &nbsp;<button type="button" onclick="return queryIt();" class="btn"><i class="icon-search"></i> 查  询 </button>
  </div>
  <table class="table-page">
    <tr>
      <td>
        <a class="btn" href="userEdit.htm?departmentId=${queryMap.departmentId?if_exists}"><i class="icon-plus"></i> 新建用户</a>
        <button type="button" class="btn" onclick="return deleteSelect();"><i class="icon-trash"></i> 删除</button>
      </td>
      <td width="70%" align="right"><#include "../common/page.ftl" ></td>
    </tr>
  </table>
  <table class="table table-bordered table-condensed">
    <thead>
    <tr>
      <th width="20"><input type="checkbox" name="chkAll" id="chkAll" onclick='checkBoxSelectAll("chkAll","chkId");'></th>
      <th width="30">序号</th>
      <th>部门</th>
      <th>名称</th>
      <th>真实名称</th>
      <th>身份</th>
      <th>角色</th>
      <th>状态</th>
      <th width="180">操作</th>
    </tr>
    </thead>
    <tbody>
    <#list userList as user>
    <tr>
      <td class="h25 tc"><input type="checkbox" name="chkId" value="${user.id?if_exists}"/></td>
      <td class="h25 tc">${(page.currentPage - 1) * page.pageSize + user_index + 1 }</td>
      <td class="h25 tc">${user.departmentName?if_exists}</td>
      <td class="h25 tc"><a href="userView.htm?id=${user.id?if_exists}">${user.username?if_exists}</a></td>
      <td class="h25 tc">${user.realname?if_exists}</td>
      <td class="h25 tc">${user.rankName?if_exists}</td>
      <td class="h25 tc">${user.rolesName?if_exists}</td>
      <td class="h25 tc">${user.statusName?if_exists}</td>
      <td class="h25 tc">
        <a href="userEdit.htm?id=${user.id?if_exists}">修改</a>
        |&nbsp;<a href="javascript:;" onclick="openModifyPasswordDialog('${user.id?if_exists}');">重置密码</a>
        |&nbsp;<a href="javascript:;" onclick="return deleteIt('${user.id?if_exists}');">删除</a>
        <#if user.status == 1>
        |&nbsp;<a href="javascript:;" onclick="return statusSet('${user.id?if_exists}',0);">冻结</a>
        <#else>
        |&nbsp;<a href="javascript:;" onclick="return statusSet('${user.id?if_exists}',1);">激活</a>
        </#if>
      </td>
    </tr>
    </#list>
    <#if userList?size != page.pageSize>
    <#list userList?size + 1..page.pageSize as i>
    <tr>
      <td class="h25">&nbsp;</td>
      <td class="h25">&nbsp;</td>
      <td class="h25">&nbsp;</td>
      <td class="h25">&nbsp;</td>
      <td class="h25">&nbsp;</td>
      <td class="h25">&nbsp;</td>
      <td class="h25">&nbsp;</td>
      <td class="h25">&nbsp;</td>
      <td class="h25">&nbsp;</td>
    </tr>
    </#list>
    </#if>
    </tbody>
  </table>
</div>
</form>
<div class="modal hide" id="modifyPasswordDialog" style="top: 75%;left: 40%;">
  <div class="modal-header">   
    <button type="button" class="close" data-dismiss="modal">&times;</button>
    <i class="icon-pencil"></i><strong> 修改密码</strong>
  </div> 
  <div class="modal-body">
    <table class="table-condensed">
      <tr>
        <td class="td-left"><span class="red"> * </span>新密码：&nbsp;</td>
        <td class="td-right"><input type="password" name="password" id="password" class="span3" maxlength="12" value="">&nbsp;6-12位密码</td>
      </tr>
    </table>
  </div>
  <div class="modal-footer">
    <button type="button" class="btn" data-dismiss="modal"><i class="icon-remove"></i>　关　闭　</button>&nbsp;
    <button type="button" id="submitBtn" class="btn btn-primary"><i class="icon-ok icon-white"></i>　提　交　</button>
  </div>
</div>
<script>    
$(document).ready(function() {
	$("#submitBtn").click(function() {
		if (checkData()) {
		    var id = $('#id').val();
		    var password = $('#password').val();
		    modifyPassword(id,password)
		}
	});
});

function queryIt() {
    document.form1.pn.value = "1";
    document.form1.submit();
}

function sortIt(orderby,descflag) {
    document.form1.pn.value = "1";
    document.form1.orderby.value = orderby;
    document.form1.descflag.value = descflag;
    document.form1.submit();
}

function deleteIt(id) {
    var result = window.confirm("您确认要删除吗？");
    if (!result) {
        return false; 
    }
    var url = "userDelete.htm?id=" + id;
  	$.post(url, function(data) {
  		alert(data.msg);
  		window.location.reload();
    }, "json");
}

function deleteSelect() {
	var ids = getCheckBoxSelectValue("chkId");
	if(ids == ""){
	    alert ("请选择删除对象！");
		return false;
	}

	var result = window.confirm("您确认要删除所选吗？");
	if (!result) {
		return false; 
	}
	var url = "userDeleteSelect.htm?ids=" + ids;
  	$.post(url, function(data) {
  		alert(data.msg);
  		window.location.reload();
    }, "json");
}

function statusSet(id,status) {
    var info = status == 1?"激活":"冻结"; 
    var result = window.confirm("您确认要" + info + "吗？");
    if (!result) {
        return false; 
    }
    var url = "userStatusSet.htm?id=" + id + "&status=" + status;
  	$.post(url, function(data) {
  		alert(data.msg);
  		window.location.reload();
    }, "json");
}


function openModifyPasswordDialog(id) {
    $("#id").val(id);
    $("#password").val("");
    $('#modifyPasswordDialog').modal({backdrop:false,keyboard:true,show:true},'show');
}

function checkData() {
	if (!checkNull("#password", "新密码"))
		return false;
	
	if (!checkMin("#password", 6, "新密码"))
		return false;	
		
	return true;
}

function modifyPassword(id,password) {
    var url = "modifyUserPassword.htm?id=" + id + "&password=" + password;
  	$.post(url, function(data){
  		alert(data.msg);
  		if(data.success) {
  		    $('#modifyPasswordDialog').modal('hide');
  		}    
    }, "json");
}
</script>
</body>
</html>