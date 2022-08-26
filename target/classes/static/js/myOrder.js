var ton = sessionStorage.getItem("ton"); // colid的值

var userId = sessionStorage.getItem("user_login"); // 当前登录的用户的id

var appUserId = sessionStorage.getItem("app_user_login"); //当前登录的app用户的id
// colid的值
var pageNums = 0; // 总页数

//弹出隐藏层
function ShowDiv(show_div) {
	document.getElementById(show_div).style.display = 'block';
};
// 顶部图片

// 新增内容
function ShowDiv1(id, conImg, conHtml, title, remark, conVideoPath, isUrl, linkUrl, conLivePath, colId, colName) {

	//	$(".panel-body").html(null); //编辑框内容设为空
	document.getElementById('save_news').style.display = 'block';
	//	getContent(id, conImg, conHtml, title, remark, conVideoPath, isUrl, linkUrl, conLivePath, colId, colName);

}
//关闭弹出层
function CloseDiv(show_div) {
	document.getElementById(show_div).style.display = 'none';
	$(".layui-progress-big").hide();
	// 一级目录的id
}

// 提交的请求
function req() {
	layui.use(['layer', 'form'], function() {
		var layer = layui.layer,
			form = layui.form;
		var form = document.getElementById("order_from");
		var fo = new FormData(form);
		$.ajax({
			cache: true,
			type: "POST",
			url: getRequestIp() + "addOrder",
			dataType: "json",
			data: fo,
			processData: false,
			contentType: false,
			error: function(request) {
				layer.msg('服务器错误', {
					icon: 2
				});
			},
			success: function(data) {
				layer.msg(data.msg);
				if(data.success) {
					$('#save_news').css('display', 'none');
					ajax1($("#bid").val());
					pageReady(pageNums);
					ajax2(1, $("#bid").val());
				}

			}
		});
	});

}

// 模糊查询
function fuzzyQery(ton) {
	$("#button3").click(function() {
		var tonid = $("#bid").val(); // 当前商铺的id
		var qery = $(".ins").val(); // 需要查询的订单编号
		ajax1(tonid, 1, null, qery, null);
		pageReady(pageNums);
		ajax2(1, tonid, 1, null, qery, null);
		sessionStorage.setItem("search", 1);
		sessionStorage.setItem("qery", qery);
		sessionStorage.removeItem('order_state');
	});
}

// 获取layer
function getlayer() {
	var layer = "";
	layui.use(['layer', 'form'], function() {
		layer = layui.layer,
			form = layui.form;

	});
	return layer;
}

