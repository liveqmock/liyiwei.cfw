<#--
 ****************************************************
 * Created on 2013-01-09 17:40:55
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>部门列表</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link href="../widget/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
<script src="../widget/zTree/js/jquery.ztree.core-3.5.js"></script>
</head>

<body>
<form name="form1" id="form1" action="departmentList.htm" method="post">
<input type="hidden" name="pn" value="${page.currentPage?if_exists}"/>
<input type="hidden" name="orderby" value="department.parentId,department.seq,department.id"/>
<input type="hidden" name="descflag"/>
<input type="hidden" name="pId" value="${pId?if_exists}"/>
<input type="hidden" id="sno" name="sno"/>
<div class="titlebar"><i class="icon-th-large"></i> <a href="departmentList.htm">部门管理</a><#if department?exists> - <a href="javascript:;" onclick="openDepartmentEditDialog(${department.id?if_exists});">${department.name?if_exists}</a></#if></div>
<div class="container-fluid">
  <div class="searchbar">
    &nbsp;部门名称：<input type="text" name="queryName" id="queryName" class="input-normal" value="${queryName?if_exists}"/>
    &nbsp;部门编码：<input type="text" name="queryCode" id="queryCode" class="input-normal" value="${queryCode?if_exists}"/>
    &nbsp;<button type="button" onclick="return queryIt();" class="btn"><i class="icon-search"></i> 查  询 </button>
  </div>
  <table class="table-page">
    <tr>
      <td>
         <button type="button" class="btn" onclick="openDepartmentEditDialog();"><i class="icon-plus"></i> 新建部门</button>
         <#if department?exists><button type="button" class="btn" onclick="openDepartmentEditDialog(${department.id?if_exists});"><i class="icon-edit"></i> 修改当前部门</button></#if>
      </td>
      <td width="50%" align="right"><#include "../common/page.ftl" ></td>
    </tr>
  </table>
  <table class="table table-bordered table-condensed">
    <thead>
    <tr>
      <th width="30">序号</th>
      <th>部门名称</th>
      <th>部门编码</th>
      <th>上级部门</th>
      <th width="170">操作</th>
    </tr>
    </thead>
    <tbody>
    <#list departmentList as department>
    <tr>
      <td class="h25 tc">${(page.currentPage - 1) * page.pageSize + department_index + 1 }</td>
      <td class="h25 tl">${department.name?if_exists}</td>
      <td class="h25 tc">${department.code?if_exists}</td>
      <td class="h25 tc">${department.parentName?if_exists}</td>
      <td class="h25 tl">
        <a href="javascript:;" onclick="openDepartmentEditDialog(${department.id?if_exists});">修改</a>
        |&nbsp;<a href="javascript:;" onclick="return deleteIt('${department.id?if_exists}');">删除</a>
        <#if pId != '' && queryName = '' && queryCode = ''>
        <#if ((page.currentPage -1) * page.pageSize + department_index > 0)>
        |&nbsp;<a href="javascript:;" onclick="return up(${(page.currentPage - 1) * page.pageSize + department_index + 1 })">上移</a>
        </#if>
        <#if ((page.currentPage -1) * page.pageSize + department_index + 1 < page.recordCount)>
        |&nbsp;<a href="javascript:;" onclick="return down(${(page.currentPage - 1) * page.pageSize + department_index + 1 })">下移</a>
        </#if>
        </#if>
      </td>
    </tr>
    </#list>
    <#if departmentList?size != page.pageSize>
    <#list departmentList?size + 1..page.pageSize as i>
    <tr>
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
    var result = window.confirm("将删除此部门及此部门下所有的子部门，您确认要删除吗？");
    if (!result) {
        return false; 
    }
    var url = "departmentDelete.htm?id=" + id;
  	$.post(url, function(data) {
  		alert(data.msg);
  		window.location.reload();
    }, "json");
}

$.ajaxSetup ({
    cache: false
});

function openDepartmentEditDialog(id) {
	$("#popWindow").load("departmentEditDialog.htm?id=" + id + "&parentId=${pId?if_exists}",function() {
	    $('#departmentEditDialog').modal({backdrop:false,keyboard:true,show:true},'show');
	});
}

function up(sno) {
    $("#sno").val(sno);
    $("#form1").attr("action","departmentUp.htm");
    $("#form1").submit();
}

function down(sno) {
    $("#sno").val(sno);
    $("#form1").attr("action","departmentDown.htm");
    $("#form1").submit();
}
</script>
</body>
</html>