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
<title>缓存列表</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
<script src="../widget/My97DatePicker/WdatePicker.js"></script>
</head>

<body>
<form name="form1" id="form1" action="cacheList.htm" method="post">
<div class="titlebar"><i class="icon-th-large"></i> <a href="cacheList.htm">缓存管理</a></div>
<div class="container-fluid">
  <div class="searchbar">
    &nbsp;Key：<input type="text" name="queryKey" id="queryKey" class="input-normal" value="${queryKey?if_exists}"/>
    &nbsp;<button type="button" id="queryIt" class="btn"><i class="icon-search"></i> 查  询 </button>
  </div>
  <table class="table-page">
    <tr>
      <td>
        <button type="button" class="btn" onclick="return deleteAll();"><i class="icon-trash"></i> 清除全部</button>
        <button type="button" class="btn" onclick="return deleteSelect();"><i class="icon-trash"></i> 清除</button>
      </td>
    </tr>
  </table>
  <table class="table table-bordered table-condensed">
    <thead>
    <tr>
      <th width="20"><input type="checkbox" name="chkAll" id="chkAll" onclick='checkBoxSelectAll("chkAll","chkId");'></th>
      <th width="30">序号</th>
      <th>Key</th>
      <th>Object</th>
      <th width="100">操作</th>
    </tr>
    </thead>
    <tbody>
    <#list cacheList as cacheMap>
    <tr>
      <td class="h25 tc"><input type="checkbox" name="chkId" value="${cacheMap.key?if_exists}"/></td>
      <td class="h25 tc">${cacheMap_index + 1 }</td>
      <td class="h25 tl">${cacheMap.key?if_exists}</td>
      <td class="h25 tl">${cacheMap.object?if_exists}</td>
      <td class="h25 tc">
        <a href="javascript:;" onclick="return deleteIt('${cacheMap.key?if_exists}');">清除</a>
      </td>
    </tr>
    </#list>
    </tbody>
  </table>
</div>
</form>
<script>
$(document).ready(function(){
	$("#queryIt").click(function(){
        $("#form1").submit();
	});
});


function deleteIt(key) {
    var result = window.confirm("您确认要清除吗？");
    if (!result) {
        return false;
    }
    var url = "cacheDelete.htm?key=" + key;
  	$.post(url, function(data) {
  		alert(data.msg);
        window.location.reload();
    }, "json");
}

function deleteSelect() {
	var ids = getCheckBoxSelectValue("chkId");
    if (ids == "") {
        alert("请选择清除对象！");
        return false;
    }

    var result = window.confirm("您确认要清除所选吗？");
    if (!result) {
        return false;
    }
    var url = "cacheDeleteSelect.htm?keys=" + ids;
  	$.post(url, function(data) {
  	    alert(data.msg);
  	    window.location.reload();
    }, "json");
}

function deleteAll() {
    var result = window.confirm("您确认要清除吗？");
    if (!result) {
        return false;
    }
    var url = "cacheDeleteAll.htm";
  	$.post(url, function(data) {
  		alert(data.msg);
        window.location.reload();
    }, "json");
}
</script>
</body>
</html>