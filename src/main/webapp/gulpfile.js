//使用方法:命令行,执行 gulp，就可以完成sass文件监听并编译
var gulp = require('gulp'),
    fs   = require('fs'),
    sass = require('gulp-sass'),
    //rev  = require('gulp-rev'),//自动版本号
    //revCollector = require('gulp-rev-collector'),//自动版本号
    livereload = require('gulp-livereload');
    sprite = require('gulp.spritesmith');



//1.编译sass
gulp.task('sass',function(){
    console.log("sass work");
    //**可以匹配所有目录下的所有文件
    gulp.src('static/sass/**/*.scss')
        .pipe(sass({
            includePaths:['static/sass']
        }))//坑，gulp－sass必须在参数里指明，@import的路径 .已修复，includePaths参数可以添加impotr路径
        .pipe(gulp.dest('static/css/'));

});


gulp.task('watch', function() {
    gulp.watch('static/sass/**/*.scss', ['sass']); 
});

//3.gulp 实时浏览器刷新
gulp.task('live', function () {  
    livereload.listen();
    //var server = livereload(9000);
    
    // gulp.watch('static/**/**', function (file) {
    //     console.log("live watch");
    //     server.changed(file.path);
    // });
        gulp.watch('static/**/*.*').on('change', function(e){
        /// and when they do change, notify the server with a path of a changed file
        console.log("live watch");
        livereload.changed(e.path);
    });
});

//2.合并雪碧图
gulp.task('sprite',function(){
	var spriteData = gulp.src('static/img/sprite/*.png') //1.需要合并的图片文件夹
		.pipe(sprite({ 
        imgName: 'sprite.png',    // 生成雪碧图的名字
        cssName: 'sprite.css'     // 生成雪碧图的css文件
    }));

    spriteData.img.pipe(gulp.dest('static/img/sprite')); // 2.合成雪碧图的文件夹，sprite.png
    spriteData.css.pipe(gulp.dest('static/img/sprite')); // 3.生成雪碧图的css文件，sprite.css
   
});

//4.自动打版本号version，测试jekins是否生效
/*gulp.task("vers",function(){
     gulp.src("static/css/global.css")
               .pipe(rev())
               //.pipe(rev.manifest())
               .pipe(gulp.dest("static/css/lib"));
});*/

gulp.task("vers",function(){
    var time = new Date();
    var day  = time.getDate();  
    var hours = time.getHours();
    fs.rename(__dirname + '/static/css/global.css', __dirname + '/static/css/global.css?v='+day+"日"+hours+"时", function (err) {
            if(err) {
                console.error(err);
                return;
            }
            console.log('重命名成功')
        });
});



gulp.task('default',['watch','live'],function(){
	console.log("执行 default");//
});