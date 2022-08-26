var ton = sessionStorage.getItem("ton"); // colid的值
// colid的值
var pageNums = 0; // 总页数

//关闭弹出层
function CloseDiv(show_div) {
	document.getElementById(show_div).style.display = 'none';
	// 一级目录的id
};
$(document).ready(function() {

	ajax2();

	$('body').on('mouseover', '.simg', function() {
		$(this).siblings('.simg1').css("display", "block");
	});
	$('body').on('mouseout', '.simg', function() {
		$(this).siblings('.simg1').css("display", "none");
	});

	// 添加
	$('#button1').click(function() {
		var mid = $('#rolltotle_id').val(null);
		var content = $('#in_conRemark').val(null);
		$('#comment').css("display", "block");
	});
	// 取消
	$('#push_qx').click(function() {
		$('#comment').css("display", "none");
	});
	// 提交
	$('#push_asd').click(function() {
		addBannerCont();
	});
	// 修改
	$('body').on('click', '.tupdate', function() {
		var id = getnum(this, 0); // id
		var content = getnum(this, 1); //弹幕内容
		$('#rolltotle_id').val(id);
		$('#in_conRemark').val(content);
		$('#comment').css("display", "block");
	});
	// 删除 
	$('body').on('click', '.tdel', function() {
		if (confirm("确定删除吗？")) {
			var id = getnum(this, 0); // id
			$.ajax({
				type: "post",
				url: getRequestIp() + "delMarquee",
				async: true,
				data: {
					'id': id
				},
				dataType: "json",
				success: function(data) {
					if(data.success) {
						alert('删除成功!');
						ajax2();
					} else {
						alert('删除失败!');
					}
				}
			});
		}
	});
});
// 添加内容
function addBannerCont() {
	var mid = $('#rolltotle_id').val();
	var content = $('#in_conRemark').val();
	$.ajax({
		type: "post",
		url: getRequestIp() + "addMarquee",
		async: true,
		data: {
			'content': content,
			'id': mid
		},
		dataType: "json",
		success: function(data) {
			if(data.success) {
				alert('添加成功!');
				$('#comment').css("display", "none");
				ajax2();
			} else {
				alert('添加失败!');
			}
		}
	});
}
// 获取对象的某个值
function getnum(obj, num) {
	return $(obj).parent().parent().children().eq(num).text();
}


//渲染数据
function ajax2() {
	$('.table tr td').remove();
	$.ajax({
		type: "post",
		url: getRequestIp() + "getAllMarquee",
		data: {},
		async: true,
		dataType: "json",
		success: function(data) {
			
			var item = "";
			$.each(data, function(i, result) {
				item += '<tr>'
				item += '<td class="roll_id">' + result.id + '</td>'
				item += '<td>' + result.content + '</td>'
				item += '<td>'
				item += '<img class="tupdate" src="img/update.png" title="修改" />&nbsp;'
				item += '<img class="tdel" src="img/delete.png" title="删除" /> &nbsp;'
				item += '</td>'
				item += '</tr>';
			});
			$('.table').append(item);
		},
		error: function(data) {
//			alert("服务器异常!")
		}

	});
}