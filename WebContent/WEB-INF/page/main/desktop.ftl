<#--
 ****************************************************
 * Created on 2013-03-11 15:21:00
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>桌面</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link href="../css/common.css" rel="stylesheet">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
<script src="../js/common/bootstrap-common.js"></script>
</head>

<body>
<div class="titlebar"><i class="icon-th-large"></i> <a href="desktop.htm">桌面</a></div>
<div class="container-fluid">
  <div class="row-fluid">
    <div class="box span12">
	  <div class="box-content">
	    <div class="fl ml10 mr30"><img src="../images/computer.png"></div>
	    <div>
		  <p>${username?if_exists}，欢迎使用通用框架系统3.0！</p>
		  <p>今天是 <strong>${today?if_exists}</strong></p>
		  <p>上次登录时间：<strong>${_lastVisitTime?if_exists}</strong></p>
		  <p>上次登录IP：<strong>${_lastIp?if_exists}</strong></p>
		  <p>&nbsp;<#if _lastIp?exists && _lastIp != _user.lastIp><span class="red b">注意！该用户上次登录IP与本次登录IP不同，本次登录IP：${_user.lastIp?if_exists}</span></#if></p>
		</div>
	  </div>
	</div>
  </div>
		
  <div class="row-fluid mt10">
    <div class="box span4">
      <div class="box-header well">
        <h2><i class="icon-th"></i> <a href="../article/list.htm?channelId=1" class="darkblue" target="_blank">公司公告</a></h2>
	  </div>
	  <div class="box-content">
	    <ul id="articleGonggaoList" class="dashboard-list">
		</ul>
	  </div>
    </div>
    
    <div class="box span4">
      <div class="box-header well">
        <h2><i class="icon-th"></i> <a href="../article/list.htm?channelId=2" class="darkblue" target="_blank">行业资讯</a></h2>
	  </div>
	  <div class="box-content">
	    <ul id="articleInfoList" class="dashboard-list">
		</ul>
	  </div>
    </div>
    
    <div class="box span4" style="margin-left:20px;">
      <div class="box-header well">
        <h2><i class="icon-info-sign"></i> 关于本系统</h2>
	  </div>
	  <div class="box-content">
	    <ul class="dashboard-list">
		  <li>Common Framework 3.0 星河工作室出品</li>
		  <li>制作人：李一伟，QQ：10420123</li>
		  <li><#if _licenseName?exists>本软件授权给${_licenseName?if_exists}使用<#else>&nbsp;</#if></li>
		  <li><#if _licenseRegisterDate?exists>注册日期：${_licenseRegisterDate?if_exists?string('yyyy-MM-dd')}<#else>&nbsp;</#if></li>
		  <li><#if _licensePermitDays?exists>授权期限：<#if _licensePermitDays !=0 >${_licensePermitDays?if_exists}天<#else>永久</#if><#else>&nbsp;</#if></li>
		</ul>
	  </div>
    </div>
  </div>
</div>  
</body>
<script>
$(document).ready(function(){
    articleGonggaoList();
    articleInfoList();
});

function articleGonggaoList(){
    var url = "../article/articleGonggaoList.htm?num=5";
    $.post(url, function(data) {
  	    $("#articleGonggaoList").html(data.msg);
    }, "json");
}

function articleInfoList(){
    var url = "../article/articleInfoList.htm?num=5";
    $.post(url, function(data) {
  	    $("#articleInfoList").html(data.msg);
    }, "json");
}
</script>
</html>