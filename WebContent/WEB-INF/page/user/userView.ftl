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
<title>查看用户</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css"/>
</head>

<body>
<div class="titlebar"><i class="icon-search"></i> <a href="userList.htm">用户管理</a> > <a href="javascript:;" onclick="window.location.reload();">查看用户</a></div>
<div class="container-fluid">
<table class="table table-bordered">
  <tr>
    <th height="20" align="center" colspan="2"><strong>用户信息</strong></td>
  </tr>
  <tr>
    <td class="td-left2">部门：&nbsp;</td>
    <td class="td-right2">${user.departmentName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">名称：&nbsp;</td>
    <td class="td-right2">${user.username?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">真实名称：&nbsp;</td>
    <td class="td-right2">${user.realname?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">角色：&nbsp;</td>
    <td class="td-right2">${user.rolesName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">职位：&nbsp;</td>
    <td class="td-right2">${user.title?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">Email：&nbsp;</td>
    <td class="td-right2">${user.email?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">手机号：&nbsp;</td>
    <td class="td-right2">${user.mobile?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">分机号：&nbsp;</td>
    <td class="td-right2">${user.ext?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">状态：&nbsp;</td>
    <td class="td-right2">${user.statusName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">注册时间：&nbsp;</td>
    <td class="td-right2">${user.registerTimeName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">最后访问时间：&nbsp;</td>
    <td class="td-right2">${user.lastVisitTimeName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">最后访问IP：&nbsp;</td>
    <td class="td-right2">${user.lastIp?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">备注：&nbsp;</td>
    <td class="td-right2">${user.note?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left"><label class="Validform_label">拥有权限：&nbsp;</label></td>
    <td class="td-right">
    <#list privilegeList as privilege>
    ${privilege?if_exists}<br/>
    </#list>
    </td>
  </tr>
  <tr>
    <td class="td-left2">&nbsp;</td>
    <td class="td-right2"><button type="button" class="btn" onclick="history.go(-1);"><i class="icon-share-alt"></i>　返　回　</button></td>
  </tr>
</table>
</div>
</body>
</html>