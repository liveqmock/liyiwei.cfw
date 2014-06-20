<#--
 ****************************************************
 * Created on 2013-01-09 17:57:45
 * @author Liyiwei
 * 
 ****************************************************/
-->
<div class="modal hide" id="departmentEditDialog">
  <div class="modal-header">   
    <button type="button" class="close" data-dismiss="modal">&times;</button>
    <i class="icon-pencil"></i><strong> <a href="javascript:;" onclick="openDepartmentEditDialog(${department.id?if_exists});" class="black"><#if !department.id?exists>新建<#else>修改</#if>部门</a></strong>
  </div> 
  <div class="modal-body">
    <form name="departmentEditForm" id="departmentEditForm" action="departmentSave.htm" method="post">
    <input type="hidden" name="id" value="${department.id?if_exists}"/>
    <input type="hidden" name="seq" value="${department.seq?if_exists}"/>
    <input type="hidden" id="parentId" name="parentId" value="${department.parentId?if_exists}"/>
    <table class="table-condensed">
      <tr>
        <td class="td-left">上级部门：&nbsp;</td>
        <td class="td-right">
	        <#if !department.id?exists>
	        <div class="selectBox">
	          <span class="selectTxt">${department.parentName?if_exists}</span>
	          <div class="option">
	            <div id="tree" class="ztree"></div>
	          </div>
	        </div>
	        <#else>
	        ${department.parentName?if_exists}
	        </#if>
        </td>
      </tr>
      <tr>
        <td class="td-left"><span class="red"> * </span>部门名称：&nbsp;</td>
        <td class="td-right"><input type="text" name="name" id="name" class="span3" maxlength="60" value="${department.name?if_exists}"></td>
      </tr>
      <tr>
        <td class="td-left">部门编码：&nbsp;</td>
        <td class="td-right"><input type="text" name="code" id="code" class="span3" maxlength="20" value="${department.code?if_exists}"></td>
      </tr>
      <tr>
        <td class="td-left">描述：&nbsp;</td>
        <td class="td-right"><textarea name="note" id="note" class="w300 h100">${department.note?if_exists}</textarea></td>
      </tr>
    </table>
    </form>
  </div>
  <div class="modal-footer">
    <button type="button" class="btn" data-dismiss="modal"><i class="icon-remove"></i>　关　闭　</button>&nbsp;
    <button type="button" id="submitBtn" class="btn btn-primary"><i class="icon-ok icon-white"></i>　提　交　</button>
  </div>
</div>
<script>
var setting = {
    view: {	
        dblClickExpand: false
	},
    data: {
        simpleData: {enable: true}
    },
    callback: {
		onClick: onClick
	}
};

var zNodes =${departmentTree?if_exists};

function onClick(e, treeId, treeNode) {
    $(".selectTxt").text(treeNode.name);
    $("#parentId").val(treeNode.id);
}

$(document).ready(function() {
    <#if !department.id?exists>
    $.fn.zTree.init($("#tree"), setting, zNodes);

    var treeObj = $.fn.zTree.getZTreeObj("tree");
    var node = treeObj.getNodeByParam("id", ${department.parentId?default(0)}, null);
    treeObj.selectNode(node,false);
    
	$(".selectBox").click(function(event) {
		event.stopPropagation();
		$(this).find(".option").toggle();
		$(this).parent().siblings().find(".option").hide();
	});
	
	$(document).click(function(event) {
		var e = $(event.target);
		if ($(".selectBox").is(":visible") && e.attr("class") != "option"	&& !e.parent(".option").length) {
			$('.option').hide();
		}	
	});
	</#if>
});


$(document).ready(function() {
	$("#submitBtn").click(function() {
		if (checkData()) {
			$("#departmentEditForm").submit();
		}
	});
});


function checkData() {
    if (!checkNull("#name", "名称"))
		return false;
				
	if (!checkMax("#note", 500,"描述"))
		return false;
	
	return true;
}
</script>