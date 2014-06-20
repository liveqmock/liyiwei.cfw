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
<title>${title?if_exists}</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link href="../css/common.css" rel="stylesheet">
</head>

<body>
<div class="center">
  <div class="login-panel">
    <div class="panel-head1"><h5>${title?if_exists}</h5></div>
    <div class="m30">
      <div style="margin:40px 30px 40px 30px;"><h4 style="vertical-align:bottom;"><img src="../images/message.png"/>&nbsp;${msg?if_exists}</h4></div>
      <div class="ml30 mtb10 gray"><a class="btn" onclick="history.go(-1);">　返　回　</a>&nbsp;<a class="btn btn-primary" onclick="self.location='../main/login.htm'">　登　录　</a></div>     
    </div>
  </div>
</div>
</body>
</html>