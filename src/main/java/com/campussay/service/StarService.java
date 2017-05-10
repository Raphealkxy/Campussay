/**
 * @author effine
 * @Date 2016年1月10日  下午8:35:33
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service;

import java.util.List;
import java.util.Map;

public interface StarService {

	/**
	 * 返回牛人集合
	 *
	 * @return
	 * @throws Exception 
	 */
	public abstract List<Map<String,Object>>  Star_list(String campus_name) throws Exception;
}
