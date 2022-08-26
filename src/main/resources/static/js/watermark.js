$(document).ready(function() {
	var appUserId = sessionStorage.getItem("app_user_login"); // 当前登录的app用户的id

	// 获得用户的对应信息
	getUserInfo(appUserId);

	$('.llf').change(function() {
		uploadImg("leftFile");
	});

	$('.rrf').change(function() {
		uploadImg("rightFile");
	});
	$('.iif').change(function() {
		uploadImg("imgFile");
	});
	
	
	$('.suol_img').mouseover(function() {
		$('.suol_img1').css('display','block');
	});
	$('.suol_img').mouseout(function() {
		$('.suol_img1').css('display','none');
	});
	
	$('.shuiyin_img').mouseover(function() {
		$('.shuiyin_img1').css('display','block');
	});
	$('.shuiyin_img').mouseout(function() {
		$('.shuiyin_img1').css('display','none');
	});
	// 点击取消的时候把选中的值清空

	$('.no').click(function() {
		$('.llf').val(null);
		$('.rrf').val(null);
		$('.iif').val(null);
		$('.app_user_name').val(null);
	});
	$('#qx_yes').click(function() {
		var leftFile = $('.leftFile').val();
		var rightFile = $('.rightFile').val();
		var imgFile = $('.imgFile').val();
		var userName = $('.app_user_name').val();
		$.ajax({
			type: "post",
			url: getRequestIp() + "addWatermark",
			async: true,
			data: {
				'leftFile': leftFile,
				'rightFile': rightFile,
				'imgFile': imgFile,
				'appUserId': appUserId,
				'userName': userName

			},
			dataType: 'json',
			success: function(data) {
				if(data.success) {
					alert('设置成功！');
					getUserInfo(appUserId);
				}
			}
		});
	});
});
// 获得用户的对应信息
function getUserInfo(appUserId) {

	$.ajax({
		type: "post",
		url: getRequestIp() + "getInfoBySession",
		async: true,
		data: {
			'id': appUserId
		},
		dataType: 'json',
		success: function(data) {
			if(data.success) {
				var syImg2 = data.list[0].syImg2; // 水印图片
				$('.shuiyin_img').attr("src", syImg2);
				$('.shuiyin_img1').attr("src", syImg2);
				var sltImg = data.list[0].sltImg; // 缩略图片
				$('.suol_img').attr("src", sltImg);
				$('.suol_img1').attr("src", sltImg);
				$('.app_user_name').val(data.list[0].userName); //用户昵称
			} else {
				alert("获取错误！")
			}
		}
	});
}

function uploadImg(inputId) {
	var form = document.getElementById(inputId);
	var fo = new FormData(form);
	$.ajax({
		type: "post",
		url: getRequestIp() + "uploadFile",
		data: fo,
		dataType: "json",
		async: true,
		processData: false, // 告诉jquery 不要去出入发送的数据
		contentType: false, // 告诉jquery不要去设置content-Type的请求头
		success: function(data) {
			if(inputId == 'leftFile') {
				// 是左上角水印图片
				$('.leftFile').val(data[0]);
			}
			if(inputId == 'rightFile') {
				// 是左上角水印图片
				$('.rightFile').val(data[0]);
			}
			if(inputId == 'imgFile') {
				// 是左上角水印图片
				$('.imgFile').val(data[0]);
			}
		}
	});
}