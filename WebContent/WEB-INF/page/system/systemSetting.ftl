<#--
 ****************************************************
 * Created on 2013-02-06 15:55:50
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>系统设置</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
</head>

<body>
<form name="form1" id="form1" action="systemSetting.htm" method="post">
<input type="hidden" id="id" name="id"/>
<input type="hidden" id="pn" name="pn" value="${page.currentPage?if_exists}"/>
<input type="hidden" id="orderby" name="orderby" value="systemSetting.id"/>
<input type="hidden" id="descflag" name="descflag"/>
<div class="titlebar"><i class="icon-th-large"></i> <a href="systemSetting.htm">系统设置</a></div>
<div class="container-fluid">
  <table class="table-page">
    <tr>
      <td>${categorySelectBox?if_exists}</td>
      <td width="70%" align="right"><#include "../common/page.ftl"></td>
    </tr>
  </table>
  <table class="table table-bordered table-condensed">
    <thead>
    <tr>
      <th width="30">序号</th>
      <th>名称</th>
      <th>值</th>
      <th>说明</th>
      <th>分类</th>
    </tr>
    </thead>
    <tbody>
    <#list systemSettingList as systemSetting>
    <tr>
      <td class="h25 tc">${(page.currentPage - 1) * page.pageSize + systemSetting_index + 1}</td>
      <td class="h25 tl">${systemSetting.name?if_exists}</td>
      <td class="h25 tl" edit="true" tableId="${systemSetting.id?if_exists}" field="value">${systemSetting.value?if_exists}</td>
      <td class="h25 tl" edit="true" tableId="${systemSetting.id?if_exists}" field="note">${systemSetting.noteBrief?if_exists}</td>
      <td class="h25 tc">${systemSetting.category?if_exists}</td>
    </tr>
    </#list>
    <#if systemSettingList?size != page.pageSize>
    <#list systemSettingList?size + 1..page.pageSize as i>
    <tr>
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
<script>
$(document).ready(function() {
	var t = $('td[edit=true]');
	t.click(tdClick);
});

function tdClick() {
	var td = $(this);
	var tdText = td.text();
	td.html("");
	td.css({width: td.width() + "px", height: td.height() + "px", padding: "0", margin: "0"});
	
	if(td.attr("context")){
	    td.append(td.attr("context"));
	    var select = td.find("select");
	    select.find("option").each(function(index) {
	        if($(this).text() == tdText){  
            	$(this).attr("selected", true); 
            }   
		});
	    var oldVal = select.val();
	    select.focus();
	    
	    select.blur(function() {
			var input = $(this);
			var inputVal = input.val();
			if(!inputVal){
			    alert("选择内容不能为空!");
			    var td = input.parent("td");
			    td.html(tdText);
				td.click(tdClick);
			} else {
				var inputText = input.find("option:selected").text();
				var td = input.parent("td");
				tdCommit(inputVal, oldVal, td);
				td.attr("context", td.html());
				td.html(inputText);
				td.click(tdClick);
			}
		});
	} else {
	    var input = $("<input>");
	    input.attr("value", tdText);
		input.css({width: td.width() + "px", height: td.height() + "px", padding: "0", margin: "0"});
		td.append(input);
		input.get(0).select();
		
		input.blur(function() {
			var input = $(this);
			var inputText = input.val();
			var td = input.parent("td");
			tdCommit(inputText, tdText, td);
			td.html(inputText);
			td.click(tdClick);
		});
	}
	td.unbind("click");
};
 
function tdCommit(val,oldVal, td){
    if(val != oldVal) {
	    var url = "systemSettingSet.htm";
	  	$.post(url, {id: td.attr("tableId"),field: td.attr("field"),value:val}, function(data) {
	  	    td.addClass("editFlag");
	  		alert(data.msg);
	  		//window.location.reload();
	    }, "json");
	}    
}

$("#queryCategory").change(function(){
    $("#pn").val("1");
    $("#form1").submit();
});


function queryIt() {
    $("#pn").val("1");
    $("#form1").submit();
}

function sortIt(orderby,descflag) {
    $("#pn").val("1");
    $("#orderby").val(orderby);
    $("#descflag").val(descflag);
    $("#form1").submit();
}
</script>
</body>
</html>