<#--
 ****************************************************
 * Created on 2013-12-24 13:58:15
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>地区列表</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link href="../css/common.css" rel="stylesheet" >
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
<script src="../widget/My97DatePicker/WdatePicker.js"></script>
</head>

<body>
<form name="form1" id="form1" action="areaList.htm" method="post">
<input type="hidden" id="pn" name="pn" value="${page.currentPage?if_exists}"/>
<input type="hidden" id="orderby" name="orderby" value="area.id"/>
<input type="hidden" id="descflag" name="descflag"/>
<div class="titlebar"><i class="icon-th-large"></i> <a href="areaList.htm">地区管理</a></div>
<div class="container-fluid">
  <div class="searchbar">
    &nbsp;地区名称：<input type="text" name="queryName" id="queryName" class="input-normal" value="${queryMap.queryName?if_exists}"/>
    &nbsp;地区编码：<input type="text" name="queryCode" id="queryCode" class="input-normal" value="${queryMap.queryCode?if_exists}"/>
    &nbsp;类型：${queryMap.queryType?if_exists}
    &nbsp;<button type="button" id="queryIt" class="btn"><i class="icon-search"></i> 查  询 </button>
  </div>
  <table class="table-page">
    <tr>
      <td>
        <button type="button" class="btn" onclick="openAreaEditDialog();"><i class="icon-plus"></i> 新建地区</button>
        <button type="button" class="btn" onclick="return deleteSelect();"><i class="icon-trash"></i> 删除</button>
      </td>
      <td width="70%" align="right"><#include "../common/page.ftl"></td>
    </tr>
  </table>
  <table class="table table-bordered table-condensed">
    <thead>
    <tr>
      <th class="h25 vc" width="20"><input type="checkbox" name="chkAll" id="chkAll" onclick='checkBoxSelectAll("chkAll","chkId");'></th>
      <th width="30">序号</th>
      <th>地区名称</th>
      <th>地区编码</th>
      <th>类型</th>
      <th width="100">操作</th>
    </tr>
    </thead>
    <tbody>
    <#list areaList as area>
    <tr>
      <td class="h25 tc"><input type="checkbox" name="chkId" value="${area.id?if_exists}"/></td>
      <td class="h25 tc">${(page.currentPage - 1) * page.pageSize + area_index + 1 }</td>
      <td class="h25 tc">${area.name?if_exists}</td>
      <td class="h25 tc">${area.code?if_exists}</td>
      <td class="h25 tc">${area.typeName?if_exists}</td>
      <td class="h25 tc">
        <a href="javascript:;" onclick="openAreaEditDialog(${area.id?if_exists});">修改</a>
        |&nbsp;<a href="javascript:;" onclick="return deleteIt('${area.id?if_exists}');">删除</a>
      </td>
    </tr>
    </#list>
    <#if areaList?size != page.pageSize>
    <#list areaList?size + 1..page.pageSize as i>
    <tr>
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
<div id="popWindow"></div>
<script>
$(document).ready(function(){
	$("#queryIt").click(function(){
        $("#pn").val("1");
        $("#form1").submit();
	});
});

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
    var url = "areaDelete.htm?id=" + id;
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
    var url = "areaDeleteSelect.htm?ids=" + ids;
  	$.post(url, function(data) {
  	    alert(data.msg);
  	    window.location.reload();
    }, "json");
}

$.ajaxSetup ({
    cache: false
});

function openAreaEditDialog(id) {
    $("#popWindow").load("areaEditDialog.htm?id=" + id, function() {
        $('#areaEditDialog').modal({backdrop:false,keyboard:true,show:true},'show');
	});
}

</script>
</body>
</html>