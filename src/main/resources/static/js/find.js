var ton = sessionStorage.getItem("ton"); // colid的值
// colid的值
var pageNums = 0; // 总页数

//弹出隐藏层
function ShowDiv(show_div) {
	document.getElementById(show_div).style.display = 'block';
	getStick('1');
};
// 顶部图片

// 新增内容
function ShowDiv1(id, conImg, conHtml, title, remark, conVideoPath, isUrl, linkUrl, conLivePath, colId, colName,playCount) {
	//	$('.layui-bg-red').css('background-color','#FF5722');
	// 栏目类型
	var num = sessionStorage.getItem("ton1");
	$("#video_mytitle").html(null);
	$("#img_mytitle").html(null);
	if(colId == undefined || null == colId || '' == colId) {
		colId = sessionStorage.getItem("ton");
	}
	if(num == 1) {
		$(".panel-body").html(null); //编辑框内容设为空
		document.getElementById('save_news').style.display = 'block';
		getContent(num, id, conImg, conHtml, title, remark, conVideoPath, isUrl, linkUrl, conLivePath, colId, colName,playCount);
	} else if(num == 2) {
		document.getElementById('add_zb').style.display = 'block';
		getContent(num, id, conImg, conHtml, title, remark, conVideoPath, isUrl, linkUrl, conLivePath, colId, colName,playCount);
	} else if(num == 3) {
		document.getElementById('add_db').style.display = 'block';
		getContent(num, id, conImg, conHtml, title, remark, conVideoPath, isUrl, linkUrl, conLivePath, colId, colName,playCount);
	}

}
//关闭弹出层
function CloseDiv(show_div) {
	document.getElementById(show_div).style.display = 'none';
	$(".layui-progress-big").hide();
	// 一级目录的id
}

// 获取一级目录
// 获取目录
function getMulu(type, colId) {
	$.ajax({
		type: "post",
		url: getRequestIp() + "getColumnByTypes",
		async: true,
		data: {
			'colLink': type
		},
		dataType: "json",
		success: function(data) {
			if(data.success) {
				$('.opti').remove();
				var item;
				for(var i = 0; i < data.list.length; i++) {
					if(colId != undefined && colId == data.list[i].id) {
						if(data.list[i].colName == '关注') {
							continue;
						}
						item = "<option class='opti' selected='selected' value=" + data.list[i].id + ">" + data.list[i].colName + "</option>";
					} else {
						if(data.list[i].colName == '关注') {
							continue;
						}
						item = "<option class='opti' value=" + data.list[i].id + ">" + data.list[i].colName + "</option>";
					}
					if(colId == 0) {
						$('#sele0').append(item);
					}
					if(type == 1) {
						$('#sel1').append(item);
					} else if(type == 2) {
						$('#sel3').append(item);
					} else if(type == 3) {
						$('#sel5').append(item);
					}

				}
			} else {
				alert('异常错误!');
			}

		},
		error: function(data) {
			//			alert("服务器异常!")
		}
	});

}
//// 获取二级栏目
//function getColumns(catalogId, type) {
//	$.ajax({
//		type : "post",
//		url : getRequestIp() + "ContentAction!getColumnByLevelNull.action",
//		async : true,
//		data : {
//			type : catalogId
//		},
//		dataType : "json",
//		success : function(data2) {
//			$('.opt').remove();
//			var str;
//			for (var j = 0; j < data2.length; j++) {
//				str = "<option class='opt' value=" + data2[j].id + ">" + data2[j].colName + "</option>";
//
//				if (type == 1) {
//					$('#sel2').append(str);
//				} else if (type == 2) {
//					$('#sel4').append(str);
//				} else if (type == 3) {
//					$('#sel6').append(str);
//				} else if (type == 0) {
//					$('#sele0').append(str);
//				}
//
//			}
//		},
//		error : function(data2) {
//			//			alert("服务器异常!")
//		}
//	});
//}
// 提交的请求
function req(videotext, conImg, conVideoPath, id, imgUrl, praiseCount, playCount, appUserId, state, hot, conHtml, conTitle, conRemark, colId, conLivePath, linkUrl) {
	//	$('.layui-progress-text').html(null);
	//	$('.layui-bg-red').css('background-color','white');
	$.ajax({
		type: "post",
		url: getRequestIp() + "addHeadLineContent",
		async: true,
		data: {
			'conVideoPath': videotext,
			'conImg': conImg,
			'conVideoPath': conVideoPath,
			'id': id,
			'imgUrl': imgUrl,
			'praiseCount': praiseCount,
			'playCount': playCount,
			'appUserId': appUserId,
			'state': state,
			'hot': hot,
			'conHtml': conHtml,
			'conTitle': conTitle,
			'conRemark': conRemark,
			'colId': colId,
			'conLivePath': conLivePath,
			'linkUrl': linkUrl,
			'redact': 1
		},
		dataType: "json",
		success: function(data) {
			if(data.success) {
				alert('添加成功!');

				document.getElementById('save_news').style.display = 'none';
				document.getElementById('add_zb').style.display = 'none';
				//				document.getElementById('add_db').style.display = 'none';
				$(".layui-progress-big").hide();
				//				sessionStorage.setItem("conImg", "");
				//				sessionStorage.setItem('start_zt','');
				$('.state_cl').html(null);
				$('#video_mytitle').html(null);
				$('#img_mytitle').html(null);
				ajax1(ton);
				pageReady(pageNums);
				ajax2(1, ton);
			}
		}
	});
}
// 新增内容
function getContent(num, id, conImg, conHtml, title, remark, conVideoPath, isUrl, linkUrl, conLivePath, colId, colName,playCount) {
	if(num == 1) {
		$('#intitle').val(title);
		$('#inconRemark').val(remark);
		$(".panel-body").html(conHtml);
		//		sessionStorage.setItem("conImg", conImg);
		$('#play_count').val(playCount);
		sessionStorage.setItem("id", id);
		// 获取一级目录       
		getMulu(num, colId);
	}
	if(num == 2) {
		$('#intitle3').val(title);
		$('#inconRemark3').val(remark);
		$('#link').val(linkUrl);
		$('#Sinatv').val(conLivePath);
		$('#play_count').val(playCount);
		//		sessionStorage.setItem("conImg", conImg);
		sessionStorage.setItem("imgUrl", isUrl);
		sessionStorage.setItem("id", id);
		getMulu(num, colId);
	}
	if(num == 3) {
		$('#intitle4').val(title);
		$('#inconRemark4').val(remark);
		$('#bunch').val(conVideoPath);
		$('#link5').val(linkUrl);
		$('#play_count').val(playCount);
		//		sessionStorage.setItem("conImg", conImg);
		sessionStorage.setItem("imgUrl", isUrl);
		sessionStorage.setItem("conVideoPath", conVideoPath);
		sessionStorage.setItem("id", id);
		getMulu(num, colId);
	}
}
// 修改内容

