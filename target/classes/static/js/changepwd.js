function checkpwd1() {
	var user = document.getElementById('pwd1').value.trim();
	var userId = sessionStorage.getItem("user_login"); // 当前登录的用户的id
	$.ajax({
		type : "post",
		url : getRequestIp() + "queryLogin",
		data : {
			'userId':userId
		},
		dataType : "json",
		async : true,
		success : function(data) {
			var uname = data.userPwd;
			var uname1 = $('#pwd1').val();
			$('.pwd_hi').val(data.id);
			if (uname == uname1) {
				$("#pwd1").parent().find(".imga").show();
				$("#pwd1").parent().find(".imgb").hide();
			} else {
				$("#pwd1").parent().find(".imgb").show();
				$("#pwd1").parent().find(".imga").hide();
				$("#pwd1").focus();
			}

		}
	});

}

function checkpwd2() {
	var user = document.getElementById('pwd2').value.trim();
	if (user.length >= 5 && user.length <= 12) {
		$("#pwd2").parent().find(".imga").show();
		$("#pwd2").parent().find(".imgb").hide();
	} else {
		$("#pwd2").parent().find(".imgb").show();
		$("#pwd2").parent().find(".imga").hide();
	}
}

function checkpwd3() {
	var user = document.getElementById('pwd3').value.trim();
	var pwd = document.getElementById('pwd2').value.trim();
	if (user.length >= 5 && user.length <= 12 && user == pwd) {
		$("#pwd3").parent().find(".imga").show();
		$("#pwd3").parent().find(".imgb").hide();
	} else {
		$("#pwd3").parent().find(".imgb").show();
		$("#pwd3").parent().find(".imga").hide();
	}
}

$(document).ready(function() {
	$('.btn_yes').click(function() {
		var unameId = $('.pwd_hi').val();
		var pwd = $('#pwd2').val();
		if (unameId.length > 0 && pwd.length > 0) {
			$.ajax({
				type : "post",
				url : getRequestIp() + "modUser",
				data : {
					'id' : unameId,
					'userPwd' : pwd
				},
				dataType : "json",
				async : true,
				success : function(data) {
					if (data.success) {
						alert('修改成功!');
						sessionStorage.setItem("uname", "");
						sessionStorage.setItem("uname_loginout", 1); // 退出登录的时候的验证信息
					}
				}
			});
		} else {
			alert('请输入密码!');
		}
		
	});
});