<#--
 ****************************************************
 * Created on 2013-12-10 14:55:00
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>数据查询工具</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/jquery/jquery.cookie.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
</head>

<body>
<form name="form1" id="form1" action="sqlTools.htm" method="post">
<input type="hidden" id="opt" name="opt"/>
<input type="hidden" id="tableName" name="tableName"/>
<div class="container-fluid">
  <span class="red">☆☆☆ 数据工具增强版V2.1 ☆☆☆</span>　　<font size=1>Made by Liyiwei</font>
  <table class="table table-bordered table-condensed">
    <tr> 
      <td width="10%" class="tr">URL：</td>
      <td width="89%"> 
        <select name="database" id="database">
		  <option value="mysql"<#if database='mysql'> selected="selected"</#if>>mysql</option>
		  <option value="oracle"<#if database='oracle'> selected="selected"</#if>>oracle</option>
		</select>
        <input type="text" name="url" id="url" class="span5" maxlength="200" value="${url?if_exists}">
        &nbsp;&nbsp;&nbsp;用户名： <input type="text" name="user" id="user" class="input-small" maxlength="20" value="${user?if_exists}">
        &nbsp;&nbsp;&nbsp;密码：  <input type="password" name="password" id="password" class="input-small" maxlength="20" value="${password?if_exists}">
      </td>
    </tr>
    <tr> 
      <td class="tr">SQL ： </td>
      <td><textarea id="sql" name="sql" cols="155" rows="6">${sql?if_exists}</textarea></td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
      <td>
        <input type="button" name="exec" value="执行[E]" accesskey="e" onClick="submitIt('exesql');">
        &nbsp;<input type="button" name="table" value="显示所有表[A]" accesskey="a" onClick="submitIt('allTable');">
                     　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　　
        <a href="#" onclick="showLayer();">历史记录</a>
      </td>
    </tr>
  </table>
  <div class="modal hide" id="layer" style="margin:-67px -100px 0 0;left: 40%;">
    <div class="modal-header">   
      <button type="button" class="close" data-dismiss="modal">&times;</button>
      <strong>历史记录</strong>
    </div> 
    <div class="modal-body">
      <table class="table-condensed">
        ${history?if_exists}
      </table>
    </div>
  </div>
  ${content?if_exists}
</div>
<script>
$(document).ready(function() {
    if($.cookie("user", { path: '/' })) {
        $("#url").val($.cookie("url", { path: '/' }));
        $("#user").val($.cookie("user", { path: '/' }));
        $("#password").val($.cookie("pwd", { path: '/' }));
        $("#database").val($.cookie("database", { path: '/' }));
    }
});

function setCookie() {
    $.cookie("user",$("#user").val(), { path: '/' });
    $.cookie("pwd",$("#password").val(), { path: '/' });
    $.cookie("url",$("#url").val(), { path: '/' });
    $.cookie("database",$("#database").val(), { path: '/' });
}        

function submitIt(opt) {
    setCookie();
    $("#opt").val(opt);
    $("#form1").submit();
}

function queryIt(opt,tableName) {
    setCookie();
    $("#opt").val(opt);
    $("#tableName").val(tableName);
    if(opt == 'exesql') {
        $("#sql").val("select * from " + tableName);
    }   
    $("#form1").submit();
}

function showLayer() {
     $('#layer').modal({backdrop:false,keyboard:true,show:true},'show');
}

function replaceSql(val) {
    var newStr = "";
    
    for (i = 0; i <= val.length - 1; i++){ 
        if(val.charAt(i) == '~'){
            newStr += "'";
        }
        else if(val.charAt(i) == '^'){
            newStr += "\"";
        }
        else if(val.charAt(i) == '$'){
            newStr += "\n";
        }
        else if(val.charAt(i) == '#'){
            newStr += "\r";
        }
        else{
            newStr += val.charAt(i);
        }   
    }
    $("#sql").val(newStr);
    $('#layer').modal('hide')
}
</script>
</form>
</body>
</html>