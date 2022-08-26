//获取滚动条当前的位置 
	function getScrollTop() {
		var scrollTop = 0;
		if(document.documentElement && document.documentElement.scrollTop) {
			scrollTop = document.documentElement.scrollTop;
		} else if(document.body) {
			scrollTop = document.body.scrollTop;
		}
		return scrollTop;
	}

//获取当前可视范围的高度 
	function getClientHeight() {
		var clientHeight = 0;
		if(document.body.clientHeight && document.documentElement.clientHeight) {
			clientHeight = Math.min(document.body.clientHeight, document.documentElement.clientHeight);
		} else {
			clientHeight = Math.max(document.body.clientHeight, document.documentElement.clientHeight);
		}
		return clientHeight;
	}

//获取文档完整的高度 
	function getScrollHeight() {
		return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
	}

//公用Ajax
	function publicAjax(url, data, success){
		$.ajax({
		  type: 'POST',
		  url:  url,
		  data: data,
		  dataType: 'json',
		  success: success,
		  error:function(){
	   			console.log("请求错误！");
	   		}
		});
	};
	
//	点赞
	function clickLike(conid, comment_color, thist) {
	        var PraiseUrl = "addPraiseCount";
	        publicAjax(PraiseUrl, {
	                'columnContentId' : conid,
	                'tag' : comment_color
	        }, function(data) {
	        	if(data.success){
	        		if (data.msg) {
		           		thist.siblings('.price').text(data.totalSize);
		           		thist.toggleClass('comment_color');
		            } else {
		            	thist.siblings('.price').text(data.totalSize);
		            }
	        	}
	        });
    }
