$(document).ready(function() {
	var transition = '';

	var confirm = sessionStorage.getItem('confirm');
	if(confirm == 1) {
		transition = 'getHotColumnContent';
		menus(1);
	} else if(confirm == 2) {
		transition = 'queryFindLisAltert';
		menus(2);
	} else {
		menus(0);
		transition = 'getHeadLines';
	};
	publi();

	$('header ul li').click(function() {
		var changs = $(this).index();
		sessionStorage.setItem('confirm', changs);
		$(this).addClass('confirm').siblings('li').removeClass('confirm');
		if(changs == 1) {
			transition = 'getHotColumnContent';
		} else if(changs == 2) {
			transition = 'queryFindLisAltert';
		} else {
			transition = 'getHeadLines';
		};
		publi();
	});

	function menus(Numbers) {
		return $('header ul li').eq(Numbers).addClass('confirm').siblings('li').removeClass('confirm');
	}

	function publi() { //通用
		
		Ajxas(address() + "getAllColumns", "", function(data) { //2级目录 新闻。直播。。。
			$('.rem').remove();
			var data = data.list;

			for(var p = 0; p < 3; p++) {
				var prepends = `<li class="rem">
								<h3 class="sheadline">
									<span><img class="colIcon" src="${data[p].colIcon}"/></span>
									<span>${data[p].colName}</span>
									<span class="genduo" data_type = "${data[p].id}">+更多</span>
								</h3>
							</li>`;
				$('.clears').eq(p).prepend(prepends);
			};

			for(let l = 3; l < data.length; l++) {
				var prepen = `<li class="rem">
								<h3 class="sheadline">
									<span><img class="colIcon" src="${data[l].colIcon}"/></span>
									<span>${data[l].colName}</span>
									<span class="genduo" data_type = "${data[l].id}">+更多</span>
								</h3>
								<div class="datays"></div>
							</li>`;
				$('.finished').append(prepen);
				houty(transition, data[l].id, l - 3);
			};
				journaLism(transition, data[0].id);
				hot(transition, data[1].id);
				discovers(transition, data[2].id);
				
				setTimeout(function(){
					var datays = $('.datays');
					for(var p=0; p<datays.length; p++){
						if(datays.eq(p).children('.finished_1').length < 1){
							datays.eq(p).parents('.rem').remove();
						}
					}
				}, 4000);	
		});
	};

	function journaLism(transition, ids) { //新闻
		Ajxas(address() + transition, {"id": ids,"appUserId": "","start": 0,"limit": 7,"tag": 5}, function(data) {
			var data = data.list;
			xingw(data);
		});
	};

	function hot(transition, ids) { //直播
		Ajxas(address() + transition, {
			"id": ids,
			"appUserId": "",
			"start": 0,
			"limit": 4,
			"tag": 5
		}, function(data) {
			var data = data.list;
			zhibo(data);
			$('body').siblings().removeClass();
		});
	};

	function discovers(transition, ids) { //博文
		Ajxas(address() + transition, {
			"id": ids,
			"appUserId": "",
			"start": 0,
			"limit": 5,
			"tag": 5
		}, function(data) {
			var data = data.list;
			diabbo(data);
		});
	};

	function houty(transition, id, ise) { //后通用
		Ajxas(address() + transition, {
			"id": id,
			"appUserId": "",
			"start": 0,
			"limit": 3,
			"tag": 5
		}, function(data) {
			var data = data.list,
				jihe = "";
			for(let i = 0; i < data.length; i++) {

				//				栏目ID   		大图				小图				 标题				内容				 阅读次数			评论次数				点赞次数			      创建时间
				var arrdata = [data[i].colId, data[i].conImg, data[i].conImg1, data[i].conTitle, data[i].conRemark, data[i].playCount, data[i].exaCount, data[i].praiseCount, times(data[i].createDate)];
				jihe += `<a class="finished_1 htmls" href="javascript:void(0)" data_id="${data[i].id}">
							<img class="float_lefts" src="${arrdata[2]}" />
							<div class="float_lefts finished_datas">
								<div class="byline">${arrdata[3]}</div>
								<p>${arrdata[4]} </p>
								<p class="interaction">
									<span>${data[i].userName}</span>
									<span>&#xe671;${arrdata[5]}</span>
									<span>&#xe621;${arrdata[6]}</span>
									<span>&#xe607;${arrdata[7]}</span>
									<span>&#xe60e;${arrdata[8]}</span>
								</p>
							</div>
						</a>
						`
			};
			$('.datays').eq(ise).html(jihe);
		});
	};

	//新闻
	function xingw(data) {
		var jihe = "";
		for(let i = 0; i < data.length - 4; i++) {
			var conHtml = "";
			if(data[i].conHtml !== "") {
				conHtml = data[i].conHtml;
			};
			//				栏目ID   		大图				小图				 标题				内容				 阅读次数			评论次数				点赞次数			      创建时间
			var arrdata = [data[i].colId, data[i].conImg, data[i].conImg1, data[i].conTitle, data[i].conRemark, data[i].playCount, data[i].exaCount, data[i].praiseCount, times(data[i].createDate), conHtml];
			jihe += `<a class="htmls" href="javascript:void(0)" data_id="${data[i].id}">
							<img class="float_lefts" src="${arrdata[2]}" />
							<div class="float_lefts datas">
								<div class="byline">${arrdata[3]}</div>
								<p>${arrdata[4]}</p>
								<p class="interaction">
									<span>${data[i].userName}</span>
									<span>&#xe671;${arrdata[5]}</span>
									<span>&#xe621;${arrdata[6]}</span>
						 		<span>&#xe607;${arrdata[7]}</span>
						 			<span>&#xe60e;${arrdata[8]}</span>
						 		</p>
						 	</div>
						 </a>
						`
		};

		var conImg = "";
		if(data[3].conImg !== "") {
			conImg = data[3].conImg;
		};
		var jihes = `<img class="htmls img im" data_id="${data[3].id}" src="${conImg}">`;
		for(let l = 4; l < data.length; l++) {
			var conHtml = "";
			if(data[l].conHtml !== "") {
				conHtml = data[l].conHtml;
			};
			//				栏目ID   		小图	
			var arrdata = [data[l].colId, data[l].conImg, conHtml];
			jihes += `<img class="htmls img" data_id="${data[l].id}" src="${arrdata[1]}">`
		};
		$('.imgbox').html(jihes);
		$('.journalism_left li').html(jihe);
	};

	//直播
	function zhibo(data) {
		var jihes = "";
		for(let i = 0; i < data.length; i++) {
			var conHtml = "";
			if(data[i].conHtml !== "") {
				conHtml = data[i].conHtml;
			};
			//				栏目ID   		大图				小图				 标题				内容				 阅读次数			评论次数				点赞次数				创建时间
			var arrdata = [data[i].colId, data[i].conImg, data[i].conImg1, data[i].conTitle, data[i].conRemark, data[i].playCount, data[i].exaCount, data[i].praiseCount, times(data[i].createDate), conHtml];

			jihes += `<li>
						<a class="htmls" data_id="${data[i].id}" href="javascript:void(0)">
							<img src="${arrdata[2]}" />
						</a>
						<p class="interaction">
							<span>${data[i].userName}</span>
							<span>&#xe671;${arrdata[5]}</span>
							<span>&#xe621;${arrdata[6]}</span>
							<span>&#xe607;${arrdata[7]}</span>
							<span>&#xe60e;${arrdata[8]}</span>
						</p>
					</li>
					`
		};
		$('.livest').html(jihes);
	};

	//博文
	function diabbo(data) {

		var conHtmls = "";
		if(data[0].conHtml !== "") {
			conHtml = data[0].conHtml;
		};
		var jihes = `<li class="float_lefts htmls" data_id="${data[0].id}">
						<a href="javascript:void(0)">
							<img src="${data[0].conImg1}" />
						</a>
						<p class="interaction">
							<span>${data[0].userName}</span>
							<span>&#xe671;${data[0].playCount}</span>
							<span>&#xe621;${data[0].exaCount}</span>
							<span>&#xe607;${data[0].praiseCount}</span>
							<span>&#xe60e;${times(data[0].createDate*1)}</span>
						</p>
					</li>
					<li class="float_lefts">
					`;
		for(let i = 1; i < data.length; i++) {
			var conHtml = "";
			if(data[i].conHtml !== "") {
				conHtml = data[i].conHtml;
			};
			//				栏目ID   		大图				小图				 标题				内容				 阅读次数			评论次数				点赞次数				创建时间
			var arrdata = [data[i].colId, data[i].conImg, data[i].conImg1, data[i].conTitle, data[i].conRemark, data[i].playCount, data[i].exaCount, data[i].praiseCount, times(data[i].createDate)];

			jihes += `<div class="float_lefts htmls" data_id="${data[i].id}">
							<a href="javascript:void(0)">
								<img src="${arrdata[2]}" />
							</a>
							<p class="interaction">
								<span>${data[i].userName}</span>
								<span>&#xe671;${arrdata[5]}</span>
								<span>&#xe621;${arrdata[6]}</span>
								<span>&#xe607;${arrdata[7]}</span>
							</p>
						</div>`
		};
		jihes += `</li>`;
		$('.dibbler_zs').html(jihes);
	};

	function times(times) {
		var time = new Date(times),
			r = time.getDate() < 10 ? "0" + time.getDate() : time.getDate(),
			y = time.getMonth() + 1 < 10 ? "0" + (time.getMonth() + 1) : time.getMonth() + 1,
			n = time.getFullYear();
		return n + "-" + y + "-" + r;
	};

		//	上广告
	Ajxas(address() + 'getAllAdvertisingByPage', {
		'site': 0,
		'page': 0,
		'limit': 10
	}, function(data) {
		var guangg = `<a class="guangg_a" href="${data.content[0].linkUrl}">
					<img src="${data.content[0].imgUrl}" />
					<p>${data.content[0].remark}</p>
				</a>`;
		$('.guanggao').append(guangg);
	});

	//	左广告
	Ajxas(address() + 'getAllAdvertisingByPage', {
		'site': 1,
		'page': 0,
		'limit': 10
	}, function(data) {
		var htnll = "";
		for(let p = 0; p < data.content.length; p++) {
			htnll += `<a href="${data.content[p].linkUrl}">
						<img src="${data.content[p].imgUrl}" />
					</a>`
		};
		$('.guanggao_left').html(htnll);
	});

	//	右广告
	Ajxas(address() + 'getAllAdvertisingByPage', {
		'site': 2,
		'page': 0,
		'limit': 10
	}, function(data) {
		var htnll = "";
		for(let p = 0; p < data.content.length; p++) {
			htnll += `<a href="${data.content[p].linkUrl}">
						<img src="${data.content[p].imgUrl}" />
					</a>`
		};
		$('.guanggao_right').html(htnll);
	});
});