package com.campussay.controller;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.User;
import com.campussay.service.TalkingTypeService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/talkingType")
public class TalkingTypeController {

    @Autowired
    private TalkingTypeService talkingTypeService;

    /**
     * 获取用户的擅长领域
     * @param userId 被获取者的id
     * @return json
     */
    @RequestMapping("getUserSkill")
    @ResponseBody
    public JSONObject getUserSkill(Integer userId){
        if (userId == null || "".equals(userId)){
            return CommonUtil.constructResponse(0,"参数错误",null);
        }
        return CommonUtil.constructResponse(1,null,talkingTypeService.getUserSkill(userId));
    }
    /**
     * @author wzh
     * @return json
     * @throws Exception 
     */
    @RequestMapping("TakingTypedetail")
    @ResponseBody
    public JSONObject TakingTypedetail() throws Exception{
    	Map<String,Object> map=new HashMap();
		try{
			map=talkingTypeService.TakingTypedetail();
		 }catch(Exception e){
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR, "数据库错误",null);
			}
        return CommonUtil.constructResponse(1,null,map);
    }

    @RequestMapping(value="/detail",method= RequestMethod.GET)
    @ResponseBody
    public JSONObject takingtypedetail(){
        //  TODO effine  代码出现错误暂时注释
        //JSONObject jo=takingtypeservice.TakingTypedetail();
        return null;
    }

	 /*
     *话题社部分
     */
    /*
     * 得到所有一级领域
     * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
     */
    @RequestMapping("getAllFirstTalkType")
    @ResponseBody
    public JSONObject getAllFirstTalkType(){
        return CommonUtil.constructHtmlResponse(1, null, talkingTypeService.selectAllOneTimesTalkType());
    }

    /*
     * 得到所用二级领域，关注与否用sesion中Id来判断,默认
     * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
     */
    @RequestMapping("getAllSecondTalkType")
    @ResponseBody
    public JSONObject getAllSecondTalkType(HttpSession session,Integer page){
       User user=(User)session.getAttribute("user");
        if(page==null||page<=0){
            page=1;
        }
        if(user==null){
            return CommonUtil.constructResponse(1,null, talkingTypeService.selectAllSecondTalkType(-1,page));
        }else {

            return CommonUtil.constructResponse(1, null, talkingTypeService.selectAllSecondTalkType(user.getUserId(),page));
        }
    }

    /*
    * 根据一级领域查出二级领域内容
    *
    * */
    @RequestMapping("getSecondTalkType")
    @ResponseBody
    public JSONObject getSecondTalkType(HttpServletRequest request,@RequestParam(required = false)Integer page, @RequestParam(required = false)Integer parent){
        User user=(User)request.getAttribute("user");
        if(page==null){
            page=1;
        }
        if(parent==null){
            parent=2;
        }
       if(user==null){
            return CommonUtil.constructResponse(1,null, talkingTypeService.selectSecondTalkType(-1,page,parent));
        }else {
            return CommonUtil.constructResponse(1, null, talkingTypeService.selectSecondTalkType(user.getUserId(),page,parent));
        }
    }
    /*
     yangchun 得到一级领域下拉菜单
     */
    @RequestMapping("getFirstTalkingType")
    @ResponseBody
    public JSONObject getFirstTalkingType(){
        List<HashMap> list=talkingTypeService.getFirstTalkingType();
        return CommonUtil.constructResponse(1,null,list);
    }
    /*
     yangchun 通过一级领域id 得到二级领域下拉菜单
     */
    @RequestMapping("getSecondTalkingType")
    @ResponseBody
    public JSONObject getSecondTalkingType(int talkingTypeId){
        List<HashMap> list=talkingTypeService.getSecondTalkingType(talkingTypeId);
        return CommonUtil.constructResponse(1,null,list);
    }


}
