<#--
 ****************************************************
 * Created on 2012-11-30 14:59:00
 * @author Liyiwei
 * 
 ****************************************************/
-->
<!doctype html>
<html>
<head>
<meta charset="utf-8"/>
<title>menu</title>
<link id="bs-css" href="../widget/charisma/css/bootstrap-spacelab.css" rel="stylesheet">

<link href="../css/common.css" rel="stylesheet">
<script src="../js/jquery/jquery-1.7.1.js"></script>
<script src="../widget/charisma/js/bootstrap.js"></script>
<style type="text/css">
  a{color:#000;text-decoration:none;}
  a:hover, a:active{color:#000;text-decoration:none;background:linear-gradient(#fff, #e5e5e5);}
  a:focus{color: #000;}
  .scroll{background:#e3edf9;}
  .scrollfont{color:#176192;}
  .choose{background:#e3edf9;}
  .sidebar-nav{padding:9px 0;PADDING-BOTTOM: 0px; MIN-HEIGHT: 0px; MARGIN-BOTTOM: 0px;-webkit-box-shadow: 0 0 10px #BDBDBD; -moz-box-shadow: 0 0 10px #BDBDBD; box-shadow: 0 0 10px #BDBDBD;}
  .bgmenu{background-color:#f3f3f3;position: absolute; left: 0; top: 0; bottom: 0;overflow-x:hidden;width: 219px;  border-right: 1px solid #ccc;}
  .separator{height:0;line-height:0;_font-size:0;border-top:#ccc 1px solid;border-bottom:#fff 1px solid;overflow:hidden;margin:5px auto}
</style>  
</head>

<body>
  <div class="row-fluid bgmenu">
    <div class="main-menu-span">
	  <div class="nav-collapse sidebar-nav">
		<div class="accordion nav nav-tabs" id="accordion">
		  <div class="nav-header">菜单</div>
		  <div class="separator"></div>	
		  <#list treeList as menuMap>
          <div class="accordion-group">
            <div class="accordion-heading">
              <div class="accordion-toggle" data-toggle="collapse" data-parent="#accordion" href="#menu${menuMap.menu.id?if_exists}"><i class="${menuMap.menu.icon?default('icon-th-list')}"></i> <strong>${menuMap.menu.name?if_exists}</strong></div>
            </div>
            <div id="menu${menuMap.menu.id?if_exists}" class="accordion-body collapse  separator">
            <#list menuMap.menuList as menu>
              <div class="accordion-inner cp" href="${menu.link?if_exists}"><i class="${menu.icon?default('icon-star')} ml10"></i> <span>${menu.name?if_exists}</span></div>
            </#list>
            </div>
          </div>
          </#list>
        </div>
      </div>
	</div>     
  </div>
<script>
function show(url) {
	parent.content.location.href = url;
}

$(".accordion-inner").click(function(){
    $('.choose').removeClass('choose');
  	$(this).addClass('choose');
  	
  	if($(this).attr('href') != undefined){
  	    show($(this).attr('href'));
  	}
});

$(".accordion-inner").mouseover(function(){
  	$(this).addClass('scroll');
  	$(this).find('span').addClass('scrollfont');
});

$(".accordion-inner").mouseout(function(){
    $(this).removeClass('scroll');
    $(this).find('span').removeClass('scrollfont');
});
</script>             
</body>
</html>