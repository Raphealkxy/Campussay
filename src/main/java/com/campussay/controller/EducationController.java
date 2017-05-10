package com.campussay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Banner;
import com.campussay.model.StudentCheck;
import com.campussay.model.User;
import com.campussay.service.BannerService;
import com.campussay.service.EducationService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;
import com.campussay.util.PropertiesUtils;
import com.campussay.util.SftpUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/education")
public class EducationController {

    @Autowired
    private EducationService educationService;

    @Autowired
    private BannerService bannerservice;

//    /**
//     * 个人设置 教育经历 添加
//     * {"educationTime":20160121,"":""}
//     *
//     * @param education
//     * @return
//     */
//    @RequestMapping("userSettingEducation")
//    @ResponseBody
//    public JSONObject userSettingEducation(HttpServletRequest request, Education education) {
//        // 从页面session中获得用户id
//        User user = (User)request.getAttribute("user");
//        if(user==null)
//            return CommonUtil.constructResponse(0,"你还未登录",null);
//        if(education.getEducationAcademe()==null||education.getEducationCampusName()==null||education.getEducationDegree()==null||education.getEducationMajor()==null
//                ||education.getEducationTime()==null )
//            return CommonUtil.constructResponse(0,"不允许空值",null);
//        int userId=user.getUserId();
//        education.setEducationUser(userId);
//        System.out.println(educationService.userSettingEducation(education));
//
//        if (educationService.userSettingEducation(education) == 1)
//            return CommonUtil.constructResponse(1, "添加成功", null);
//        else
//            return CommonUtil.constructResponse(0, "添加失败", null);
//    }
    /*
    *for 放在serverce
    */
    @RequestMapping("userSettingEducation")
    @ResponseBody
    public JSONObject userSettingEducation(HttpServletRequest request,@RequestParam("data") String data){
        int userId = ((User)request.getAttribute("user")).getUserId();
        if (data != null){
            try {
                JSONObject jo = JSONObject.parseObject(data);
                JSONArray changeArray = jo.getJSONArray("change"); //获取修改的array
                JSONArray addArray = jo.getJSONArray("add");  //获取添加的array
                String del = jo.getString("del");
//                for (Object o : changeArray){
//                    JSONObject j = JSONObject.parseObject(o.toString());
//                    j.put("user_id",userId);
//                    educationService.updateEducation(j);
//                }
                try{
                	educationService.updateEducation(changeArray,userId);
                }catch(Exception e){
                	return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库错误",null);
  			  	}
//                for (Object o : addArray){
//                    JSONObject j = JSONObject.parseObject(o.toString());
//                    j.put("user_id",userId);
//                    educationService.addEducation(j);
//                }
                try{
                	educationService.addEducation(addArray,userId);
            	}catch(Exception e){
            		return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库错误",null);
			  	}
                if (del != null && !"".equals(del)){
                    educationService.delEducation(del,userId);
                }
            }catch (Exception e){
                return CommonUtil.constructResponse(0,"解析错误",null);
            }
        }
        return CommonUtil.constructResponse(1,null,null);
    }
    /**
     * 个人设置 教育经历 查询
     *
     * @return
     */
    @RequestMapping("getUserSettingEducation")
    @ResponseBody
    public JSONObject getUserSettingEducation(HttpServletRequest request) {
        // 从页面session中获得用户id
        try {
            User user = (User) request.getAttribute("user");
            int userId = user.getUserId();
            return educationService.getUserSettingEducation(userId,10);
        } catch (Exception e) {
            return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
        }
    }

    /**
     * 个人设置 教育经历 查询学院
     * @param academy
     * 根据前台实时传来的字符串来进行模糊查询，返回给前端
     */
    @RequestMapping("getAllAcademy")
    @ResponseBody
    public JSONObject getAllAcademy(String academy) {
        try {
            return educationService.getAllAcademy(academy);
        } catch (Exception e) {
            return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
        }
    }


    /**
     * 个人设置 教育经历 查询专业
     * @param major
     * 前台发送用户填写的专业名称，在数据库中进行实时查询，返回给前端
     */
    @RequestMapping("getAllMajor")
    @ResponseBody
    public JSONObject getAllMajor(HttpServletRequest request, String major) {
        User user = (User) request.getAttribute("user");
        if (user == null) {
            return CommonUtil.constructResponse(0, "你还未登陆", null);
        }
        try {
            return educationService.getAllMajor(major);
        } catch (Exception e) {
            return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
        }
    }


