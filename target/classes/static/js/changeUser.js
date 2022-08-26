var userId = sessionStorage.getItem("user_login"); // 当前登录的用户的id
$(document).ready(function() {

	// 获取用户信息
	showUserInfo(userId);


	$('.start_img').mouseover(function() {
		$('.start_img1').css('display','block');
	});
	$('.start_img').mouseout(function() {
		$('.start_img1').css('display','none');
	});
	
	$('.wa_img').mouseover(function() {
		$('.wa_img1').css('display','block');
	});
	$('.wa_img').mouseout(function() {
		$('.wa_img1').css('display','none');
	});


	$('.us_img').mouseover(function() {
		$('.us_img1').css('display','block');
	});
	$('.us_img').mouseout(function() {
		$('.us_img1').css('display','none');
	});
	$('.user_wa_img').mouseover(function() {
		$('.user_wa_img1').css('display','block');
	});
	$('.user_wa_img').mouseout(function() {
		$('.user_wa_img1').css('display','none');
	});
	// 把输入框的值设为空
	$('.btn_no').click(function() {
		$('.nick,.show_time,.start_fil,.sFile,.imgFile').val(null);

	});
	// 设置ios微信登录隐藏或显示
	$('#ios').click(function() {
		var flag = $('.ios_flag').val();
		if(flag == 1) {
			$('.ios_flag').val(0);
		} else {
			$('.ios_flag').val(1);
		}
		tj(userId);
	});
	// 提交请求
	$('.btn_yes').click(function() {
		tj(userId);
	});
});
// 提交请求
function tj(userId) {
	$('.user_id').val(userId);
	var form = document.getElementById("form_user3");
	var fo = new FormData(form);
	$.ajax({
		type: "post",
		url: getRequestIp() + "updateUserInfo",
		data: fo,
		dataType: "json",
		async: true,
		processData: false, // 告诉jquery 不要去出入发送的数据
		contentType: false, // 告诉jquery不要去设置content-Type的请求头
		success: function(data) {
			if(data.success) {
				alert('修改成功!');
				showUserInfo(userId);
				//					sessionStorage.setItem("uname", "");
				//					sessionStorage.setItem("uname_loginout", 1); // 退出登录的时候的验证信息
			}
		}
	});
}
// 显示用户信息
function showUserInfo(userId) {
	$.ajax({
		type: "post",
		url: getRequestIp() + "queryLogin",
		async: true,
		data: {
			'userId': userId
		},
		dataType: 'json',
		success: function(data) {
		$('.nick').val(data.nickName) //用户昵称
				$('.start_img').attr('src', data.startPath) //启动图片
				$('.start_img1').attr('src', data.startPath) //启动图片
				$('.wa_img').attr('src', data.watermark) //水印图片
				$('.wa_img1').attr('src', data.watermark) //水印图片
				$('.us_img').attr('src', data.img) //头像
				$('.us_img1').attr('src', data.img) //头像
				$('.user_wa_img').attr('src', data.userWatermark) //用户左上角水印图片
				$('.user_wa_img1').attr('src', data.userWatermark) //用户左上角水印图片
				$('.show_time').val(data.showTime) //显示时间
				$('.ios_flag').val(data.flag); //是否显示ios微信登录  0.隐藏 1.显示
				if(data.flag == 1) {
					$('#ios').text('显示ios微信登录');
				} else {
					$('#ios').text('隐藏ios微信登录');
				}
		}
	});
}