<#--
 ****************************************************
 * Created on 2013-02-18 15:20:24
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>分类列表</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link href="../widget/zTree/css/zTreeStyle/zTreeStyle.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
<script src="../widget/zTree/js/jquery.ztree.core-3.5.js"></script>
</head>

<body>
<form name="form1" id="form1" action="categoryList.htm" method="post">
<input type="hidden" id="id" name="id"/>
<input type="hidden" id="pn" name="pn" value="${page.currentPage?if_exists}"/>
<input type="hidden" id="orderby" name="orderby" value="category.parentId,category.seq,category.id"/>
<input type="hidden" id="descflag" name="descflag"/>
<input type="hidden" id="sno" name="sno"/>
<input type="hidden" name="parentId" value="${queryMap.parentId?if_exists}"/>
<div class="titlebar"><i class="icon-th-large"></i> <a href="categoryList.htm">分类管理</a><#if category?exists> - <a href="javascript:;" onclick="openCategoryEditDialog(${category.id?if_exists});">${category.name?if_exists}</a></#if></div>
<div class="container-fluid">
  <table class="table-page">
    <tr>
      <td>
        <button type="button" class="btn" onclick="openCategoryEditDialog();"><i class="icon-plus"></i> 新建分类</button>
        <#if category?exists><button type="button" class="btn" onclick="openCategoryEditDialog(${category.id?if_exists});"><i class="icon-edit"></i> 修改当前分类</button></#if>
        <button type="button" class="btn" onclick="return deleteSelect();"><i class="icon-trash"></i> 删除</button>
      </td>
      <td width="50%" align="right"><#include "../common/page.ftl"></td>
    </tr>
  </table>
  <table class="table table-bordered table-condensed">
    <thead>
    <tr>
      <th width="20"><input type="checkbox" name="chkAll" id="chkAll" onclick='checkBoxSelectAll("chkAll","chkId");'></th>
      <th width="30">序号</th>
      <th>名称</th>
      <th>上级分类</th>
      <th width="170">操作</th>
    </tr>
    </thead>
    <tbody>
    <#list categoryList as category>
    <tr>
      <td class="h25 tc"><input type="checkbox" name="chkId" value="${category.id?if_exists}"/></td>
      <td class="h25 tc">${(page.currentPage - 1) * page.pageSize + category_index + 1 }</td>
      <td class="h25 tl">${category.name?if_exists}</td>
      <td class="h25 tc">${category.parentName?if_exists}</td>
      <td class="h25 tl">
        <a href="javascript:;" onclick="openCategoryEditDialog(${category.id?if_exists});">修改</a>
        |&nbsp;<a href="javascript:;" onclick="return deleteIt('${category.id?if_exists}');">删除</a>
        <#if queryMap.parentId?exists && !queryMap.queryName?exists>
        <#if ((page.currentPage -1) * page.pageSize + category_index > 0)>
        |&nbsp;<a href="#" onclick="return up(${(page.currentPage - 1) * page.pageSize + category_index + 1 })">上移</a>
        </#if>
        <#if ((page.currentPage -1) * page.pageSize + category_index + 1 < page.recordCount)>
        |&nbsp;<a href="#" onclick="return down(${(page.currentPage - 1) * page.pageSize + category_index + 1 })">下移</a>
        </#if>
        </#if>
      </td>
    </tr>
    </#list>
    <#if categoryList?size != page.pageSize>
    <#list categoryList?size + 1..page.pageSize as i>
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
function sortIt(orderby, descflag) {
    $("#pn").val("1");
    $("#orderby").val(orderby);
    $("#descflag").val(descflag);
    $("#form1").submit();
}

function deleteIt(id) {
    var result = window.confirm("您确认要删除吗？");
    if (!result) {
        return false;
    }
    var url = "categoryDelete.htm?id=" + id;
  	$.post(url, function(data) {
  		alert(data.msg);
        window.location.reload();
    }, "json");
}

function deleteSelect() {
	var ids = getCheckBoxSelectValue("chkId");
    if (ids == "") {
        alert("请选择删除对象！");
        return false;
    }

    var result = window.confirm("您确认要删除所选吗？");
    if (!result) {
        return false;
    }
    var url = "categoryDeleteSelect.htm?ids=" + ids;
  	$.post(url, function(data) {
  	    alert(data.msg);
  	    window.location.reload();
    }, "json");
}

$.ajaxSetup ({
    cache: false
});

function openCategoryEditDialog(id) {
    $("#popWindow").load("categoryEditDialog.htm?id=" + id + "&parentId=${queryMap.parentId?if_exists}", function() {
        $('#categoryEditDialog').modal({backdrop:false,keyboard:true,show:true},'show');
	});
}

function up(sno) {
    $("#sno").val(sno);
    $("#form1").attr("action","categoryUp.htm");
    $("#form1").submit();
}

function down(sno) {
    $("#sno").val(sno);
    $("#form1").attr("action","categoryDown.htm");
    $("#form1").submit();
}
</script>
</body>
</html>