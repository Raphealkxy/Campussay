var kit = require('nokit');
var proxy = kit.require('proxy');
module.exports = function(app,opts){
    app.push(
        //拦截/api/mock,返回mock/jipeng/test.json文件
    );
}