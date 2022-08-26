
(function($) {
	var dian = 1,
		dians = 2;
	var window_width = $(window).height();
	$("#doing").height(window_width);
	$.getUrlParam = function(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r != null) return unescape(r[2]);
		return null;
	};
	data_arr = $.getUrlParam('conId');
	cindex = $.getUrlParam('cindex');

	var recommendp = "getHeadLines";

	
	if (cindex == 0) {
		recommendp = "getHeadLines";
	} else if (cindex == 1) {
		recommendp = "getHotColumnContent";
	} else if (cindex == 3) {
		recommendp = "queryFindLisAltert";
	}
	;

	var userObjs = appcan.locStorage.getVal("app_user");
	var datalength = 5; //页面数据显示条数

	//base64位加密

	Base64 = {

		// private property
		_keyStr : "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

		// public method for encoding
		encode : function(input) {
			var output = "";
			var chr1,
				chr2,
				chr3,
				enc1,
				enc2,
				enc3,
				enc4;
			var i = 0;

			input = Base64._utf8_encode(input);

			while (i < input.length) {

				chr1 = input.charCodeAt(i++);
				chr2 = input.charCodeAt(i++);
				chr3 = input.charCodeAt(i++);

				enc1 = chr1 >> 2;
				enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
				enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
				enc4 = chr3 & 63;

				if (isNaN(chr2)) {
					enc3 = enc4 = 64;
				} else if (isNaN(chr3)) {
					enc4 = 64;
				}

				output = output + this._keyStr.charAt(enc1) + this._keyStr.charAt(enc2) + this._keyStr.charAt(enc3) + this._keyStr.charAt(enc4);

			}

			return output;
		},

		// public method for decoding
		decode : function(input) {
			var output = "";
			var chr1,
				chr2,
				chr3;
			var enc1,
				enc2,
				enc3,
				enc4;
			var i = 0;

			input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");

			while (i < input.length) {

				enc1 = this._keyStr.indexOf(input.charAt(i++));
				enc2 = this._keyStr.indexOf(input.charAt(i++));
				enc3 = this._keyStr.indexOf(input.charAt(i++));
				enc4 = this._keyStr.indexOf(input.charAt(i++));

				chr1 = (enc1 << 2) | (enc2 >> 4);
				chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
				chr3 = ((enc3 & 3) << 6) | enc4;

				output = output + String.fromCharCode(chr1);

				if (enc3 != 64) {
					output = output + String.fromCharCode(chr2);
				}
				if (enc4 != 64) {
					output = output + String.fromCharCode(chr3);
				}

			}

			output = Base64._utf8_decode(output);

			return output;

		},

		// private method for UTF-8 encoding
		_utf8_encode : function(string) {
			string = string.replace(/\r\n/g, "\n");
			var utftext = "";

			for (var n = 0; n < string.length; n++) {

				var c = string.charCodeAt(n);

				if (c < 128) {
					utftext += String.fromCharCode(c);
				} else if ((c > 127) && (c < 2048)) {
					utftext += String.fromCharCode((c >> 6) | 192);
					utftext += String.fromCharCode((c & 63) | 128);
				} else {
					utftext += String.fromCharCode((c >> 12) | 224);
					utftext += String.fromCharCode(((c >> 6) & 63) | 128);
					utftext += String.fromCharCode((c & 63) | 128);
				}

			}

			return utftext;
		},

		// private method for UTF-8 decoding
		_utf8_decode : function(utftext) {
			var string = "";
			var i = 0;
			var c = c1 = c2 = 0;

			while (i < utftext.length) {

				c = utftext.charCodeAt(i);

				if (c < 128) {
					string += String.fromCharCode(c);
					i++;
				} else if ((c > 191) && (c < 224)) {
					c2 = utftext.charCodeAt(i + 1);
					string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
					i += 2;
				} else {
					c2 = utftext.charCodeAt(i + 1);
					c3 = utftext.charCodeAt(i + 2);
					string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
					i += 3;
				}

			}

			return string;
		}
	};


	if (userObjs === null) {
		var userObj = null;
	} else {
		var userObj = JSON.parse(userObjs); //登录用户信息
		var userObj = userObj.id;
	}
	;

	//下拉加载
	scrollsd(true, datalength, userObj);
	publicAjax("getColContentById", {
		"id" : data_arr * 1
	}, function(data) {

		if (data.success) {
			Reading(data_arr);
			var datas = data.list;
			var dataConHtml = "",
				dataVideoPath = "",
				userId,
				sltImg,
				userNames,
				FollowedUserId;
			appcan.locStorage.setVal("dataObj", datas[0]);
			appcan.locStorage.setVal("articleId", datas[0].id);
			//判断评论是否被点赞
			getCommenty(datas[0].id, userObj, 0, datalength, false);

			if (datas[0].userName === null) {
				userNames = "酷编";
			} else {
				userNames = datas[0].userName;
			}
			;

			if (datas[0].appUserId !== null) {
				userId = datas[0].appUserId;
				FollowedUserId = appcan.locStorage.setVal("FollowedUserId", 0);
			} else {
				userId = datas[0].userId;
				FollowedUserId = appcan.locStorage.setVal("FollowedUserId", 1);
			}
			;


			appcan.locStorage.setVal("createId", userId);

			if (datas[0].conHtml !== null) {
				dataConHtml = datas[0].conHtml.replace(new RegExp("!important","g"), "");
			}
			;

			if (datas[0].conVideoPath !== null) {
				dataVideoPath = datas[0].conVideoPath;
			}
			;

			if (dataVideoPath !== "") {
				$('.slideshow').hide();
				$('#videos').show();
				$('#videos').attr('src', dataVideoPath);
			} else {
				$('#videos').hide();
				$('.slideshow').attr("src", datas[0].conImg);
			}
			;

			if (datas[0].sltImg === null) {
				sltImg = "img/kubian-new.png";
			} else {
				sltImg = datas[0].sltImg;
			}
			;

			con_img = datas[0].conImg1;
			con_title = datas[0].conTitle;
			con_remark = datas[0].conRemark;
			con_backMp3 = datas[0].backgroundMusic;

			$('.mingzis').html(datas[0].userName);
			$('.wai_header_img').attr('src', sltImg);
			$('.wai_header_h3').text(userNames);
			$('#biaoti').html(datas[0].conTitle);
			$(document).attr("title",datas[0].conTitle);

			$('.datans').html(datas[0].praiseCount);
			$('.datan').html(times(datas[0].createDate));
			$('.yuedu').html("阅读："+datas[0].playCount);
			$('.zhaiyao').html(datas[0].conRemark);
			$('.faxias').html(datas[0].colName);
			var conLivePath = datas[0].conLivePath || datas[0].conVideoPath;
            if(conLivePath == null || conLivePath == ""){
                $('.presentation').html(dataConHtml);
            }else{              
                var conLivePaths = conLivePath.slice(0,1);
                if(conLivePaths == 'r'){
                    $('.presentation').html(rtmpHtnl(conLivePath, datas[0].conImg));
                }else if(conLivePaths == 'h'){
                	$('.presentation').html(videoHtml(conLivePath, datas[0].conImg));
                }else{
                    $('.presentation').html(conLivePath);
                }
            };
            
            var gallery = $('.my-gallery');
		gallery.children('.figures').css("margin-top", 0);
            for(var l=0; l< gallery.length; l++){
                if(gallery.eq(l).children('.figures').length > 1){
                    gallery.eq(l).children('.figures').height('6em');
                    gallery.eq(l).children('.figures').children('.figure_div').height('6em');   
                }
            };
            
            initPhotoSwipeFromDOM('.my-gallery');
			//广告
			advertising(datas[0].colId);

			isAtt_isCollect('getAttention', true, userObj);

			//推荐阅读
			recommends(recommendp, datas[0].colId, datas[0].rId, userId);

			//如果有背景音乐
			if (con_backMp3 != null) {
				$("#back_mp3_div").css("display", "");
				$("#mp3_source").attr("src", con_backMp3);
				mp3IconRotate();

			}
			;

			//加载语音转换的Mp3
			loadTTS();
			setTimeout(sds, 500);
		}
	});
	/**
	 *rtmp视频
	 * rtmp://114.112.101.230/kblive/live1234
	 */
	function rtmpHtnl(url, img) {
		return `<p style="width: 100%;height: 15em;background-image: url(${img});background-size: 100% 100%;position: relative;">                      
                    <img src="img/play.png" style="width: 3em;height: 3em; position: absolute;left: 50%;top: 50%;margin-left: -1.5em;margin-top: -1.5em;" onclick="setVideo('${url}');"></img>                     
                </p>`
	}
	;

	/**
     *html5视频
     * http://www.w3school.com.cn/i/movie.mp4
     */
    function videoHtml(src,img){
        return  `<p style="overflow: hidden;position: relative;">
                        <video id="video" controls="controls"  poster="${img}" style="width: 100%;">
                            <source src="${src}">
                        </video>
                        <div id="output"></div>
                        <div class="outputs" style="position: absolute;left: 0;top: 0;background: rgba(0,0,0,.3);height: 100%;width: 100%;">
                            <img class="video_img" style="width: 3em;height: 3em;position: absolute;left: 50%;top: 50%;margin-top: -1.5em;margin-left: -1.5em;" src="../img/play.png" />
                        </div>
                    </p>`
    };

	//html5视频暂停
	$('.presentation').on('click', '.video_img', function() {
		var myVideo = document.getElementById("video");
		myVideo.play();
		$(".outputs").hide();
	});

	//html5视频播放
	$('.presentation').on('click', '#video', function() {
		var myVideo = document.getElementById("video");
		myVideo.pause();
		$(".outputs").show();
	});
	/**
	 *点赞
	 */
	$('.comments').click(function() {
		if (dian == 1) {
			var thist = $(this);
			var articleId = appcan.locStorage.getVal("articleId");
			clickLike("addPraiseCount", articleId, "", thist);
			dian = "mei";
		}
		;
	});

	/**
	 *收藏
	 */
	$('.share2').click(function() {
		if (!userIsLogin()) {
			return;
		}
		var thist = $(this);
		var judge = thist.children('div').hasClass("text_color");
		publicPC("addCollect", thist, judge, false, userObj);

	});

	/**
	 *关注
	 */
	$('.mingzis2').click(function() {

		if (!userIsLogin()) {
			return;
		}
		var thist = $(this);
		var judge = thist.hasClass("background_color");
		publicPC("addAttention", thist, judge, true, userObj);
	});

	/**
	 *给评论点赞
	 */
	$('#comment').on('click', '.commentss', function() {
		if (dians == 2) {
			var thist = $(this);
			var data_id = thist.attr("data_o");
			clickLike("giveComment", data_id, userObj, thist);
			dians = "mei";
		}
		;
	});

	function sds() {
		//判断是否被收藏
		isAtt_isCollect('isCollect', false, userObj)
	}
	;

	//判断是否被关注或收藏
	function isAtt_isCollect(locations, users, userObj) {
		if (userObj === null) {

		} else {
			var createId,
				data_Object,
				articleId;

			createId = appcan.locStorage.getVal("createId");
			articleId = appcan.locStorage.getVal("articleId");

			if (users) {
				data_Objec = {
					"followerId" : userObj,
					"beFollowedId" : createId
				};
			} else {
				data_Objec = {
					"appUserId" : userObj,
					"columnContentId" : articleId
				};
			}
			;

			publicAjax(locations, data_Objec, function(data) {

				if (data.success) {
					if (users) {
						console.log("关注");
						$('.mingzis2').addClass("background_color").html("已关注");
					} else {
						console.log("收藏");
						$('.share2').children('div').addClass("text_color");
					}
				}
			})
		}
	}
	;

	/**
	 *关注 ,收藏
	 */
	function publicPC(ddAttention, thist, judge, trues, userObj) {
		if (userObj === null) {

		} else {
			var tags,
				createId,
				data_Object,
				FollowedUserId,
				articleId;
			judge ? tags = 0 : tags = 1;
			createId = appcan.locStorage.getVal("createId");
			FollowedUserId = appcan.locStorage.getVal("FollowedUserId");
			articleId = appcan.locStorage.getVal("articleId");

			if (userObj !== createId) {
				if (trues) {
					data_Object = {
						"followerId" : userObj,
						"beFollowedId" : createId,
						"tag" : tags,
						"judge" : FollowedUserId
					};
				} else {
					data_Object = {
						"appUserId" : userObj,
						"columnContentId" : articleId,
						"tag" : tags
					};
				}
				;
				attention_collect(ddAttention, data_Object, thist, trues);
			}
		}
	}
	;

	var mask_Mask = $("#Mask");
	var quxiao = $(".quxiao");
	//取消发布
	var queding = $(".queding");
	//确定发布
	var share3 = $(".share3");
	var mask_Masks = $("#Masks");

	var articlecommon_a = $(".articlecommon_a");
	appcan.button("#nav-left", "btn-act", function() {
		appcan.closeWin(-1);
	});
	appcan.button("#nav-right", "btn-act", function() {});
	appcan.button("#Box_footer", "btn-act", function() {
		mask_Mask.show();
		setTimeout(function() {
			$('.articlecommon_as').focus();
		}, 300);
	});
	appcan.ready(function() {})
	mask_Mask.on("touchstart", function(e) {
		e.preventDefault();
	});
	mask_Mask.on("touchmove", function(e) {
		e.stopPropagation();
	});
	mask_Mask.on("tap", function(e) {
		if (e.target.id == 'Mask') {
			$(e.target).removeClass("show");
			setTimeout(function() {
				$(e.target).removeClass("active");
				$("#Box_footer_maskout").addClass('uhide');
			}, 300)
		}
	});
	mask_Mask.show = function() {
		var self = this;
		self.addClass("active");
		setTimeout(function() {
			self.addClass("show");
			$("#Box_footer_maskout").removeClass('uhide');
		}, 1);
	};

	mask_Masks.on("touchstart", function(e) {
		e.preventDefault();
	});
	mask_Masks.on("touchmove", function(e) {
		e.stopPropagation();
	});
	mask_Masks.on("tap", function(e) {
		$(e.target).removeClass("show");
		$("#Footer_mask").addClass('uhide');
		setTimeout(function() {
			$(e.target).removeClass("active");
		}, 300)
	});
	mask_Masks.show = function() {
		var self = this;
		self.addClass("active");
		$("#Footer_mask").removeClass('uhide');
		setTimeout(function() {
			self.addClass("show");
		}, 1);
	}
	/**
	 *分享
	 */
	share3.on("tap", function(e) {
		setTimeout(function() {
			mask_Masks.show();
		}, 500);
	});

	quxiao.on("tap", function(e) {
		mask_Mask.removeClass("show");
		setTimeout(function() {
			$(e.target).removeClass("active");
			$("#Box_footer_maskout").addClass('uhide');
		}, 300);
	});

	/**
	 *发布评论
	 */
	queding.on("tap", function(e) {
		var dataObj = appcan.locStorage.getVal("dataObj"),
			userObj = appcan.locStorage.getVal("app_user"),
			dataObj = JSON.parse(dataObj),
			userObj,
			userNames,
			userId,
			userImg;
			var str = window.location.href;
			
		if (str.indexOf("code") != -1) {
			str = str.split("=");
			str = str[4].split("&");
			
			$.ajax({
				type: 'POST',
				url: serverPath + "weixinOauth",
				data: {
					"code": str[0]
				},
				async: false,
				dataType: "json",
				success: function (data) {
					if (data.success) {
						var data = data.list;
						userImg = data[0].userImg;
						userNames =	data[0].userName;
						userId = data[0].id;
					}
				}
			});
	} else {
		var url = encodeURIComponent(location.href.split('#')[0]);
		window.location.href = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx064cb833dc1853c4&redirect_uri="+ url +"&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect"
		return
	};

		var content = $("#articlecommon_a").val();
		var contents = Base64.encode(content);

		var discuss = {
			"conId" : dataObj.id,
			"content" : contents,
			"username" : userNames,
			"appUserId" : userId,
			"avator" : userImg
		}
		var contents = $("#articlecommon_a").val();
		if (contents !== "") {
			//          uexLoadingView.openCircleLoading();
			publicAjax("addComments", discuss, function(data) {
				if (data.success) {
					mask_Mask.removeClass("show");
					//                	uexLoadingView.close();
					setTimeout(function() {

						getCommenty(dataObj.id, userId, 0, datalength, false);
						scrollsd(true, datalength, userId);

						$(e.target).removeClass("active");
						$("#Box_footer_maskout").addClass('uhide');
					}, 300);
				}
			})
		}
	});

	//评论数据渲染 方式    
	function getCommenty(UserId, userObj, start, limit, trues) {
		// console.log("?conId="+UserId+"&appUserId="+userObj+"&start="+start+"&limit="+limit+"&tag=5");
		publicAjax("getComments", {
			"conId" : UserId,
			"appUserId" : userObj,
			"start" : start,
			"limit" : limit,
			"tag" : "5"
		}, function(data) {
			
			if (data.success) {
				$('.xiaobiao').html(data.totalSize);
				if (trues) {
					var totalSizes = Math.ceil(data.totalSize / limit) * 1 - 1;

					if (start > totalSizes) {
						console.log("没有更多数据！");
						$('#nothing').show();
					} else {
						comments_html(data, trues);
					}
					;
				} else {
					comments_html(data, trues);
				}
			}
		});
	}
	;

	//评论数据渲染    
	function comments_html(datapo, trues) {
		var datap = datapo.list,
			htmls = "",
			data_length;
		if (datap === null) {
			data_length = 0;
		} else {
			data_length = datap.length;
		}
		;

		for (var l = 0; l < data_length; l++) {

			htmls += comments_BQ(datap, l);
		}
		;

		if (trues) {
			$("#comment").append(htmls);
		} else {
			$("#comment").html(htmls);
		}
		;
	}
	;

	//评论html结构
	function comments_BQ(datas, l) {
		var praiseCount,
			comment_colo,
			avator,
			username;
		if (datas[l].username == "游客" || datas[l].username == null) {
			avator = "img/accuser.png";
			username = "游客";
		} else {
			username = datas[l].username;
			avator = datas[l].avator;
		}
		;

		if (datas[0].give == '1') {
			$(".comments").addClass('comment_color');
		}
		;

		if (datas[l].praiseCount === null) {
			praiseCount = 0;
		} else {
			praiseCount = datas[l].praiseCount;
		}
		;

		//判断是否bese64位加密
		var contents;
		var $str = datas[l].content;
		if ($str == base64_encode(base64_decode($str))) {
			contents = Base64.decode(datas[l].content);
		} else {
			contents = datas[l].content;
		}
		;

		return `<ul class="comments_shows">
                    <li class="comments_user">
                       <p><img src="${avator}" alt="懒"/></p>
                       <p>${username}</p>
                       <p>
	                        <span class="font_sizes fa fa-thumbs-up commentss comment_like ${comment_colo}" data_o="${datas[l].id}"></span>
	                        <span class="font_sizes datans">${praiseCount}</span>
                       </p>
                     </li>
                     <li class="comments_content">
                         <p>${contents}</p>
                         <p class="font_sizes">${pastTense(datas[l].createDate)}</p>
                     </li>
                  </ul>`
	}
	;

	//滚动事件触发
	function scrollsd(bianl, datalength, userObj) {
		var lengths = 0;
		window.onscroll = function() {
			if (getScrollTop() + getClientHeight() == getScrollHeight()) {
				lengths++
				var dataObj = appcan.locStorage.getVal("dataObj");
				var dataObj = JSON.parse(dataObj);

				if (bianl) {
					getCommenty(dataObj.id, userObj, lengths, datalength, true);
				}
			}
		}
	}
	;
})($);