// 顶部图片显示
function getStick(hot) {
	var num = sessionStorage.getItem("ton");
	$('.table1 tr td').remove();
	$.ajax({
		type: "post",
		url: getRequestIp() + "queryHotContent",
		data: {
			'topId': num,
			'hot': hot
		},
		async: true,
		dataType: "json",
		success: function(data) {
			var item;
			for(var i = 0; i < data.list.length; i++) {
				var data2 = data.list[i];
				var timesjc = data2.createDate;
				var dataTime = new Date(timesjc);
				var year = dataTime.getFullYear();
				var moth = dataTime.getMonth();
				if(moth < 10) {
					moth = "0" + moth;
				}
				var day = dataTime.getDate();
				if(day < 10) {
					day = "0" + day;
				}
				var time = year + "-" + moth + "-" + day;
				var contitle = data2.conTitle;
				if(contitle.length > 10) {
					contitle = contitle.slice(0, 10);
				}
				var conremark = data2.conRemark;
				if(conremark.length > 10) {
					conremark = conremark.slice(0, 10)
				}
				item += '<tr>'
				item += '<td class="ttd">' + data2.id + '</td>'
				item += '<td>' + contitle + '</td>'
				item += '<td>' + conremark + '</td>'
				item += '<td class="timg"><img class="simg" src="' + data2.conImg + '" /><img class="simg1" src="' + data2.conImg + '" /></td>'
				item += '<td>' + time + '</td>'
				if(data2.userName == null) {
					item += '<td></td>'
				} else {
					item += '<td>' + data2.userName + '</td>'
				}
				item += '<td>' + data2.praiseCount + '</td>'
				item += '<td>' + data2.playCount + '</td>'
				item += '<td>' + data2.exaCount + '</td>'
				item += '<td style="display: none;" class="ttd_hot">' + data2.hot + '</td>'
				item += '<td class="timg" id="ttimg"><img src="images/qx.png" title="取消" /></td>'
				item += '</tr>';
			}
			$('.table1').append(item);
		},
		error: function(data) {
			//			alert("服务器异常!")
		}
	});
}
// 模糊查询
function fuzzyQery(ton) {
	$("#button3").click(function() {
		var tonid = sessionStorage.getItem("ton"); // 需要查询的当前对象的colid
		var qery = $(".ins").val(); // 需要查询的作者或标题
		ajax1(tonid, 1, null, qery, null);
		pageReady(pageNums, tonid, 1, null, qery, null);
		ajax2(1, tonid, 1, null, qery, null);
		sessionStorage.setItem("search", 1);
		sessionStorage.setItem("qery", qery);
	});
}
// 顶部滚动图片和滚动头条
function delDb(id, tag) {
	$.ajax({
		type: "post",
		url: getRequestIp() + "delTopContent",
		async: true,
		data: {
			'topId': id,
			'tag': tag,
			'flag': '2'
		},
		dataType: "json",
		success: function(data) {
			if(data.success) {
				var ton = sessionStorage.getItem("ton");
				ajax1(ton);
				pageReady(pageNums);
				ajax2(1, ton);
				if(tag == 5) {
					getStick('2');
				} else {
					getStick('1');
				}

				//				alert("删除成功!");
			}
		},
		error: function(data) {
			//				alert("服务器异常!")
		}
	});
}
// 获取顶部内容
function getTopContent() {
	$.ajax({
		type: "post",
		url: getRequestIp() + "getFindConByColId",
		async: true,
		data: {
			'colId': ton
		},
		dataType: "json",
		success: function(data) {
			if(data.success) {
				$('.table1 tr td').remove();
				var item;
				for(var i = 0; i < data.list.length; i++) {
					var data2 = data.list[i];
					var timesjc = data2.createDate;
					var dataTime = new Date(timesjc);
					var year = dataTime.getFullYear();
					var moth = dataTime.getMonth();
					if(moth < 10) {
						moth = "0" + moth;
					}
					var day = dataTime.getDate();
					if(day < 10) {
						day = "0" + day;
					}
					var time = year + "-" + moth + "-" + day;
					var contitle = data2.conTitle;
					if(contitle.length > 10) {
						contitle = contitle.slice(0, 10);
					}
					var conremark = data2.conRemark;
					if(conremark.length > 10) {
						conremark = conremark.slice(0, 10)
					}
					item += '<tr>'
					item += '<td class="ttd">' + data2.id + '</td>'
					item += '<td>' + contitle + '</td>'
					item += '<td>' + conremark + '</td>'
					item += '<td class="timg"><img class="simg" src="' + data2.conImg + '" /><img class="simg1" src="' + data2.conImg + '" /></td>'
					item += '<td>' + time + '</td>'
					if(data2.userName == null) {
						item += '<td></td>'
					} else {
						item += '<td>' + data2.userName + '</td>'
					}
					item += '<td>' + data2.praiseCount + '</td>'
					item += '<td>' + data2.playCount + '</td>'
					item += '<td>' + data2.exaCount + '</td>'
					item += '<td style="display: none;" class="ttd_hot">' + data2.hot + '</td>'
					item += '<td style="display: none;" class="ttd_top">' + data2.top + '</td>'
					item += '<td class="topimg"><img src="images/qx.png" title="取消" /></td>'
					item += '</tr>';
				}
				$('.table1').append(item);
			} else {
				alert(data.mag);
			}
		}
	});
}
// 置顶的请求
function zhiding(id, top, ton) {
	$.ajax({
		type: "post",
		url: getRequestIp() + "setTopById",
		async: true,
		data: {
			'id': id,
			'top1': top,
			'tag': 2,
			'colId': ton
		},
		dataType: "json",
		success: function(data) {
			if(data.success) {

				ajax1(ton);
				pageReady(pageNums);
				ajax2(1, ton);
				getTopContent();
				alert('设置成功！');
			} else {
				alert('设置失败' + data.msg);
			}
		}
	});
}
$(document).ready(function() {
	$('.banDel').show();
	ajax1(ton);
	pageReady(pageNums);
	ajax2(1, ton);
	getInfo();
	fuzzyQery(ton);
	var conte = sessionStorage.getItem("conte");
	// 删除顶部图片展示
	$('body').on('click', '#ttimg', function() {
		var id = $(this).siblings('.ttd').text(); //对象id
		if(confirm('确定取消序号为' + id + '的数据吗？')) {

			var hot = $(this).siblings('.ttd_hot').text();
			if(hot == 1) {
				delDb(id);
			} else if(hot == 2) {
				delDb(id, 5);
			}
		}
	});
	// 顶部内容top_content
	$('#top_content').click(function() {
		document.getElementById('top_img').style.display = 'block';
		getTopContent();
	});
	// 清除置顶的内容
	$('body').on('click', '.topimg', function() {
		var id = $(this).siblings('.ttd').text(); //对象id
		var top = $(this).siblings('.ttd_top').text();
		if(confirm("确定取消序号为" + id + "的数据吗？")) {
			zhiding(id, 1, ton);
		}
	});

	// 阅读yuedu
	$('body').on('click', '.yuedu', function() {
		var id = getnum(this, 0); //对象id
		window.open("/pc/displayDesign.html?conId=" + id);
	})
	// 置顶
	$('body').on('click', '.thot', function() {
		var id = getnum(this, 0);
		var top = getnum(this, 21);
		var state = getnum(this, 2);
		if(state == 0 || state == 3) {
			alert("隐藏的内容不能置顶！");
		} else {
			if(top == 1) {
				if(confirm("该内容已经是置顶内容，确定取消吗?")) {
					zhiding(id, 1, ton);
				}
			} else {
				zhiding(id, 0, ton);
			}
		}

	})
	// 滚动头条
	$('#roll_head').click(function() {
		document.getElementById('top_img').style.display = 'block';
		getStick('2');
	});
	//删除内容
	$('body').on('click', '.tdel', function() {
		var id = getnum(this, 0); //对象id
		if(confirm("确定删除序号为" + id + "的数据吗？")) {

			$.ajax({
				type: "post",
				url: getRequestIp() + "delColumnContentById",
				async: true,
				data: {
					'id': id
				},
				dataType: "json",
				success: function(data) {
					if(data.success) {
						ajax1(ton);
						pageReady(pageNums);
						ajax2(1, ton);
						//						alert('删除成功!');
					} else {
						alert('删除失败!');
					}
				},
				error: function(data) {
					//					alert("服务器异常!")
				}
			});
		}
	});
	// 修改内容
	$('body').on('click', '.tupdate', function() {
		var id = getnum(this, 0); //对象id
		//		var conImg = getnum(this, 3); // 缩略图地址
		//		var conHtml = getnum(this, 4); // 编辑框内容
		//		var title = getnum(this, 19); // 标题
		//		var remark = getnum(this, 20); // 摘要
		//		var conVideoPath = getnum(this, 6); //  点播地址
		//		var isUrl = getnum(this, 5); // 视频上方广告图地址
		//		var linkUrl = getnum(this, 9); // 链接地址
		//		var conLivePath = getnum(this, 10); // 直播地址
		var colId = sessionStorage.getItem("ton"); // 一级栏目id
		var colName = sessionStorage.getItem("conte"); // 一级栏目名称
		var statr = getnum(this, 2); //对象状态
		//		sessionStorage.setItem('start_zt',statr);
		$('.state_cl').html(statr);
		// 根据id获取数据
		$.ajax({
			type: "post",
			url: getRequestIp() + "getColContentById",
			data: {
				"id": id,
			},
			dataType: "json",
			async: true,
			success: function(data) {
				if(data.success) {
					data = data.list[0];
					var conImg = data.conImg;
					var conHtml = data.conHtml;
					var title = data.conTitle;
					var remark = data.conRemark;
					var conVideoPath = data.conVideoPath;
					var linkUrl = data.linkUrl;
					var conLivePath = data.conLivePath;
					var isUrl = data.imgUrl;
					var playCount = data.playCount;
					// 弹出修改的窗口
					ShowDiv1(id, conImg, conHtml, title, remark, conVideoPath, isUrl, linkUrl, conLivePath, colId, colName,playCount);
				} else {
					alert('异常错误！');
				}
			}
		});
	});
	// 评论管理
	$('body').on('click', '.tcontent', function() {
		var id = getnum(this, 0); //对象id
		document.getElementById("comment").style.display = 'block';
		comment(id);

	});
	//屏蔽评论内容
	$('body').on('click', '.pb', function() {
		var data_p = $(this).attr("data-p");
		var cid = getnum(this, 0); //对象id
		var state = getnum(this, 1); // 状态0显示 1 屏蔽
		if(state == 1) {
			var flag = confirm('已经是屏蔽状态,是否取消屏蔽？');
			if(flag) {
				setExaComment(cid, "setCommentState", data_p, "0");
			}
		} else {
			var flag = confirm('确定屏蔽吗？');
			if(flag) {
				setExaComment(cid, "setCommentState", data_p, "1");
			}
		}
	});
	// 隐藏
	$('body').on('click', '.thide', function() {
		var id = getnum(this, 0);
		var state = getnum(this, 2);
		if(state == 3) {
			if(confirm("该内容已经是隐藏状态，确定取消吗?")) {
				ajax3(id, 'hideContent');
			};
		} else {
			ajax3(id, 'hideContent');
		}
	});
	// 推送
	$('body').on('click', '.torder', function() {
		var tid = sessionStorage.getItem("ton1");
		var id = getnum(this, 0); // id
		//		var conTitle = getnum(this, 19); //标题
		//		var remark = getnum(this, 20); // 摘要
		//		var imgUrl = getnum(this, 5); // 视频上方广告图地址
		getMulu(tid, 0); //获取栏目

		$('#in_colid').val(id); // 设置id
		//		$('#in_imgUrl').val(imgUrl); // 设置视频上方广告图地址
		//		$('#in_conRemark').val(remark); // 设置摘要
		$('#push').css("display", "block");

	});
	// 推送的提交
	$('#push_asd').click(function() {
		var id = $('#in_colid').val();
		var backupConTitle = $('#in_title').val(); // 获取自己加的标题
		var backUpColId = $("#sele0 option:selected").val();
		console.log(backupConTitle + "===" + backUpColId);

		$.ajax({
			type: "post",
			url: getRequestIp() + "pushFindContent",
			async: true,
			data: {
				'id': id,
				'backupConTitle': backupConTitle,
				'isPush': backUpColId
			},
			dataType: "json",
			success: function(data) {
				alert(data.msg);
				$('#push').css("display", "none");
			}
		});
		//		pushContent();
	});
	// 推送的取消
	$('#push_qx').click(function() {
		$('#push').css("display", "none");
	});
	// 推送请求
	function pushContent() {
		var form = document.getElementById("form0");
		var fd = new FormData(form);
		$.ajax({
			url: getRequestIp() + "pushFindContent",
			'type': "POST",
			data: fd,
			processData: false, // 告诉jQuery不要去处理发送的数据
			contentType: false, // 告诉jQuery不要去设置Content-Type请求头
			dataType: "json",
			success: function(data) {
				if(data.success) {
					alert('推送成功!');
					$('#push').css("display", "none");
					ajax1(ton);
					pageReady(pageNums);
					ajax2(1, ton);
				} else {
					alert('异常错误！');
				}
			}
		});
	}
	// 设为滚动轮播内容
	$('body').on('click', '.tStick', function() {
		var id = getnum(this, 0);
		var hot = getnum(this, 1);
		var state = getnum(this, 2);
		if(state == 0 || state == 3) {
			alert("隐藏的内容不能设为轮播图片！");
		} else {
			if(hot == 1) {
				if(confirm("该内容已经是顶部滚动内容，确定取消吗?")) {
					delTopContent(id);
				}
			} else if(hot == 2) {
				alert('该内容已经是滚动头条不能设为顶部滚动图片!');
			} else {
				delTopContent(id);
			}
		}

	});

	// 设置滚动头条
	$('body').on('click', '.gun_h', function() {
		var id = getnum(this, 0);
		var hot = getnum(this, 1);
		var state = getnum(this, 2);
		if(state == 0 || state == 3) {
			alert("隐藏的内容不能设为滚动头条！");
		} else {
			if(hot == 2) {
				if(confirm("该内容已经是滚动头条，确定取消吗?")) {
					delTopContent(id, 5);
				};
			}
			if(hot == 1) {
				alert('该内容已经是顶部滚动图片不能设为滚动头条!');
			} else {
				delTopContent(id, 5);
			}
		}

	});
	// 设置延伸阅读
	$('body').on('click', '.tread', function() {
		var id = getnum(this, 0);
		$.ajax({
			type: "post",
			url: getRequestIp() + "setFurther",
			async: true,
			data: {
				'id': id
			},
			dataType: "json",
			success: function(data) {

				alert('设置延伸阅读成功!');
			},
			error: function(data) {
				alert("设置延伸阅读成功!")
			}
		});
	});
	$('body').on('mouseover', '.simg', function() {
		$(this).siblings('.simg1').css("display", "block");
	});
	$('body').on('mouseout', '.simg', function() {
		$(this).siblings('.simg1').css("display", "none");
	});

	// 顶部图片
	$('#button1').click(function() {
		$('#topDiv').css("display", "block");
	});
	$('.topOff').click(function() {
		$('#topDiv').css("display", "none");
	});
	// 上传顶部图片
	$('#topUpload').click(function() {
		var fileObj = document.getElementById('topFile').files[0]; // js 获取文件对象
		var formFile = new FormData();
		formFile.append("myUploadBg", fileObj); //加入文件对象

		if(typeof(fileObj) == "undefined" || fileObj.size <= 0) {
			alert("请选择图片");
			return;
		}
		$.ajax({
			type: 'post',
			data: formFile,
			url: getRequestIp() + 'ContentAction!findBg.action',
			cache: false,
			dataType: 'json',
			processData: false, //用于对data参数进行序列化处理 这里必须false
			contentType: false, //必须
			success: function(data) {
				if(data.success) {
					alert('上传成功!');
					$('#topDiv').css("display", "none");
				} else {
					alert('异常错误！');
				}
			}
		});
	});

	// 提交
	$('#asd').click(function() {
		var colId = $('#sel1').val(); // 目录id
		var conHtml = $(".panel-body").html(); // 编辑框内容
		//		var conImg = sessionStorage.getItem("conImg"); // 图片存储的地址
		var conImg = $('#img_mytitle').html();
		if(conImg == 'undefined') {
			conImg = null;
		}
		var conTitle = $('#intitle').val(); //标题
		var conRemark = $('#inconRemark').val(); // 摘要
		var videotext; //视频地址
		var collink = 1;
		var conVideoPath = $('#video_mytitle').html(); // 上传视频的地址
		if(conVideoPath != null && conVideoPath != "") {
			var lastSuffix = conVideoPath.substring(conVideoPath.lastIndexOf('.') + 1).toLocaleLowerCase();
			if(lastSuffix == 'mp4' || lastSuffix == 'mpg' || lastSuffix == 'avi' || lastSuffix == 'rm' || lastSuffix == 'rmvb' || lastSuffix == 'asf' || lastSuffix == 'dat') {
				var path = conVideoPath.substring(0, conVideoPath.lastIndexOf('.')) + '.jpg';
				conImg = path;
			} else {
				conVideoPath = null;
			}
		}
		var id = sessionStorage.getItem("id");
		if(id == 'undefined') {
			id = null;
		}
		var imgUrl;
		var praiseCount;
		var playCount = $('#play_count').val();;
		var appUserId;
		//		var state = sessionStorage.getItem('start_zt');  // 状态
		var state = $('.state_cl').html();
		if(state == "") {
			state = "1";
		}
		var hot;
		var conLivePath;
		var linkUrl;
		req(videotext, conImg, conVideoPath, id, imgUrl, praiseCount, playCount, appUserId, state, hot, conHtml, conTitle, conRemark, colId, conLivePath, linkUrl);
	});
	$('#asd3').click(function() {
		var colId = $('#sel3').val(); // 目录id
		var conHtml; // 编辑框内容
		//var conImg = sessionStorage.getItem("conImg"); // 图片存储的地址
		var conImg = $('#img_mytitle').html();
		if(conImg == 'undefined') {
			conImg = null;
		}
		var conTitle = $('#intitle3').val(); //标题
		var conRemark = $('#inconRemark3').val(); // 摘要
		var elm2; // 编辑框内容
		var videotext; //视频地址
		var isDraft = 1;
		var collink = 2;
		var conVideoPath = $('#video_mytitle').html(); // 上传视频的地址
		if(conVideoPath != null && conVideoPath != "") {
			var lastSuffix = conVideoPath.substring(conVideoPath.lastIndexOf('.') + 1).toLocaleLowerCase();
			if(lastSuffix == 'mp4' || lastSuffix == 'mpg' || lastSuffix == 'avi' || lastSuffix == 'rm' || lastSuffix == 'rmvb' || lastSuffix == 'asf' || lastSuffix == 'dat') {
				var path = conVideoPath.substring(0, conVideoPath.lastIndexOf('.')) + '.jpg';
				conImg = path;
			} else {
				conVideoPath = null;
			}
		}
		var id = sessionStorage.getItem("id");
		if(id == 'undefined') {
			id = null;
		}
		//var imgUrl = sessionStorage.getItem("imgUrl"); // 视频上方广告图片
		var imgUrl;
		if(imgUrl == 'undefined') {
			imgUrl = null;
		}
		var praiseCount;
		var playCount = $('#play_count').val();;
		var appUserId;
		//		var state = sessionStorage.getItem('start_zt');  // 状态
		var state = $('.state_cl').html();
		if(state == "") {
			state = "1";
		}
		var hot;
		var conLivePath = $('#Sinatv').val(); // 直播地址
		var linkUrl = $('#link').val(); // 链接地址
		var type = 2;
		req(videotext, conImg, conVideoPath, id, imgUrl, praiseCount, playCount, appUserId, state, hot, conHtml, conTitle, conRemark, colId, conLivePath, linkUrl);
	});

});
// 点中当前行变色
function bs(Object) {
	$('.trr').css('background-color', 'white');
	$(Object).css('background-color', '#DFE8F6');
}

