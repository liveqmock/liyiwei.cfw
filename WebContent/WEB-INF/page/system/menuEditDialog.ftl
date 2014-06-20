<#--
 ****************************************************
 * Created on 2013-02-18 15:20:24
 * @author Liyiwei
 * 
 ****************************************************/
-->
<div class="modal hide" id="menuEditDialog">
  <div class="modal-header">   
    <button type="button" class="close" data-dismiss="modal">&times;</button>
    <i class="icon-pencil"></i><strong> <#if !menu.id?exists>新建<#else>修改</#if>菜单</strong>
  </div>
  <div class="modal-body">
    <form name="menuEditForm" id="menuEditForm" action="menuSave.htm" method="post">
    <input type="hidden" name="id" value="${menu.id?if_exists}"/>
    <input type="hidden" name="seq" value="${menu.seq?if_exists}"/>
    <input type="hidden" id="parentId" name="parentId" value="${menu.parentId?if_exists}"/>
    <table class="table-condensed">
      <tr>
        <td class="td-left">上级菜单：&nbsp;</td>
        <td class="td-right">
	        <#if !menu.id?exists>
	        <div class="selectBox">
	          <span class="selectTxt">${menu.parentName?if_exists}</span>
	          <div class="option">
	            <div id="tree" class="ztree"></div>
	          </div>
	        </div>
	        <#else>
	        ${menu.parentName?if_exists}
	        </#if>
        </td>
      </tr>
      <tr>
        <td class="td-left"><label class="Validform_label"><span class="red"> * </span>名称：&nbsp;</label></td>
        <td class="td-right"><input type="text" name="name" id="name" class="span3" maxlength="40" value="${menu.name?if_exists}"></td>
      </tr>
      <tr>
        <td class="td-left"><label class="Validform_label">图标：&nbsp;</label></td>
        <td class="td-right"><input type="text" name="icon" id="icon" class="span5" maxlength="60" value="${menu.icon?if_exists}"></td>
      </tr>
      <tr>
        <td class="td-left"><label class="Validform_label">链接：&nbsp;</label></td>
        <td class="td-right"><input type="text" name="link" id="link" class="span5" maxlength="255" value="${menu.link?if_exists}"></td>
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

var zNodes =${menuTree?if_exists};

function onClick(e, treeId, treeNode) {
    $(".selectTxt").text(treeNode.name);
    $("#parentId").val(treeNode.id);
}

$(document).ready(function() {
    <#if !menu.id?exists>
    $.fn.zTree.init($("#tree"), setting, zNodes);

    var treeObj = $.fn.zTree.getZTreeObj("tree");
    var node = treeObj.getNodeByParam("id", ${menu.parentId?default(0)}, null);
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
            $("#menuEditForm").submit();
        }
    });
});

function checkData() {
    if (!checkNull("#name", "名称"))
        return false;
		
    return true;
}
</script>