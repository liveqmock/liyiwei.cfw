<#--
 ****************************************************
 * Created on 2014-04-10 15:25:00
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>软件注册</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link href="../css/common.css" rel="stylesheet">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../js/common/jquery-common.js"></script>
<script src="../widget/My97DatePicker/WdatePicker.js"></script>
</head>

<body>
<form name="form1" id="form1" action="licenseRegisterAction.htm" method="post">
<div class="container-fluid">
<div class="center">
  <div class="login-panel" style="width: 850px;margin-top:-30px;">
    <div class="panel-head"><h3>软件注册</h3></div>
    <div class="m30">
      <div class="ml30 mt20"><span class="red"> * </span>公司名称：<input type="text" name="name" id="name" class="span4" maxlength="100" value=""></div>
      <div class="ml30 mt20"><span class="red"> * </span>注册日期：<input type="text" name="registerDate" id="registerDate" class="Wdate span2" value="" onClick="WdatePicker();">&nbsp;<span class="red"> * </span>授权天数：<input type="text" name="permitDays" id="permitDays" class="span1" maxlength="5" value=""></div>
      <div class="ml30 mt20"><span class="red"> * </span>授权码　：<input type="text" name="licenseCode" id="licenseCode" size="103" maxlength="255" value=""></div>
      <br/>
      <div class="ml30 mtb10 gray">　　　　　　<a id="submitBtn" class="btn-primary btn" onclick="return submitIt();">　注　册　</a>&nbsp;<a id="return" class="btn" onclick="self.location='../main/login.htm'">　返　回　</a></div>     
    </div>
    <div class="panel-foot"><span class="gray">请与QQ10420123联系获取授权码</span></div>
  </div>
</div>
</div>
</form>
<script>
$(document).ready(function() {
    $("#submitBtn").click(function() {
        if (checkData()) {
            $("#form1").submit();
        }
    });
});

function checkData() {
    if (!checkNull("#name", "公司名称"))
        return false;
		
    if (!checkNull("#registerDate", "注册日期"))
        return false;
		
    if (!checkNull("#permitDays", "授权天数"))
        return false;
		
    if (!checkNum("#permitDays", "授权天数"))
        return false;

    if (!checkNull("#licenseCode", "授权码"))
        return false;
		
    return true;
}
</script>
</body>
</html>