// 渲染分类的数据
function getclassify() {
	layui.use(['layer', 'form'], function() {
		var layer = layui.layer,
			form = layui.form;

		//			layer.alert('见到你真的很高兴', {
		//				icon: 6
		//			});
		// 根据用户id获取用户的商铺信息
		$('.col_tab tr td').remove();
		$.ajax({
			type: "post",
			url: getRequestIp() + "getBusinessByUserId",
			async: true,
			data: {
				'appUserId': appUserId
			},
			dataType: 'json',
			success: function(data1) {
				if(data1.success) {
					layer.msg('获取成功！');
					// 根据商铺id获取分类
					var id = data1.list[0].id;
					$.ajax({
						type: "post",
						url: getRequestIp() + "getClassifyBybid",
						async: true,
						data: {
							'id': id
						},
						dataType: 'json',
						success: function(datas) {
							if(datas.success) {
								layer.msg('获取成功！');
								var item = "";
								$.each(datas.list, function(i, result) {
									item += '<tr>'
									item += '<td class="tid">' + result.id + '</td>'
									item += '<td class="col_name">' + result.name + '</td>'
									item += '<td class="bid" style="display: none;">' + result.bid + '</td>'
									item += '<td>'
									item += '<img class="classif_update" src="img/update.png" title="修改" />&nbsp;&nbsp;'
									item += '<img class="classif_del" src="img/delete.png" title="删除" /> &nbsp;'
									item += '</td>'
									item += '<tr>'

								});
								$('.col_tab').append(item);

							} else {
								layer.msg('异常错误！');
							}
						}
					});
				} else {
					layer.msg('异常错误！');
				}
			}
		});

		$(".banDel_lm").show();
	});

}
$(document).ready(function() {
	$('.banDel').show();

	var conte = sessionStorage.getItem("conte");
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
			$("#bid").val(data.list[0].id);
			ajax1($("#bid").val());
			pageReady(pageNums);
			ajax2($("#bid").val());
			getInfo();
			fuzzyQery($("#bid").val());
		}
	});

	// 修改订单的提交提交
	$('#asd').click(function() {
		req();
	});
	$('#closd').click(function() {
		layui.use(['layer', 'form'], function() {
			var layer = layui.layer,
				form = layui.form;

			//			layer.msg('Hello World');

		});

		$(".banDel_lm").hide();
	});
	// 根据订单状态获取订单
	$('.order_select').change(function() {
		var state = $('.order_select').val(); //订单状态
		var bid = $("#bid").val(); // 商铺id
		ajax1(bid, 1, null, null, state);
		pageReady(pageNums);
		ajax2(1, bid, 1, null, null, state);
		sessionStorage.setItem("order_state", state);
		sessionStorage.removeItem('qery');
	});

	//删除订单
	$('body').on('click', '.tdel', function() {
		var id = getnum(this, 0); //对象id
		layui.use(['layer', 'form'], function() {
			var layer = layui.layer,
				form = layui.form;
			if(confirm("确定删除序号为" + id + "的订单吗？")) {

				$.ajax({
					type: "post",
					url: getRequestIp() + "deleteOrder",
					async: true,
					data: {
						'id': id
					},
					dataType: "json",
					success: function(data) {
						layer.msg(data.msg);
						ajax1($("#bid").val());
						pageReady(pageNums);
						ajax2(1, $("#bid").val());
						//						alert('删除成功!');
					},
					error: function(data) {
						//					alert("服务器异常!")
					}
				});
			}
		});

	});

	// 修改订单信息
	$('body').on('click', '.tupdate', function() {
		var id = getnum(this, 0); //对象id
		$("#id").val(id);
		var orderNumber = getnum(this, 1); //订单编号
		$("#orderNumber").val(orderNumber);
		var receiverName = getnum(this, 2); //买家姓名
		$('#receiverName').val(receiverName);
		var receiverPhone = getnum(this, 3); //买家电话
		$('#receiverPhone').val(receiverPhone);
		var receiverAddress = getnum(this, 4); //收货地址
		$('#receiverAddress').val(receiverAddress);
		var remark = getnum(this, 6); //订单备注
		$('#remark').val(remark);
		var expressNum = getnum(this, 8); //物流单号
		$('#expressNum').val(expressNum);
		var carriage = getnum(this, 9); //运费
		$('#carriage').val(carriage);
		var sumPrice = getnum(this, 10); //商品总价
		$('#sumPrice').val(sumPrice);
		var realPrice = getnum(this, 11); //订单总价
		$('#realPrice').val(realPrice);
		var state = getnum(this, 16); //订单状态 0:未付款 1:待发货 2:待收货 3:待评价 4:已评价
		document.getElementsByClassName('order_option')[state].selected = true;

		// 弹出修改的窗口
		ShowDiv1();
	});

	//查看订单详情
	$('body').on('click', '.show_details', function() {
		var id = getnum(this, 0); //对象id
		$('.order_id').val(id);
		getOrderDetal(id);
	});

	// 删除订单详情
	$('body').on('click', '.details_del', function() {
		var id = getnum(this, 0);
		if(confirm("确定删除序号为" + id + "的详情订单吗？")) {
			layui.use(['layer', 'form'], function() {
				var layer = layui.layer,
					form = layui.form;
				$.ajax({
					type: "post",
					url: getRequestIp() + 'delOrderBetail',
					async: true,
					data: {
						'id': id
					},
					dataType: "json",
					success: function(data) {

						if(data.success) {
							getOrderDetal($('.order_id').val());
							layer.msg('删除成功！');
						} else {
							layer.msg(data.msg);
						}
					}
				});
			});
		}
	});
	// 修改订单详情
	$('body').on('click', '.details_update', function() {
		$('#add_zb').css('display', 'block');
		var id = getnum(this, 0); // 详情id
		$('#did').val(id);
		var name = getnum(this, 1); //商品名称
		$('#shop_name').val(name);
		var shop_specs = getnum(this, 2); //规格名称
		$('#shop_specs').val(shop_specs);
		var shop_count = getnum(this, 3); //商品数量
		$('#shop_count').val(shop_count);
		var shop_price = getnum(this, 4); //商品价格
		$('#shop_price').val(shop_price);
		var shop_carriage = getnum(this, 5); //运费
		$('#shop_carriage').val(shop_carriage);

	});

	// 订单详情的提交
	$('#shop_submit').click(function() {
		layui.use(['layer', 'form'], function() {
			var layer = layui.layer,
				form = layui.form;
			var form = document.getElementById("order_detail_from");
			var fo = new FormData(form);
			$.ajax({
				cache: true,
				type: "POST",
				url: getRequestIp() + "updateOrderBetail",
				dataType: "json",
				data: fo,
				processData: false,
				contentType: false,
				error: function(request) {
					layer.msg('服务器错误', {
						icon: 2
					});
				},
				success: function(data) {
					layer.msg(data.msg);
					if(data.success) {
						$('#add_zb').css('display', 'none');
						getOrderDetal($('.order_id').val());
					}

				}
			});
		});
	});
	$('body').on('mouseover', '.simg', function() {
		$(this).siblings('.simg1').css("display", "block");
	});
	$('body').on('mouseout', '.simg', function() {
		$(this).siblings('.simg1').css("display", "none");
	});

});
// 获取订单详情
function getOrderDetal(id) {

	$('#top_img').show();
	layui.use(['layer', 'form'], function() {
		var layer = layui.layer,
			form = layui.form;
		$('.table1 tr td').remove();
		$.ajax({
			type: "post",
			url: getRequestIp() + "getOrderBetailByOederId",
			async: true,
			data: {
				'orderid': id
			},
			dataType: "json",
			success: function(data) {
				layer.msg(data.msg);
				if(data.success) {
					var item = '';
					$.each(data.list, function(i, result) {
//						var timesjc = result.payTime; //付款时间
//						var ptime = '';
//						if(timesjc != null) {
//							var dataTime = new Date(timesjc);
//							var year = dataTime.getFullYear();
//							var moth = dataTime.getMonth() + 1;
//							if(moth < 10) {
//								moth = "0" + moth;
//							}
//							var day = dataTime.getDate();
//							if(day < 10) {
//								day = "0" + day;
//							}
//							ptime = year + "-" + moth + "-" + day;
//						}
//
//						var timesjc1 = result.deliveryTime; //发货时间
//						var dtime = '';
//						if(timesjc1 != null) {
//							var dataTime1 = new Date(timesjc1);
//							var year1 = dataTime1.getFullYear();
//							var moth1 = dataTime1.getMonth() + 1;
//							if(moth1 < 10) {
//								moth1 = "0" + moth1;
//							}
//							var day1 = dataTime1.getDate();
//							if(day1 < 10) {
//								day1 = "0" + day1;
//							}
//							dtime = year1 + "-" + moth1 + "-" + day1;
//						}

						item += '<tr>'
						item += '<td>' + result.id + '</td>'
						item += '<td>' + result.product.name + '</td>'
						item += '<td>' + result.specs.specsName + '</td>'
						item += '<td>' + result.count + '</td>'
						item += '<td>' + result.price + '</td>'
						item += '<td>' + result.carriage + '</td>'
//						item += '<td>' + ptime + '</td>'
//						item += '<td>' + dtime + '</td>'
						item += '<td>'
						item += '<img class="details_update" src="img/update.png" title="修改" />&nbsp;&nbsp;'
						item += '<img class="details_del" src="img/delete.png" title="删除" /> &nbsp;'
						item += '</td>'
						item += '<tr>'

					});
					$('.table1').append(item);
				}
			}
		});
	});
}

