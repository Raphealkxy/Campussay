/**
 * Created by nerzer on 2015/12/21.
 */
define(function (require,exports,module) {
    //获取依赖
    var $ = require('lib/jquery'),
        kkpager = require('kkpager');
    function _getParameter(name) {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r!=null) return unescape(r[2]); return null;
    }

    function pageInit(total,records,fn){
        var totalPage = total;
        var totalRecords = records;
        var pageNo = _getParameter('pno');
        if(!pageNo){
            pageNo = 1;
        }
        //生成分页
        //有些参数是可选的，比如lang，若不传有默认值
        kkpager.generPageHtml({
//		不显示首页
            isShowFirstPageBtn:false,
//		不尾部
            isShowLastPageBtn:false,
//		不现实当前页
            isShowCurrPage:false,
            pno : pageNo,
            //总页码
            total : totalPage,
            //总数据条数
            totalRecords : totalRecords,
            getLink : function(n){
                return this.hrefFormer + this.hrefLatter + "?pno="+n;
            },
            mode : 'click',//默认值是link，可选link或者click
            click : function(n){
                this.selectPage(n);
                fn&&fn(n);
                return false;
            }
        });
    }
    module.exports = {
        pageInit:pageInit
    }
});