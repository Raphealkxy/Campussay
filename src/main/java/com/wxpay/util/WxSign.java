package com.wxpay.util;

import cn.util.MD5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * 
 * @功能：微信支付取得签名
 * @作者：dinghongxing
 * @文件名：WxSign.java 
 * @包名：com.wxpay.util 
 * @项目名：zhuanquanquan
 * @部门：伏守科技项目开发部
 * @日期：2015年12月28日 下午1:38:43 
 * @版本：V1.0
 */
public class WxSign {
	/**
	 * 
	 * @描述：生成微信支付签名
	 * @作者:丁洪星 
	 * @部门：伏守科技项目开发部
	 * @日期： 2015年11月4日 下午1:41:39 
	 * @版本： V1.0 
	 * @param map
	 * @param key
	 */
	public static String getSign(Map<String,Object> map,String key){
		ArrayList<String> list = new ArrayList<String>();
		
        for(Map.Entry<String,Object> entry:map.entrySet()){
            if(entry.getValue()!=""){
            	list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        result = MD5.MD5Encode(result,"").toUpperCase();
        return result;
    }
}
