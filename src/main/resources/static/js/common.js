var serverPath = "https://app.kuedit.com/";
//var serverPath = "http://192.168.10.108:8888/";
//var serverPath = "https://www.etcast.com/KuBian/";
/**
 * 公用Ajax
 * @param {Object} ItemId
 */
//公用Ajax https://www.etcast.com/KuBian/
function publicAjax(url, data, success, beforeSend, complete) {
	var timer;
	$.ajax({
		type: 'POST',
		url: url,
		data: data,
		dataType: 'json',
		beforeSend: beforeSend,
		success: success,
		complete: complete,
		error: function() {
			console.log("请求错误！");
		}
	});
};

var app_serect = "bb4c776019f9bf152d82617d463a1537";
var appId = "wx73c2d36878ef2aa7";

var DataLimit = "10";

var currentUserId = "0";

function window_width() {
	var window_width = $(window).width();
	return window_width;
}

/**
 *拿用户ID
 */
function getUserID() {
	var currentUserInfo = JSON.parse(appcan.locStorage.getVal("app_user"));
	if(currentUserInfo != null) {
		currentUserId = currentUserInfo.id;
	}
	return currentUserId;

}

/**
 *设置内容ID ， 并跳转到详情/更多页面
 * @param {Object} ItemId
 */
function checkItemDetail(filePath, ItemId) {
	appcan.locStorage.setVal("data_arr", ItemId);
	if(filePath.length > 0) {
		appcan.openWinWithUrl('discover2', filePath + '/discover2.html');
	} else {

		appcan.openWinWithUrl('discover2', 'discover2.html');
	}
};

/**
 *打开一个图片浏览窗口
 * @param {Object} ItemId
 */
function checkImg(img_src) {
	appcan.locStorage.setVal("img_src", img_src);
	appcan.openWinWithUrl('ImgWin', '../imgWin.html');
};

//var serverPath = "http://manage.app4u.tv/HanaGlobal/";

/**
 * 获取滚动条当前的位置
 * @param {Object} ItemId
 */
function getScrollTop() {
	var scrollTop = 0;
	if(document.documentElement && document.documentElement.scrollTop) {
		scrollTop = document.documentElement.scrollTop;
	} else if(document.body) {
		scrollTop = document.body.scrollTop;
	}
	return scrollTop;
}

/**
 * 获取当前可视范围的高度
 * @param {Object} ItemId
 */
function getClientHeight() {
	var clientHeight = 0;
	if(document.body.clientHeight && document.documentElement.clientHeight) {
		clientHeight = Math.min(document.body.clientHeight, document.documentElement.clientHeight);
	} else {
		clientHeight = Math.max(document.body.clientHeight, document.documentElement.clientHeight);
	}
	return clientHeight;
}

/**
 * 获取文档完整的高度
 * @param {Object} ItemId
 */
function getScrollHeight() {
	return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
}

/**
 * 点赞
 * @param {Object} ItemId
 */
function clickLike(PraiseUrl, conid, userId, thist) {

	publicAjax(PraiseUrl, {
		'id': conid,
		'appUserId': userId,
	}, function(data) {

		if(data.success) {
			console.log(data.totalSize)
			if(data.msg) {
				thist.siblings('.datans').text(data.totalSize);
				thist.toggleClass('comment_color');
			} else {
				thist.siblings('.datans').text(data.totalSize);
			}
		} else {
			appcan.window.alert({
				title: "提醒",
				content: data.msg,
				buttons: '确定'
			});
		}
	});

};

/**
 * 阅读
 * @param {Object} ItemId
 */
function Reading(conid, thist) {
	var PraiseUrl = "getColContentById";
	publicAjax(PraiseUrl, {
		'id': conid,
	}, function(data) {
		if(data.success) {
			console.log(JSON.stringify(data.msg))
		} else {
			appcan.window.alert({
				title: "提醒",
				content: data.msg,
				buttons: '确定'
			});
		}
	});
};

/**
 *用Token换取用户信息
 */
