/**
 *  Created by nerzer on 2015/12/21.
 */
require.config({
	shim:{
		kkpager:{
			deps:['jquery'],
			exports:'kkpager'
		}
	},
    paths:{
        jquery:"jquery-1.10.2.min",
	    kkpager:"kkpager"
    }
});
//仅为演示代码
require(['jquery','pager'], function ($, page) {
    $(function () {
        //pageInit(totalPage,totalList,nextStep)
        //总页数，总条数，回掉函数
        page.pageInit(20,390, function (n) {
            //n表示即将跳转的页数
            //这里写自己的ajax请求代码
	        console.log("go to"+n+"页");
        });
    })
});
