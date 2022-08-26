$(document).ready(function() {
	var appUserId = sessionStorage.getItem("app_user_login"); // 当前登录的app用户的id
	getAppUserInfo(appUserId);
	// 图片操作
	$('body').on('mouseover', '.simg', function() {
		$(this).siblings('.simg1').css("display", "block");
	});
	$('body').on('mouseout', '.simg', function() {
		$(this).siblings('.simg1').css("display", "none");
	});

	//	// 左上方标题
	//	$('.page span').text(sessionStorage.getItem('conte'));
	// 修改直播
	$('body').on('click', '.userupdate', function() {
		var id = getnum(this, 0);
		var lsName = getnum(this, 1);
		var lsParh = getnum(this, 2);
		var lsPwd = getnum(this, 3);
		var lookPath = getnum(this, 4);
		$('.zb_id').val(id);
		$('.lsName').val(lsName);
		$('.lsParh').val(lsParh);
		$('.lsPwd').val(lsPwd);
		$('.lookPath').val(lookPath);
		$(".banDel").show();
	});
	// 添加直播信息

	$("#add_live").click(function() {
		var id = $('.zb_id').val(null);
		var lsName = $('.lsName').val(null);
		var lsParh = $('.lsParh').val(null);
		var lsPwd = $('.lsPwd').val(null);
		var lookPath = $('.lookPath').val(null);
		$(".banDel").show();
	});
	// 修改添加直播信息
	$("#qx_yes").click(function() {
		var id = $('.zb_id').val();
		var lsName = $('.lsName').val();
		var lsParh = $('.lsParh').val();
		var lsPwd = $('.lsPwd').val();
		var lookPath = $('.lookPath').val();
		czzb(id, lsName, lsParh, lsPwd, lookPath, appUserId);
	});
	$(".close").click(function() {
		$(".banDel").hide();
	});
	$(".no").click(function() {
		$(".banDel").hide();
	});
	// 管理员密码
	$('#user_push').click(function() {
		var upwd = $('#user_pwd').val();
		if(upwd == 'userkbadmin') {
			$('#server_Div').css('display', 'none');
			$('#pageAll').css('display', 'block');
		} else {
			alert('密码错误！');
		}
	});

	// 删除对应的直播
	$('body').on('click', '.updataPwd', function() {
		$('#up_pwd').css('display', 'block');
		var id = getnum(this, 0);
		$('.hidden_appUserId').val(id);
		//		var newPwd = $('.new_pwd').val();
		//		alert(newPwd)
	});
	// 删除对应的直播
	$("#pwd_yes").click(function() {
		var id = $('.hidden_appUserId').val();
		alert(id);
		$.ajax({
			type: "post",
			url: getRequestIp() + "delLiveStream",
			async: true,
			data: {
				'id': id
			},
			dataType: "json",
			success: function(data) {
				if(data.success) {
					alert("删除成功！");
					$(".banDel").hide();
					getAppUserInfo(appUserId);
				}
			}
		});
	});
});
// 添加修改直播信息
function czzb(id, lsName, lsParh, lsPwd, lookPath, appUserId) {
	$.ajax({
		type: "post",
		url: getRequestIp() + "addLiveStream",
		async: true,
		data: {
			'id': id,
			'lsName': lsName,
			'lsParh': lsParh,
			'lsPwd': lsPwd,
			'lookPath': lookPath,
			'appUserId': appUserId
		},
		dataType: "json",
		success: function(data) {
			if(data.success) {
				alert("操作成功！");
				$(".banDel").hide();
				getAppUserInfo(appUserId);
			}
		}
	});
}
// 获取app用户直播数据
function getAppUserInfo(appUserId) {
	$.ajax({
		type: "post",
		url: getRequestIp() + "getLiveStream",
		async: true,
		data: {
			'appUserId': appUserId
		},
		dataType: "json",
		success: function(data) {
			getAppUser(data.list);
		}
	});

}
// 获取对象的某个值
function getnum(obj, num) {
	return $(obj).parent().parent().children().eq(num).text();
}

// 渲染用户直播数据
function getAppUser(data) {
	$('.user_tab tr td').remove();
	$('.user_tr').remove();
	var item;
	$.each(data, function(i, result) {
		var timesjc = result.createDate;
		var dataTime = new Date(timesjc);
		var year = dataTime.getFullYear();
		var moth = dataTime.getMonth() + 1;
		if(moth < 10) {
			moth = "0" + moth;
		}
		var day = dataTime.getDate();
		if(day < 10) {
			day = "0" + day;
		}
		var time = year + "-" + moth + "-" + day;
		item += '<tr class="user_tr">'
		item += '<td class="uid">' + result.id + '</td>'
		item += '<td class="show_lsName">' + result.lsName + '</td>'
		item += '<td class="show_lsParh">' + result.lsParh + '</td>'
		item += '<td class="show_lsPwd">' + result.lsPwd + '</td>'
		item += '<td class="show_lookPath">' + result.lookPath + '</td>'
		item += '<td class="show_time">' + time + '</td>'
		item += '<td>'
		item += '<img class="userupdate" src="img/update.png" title="修改" />&nbsp;&nbsp;'
		item += '<img class="updataPwd" src="img/delete.png" title="删除" />&nbsp;&nbsp;'
		item += '</td>'
		item += '<td style="display: none;"></td>'
		item += '</tr>';
	});
	$('.user_tab').append(item);
}