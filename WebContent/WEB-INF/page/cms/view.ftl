<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>${article.title?if_exists}</title>
<link rel="stylesheet" type="text/css" href="../widget/cms/css/cms.css">
<script src="../js/jquery/jquery-1.7.1.js"></script>
</head>

<body>
<#include "header.ftl">
<div id="wp" class="wp">
  <div class="w-920" id="ct">
    <div class="w-long z ie6-shousi">
      <div class="neirong">
        <div class="h view-h">
          <h1 class="ph xs5">${article.title?if_exists}</h1>
          <div class="other">
            <span class="author-name">作者：${article.modifyUserName?if_exists} </span>
            <span class="author-name">${article.modifyTimeName?if_exists}</span>
            <span class="author-name">查看次数(${article.viewCount?if_exists})</span>
            <#if article.tag?exists && article.tag != ""><span class="author-name">TAG：${article.tag?if_exists}</span></#if>
          </div>
        </div>
        <div class="d">
          <table cellpadding="0" cellspacing="0"  class="vwtb">
            <tbody>
              <tr>
                <td id="article_content">
                <#if article.smallPic?exists && article.smallPic != ""><span class="span-img"><img src="../${rootPath?default('upload')}/${article.smallPic?if_exists}" alt="${article.title?if_exists}"></span></#if>
                <div>${article.content?if_exists}</div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
        <div class="related-list">
          <h3>您可能感兴趣的文章</h3>
          <div class="bm_c">
            <ul class="xl cl">
              <#list relationArticleList as article>
              <li><a href="${article.id?if_exists}.htm">${article.title?if_exists}</a></li>
              </#list>
            </ul>
          </div>
        </div>
      </div>
    </div>

    <div class="w-short y">
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
  <#include "footer.ftl">
<script>
$(document).ready(function(){
    $('#nv ul li').removeClass('a');
  	<#if article?exists && article.channelId == 1>
  	$('#nv1').addClass('a');
  	<#elseif article?exists && article.channelId == 2>
  	$('#nv2').addClass('a');
  	<#elseif article?exists && article.channelId == 3>
  	$('#nv3').addClass('a');
  	<#elseif article?exists && article.channelId == 4>
  	$('#nv4').addClass('a');
  	<#elseif article?exists && article.channelId == 5>
  	$('#nv5').addClass('a');
  	<#else>
  	$('#nv1').addClass('a');
  	</#if>
});
</script>    
</body>
</html>