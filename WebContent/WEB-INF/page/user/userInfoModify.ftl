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
<title>修改个人信息</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css"/>
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
</head>

<body>
<form name="form1" id="form1" action="userInfoSave.htm" method="post">
<input type="hidden" name="id" value="${_user.id?if_exists}"/>
<input type="hidden" name="rawPassword" value="${_user.rawPassword?if_exists}"/>
<input type="hidden" name="status" value="${_user.status?if_exists}"/>
<input type="hidden" name="registerTime" value="${_user.registerTimeName?if_exists}"/>
<input type="hidden" name="lastVisitTime" value="${_user.lastVisitTimeName?if_exists}"/>
<input type="hidden" name="lastIp" value="${_user.lastIp?if_exists}"/>
<input type="hidden" name="departmentId" value="${_user.departmentId?if_exists}"/>
<div class="titlebar"><i class="icon-user"></i> <a href="userInfo.htm">查看个人信息</a> > <a href="javascript:;" onclick="window.location.reload();">修改个人信息</a></div>
<div class="container-fluid">
<table class="table table-condensed table-bordered">
  <thead>
  <tr>
    <th height="25" align="center" colspan="2"><strong>个人信息</strong></td>
  </tr>
  </thead>
  <tbody>
  <tr>
    <td class="td-left">部门：&nbsp;</td>
    <td class="td-right">${_user.departmentName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left">名称：&nbsp;</td>
    <td class="td-right">${_user.username?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left">真实名称：&nbsp;</td>
    <td class="td-right"><input type="text" name="realname" id="realname" class="span3" maxlength="40" value="${_user.realname?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left">职位：&nbsp;</td>
    <td class="td-right"><input type="text" name="title" id="title" class="span3" maxlength="40" value="${_user.title?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left">Email：&nbsp;</td>
    <td class="td-right"><input type="text" name="email" id="email" class="span3" maxlength="60" value="${_user.email?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left">手机号：&nbsp;</td>
    <td class="td-right"><input type="text" name="mobile" id="mobile" class="span3" maxlength="60" value="${_user.mobile?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left">分机号：&nbsp;</td>
    <td class="td-right"><input type="text" name="ext" id="ext" class="span3" maxlength="20" value="${_user.ext?if_exists}"></td>
  </tr>
  <tr>
    <td class="td-left">备注：&nbsp;</td>
    <td class="td-right"><textarea name="note" id="note" class="w300 h100">${_user.note?if_exists}</textarea></td>
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
$(document).ready(function() {
	$("#submitBtn").click(function() {
		if (checkData()) {
			$("#form1").submit();
		}
	});
});

function checkData() {
    if (!checkEmail("#email"))
		return false;
		
	if (!checkMax("#note", 500,"描述"))
		return false;	
				
	return true;
}
</script>
</body>
</html>