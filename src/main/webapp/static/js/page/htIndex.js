require.config({
    baseUrl: CP.STATIC_ROOT
});
require(['lib/jquery','modules/htIndexData','modules/baseMoudle'], function ($,htData) {
    $(function () {
        htData.init();
    });
});