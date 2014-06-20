/*******************************************************************************
 * 
 * 程序功能：屏蔽右键拷贝 作者： 李一伟 编写日期：2012-04-07
 * 
 ******************************************************************************/
document.oncontextmenu = mousemenu;
document.oncopy = nocopy;
document.onselectstart = nocopy;
	
window.onerror = killErrors;
	
function mousemenu() {
	window.event.cancelBubble = true;
	window.event.returnValue=false;
	return false;
}
	
function nocopy() {
	window.event.returnValue=false;
	return false;
}
	
function killErrors() {
   	return true;
}   