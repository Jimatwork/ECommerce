$(document).ready(function() {

	layui.use(['layer', 'table'], function() {
		table = layui.table, form = layui.form; //表格
		var sid = sessionStorage.getItem("shopClassifySon_id");
		//执行一个 table 实例
		table.render({
			elem: '#test',
			url: getRequestIp() + 'getShopClassifySon' //数据接口
				,
			id: 'testReload',
			method: 'post',
			toolbar: 'true',
			where: {
				'sId': sid
			},
			response: {
				statusName: 'success', //数据状态的字段名称，默认：code
				statusCode: true, //成功的状态码，默认：0
				msgName: null, //状态信息的字段名称，默认：msg
				countName: 'totalSize', //数据总数的字段名称，默认：count
				dataName: 'list' //数据列表的字段名称，默认：data
			},
			page: { //支持传入 laypage 组件的所有参数（某些参数除外，如：jump/elem） - 详见文档
				layout: ['limit', 'count', 'prev', 'page', 'next', 'skip'], //自定义分页布局 //,curr: 5 //设定初始在第 5 页
			},
			cellMinWidth: 10,
			cols: [
				[ //表头
					{
						field: 'id',
						title: '序号',
						fixed: 'left',
						align: 'center'
					}, {
						field: 'name',
						title: '分类名称',
						align: 'center'
					}, {
						field: 'img',
						title: '分类图标',
						align: 'center',
						templet: function(data) {
							var imag = JSON.stringify(data.img);
							return "<img src='" + data.img + "' width='50' height='35' onmouseover='mouseoverzz(" + JSON.stringify(data.img) + ")'/>";
						}
					},
					{
						field: 'sequence',
						title: '分类顺序',
						align: 'center'
					},
					{
						field: 'state',
						title: '状态',
						align: 'center',
						templet: function(data) {
							if(data.state == 0) {
								return '<input checked="\true\"  id="' + "check" + data.id + '" value="' + data.id + '" type="\checkbox\" name="\lock\" title="\显示\" lay-filter="status">';

							} else {
								return '<input type="\checkbox\"  id="' + "check" + data.id + '"  value="' + data.id + '" name="\lock\" title="\隐藏\" lay-filter="status">';
							}
						}
					},
					{
						fixed: 'right',
						title: '操作',
						align: 'center',
						toolbar: '#barDemo'
					}
				]
			]
		});
		//监听管理员状态操作
		form.on('checkbox(status)', function(obj) {

			var obje = this;
			var formData = new FormData();
			formData.append("id", this.value);
			formData.append("state", obj.elem.checked == true ? "0" : "1");
			$.ajax({
				cache: true,
				type: "POST",
				url: getRequestIp() + "addShopClassifySon",
				data: formData,
				async: true,
				dataType: "json",
				processData: false,
				contentType: false,
				error: function() {
					layer.msg('服务器错误', {
						icon: 2
					});
				},
				success: function(data) {
					layer.msg(data.msg);

					var flag = $(obje).next().children('span').html();
					if(flag == '隐藏') {
						$(obje).next().children('span').html('显示')
					}
					if(flag == '显示') {
						$(obje).next().children('span').html('隐藏')
					}
				}
			});
		});

		//监听工具条
		table.on('tool(demo)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"
			var data = obj.data //获得当前行数据
				,
				layEvent = obj.event; //获得 lay-event 对应的值
			if(layEvent === 'detail') {
				// 修改分类
				layer.open({
					type: 1,
					title: '修改分类',
					maxmin: true,
					shadeClose: true, // 点击遮罩关闭层
					area: ['650px', '350px'],
					content: $('#alert_div') // iframe的url
				});

				var id = data.id;
				var name = data.name;
				var sequence = data.sequence;
				var state = data.state;
				$('#id').val(id);
				$('#name').val(name);
				$('#sequence').val(sequence);
				$(":radio[name='state'][value='" + state + "']").prop("checked", "checked"); // 设置单选框选中
			} else if(layEvent === 'del') {
				layer.confirm('确定删除此数据吗？', function(index) {

					layer.close(index);
					//向服务端发送删除指令
					$.ajax({
						cache: true,
						type: "POST",
						url: getRequestIp() + "delShopClassifySon",
						data: {
							'id': data.id,
						},
						async: true,
						dataType: "json",
						error: function(request) {
							layer.msg('服务器错误', {
								icon: 2
							});
						},
						success: function(data) {
							layer.msg(data.msg);
							if(data.success) {
								obj.del(); //删除对应行（tr）的DOM结构
							}

						}
					});
				});
			}
		});
	});

	//执行重载
	//	$("#search_order").click(function() {
	//		console.log($("#orderselect").val());
	//		//获取选择的状态值
	//		var state = $("#orderselect").val();
	//		var orderNumber = $('#demoReload').val();
	//
	//		layui.use('table', function() {
	//			var table = layui.table;
	//
	//			//执行重载
	//			table.reload('testReload', {
	//				page: {
	//					curr: 1 //重新从第 1 页开始
	//				},
	//				where: {
	//					orderNumber: orderNumber,
	//					state: state
	//				}
	//			});
	//		});
	//	});
	// 添加分类
	$("#search_order").click(function() {
		layer.open({
			type: 1,
			title: '添加分类',
			maxmin: true,
			shadeClose: true, // 点击遮罩关闭层
			area: ['650px', '350px'],
			content: $('#alert_div') // iframe的url
		});
		$('#name').val(null);
		$('#sequence').val(null);
		var obj = document.getElementById('img');
		obj.outerHTML = obj.outerHTML;
	});
	// 添加分类
	$('#submit').click(function() {
		var index = layer.index;
		updateClassify(index);
	});
});

