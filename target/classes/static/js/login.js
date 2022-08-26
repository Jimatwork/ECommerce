$(document).ready(function() {
	$('.logC input').click(function() {

	});

	$('.login_sub').click(function() {
		var form = document.getElementById("login_form");
		var fo = new FormData(form);
		$.ajax({
			type: "post",
			url: getRequestIp() + "login",
			data: fo,
			dataType: "json",
			async: true,
			processData: false, // 告诉jquery 不要去出入发送的数据
			contentType: false, // 告诉jquery不要去设置content-Type的请求头
			success: function(data) {
				if(data.success) {
					var uname = $('#uname').val();
					sessionStorage.setItem("uname_login", uname);
					sessionStorage.setItem("user_login", data.list[0].id);
					sessionStorage.setItem("user_role", data.list[0].userRole);
					sessionStorage.setItem("app_user_login", data.list[0].auId);
					window.location.href = 'index.html';
				} else {
//					sessionStorage.setItem("uname_loginout", 1); // 退出登录的时候的验证信息
					alert('用户名或密码错误！');
				}
			}
		});
	});
});