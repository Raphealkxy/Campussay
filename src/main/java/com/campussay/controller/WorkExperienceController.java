package com.campussay.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.campussay.model.User;
import com.campussay.service.WorkExprienceService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


@Controller
@RequestMapping("/work")
public class WorkExperienceController {

    @Autowired
    private WorkExprienceService workExprienceService;

    /**
     * 个人设置 工作经历 添加
     * @return
     */
    @RequestMapping("userSettingWorkExperience")
    @ResponseBody
    public JSONObject userSettingWorkExperience(HttpServletRequest request, String data) {
        int userId = ((User)request.getAttribute("user")).getUserId();
        if (data != null){
            try {
                JSONObject jo = JSONObject.parseObject(data);
                JSONArray changeArray = jo.getJSONArray("change"); //获取修改的array
                JSONArray addArray = jo.getJSONArray("add");  //获取添加的array
                String del = jo.getString("del");
                for (Object o : changeArray){
                    JSONObject j = JSONObject.parseObject(o.toString());
                    j.put("user_id",userId);
                    workExprienceService.updateWorkExprience(j);
                }
                for (Object o : addArray){
                    JSONObject j = JSONObject.parseObject(o.toString());
                    j.put("user_id",userId);
                    workExprienceService.addWorkExprience(j);
                }
                if (del != null && !"".equals(del)){
                    workExprienceService.delWorkExprience(del,userId);
                }
            }catch (Exception e){
                e.printStackTrace();
                return CommonUtil.constructResponse(0,"解析错误",null);
            }
        }
        return CommonUtil.constructResponse(1,null,null);
    }

    /**
     * 个人设置 工作经历 查询
     * @return
     */
    @RequestMapping("getUserSettingWorkExperience")
    @ResponseBody
    public JSONObject getUserSettingWorkExperience(HttpServletRequest request) {
        try {
            return workExprienceService.getUserSettingWorkExperience(((User)request.getAttribute("user")).getUserId(),10);
        } catch (Exception e) {
            return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
        }
    }

}
