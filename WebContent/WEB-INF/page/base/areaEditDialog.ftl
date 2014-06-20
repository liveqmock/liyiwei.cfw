<#--
 ****************************************************
 * Created on 2013-12-24 13:58:15
 * @author Liyiwei
 * 
 ****************************************************/
-->
<div class="modal hide" id="areaEditDialog">
  <div class="modal-header">   
    <button type="button" class="close" data-dismiss="modal">&times;</button>
    <i class="icon-pencil"></i><strong> <a href="javascript:;" onclick="openAreaEditDialog(${area.id?if_exists});" class="black"><#if !area.id?exists>新建<#else>修改</#if>地区</a></strong>
  </div>
  <div class="modal-body">
    <form name="areaEditForm" id="areaEditForm" action="areaSave.htm" method="post">
    <input type="hidden" name="id" value="${area.id?if_exists}"/>
    <table class="table-condensed">
      <tr>
        <td class="td-left"><label class="Validform_label"><span class="red"> * </span>地区名称：&nbsp;</label></td>
        <td class="td-right"><input type="text" name="name" id="name" class="span3" maxlength="100" value="${area.name?if_exists}"></td>
      </tr>
      <tr>
        <td class="td-left"><label class="Validform_label"><span class="red"> * </span>地区编码：&nbsp;</label></td>
        <td class="td-right"><input type="text" name="code" id="code" class="span3" maxlength="12" value="${area.code?if_exists}"></td>
      </tr>
      <tr>
        <td class="td-left"><label class="Validform_label"><span class="red"> * </span>类型：&nbsp;</label></td>
        <td class="td-right">${area.typeSelectBox?if_exists}</td>
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
            $("#areaEditForm").submit();
        }
    });
});

function checkData() {
    if (!checkNull("#name", "地区名称"))
        return false;
		
    if (!checkNull("#code", "地区编码"))
        return false;
		
    if (!checkSelectBox("#type", "类型"))
        return false;

    return true;
}
</script>