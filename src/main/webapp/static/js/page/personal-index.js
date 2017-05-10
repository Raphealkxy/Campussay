require.config({
    baseUrl: CP.STATIC_ROOT
});
require(['lib/jquery','modules/personalIndex','modules/baseMoudle'],function($,user){
    user.init();
});