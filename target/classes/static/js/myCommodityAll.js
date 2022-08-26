var sId = sessionStorage.getItem("ton"); // 一级栏目的id

var bus_id = sessionStorage.getItem("bus_id"); //当前商家id

var userId = sessionStorage.getItem("user_login"); // 当前登录的用户的id

var appUserId = sessionStorage.getItem("app_user_login"); //当前登录的app用户的id
// 
var pageNums = 0; // 总页数

//弹出隐藏层
function ShowDiv(show_div) {
	document.getElementById(show_div).style.display = 'block';
};
// 顶部图片

// 新增内容
function ShowDiv1(sId , sIds) {
	console.log(sId + '=='+sIds);
	//	$(".panel-body").html(null); //编辑框内容设为空
	if($("#bid").val() != null && $("#bid").val() != undefined && $("#bid").val() != '') {
		document.getElementById('save_news').style.display = 'block';
		getonClassify(sId , sIds);
	} else {
		alert('请先成为商家！');
	}

	//	getContent(id, conImg, conHtml, title, remark, conVideoPath, isUrl, linkUrl, conLivePath, colId, colName);

}
//关闭弹出层
function CloseDiv(show_div) {
	document.getElementById(show_div).style.display = 'none';
	$(".layui-progress-big").hide();
	$("#page").unbind();
}


// 提交的请求
function req() {
	var formData = new FormData();
	formData.append("name", $("#name").val()); //商品名
	formData.append("isClose", $("input[name='isClose']:checked").val()); //是否上下架(0:上架 1:下架)
	formData.append("expressMoney", $('#expressMoney').val()); //快递费用
	formData.append("bid", $("#bid").val()); //商家ID
	//	formData.append("cid", $("#sel1").val()); //分类id
	formData.append("id", $("#id").val()); //商品id
	formData.append("sId",  $("#shopclassidy").val()); //一级分类id
	formData.append("sIds", $("#shopclassidy_son").val()); //二级分类id
	formData.append("status", 0); //商品状态 (0:正常 1:禁用)
	formData.append("introduction", $(".panel-body").html()); // 商品介绍
	var introductionPhotos = $('#introductionPhotos')[0].files; //商品介绍图
	for(var i = 0; i < introductionPhotos.length; i++) {
		formData.append("introductionPhotos", introductionPhotos[i]);
	}
	if($('#image')[0].files[0] != null) {
		formData.append("images", $('#image')[0].files[0]);
	}
	//	if ($("#name").val()==''||) {}
	layui.use(['layer', 'form'], function() {
		var layer = layui.layer,
			form = layui.form;
		$.ajax({
			cache: true,
			type: "POST",
			url: getRequestIp() + "saveProduct",
			dataType: "json",
			data: formData,
			processData: false,
			contentType: false,
			error: function(request) {
				layer.msg('服务器错误', {
					icon: 2
				});
				// 把填好的内容存在本地
				//商品名称
				sessionStorage.setItem('mc_name', $("#name").val());
				//快递费
				sessionStorage.setItem('mc_expressMoney', $("#expressMoney").val());
				//商品介绍
				sessionStorage.setItem('mc_panel-body', $(".panel-body").html());
				//商品图片
				sessionStorage.setItem('mc_image', $("#image").val());
				// 轮播图片
				sessionStorage.setItem('mc_introductionPhotos', $("#introductionPhotos").val());

				console.log($("#name").val() + '--' + $("#expressMoney").val() + '--' + $(".panel-body").html() + '--' + $("#image").val() + '--' + $("#introductionPhotos").val());

			},
			success: function(data) {
				if(data.code == 0) {
					document.getElementById('save_news').style.display = 'none';
					layer.msg('' + data.msg + ',请添加商品规格', {
						icon: 1
					});
					//				parent.layui.table.reload('testReload');
					//				var index = parent.layer.getFrameIndex(window.name); // 获取窗口索引
					//				parent.layer.close(index);
				} else if(data.code == 500) {
					layer.msg('' + data.msg + '', {
						icon: 2
					});
				} else if(data.code == 501) {
					layer.msg('' + data.msg + '', {
						icon: 7
					});
				}
				$("#id").val(null);
				$("#name").val(null);
				$('#expressMoney').val(null);
				$(".panel-body").html(null);
				ajax1($("#bid").val(), $(".shopping_classify_son").val());
				pageReady(pageNums);
				ajax2(1, $("#bid").val(), $(".shopping_classify_son").val());
			}
		});

	});

}

