$(document).ready(function() {
	var uname = sessionStorage.getItem("uname_login");
	if(uname != null && uname != "") {
		$('.sp_uname').text(uname);
	}

	$('.quit_s').click(function() {
		if(confirm('确定退出吗？')) {
			sessionStorage.setItem("uname_login", ""); // 把用户信息设为空
			sessionStorage.setItem("uname_loginout", 1); // 退出登录的时候的验证信息
			$.ajax({
				type: "post",
				url: getRequestIp() + "loginout",
				async: true,
				data: {},
				dataType: "json",
				success: function(data) {
					if(data.success) {
						alert('退出成功！');
					}
				}
			});
		}

	});
});