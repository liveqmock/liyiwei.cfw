<#--
 ****************************************************
 * Created on 2013-01-15 17:32:33
 * @author Liyiwei
 * 
 ****************************************************/
-->
<div class="modal hide" id="roleEditDialog">
  <div class="modal-header">   
    <button type="button" class="close" data-dismiss="modal">&times;</button>
    <i class="icon-pencil"></i><strong> <#if !role.id?exists>新建<#else>修改</#if>角色</strong>
  </div> 
  <div class="modal-body">
    <form name="roleEditForm" id="roleEditForm" action="roleSave.htm" method="post">
    <input type="hidden" name="id" value="${role.id?if_exists}"/>
    <table class="table-condensed">
      <tr>
        <td class="td-left"><span class="red"> * </span>名称：&nbsp;</td>
        <td class="td-right"><input type="text" name="name" id="name" class="span3" maxlength="40" value="${role.name?if_exists}"></td>
      </tr>
      <tr>
        <td class="td-left">说明：&nbsp;</td>
        <td class="td-right"><textarea name="note" id="note" class="w300 h100">${role.note?if_exists}</textarea></td>
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
$(document).ready(function() {
	$("#submitBtn").click(function() {
		if (checkData()) {
			$("#roleEditForm").submit();
		}
	});
});

function checkData() {
    if (!checkNull("#name", "名称"))
		return false;
				
	if (!checkMax("#note", 500, "说明"))
		return false;
	
	return true;
}
</script>