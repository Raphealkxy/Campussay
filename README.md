
调试须知：
	
	1、以maven工程方式导入开发工具（eclipse/idea）
	2、修改数据库配置文件: src/main/resources/database.properties
	3、调试示例参考：src/test/java/com/campussay/TestMyBatis.java
	4、由于*Mapper.xml中很多代码未调试导致项目无法启动，我统一移动到com.campussay.mapper_backup下
	
	
代码提交须知：

	1、代码本地调试通过，才能push到远程
	2、对所提交的功能进行必要的描述（尽量完成一个功能进行一次提交commit）


页面跳转提示：

	1.controller 配置访问请求的URL(通过注解 @RequestMapping 在方法或类设置)
	2.方法的返回值配置页面路径即可; 
	3.通过命令 "mvn jetty:run" 运行，默认访问地址：http://localhost:8000/
	
	案例： 
		请       求：localhost:8000/test/topage
		跳转页面： src/main/webapp/WEB-INF/views/personal-page/personal-comment.ftl
		参考: com.campussay.controller.BaseController.java  -->  toTestPage()方法  
		
		
		
	