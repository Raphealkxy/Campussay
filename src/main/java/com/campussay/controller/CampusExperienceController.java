package com.campussay.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.campussay.model.User;
import com.campussay.service.CampusExperienceService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/experience")
public class CampusExperienceController {

    @Autowired
    private CampusExperienceService campusExperienceService;

    /**
     * 个人设置 实践经历 添加
     *yc: 两个for 循环必须改
     */
    @RequestMapping("userSettingCampusExperience")
    @ResponseBody
    public JSONObject userSettingCampusExperience(HttpServletRequest request,String data){
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
//                    campusExperienceService.updateCampusExperience(j);
//                }
                try{
                	campusExperienceService.updateCampusExperience(changeArray,userId);
                }catch(Exception e){
  				  	return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库错误",null);
  			  	}
//                for (Object o : addArray){
//                    JSONObject j = JSONObject.parseObject(o.toString());
//                    j.put("user_id",userId);
//                    campusExperienceService.addCampusExperience(j);
//                }
                try{
                    campusExperienceService.addCampusExperience(addArray,userId);
                }catch(Exception e){
  				  	return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库错误",null);
  			  	}
                if (del != null && !"".equals(del)){
                    campusExperienceService.delCampusExperience(del,userId);
                }
            }catch (Exception e){
                return CommonUtil.constructResponse(0,"解析错误",null);
            }
        }
        return CommonUtil.constructResponse(1,null,null);
    }

    /**
     *  实践经历 查询
     */
    @RequestMapping("getUserSettingCampusExperience")
    @ResponseBody
    public JSONObject getUserSettingPracticeExperience(HttpServletRequest request) {
        User user=(User)request.getAttribute("user");
        if(user==null)
            return CommonUtil.constructResponse(0,"你还未登陆",null);
        int userId=user.getUserId();
        List<HashMap<String, Objects>> list=null;
        try {
        	list=campusExperienceService.getCampusExperience(userId,10);
        } catch (Exception e) {
            return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
        }
        if(list==null){
        	return CommonUtil.constructResponse(1,"数据库没有数据",list);
        }
        return CommonUtil.constructResponse(1,"获取数据成功",list);
    }


}
