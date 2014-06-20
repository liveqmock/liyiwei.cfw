<#--
 ****************************************************
 * Created on 2013-01-15 17:32:33
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>角色列表</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
<script src="../widget/My97DatePicker/WdatePicker.js"></script>
</head>

<body>
<form name="form1" id="form1" action="roleList.htm" method="post">
<input type="hidden" name="id"/>
<input type="hidden" name="pn" value="${page.currentPage?if_exists}"/>
<input type="hidden" name="orderby" value="role.id"/>
<input type="hidden" name="descflag"/>
<div class="titlebar"><i class="icon-th-large"></i> <a href="roleList.htm">角色管理</a></div>
<div class="container-fluid">
  <table class="table-page">
    <tr>
      <td>
        <button type="button" class="btn" onclick="openRoleEditDialog();"><i class="icon-plus"></i> 新建角色</button>
      </td>
      <td width="70%" align="right"><#include "../common/page.ftl" ></td>
    </tr>
  </table>
  <table class="table table-bordered table-condensed">
    <thead>
    <tr>
      <th width="30">序号</th>
      <th>名称</th>
      <th>说明</th>
      <th width="190">操作</th>
    </tr>
    </thead>
    <tbody>
    <#list roleList as role>
    <tr>
      <td class="h25 tc">${(page.currentPage - 1) * page.pageSize + role_index + 1 }</td>
      <td class="h25 tc">${role.name?if_exists}</td>
      <td class="h25 tc">${role.noteBrief?if_exists}</td>
      <td class="h25 tc">
        <a href="rolePrivilegeSet.htm?roleId=${role.id?if_exists}">权限设置</a>
        |&nbsp;<a href="roleView.htm?id=${role.id?if_exists}">查看</a>
        |&nbsp;<a href="javascript:;" onclick="openRoleEditDialog(${role.id?if_exists});">修改</a>
        |&nbsp;<a href="javascript:;" onclick="return deleteIt('${role.id?if_exists}');">删除</a>
      </td>
    </tr>
    </#list>
    <#if roleList?size != page.pageSize>
    <#list roleList?size + 1..page.pageSize as i>
    <tr>
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
<div id="popWindow"></div>
<script>    
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
    var url = "roleDelete.htm?id=" + id;
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
	var url = "roleDeleteSelect.htm?ids=" + ids;
  	$.post(url, function(msg) {
  		alert(data.msg);
  		window.location.reload();
    }, "json");
}

$.ajaxSetup ({
    cache: false
});

function openRoleEditDialog(id) {
	$("#popWindow").load("roleEditDialog.htm?id=" + id,function() {
	    $('#roleEditDialog').modal({backdrop:false,keyboard:true,show:true},'show');
	});
}

</script>
</body>
</html>