//广告
function advertising(Cid, window_width) {
	publicAjax("queryExNew", {
		"colid" : Cid,
		"page" : 0,
		limit : 10
	}, function(data) {
		if (data.success) {
			var upperAdvertising = 0,
				belowAdvertising = 0,
				upperArr = [],
				belowArr = [];
			var data = data.list;
			for (var i = 0; i < data.length; i++) {
				if (data[i].site == 1 || data[i].site == 2) {
					upperAdvertising += 1;
					upperArr.push(data[i].linkUrl, data[i].imgUrl);
				} else {
					belowAdvertising += 1;
					belowArr.push(data[i].linkUrl, data[i].imgUrl);
				}
			}
			;

			Advertising(upperAdvertising, upperArr, true);
			Advertising(belowAdvertising, belowArr, false);
		}
		;
	}, function(XMLHttpRequest) {}, function(XMLHttpRequest, textStatus) {
		$("#doing").hide();
	})
}
;

/**
*广告方法
*/
function Advertising(pps, arr, trues) {
	(function(l, arrs, t) {
		var lengs = 0;
		for (var l = 0; l < arrs.length / 2; l++) {
			if (t) {
				$('#advertisings').append(advertisingHtml(arrs, lengs));
			} else {
				$('#advertising').append(advertisingHtml(arrs, lengs));
			}
			;
			lengs += 2;
		}
		;
	})(pps, arr, trues);
}
;

