
var CitySN = sessionStorage.getItem('returnCitySN') * 1;

if(typeof CitySN == "number" && !isNaN(CitySN)) {
	function getRequestIp() {
		// return 'http://192.168.10.108:8888/';
				return "https://app.issop.cn/kumai/";
	}

} else {
	function getRequestIp() {
		// return 'http://192.168.10.108:8888/';
				return "https://app.issop.cn/kumai/";
	}

};