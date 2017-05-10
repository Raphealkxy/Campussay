package com.campussay.dao;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Follow;
import com.campussay.model.Skill;
import com.campussay.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

import java.util.Map;
import java.util.Objects;

@Repository
public interface UserDao {

	/**
	 * 通过邮箱获取用户 wangwenxiang
	 */
	User getUserByMail(String mail);

	/**
	 * 通过手机获取用户 wangwenxiang
	 */
	User getUserByPhone(String mail);

	/**
	 * 根据邮箱获取用户数目
	 * 
	 * @param user_mail
	 *            邮箱
	 * @return 数量
	 */
	int checkMail(String user_mail);

	/**
	 * 根据邮箱获取用户数目
	 * 
	 * @param user_phone
	 *            电话
	 * @return 数量
	 */
	int checkPhone(String user_phone);

	/**
	 * 添加邮箱注册用户
	 * 
	 * @param user
	 *            用户信息
	 * @return 修改行数
	 */
	int addUserByMail(User user);

	/**
	 * 添加手机注册用户
	 * 
	 * @param user
	 *            用户信息
	 * @return 修改行数
	 */
	int addUserByphone(User user);

	/**
	 * 通过邮箱修改密码
	 * 
	 * @param password
	 *            新密码
	 * @param mail
	 *            邮箱账号
	 * @return 修改行数
	 */
	int updatePasswordByMail(String mail, String password);

	/**
	 * 通过手机修改密码
	 * 
	 * @param password
	 *            新密码
	 * @param phone
	 *            邮箱账号
	 * @return 修改行数
	 */
	int updatePasswordByPhone(String phone, String password);

	/**
	 * 通过userId和原密码修改密码
	 * 
	 * @param userId
	 *            用户id
	 * @param oldPassword
	 *            原密码
	 * @param newPassword
	 *            新密码
	 * @return 修改行数
	 */
	int updatePasswordByUserId(int userId, String oldPassword,
			String newPassword);

	/**
	 * 根据userId获取用户的基本信息
	 * 
	 * @param userId
	 *            用户id
	 * @return 用户信息map
	 */
	HashMap<String, Object> getBasicInfoByUserId(int userId);

	/**
	 * 根据userId获取用户的所有基本信息
	 */
	HashMap<String,Object>getAllUserInfoByUserId(int id);

	/**
	 * 根据userId获取用户的关注领域
	 */
	List<HashMap<String,Object>> getUserFollowArea(int id);
	/**
	 * 根据userId获取用户的擅长领域
	 */
	List<HashMap<String,Object>> getUserSkillArea(int id);
	/**
	 * 获取用户是否已经认证
	 */
	Map<String,Object> getUserCheckResult(int id);
	/**
	 * 根据userId获取用户的关注数量
	 * 
	 * @param userId
	 *            用户id
	 * @return 数量
	 */
	int getAttentionCount(int userId);

	/**
	 * 根据userId获取用户的粉丝数量
	 * 
	 * @param userId
	 *            用户id
	 * @return 数量
	 */
	int getFansCount(int userId);

	/**
	 * 获取所有的一级关注领域
	 */
	List<HashMap<String,Objects>> getAllFirstArea();

	/**
	 * 获取所有的二级关注领域
	 */
	List<HashMap<String, Objects>>getAllSecondArea(int id);

	/**
	 * 添加用户设置的关注领域
	 * @param
	 */
	int setUserFollowArea(List<Follow> lists);

	/**
	 * 添加用户设置的擅长领域
	 * @param
	 */
	int setUserSkillArea(List<Skill> lists);

	/**
	 * 删除用户设置的擅长领域
	 */
	int deleUserSkillArea(int userId,@Param("del")String a[]);

	/**
	 * 删除用户设置的关注领域
	 */
	int deleUserFollowArea(int userId,@Param("del")String a[]);

	/**
	 * 获取用户的粉丝详情列或者关注详情列
	 * 
	 * @param userId
	 *            用户id
	 * @return 用户信息map
	 */
	List<HashMap<String, Object>> getAttentionList(int userId,
			@Param("listType") int listType, int first, int size);

	/**
	 * 用户关注其他用户
	 * 
	 * @param userId
	 *            关注者
	 * @param beAttentionUserId
	 *            被关注者
	 * @return 修改数据库条数
	 */
	int attentionUser(int userId, int beAttentionUserId);

	/**
	 * 获取两个用户之间的关系
	 * @param userId1 主用户
	 * @param userId2 次用户
	 */
	int userRelation(int userId1, int userId2);


	/*
	 *话题社的用户信息
	 */
	HashMap<String,Object> getUserInfo(int userId);


	/**
	 * 修改用户个人设置信息
	 * @param jsonObject(所修改的信息封装成实体类)
	 */
	int updateUserInfo(JSONObject jsonObject);

	/**
	 * 设置用户的支付宝收款账户
	 */
	int setUserPayAccountInfo(int userId,String userPayAccount,String userPayName);

	int unAttentionUser(int userId,int beAttentionUser);

	int getAttention(int ownId,int userId);

	/**
	 * 自增用户表的talkingCount字段
	 * @param userId
     */
	void IncreTalkingCount(int userId);

	/**
	 * 自减用户表的talkingCount字段
	 */
	void reducTalkingCount(int userId);

	int getUserTalkingCount(int userId);

	int getMyConcernMsgNum(int userId);
    int getMyFansMsgNum(int userId);

	List<Map<String,Object>> getMyConcernMsg(int userId,int begin,int end);

	List<Map<String,Object>> getMyFansMsg(int userId,int begin,int end);

	int getEachOtherAttention(int ownerId,int userId);
	
	
	
	public void updateUserBalance(double cash,String userId) ;
	
	public Map getUserBalance(String userId);
	public void applyToCash(Double cash,int getcashStatus,String userId);

	/*
	* 移动端版本更新接口
	* */
	public Map apkVersion();
	
	/**
	 * 
	 * @Title: selectUserPasswordById 
	 * @Description: 根据用户ID查询用户密码
	 * @param userId
	 * @return
	 * @return: Map
	 */
	public Map selectUserPasswordById(String userId);
	
	public int showNotReadInfoCount(int information_user);
	

	public int updateuserphoto(String user_photo,int user_id);
	
	public int updateInfo_systemReaded(int user_id);
	 
	public int deleteInfo_systemReaded(int user_id);
	
	/*
	* 验证密码是否正确
	* */
	public String istruepassword(int user_id,String password);
}
