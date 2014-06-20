<#--
 ****************************************************
 * Created on 2013-02-19 17:38:09
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>查看角色</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css"/>
</head>

<body>
<div class="titlebar"><i class="icon-search"></i> <a href="roleList.htm">角色管理</a> > <a href="javascript:;" onclick="window.location.reload();">查看角色</a></div>
<div class="container-fluid">
<table class="table table-condensed table-bordered">
  <thead>
  <tr>
    <th height="25" align="center" colspan="2"><strong>查看角色</strong></td>
  </tr>
  </thead>
  <tbody>
  <tr>
    <td class="td-left"><label class="Validform_label">角色名称：&nbsp;</label></td>
    <td class="td-right">${role.name?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left"><label class="Validform_label">描述：&nbsp;</label></td>
    <td class="td-right">${role.note?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left"><label class="Validform_label">拥有权限：&nbsp;</label></td>
    <td class="td-right">
    <#list rolePrivilegeList as rolePrivilege>
    ${rolePrivilege.privilegeName?if_exists}<br/>
    </#list>
    </td>
  </tr>
  <tr>
    <td class="td-left2">&nbsp;</td>
    <td class="td-right2"><button type="button" class="btn" onclick="history.go(-1);"><i class="icon-share-alt"></i>　返　回　</button></td>
  </tr>
  </tbody>
</table>
</div>
<script>
</body>
</html>