/**
 *广告Html
 */
function advertisingHtml(datas, lengs) {
	return `<ul style="height: 10em;margin-bottom: .03em;">
                <a href="${datas[0 + lengs]}">
                    <img class="advertising_img" src="${datas[1 + lengs]}" />
                </a>
            </ul>`
}
;

/**
 *推荐
 */
function recommends(recommendp, id ,rIds, userObj){
     
    if(rIds == "" || rIds == null){
        publicAjax("getRandom", "", function(data) {
            
            var htmlse = "";
            for(var l=0; l<data.content.length; l++){
    
                htmlse += recommendDa(data.content, l);
            };
            
           $("#recommend").html(htmlse);
        })
    }else{      
        publicAjax("getRecommendCon", {"rId": rIds, "appUserId": userObj}, function(data) {
            
            var htmlse = "";
            for(var l=0; l<data.list.length; l++){
    
                htmlse += recommendDa(data.list, l);
            };
            
           $("#recommend").html(htmlse);
        })
    };
};
// function recommends(recommendp, id) {
// 	publicAjax("getRandom", "", function(data) {
// 
// 		var htmlse = "";
// 		for (var l = 0; l < data.content.length; l++) {
// 
// 			htmlse += recommendDa(data.content, l);
// 		}
// 		;
// 
// 		$("#recommend").html(htmlse);
// 	});
// }
// ;