// 屏蔽评论
function setExaComment(cid, url, data_p, state) {
	$.ajax({
		type: "post",
		url: getRequestIp() + url,
		async: true,
		data: {
			'id': cid,
			'state': state
		},
		dataType: "json",
		success: function(data) {
			if(data.success) {
				comment(data_p);
			} else {
				alert('异常错误！');
			}
		}
	});
}
// 评论管理
function comment(id) {
	var nums = 1;
	$.ajax({
		type: "post",
		url: getRequestIp() + "getComments",
		async: true,
		data: {
			'start': 0,
			'limit': 10,
			'conId': id
		},
		dataType: "json",
		success: function(data) {
			apply(data.list, id);
			nums = data.totalSize;
			var pages = nums / 10;
			pages = Math.ceil(pages) * 1;
			//分页
			$("#page").paging({
				pageNo: 1,
				totalPage: pages,
				totalSize: nums,
				callback: function(num) {
					$.ajax({
						type: "post",
						url: getRequestIp() + "getComments",
						async: true,
						data: {
							'start': num - 1,
							'limit': 10,
							'conId': id
						},
						dataType: "json",
						success: function(datas) {
							if(datas.success) {
								apply(datas.list, id);
							} else {
								alert('异常错误！');
							}

						}
					});
				}
			});

		}
	});
	// 渲染数据
	function apply(data, idw) {
		var st = "";
		$('.table3 tr td').remove();
		for(var i = 0; i < data.length; i++) {
			var zt = "";
			var timesjc = data[i].createDate;
			var date = new Date(timesjc); //时间戳为10位需*1000，时间戳为13位的话不需乘1000
			YY = date.getFullYear() + '-';
			MM = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
			DD = date.getDate() + ' ';
			hh = date.getHours() + ':';
			mm = date.getMinutes() + ':';
			ss = date.getSeconds();
			var time = YY + MM + DD + hh + mm + ss;
			console.log(time);
			st += '<tr class="trr">'
			st += '<td>' + data[i].id + '</td>' //id
			st += '<td style="display: none;">' + data[i].state + '</td>' // 状态 0显示 1 屏蔽
			st += '<td>' + data[i].content + '</td>' // 内容
			st += '<td>' + time + '</td>' // 时间
			st += '<td>' + data[i].ip + '</td>' // ip
			if(data[i].state == 0) {
				zt = "显示";
			} else {
				zt = "隐藏";
			}
			st += '<td >' + zt + '</td>' // 状态
			st += '<td style="padding-left: 20px;"><img data-p="' + idw + '" class="pb" src="img/dialog_remove.png" title="屏蔽"/></td>' //操作
			st += '</tr>';
		}
		$('.table3').append(st);
	}
}
// 滚动图片和滚动头条
function delTopContent(id, tag) {
	$.ajax({
		type: "post",
		url: getRequestIp() + "delTopContent",
		async: true,
		data: {
			'topId': id,
			'tag': tag,
			'flag': '2'
		},
		dataType: "json",
		success: function(data) {
			if(data.success) {
				ajax1(ton);
				pageReady(pageNums);
				ajax2(1, ton);
				alert('操作成功!');
			} else {
				alert('异常错误！');
			}
		}
	});
}
// 隐藏 排序 请求
function ajax3(id, address, sort) {
	$.ajax({
		type: "post",
		url: getRequestIp() + address,
		async: true,
		data: {
			'id': id,
			'sort': sort
		},
		dataType: "json",
		success: function(data) {
			if(data.success) {
				ajax1(ton);
				pageReady(pageNums);
				ajax2(1, ton);
				alert('操作成功!');
			} else {
				alert('操作失败!');
			}
		},
		error: function(data) {
			//			alert("服务器异常!")
		}
	});
}
// 获取对象的某个值
function getnum(obj, num) {
	return $(obj).parent().parent().children().eq(num).html();
}
// 根据不同的子目录获取数据
function getInfo() {
	$(".filter").change(function() {
		// 把模糊查询的参数设为空
		sessionStorage.setItem("search", "");
		sessionStorage.setItem("qery", "");
		var num = $(this).val();
		sessionStorage.setItem("ton", num);
		ajax1(num);
		pageReady(pageNums);
		ajax2(1, num);
	});
}

