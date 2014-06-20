<#--
 ****************************************************
 * Created on 2012-12-04 16:07:00
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>登录</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link href="../css/common.css" rel="stylesheet">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/jquery/jquery.cookie.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
</head>

<body>
<form name="form1" id="form1" action="loginAction.htm" method="post">
<input type="hidden" id="height" name="height"/>
<input type="hidden" id="width" name="width"/>

<div class="center">
  <div class="login-panel">
    <div class="panel-head"><h3>通用框架系统 V2.0</h3></div>
    <div class="panel-content">
      <div>
        <span class="alert alert-error"><span></span></span>
      </div>      
      <div class="input-prepend"><span class="add-on"><i class="icon-user"></i></span><input class="span3" placeholder="用户名" type="text" id="username" name="username" maxlength="20"></div>
      <div class="input-prepend"><span class="add-on"><i class="icon-lock"></i></span><input class="span3" placeholder="密码" type="password" id="password" name="password" maxlength="12"></div>
      <#if validateCodeFlag?exists>
      <div class="input-prepend">
        <span class="add-on"><i class="icon-picture"></i></span><input class="span2" placeholder="验证码" type="text" id="validateCode" name="validateCode" maxlength="4">
        <img src="../system/validateCode.htm" id="validateCodeImage"/>
      </div>
      </#if>
      <div>
        <input type="checkbox" id="remember" name="remember" value="1"> <label for="remember">记住密码</label>　　
        <button id="login" type="button" class="btn-primary btn">　登　录　</button>
      </div>
  </div>
  <div class="panel-foot">
    <span class="ml10 black">Powered By Liyiwei,CopyRight © 2013</span> 
  </div>
</div>
</form>

<script>
$(document).keydown(function(event) {
	if (event.keyCode == 13 || event.keyCode == 10) {
		login();
		event.returnValue = false;
	}
})

$(document).ready(function() {
    if($.cookie("username", { path: '/' })) {
        $("#username").val($.cookie("username", { path: '/' }));
        $("#password").val($.cookie("password", { path: '/' }));
        $("[name=remember]:checkbox").attr("checked",true);
    }
 	
    $("#username").focus();
    $("#height").val(document.documentElement.clientHeight);
	$("#width").val(document.documentElement.clientWidth);
	
	<#if msg?exists && msg != ''>
	$(".alert span").html("${msg?if_exists}");
    $(".alert").show();
	<#else>
	$(".alert").hide();
	</#if>
	
	$("#login").click(function(){
        login();
	});
	
	$("#validateCodeImage").click(function(){
        $("#validateCodeImage").attr('src','../system/validateCode.htm');
	});
});

function login(){
    if($("[name=remember]:checkbox").attr("checked") == "checked") {
        $.cookie("username",$("#username").val(), { path: '/' });
        $.cookie("password",$("#password").val(), { path: '/' });
        $.cookie("remember",$("#remember").val(), { path: '/' });
    } else {
        $.cookie("username",null, { path: '/' });
        $.cookie("password",null, { path: '/' });
        $.cookie("remember",null, { path: '/' });
    }
 
    if (checkData()) {
        $("#form1").submit();
    }
}

function checkData(){
    if (!checkNull("#username", "用户名"))
		return false;
	if (!checkNull("#password", "密码"))
		return false;
	<#if validateCodeFlag?exists>	
	if (!checkNull("#validateCode", "验证码"))
		return false;		
	</#if>	
    return true;
}
</script>
</body>
</html>