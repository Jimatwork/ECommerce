setInterval("showTime()", "3000");
var appUserId = sessionStorage.getItem("app_user_login"); //当前登录的app用户的id
function showTime() {
	var num = sessionStorage.getItem("menus");
	if(num == 1) {
		getcol();
		sessionStorage.setItem("menus", '');
	}
}

$(document).ready(function() {

	getcol();
	getShoppingClassify();
	getShopId(appUserId);
	$('body').on('click', '.headline,.shopping_all', function() {
		var rname = $(this).children('a').text(); // 一级栏目的名称
		sessionStorage.setItem("ton", $(this).children('p').text()); // 一级栏目的id
		sessionStorage.setItem("ton1", $(this).children('span').text()); // 栏目链接
		sessionStorage.setItem("conte", rname);
		sessionStorage.setItem("search", "");
		sessionStorage.setItem("qery", "");
	})
	$('#quit_sys').click(function() {

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

	var userName = sessionStorage.getItem('uname_login');
	var user_role = sessionStorage.getItem('user_role');
	if(userName == '' || userName == undefined || userName == null) {
		$('.system_log').css('display', 'none');
		sessionStorage.setItem("uname_loginout", 1); // 退出登录的时候的验证信息
		alert('请登录！');
	} else if(user_role != 1) {
		$('.system_log').css('display', 'none');
		$('#vip_user').css('display', 'block');
		$('#vip_user2').css('display', 'block');
		$('#vip_user3').css('display', 'block');
		$('#user_shop').css('display', 'block');
		$('#user_shopping').css('display', 'block');
	}

	if(userName == 'portaladm') {
		$('.system_log').css('display', 'none');
		$('#headline_dl').css('display', 'block');
		$('#hot_dl').css('display', 'block');
		$('#find_dl').css('display', 'block');

	}
});

// 获取商品分类
function getShoppingClassify() {
	$('#user_shopping .headline').remove();
	$.ajax({
		type: "post",
		url: getRequestIp() + "getShopClassify",
		async: true,
		data: {},
		dataType: "json",
		success: function(data) {
			var item1 = '';
			$.each(data.list, function(i, result) {
			
				item1 += '<dd class="headline">'
				item1 += '<img class="coin11" src="img/coin111.png" /><img class="coin22" src="img/coin222.png" />'
				item1 += '<a class="cks" href="myCommodity.html" target="main">' + result.name + '</a>'
				item1 += '<img class="icon5" src="img/coin21.png" />'
				item1 += '<p style="display: none;">' + result.id + '</p>'
				item1 += '</dd>';

			});
			$('#user_shopping').append(item1);
		}
	});
}

function getcol() {
	$('#headline_dl .headline').remove();
	$('#hot_dl .headline').remove();
	$('#find_dl .headline').remove();
	$('#vip_user .headline').remove();
	$.ajax({
		type: "post",
		url: getRequestIp() + "getAllColumns",
		async: true,
		data: {},
		dataType: "json",
		success: function(data) {
			var item1 = '';
			var item2 = '';
			var item3 = '';
			var item4 = '';
			//			var item5 = '';
			$.each(data.list, function(i, result) {
				if(result.colName == '关注') {
					return;
				}
				item1 += '<dd class="headline">'
				item1 += '<img class="coin11" src="img/coin111.png" /><img class="coin22" src="img/coin222.png" />'
				item1 += '<a class="cks" href="tab.html" target="main">' + result.colName + '</a>'
				item1 += '<img class="icon5" src="img/coin21.png" />'
				item1 += '<p style="display: none;">' + result.id + '</p>'
				item1 += '<span style="display: none;">' + result.colLink + '</span>'
				item1 += '</dd>';

				item2 += '<dd class="headline">'
				item2 += '<img class="coin11" src="img/coin111.png" /><img class="coin22" src="img/coin222.png" />'
				item2 += '<a class="cks" href="hot.html" target="main">' + result.colName + '</a>'
				item2 += '<img class="icon5" src="img/coin21.png" />'
				item2 += '<p style="display: none;">' + result.id + '</p>'
				item2 += '<span style="display: none;">' + result.colLink + '</span>'
				item2 += '</dd>';

				item3 += '<dd class="headline">'
				item3 += '<img class="coin11" src="img/coin111.png" /><img class="coin22" src="img/coin222.png" />'
				item3 += '<a class="cks" href="find.html" target="main">' + result.colName + '</a>'
				item3 += '<img class="icon5" src="img/coin21.png" />'
				item3 += '<p style="display: none;">' + result.id + '</p>'
				item3 += '<span style="display: none;">' + result.colLink + '</span>'
				item3 += '</dd>';

				item4 += '<dd class="headline">'
				item4 += '<img class="coin11" src="img/coin111.png" /><img class="coin22" src="img/coin222.png" />'
				item4 += '<a class="cks" href="myEssay.html" target="main">' + result.colName + '</a>'
				item4 += '<img class="icon5" src="img/coin21.png" />'
				item4 += '<p style="display: none;">' + result.id + '</p>'
				item4 += '<span style="display: none;">' + result.colLink + '</span>'
				item4 += '</dd>';

				//				item5 += '<dd class="headline">'
				//				item5 += '<img class="coin11" src="img/coin111.png" /><img class="coin22" src="img/coin222.png" />'
				//				item5 += '<a class="cks" href="myVideo.html" target="main">' + result.colName + '</a>'
				//				item5 += '<img class="icon5" src="img/coin21.png" />'
				//				item5 += '<p style="display: none;">' + result.id + '</p>'
				//				item5 += '<span style="display: none;">' + result.colLink + '</span>'
				//				item5 += '</dd>';
			});
			$('#headline_dl').append(item1);
			$('#hot_dl').append(item2);
			$('#find_dl').append(item3);
			$('#vip_user').append(item4);
			//			$('#vip_user3').append(item5);
		}
	});
}
function getShopId(appUserId){
		// 获取商铺的id
	$.ajax({
		type: "post",
		url: getRequestIp() + "getBusinessByUserId",
		async: true,
		data: {
			'appUserId': appUserId
		},
		dataType: 'json',
		success: function(data) {
			if(data.list != null) {
				sessionStorage.setItem("bus_id", data.list[0].id);
			} else {
				sessionStorage.removeItem('bus_id');
			}
		}
	});
}