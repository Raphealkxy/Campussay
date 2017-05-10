package com.campussay.dao;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Talking;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface TalkingDao {
	
	public int updateout(int talking_id);
	/**
	 * 获取banner的首页图片
	 * @return
	 */
	public Map<String,Object> getBannerPicture();
	/**
	 * wzh
	 * 课程详情展示
	 */
	public Map<String,Object> getTaking(int taking_id);
	/**
	 * wzh
	 * 返回用户id
	 */
	public Map<String,Object> userisbuy(int taking_id,int user_id);
	/**
	 * wzh
	 * 返回用户id
	 */
	public int getUserId(int taking_id);
	/**
	 * wzh
	 * 返回用户姓名
	 * @param user_Id 用户id
	 */
	public String getUserName(int user_id);
	/**
	 * wzh
	 * 返回类型名字
	 * @param talkingtypeid 说说类型ID
	 */
	public String gettalkingtypename(int talkingtypeid);
	/**
	 * wzh
	 * 课程详情评价展示
	 * @param userId 用户id
	 * @param page 评价页数
	 */
	public List<Map<String,Object>> getTakingComment(int taking_id,int page);
	/**
	 * wzh
	 * 课程详情用户头像
	 * @param user_id 用户id
	 */
	public Object getUserPhoto(int user_id);
	/**
	 * wzh
	 * 课程详情说说标题
	 * @param talking_id 说说id
	 */
	public String getTalkingtitle (int talking_id);
	/**
	 * wzh
	 * 课程总数
	 * @param talking_id 课程id
	 */
	public int CountTalking(int talking_id);
	/**
	 * wzh
	 * 课程详情立即参与
	 * @param userId 用户id
	 *  @param takingId 课程id
	 *  @param taking_price 课程价格
	 */
	public int joinTaking(int taking_id, int user_id, double taking_price);


	/**
	 * wzh
	 * 该说友其他课程
	 * @param talking_user 用户id
	 */
	
	public List<Map<String,Object>> getothertalking(int talking_user);
	
	/**
	 * @author wzh
	 * taking详情页
	 * 获取user_id
	 * @param TalkingId Talkingid
	 */
	public int getUserID(int TalkingId);
	
	/**
	 * @author wzh
	 * taking详情页
	 * 说有介绍
	 * @param user_id 用户id
	 */
	public Map<String,Object> getUserDetails(int user_id);
	
	/**
	 * @author wzh
	 * taking发布页
	 * 获取taking类别
	 *  @param taking_type_parent 父节点
	 */
	public List<Map<String,Object>> getTalkingType(int taking_type_parent);
	
	/**
	 * @author wzh
	 * taking发布页
	 * 获取用户通信信息
	 * @param user_id 用户id
	 */
	public Map<String,Object> getUserContact(int user_id);
	/**
	 * @author wzh
	 * 总的平均
	 * @param 
	 */
	public String avgcomment(int user_id);
	
	/**
	 * @author wzh
	 * taking发布页
	 * 获取获取第一集别takingtype
	 */
	public JSONObject firsttakingType();
	/**
	 * @author wzh
	 * takingcount+1
	 * 
	 */
	public int Addtalkingcount(int user_id);

	/**
	 * @author wzh
	 * taking发布页
	 * 获取获取第一集别takingtype
	 */
	public int CreateNewTalking(Talking t);
	/**
	 * @author wzh
	 * taking发布页
	 * 获取学校名称
	 */
	public String getUserCampusName(Integer user_id);
	/**
	 * @author wzh
	 * taking发布页
	 * 获取学校名称
	 */
	public int getUserCampusId(String campus_name);
	/**
	 * @author wzh
	 * taking发布页
	 * 获取talking编辑详情
	 */
	public Map<String,Object> gettalkingdetails(int talkingid);
	/**
	 * @author wzh
	 * taking发布页
	 * 修改已有talking
	 */
	public int updatetalkingdetails(Talking t);
	
	/**
	 * @author wzh
	 * taking发布页
	 * 精品talking
	 */
	public List<Map<String,Object>> Specialtalking(int talking_campus);
	/**
	 * ld
	 * 获取用户的发布的talking
	 * @param userId 用户id
	 */
	List<HashMap<String,Object>> getUserTalking(int userId, int first,int size,int state);

	/**
	 * ld
	 * 获取用户所发布的talking的数量
	 * @param userId 用户id
	 */
	int getUserTalkingCount(int userId,int state);

	/**
	 * ld
	 * 获取用户删除的talking
	 * @param userId
	 * @param first
	 * @param size
	 * @param state
     * @return
     */
	List<HashMap<String,Object>> getDeleUserTalking(int userId, int first,int size,int state);

	/**
	 * ld
	 * 获取用户删除的talking
	 * @param userId
	 * @param state
	 * @return
	 */
	int getUserDeleTalkingCount(int userId,int state);

	/**
	 * wangwenxiang
	 * 获取他所参与（购买）的talking信息列表
	 * @param userId 用户id
	 */
	List<HashMap<String,Object>> getUserBuyTalking(int userId, int first,int size,int state);

	/**
	 *
	 */
	int getUserBuyTalkingCount(int userId, int state);





	int delTalking(int talkingId);

		/**
		 * 查找出听听堂首页各树节点下面的课程(暂定只返回前8条)
		 * Author:JavaGR_ais
		 */
		
		//查找出t_taking_type表中所有根节点的课程类型数据
		public List<Map<String,Object>> SelectTalkingHomePage();
		//查找出t_taking_type表中所有根节点的课程类型数据
		public List<Map<String,Object>> SelectPushTalkingHomePage();
		
		//根据t_taking_type表根节点查找出它所有下一层的节点
		public List<Map<String,Object>>  SelectNodeByRootNode(@Param("taking_type_parent")List<Integer> taking_type_parent);
		
		//根据t_taking_type表一个根节点查找出它所有下一层的节点
		public List<Map<String,Object>>  SelectNodeByRootNodeOne(@Param("taking_type_parent")int taking_type_parent);
		
		//根据所有的叶子节点查询出所有的课程
		public List<Talking> SelectTalkingByLeafNode(@Param("leafNode") List<Integer> leafNode);
		
		//根据三级结构找出所有叶子节点的taking_type_id
		public List<Map<String, Object>> SelectT_taking_type_idByNode(@Param("taking_type_id") List<Integer> taking_type_id);


		//根据根节点查找t_taking表所有的课程
		public List<Map<String,Object>> SelectT_takingByTaking_RootNode(@Param("taking_rootnode") int taking_rootnode);
		
		//talking_root_type
		public List<Map<String,Object>> SelectT_talkingByTaking_RootNode(@Param("taking_rootnode") int taking_rootnode);
		
		//根据根节点的taking_type_id查找出对应Name

		public String SelectRootNodeNameBytaking_type_id(@Param("taking_type_id") int taking_type_id);
		
		//根据三级结构找出所有的叶子节点
		public List<Map<String, Object>> SelectT_taking_leafNodesByNode(@Param("taking_type_id") List<Integer> taking_type_id);

		//根据三级结构找出所有叶子节点的taking_type_id
		public List<Integer> SelectT_taking_type_idByNode_id(@Param("taking_type_id") int taking_type_id);
		
		//查找出所有城市,city表
		public List<Map<String,Object>> SelectCity();
		
		//查找出所有学校,t_campus表
		public List<Map<String,Object>> SelectCampus();
		
		//查找出所有学校id根据城市id
		public List<Integer> SelectCampus_idByCity_id(@Param("campus_city") int campus_city);
		
		//组合查询课程
		public List<Map<String,Object>> SelectTalkingByCf(@Param("taking_type")int taking_type,@Param("taking_rootnode")int taking_rootnode,@Param("taking_campus")int taking_campus,@Param("city") Object city,@Param("pageCount") int pageCount,@Param("offset") int offset,@Param("sex")int sex,@Param("type") int type);
		
		//根据user_id查出user_name
		public String SelectUser_NameByUser_Id(int user_id);
		
		//根据city_id找出他所属的学校集合
		public List<Map<String,Object>> SelectCampusByCity_id(int campus_city);
		
		//根据city_id找出city_name
		public String SelectCityNameByCity_id(int campus_city);
		
		//找出组合查询所有的size
		public int SelectTalkingsSize(@Param("taking_type")int taking_type,@Param("taking_rootnode")int taking_rootnode,@Param("taking_campus")int taking_campus,@Param("city") Object city,@Param("sex")int sex,@Param("type") int type);

		//
		public int SelectFound_Rows();
		
		//SelectT_taking_leafNodesByNode_id
		public List<Map<String,Object>> SelectT_taking_leafNodesByNode_id(int taking_type_id);
		
		//
		public int SelectSexByUser_id(int user_id);
		
		/**
		 * 
		 * @Title: updateTalkingRemainNum 
		 * @Description: 修改课程生序数量
		 * @param talkingId
		 * @return
		 * @return: int 修改影响行数
		 */
	    public int updateTalkingRemainNum(int talkingId);
	    /**
	     * @author liupengyan
	     * @Title: getTalkingInfoForCreateOrder 
	     * @Description: 得到课程信息 主要用于下单时 确认产品信息
	     * @param talkingId
	     * @return
	     * @return: HashMap<String,Object>
	     */
	    public HashMap<String, Object> getTalkingInfoForCreateOrder(int talkingId);
	    
	    
	    public Map<String,Object> getTakingInfo(int taking_id);
	    
	    public Map<String,Object>getTalkingInfoAndPhoneNUM(int taking_id);
	    
	    /**
	     * 修改课程状态
	     * @Title: updateTalkingStatus 
	     * @Description: TODO
	     * @param talkingStatus
	     * @param talkingID
	     * @throws Exception
	     * @return: void
	     */
	    public void updateTalkingStatus(Integer talkingStatus,Integer talkingID) throws Exception;
	    
	    public int updateTalkingOverdue(int talking_id);
	    
	    public int judgeIsDelTalking(int talking_id);
	    
	    public Map<String,Object> getUserTalkingInfo(int talking_id);
}
