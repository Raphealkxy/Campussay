/**
 * 
 *  图片滚动插件如：首页精品talking专区
 *  @author jipeng
 */

define(['lib/jquery','util/request','util/funcTpl','lib/juicer'], function ($,request,funcTpl) {
	'use strict';
	var scroll = {
		init:function (domObj) {
			scroll._decorators(domObj);
		},
		_tpl:function () {
			/*
			<div class="talkings">
				<ul class="talking-list">
					 {@each imgList as item,index}
					    <li class="talk-item {@if index%4 == 3}last-item{@/if}">
							<img src="${item.src}" alt="${item.name}" onclick="location.href='${item.link}'"/>
						</li>
					 {@/each}
				</ul>
				<a class="arrow arrow-left"><i></i></a>
				<a class="arrow arrow-right"><i></i></a>
			</div>
			*/
		},
		_decorators:function (domObj) {
			request.post('/gettalkingList',{},function (res) {
				res = JSON.parse(res);
			    if(0 == res.code){
					var tmp = juicer(funcTpl(scroll._tpl),res.data);
					$(domObj).append(tmp);
					scroll._annimation();
				}else{
					
				}
			})
		},
		_annimation:function () {
			
		}
	}
	return scroll.init;
})