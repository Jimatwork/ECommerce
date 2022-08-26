$(document).ready(function() {
	getServerPath();

	$('#push_asd').click(function() {
		var serverPath = $('#server_address').val(); //国内服务器
		var fcServerPath = $('#server_address1').val(); //国外服务器
		var id = $('.serverId').val(); //id
		$.ajax({
			type: "post",
			url: getRequestIp() + "setServerPath",
			async: true,
			data: {
				'serverPath': serverPath,
				'fcServerPath': fcServerPath,
				'id': id
			},
			dataType: "json",
			success: function(data) {
				if(data.success) {
					alert(data.msg);
					getServerPath();
				}
			}
		});
	});
});
// 获取服务器问题
function getServerPath() {
	$.ajax({
		type: "post",
		url: getRequestIp() + "getServerPath",
		async: true,
		data: {
			'tag': '5'
		},
		dataType: "json",
		success: function(data) {
			$('#server_address').val(data.serverPath); //国内服务器地址
			$('#server_address1').val(data.fcServerPath); //国外服务器地址
			$('.serverId').val(data.id); //id

		}
	});
}