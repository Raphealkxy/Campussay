var kit = require('nokit');
//同步文件的客服端
var client = require('nobone-sync/client');
var proxy = kit.require('proxy');
/**
 * 用于liveReload浏览器自动刷新的服务端
 * 参考：https://github.com/ysmood/nokit 里面热词serverHelper
 */
var sHandler = proxy.serverHelper();
/**
 * 被注入到浏览器里面的刷新页面的代码
 * 参考：https://github.com/ysmood/nokit 里面热词browserHelper
 */
var cHandler = kit.browserHelper();
//取得nofile传过来的option
var opts = JSON.parse(process.argv[2]);
var app = proxy.flow();
var mock = require('./mock/'+opts.user)(app,opts);
//打印log
app.push(function (ctx) {
    console.log("access:", ctx.req.url);
    return ctx.next();
});
app.push(
    sHandler,
    proxy.select('/static', proxy.static('static')),
    proxy.url({
        url: '103.37.161.234',
        isForceHeaderHost:false,
        handleResBody:function(body, req, proxyRes){
            var contentType = proxyRes.headers['content-type'];
            if(contentType && /^text\/html/.test(contentType)){
                return body + cHandler;
            }else{
                return body;
            }
        }
    })
);
/*
 * 监听本地static里面的文件改变
 * 然后想浏览器客服端发送请求，用的是HTML5 Server Sent Event
 */
kit.watchFiles('static/**', {
    handler: function(path, curr, prev, isDeletion){
        //Server Sent Event
        //https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events
        sHandler.sse.emit('fileModified',path);
    }
});
var  nb_config = require('./nobone-sync.config')
nb_config.onChange = function (type, path, oldPath) {
    sHandler.sse.emit('fileModified',path);
}
client(nb_config);
app.listen(opts.port);