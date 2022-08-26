function address(){
	//return	"http://192.168.10.105:8080/HanaGlobal/"
// 	return	"http://192.168.10.108:8888/"
	return "https://app.kuedit.com/"
}

function Ajxas(urls, data, success){
	$.ajax({
		type: "POST",
		url: urls,
		data: data,
		async: true,
		dataType: "json",
		success: success,
		error: function(){
			console.log("未知错误！")
		}
	})
};