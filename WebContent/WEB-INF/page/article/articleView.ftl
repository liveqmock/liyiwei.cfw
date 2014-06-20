<#--
 ****************************************************
 * Created on 2013-03-13 16:32:35
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8" />
<title>查看文章</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">
<link rel="stylesheet" href="../css/common.css"/>
</head>

<body>
<div class="titlebar"><i class="icon-search"></i> <a href="articleList.htm">文章管理</a> > <a href="javascript:;" onclick="window.location.reload();">查看文章</a></div>
<div class="container-fluid">
<table class="table table-bordered">
  <tr>
    <th height="20" align="center" colspan="2"><strong>文章信息</strong></td>
  </tr>
  <tr>
    <td class="td-left2">频道：&nbsp;</td>
    <td class="td-right2">${article.channelName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">标题：&nbsp;</td>
    <td class="td-right2">${article.title?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">子标题：&nbsp;</td>
    <td class="td-right2">${article.subTitle?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">摘要：&nbsp;</td>
    <td class="td-right2">${article.brief?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">缩略图：&nbsp;</td>
    <td class="td-right2"><#if article.smallPic?exists && article.smallPic != ""><img src="../${rootPath?default('upload')}/${article.smallPic?if_exists}"></#if></td>
  </tr>
  <tr>
    <td class="td-left2">内容：&nbsp;</td>
    <td class="td-right2">${article.content?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">作者：&nbsp;</td>
    <td class="td-right2">${article.author?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">标签：&nbsp;</td>
    <td class="td-right2">${article.tag?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">来源：&nbsp;</td>
    <td class="td-right2">${article.source?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">状态：&nbsp;</td>
    <td class="td-right2">${article.statusName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">查看次数：&nbsp;</td>
    <td class="td-right2">${article.viewCount?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">审批状态：&nbsp;</td>
    <td class="td-right2">${article.approveStatusName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">审批人：&nbsp;</td>
    <td class="td-right2">${article.approveUserName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">审批时间：&nbsp;</td>
    <td class="td-right2">${article.approveTimeName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">审批意见：&nbsp;</td>
    <td class="td-right2">${article.approveNote?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">创建用户：&nbsp;</td>
    <td class="td-right2">${article.createUserName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">创建时间：&nbsp;</td>
    <td class="td-right2">${article.createTimeName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">修改用户：&nbsp;</td>
    <td class="td-right2">${article.modifyUserName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">修改时间：&nbsp;</td>
    <td class="td-right2">${article.modifyTimeName?if_exists}</td>
  </tr>
  <tr>
    <td class="td-left2">&nbsp;</td>
    <td class="td-right2"><button type="button" class="btn" onclick="history.go(-1);"><i class="icon-share-alt"></i>　返　回　</button></td>
  </tr>
</table>
</div>
</body>
</html>