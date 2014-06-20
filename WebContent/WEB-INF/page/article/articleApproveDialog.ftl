<#--
 ****************************************************
 * Created on 2013-03-25 20:25:00
 * @author Liyiwei
 * 
 ****************************************************/
-->
<div class="modal hide" id="articleApproveDialog">
  <div class="modal-header">   
    <button type="button" class="close" data-dismiss="modal">&times;</button>
    <i class="icon-pencil"></i><strong> 审批意见</strong>
  </div> 
  <div class="modal-body">
    <form name="articleApproveForm" id="articleApproveForm" action="articleApproveSave.htm" method="post">
    <input type="hidden" name="ids" value="${ids?if_exists}"/>
    <input type="hidden" name="approveStatus" value="${approveStatus?if_exists}"/>
    <input type="hidden" name="chId" value="${channelId?if_exists}"/>
    <table class="table-condensed">
      <tr>
        <td class="td-left">审批意见：&nbsp;</td>
        <td class="td-right"><textarea name="approveNote" id="approveNote" rows="6" cols="70"></textarea></td>
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
			$("#articleApproveForm").submit();
		}
	});
});

function checkData() {
	if (!checkMax("#approveNote", 200, "审批意见"))
		return false;
	
	return true;
}
</script>