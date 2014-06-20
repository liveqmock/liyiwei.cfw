<#--
 ****************************************************
 * Created on 2013-02-18 15:20:24
 * @author Liyiwei
 * 
 ****************************************************/
-->
<div class="modal hide" id="categoryEditDialog">
  <div class="modal-header">   
    <button type="button" class="close" data-dismiss="modal">&times;</button>
    <i class="icon-pencil"></i><strong> <#if !category.id?exists>新建<#else>修改</#if>分类</strong>
  </div>
  <div class="modal-body">
    <form name="categoryEditForm" id="categoryEditForm" action="categorySave.htm" method="post">
    <input type="hidden" name="id" value="${category.id?if_exists}"/>
    <input type="hidden" name="seq" value="${category.seq?if_exists}"/>
    <input type="hidden" id="parentId" name="parentId" value="${category.parentId?if_exists}"/>
    <table class="table-condensed">
      <tr>
        <td class="td-left">上级分类：&nbsp;</td>
        <td class="td-right">
	        <#if !category.id?exists>
	        <div class="selectBox">
	          <span class="selectTxt">${category.parentName?if_exists}</span>
	          <div class="option">
	            <div id="tree" class="ztree"></div>
	          </div>
	        </div>
	        <#else>
	        ${category.parentName?if_exists}
	        </#if>
        </td>
      </tr>
      <tr>
        <td class="td-left"><label class="Validform_label"><span class="red"> * </span>名称：&nbsp;</label></td>
        <td class="td-right"><input type="text" name="name" id="name" class="span3" maxlength="40" value="${category.name?if_exists}"></td>
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

var zNodes =${categoryTree?if_exists};

function onClick(e, treeId, treeNode) {
    $(".selectTxt").text(treeNode.name);
    $("#parentId").val(treeNode.id);
}

$(document).ready(function() {
    <#if !category.id?exists>
    $.fn.zTree.init($("#tree"), setting, zNodes);

    var treeObj = $.fn.zTree.getZTreeObj("tree");
    var node = treeObj.getNodeByParam("id", ${category.parentId?default(0)}, null);
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
            $("#categoryEditForm").submit();
        }
    });
});

function checkData() {
    if (!checkNull("#name", "名称"))
        return false;
		
    return true;
}
</script>