function getLoginUserInfo(token, openid) {
	var params = {
		access_token: token,
		openid: openid
	};
	var info = JSON.stringify(params);
	uexWeiXin.getLoginUnionID(info, function(data) {
		/*openid  String  是   普通用户的标识,对当前开发者帐号唯一
		 nickname    String  是   普通用户昵称
		 sex String  是   普通用户性别,1为男性,2为女性
		 language    String  是   微信客户端当前语言
		 city    String  是   普通用户个人资料填写的城市
		 province    String  是   普通用户个人资料填写的省份
		 country String  是   国家,如中国为CN
		 headimgurl  String  是   用户头像,最后一个数值代表正方形头像大小(有0、46、64、96、132数值可选,0代表640*640正方形头像),用户没有头像时该项为空
		 privilege   String  是   用户特权信息,json数组,如微信沃卡用户为(chinaunicom)
		 unionid String  是   用户统一标识.针对一个微信开放平台帐号下的应用,同一用户的unionid是唯一的.
		 */
		var json = {
			"openId": data.openid,
			"userName": data.nickname,
			"userImg": data.headimgurl
		}
		publicAjax("appWeixinLogin", json, function(data) {
			if(data.success) {
				appcan.window.publish('succLogin', true);
				appcan.locStorage.setVal("app_user", data.list[0]);
				appcan.window.close(-1);
			}
		})
	});
}

/**
 * 背景音乐图标旋转
 *
 */
var current = 0;
var mp3ivl = null;

function mp3IconRotate() {
	var t = document.getElementById('music_btn');
	if(mp3ivl == null && $("#mp3_source").attr("src") != null) {
		mp3ivl = setInterval(function() {
			current = (current + 10) % 360;
			t.style.transform = 'rotate(' + current + 'deg)';
		}, 200);
		$("#mp3_audio")[0].play();
	} else {
		clearInterval(mp3ivl);
		mp3ivl = null;
		$("#mp3_audio")[0].pause();
	}
}

/**
 * 检测用户是否登录
 */
function userIsLogin() {
	var loginUser = appcan.locStorage.getVal("app_user");
	//如果是没登录就提示用户登录
	if(loginUser == null) {
		appcan.window.confirm({
			title: '提示',
			content: '您还没有登录，是否去登录？',
			buttons: ['是', '否'],
			callback: function(err, data, dataType, optId) {
				if(data == 0) {
					appcan.openWinWithUrl('login_win', '../login.html');
				} else {
					return false;
				}
			}
		});
	} else {
		return true;
	}
};

//数据展示视频结构
function html1(arrjih) {
	return `<ul class="emphasis tong deltree2" data_arr='${arrjih[0]}'>
            <li>
                <img class="imgs" src="${arrjih[1]}"/>
                <p>${arrjih[2]}</p>
                <p>${arrjih[5]}</p>
                <p></p>
            </li>
            ${html_s(arrjih)}
        </ul>`
};

//图文结构
function html2(arrjih) {
	return `<ul class="tong deltree2" data_arr='${arrjih[0]}'>
                <li class="t_t">
                    <img class="biandong float_lefts" src="${arrjih[1]}" />
                    <div class="float_lefts">
                        <p>${arrjih[2]}</p>
                        <p>${arrjih[5]}</p>
                    </div>
                </li>
                ${html_s(arrjih)}
                </ul>`
};

//文字结构
function html3(arrjih) {
	return `<ul class="tong deltree2" data_arr='${arrjih[0]}'>
                <li class="biaoti">${arrjih[2]}</li>
                <li class="t_ts">
                    ${arrjih[5]}
                </li>
                ${html_s(arrjih)}
            </ul>`
};

//点赞、阅读
function html_s(arrjih) {
	return `<li>
        <div class="dianp">
            <div class="dianp_left float_lefts">
                <img class="float_lefts" src="${arrjih[10]}" />
                <p class="float_lefts">${arrjih[11]}</p>
            </div>
            <div class="dianp_rigth float_lefts">
                <span class="price">${arrjih[8]}</span>
                <span data_o="${arrjih[0]}" data_id="${arrjih[14]}" class="fa fa-thumbs-up comments ${arrjih[12]}"></span>
                <span class="fa fa-newspaper-o sharing">
                <span class="reads">${arrjih[4]}</span>
                </span>
            </div>
        </div>
    </li>`
};

