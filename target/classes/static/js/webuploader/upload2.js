var element = null;

var uploader = WebUploader.create({
	// swf文件路径
	swf: '/js/webuploader/Uploader.swf',
	// 文件接收服务端。
	server: 'http://192.168.10.105:8888/uploadfile',
	// 选择文件的按钮。可选。
	// 内部根据当前运行是创建，可能是input元素，也可能是flash.
	pick: '#picker2',

	//分块
	chunked: true,
	auto: true,
	disableGlobalDnd: true,
	chunkSize: 1024 * 1024, //1M 为一分片
	fileNumLimit: 1024,
	fileSizeLimit: 1024 * 1024 * 1024, // 200 M
	fileSingleSizeLimit: 1024 * 1024 * 1024, // 50 M

	// 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
	resize: false
});

var $list = $("#thelist2");
var $btn = $('#ctlBtn2');

// 当有文件被添加进队列的时候
uploader.on('fileQueued', function(file) {

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
	console.log(parseInt(percentage * 100) + '%');
	$("#jindutiao2").show();
	element.progress('demo_zb2', parseInt(percentage * 100) + '%')
});

uploader.on('uploadSuccess', function(file, object) {
//	$('#' + file.id).find('p.state').text('已上传');

	var url = "";
	for(i in object) {
		url = object[i];
	}

	console.log("已上传:" + url);

	$("#mytitle2").html(url);

});

uploader.on('uploadError', function(file) {
	$('#' + file.id).find('p.state').text('上传出错');
});

uploader.on('uploadComplete', function(file) {

	console.log("上传完成");

});

//进度条相关
$(document).ready(function() {
	$("#jindutiao2").hide();
	layui.use('element', function() {
		element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
	});
});