/**
 *推荐htnl
 */
function recommendDa(datalist, l) {
	var userName;
	if (datalist[l].userName === null) {
		userName = "酷编";
	} else {
		userName = datalist[l].userName;
	}
	;
	return `<li onclick="recommend(${datalist[l].id})">
                <div>
                    <p>${datalist[l].conTitle}</p>
                    <p>${userName}<span style="float: right;display: inline-block;">  ${pastTense(datalist[l].createDate)}</span></p>
                </div>
                <img src="${datalist[l].conImg}"/>
           </li>`
}
;

/**
 *推荐方法
 */

function recommend(ids) {
	window.location.href = "discover2.html?conId=" + ids + "&cindex=" + cindex;
	return
}
;

function facebook(){
    alert("暂未开放！")
}

/**
 *分享 到微信
 */
function shareWeixing(scene) {
	var url = serverPath + "/discover2.html?conId=" + data_arr;
	var json = {
		thumbImg : con_img, //缩略图地址Url(大小必须小于32k)
		wedpageUrl : url, //链接的地址
		scene : scene, //发送的目标场景 0-朋友   1-朋友圈
		title : con_title, //标题
		description : con_remark //描述
	}
	shareWeixin(json);
}

/**
 *语音阅读
 * ---------------------------------
 */