// 模糊查询
function fuzzyQery() {
	$("#button3").click(function() {
		var tonid = $("#bid").val(); // 当前商铺的id
		var qery = $(".ins").val(); // 需要查询的商品名
		ajax1(tonid, 1, null, qery, null);
		pageReady(pageNums);
		ajax2(1, tonid, 1, null, qery, null);
		sessionStorage.setItem("search", 1);
		sessionStorage.setItem("qery", qery);
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

// 获取一级分类
function getonClassify(ontId , twoId){
		$.ajax({
		type: "post",
		url: getRequestIp() + "getShopClassify",
		async: true,
		data: {},
		dataType: "json",
		success: function(data) {
			if(data.success) {
				$('.opti1').remove();
				var item;
				for(var i = 0; i < data.list.length; i++) {
					if(ontId == data.list[i].id) {
						item = "<option selected='selected' class='opti1' value=" + data.list[i].id + ">" + data.list[i].name + "</option>";
					} else {
						item = "<option class='opti1' value=" + data.list[i].id + ">" + data.list[i].name + "</option>";
					}
					if (ontId == data.list[i].id) {
						getShoppingClassIfySon(data.list[i].id , -1 , twoId);
					} else if (i == 0) {
						// 默认获取第一个一级分类的二级分类
						getShoppingClassIfySon(data.list[i].id , -1 , 0);
					}
//					$('.shopping_classify_son').append(item);
					$('#shopclassidy').append(item)
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
$(document).ready(function() {
	$('.banDel').show();

	var conte = sessionStorage.getItem("conte");
	// 获取商铺的id
	if(bus_id != null && bus_id != undefined && bus_id != '') {
		$("#bid").val(bus_id);
		getShoppingClassIfySon(sId , 0 , 0);
		// 是商家 显示修改商家信息
		$("#col").hide();
		$("#col2").show();
	} else {
		// 不是商家 显示成为商家
		$("#col").show();
		$("#col2").hide();
		$('.banDel').hide();
	}

	// 正序 倒序获取数据
	$('.select_option').change(function() {
		ajax1($(".select_option").val(), $(".shopping_classify_son").val());
		pageReady(pageNums);
		ajax2(1, $(".select_option").val(), $(".shopping_classify_son").val());
	});
	// 一级分类变化的时候获取对应的二级分类的数据
	$('#shopclassidy').change(function () {
		var sId = $('#shopclassidy').val();
		getShoppingClassIfySon(sId , -1 , 0);
	});
	// 根据二级分类获取数据
//	$('.shopping_classify_son').change(function() {
//		ajax1($(".select_option").val(), $(".shopping_classify_son").val());
//		pageReady(pageNums);
//		ajax2(1, $(".select_option").val(), $(".shopping_classify_son").val());
//	});
	// 商品提交
	$('#asd').click(function() {

		req();
	});

	//点击添加商品的时候把信息清空
	$('#button1').click(function() {
		$("#id").val(null);
		$("#name").val(null);
		$('#expressMoney').val(null);
		$(".panel-body").html(null);

		//商品名称
		$("#name").val(sessionStorage.getItem('mc_name'));
		//快递费
		$("#expressMoney").val(sessionStorage.getItem('mc_expressMoney'));
		//商品介绍
		$(".panel-body").html(sessionStorage.getItem('mc_panel-body'));
		//商品图片
		//		$("#image").val(sessionStorage.getItem('mc_image'));
		// 轮播图片
		//		$("#introductionPhotos").val(sessionStorage.getItem('mc_introductionPhotos'));
		sessionStorage.removeItem('mc_name');
		sessionStorage.removeItem('mc_expressMoney');
		sessionStorage.removeItem('mc_panel-body');
		sessionStorage.removeItem('mc_image');
		sessionStorage.removeItem('mc_introductionPhotos');

	})
	$('#closd').click(function() {
		layui.use(['layer', 'form'], function() {
			var layer = layui.layer,
				form = layui.form;

			//			layer.msg('Hello World');

		});

		$(".banDel_lm").hide();
	});
	// 添加修改商铺的分类
	//	$('#colu_add').click(function() {
	//		var layer = getlayer();
	//		var id = $('.col_id').val(); //分类的id
	//		var name = $('.col_colName').val(); // 名称
	//		if(name != '' && name != undefined && name != null) {
	//			$.ajax({
	//				type: "post",
	//				url: getRequestIp() + "saveClassify",
	//				async: true,
	//				data: {
	//					'id': id,
	//					'name': name,
	//					'bid': 1 // 商铺的id
	//				},
	//				dataType: 'json',
	//				success: function(data) {
	//					layer.msg(data.msg);
	//					getclassify();
	//				}
	//			});
	//		}
	//
	//	});
	// 修改商铺分类
	//	$('body').on('click', '.classif_update', function() {
	//		var id = getnum(this, 0); //分类id
	//		var name = getnum(this, 1); //分类名称
	//		$('.col_id').val(id);
	//		$('.col_colName').val(name);
	//	});

	//删除商品
	$('body').on('click', '.tdel', function() {
		var id = getnum(this, 0); //对象id
		layui.use(['layer', 'form'], function() {
			var layer = layui.layer,
				form = layui.form;
			if(confirm("确定删除序号为" + id + "的商品吗？")) {

				$.ajax({
					type: "post",
					url: getRequestIp() + "delProduct",
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

	// 修改内容
	$('body').on('click', '.tupdate', function() {
		var id = getnum(this, 0); //对象id
		$("#id").val(id);
		var name = getnum(this, 9); //商品名称
		$("#name").val(name);
		var expressMoney = getnum(this, 4); //快递费
		$('#expressMoney').val(expressMoney);
		var soldcount = getnum(this, 5); //总销量
		var isClose = getnum(this, 7); //是否上架 0.上架 1.下架
		var introduction = getnum(this, 8); //商品介绍
		$(".panel-body").html(introduction);
		var sIds = getnum(this, 12); // 二级分类id
		var sId = getnum(this, 13); // 一级分类id
//		getonClassify(sId , sIds);
//		getShoppingClassIfySon(sId , 0);
		//		if (isClose == 1) {
		//			$('input[type=radio][name=isClose][value=1]').attr("checked", 'checked');
		//			$('input[type=radio][name=isClose][value=0]').attr("checked", false);
		//		} else {
		//			$('input[type=radio][name=isClose][value=0]').attr("checked", 'checked');
		//			$('input[type=radio][name=isClose][value=1]').attr("checked", false);
		//		}

		// 弹出修改的窗口
		ShowDiv1(sId , sIds);
	});

	//添加规格
	$('body').on('click', '.add_specs', function() {
		var id = getnum(this, 0); //对象id
		layui.use(['layer', 'form'], function() {
			var layer = layui.layer,
				form = layui.form;
			layer.open({
				type: 2,
				title: '规格管理',
				maxmin: true,
				shadeClose: true, // 点击遮罩关闭层
				area: ['1050px', '550px'],
				content: 'specs.html' // iframe的url
			});
			sessionStorage.setItem("specd_manage_id", id);
		});

	})

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
	// 上下架
	$('body').on('click', '.thide', function() {
		var id = getnum(this, 0);
		var isClose = $(this).parent().parent().children().eq(3);
		layui.use(['layer', 'form'], function() {
			var layer = layui.layer,
				form = layui.form;
			$.ajax({
				type: "post",
				url: getRequestIp() + 'setIsColse',
				async: true,
				data: {
					'id': id
				},
				dataType: "json",
				success: function(data) {
					if(data.success) {
						layer.msg(data.msg);
						if(isClose.html() == '上架') {
							isClose.html('下架');
						} else if(isClose.html() == '下架') {
							isClose.html('上架');
						}
					} else {
						alert('异常错误！');
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
	$('body').on('mouseover', '.simg2', function() {
		$(this).siblings('.simg3').css("display", "block");
	});
	$('body').on('mouseout', '.simg2', function() {
		$(this).siblings('.simg3').css("display", "none");
	});

	// 获取shangp
	// 填写商铺信息
	$('#col').click(function() {
		sessionStorage.setItem("save_bus_appid", appUserId);
		layui.use('layer', function() { //独立版的layer无需执行这一句
			layer.open({
				type: 2,
				title: '添加商家',
				maxmin: true,
				shadeClose: true, // 点击遮罩关闭层
				area: ['1050px', '550px'],
				content: 'addbusiness.html' // iframe的url
			});
		});
	});
	// 修改商铺信息
	$('#col2').click(function() {
		sessionStorage.setItem("edit_bus_id", $('#bid').val());
		layui.use('layer', function() { //独立版的layer无需执行这一句
			layer.open({
				type: 2,
				title: '修改商铺',
				maxmin: true,
				shadeClose: true, // 点击遮罩关闭层
				area: ['1050px', '550px'],
				content: 'editbusiness.html' // iframe的url
			});
		});
	});

	// 刷新当前页面
	$('#refresh').click(function() {
		window.location.reload()
	});

	// 获取热销商品
	$('#r_shop').click(function() {
		$('#comment').show();
		getSlhcOrSales($("#bid").val(), 0);
	});
	// 获取促销商品rx_shop
	$('#c_shop').click(function() {
		$('#comment').show();
		getSlhcOrSales($("#bid").val(), 1);
	});
	// 设为热销商品
	$('body').on('click', '.rx_shop', function() {
		var slhc = getnum(this, 10); // 1.热销商品 null普通商品
		var id = getnum(this, 0); // 对象id
		if(slhc == 1) {
			if(confirm('当前商品已是热销商品确定取消吗？')) {
				setSlhcOrSales(id, 0, 0, this);
			}
		} else {
			// 设为热销商品
			setSlhcOrSales(id, 0, 0, this);
		}
	});
	// 设为促销商品
	$('body').on('click', '.cx_shop', function() {
		var sales = getnum(this, 11); // 1.促销品 null普通商品
		var id = getnum(this, 0); // 对象id
		if(sales == 1) {
			if(confirm('当前商品已是促销商品确定取消吗？')) {
				setSlhcOrSales(id, 1, 0, this);
			}
		} else {
			// 设为热销商品
			setSlhcOrSales(id, 1, 0, this);
		}
	});

	// 取消 热销或促销的商品
	$('body').on('click', '.scancel', function() {
		var flag = $('.abrogate').val();
		var id = getnum(this, 0); // 对象id

		if(flag == 0) {
			setSlhcOrSales(id, 0, 1, this);
		} else if(flag == 1) {
			setSlhcOrSales(id, 1, 1, this);
		}

	});
});

// 根据一级栏目id获取二级栏目
function getShoppingClassIfySon(sId , opId , twoId) {
	console.log('twoId:'+twoId);
	$.ajax({
		type: "post",
		url: getRequestIp() + "getShopClassifySon",
		async: true,
		data: {
			'sId': sId
		},
		dataType: "json",
		success: function(data) {
			if(data.success) {
				if (opId == 0) {
					ajax1($("#bid").val(), $(".shopping_classify_son").val());
					pageReady(pageNums);
					ajax2(1, $("#bid").val(), $(".shopping_classify_son").val());
					getInfo();
					fuzzyQery($("#bid").val(), $(".shopping_classify_son").val());

				}
					
				$('.opti').remove();
				var item;
				for(var i = 0; i < data.list.length; i++) {
					if(twoId == data.list[i].id) {
						item = "<option selected='selected' class='opti' value=" + data.list[i].id + ">" + data.list[i].name + "</option>";
					} else {
						item = "<option class='opti' value=" + data.list[i].id + ">" + data.list[i].name + "</option>";
					}

//					$('.shopping_classify_son').append(item);
					$('#shopclassidy_son').append(item)
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

// 分页获取促销 热销的商品
function getSlhcOrSales(id, flag) {
	var nums = 1;
	$('.abrogate').val(flag);
	$.ajax({
		type: "post",
		url: getRequestIp() + "getProductBySlhcOrSales",
		async: true,
		data: {
			'page': 0,
			'limit': 10,
			'bid': id,
			'flag': flag
		},
		dataType: "json",
		success: function(data) {
			apply(data);
			nums = data.totalSize;
			var pages = nums / 10;
			pages = Math.ceil(pages) * 1;
			//分页
			$("#page").paging({
				pageNo: 1,
				totalPage: pages,
				totalSize: nums,
				callback: function(nums) {
					$.ajax({
						type: "post",
						url: getRequestIp() + "getProductBySlhcOrSales",
						async: true,
						data: {
							'page': nums - 1,
							'limit': 10,
							'bid': id,
							'flag': flag
						},
						dataType: "json",
						success: function(datas) {
							if(datas.success) {
								if(datas.list != null) {
									apply(datas);
								}
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
	function apply(data) {
		var item = "";
		$('.table3 tr td').remove();
		if(data.list == null) {
			return;
		}
		$.each(data.list, function(i, result) {
			var name = result.name;
			if(name != '' && name != null && name != undefined) {
				name = name.slice(0, 15);
			}
			var bian = '';
			if(result.specsList != null && result.specsList.length < 1) {
				bian = "hhh";
			}
			item += '<tr onclick="bs(this)" class="trr ' + bian + '">'
			item += '<td class="tid">' + result.id + '</td>'
			item += '<td >' + name + '</td>'
			item += '<td class="timg"><img class="simg2" src="' + result.image + '" /><img class="simg3" src="' + result.image + '" /></td>'
			if(result.isClose == 0) {
				item += '<td >上架</td>'
			} else {
				item += '<td >下架</td>'
			}
			item += '<td>' + result.expressMoney + '</td>'
			item += '<td>' + result.soldcount + '</td>'
			item += '<td>'
			item += '<img class="scancel " src="images/qx.png" title="取消" /> &nbsp;'
			item += '</td>'
			item += '<td style="display: none;">' + result.isClose + '</td>'
			item += '<td style="display: none;">' + result.introduction + '</td>'
			item += '<td style="display: none;">' + result.name + '</td>'
			item += '<td style="display: none;">' + result.slhc + '</td>'
			item += '<td style="display: none;">' + result.sales + '</td>'
			item += '</tr>';
		});
		$('.table3').append(item);
	}
}

// 促销  热销的请求
function setSlhcOrSales(id, flag, num, obje) {
	var slhc = $(obje).parent().parent().children().eq(10);
	var sales = $(obje).parent().parent().children().eq(11);
	var obj = $(obje).parent().parent();

	layui.use(['layer', 'form'], function() {
		var layer = layui.layer,
			form = layui.form;
		$.ajax({
			type: "post",
			url: getRequestIp() + 'updateSlhc',
			async: true,
			data: {
				'id': id,
				'flag': flag
			},
			dataType: "json",
			success: function(data) {
				if(data.success) {
					layer.msg(data.msg);
					if(num == 1) {
						obj.html(null);
						ajax1($("#bid").val());
						pageReady(pageNums);
						ajax2(1, $("#bid").val());
					}
					if(flag == 0) {
						if(slhc.html() == 1) {
							slhc.html(null);
						} else {
							slhc.html(1);
						}
					}
					if(flag == 1) {
						if(sales.html() == 1) {
							sales.html(null);
						} else {
							sales.html(1);
						}
					}
				} else {
					alert('异常错误！');
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
		sessionStorage.setItem("getRequestIp1()", num);
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
		url: getRequestIp() + "getProductByBid",
		data: {
			'bid': bid,
			'limit': 10,
			'page': 0,
			'search': search_str,
			'num': num,
			'sId': 0

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
		url: getRequestIp() + "getProductByBid",
		data: {
			'bid': bid,
			'limit': 10,
			'page': clickPage - 1,
			'search': search_str,
			'num': num,
			'sId': 0

		},
		async: true,
		dataType: "json",
		success: function(data) {
			if(data.success) {
				var item = "";
				$.each(data.list, function(i, result) {
					var name = result.name;
					if(name != '' && name != null && name != undefined) {
						name = name.slice(0, 15);
					}
					var bian = '';
					if(result.specsList != null && result.specsList.length < 1) {
						bian = "hhh";
					}
					item += '<tr onclick="bs(this)" class="trr ' + bian + '">'
					item += '<td class="tid">' + result.id + '</td>'
					item += '<td >' + name + '</td>'
					item += '<td class="timg"><img class="simg" src="' + result.image + '" /><img class="simg1" src="' + result.image + '" /></td>'
					if(result.isClose == 0) {
						item += '<td >上架</td>'
					} else {
						item += '<td >下架</td>'
					}
					item += '<td>' + result.expressMoney + '</td>'
					item += '<td>' + result.soldcount + '</td>'
					item += '<td>'
					item += '<img class="add_specs" src="images/fx1.png" title="添加规格" />&nbsp;'
					item += '<img class="tupdate" src="img/update.png" title="修改" />&nbsp;'
					item += '<img class="rx_shop" src="img/hot.png" title="设为热销商品" /> &nbsp;'
					item += '<img class="cx_shop" src="img/cx.png" title="设为促销商品" /> &nbsp;'
					item += '<img class="thide" src="img/hide.png" title="上下架" /> &nbsp;'
					item += '<img class="tdel" src="img/delete.png" title="删除" /> &nbsp;'
					item += '</td>'
					item += '<td style="display: none;">' + result.isClose + '</td>'
					item += '<td style="display: none;">' + result.introduction + '</td>'
					item += '<td style="display: none;">' + result.name + '</td>'
					item += '<td style="display: none;">' + result.slhc + '</td>'
					item += '<td style="display: none;">' + result.sales + '</td>'
					item += '<td style="display: none;">' + result.sIds + '</td>'
					item += '<td style="display: none;">' + result.sId + '</td>'
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
	var num = $(".select_option").val();
	var search = $(".shopping_classify_son").val();
	var qery = sessionStorage.getItem("qery");
	ajax2(clickPage, num, search, null, qery, null);
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