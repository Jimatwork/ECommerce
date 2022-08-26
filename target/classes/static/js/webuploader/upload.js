var element = null;

var uploader = WebUploader.create({
	// swf文件路径
	swf: '/js/webuploader/Uploader.swf',
	// 文件接收服务端。
	server: getRequestIp() + 'uploadfile',
	// 选择文件的按钮。可选。
	// 内部根据当前运行是创建，可能是input元素，也可能是flash.
	pick: '.tttt',

	//分块
	chunked: true,
	auto: true,
	disableGlobalDnd: true,
	chunkSize: 1024 * 1024 * 500, //500M 为一分片
	fileNumLimit: 1024,
	fileSizeLimit: 1024 * 1024 * 1024, // 200 M
	fileSingleSizeLimit: 1024 * 1024 * 1024, // 50 M

	// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
	resize: false
});

//var $list = $("#video_thelist");
//var $btn = $('#ctlBtn');

// 当有文件被添加进队列的时候
uploader.on('fileQueued', function(file) {
	console.log('开始!');
	$('.banDel').show();
	//	$list.append('<div id="' + file.id + '" class="item">' +
	//		'<h4 class="info">' + file.name + '</h4>' +
	//		'<p class="state">等待上传...</p>' +
	//		'</div>');

});

//开始上传
//$btn.on('click', function() {
//	uploader.upload();
//});

// 文件上传过程中创建进度条实时显示。
uploader.on('uploadProgress', function(file, percentage) {

	//	console.log(parseInt(percentage * 100) + '%');
	$(".layui-progress").show();
	element.progress('demo1', parseInt(percentage * 100) + '%')
});

uploader.on('uploadSuccess', function(file, object) {
	//	$('#' + file.id).find('p.state').text('已上传');

	var url = "";
	for(i in object) {
		url = object[i];
	}

	console.log(url);
	if(url == '上传失败,视频不能大于五分钟！') {
		alert('上传失败,视频不能大于五分钟！');
		$('.banDel').hide();
		$(".layui-progress").hide();
	} else if(url == '上传失败!') {
		alert('上传失败!');
		$('.banDel').hide();
		$(".layui-progress").hide();
	} else {
		$("#img_mytitle").html(url);
		$('.note-video-url').val(url);
		$('.note-image-url').val(url);
		$('.banDel').hide();
	}

});

uploader.on('uploadError', function(file) {
	alert('上传失败!');
	$('.banDel').hide();
	$(".layui-progress").hide();
	$('#' + file.id).find('p.state').text('上传出错');
});

uploader.on('uploadComplete', function(file) {

	//	console.log("上传完成");

});

//进度条相关
$(document).ready(function() {
	$(".layui-progress").hide();
	layui.use('element', function() {
		element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
	});
//	note-image-input form-control
//	$('body').on('click', '.dd_dd_ss', function() {
//
//		$('.webuploader-element-invisible').eq(0).trigger('click');
//
//	});
//	$('body').on('click', '.xz_img', function() {
//		$('.webuploader-element-invisible').eq(0).trigger('click');
//
//	});
//	//note-btn btn btn-default btn-sm
//	$('body').on('click', '.btn-sm', function() {
//		var title = $(this).attr('data-original-title');
//		if(title == '视频') {
//			$('.dd_dd_ss').css('display', 'block');
//		} else {
//			$('.dd_dd_ss').css('display', 'none');
//		}
//		if(title == '图片') {
////			alert(2)
//		}
//	});
});