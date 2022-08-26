$(document).ready(function() {
	//分页
	$("#page").paging({
		pageNo: 1,
		totalPage: 10,
		totalSize: 299,
		callback: function(num) {
			alert(num)
		}
	})
});