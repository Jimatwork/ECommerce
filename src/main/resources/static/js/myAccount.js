var ton = sessionStorage.getItem("ton"); // colid的值

var userId = sessionStorage.getItem("user_login"); // 当前登录的用户的id

var appUserId = sessionStorage.getItem("app_user_login"); //当前登录的app用户的id
// colid的值
var pageNums = 0; // 总页数

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
			$(".balance").html(data.list[0].money); //账户余额
			ajax1();
			pageReady(pageNums);
			ajax2();
		}
	});

	// 分类查询
	$('.order_select').change(function() {
		var state = $('.order_select').val();
		ajax1(state);
		pageReady(pageNums);
		ajax2(1,state);
	});
});

// 点中当前行变色
function bs(Object) {
	$('.trr').css('background-color', 'white');
	$(Object).css('background-color', '#DFE8F6');
}

// 获取对象的某个值
function getnum(obj, num) {
	return $(obj).parent().parent().children().eq(num).html();
}

// 得到数据的总页数
function ajax1(state) {
	var bid = $("#bid").val();
	$.ajax({
		type: "post",
		url: getRequestIp() + "getRunningTabByBid",
		data: {
			'bid': bid,
			'size': 10,
			'page': 0,
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
function ajax2(clickPage, state) {
	if(clickPage == undefined) {
		clickPage = 1;
	}
	$('.table tr td').remove();
	var bid = $("#bid").val();
	$.ajax({
		type: "post",
		url: getRequestIp() + "getRunningTabByBid",
		data: {
			'bid': bid,
			'size': 10,
			'page': clickPage - 1,
			'state': state

		},
		async: true,
		dataType: "json",
		success: function(data) {
			if(data.success) {
				var item = "";
				$.each(data.list, function(i, result) {
					var tonn = sessionStorage.getItem("ton1");
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
					item += '<tr class="trr">'
					item += '<td class="tid">' + result.id + '</td>'
					item += '<td >' + result.money + '</td>'
					item += '<td class="timg">' + time + '</td>'
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