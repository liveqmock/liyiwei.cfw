<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${channelName?if_exists}</title>
<link rel="stylesheet" type="text/css" href="../widget/cms/css/cms.css">
<link rel="stylesheet" href="../css/common.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
</head>

<body>
<form name="form1" id="form1" action="list.htm" method="post">
<input type="hidden" id="pn" name="pn" value="${page.currentPage?if_exists}"/>
<input type="hidden" name="channelId" value="${channelId?if_exists}"/>
<#include "header.ftl">
<div id="wp" class="wp">
  <div class="clear"></div>
  <div class="w920">
    <div class="z subleft">
      <#list articleList as article>
      <div class="article-list">
        <#if article.smallPic?exists && article.smallPic != ""><div class="z article-list-img"><a class="a-img" href="${article.id?if_exists}.htm" target="_blank"><img src="../${rootPath?default('upload')}/${article.smallPic?if_exists}" alt="${article.title?if_exists}" title="${article.title?if_exists}"></a></div></#if>
        <div class="article-list-content">
          <ul class="title-top">
            <li class="title-c"><a href="${article.id?if_exists}.htm" target="_blank">${article.title?if_exists}</a> </li>
            <li><span>${article.modifyUserName?if_exists}</span>&nbsp;&nbsp;<span class="f-c">发表于</span>&nbsp;&nbsp;<span class="fc09">${article.modifyTimeName?if_exists}</span></li>
            <li class="summary">${article.briefBrief?if_exists}</li>
          </ul>
          <ul class="comment">
            <li><span class="pinglun">查看次数(${article.viewCount?if_exists})</span><#if article.tag?exists && article.tag != ""><span> TAG：${article.tag?if_exists}</span></#if></li>
          </ul>
        </div>
        <div class="clear"></div>
      </div>
      </#list>
      <br/>
      <#include "page.ftl">
    </div>

    <div class="y subright">
      <div class="tx-list b-rmwz">
        <h2 class="t-h">热门文章</h2>
        <#list hotArticleList as article>
        <div class="tx-box">
          <h2 class="t-h2"><a href="${article.id?if_exists}.htm" title="${article.title?if_exists}">${article.title?if_exists}</a></h2>
          <p class="t-sub"><a href="${article.id?if_exists}.htm">${article.briefBrief?if_exists}</a></p>
          <p class="other"><span class="s-more">查看次数(${article.viewCount?if_exists})&nbsp;&nbsp;${article.modifyTimeName?if_exists}</span></p>
        </div>
        </#list>
      </div>
    </div>
    <div class="cl"></div>
  </div>
  </form>
  <#include "footer.ftl">
<script>
$(document).ready(function(){
    $('#nv ul li').removeClass('a');
  	<#if channelId?exists && channelId == 1>
  	$('#nv1').addClass('a');
  	<#elseif channelId?exists && channelId == 2>
  	$('#nv2').addClass('a');
  	<#elseif channelId?exists && channelId == 3>
  	$('#nv3').addClass('a');
  	<#elseif channelId?exists && channelId == 4>
  	$('#nv4').addClass('a');
  	<#elseif channelId?exists && channelId == 5>
  	$('#nv5').addClass('a');
  	<#else>
  	$('#nv1').addClass('a');
  	</#if>
});
</script>  
</body>
</html>