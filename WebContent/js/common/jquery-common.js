/*******************************************************************************
 * 
 * 作者： 李一伟 编写日期：2012-04-29
 * 
 ******************************************************************************/

/* 检查文本框是否为空 */
function checkNull(name, message) {
	var obj = $(name);
	if ($.trim(obj.val()) == "") {
		window.alert(message + "不能为空！");
		obj.focus();
		return false;
	}
	return true;
}

/* 检查下拉框是否被选中 */
function checkSelectBox(name, message) {
	var obj = $(name);
	if ($.trim(obj.val()) == "") {
		window.alert("请选择" + message + "！");
		obj.focus();
		obj.select();
		return false;
	}
	return true;
}

/* 检查复选框是否被选中 */
function checkCheckBox(name, message) {
	var i = $("input[name='" + name + "']:checked").length;
	if (i == 0) {
		window.alert("请选择" + message + "！");
		$("input[name='" + name + "']:first").focus();
		return false;
	}
	return true;
}

/* 检查单选钮是否被选中 */
function checkRadio(name, message) {
	return checkCheckBox(name, message);
}

/* 检查输入值是否少于指定长度 */
function checkMin(name, length, message) {
	var obj = $(name);
	if ($.trim(obj.val() != "")) {
		if (obj.val().length < length) {
			window.alert(message + "最小长度不能少于" + length + "个字！");
			obj.focus();
			return false;
		}
	}
	return true;
}

/* 检查输入值是否超过指定长度 */
function checkMax(name, length, message) {
	var obj = $(name);
	if ($.trim(obj.val() != "")) {
		if (obj.val().length > length) {
			window.alert(message + "最大长度不能超过" + length + "个字！");
			obj.focus();
			return false;
		}
	}
	return true;
}

/* 检查输入值是否为数字 */
function checkNum(name, message) {
	var obj = $(name);
	if ($.trim(obj.val() != "")) {
		if (isNaN(obj.val())) {
			window.alert(message + "必须是数字！");
			obj.focus();
			return false;
		}
	}
	return true;
}

/* 检查输入值是否为合法的Email */
function checkEmail(name) {
	var obj = $(name);
	if ($.trim(obj.val()) != "") {
		if (!isEmail(obj.val())) {
			window.alert("请输入正确的Email邮箱！");
			obj.focus();
			return false;
		}
	}
	return true;
}

/* 检查密码是否相同 */
function checkPassword(name1, name2) {
	var obj = $(name1);
	if ($.trim(obj.val()) != $.trim($(name2).val())) {
		window.alert("请确认您两次输入的密码是否一致!");
		obj.focus();
		return false;
	}
	return true;
}

/* 复选框全选功能 */
function checkBoxSelectAll(name1, name2) {
	if ($("input[name='" + name1 + "']").attr("checked") == "checked") {
		$("input[name='" + name2 + "']").each(function() {
			$(this).attr("checked", true);
		});
	} else {
		$("input[name='" + name2 + "']").each(function() {
			$(this).attr("checked", false);
		});
	}
}

/* 返回复选框以逗号分隔的选中值 */
function getCheckBoxSelectValue(name) {
	var value = [];
	var i = 0;
	$("input[name*=" + name + "]:checked").each(function() {
		value[i] = this.value;
		i++;
	});
	return value.join(',');
}

/* 设置复选框单选钮选中值 */
function setCheckedValue(name, value) {
	$("input[name='" + name + "']").attr("checked", '" + value + "');
}

/* 检查是否为合法的Email */
function isEmail(email) {
	var regex = /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	return regex.test(email);
}

function changeBackColor() {
	$("td").hover(function() {
		$(this).addclass("ECECF4");
	}, function() {
		$(this).removeclass("hover");
	});
}

/* 隐藏或显示块 */
function toggle(name) {
	$(name).toggle();
}