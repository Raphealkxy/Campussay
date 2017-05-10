/**
 * 数组去重
 * wangxinyu
**/

define(function(){
	return function(arr){
		var result = [];
		for (var i = 0; i < arr.length; i++) {
			if (result.indexOf(arr[i]) == -1) result.push(arr[i]);
		}
		return result;
	}
})