$(".tts").click(function() {
	ttsIconRotate();
})

/**
 *tts图标旋转
 * 
 */
var ttsc = 0;
var ttsivl = null;
function ttsIconRotate() {
	var t = document.getElementById('tts_img_btn');
	if (ttsivl == null) {
		ttsivl = setInterval(function() {
			current = (current + 10) % 360;
			t.style.transform = 'rotate(' + current + 'deg)';
		}, 200);
		$("#tts_autio_id")[0].play();
	} else {
		clearInterval(ttsivl);
		ttsivl = null;
		$("#tts_autio_id")[0].pause();
	}
}


function loadTTS() {
	var tts_txt = "标题：" + con_title + "。摘要：" + con_remark + "。" + $('.presentation').text();
	if (tts_txt.length < 1) {
		return;
	}
	addAudio(tts_txt);
}

function addAudio(text) {
	var url = serverPath + "getTtsMp3";
	$.ajax({
		url : url,
		type : 'POST',
		dataType : "json",
		data : {
			"str" : text,
			"conid" : data_arr
		},
		success : function(data) {
			var audhtml = '<audio id="tts_autio_id">';
			audhtml += '<source id="tts_source_id" src="' + data.msg + '" type="audio/mpeg">';
			audhtml += '</audio>';
			$("#tts_div").html(audhtml);

		},
		failure : function(data) {
			alert("失败--" + data);
		}
	});
}
;

function downloadApp() {
	window.location.href = "https://app.kuedit.com/KuBianImg/kubian.apk";
}
;

document.addEventListener('DOMContentLoaded', function() {
	function audioAutoPlay() {
		setTimeout(function() {

			var audio_ = $("#mp3_audio")[0];
			audio_.load();
			audio_.play();
			document.addEventListener("WeixinJSBridgeReady", function() {
				audio_.play();
			}, false);
		}, 500);
	}
	audioAutoPlay();
});
