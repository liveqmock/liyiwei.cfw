<#--
 ****************************************************
 * Created on 2013-02-06 16:45:33
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>数据字典列表</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
</head>

<body>
<form name="form1" id="form1" action="dictList.htm" method="post">
<input type="hidden" name="id"/>
<input type="hidden" id="pn" name="pn" value="${page.currentPage?if_exists}"/>
<input type="hidden" name="orderby" value="dict.id"/>
<input type="hidden" name="descflag"/>
<div class="titlebar"><i class="icon-th-large"></i> <a href="dictList.htm">数据字典管理</a></div>
<div class="container-fluid">
  <table class="table-page">
    <tr>
      <td>
        ${queryTableSelectBox?if_exists}&nbsp;${queryFieldSelectBox?if_exists}&nbsp;<button type="button" class="btn" onclick="openDictEditDialog();"><i class="icon-plus"></i> 新建数据字典</a>
      </td>
      <td width="60%" align="right"><#include "../common/page.ftl" ></td>
    </tr>
  </table>
  <table class="table table-bordered table-condensed">
    <thead>
    <tr>
      <th width="30">序号</th>
      <th>表</th>
      <th>字段</th>
      <th>值</th>
      <th>含义</th>
      <th>说明</th>
      <th width="100">操作</th>
    </tr>
    </thead>
    <tbody>
    <#list dictList as dict>
    <tr>
      <td class="h25 tc">${(page.currentPage - 1) * page.pageSize + dict_index + 1 }</td>
      <td class="h25 tc"><a href="dictList.htm?queryTable=${dict.tableName?if_exists}" class="black">${dict.tableName?if_exists}</a></td>
      <td class="h25 tc"><a href="dictList.htm?queryTable=${dict.tableName?if_exists}&queryField=${dict.fieldName?if_exists}" class="black">${dict.fieldName?if_exists}</a></td>
      <td class="h25 tc">${dict.value?if_exists}</td>
      <td class="h25 tl" edit="true" tableId="${dict.id?if_exists}" field="meaning">${dict.meaning?if_exists}</td>
      <td class="h25 tc" edit="true" tableId="${dict.id?if_exists}" field="note">${dict.noteBrief?if_exists}</td>
      <td class="h25 tc">
        <a href="javascript:;" onclick="openDictEditDialog(${dict.id?if_exists});">修改</a>
      </td>
    </tr>
    </#list>
    <#if dictList?size != page.pageSize>
    <#list dictList?size + 1..page.pageSize as i>
    <tr>
      <td class="h25">&nbsp;</td>
      <td class="h25">&nbsp;</td>
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
<div id="popWindow"></div>
<script>
$(document).ready(function(){
	$("#queryTable").change(function(){
		$("#pn").val("1");
		$("#queryField").val("");
        $("#form1").submit();
	});
	
	$("#queryField").change(function(){
        $("#pn").val("1");
        $("#form1").submit();
    });
    
    var t = $('td[edit=true]');
	t.click(tdClick);
});
     
function queryIt() {
    document.form1.pn.value = "1";
    document.form1.submit();
}

function sortIt(orderby,descflag) {
    document.form1.pn.value = "1";
    document.form1.orderby.value = orderby;
    document.form1.descflag.value = descflag;
    document.form1.submit();
}

$.ajaxSetup ({
    cache: false
});

function openDictEditDialog(id) {
	$("#popWindow").load("dictEditDialog.htm",{id: id,tableName: $("#queryTable").val(),fieldName:$("#queryField").val()},function() {
	    $('#dictEditDialog').modal({backdrop:false,keyboard:true,show:true},'show');
	});
}

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
	    var url = "dictSet.htm";
	  	$.post(url, {id: td.attr("tableId"),field: td.attr("field"),value:val}, function(data) {
	  	    td.addClass("editFlag");
	  		//alert(data.msg);
	  		//window.location.reload();
	    }, "json");
	}    
}
</script>
</body>
</html>