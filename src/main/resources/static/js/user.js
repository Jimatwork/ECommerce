$(document).ready(function () {
    $('#band').show();

    var search = $('.ins').val();
    getAppUserInfo(search);
    // 图片操作
    $('body').on('mouseover', '.simg', function () {
        $(this).siblings('.simg1').css("display", "block");
    });
    $('body').on('mouseout', '.simg', function () {
        $(this).siblings('.simg1').css("display", "none");
    });

    //	// 左上方标题
    //	$('.page span').text(sessionStorage.getItem('conte'));
    // 修改权限
    $('body').on('click', '.userupdate', function () {
        var uid = getnum(this, 0);
        var qx = getnum(this, 6);
        $('.by_value1').val(uid);
        $('.by_value2').val(qx);
//      if (qx == 1) {
//          $('.delP1').text('确认把当前用户权限设为拒绝PC段登录吗？');
//
//      } else {
//          $('.delP1').text('确认把当前用户权限设为允许PC段登录吗？');
//      }
//      $(".banDel").show();
		var state = $(this).parent().parent().children().eq(4);
		var isPc = $(this).parent().parent().children().eq(6);
        if (qx == 1) {
            qx = 0;
        } else if (qx == 0) {
            qx = 1;
        }
        $.ajax({
            type: "post",
            url: getRequestIp() + "setPcLogin",
            async: true,
            data: {
                'id': uid,
                'isPc': qx
            },
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    alert("修改成功！");
                    if(state.html() == '允许') {
						state.html('拒绝');
					} else if(state.html() == '拒绝') {
						state.html('允许');
					}
					if (isPc.html() == 1) {
						isPc.html(0);
					} else if (isPc.html() == 0) {
						isPc.html(1);
					}
//                  $(".banDel").hide();
                    
//                  getAppUserInfo(search);
                }
            }
        });
    });

    // 修改权限
    $("#qx_yes").click(function () {

    });
    $(".close").click(function () {
        $(".banDel").hide();
    });
    $(".no").click(function () {
        $(".banDel").hide();
    });
    // 管理员密码
    $('#user_push').click(function () {
        var upwd = $('#user_pwd').val();
        if (upwd == 'userkbadmin') {
            $('#server_Div').css('display', 'none');
            $('#pageAll').css('display', 'block');
        } else {
            alert('密码错误！');
        }
    });

    // 修改用户密码
    $('body').on('click', '.updataPwd', function () {
        $('#up_pwd').css('display', 'block');
        var appUserId = getnum(this, 0);
        $('.hidden_appUserId').val(appUserId);
        //		var newPwd = $('.new_pwd').val();
        //		alert(newPwd)
    });
    // 修改用户密码的提交
    $("#pwd_yes").click(function () {
        var newPwd = $('.new_pwd').val();
        var appUserId = $('.hidden_appUserId').val();
        $.ajax({
            type: "post",
            url: getRequestIp() + "updateAppUserPwd",
            async: true,
            data: {
                'id': appUserId,
                'newPwd': newPwd
            },
            dataType: "json",
            success: function (data) {
                if (data.success) {
                    alert("修改成功！");
                    $(".banDel").hide();
//                  getAppUserInfo(search);
                }
            }
        });
    });
    // 删除用户
    $('body').on('click', '.del_user', function () {
        var appUserId = getnum(this, 0);
        if (confirm("确定删除序号为" + appUserId + "的数据吗？")) {

            $.ajax({
                type: "post",
                url: getRequestIp() + "delAppUserById",
                async: true,
                data: {
                    'id': appUserId
                },
                dataType: 'json',
                success: function (data) {
                    if (data.success) {
                        alert(data.msg);
                        getAppUserInfo(search);
                    } else {
                        alert(data.msg);
                    }
                }
            });
        }

    });
    // 成为商家
    $('body').on('click', '.save_bus', function () {
        var appUserId = getnum(this, 0);
        sessionStorage.setItem("save_bus_appid", appUserId);
        layui.use('layer', function () { //独立版的layer无需执行这一句
            layer.open({
                type: 2,
                title: '添加商家',
                maxmin: true,
                shadeClose: true, // 点击遮罩关闭层
                area: ['1050px', '550px'],
                content: 'addbusiness.html' // iframe的url
            });
        });
    });
    // 模糊搜索用户

    $("#search_user").click(function () {
        var search = $('.ins').val();
        getAppUserInfo(search);
    });

});