// 点中当前行变色
function bs(Object) {
	$('.trr').css('background-color', 'white');
	$(Object).css('background-color', '#DFE8F6');
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
	var bid = $("#bid").val();
	$.ajax({
		type: "post",
		url: getRequestIp() + "getOrderByBid",
		data: {
			'bid': bid,
			'size': 10,
			'page': 0,
			'orderNumber': search_str,
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
	var bid = $("#bid").val();
	$.ajax({
		type: "post",
		url: getRequestIp() + "getOrderByBid",
		data: {
			'bid': bid,
			'size': 10,
			'page': clickPage - 1,
			'orderNumber': search_str,
			'state': state

		},
		async: true,
		dataType: "json",
		success: function(data) {
			if(data.success) {
				var item = "";
				$.each(data.list, function(i, result) {
					var tonn = sessionStorage.getItem("ton1");
					var timesjc = result.createTime;
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
					item += '<tr class="trr">'
					item += '<td class="tid">' + result.id + '</td>'
					item += '<td >' + result.orderNumber + '</td>'
					item += '<td class="timg">' + result.receiverName + '</td>'
					item += '<td>' + result.receiverPhone + '</td>'
					item += '<td>' + result.receiverAddress + '</td>'
					item += '<td>' + time + '</td>'
					item += '<td>' + result.remark + '</td>'

					if(result.state == 0) {
						item += '<td >待付款</td>'
					} else if(result.state == 1) {
						item += '<td >待发货</td>'
					} else if(result.state == 2) {
						item += '<td >待收货</td>'
					} else if(result.state == 3) {
						item += '<td >待评价</td>'
					} else if(result.state == 4) {
						item += '<td >已完成</td>'
					}
					item += '<td>' + result.expressNum + '</td>'
					item += '<td>' + result.carriage + '</td>'
					item += '<td>' + result.sumPrice + '</td>'
					item += '<td>' + result.realPrice + '</td>'
					if(result.payMethod == 1) {
						item += '<td >支付宝</td>'
					} else if(result.payMethod == 2) {
						item += '<td >微信</td>'
					} else {
						item += '<td >线下支付</td>'
					}
					item += '<td>'
					item += '<img class="show_details" src="img/coin17.png" title="查看详情" />&nbsp;'
					item += '<img class="tupdate" src="img/update.png" title="修改" />&nbsp;'
					//					item += '<img class="thide" src="img/hide.png" title="上下架" /> &nbsp;'
					item += '<img class="tdel" src="img/delete.png" title="删除" /> &nbsp;'
					item += '</td>'
					item += '<td style="display: none;">' + result.isClose + '</td>'
					item += '<td style="display: none;">' + result.introduction + '</td>'
					item += '<td style="display: none;">' + result.state + '</td>'
					item += '</tr>';
				});
				$('.table').append(item);

				$('.banDel').hide();
			} else {
				alert('异常错误！');
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
	var toni = $("#bid").val();
	var search = sessionStorage.getItem("search");
	var qery = sessionStorage.getItem("qery");
	var state = sessionStorage.getItem("order_state");
	ajax2(clickPage, toni, search, null, qery, state);
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

//$(function() {
//	$('.summernote').summernote({
//		height: 350,
//		tabsize: 2,
//		lang: 'zh-CN'
//	});
//});