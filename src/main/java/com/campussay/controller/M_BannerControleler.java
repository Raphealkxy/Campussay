package com.campussay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Banner;
import com.campussay.service.BannerService;
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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Controller
@RequestMapping("/banner")
public class M_BannerControleler {
	@Autowired
	private BannerService bannerservice;
	/***
	 * author:汪治宏
	 * 上传Banner图片(ajax异步操作)
	 * @throws Exception 
	 * 
	 */
	@RequestMapping(value="addbanner/",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject addbannner(HttpServletRequest request,
			@RequestParam("date") String date) throws Exception {
		List<Banner> list=JSON.parseArray(date, Banner.class);
		int i;
		try{
			i=bannerservice.addBanner(list);
		}catch(Exception e){
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
		}
		if(i!=0){
			return CommonUtil.constructResponse(1, null, null);
		}else{
			return CommonUtil.constructResponse(0, null, null);
		}
	}
	/***
	 * author:汪治宏
	 * 上传Banner图片(ajax异步操作)
	 * 
	 */
	@RequestMapping(value="bannerpicupload/",method=RequestMethod.POST)
	@ResponseBody
	public JSONObject bannerpicupload(HttpServletRequest request,
			long campus_id) {
		SimpleDateFormat myFmt2 = new SimpleDateFormat("yyyyMMdd");// 等价于now.toLocaleString()
		Date date = new Date();
		int count=0;
		//时间
		String banner_type="banner"+campus_id;
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
				String fileSuffixName = filename.substring(filename
						.indexOf("."));
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
				if (os.indexOf("win") >= 0)
					path = PropertiesUtils.getProp("path.win") + "/"+banner_type+"/"+date;
				else
					path = PropertiesUtils.getProp("path.linux") + "/"+banner_type+"/"+date;
				
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
					if (status) {
						// 存入数据库
						//bannerservice.bannerPicUploadfile(banner_type+"/"+filename1, campus_id
								//map.get(count).getBanner_desc(),map.get(count).getBanner_url());
						result = "上传成功";
						System.out.println("上传成功***");
						// 删除WEB服务器文件
							file3.delete();
						// TODO effine 文件上传成功返回URL需要讨论确
						return CommonUtil.constructResponse(1, result,
								banner_type+"/"+date+"/"+filename1);
					}
					count++;
				} catch (Exception e) {
					result = e.getMessage();

					e.printStackTrace();
					return CommonUtil.constructResponse(0, result, null);
				}

			}

		}
		return CommonUtil.constructResponse(0, null, null);
	}

}