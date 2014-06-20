/*******************************************************************************
 * 
 * 程序功能：通用JAVASCRIPT函数库 作者： 李一伟 编写日期：2012-04-07
 * 
 ******************************************************************************/

/* 检查文本框是否为空 */
function checkNull(name, message) {
	if (name.value == "") {
		window.alert(message + "不能为空！");
		name.focus();
		return false;
	}
	return true;
}

/* 检查下拉框是否被选中 */
function checkSelectBox(name, message) {
	if (name[0].selected == true) {
		window.alert(message + "不能为空！");
		name.focus();
		name.select();
		return false;
	}
	return true;
}

/* 检查复选框是否被选中 */
function checkCheckBox(name, message) {
	var array = document.getElementsByName(name);
	for ( var i = 0; i < array.length; i++) {
		if (array[i].checked) {
			return true;
		}
	}
	window.alert(message + "不能为空！");
	array[0].focus();
	return false;
}

/* 检查单选钮是否被选中 */
function checkRadio(name, message) {
	return checkCheckBox(name, message);
}

/* 检查输入值是否少于指定长度 */
function checkMin(name, length, message) {
	if (name.value != "") {
		if (name.value.length < length) {
			window.alert(message + "最小长度不能少于" + length + "个字！");
			name.focus();
			return false;
		}
	}
	return true;
}

/* 检查输入值是否超过指定长度 */
function checkMax(name, length, message) {
	if (name.value != "") {
		if (name.value.length > length) {
			window.alert(message + "最大长度不能超过" + length + "个字！");
			name.focus();
			return false;
		}
	}
	return true;
}

/* 检查输入值是否为数字 */
function checkNum(name, message) {
	if (name.value != "") {
		if (isNaN(name.value)) {
			window.alert(message + "必须是数字！");
			name.focus();
			return false;
		}
	}
	return true;
}

/* 检查输入值是否为合法的Email */
function checkEmail(name) {
	if (name.value != "") {
		if (!isEmail(name.value)) {
			window.alert("请输入正确的Email邮箱！");
			name.focus();
			return false;
		}
	}
	return true;
}

/* 检查密码是否相同 */
function checkPassword(name1, name2) {
	if (name1.value != name2.value) {
		window.alert("请确认您两次输入的密码密码是否一致!");
		name1.focus();
		return false;
	}
	return true;
}

/* 复选框全选功能 */
function checkBoxSelectAll(name1, name2) {
	var array1 = document.getElementsByName(name1);
	var array2 = document.getElementsByName(name2);
	if (array1[0].checked) {
		for ( var i = 0; i < array2.length; i++) {
			array2[i].checked = true;
		}
	} else {
		for ( var i = 0; i < array2.length; i++) {
			array2[i].checked = false;
		}
	}
}

/* 返回复选框以逗号分隔的选中值 */
function getCheckBoxSelectValue(name) {
	var value = [];
	var array = document.getElementsByName(name);
	for ( var i = 0, j = 0; i < array.length; i++) {
		if (array[i].checked) {
			value[j] = array[i].value;
			j++;
		}
	}
	return value.join(',');
}

/* 设置复选框单选钮选中值 */
function setCheckedValue(name, value) {
	var array = document.getElementsByName(name);
	for ( var i = 0; i < array.length; i++) {
		if (array[i].value == value) {
			array[i].checked = true;
		} else {
			array[i].checked = false;
		}
	}
}

/* 检查是否为合法的Email */
function isEmail(email) {
	var regex = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	return regex.test(email);
}

/* 隐藏或显示块 */
function hide(id, hide) {
	var target = document.getElementById(id);
	if (hide) {
		target.style.display = "none";
	} else {
		target.style.display = "block";
	}
}