package com.campussay.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.campussay.model.User;
import com.campussay.service.PrizeService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/prize")
public class PrizeController {

    @Autowired
    private PrizeService prizeService;
    /**
     * 个人设置，获奖成果 添加
     * @param
     * @return
     */
    @RequestMapping("userSettingPrize")
    @ResponseBody
    public JSONObject userSettingPrize(HttpServletRequest request, String data) {
        // TODO 文件上传
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
//                    prizeService.updatePrize(j);
//                }
                try{
                	prizeService.updatePrize(changeArray,userId);
                }catch(Exception e){
                	 return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库错误",null);
                }
//                for (Object o : addArray){
//                    JSONObject j = JSONObject.parseObject(o.toString());
//                    j.put("user_id",userId);
//                    prizeService.addPrize(j);
//                }
                try{
                	prizeService.addPrize(addArray,userId);
                }catch(Exception e){
                	return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"数据库错误",null);
                }
                if (del != null && !"".equals(del)){
                    prizeService.delPrize(del,userId);
                }
            }catch (Exception e){
                e.printStackTrace();
                return CommonUtil.constructResponse(0,"解析错误",null);
            }
        }
        return CommonUtil.constructResponse(1,null,null);
    }

    /**
     * 个人设置，获奖成果 查询
     * @return
     */
    @RequestMapping("getUserSettingPrize")
    @ResponseBody
    public JSONObject getUserSettingPrize(HttpServletRequest request) {
        try {
            User user = (User)request.getAttribute("user");
            int userId = user.getUserId();
            return prizeService.getUserSettingPrize(userId,10);
        } catch (Exception e) {
            return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "系统错误", null);
        }
    }
}
