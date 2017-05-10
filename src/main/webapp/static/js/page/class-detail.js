/**
 *  @description 课程详情
 *  @author 吴伦毅
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});
require(['lib/jquery','modules/talkingDetail'], function ($,talkingDetail) {
   $(function () {
       talkingDetail.init();
   });
});