function updateClassify(index) {
	var formData = new FormData();
	formData.append('id', $('#id').val()); // 分类名称
	formData.append('sId', sessionStorage.getItem("shopClassifySon_id")); // 一级分类的id
	formData.append('name', $('#name').val()); // 分类名称
	formData.append('state', $("input[name='state']:checked").val()); // 分类状态 0.显示 1.隐藏
	formData.append('sequence', $('#sequence').val()); // 显示顺序
	if($('#img')[0].files[0] != null) { //分类图标
		formData.append("images", $('#img')[0].files[0]);
	}
	$.ajax({
		type: "post",
		url: getRequestIp() + "addShopClassifySon",
		async: true,
		data: formData,
		processData: false,
		contentType: false,
		dataType: 'json',
		error: function(request) {
			layer.msg('服务器错误', {
				icon: 2
			});
		},
		success: function(data) {
			layer.msg(data.msg);
			if(data.success) {
				$('#id').val(null);
				$('#name').val(null);
				$('#sequence').val(null);
				var obj = document.getElementById('img');
				obj.outerHTML = obj.outerHTML;
				layer.close(index);
				//执行重载
				table.reload('testReload', {
					page: {
						curr: 1 //重新从第 1 页开始
					}
				});
			}
		}
	});
}

function formatDate(now) {
	var now = new Date(now);
	var year = now.getFullYear();
	var month = now.getMonth() + 1;
	var date = now.getDate();
	var hour = now.getHours();
	var minute = now.getMinutes();
	var second = now.getSeconds();
	if(month < 10) {
		month = "0" + month;
	}
	if(date < 10) {
		date = "0" + date;
	}
	if(minute < 10) {
		minute = "0" + minute;
	}
	if(second < 10) {
		second = "0" + second;
	}
	if(hour < 10) {
		hour = "0" + hour;
	}
	return year + "-" + month + "-" + date + "   " + hour + ":" + minute + ":" + second;
}
//图片放大
function mouseoverzz(img) {
	layer.open({
		type: 1,
		title: false,
		closeBtn: 0,
		area: '450px',
		skin: 'layui-layer-nobg', //没有背景色
		shadeClose: true,
		content: "<img src=" + img + " width=450 height=350>"
	});
}