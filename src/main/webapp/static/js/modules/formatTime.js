/**
 * 格式化时间展示 注册到juicer-handleTime上
 */
define(['lib/juicer'], function () {
	var _handleTime = function(time) {
		var currentTime = new Date();
        var startTime = new Date(time);
        var year = currentTime.getFullYear()-startTime.getFullYear();
        var month = currentTime.getMonth()-startTime.getMonth();
        var day = currentTime.getDate()-startTime.getDate();
        if(year){
            return year+"年前"
        }
        if(month>1){
            return month+"月前"
        }
        if(day>0){
            if(day ==1 ){
                if(startTime.getMinutes()<10){
                    return '昨天'+startTime.getHours()+':0'+startTime.getMinutes()
                }else{
                    return '昨天'+startTime.getHours()+':'+startTime.getMinutes()
                }
            }else{
                return day+"天前"
            }
        }else{
            if(startTime.getMinutes()<10){
                return startTime.getHours()+':0'+startTime.getMinutes()
            }else{
                return startTime.getHours()+':'+startTime.getMinutes()
            }
        }
	};

	juicer.register('handleTime', _handleTime);
});