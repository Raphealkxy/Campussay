require.config({
    baseUrl :  CP.STATIC_ROOT
});
require(['lib/jquery','modules/indexBanner','modules/picScroll','util/request','util/funcTpl','modules/baseMoudle','modules/switchSchool/schoolIndex'],
		function($,banner,picScroll,request,funcTpl,baseMoudle,schoolIndex) {
	var index = {
		init:function () {	
			//初始化学校，并且初始化于学校相关的banner，牛人，精品talking
			schoolIndex.initSchool();

		}		
	};

	index.init();
});

