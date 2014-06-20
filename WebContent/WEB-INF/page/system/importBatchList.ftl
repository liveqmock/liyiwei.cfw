<#--
 ****************************************************
 * Created on 2013-04-27 15:09:15
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>查看导入记录</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
<script src="../widget/My97DatePicker/WdatePicker.js"></script>
</head>

<body>
<form name="form1" id="form1" action="importBatchList.htm" method="post">
<input type="hidden" id="pn" name="pn" value="${page.currentPage?if_exists}"/>
<input type="hidden" id="orderby" name="orderby" value="importBatch.id"/>
<input type="hidden" id="descflag" name="descflag"/>
<div class="titlebar"><i class="icon-th-large"></i> <a href="importBatchList.htm">查看导入记录</a></div>
<div class="container-fluid">
  <div class="searchbar">
    &nbsp;导入模块：<input type="text" name="queryModule" id="queryModule" class="input-normal" value="${queryMap.queryModule?if_exists}"/>
    &nbsp;<button type="button" id="queryIt" class="btn"><i class="icon-search"></i> 查  询 </button>
  </div>
  <table class="table-page">
    <tr>
      <td></td>
      <td width="70%" align="right"><#include "../common/page.ftl"></td>
    </tr>
  </table>
  <table class="table table-bordered table-condensed">
    <thead>
    <tr>
      <th width="30">序号</th>
      <th>导入模块</th>
      <th>导入文件名</th>
      <th>总条数</th>
      <th>成功条数</th>
      <th>失败条数</th>
      <th>导入人</th>
      <th>导入时间</th>
    </tr>
    </thead>
    <tbody>
    <#list importBatchList as importBatch>
    <tr>
      <td class="h25 tc">${(page.currentPage - 1) * page.pageSize + importBatch_index + 1 }</td>
      <td class="h25 tc"><a href="importBatchList.htm?queryModule=${importBatch.module?if_exists}" class="black">${importBatch.module?if_exists}</a></td>
      <td class="h25 tc">${importBatch.filename?if_exists}</td>
      <td class="h25 tc">${importBatch.totalCount?if_exists}</td>
      <td class="h25 tc">${importBatch.finishCount?if_exists}</td>
      <td class="h25 tc">${importBatch.failCount?if_exists}</td>
      <td class="h25 tc">${importBatch.importUserName?if_exists}</td>
      <td class="h25 tc">${importBatch.importTimeName?if_exists}</td>
    </tr>
    </#list>
    <#if importBatchList?size != page.pageSize>
    <#list importBatchList?size + 1..page.pageSize as i>
    <tr>
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
    var url = "importBatchDelete.htm?id=" + id;
  	$.post(url, function(data) {
  		alert(data.msg);
        window.location.reload();
    }, "json");
}
</script>
</body>
</html>