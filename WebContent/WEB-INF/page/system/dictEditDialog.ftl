<#--
 ****************************************************
 * Created on 2013-02-06 16:45:33
 * @author Liyiwei
 * 
 ****************************************************/
-->
<div class="modal hide" id="dictEditDialog">
  <div class="modal-header">   
    <button type="button" class="close" data-dismiss="modal">&times;</button>
    <i class="icon-pencil"></i><strong> <#if !dict.id?exists>新建<#else>修改</#if>数据字典</strong>
  </div> 
  <div class="modal-body">
    <form name="dictEditForm" id="dictEditForm" action="dictSave.htm" method="post">
    <input type="hidden" name="id" value="${dict.id?if_exists}"/>
    <table class="table-condensed">
      <tr>
        <td class="td-left"><label class="Validform_label"><#if !dict.id?exists><span class="red"> * </span></#if>表：&nbsp;</label></td>
        <td class="td-right"><#if !dict.id?exists><input type="text" name="tableName" id="tableName" class="span3" maxlength="40" value="${dict.tableName?if_exists}"><#else><input type="hidden" name="tableName" id="tableName" value="${dict.tableName?if_exists}">${dict.tableName?if_exists}</#if></td>
      </tr>
      <tr>
        <td class="td-left"><label class="Validform_label"><#if !dict.id?exists><span class="red"> * </span></#if>字段：&nbsp;</label></td>
        <td class="td-right"><#if !dict.id?exists><input type="text" name="fieldName" id="fieldName" class="span3" maxlength="40" value="${dict.fieldName?if_exists}"><#else><input type="hidden" name="fieldName" id="fieldName" value="${dict.fieldName?if_exists}">${dict.fieldName?if_exists}</#if></td>
      </tr>
      <tr>
        <td class="td-left"><label class="Validform_label"><#if !dict.id?exists><span class="red"> * </span></#if>值：&nbsp;</label></td>
        <td class="td-right"><#if !dict.id?exists><input type="text" name="value" id="value" class="span3" maxlength="40" value="${dict.value?if_exists}"><#else><input type="hidden" name="value" id="value" value="${dict.value?if_exists}">${dict.value?if_exists}</#if></td>
      </tr>
      <tr>
        <td class="td-left"><label class="Validform_label"><span class="red"> * </span>含义：&nbsp;</label></td>
        <td class="td-right"><input type="text" name="meaning" id="meaning" class="span3" maxlength="40" value="${dict.meaning?if_exists}"></td>
      </tr>
      <tr>
        <td class="td-left"><label class="Validform_label">说明：&nbsp;</label></td>
        <td class="td-right"><textarea name="note" id="note" class="w300 h100">${dict.note?if_exists}</textarea></td>
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
		    <#if !dict.id?exists>
		    isExist();
		    <#else>
		    $("#dictEditForm").submit();
		    </#if>
		}
	});
});

function checkData() {
    <#if !dict.id?exists>
    if (!checkNull("#tableName", "表"))
		return false;
				
    if (!checkNull("#fieldName", "字段"))
		return false;
				
    if (!checkNull("#value", "值"))
		return false;

	</#if>
    if (!checkNull("#meaning", "含义"))
		return false;
				
	if (!checkMax("#note", 255, "说明"))
		return false;
	
	return true;
}

function isExist() {
	var url = "isExist.htm";
  	$.post(url, {tableName:$("#tableName").val(),fieldName:$("#fieldName").val(),value:$("#value").val()},function(data) {
  	    if(data.isExist){
  		    alert(data.msg);
  		    return false;
  		} else {
  		    $("#dictEditForm").submit();
  		}
    }, "json");
}
</script>