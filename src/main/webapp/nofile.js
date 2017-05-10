/**
 *  前端脚手架入口文件
 *  参考：https://github.com/ysmood/nokit
 *  最完整的脚手架：https://github.com/ysmood/mx-fe-bone
 **/
var kit = require('nokit');
module.exports = function (task, option) {
    option('--port <number>', '服务器端口号', 8081);
    option('--liveReload <str>', '是否启动自动刷新页面: on 或 off', 'on');
    option('--user <str>', 'mock数据', 'jipeng');
    task('default', '启动开发服务', function (option) {
        /**
         * 监听server.js,mock文件里面的改动，然后重启no服务
         * 达到改变被监听的文件不用手动启动node服务的目的 
         **/
        kit.monitorApp({
            args: ['server.js', JSON.stringify(option)],
            watchList:['server.js','mock/**']
        });
    });
};