// 得到数据的总页数
function ajax1(num, search, appuser, search_str, state) {
	$.ajax({
		type: "post",
		url: getRequestIp() + "queryFindLisAltert",
		data: {
			'id': num,
			'limit': 10,
			'start': 0,
			'search': search,
			'appuser': appuser,
			'search_str': search_str,
			'state': state
		},
		async: true,
		dataType: "json",
		success: function(data) {
			if(data.success) {
				var totalSize = data.totalSize;
				var tsize = totalSize / 10;
				pageNums = Math.ceil(tsize);
				pageReady(pageNums);
			} else {
				alert('异常错误！');
			}

		},
		error: function(data) {
			alert("服务器异常!")
		}
	});
}
//渲染数据
function ajax2(clickPage, num, search, appuser, search_str, state) {
	if(clickPage == undefined) {
		clickPage = 1;
	}
	$('.table tr td').remove();
	$.ajax({
		type: "post",
		url: getRequestIp() + "queryFindLisAltert",
		data: {
			'id': num,
			'limit': 10,
			'start': clickPage - 1,
			'search': search,
			'appuser': appuser,
			'search_str': search_str,
			'state': state
		},
		async: true,
		dataType: "json",
		success: function(data) {
			if(data.msg == '没有数据！') {
				alert('没有数据！');
			}
			if(data.success) {
				var item = "";
				$.each(data.list, function(i, result) {
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
					var contitle = result.conTitle;
					if(contitle.length > 10) {
						contitle = contitle.slice(0, 10);
					}
					var conremark = result.conRemark;
					if(conremark.length > 10) {
						conremark = conremark.slice(0, 10)
					}
					var tonn = sessionStorage.getItem("ton1");

					item += '<tr onclick="bs(this)" class="trr">'
					item += '<td class="tid">' + result.id + '</td>'
					item += '<td style="display: none;">' + result.hot + '</td>'
					item += '<td style="display: none;">' + result.state + '</td>'
					item += '<td style="display: none;">' + result.conImg + '</td>'
					item += '<td style="display: none;">' + result.conHtml + '</td>'
					item += '<td style="display: none;">' + result.imgUrl + '</td>'
					item += '<td style="display: none;">' + result.conVideoPath + '</td>'
					item += '<td>' + contitle + '</td><td>' + conremark + '</td>'
					item += '<td style="display: none;">' + result.linkUrl + '</td>'
					item += '<td style="display: none;">' + result.conLivePath + '</td>'
					item += '<td class="timg"><img class="simg" src="' + result.conImg + '" /><img class="simg1" src="' + result.conImg + '" /></td>'
					item += '<td>' + time + '</td>'
					if(result.userName == null) {
						item += '<td></td>'
					} else {
						item += '<td>' + result.userName + '</td>'
					}
					item += '<td>' + result.praiseCount + '</td>'
					item += '<td>' + result.playCount + '</td>'
					item += '<td>' + result.exaCount + '</td>'
					item += '<td>'
					item += '<img class="yuedu" src="img/yuedu.png" title="阅读" />&nbsp;'
					item += '<img class="tupdate" src="img/update.png" title="修改" />&nbsp;'
					item += '<img class="tcontent" src="img/coin17.png" title="评论管理"/> &nbsp;'
					item += '<img class="tdel" src="img/delete.png" title="删除" /> &nbsp;'
					item += '<img class="thide" src="img/hide.png" title="隐藏" /> &nbsp;'
					item += '<img class="torder" src="img/push.png" title="推送" /> &nbsp;'
//					if(tonn == 1 || tonn == 2) {
//						item += '<img class="tStick" src="img/gd.png" title="设为滚动图片" /> &nbsp;'
//					}
					item += '<img class="thot" src="img/hot.png" title="置顶" /> &nbsp;'
					item += '<img class="gun_h" src="images/gunH.png" title="设为滚动头条" /> &nbsp;'
					item += '</td>'
					item += '<td style="display: none;">' + result.colId + '</td>'
					item += '<td style="display: none;">' + result.conTitle + '</td>'
					item += '<td style="display: none;">' + result.conRemark + '</td>'
					item += '<td style="display: none;" class="rtop">' + result.top + '</td>'
					item += '<td style="display: none;">' + result.conRemark + '</td>'
					item += '</tr>';
				});
				$('.table').append(item);

				$('.banDel').hide();
			} else {
				//				alert('异常错误！');
			}

		},
		error: function(data) {
			//			alert("服务器异常!")
		}
	});
}
// 分页插件
var pageNavObj = null;