    /***
     * author:汪治宏
     * 上传Banner图片(ajax异步操作)
     * @throws Exception 
     */
    @RequestMapping(value = "addbanner/", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject addbannner(HttpServletRequest request,
                                 @RequestParam("date") String date) throws Exception {
        List<Banner> list = JSON.parseArray(date, Banner.class);
        if (bannerservice.addBanner(list) != 0) {
            return CommonUtil.constructResponse(1, null, null);
        } else {
            return CommonUtil.constructResponse(0, null, null);
        }
    }


    
    @RequestMapping(value="/educationupload")
    @ResponseBody()
    public JSONObject educationupload(HttpServletRequest request,
                                       HttpServletResponse response)
    {
        response.setContentType("application/json; charset=UTF-8");
        User user=(User) request.getAttribute("user");
        int user_id = user.getUserId();
        SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyyMMdd");// 等价于now.toLocaleString()
        Calendar now = Calendar.getInstance();
        int year=now.get(Calendar.YEAR);
        int mouth=(now.get(Calendar.MONTH) + 1);
        String date=year+"."+mouth;
        //时间
        String Releasetalking_user="student_check_picture"+user_id;
        String result = null;// 上传后返回情况说明
        String path = null;// 上传图片路径
        // //创建一个通用的多部分解析器
        CommonsMultipartResolver cmr = new CommonsMultipartResolver(request
                .getSession().getServletContext());
        // //判断 request 是否有文件上传,即多部分请求
        if (cmr.isMultipart(request)) {
            // 转换成多部分request
            MultipartHttpServletRequest mhsr = (MultipartHttpServletRequest) request;
            // //取得request中的所有文件名
            Iterator<String> iter = mhsr.getFileNames();
            while (iter.hasNext()) {
                // //取得上传文件
                MultipartFile file = mhsr.getFile((String) iter.next());
                // //取得当前上传文件的文件名称
                String filename = file.getOriginalFilename();
                // 获得文件后缀
                String fileSuffixName = filename.substring(filename.indexOf("."));
                /**
                 * 上传文件大小,类型判断
                 */
                if (file.getSize() > 1048576) {
                    result = "上传失败：上传文件大小大于1M";

                    return CommonUtil.constructResponse(0, result, null);

                } else if (!fileSuffixName.equals(".jpg")
                        && !fileSuffixName.equals(".png")
                        && !fileSuffixName.equals(".gif")) {
                    result = "上传失败：上传文件类型不正确";

                    return CommonUtil.constructResponse(0, result, null);

                }

                // 生成的GUID为一串32位字符组成的128位数据上传文件重命名filename1
                CommonUtil cu = new CommonUtil();
                String UUID = cu.GUID();

                String filename1 = UUID + fileSuffixName;
                // 验证当前操作系统，构建上传路径
                String os = System.getProperty("os.name").toLowerCase();
                if (os.indexOf("win") >= 0){
                    path = PropertiesUtils.getProp("path.win") + "/"+Releasetalking_user+"/"+date.toString();
                   System.out.print("path"+path);
                }
                else
                    path = PropertiesUtils.getProp("path.linux") + "/"+Releasetalking_user+"/"+date.toString();

                // 定义上传路径
                // path =
                // "http://103.249.252.139:9001/usr/sendfoodpic/"+filename1;
                // String path2 =
                // path = "/static/image/icon/"+filename1;

				/*
				 * File file2 = new File(path2);
				 *
				 * String snacks_pic1 = path2;
				 */

                File file2 = new File(path);
                if(!file2.exists()){
                    file2.mkdirs();
                }

                File file3 = new File(path+"/"+filename1);
                // String snacks_pic1 = filename1;
                try {
                    // transfer方法是MultipartFile包中提供的方法，直接可以写入文件到指定目录
                    file.transferTo(file3);

                    // 复制Web服务器文件到文件服务器
                    boolean status = SftpUtils.uploadFile(path+"/"+filename1);
                    System.out.println("path2"+path);


                    if (status) {
                        result = "上传成功";
                        System.out.println("上传成功***");
                        // 删除WEB服务器文件
                        file3.delete();
                        // TODO effine 文件上传成功返回URL需要讨论确定
                        JSONObject jo=new JSONObject();
                        jo.put("url", Releasetalking_user+"/"+date.toString()+"/"+filename1);
                        return CommonUtil.constructResponse(1, result, jo);
                    }

                } catch (Exception e) {
                    result = e.getMessage();

                    e.printStackTrace();
                    return CommonUtil.constructResponse(0, result, null);
                }

            }
        }
        return  CommonUtil.constructResponse(0, null, null);
    }

       /**
        * 个人设置2 插入个人教育信息
        * 学生认证
        *
        * @param sc (StudentCheck的封装类)
        * @return JSONbject
        * pzy
        */

    @RequestMapping(value = "insertStudent")
    @ResponseBody
    public JSONObject insertStudent(HttpServletRequest request, StudentCheck sc) {
        User user = (User) request.getAttribute("user");
        if (user == null) {
            return CommonUtil.constructResponse(0, "你还未登陆", null);
        } else if (sc.getStudentCheck()<=-1||sc.getStudentCheckPicture()==""||"".equals(sc.getStudentCheckPicture())) {
            return CommonUtil.constructHtmlResponse(0,"请输入正确参数",null);
        }
               sc.setStudentCheckUer(user.getUserId());
               int st = educationService.insertStudInform(sc);
               return CommonUtil.constructResponse(1, null, st);
           }


    /*
    * 个人设置，显示学生证图片
    * */
    @RequestMapping(value = "getStudentPicture")
    @ResponseBody
    public JSONObject getStudentPicture(HttpSession session){
        User user= (User) session.getAttribute("user");
        if(user==null){
            return CommonUtil.constructResponse(0,null,"请登录");
        }
        return CommonUtil.constructResponse(1,null,educationService.studentPicture(user.getUserId()));
    }

    }


