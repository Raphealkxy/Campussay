/**
 *  Created by nerzer on 2015/12/13.
 */
require.config({
    baseUrl: CP.STATIC_ROOT
});
require(['lib/jquery', 'modules/talkingList'], function ($, talking) {
    var index = {
        init: function () {
            talking.init();
        }
    };
    index.init();
});