function pageReady(pageNums) {
	//初始化
	//pageNavCreate("PageNav",200,1,pageNavCallBack);
	pageNavObj = new PageNavCreate("PageNavId", {
		pageCount: pageNums, //总页数
		currentPage: 1, //当前页
		perPageNum: 5, //每页按钮数
	})
	pageNavObj.afterClick(pageNavCallBack);
};
//			jQuery(document).ready(function($) {
//				alert(pageNums)
//				//初始化
//				//pageNavCreate("PageNav",200,1,pageNavCallBack);
//				pageNavObj = new PageNavCreate("PageNavId", {
//					pageCount: pageNums, //总页数
//					currentPage: 1, //当前页
//					perPageNum: 5, //每页按钮数
//				});
//				pageNavObj.afterClick(pageNavCallBack);
//			});

//翻页按钮点击后触发的回调函数
function pageNavCallBack(clickPage) {
	//clickPage是被点击的目标页码
	//console.log(clickPage);
	var toni = sessionStorage.getItem("ton");
	var search = sessionStorage.getItem("search");
	var qery = sessionStorage.getItem("qery");
	ajax2(clickPage, toni, search, null, qery, null);
	//一般来说可以在这里通过clickPage,执行AJAX请求取数来重写页面

	//最后别忘了更新一遍翻页导航栏
	//pageNavCreate("PageNav",pageCount,clickPage,pageNavCallBack);
	pageNavObj = new PageNavCreate("PageNavId", {
		pageCount: getPageSet().pageCount, //总页数
		currentPage: clickPage, //当前页
		perPageNum: getPageSet().perPageNum, //每页按钮数
	});
	pageNavObj.afterClick(pageNavCallBack);
}

function getPageSet() {
	var obj = {
		pageCount: null, //总页数
		currentPage: null, //当前页
		perPageNum: null, //每页按钮数
	}
	if($("#testPageCount").val() && !isNaN(parseInt($("#testPageCount").val()))) {
		obj.pageCount = parseInt($("#testPageCount").val());
	} else {
		obj.pageCount = parseInt($(".page-input-box > input").attr("placeholder"));
	}

	if($("#testCurrentPage").val() && !isNaN(parseInt($("#testCurrentPage").val()))) {
		obj.currentPage = parseInt($("#testCurrentPage").val());
		obj.currentPage = (obj.currentPage <= obj.pageCount ? obj.currentPage : obj.pageCount);
	} else {
		obj.currentPage = 1;
	}

	if($("#testPerPageNum").val() && !isNaN(parseInt($("#testPerPageNum").val()))) {
		obj.perPageNum = parseInt($("#testPerPageNum").val());
	} else {
		obj.perPageNum = null;
	}

	return obj;
}

$(function() {
	$('.summernote').summernote({
		height: 350,
		tabsize: 2,
		lang: 'zh-CN'
	});
});