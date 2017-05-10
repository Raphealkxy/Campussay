package com.campussay.controller;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.User;
import com.campussay.service.StudentCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/student")
public class StudentCheckController {

    @Autowired
    private StudentCheckService studentCheckService;

    /**
     * 根据用户名获得所有订单
     * http://localhost:8080/student/getStudentCheck
     * {"msg":"succeed","code":1,"data":{"student_check_picture":"1","campus_name":"重庆邮电大学","student_check_academe":"1"}}
     * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
     */
    @RequestMapping("/getStudentCheck")
    @ResponseBody
    public JSONObject getOrderByUserID(HttpServletRequest request) {
        User user = (User)request.getAttribute("user");
        if(user != null) {
            int userID = user.getUserId();
            return studentCheckService.getStudentCheck(userID);
        } else {
            // TODO 未登录错误处理
            return null;
        }

    }

}
