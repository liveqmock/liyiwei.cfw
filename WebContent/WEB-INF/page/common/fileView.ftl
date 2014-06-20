<#--
 ****************************************************
 * Created on 2013-12-04 15:07:00
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>文件查看工具</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css">
</head>

<body>
<form name="form1" action="fileView.htm" method="post">
<input type="hidden" name="url"/>
<div class="container-fluid">
  <p><#if parentPath?exists><a href="#" onclick="submitIt('${parentPath?if_exists}');">返回上级目录</a></#if>　　路径：${path?if_exists} <#if count?exists>　　文件数：${count?if_exists}</#if></p>
  <#if isDirectory = '1'>
  <table class="table table-bordered table-condensed">
    <thead>
    <tr>
      <th>文件名</th>
      <th width="100">类型</th>
      <th width="100">大小</th>
      <th width="180">修改时间</th>
    </tr>
    </thead>
    <tbody>
    <#list fileList as file>
    <tr>
      <td class="h25 tl"><a href="#" onclick="submitIt('${file.path?if_exists}');">${file.name?if_exists}</a></td>
      <td class="h25 tc">${file.type?if_exists}</td>
      <td class="h25 tc">${file.size?if_exists}</td>
      <td class="h25 tc">${file.modifyTime?if_exists}</td>
    </tr>
    </#list>
  </table>
  <#else>
    <div class="box">
      <div class="box-content">
        ${content?if_exists}
      </div>
    </div>
  </#if>
</div>
</form>
<script>
function submitIt(url) {
    document.form1.url.value = url;
    document.form1.submit();
}
</script>
</body>
</html>