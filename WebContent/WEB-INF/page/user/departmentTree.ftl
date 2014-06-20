<#--
 ****************************************************
 * Created on 2013-01-11 15:10:00
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>部门树</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<link rel="stylesheet" href="../widget/zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
<script src="../widget/zTree/js/jquery.ztree.core-3.5.js"></script>
</head>
<body  style="position: absolute; left: 0; top: 0; bottom: 0;overflow-x:hidden;width: 219px;border-right: 1px solid #ccc;">
<div class="titlebar"><a href="departmentTree.htm?type=${type?if_exists}"><#if type?if_exists = 1><i class="icon-refresh"></i></#if></a> 组织机构</div>
<div id="tree" class="ztree" style="margin-top:-10px"></div>
<script>
var setting = {
    data: {
        simpleData: {enable: true}
    }
};
var zNodes =${departmentTree?if_exists};

$(document).ready(function(){
    $.fn.zTree.init($("#tree"), setting, zNodes);
});

</script>
</body>
</html>