// 获取app用户数据
function getAppUserInfo(search) {
    $.ajax({
        type: "post",
        url: getRequestIp() + "getAppUserList",
        async: true,
        data: {
            'page': 0,
            'limit': 10,
            'search': search
        },
        dataType: "json",
        success: function (data) {
            getAppUser(data.list);
            var pageNum = data.totalSize / 10;
            pageNum = Math.ceil(pageNum);
            // 分页
            var pageNavObj = null;
            jQuery(document).ready(function ($) {
                //初始化
                //pageNavCreate("PageNav",200,1,pageNavCallBack);
                pageNavObj = new PageNavCreate("PageNavId", {
                    pageCount: pageNum, //总页数
                    currentPage: 1, //当前页
                    perPageNum: 5, //每页按钮数
                });
                pageNavObj.afterClick(pageNavCallBack);
            });

            //翻页按钮点击后触发的回调函数
            function pageNavCallBack(clickPage) {
                $.ajax({
                    type: "post",
                    url: getRequestIp() + "getAppUserList",
                    async: true,
                    data: {
                        'page': clickPage - 1,
                        'limit': 10,
                        'search': search
                    },
                    dataType: "json",
                    success: function (data2) {
                        getAppUser(data2.list);
                    }
                });
                pageNavObj = new PageNavCreate("PageNavId", {
                    pageCount: getPageSet().pageCount, //总页数
                    currentPage: clickPage, //当前页
                    perPageNum: getPageSet().perPageNum, //每页按钮数
                });
                pageNavObj.afterClick(pageNavCallBack);
            }

            //本示例页的一些js
            function getPageSet() {
                var obj = {
                    pageCount: null, //总页数
                    currentPage: null, //当前页
                    perPageNum: null, //每页按钮数
                }
                if ($("#testPageCount").val() && !isNaN(parseInt($("#testPageCount").val()))) {
                    obj.pageCount = parseInt($("#testPageCount").val());
                } else {
                    obj.pageCount = parseInt($(".page-input-box > input").attr("placeholder"));
                }

                if ($("#testCurrentPage").val() && !isNaN(parseInt($("#testCurrentPage").val()))) {
                    obj.currentPage = parseInt($("#testCurrentPage").val());
                    obj.currentPage = (obj.currentPage <= obj.pageCount ? obj.currentPage : obj.pageCount);
                } else {
                    obj.currentPage = 1;
                }

                if ($("#testPerPageNum").val() && !isNaN(parseInt($("#testPerPageNum").val()))) {
                    obj.perPageNum = parseInt($("#testPerPageNum").val());
                } else {
                    obj.perPageNum = null;
                }

                return obj;
            }
        }
    });
}

// 获取对象的某个值
function getnum(obj, num) {
    return $(obj).parent().parent().children().eq(num).text();
}

// 渲染用户数据
function getAppUser(data) {
    $('.user_tab tr td').remove();
    $('.user_tr').remove();
    var item;
    $.each(data, function (i, result) {
        item += '<tr class="user_tr">'
        item += '<td class="uid">' + result.id + '</td>'
        if (result.phone != null) {
        	item += '<td class="uphone">' + result.phone + '</td>'
        } else {
        	item += '<td class="uphone"></td>'
        }
        
       	if (result.userName != null) {
       		 item += '<td class="uname">' + result.userName + '</td>'
       	} else {
       		 item += '<td class="uname"></td>'
       	}
       
        item += '<td class="timg"><img class="simg" src="' + result.userImg + '" /><img class="simg1" src="' + result.userImg + '" /></td>'
        if (result.isPc == 1) {
            item += '<td>' + "允许" + '</td>'
        } else {
            item += '<td>' + "拒绝" + '</td>'
        }
        item += '<td>'
        item += '<img class="userupdate" src="img/update.png" title="修改权限" />&nbsp;&nbsp;'
        item += '<img class="updataPwd" src="images/mima.png" title="修改密码" />&nbsp;&nbsp;'
        item += '<img class="del_user" src="img/delete.png" title="删除" />&nbsp;&nbsp;'
//      if (result.bid == null) {
//          item += '<img class="save_bus" src="img/business1.png" title="成为商家" />&nbsp;&nbsp;'
//      }
        item += '</td>'
        item += '<td style="display: none;">' + result.isPc + '</td>'
        item += '</tr>';
    });
    $('.user_tab').append(item);

    $('.banDel').hide();
}