//获取时分秒
function time(times) {
	var time = new Date(times * 1),
		myFather = new dateYTD(time);
	return myFather.times();
};

//获取年月日
function times(times) {
	var time = new Date(times * 1),
		r = time.getDate() < 10 ? "0" + time.getDate() : time.getDate(),
		y = time.getMonth() + 1 < 10 ? "0" + (time.getMonth() + 1) : time.getMonth() + 1,
		n = time.getFullYear();
	s = time.getHours() < 10 ? "0" + time.getHours() : time.getHours(),
		f = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes(),
		m = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
	return n + "-" + y + "-" + r + "-" + s + ":" + f;
};

//过去时间    
function pastTense(times) {
	var n = 1000 * 60 * 60 * 24 * 365,
		y = 1000 * 60 * 60 * 24 * 30,
		r = 1000 * 60 * 60 * 24,
		s = 1000 * 60 * 60,
		f = 1000 * 60,
		time = new Date().getTime(),
		jihe = time - times,
		arrleng,
		lengs = ["年前", "月前", "天前", "小时前", "分钟前"],
		leng = [n, y, r, s, f];

	for(var l = 0; l < leng.length; l++) {
		if(jihe <= 60000) {
			return "刚刚";
		} else {
			if(jihe > leng[l]) {
				return Math.floor(jihe / leng[l]) + " " + lengs[l];
			}
		}
	}
};

function dateYTD(time) {
	this.hour = time.getHours() < 10 ? "0" + time.getHours() : time.getHours(),
		this.minute = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes(),
		this.second = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds(),
		this.times = function() {
			return this.hour + ":" + this.minute;
		}
};

// 判断是否加密bese64
function base64_decode(str) {
	var c1, c2, c3, c4;
	var base64DecodeChars = new Array(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57,
		58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6,
		7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
		25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36,
		37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1
	);
	var i = 0,
		len = str.length,
		string = '';

	while(i < len) {
		do {
			c1 = base64DecodeChars[str.charCodeAt(i++) & 0xff]
		} while (
			i < len && c1 == -1
		);

		if(c1 == -1) break;

		do {
			c2 = base64DecodeChars[str.charCodeAt(i++) & 0xff]
		} while (
			i < len && c2 == -1
		);

		if(c2 == -1) break;

		string += String.fromCharCode((c1 << 2) | ((c2 & 0x30) >> 4));

		do {
			c3 = str.charCodeAt(i++) & 0xff;
			if(c3 == 61)
				return string;

			c3 = base64DecodeChars[c3]
		} while (
			i < len && c3 == -1
		);

		if(c3 == -1) break;

		string += String.fromCharCode(((c2 & 0XF) << 4) | ((c3 & 0x3C) >> 2));

		do {
			c4 = str.charCodeAt(i++) & 0xff;
			if(c4 == 61) return string;
			c4 = base64DecodeChars[c4]
		} while (
			i < len && c4 == -1
		);

		if(c4 == -1) break;

		string += String.fromCharCode(((c3 & 0x03) << 6) | c4)
	}
	return string;
};

function base64_encode(str) {
	var c1, c2, c3;
	var base64EncodeChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
	var i = 0,
		len = str.length,
		string = '';

	while(i < len) {
		c1 = str.charCodeAt(i++) & 0xff;
		if(i == len) {
			string += base64EncodeChars.charAt(c1 >> 2);
			string += base64EncodeChars.charAt((c1 & 0x3) << 4);
			string += "==";
			break;
		}
		c2 = str.charCodeAt(i++);
		if(i == len) {
			string += base64EncodeChars.charAt(c1 >> 2);
			string += base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
			string += base64EncodeChars.charAt((c2 & 0xF) << 2);
			string += "=";
			break;
		}
		c3 = str.charCodeAt(i++);
		string += base64EncodeChars.charAt(c1 >> 2);
		string += base64EncodeChars.charAt(((c1 & 0x3) << 4) | ((c2 & 0xF0) >> 4));
		string += base64EncodeChars.charAt(((c2 & 0xF) << 2) | ((c3 & 0xC0) >> 6));
		string += base64EncodeChars.charAt(c3 & 0x3F)
	}
	return string
};