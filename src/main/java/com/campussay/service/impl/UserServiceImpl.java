/**
 * @author effine
 * @Date 2016年1月10日  下午8:51:35
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpSession;

import com.campussay.model.Follow;
import com.campussay.model.Skill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.campussay.dao.ApplayToCashRecodeDao;
import com.campussay.dao.UserDao;
import com.campussay.model.ApplayToCashRecode;
import com.campussay.model.User;
import com.campussay.service.UserService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;

import cn.util.MD5;

@Service
@Transactional(rollbackFor=Exception.class)
public class UserServiceImpl implements UserService {

	/**
	 * 申请提现中
	 */
	private static final int GETCASH_STATUS_REQUREING = 1;
	

	@Autowired
	private UserDao userDao;
	@Autowired
	private ApplayToCashRecodeDao applayToCashRecodeDao;

	@Override
	public User getUserByAccount(Integer loginType, String account) {
		// TODO unimplements method stub
		if (loginType == 0) {
			return userDao.getUserByMail(account);
		} else if (loginType == 1) {
			return userDao.getUserByPhone(account);
		}
		return null;
	}

	@Override
	public int checkMailorPhone(int registerType, String account) {
		// TODO unimplements method stub
		if (registerType == 0) {
			return userDao.checkMail(account);
		} else if (registerType == 1) {
			return userDao.checkPhone(account);
		}
		return -1;
	}

	@Override
	public int addUser(int registerType, User user) {
		// TODO unimplements method stub
		try {
			if (registerType == 0) {
                return userDao.addUserByMail(user);
            } else {
                return userDao.addUserByphone(user);
            }
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public boolean updatePasswordByAccount(int accountType, String account,
										   String password) {
		// TODO unimplements method stub
		try {
			if (accountType == 0) {
                return userDao.updatePasswordByMail(account, password) == 1;
            } else if (accountType == 1) {
                return userDao.updatePasswordByPhone(account, password) == 1;
            }
		} catch (Exception e) {
			throw e;
		}
		return false;
	}

	@Override
	public boolean updatePasswordByUserId(int userId, String oldPassword,
										  String newPassword) {
		// TODO unimplements method stub
		String oldPasswd=MD5.MD5Encode(oldPassword, "utf-8");
		return userDao.updatePasswordByUserId(userId, oldPasswd, newPassword) == 1;
	}

	@Override
	public Map<String,Object> getUserCheckResult(int id){

		return userDao.getUserCheckResult(id);
	}
	@Override
	public HashMap<String, Object> getBasicInfoByUserId(int userId) {
		// TODO unimplements method stub
		HashMap<String, Object> result=new HashMap<String,Object>();
		List<HashMap<String,Object>> followAreas=userDao.getUserFollowArea(userId);
		result=userDao.getBasicInfoByUserId(userId);
		result.put("followareas",followAreas);
		return result;
	}
	
	

	@Override
	public HashMap<String,Object> getAllUserInfoByUserId(int id)
	{

		try {
			return userDao.getAllUserInfoByUserId(id);
		} catch (Exception e) {
			//捕获异常然后抛出到Controller
			throw e;
		}
	}

	/**
	 * 获取用户保存的关注领域
	 */
	@Override
	public List<HashMap<String,Object>> getUserFollowArea(int id)
	{
		try {
			return userDao.getUserFollowArea(id);
		} catch (Exception e) {
			//捕获异常然后抛出到Controller
			throw e;
		}
	}

	/**
	 * 获取用户保存的擅长领域
	 */
	@Override
	public List<HashMap<String,Object>> getUserSkillArea(int id)
	{
		return userDao.getUserSkillArea(id);
	}


	@Override
	public HashMap<String, Integer> getAttentionCount(int userId) {
		// TODO unimplements method stub
		HashMap<String, Integer> counts = null;
		try {
			counts = new HashMap<String, Integer>();
		} catch (Exception e) {
			throw e;
		}
		counts.put("fansCount", userDao.getFansCount(userId));
		counts.put("attentionCount",userDao.getAttentionCount(userId));
		return counts;
	}

	@Override
	public List<HashMap<String, Object>> getAttentionList(int userId,
														  int listType, Integer page) {
		// TODO unimplements method stub
		if (page == null || page < 1) {
			page = 1;
		}
		if (listType != 0 && listType != 1)   //过滤掉错误查询
			return null;
		return userDao.getAttentionList(userId, listType, (page - 1) * 10, 10);
	}

	@Override
	public int attentionUser(int userId, int beAttentionUserId,int attention) {
		// TODO unimplements method stub
		if (attention == 1){
			int count;
			try {
				count = userDao.attentionUser(userId,beAttentionUserId);
			} catch (Exception e) {
				throw e;
			}
			return count;
		}else if (attention == 0){
			return userDao.unAttentionUser(userId,beAttentionUserId);
		}
		return 0;
	}

	@Override
	public int userRelation(int userId1, int userId2) {
		int relation = 0;
		relation += userDao.userRelation(userId1, userId2);
		if (relation == 0) {
			return 0;
		} else {
			return relation + userDao.userRelation(userId2, userId1);
		}
	}

	@Override
	public JSONObject getAllFirstArea()
	{
		List<HashMap<String,Objects>> lists= null;
		try {
			lists = userDao.getAllFirstArea();
		} catch (Exception e) {
			throw e;
		}
		int code=0;
		String result="没有一级领域";
		if(lists.size()!=0) {
			code=1;
			result="success";
		}
		return CommonUtil.constructHtmlResponse(code,result,lists);
	}

	@Override
	public JSONObject getAllSecondArea(int id)
	{
		List<HashMap<String,Objects>> lists=null;
		try {
			lists=userDao.getAllSecondArea(id);
		} catch (Exception e) {
			throw e;
		}
		int code=0;
		String result="没有二级领域";
		if(lists.size()!=0) {
			code=1;
			result="success";
		}
		return CommonUtil.constructHtmlResponse(code,result,lists);
	}

	public JSONObject userUpdateBasic(int userId,String data){

		//各种领域操作的结果状态，-1状态为系统问题，1为修改成功，0表示没有修改值
		int addfollowresult=0;
		int delefollowresult=0;
		int addskillresult=0;
		int deleskillresult=0;

		//获取前台传来的字符
		JSONObject jo=JSONObject.parseObject(data);
		JSONObject u=jo.getJSONObject("user");
		int update=userDao.updateUserInfo(u);


		//操作用户的关注领域
		String[] addFollowArray=(jo.getString("addFollow").substring(1,jo.getString("addFollow").length()-1)).split(",");
		String[] deleFollowArray=(jo.getString("deleFollow").substring(1,jo.getString("deleFollow").length()-1)).split(",");

		if(!addFollowArray[0].equals("")) {
			List<Follow>lists=new ArrayList<Follow>();
			//添加用户的关注领域
			for (int i = 0; i < addFollowArray.length; i++) {
				Follow f=new Follow();
				f.setUid(userId);
				f.setFieldId(Integer.parseInt(addFollowArray[i]));
				lists.add(f);
			}
			if(userDao.setUserFollowArea(lists)!=0){
				addfollowresult=1;
			}else{
				delefollowresult=-1;
			}
		}
		if(!deleFollowArray[0].equals("")) {
			//删除用户的关注领域
			if(userDao.deleUserFollowArea(userId,deleFollowArray)!=0){
				delefollowresult=1;
			}else {
				deleskillresult=-1;
			}
		}
		/**
		 * 操作用户的擅长领域
		 */
		String[] addSkillArray=(jo.getString("addSkill").substring(1,jo.getString("addSkill").length()-1)).split(",");
		String[] deleSkillArray=(jo.getString("deleSkill").substring(1,jo.getString("deleSkill").length()-1)).split(",");

		if(!addSkillArray[0].equals("")) {
			List<Skill> lists=new ArrayList<Skill>();
			//添加用户的擅长领域
			for (int i = 0; i < addSkillArray.length; i++) {
				Skill s=new Skill();
				s.setSkillUser(userId);
				s.setSkillTalkingType(Integer.parseInt(addSkillArray[i]));
				lists.add(s);
			}
			if(userDao.setUserSkillArea(lists)!=0){
				addfollowresult=1;
			}else{
				deleskillresult=-1;
			}
		}

		if(!deleSkillArray[0].equals("")) {

			//删除用户的擅长领域
			if (userDao.deleUserSkillArea(userId,deleSkillArray) != 0) {
				deleskillresult=1;
			}else {
				deleskillresult=-1;
			}
		}


		//判断所有的结果状态，给出结论
		try {
			if(addfollowresult==-1||addskillresult==-1||deleskillresult==-1||delefollowresult==-1)
			{
				return CommonUtil.constructResponse(0,"系统错误",null);
			}
			if(addfollowresult==0&&addskillresult==0&&deleskillresult==0&&delefollowresult==0&&update==0)
				return CommonUtil.constructResponse(0,"没有值被修改",null);
			else
				return CommonUtil.constructResponse(1,"操作成功",null);
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public boolean setUserPayAccountInfo(int userId, String userPayAccount, String userPayName) {
		return userDao.setUserPayAccountInfo(userId,userPayAccount,userPayName) == 1;
	}

	@Override
	public void IncreTalkingCount(int userId) {
		userDao.IncreTalkingCount(userId);
	}

	@Override
	public void reducTalkingCount(int userId) {
		userDao.reducTalkingCount(userId);
	}

	@Override
	public int getUserTalkingCount(int userId) {
		return userDao.getUserTalkingCount(userId);
	}

	@Override
	public boolean updateUserInfo(JSONObject jsonObject) {
		return userDao.updateUserInfo(jsonObject)==1;
	}

	public Map<String,Object> getMyConcernMsg(int userId,int begin,int end){
		Map<String,Object> resultmap=new HashMap<String,Object>();
		List<Map<String,Object>> userMap=userDao.getMyConcernMsg(userId,begin,end);
		int num=userDao.getMyConcernMsgNum(userId);
		if(num>0) {
			resultmap.put("rows",num);
			for (Map<String, Object> map : userMap) {
				List<HashMap<String, Object>> skillmap = userDao.getUserSkillArea((int) map.get("user_id"));
				List<HashMap<String, Object>> followmap = userDao.getUserFollowArea((int) map.get("user_id"));
				map.put("skillArea", skillmap);
				map.put("followmap", followmap);
				if(userDao.getEachOtherAttention((int)map.get("user_id"),userId)>0){
					map.put("eachOther",1);
				}else{
					map.put("eachOther",0);
				}
			}
			resultmap.put("list",userMap);
		}
		return resultmap;
	}

	public Map<String,Object> getMyFansMsg(int userId,int begin,int end){
		Map<String,Object> resultmap=new HashMap<String,Object>();
		List<Map<String,Object>> userMap=userDao.getMyFansMsg(userId,begin,end);
		int num=userDao.getMyFansMsgNum(userId);
		if(num>0) {
			resultmap.put("rows",num);
			for (Map<String, Object> map : userMap) {
				List<HashMap<String, Object>> skillmap = userDao.getUserSkillArea((int) map.get("user_id"));
				List<HashMap<String, Object>> followmap = userDao.getUserFollowArea((int) map.get("user_id"));
				map.put("skillArea", skillmap);
				map.put("followmap", followmap);
				if(userDao.getEachOtherAttention(userId,(int)map.get("user_id"))>0){
					map.put("eachOther",1);
				}else{
					map.put("eachOther",2);
				}
			}
			resultmap.put("list",userMap);
		}
		return resultmap;
	}

	/**
	 * 申请提现
	 */
	@Transactional(rollbackFor=Exception.class)
	@Override
	public JSONObject applyToCash(String userId, Double cash,HttpSession session) throws Exception {
		
		try {
			    Map userBalance =(Map)	userDao.getUserBalance(userId);
			
				if(userBalance==null){
					return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"系统错误",null);
				}else{
				    Double d_user_balance=(Double)userBalance.get("user_balance");
				    Integer I_user_getcash_status=(Integer)userBalance.get("user_getcash_status");
					    
				    if(I_user_getcash_status == null){
						return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"系统错误",null);
					}
					if(d_user_balance == null){
						return  CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,"系统错误",null);
					}

					if(I_user_getcash_status.intValue()==0){
						
						if(d_user_balance.doubleValue()<cash.doubleValue()){
							return  CommonUtil.constructResponse(EnumUtil.TOO_MUCH_CASH,"提取金额不能大于余额",null);
						
						}else{
							userDao.applyToCash(cash, GETCASH_STATUS_REQUREING, userId);
							ApplayToCashRecode applayToCashRecode = new ApplayToCashRecode();
							applayToCashRecode.setApplayToCashRecodeContent(cash+"");
							applayToCashRecode.setApplayToCashRecodeId(CommonUtil.GUID().replace("_", ""));
							applayToCashRecode.setApplayToCashRecodeTime(CommonUtil.getSystemTime());
							applayToCashRecode.setApplayToCashRecodeUser(userId);
							applayToCashRecodeDao.addRecode(applayToCashRecode);
							User user= (User) session.getAttribute("user");
							if(user==null){
								return  CommonUtil.constructResponse(EnumUtil.NOT_LOGIN,"请登录",null);
							}
						    //将更新后到余额更新到session中开始
							BigDecimal bd_d_user_balance= new BigDecimal(d_user_balance);
							BigDecimal bd_cash= new BigDecimal(cash);
							
						    //double userLeftBalance =	bd_d_user_balance.subtract(bd_cash).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
						    user.setUserBalance(bd_d_user_balance.doubleValue());
							session.setAttribute("user", user);
							//将更新后到余额更新到session中结束
							return CommonUtil.constructResponse(EnumUtil.OK,"成功",null);
						}
					}else{
						return  CommonUtil.constructResponse(EnumUtil.HAD_APPLY_TO_CASH,"已经申请提现，请耐心等待",null);
					}
					
				
				}

		} catch (Exception e) {
			throw e;
		}
	}
	
	
	//得到我的关注量和粉丝量
	public HashMap getMyFansAndConcernNum(int userId) throws Exception{
		HashMap mp=new HashMap();
		int fansNum=0;
		int concernNum=0;
		try{
			fansNum=userDao.getFansCount(userId);
			concernNum=userDao.getAttentionCount(userId);
		}catch(Exception e){
			throw new Exception("数据库操作异常");
		}
		mp.put("fansNum", fansNum);
		mp.put("concernNum", concernNum);
		return mp;
	}

	@Override
	public Map apkVersion() {
		return userDao.apkVersion();
	}

	@Override
	public HashMap getUserBalance(String userId) throws Exception {
		try {
		  return 	(HashMap) userDao.getUserBalance(userId);
		} catch (Exception e) {
			// TODO: handle exception
			throw e;
		}
	
	}

	/**
	 *  根据用户id查询用户密码信息
	 */
	@Override
	public Map selectUserPasswordById(String userId) throws Exception {
		   try {
			return    userDao.selectUserPasswordById(userId);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
		
	}

	/**
	 * 展示出用户有几条未读信息
	 */
	@Override
	public int showNotReadInfoCount(int user_id) throws Exception {
		
		return userDao.showNotReadInfoCount(user_id);
	}

//	@Override
//	public int insertuserphoto(String user_photo, int user_id) {
//		return userDao.insertuserphoto(user_photo, user_id);
//
//	}

	@Override
	public int updateuserphoto(String user_photo, int user_id) {
		// TODO Auto-generated method stub
		return userDao.updateuserphoto(user_photo, user_id);
	}

	@Override
	public int updateInfo_systemReaded(int user_id) {

		return userDao.updateInfo_systemReaded(user_id);
	}

	@Override
	public int deleteInfo_systemReaded(int user_id) {

		return userDao.deleteInfo_systemReaded(user_id);
	}

	@Override
	public String istruepassword(int user_id, String password) {
		// TODO Auto-generated method stub
		String name=null;
		try{
			name=userDao.istruepassword(user_id, password);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			throw e;
		}
		return name;
	}

}

