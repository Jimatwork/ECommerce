(function($) {
    document.addEventListener('plusready', function(){
        //console.log("所有plus api都应该在此事件发生后调用，否则会出现plus is undefined。"
   });
   $.getUrlParam = function(name){  
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");  
        var r = window.location.search.substr(1).match(reg);  
        if (r!=null) return unescape(r[2]); return null;  
   };
   var data_arr = $.getUrlParam('conId');
   publicAjax("getColContentById", {"id":data_arr}, function(data){
        if(data.success){
            var datas = data.list;
            
            var dataConHtml = "", dataVideoPath = "";
            
            if(datas[0].conHtml !== null) {
                dataConHtml = datas[0].conHtml;
            };
            
            if(datas[0].conVideoPath !== null) {
                dataVideoPath = datas[0].conVideoPath;
            };
            
            if(dataVideoPath !== ""){
                $('.slideshow').hide(); 
                $('#videos').attr('src', dataVideoPath);
            }else{
                $('#videos').hide();
                $('.slideshow').attr("src",datas[0].conImg);
            };
            
            $('.mingzis').html(datas[0].userName);       
            $('.datan').html(times(datas[0].createDate)+'<span>/'+times2(datas[0].createDate)+'</span>');
            $('.yuedu').html(datas[0].playCount);
            $('.zhaiyao').html(datas[0].conRemark);
            $('.presentation').html(dataConHtml+"</br></br></br></br></br>");
            $('.xiaobiao').html(datas[0].exaCount);
            
        };
   });
    
    /**
     *点赞 
     */
    $('.share1').click(function(){
        var vals = $(this).siblings(".share").children().val();
    });
    
    /**
     *收藏 
     */
    $('.share2').click(function(){
        var vals = $(this).siblings(".share").children().val();
    });
    
    /**
     *关注 
     */
    $('.mingzis2').click(function(){
        var vals = $(this).html();
    });

//获取年月日
    function times(times){
        var time = new Date(times*1),
            r = time.getDate() < 10 ? "0" + time.getDate() : time.getDate(),
            y = time.getMonth() + 1 < 10 ? "0" + (time.getMonth() + 1): time.getMonth() + 1,
            n = time.getFullYear();
        return n +"-"+ y +"-"+ r;
    };
    
//获取时分秒
    function times2(times){
        var time = new Date(times*1),
            hour = time.getHours() < 10 ? "0" + time.getHours() : time.getHours();
            minute = time.getMinutes() < 10 ? "0" + time.getMinutes() : time.getMinutes();
            second = time.getSeconds() < 10 ? "0" + time.getSeconds() : time.getSeconds();
        return hour + ":" + minute;
    };
})($);

