/**
 * @author effine
 * @Date 2016年1月10日  下午8:51:26
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;


public interface UserService {
	/**
	 *
	 * @param loginType
	 * @param account
	 * @return
	 */
	public abstract User getUserByAccount(Integer loginType, String account);

	/**
	 * 根据手机或者邮箱获取用户数
	 * 
	 * @param registerType
	 *            类型
	 * @param account
	 *            账户
	 * @return 数目
	 */
	public abstract int checkMailorPhone(int registerType, String account);

	public abstract int addUser(int registerType, User user);

	/**
	 * 根据账号类型修改密码
	 * 
	 * @param accountType
	 *            账号类型
	 * @param account
	 *            账号
	 * @param password
	 *            新密码
	 * @return 是否修改成功
	 */
	public abstract boolean updatePasswordByAccount(int accountType,
			String account, String password);

	/**
	 * 根据userId和原密码修改密码
	 * 
	 * @param userId
	 *            用户id
	 * @param oldPassword
	 *            原密码
	 * @param newPassword
	 *            新密码
	 * @return 是否修改成功
	 */
	public abstract boolean updatePasswordByUserId(int userId,
			String oldPassword, String newPassword);

	/**
	 * 个人设置中获取用户的基本信息
	 * 
	 * @param userId
	 *            用户id
	 * @return 结果
	 */
	public abstract HashMap<String, Object> getBasicInfoByUserId(int userId);

	/**
	 * 个人设置中获取用户的信息，包括关注领域和擅长领域
	 */
	public abstract  HashMap<String,Object>getAllUserInfoByUserId(int userId);

	/**
	 * 个人设置中修改用户的基本信息
	 * @user 修改的内容封装成类
	 */
	public abstract boolean updateUserInfo(JSONObject jsonObject);

	/**
	 * 查看用户的粉丝数量和关注数量 分开获取
	 * 
	 * @param userId
	 *            用户id
	 * @return 结果
	 */
	public abstract HashMap<String, Integer> getAttentionCount(int userId);

	/**
	 * 获取所有的一级关注领域
	 */
	public abstract JSONObject getAllFirstArea();

	/**
	 * 获取所有的二级关注领域
	 */
	public abstract JSONObject getAllSecondArea(int id);

	/**
	 * 保存用户对领域的各种操作以及对用户基本信息的修改
	 */
	public abstract JSONObject userUpdateBasic(int userId,String data);
	/**
	 * 获取用户的粉丝详情列或者关注详情列
	 * 
	 * @return 结果
	 */
	public abstract List<HashMap<String, Object>> getAttentionList(int userId,
			int listType, Integer page);

	/**
	 * 获取用户是否已经被认证
	 */
	public abstract Map<String,Object> getUserCheckResult(int userId);
	/**
	 * 获取用户保存的关注领域
	 */
	public abstract  List<HashMap<String,Object>> getUserFollowArea(int id);

	/**
	 * 获取用户保存的擅长领域
	 */
	public abstract List<HashMap<String,Object>> getUserSkillArea(int id);

	/**
	 * 用户关注其他用户
	 * 
	 * @param userId
	 *            关注者
	 * @param beAttentionUserId
	 *            被关注者
	 * @return 修改数据库条数
	 */
	public abstract int attentionUser(int userId, int beAttentionUserId,int attention);

	/**
	 * 获取两个用户之间的关系
	 * @param userId1 主用户
	 * @param userId2 次用户
	 *
	 * 0-主用户未关注次用户
	 * 1-主用户关注次用户
	 * 2-互相关注
	 */
	public abstract int userRelation(int userId1, int userId2);


	boolean setUserPayAccountInfo(int userId,String userPayAccount, String userPayName);

	void IncreTalkingCount(int userId);

	void reducTalkingCount(int userId);

	int getUserTalkingCount(int userId);

	public Map<String,Object> getMyConcernMsg(int userId,int begin,int end);

	public Map<String,Object> getMyFansMsg(int userId,int begin,int end);
	/**
	 * 
	 * @Title: applyToCash 
	 * @Description: 用户提现
	 * @param userId
	 * @param cash
	 * @return: void
	 * @throws Exception 
	 */
	public JSONObject applyToCash(String userId,Double cash,HttpSession session) throws Exception;
	
	public HashMap getMyFansAndConcernNum(int userId) throws Exception;


	//移动端接口，版本更新
	Map apkVersion();
	
	
	public HashMap getUserBalance(String userId) throws Exception;
	/**
	 * 
	 * @Title: selectUserPasswordById 
	 * @Description: 根据用户ID查询用户密码信息
	 * @param userId
	 * @return
	 * @throws Exception
	 * @return: Map
	 */
	public Map selectUserPasswordById(String userId) throws Exception;
	
	/**
	 * 
	 * @Title: showNotReadInfoCount
	 * @Description: 根据用户ID查询用户密码信息
	 * @param int user_id
	 * @throws Exception
	 * @return: int
	 * @author JavaGR_ais
	 */
	 public int showNotReadInfoCount(int user_id)throws Exception;
	 
	 

	 public abstract int updateuserphoto(String user_photo,int user_id);
	 
	 public int updateInfo_systemReaded(int user_id);
	 
	 public int deleteInfo_systemReaded(int user_id);
	 
	 /*
		* 验证密码是否正确
		* */
	 public abstract String istruepassword(int user_id,String password);

}
