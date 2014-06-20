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
<title>角色权限设置</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css"/>
<link rel="stylesheet" href="../widget/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/zTree/js/jquery.ztree.core-3.5.js"></script>
<script src="../widget/zTree/js/jquery.ztree.excheck-3.5.js"></script>
</head>

<body>
<form name="form1" id="form1" action="rolePrivilegeSave.htm" method="post">
<input type="hidden" name="roleId" value="${role.id?if_exists}"/>
<input type="hidden" id="privileges" name="privileges" value=""/>
<div class="titlebar"><i class="icon-edit"></i> <a href="roleList.htm">角色管理</a> > <a href="javascript:;" onclick="window.location.reload();">角色权限设置</a></div>
<div class="container-fluid">
<table class="table table-condensed table-bordered">
  <thead>
  <tr>
    <th height="25" align="center" colspan="2"><strong>角色权限设置</strong></td>
  </tr>
  </thead>
  <tbody>
  <tr>
    <td class="td-left"><label class="Validform_label">角色名称：&nbsp;</label></td>
    <td class="td-right">${role.name?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left"><label class="Validform_label">权限：&nbsp;</label></td>
    <td class="td-right"><div id="tree" class="ztree"></div></td>
  </tr>
  <tr>
    <td class="td-left">&nbsp;</td>
    <td class="td-right">
      <button type="button" id="submitBtn" class="btn btn-primary"><i class="icon-ok icon-white"></i>　提　交　</button>&nbsp;
      <button type="button" class="btn" onclick="history.go(-1);"><i class="icon-share-alt"></i>　返　回　</button>
    </td>
  </tr>
  </tbody>
</table>
</div>
</form>
<script>
var setting = {
    check: {enable: true}, data: {simpleData: {enable: true}}
};
		
var zNodes =${privilegeTree?if_exists};

$(document).ready(function(){
    $.fn.zTree.init($("#tree"), setting, zNodes);
});

$(document).ready(function() {
    var treeObj = $.fn.zTree.getZTreeObj("tree");
    <#list rolePrivilegeList as rolePrivilege>
    treeObj.checkNode(treeObj.getNodeByParam("id", ${rolePrivilege.privilegeId?if_exists}, null),true,false);
    </#list>
    
    $("#submitBtn").click(function() {
        var treeObj = $.fn.zTree.getZTreeObj("tree");
    	var nodes = treeObj.getCheckedNodes(true);
		v = "";
		nodes.sort(function compare(a,b){return a.id-b.id;});
		for (var i=0, l=nodes.length; i<l; i++) {
			v += nodes[i].id + ",";
		}
    	if (v.length > 0 ) v = v.substring(0, v.length-1);
    	$("#privileges").val(v);
        $("#form1").submit();
    });
});
</script>
</body>
</html>