$(document).ready(function(){
	
	var id = sessionStorage.getItem('id');
	Ajxas(address() + "getColumnsById", {"id" : id}, function(data) {
		$('#heigtop').html(data.list[0].colName);
	});

	
	
	
		
	var confirm = sessionStorage.getItem('confirm');

	var transition = "" ;
	
	if(confirm == 1) {
		transition = 'queryFindLisAltert';
	} else if(confirm == 2) {
		transition = 'getHotColumnContent';
	} else {
		transition = 'getHeadLines';
	};
	var pand = "";
	alertfy(0, id, transition);
	
	var totalSiz = sessionStorage.getItem('totalSize');
	
	
	function alertfy(num, id, transition){
		Ajxas(address() + transition, {"id" : id, "appUserId" : "", "start" : num, "limit" : 10, "tag" : 5}, function(data) {
				
			sessionStorage.setItem('totalSize', data.totalSize);
			
			var totalSize = data.totalSize;
			if(pand == ""){
				$("#page").paging({
					pageNo: 1,
					totalPage: Math.ceil(totalSize/10),
					totalSize: totalSize,
					callback: function(num) {
						alertfy(num, id, transition);
						pand = num;
					}
				})
			};
			
			var data = data.list;
			console.log(JSON.stringify(data))
			var jihe = "";
			for(let i=0; i<data.length; i++){
				
				var time = new Date(data[i].createDate*1), conHtml,
				r = time.getDate() < 10 ? "0" + time.getDate() : time.getDate(),
				y = time.getMonth() + 1 < 10 ? "0" + (time.getMonth() + 1): time.getMonth() + 1,
				n = time.getFullYear(),
				he = n +"-"+ y +"-"+ r;
				if(data[i].conHtml !== ""){
					conHtml = data[i].conHtml;
				};
	
				//				栏目ID   		大图				小图				 标题				内容				 阅读次数			评论次数				点赞次数			创建时间
				var arrdata = [data[i].colId, data[i].conImg, data[i].conImg1, data[i].conTitle, data[i].conRemark, data[i].playCount, data[i].exaCount, data[i].praiseCount, he, conHtml];
			
				jihe += `<a class="tiaoz" data_txt='${data[i].id}' href="#">
							<img class="float_lefts" src="${arrdata[2]}" />
							<div class="float_lefts datas">
								<div class="byline">${arrdata[3]}</div>
									<p>${arrdata[4]} </p>
									<p class="interaction">
										<span>汉纳编辑</span>
										<span>&#xe671;${arrdata[5]}</span>
										<span>&#xe621;${arrdata[6]}</span>
										<span>&#xe607;${arrdata[7]}</span>
										<span>&#xe60e;${arrdata[8]}</span>
									</p>
							</div>
						</a>
						`
			};
			var lengths =  data.length < 4 ? data.length : data.length = 4 ;
			
			var jihes = `<img class="tiaoz img im" data_txt='${data[0].conHtml}' src="${data[0].conImg}">`;
			for(let l = 1; l < lengths; l++) {
				//				栏目ID   		小图	
				var arrdatas = [data[l].colId, data[l].conImg1, data[l].conHtml];
				jihes += `<img class="tiaoz img" data_txt='${arrdatas[2]}' src="${arrdatas[1]}">`
			};
			$('.imgbox').html(jihes);
			$('.journalism_left li').html(jihe);
		})
	};
});
