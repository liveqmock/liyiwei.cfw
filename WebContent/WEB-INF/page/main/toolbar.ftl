<#--
 ****************************************************
 * Created on 2012-11-29 16:13:00
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8" />
<title>toolbar</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
</head>

<body>
<div class="topbar">
  <span class="brand">通用框架系统</span>
  <a class="btn fr" href="javascript:exit();"><i class="icon-share-alt"></i> 退出 </a>
  <a class="btn fr" onclick="show('../user/userInfo.htm')"><i class="icon-info-sign"></i> 个人信息 </a>
  <a class="btn fr" onclick="show('../main/desktop.htm')"><i class="icon-home"></i> 桌面 </a>
  <span class="toptext fr"><i class="icon-user icon-white"></i> ${username?if_exists}【${departmentName?if_exists} - ${rolesName?if_exists}】 </span>
</div>
<script>
function show(url) {
	parent.content.location.href = url;
}

function exit() {
	parent.location.replace("../main/login.htm");
}
</script>
</body>
</html>