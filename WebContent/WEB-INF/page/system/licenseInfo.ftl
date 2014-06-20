<#--
 ****************************************************
 * Created on 2014-04-10 18:24:00
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>软件注册信息</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link href="../css/common.css" rel="stylesheet">
</head>

<body>
<div class="container-fluid">
<div class="center">
  <div class="login-panel" style="width: 600px;">
    <div class="panel-head1"><h5>软件注册信息</h5></div>
    <div class="m30">
      <div style="margin:40px 0px 40px 0px;"><h4 style="vertical-align:bottom;"><img src="../images/message.png"/>&nbsp;<#if license?exists>软件已经超出使用期，请联系技术人员重新获取授权码进行注册！<#else>软件尚未注册，请联系技术人员获取授权码进行注册！</#if></h4></div>
      <div class="ml30 mtb10 gray"><a class="btn btn-primary" onclick="self.location='../system/licenseRegister.htm'">　注　册　</a>&nbsp;<a class="btn" onclick="self.location='../main/login.htm'">　登　录　</a></div>     
    </div>
  </div>
</div>
</div>
</body>
</html>