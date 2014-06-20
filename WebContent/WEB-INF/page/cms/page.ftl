<#--
 ****************************************************
 * Created on 2012-06-13 15:36:00
 * @author Liyiwei
 * 
 ****************************************************/
-->
<#if page?exists>
<div class="page-list">
  <div class="page">
    <#if (page.currentPage <= 1)>
  	<span class="prev">上一页</span>
  	<#else>
  	<a href="javascript:;" class="prev" onclick="goPage(${page.currentPage?if_exists - 1});">上一页</a>
  	</#if>
  	<#assign pre=3/>
    <#assign next=5/>
    <#if (page.pageCount >= pre + next) && (page.currentPage > pre + 1)>
  	  <a href="javascript:;" onclick="goPage(1);">1</a>
  	  <#if (page.currentPage > pre + 2)>
  	  <span>..</span>
  	  </#if>
  	</#if>
  	<#list 1..page.pageCount as i>
  	  <#if (page.pageCount < pre + next) || ((i >= page.currentPage - pre) && (i <= page.currentPage + next))>
  	    <#if (i == page.currentPage)>
  		<span class="current">${i}</span>
  		<#else>
  		<a href="javascript:;" onclick="goPage(${i});">${i}</a>
  		</#if>
  	  </#if>
  	</#list>
  	<#if (page.pageCount >= pre + next) && (page.currentPage < page.pageCount - next)>
  	  <#if (page.currentPage < page.pageCount - next - 1)>
  	  <span>..</span>
  	  </#if>
  	  <a href="javascript:;" onclick="goPage(${page.pageCount?if_exists});">${page.pageCount?if_exists}</a>
  	</#if>
  	<#if (page.currentPage >= page.pageCount)>
  	<span class="next">下一页</span>
  	<#else>
  	<a href="javascript:;" class="next" onclick="goPage(${page.currentPage?if_exists + 1});">下一页</a>
  	</#if>
  </div>	 
</div>
<script>
function goPage(pn) {
	document.${page.formName?if_exists}.${page.pageNoName?if_exists}.value = pn;
	document.${page.formName?if_exists}.submit();
}
</script> 
 </#if>             