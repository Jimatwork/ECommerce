/*手机双击两次返回键退出APP*/
document.addEventListener('plusready', function() {
    var webview = plus.webview.currentWebview();
    plus.key.addEventListener('backbutton', function() {
        webview.canBack(function(e) {
            if(e.canBack) {
                webview.back();
            } else {
                webview.close(); //hide,quit
                //plus.runtime.quit();
            }
        })
    });
});
function token() {
	var token = null;
	var userName = "test1";
	var cipherCode = "666888";
	var url = "http://help.qhinfo.net/index.php/api/account/checkAccount";
	var json = {
		"user": {
			"account": userName,
			"passwd": cipherCode
		}
	};
	$($.ajax({
		type: "POST",
		async: false,
		dataType: "json",
		data: "json=" + JSON.stringify(json),
		url: url,
		success: function(data) {
			return token = data.account.token;
		},
		error: function(data) {
			var wangl = "";
			wangl = '<div style="width: 100%;height: 100%;text-align: center;margin-top: 40%">'
			wangl += '<h3 style="width: 100%;">网络连接失败！</h3>'
			wangl += '</div>'
			$('.area').html(wangl);
		}
	}));
}
function callback(apis, datas, tok, callb, calla) {
	if (tok !== null) {
		var usurl = apis + tok;
		var usjson = datas;
		$.ajax({
			type: "POST",
			dataType: "json",
			data: "json=" + JSON.stringify(usjson),
			url: usurl,
			success: callb,
			error: calla
		})
	}
};
/*通用请求封装*/
function callbacka(pid, tok, callb, calla) {
	if (tok !== null) {
		var usurl = "http://help.qhinfo.net/index.php/api/area/getChildren?accessToken=" + tok;
		var usjson = {
			'area': {
				'pid': pid
			}
		};
		$.ajax({
			type: "POST",
			dataType: "json",
			data: "json=" + JSON.stringify(usjson),
			url: usurl,
			success: callb,
			error: calla
		})
	}
};
/*菜单*/	
function menudy(menua, menub, menuc, menud, classa, classb, classc, classd) {
	var tianj = "";
	tianj = '<li class="floa-left ' + classa + '"><a class="sss" href="#">' + menua + '</a></li>'
	tianj += '<li class="floa-left ' + classb + '"><a href="#">' + menub + '</a></li>'
	tianj += '<li class="floa-left ' + classc + '"><a href="#">' + menuc + '</a></li>'
	tianj += '<li class="floa-left ' + classd + '"><a href="#">' + menud + '</a></li>'
	$(".cai-dan").html(tianj);
};