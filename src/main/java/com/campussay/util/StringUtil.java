package com.campussay.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangwenxiang on 15-12-11.
 */
public class StringUtil {
    /**
     * 验证邮箱格式
     * @param email
     * @return
     */
    public static boolean checkEmail(String email){
        boolean flag = false;
        try{
            String check = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(email);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    /**
     * 验证手机格式
     * @param phoneNumber
     * @return
     */
    public static boolean checkPhoneNumber(String phoneNumber){
        boolean flag = false;
        try{
            String check = "^(13[0-9]|14[0-9]|15[0-9]|18[0-9])\\d{8}$";
            Pattern regex = Pattern.compile(check);
            Matcher matcher = regex.matcher(phoneNumber);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    /**
     * 产生n位的随机数字验证码
     * @param length 数字长度
     * @return 返回随机验证码
     */
    public static String getRandomCode(int length){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<length;i++){
            sb.append((int)(Math.random()*10));
        }
        return sb.toString();
    }
    
    /**
     * 
     * @Title: isEmpty 
     * @Description: 检测字符串是否为null或去掉两端空格后是空串
     * @param checkStr
     * @return: boolean 如果是空 则返回true  否则返回false;
     */
    public static boolean isEmpty(String checkStr){
    	
    	if (checkStr == null || "".equals(checkStr.trim())){
    		return true;
    	}else{
    		return false;
    	}
    }
}
