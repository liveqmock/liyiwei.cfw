<#--
 ****************************************************
 * Created on 2013-03-13 16:32:35
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>文章列表</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
<script src="../widget/My97DatePicker/WdatePicker.js"></script>
</head>

<body>
<form name="form1" id="form1" action="articleList.htm" method="post">
<input type="hidden" id="pn" name="pn" value="${page.currentPage?if_exists}"/>
<input type="hidden" id="orderby" name="orderby" value="article.modifyTime"/>
<input type="hidden" id="descflag" name="descflag" value="desc"/>
<input type="hidden" name="channelId" value="${queryMap.channelId?if_exists}"/>
<div class="titlebar"><i class="icon-th-large"></i> <a href="articleList.htm">文章管理</a></div>
<div class="container-fluid">
  <div class="searchbar">
           标题：<input type="text" name="queryTitle" id="queryTitle" class="input-small" value="${queryMap.queryTitle?if_exists}"/>
    &nbsp;内容：<input type="text" name="queryContent" id="queryContent" class="input-small" value="${queryMap.queryContent?if_exists}"/>
    &nbsp;状态：${statusSelectBox?if_exists}
    &nbsp;审批状态：${approveStatusSelectBox?if_exists}
    &nbsp;修改用户：${modifyUserSelectBox?if_exists}
    <button type="button" id="queryIt" class="btn"><i class="icon-search"></i> 查  询 </button>
  </div>
  <table class="table-page">
    <tr>
      <td>
        <a class="btn" href="articleEdit.htm?channelId=${queryMap.channelId?if_exists}"><i class="icon-plus"></i> 新建文章</a>
        <button type="button" class="btn" onclick="return deleteSelect();"><i class="icon-trash"></i> 删除</button>
      </td>
      <td width="70%" align="right"><#include "../common/page.ftl"></td>
    </tr>
  </table>
  <table class="table table-bordered table-condensed">
    <thead>
    <tr>
      <th width="20"><input type="checkbox" name="chkAll" id="chkAll" onclick='checkBoxSelectAll("chkAll","chkId");'></th>
      <th width="30">序号</th>
      <th>频道</th>
      <th>${sortCol("title","标题",orderby,descflag)}</th>
      <th>${sortCol("status","状态",orderby,descflag)}</th>
      <th>审批状态</th>
      <th>修改用户</th>
      <th>修改时间</th>
      <th width="150">操作</th>
    </tr>
    </thead>
    <tbody>
    <#list articleList as article>
    <tr>
      <td class="h25 tc"><input type="checkbox" name="chkId" value="${article.id?if_exists}"/></td>
      <td class="h25 tc">${(page.currentPage - 1) * page.pageSize + article_index + 1}</td>
      <td class="h25 tc">${article.channelName?if_exists}</td>
      <td class="h25 tl"><a href="articleView.htm?id=${article.id?if_exists}">${article.titleBrief?if_exists}</a></td>
      <td class="h25 tc">${article.statusName?if_exists}</td>
      <td class="h25 tc">${article.approveStatusName?if_exists}</td>
      <td class="h25 tc">${article.modifyUserName?if_exists}</td>
      <td class="h25 tc">${article.modifyTimeName?if_exists}</td>
      <td class="h25 tc">
        <a href="articleEdit.htm?id=${article.id?if_exists}">修改</a>
        |&nbsp;<a href="${article.id?if_exists}.htm" target="_blank">预览</a>
        |&nbsp;<a href="javascript:;" onclick="return deleteIt('${article.id?if_exists}');">删除</a>
        <#if article.status == 1>
        |&nbsp;<a href="javascript:;" onclick="return statusSet('${article.id?if_exists}',0);">失效</a>
        <#else>
        |&nbsp;<a href="javascript:;" onclick="return statusSet('${article.id?if_exists}',1);">生效</a>
        </#if>
      </td>
    </tr>
    </#list>
    <#if articleList?size != page.pageSize>
    <#list articleList?size + 1..page.pageSize as i>
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
    var url = "articleDelete.htm?id=" + id;
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
    var url = "articleDeleteSelect.htm?ids=" + ids;
  	$.post(url, function(data) {
  	    alert(data.msg);
  	    window.location.reload();
    }, "json");
}

function statusSet(id,status) {
    var info = status == 1?"生效":"失效"; 
    var result = window.confirm("您确认要" + info + "吗？");
    if (!result) {
        return false; 
    }
    var url = "articleStatusSet.htm?id=" + id + "&status=" + status;
  	$.post(url, function(data) {
  		alert(data.msg);
  		window.location.reload();
    }, "json");
}
</script>
</body>
</html>