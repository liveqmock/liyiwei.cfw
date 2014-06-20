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
<meta charset="utf-8" />
<title>查看个人信息</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css"/>
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
</head>

<body>
<div class="titlebar"><i class="icon-user"></i> <a href="userInfo.htm">查看个人信息</a></div>
<div class="container-fluid">
<table class="table table-bordered">
  <tr>
    <th height="20" align="center" colspan="2"><strong>个人信息</strong></td>
  </tr>
  <tr>
    <td class="td-left2">部门：&nbsp;</td>
    <td class="td-right2">${_user.departmentName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">名称：&nbsp;</td>
    <td class="td-right2">${_user.username?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">真实名称：&nbsp;</td>
    <td class="td-right2">${_user.realname?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">角色：&nbsp;</td>
    <td class="td-right2">${_user.rolesName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">职位：&nbsp;</td>
    <td class="td-right2">${_user.title?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">Email：&nbsp;</td>
    <td class="td-right2">${_user.email?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">手机号：&nbsp;</td>
    <td class="td-right2">${_user.mobile?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">分机号：&nbsp;</td>
    <td class="td-right2">${_user.ext?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">状态：&nbsp;</td>
    <td class="td-right2">${_user.statusName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">注册时间：&nbsp;</td>
    <td class="td-right2">${_user.registerTimeName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">最后访问时间：&nbsp;</td>
    <td class="td-right2">${_user.lastVisitTimeName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">最后访问IP：&nbsp;</td>
    <td class="td-right2">${_user.lastIp?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">备注：&nbsp;</td>
    <td class="td-right2">${_user.note?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">&nbsp;</td>
    <td class="td-right2">
      <a class="btn" href="userInfoModify.htm"><i class="icon-user"></i> 修改个人信息</a>
      <button type="button" class="btn" onclick="openModifyPasswordDialog();"><i class="icon-lock"></i> 修改密码</button>
    </td>
  </tr>
</table>
</div>
<div class="modal hide" id="modifyPasswordDialog">
  <div class="modal-header">   
    <button type="button" class="close" data-dismiss="modal">&times;</button>
    <i class="icon-pencil"></i><strong> 修改密码</strong>
  </div> 
  <div class="modal-body">
    <table class="table-condensed">
      <tr>
        <td class="td-left"><span class="red"> * </span>旧密码：&nbsp;</td>
        <td class="td-right"><input type="password" name="oldPassword" id="oldPassword" class="span3" maxlength="12" value=""></td>
      </tr>
      <tr>
        <td class="td-left"><span class="red"> * </span>新密码：&nbsp;</td>
        <td class="td-right"><input type="password" name="password" id="password" class="span3" maxlength="12" value="">&nbsp;6-12位密码</td>
      </tr>
      <tr>
        <td class="td-left"><span class="red"> * </span>确认密码：&nbsp;</td>
        <td class="td-right"><input type="password" name="recheckPassword" id="recheckPassword" class="span3" maxlength="12" value=""></td>
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
		    var oldPassword = $('#oldPassword').val();
		    var password = $('#password').val();
		    modifyPassword(oldPassword,password)
		}
	});
});

function openModifyPasswordDialog() {
    $('#modifyPasswordDialog').modal({backdrop:false,keyboard:true,show:true},'show');
}

function checkData() {
    if (!checkNull("#oldPassword", "旧密码"))
		return false;
		
	if (!checkNull("#password", "新密码"))
		return false;
	
	if (!checkMin("#password", 6, "新密码"))
		return false;	
		
	if (!checkNull("#recheckPassword", "确认密码"))
		return false;
			
	if (!checkPassword("#password", "#recheckPassword"))
		return false;			
				
	return true;
}

function modifyPassword(oldPassword,password) {
    var url = "modifyPassword.htm?oldPassword=" + oldPassword + "&password=" + password;
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