<#--
 ****************************************************
 * Created on 2013-02-17 19:33:51
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>查看操作日志</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
<script src="../widget/My97DatePicker/WdatePicker.js"></script>
</head>

<body>
<form name="form1" id="form1" action="actionLogList.htm" method="post">
<input type="hidden" id="id" name="id"/>
<input type="hidden" id="pn" name="pn" value="${page.currentPage?if_exists}"/>
<input type="hidden" id="orderby" name="orderby" value="actionLog.id"/>
<input type="hidden" id="descflag" name="descflag" value="desc"/>
<div class="titlebar"><i class="icon-th-large"></i> <a href="actionLogList.htm">查看操作日志</a></div>
<div class="container-fluid">
  <div class="searchbar">
    &nbsp;系统：<input type="text" name="querySystem" id="querySystem" class="input-small" value="${queryMap.querySystem?if_exists}"/>
    &nbsp;模块：<input type="text" name="queryModule" id="queryModule" class="input-small" value="${queryMap.queryModule?if_exists}"/>
    &nbsp;动作：<input type="text" name="queryAction" id="queryAction" class="input-small" value="${queryMap.queryAction?if_exists}"/>
    &nbsp;操作人：${userSelectBox?if_exists}
    &nbsp;操作时间：<input type="text" name="queryStartLogTime" id="queryStartLogTime" class="input-small Wdate" onClick="WdatePicker();" value="${queryMap.queryStartLogTime?if_exists}"/>
     - <input type="text" name="queryEndLogTime" id="queryEndLogTime" class="input-small Wdate" onClick="WdatePicker();" value="${queryMap.queryEndLogTime?if_exists}"/>
    &nbsp;<button type="button" id="queryIt" class="btn"><i class="icon-search"></i> 查  询 </button>
  </div>
  <table class="table-page">
    <tr>
      <td>
      </td>
      <td width="70%" align="right"><#include "../common/page.ftl"></td>
    </tr>
  </table>
  <table class="table table-bordered table-condensed">
    <thead>
    <tr>
      <th width="30">序号</th>
      <th>系统</th>
      <th>模块</th>
      <th>动作</th>
      <th>操作对象ID</th>
      <th>说明</th>
      <th>IP</th>
      <th>操作人</th>
      <th>操作时间</th>
   </tr>
    </thead>
    <tbody>
    <#list actionLogList as actionLog>
    <tr>
      <td class="h25 tc">${(page.currentPage - 1) * page.pageSize + actionLog_index + 1}</a></td>
      <td class="h25 tc"><a href="actionLogList.htm?querySystem=${actionLog.system?if_exists}" class="black">${actionLog.system?if_exists}</td>
      <td class="h25 tc"><a href="actionLogList.htm?queryModule=${actionLog.module?if_exists}" class="black">${actionLog.module?if_exists}</a></td>
      <td class="h25 tc"><a href="actionLogList.htm?queryAction=${actionLog.action?if_exists}" class="black">${actionLog.action?if_exists}</a></td>
      <td class="h25 tc">${actionLog.objectIdBrief?if_exists}</td>
      <td class="h25 tl">${actionLog.noteBrief?if_exists}</td>
      <td class="h25 tc">${actionLog.ip?if_exists}</td>
      <td class="h25 tc">${actionLog.userName?if_exists}</td>
      <td class="h25 tc">${actionLog.logTimeName?if_exists}</td>
    </tr>
    </#list>
    <#if actionLogList?size != page.pageSize>
    <#list actionLogList?size + 1..page.pageSize as i>
    <tr>
      <td class="h25">&nbsp;</td>
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
    var url = "actionLogDelete.htm?id=" + id;
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
    var url = "actionLogDeleteSelect.htm?ids=" + ids;
  	$.post(url, function(data) {
  	    alert(data.msg);
  	    window.location.reload();
    }, "json");
}

</script>
</body>
</html>