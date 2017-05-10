/**
 * @author effine
 * @Date 2016年1月10日  下午9:07:38
 * @email verphen#gmail.com
 * @site http://www.effine.cn
 */

package com.campussay.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.campussay.dao.TalkingDao;
import com.campussay.dao.TalkingTypeDao;
import com.campussay.dao.UserDao;
import com.campussay.model.Talking;
import com.campussay.service.TalkingService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil.TalkingStatus;


@Service
@Transactional(rollbackFor=Exception.class)
public class TalkingServiceImpl implements TalkingService {

	@Autowired
	private TalkingDao talkingDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private TalkingTypeDao talkingtypedao;

	@Override
	public Map<String,Object> getBannerPicture(){
		// TODO 自动生成的方法存根
		return talkingDao.getBannerPicture();
	}
	/**
	 * wzh
	 * 课程详情展示
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> TalkingShow(int Talking_id) throws Exception {
		//获取taking基本信息
		Map<String,Object> takingclass=new HashMap();
		if(takingclass==null){
			return takingclass;
		}
		try{
			takingclass=talkingDao.getTaking(Talking_id);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}
				//获取第一个class
				if(takingclass==null)
					return null;
					//获取讲课教师信息
				try{
					int user_id=talkingDao.getUserId(Talking_id);
					String user_name=talkingDao.getUserName(user_id);
				}catch(Exception e){
					throw new Exception("数据库错误");
				}
					//处理时间戳转string问题
					
					//将剩余人数转化为已有人数
					takingclass.put("talking_now_persion", (int)takingclass.get("talking_max_persion")-(int)takingclass.get("talking_now_persion"));
					if(Integer.parseInt(takingclass.get("talking_state").toString())!=100){
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
						long str = date.getTime();
						long str1= sdf.parse(takingclass.get("talking_start_time").toString()).getTime();
						if(str>str1){
							try{
								talkingDao.updateout((int)takingclass.get("talking_id"));
								takingclass.put("talking_state", 100);
							}catch(Exception e){
								e.printStackTrace();
								throw new Exception("数据库错误");
							}
						}
						if((int)takingclass.get("talking_is_online")==1){
							takingclass.remove("talking_tool_num");
						}
						
					}
				return takingclass;
	}
	public int userisbuy(int taking_id,int user_id) throws Exception{
		Map<String,Object> map=new HashMap();
		int i = 0;
		try{
			map=talkingDao.userisbuy(taking_id, user_id);
			System.out.println(map);
			if(map==null){
				i=0;
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("数据库错误");
		}
		return i;
	}
	@Override
	public Map<String, Object> TalkingDetails(int Talking_id) {
		// TODO unimplements method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> TalkingClass(int Talking_id) {
		// TODO unimplements method stub
		return null;
	}
	/**
	 * wzh
	 * 课程详情评价展示
	 * @param page 评价页数
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> TalkingComment(int Talking_id, int page) throws Exception {
		page=(page-1)*10;
		List<Map<String,Object>> takingcomment=new ArrayList();
		try{
		takingcomment=talkingDao.getTakingComment(Talking_id,page);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}
		if(takingcomment!=null){
			for(int i=0;i<takingcomment.size();i++){
				//获取用户名和头像
				String user_name=null;
				String user_photo=null;
				String talking_title=null;
				try{
			    user_name=talkingDao.getUserName((Integer)takingcomment.get(i).get("talking_comment_user"));
			    user_photo=(String) talkingDao.getUserPhoto((Integer)takingcomment.get(i).get("talking_comment_user"));
			    talking_title=talkingDao.getTalkingtitle(Talking_id);
				}catch(Exception e){
					throw new Exception("数据库错误");
				}
			    takingcomment.get(i).put("user_name",user_name);
			    takingcomment.get(i).put("user_photo",user_photo);
			    takingcomment.get(i).put("talking_title", talking_title);
			}
		}
		return takingcomment;
	}

	@Override
	public int countComment(int Talking_id) {
		return  talkingDao.CountTalking(Talking_id);
	}
	/**
	 * wzh
	 * 课程详情立即参与
	 * @throws Exception 
	 */
	@Override
	public int joinTalking(int Talking_id, int user_id) throws Exception {
		Map<String, Object> taking=new HashMap();
		int i;
		try{
			taking=talkingDao.getTaking(Talking_id);
			double taking_price=(Double) taking.get("talking_price");
			i=talkingDao.joinTaking(Talking_id,user_id,taking_price);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}
		return i;
		
	}
	/*
	 * @author wzh
	 * taking详情页
	 * 该说友其他课程
	 */
	public List<Map<String,Object>> getothertalking(int talking_user) throws Exception{
		List<Map<String,Object>> list=new ArrayList();
		try{
			list=talkingDao.getothertalking(talking_user);
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("数据库错误");
		}
		if(list!=null){
			for(int i=0;i<list.size();i++){
				list.get(i).put("user_name",talkingDao.getUserName(talking_user));
				list.get(i).put("talking_type_name", talkingDao.gettalkingtypename((int)list.get(i).get("talking_type")));
				list.get(i).put("talking_now_persion", (int)list.get(i).get("talking_max_persion")-(int)list.get(i).get("talking_now_persion"));
				if(Integer.parseInt(list.get(i).get("talking_state").toString())!=100){
					Date date = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					long str = date.getTime();
					long str1= sdf.parse(list.get(i).get("talking_start_time").toString()).getTime();
					if(str>str1){
						try{
							talkingDao.updateout((int)list.get(i).get("talking_id"));
							list.get(i).put("talking_state", 100);
						}catch(Exception e){
							e.printStackTrace();
							throw new Exception("数据库错误");
						}
					}
				}
			}
		}
		return list;
		
	}
	/**
	 * @author wzh
	 * taking发布页
	 * 获取用户通信信息
	 * @param user_id 用户id
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> userContact(int user_id) throws Exception {
		Map<String, Object> map=new HashMap();
		try{
		map=talkingDao.getUserContact(user_id);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}	
		return map;
	}
	/*
	 * @author wzh
	 * taking发布页
	 * 获取获取taking
	 */
	public List<Map<String,Object>> firsttakingType() throws Exception{
		List<Map<String,Object>> firsttalking=new ArrayList();
		try{
		firsttalking=talkingtypedao.talkingType_floor();
		}catch(Exception e){
			throw new Exception("数据库错误");
		}	
		if(firsttalking!=null){
			for(int i=0;i<firsttalking.size();i++){
				firsttalking.get(i).remove("talking_type_picture");
				firsttalking.get(i).remove("talking_type_floor");
				firsttalking.get(i).remove("talking_type_parent");
				firsttalking.get(i).remove("talking_type_is_leaf");
			}
		}
		return firsttalking;
	}
	/**
	 * @author wzh
	 * taking发布页
	 * 获取taking类别
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> TalkingType(int Talking_type_parent) throws Exception {
		List<Map<String,Object>> firsttalking=new ArrayList();
		try{
		firsttalking=talkingDao.getTalkingType(Talking_type_parent);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}	
		return firsttalking;
	}
	/**
	 * @author wzh
	 * taking发布页
	 * 获取taking类别
	 * @throws Exception 
	 */
	@Override
	public int CreateNewTalking(Talking talking) throws Exception {
		//talkingcount+1
		int i,j;
		try{
		i=talking.getTalkingUser();
		talkingDao.Addtalkingcount(i);
		j=talkingDao.CreateNewTalking(talking);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}	
		return j;
	}
	/*
	 * @author wzh
	 * 获取user_id
	 * @param TalkingId talkingid
	 */
	public int getUserID(int TalkingId) throws Exception{
		int i;
		try{
		i=talkingDao.getUserID(TalkingId);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}	
		return i;
	}
	/**
	 * @author wzh
	 * taking详情页
	 * 说有介绍
	 * @param user_id 用户id
	 * @throws Exception 
	 */
	public Map<String,Object> getUserDetails(int user_id) throws Exception{
		Map<String,Object> map=new HashMap();
		 String avg1=null;
		 float avg = 0;
		try{
			map=talkingDao.getUserDetails(user_id);
		    avg1=talkingDao.avgcomment(user_id);
		    if(avg1==null||"".equals(avg1)){
		    	avg=0;
		    }else{
		    	avg=Float.parseFloat(avg1);
		    }
		}catch(Exception e){
			e.printStackTrace();
		}	
		if(Math.round(avg)>avg){
			avg=(float) (Math.floor(avg)+Math.ceil(avg))/2;
		}else{
			avg=Math.round(avg);
		}
		map.put("avg", avg);
		return map;
	}
	/*
	 * @author wzh 获得学校名称
	 */
	public String getUserCampusName(Integer user_id) throws Exception{
		String string;
		try{
		string=talkingDao.getUserCampusName(user_id);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}	
		return string;
	}

	@Override
	public int newTalking(Talking Talking) {
		// TODO unimplements method stub`````````
		return 0;
	}

	@Override
	public int getUserCampusId(String campus_name) throws Exception {
		int i;
		try{
		i=talkingDao.getUserCampusId(campus_name);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}	
		return i;
	}
	
	/*
	 * @author wzh 重新编辑详情
	 */
	public Map<String,Object> gettalkingdetails(int talkingid) throws Exception{
		Map<String,Object> map=new HashMap();
		try{
		map=talkingDao.gettalkingdetails(talkingid);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}
		if(map!=null){
			map.put("talking_root_type_name", talkingDao.gettalkingtypename((int)map.get("talking_root_type")));
			map.put("talking_type_name", talkingDao.gettalkingtypename((int)map.get("talking_type")));
			map.put("talking_now_persion", (int)map.get("talking_max_persion")-(int)map.get("talking_now_persion"));
		}
		return map;
	}
	/**
	 * @author wzh
	 * taking发布页
	 * 修改已有talking
	 * @throws Exception 
	 */
	public int updatetalkingdetails(Talking t) throws Exception{
		int i;
		try{
			i=talkingDao.updatetalkingdetails(t);
			if(i==1){
				talkingDao.CountTalking(t.getTalkingUser());
			}
		}catch(Exception e){
			e.printStackTrace();
			throw new Exception("数据库错误");
		}
		return i;
	}
	/**
	 * @author wzh
	 * taking发布页
	 * 精品talking
	 * @throws Exception 
	 */
	public List<Map<String,Object>> Specialtalking(int talking_campus) throws Exception{
		List<Map<String,Object>> list=new ArrayList();
		try{
		list=talkingDao.Specialtalking(talking_campus);
		}catch(Exception e){
			throw new Exception("数据库错误");
		}
		if(list!=null){
			for(int i=0;i<list.size();i++){
				list.get(i).put("talking_now_persion", (int)list.get(i).get("talking_max_persion")-(int)list.get(i).get("talking_now_persion"));
			}
		}
		return list;
	}

	/**
	 * 返回听听堂首页根节点与其下面的talings
	 * @param
	 * @return
	 * @author JavaGR_ais
	 */
	@Override
	public JSONArray ShowHomePageNode() {
		//JSONObject jsonObject = new JSONObject();
		// 拿到所有根节点
		List<Map<String, Object>> rootNodes = talkingDao
				.SelectPushTalkingHomePage();
		
		JSONArray  rootNodes_array = new JSONArray();
		//利用jsonobject进行第二次封装
		for (int i = 0; i < rootNodes.size(); i++) {
			JSONObject rootNodes_json = new JSONObject();
			Map<String, Object> map = rootNodes.get(i);
			rootNodes_json.put("talking_type_state",map.get("talking_type_state"));	
			rootNodes_json.put("talking_type_picture",map.get("talking_type_picture"));	
			rootNodes_json.put("talking_type_id",map.get("talking_type_id"));	
			rootNodes_json.put("talking_type_name",map.get("talking_type_name"));	
			
			
			
			List<Map<String, Object>> talkings = talkingDao
					.SelectT_talkingByTaking_RootNode((int) map.get("talking_type_id"));
			//JSONArray  talkings_array = new JSONArray();
			
			rootNodes_json.put("talkings",talkings);
			
			rootNodes_array.add(rootNodes_json);
		}
		return rootNodes_array;
	}

	

	/**
	 * 听听堂分类列表,一次性封装好并返回分类,城市,学校。
	 * @param
	 * @return 
	 * @author JavaGR_ais
	 */
	@Override
	public JSONObject ShowTalkingCf() {
		JSONObject jsonObject = null;
		JSONObject jsonObject2 = null;
		JSONObject jsonObject3 = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArray2 = null;
		JSONArray jsonArray3 = new JSONArray();
		List<Map<String, Object>> talking_RootNodes = talkingDao
				.SelectTalkingHomePage();// 分类根节点
		List<Map<String, Object>> talking_Nodes = new ArrayList<Map<String, Object>>();// 定义第二级节点的集合
		//List<Map<String, Object>> talking_LeafNodes = new ArrayList<Map<String, Object>>();// 定义第三级叶子的集合

		// 遍历根节点分类集合
		for (int i = 0; i < talking_RootNodes.size(); i++) {
			jsonObject = new JSONObject();
			jsonArray2 = new JSONArray();
			Map<String, Object> map = talking_RootNodes.get(i);
			jsonObject.put("taking_type_name", map.get("talking_type_name"));
			jsonObject.put("taking_type_id", map.get("talking_type_id"));
			for (String key : map.keySet()) {
				if (key.equals("talking_type_id")) {
					talking_Nodes = talkingDao
							.SelectNodeByRootNodeOne((Integer) map.get(key));// 根据根节点id查找出它的下一层节点集合
					for (int j = 0; j < talking_Nodes.size(); j++) {
						jsonObject2 = new JSONObject();
						Map<String, Object> map1 = talking_Nodes.get(j);
					
							jsonObject2.put("talking_type_id",
									map1.get("talking_type_id"));
							jsonObject2.put("talking_type_name",
									map1.get("talking_type_name"));
						jsonArray2.add(jsonObject2);
					}
					jsonObject.put("LeafNodes", jsonArray2);
				}
			}
			jsonArray.add(jsonObject);

		}
		List<Map<String,Object>> citys =  talkingDao.SelectCity();
		
		jsonObject3.put("classification", jsonArray);
		
		for (int i = 0; i < citys.size(); i++) {
			JSONObject campus_json = new JSONObject();
			Map<String, Object> map = citys.get(i);
			
			List<Map<String,Object>> campus = talkingDao.SelectCampusByCity_id((int) map.get("city_id"));
			campus_json.put("city_name",map.get("city_name"));
			campus_json.put("city_id", map.get("city_id"));
			campus_json.put("campus", campus);
			jsonArray3.add(campus_json);
		}
		jsonObject3.put("city_campus", jsonArray3);
		return jsonObject3;
	}

	/**
	 * 听听堂分类,组合查询课程或者说友。
	 * @param LeafNode_id
	 * @param RootNode_id
	 * @param city_id
	 * @param campus_id
	 * @param pageCount
	 * @param type
	 * @return
	 * @author JavaGR_ais
	 */
	@Override
	public JSONObject ShowTalkingByCfOrCyOrCs(int LeafNode_id, int RootNode_id,
			 int city_id, int campus_id, int pageCount,int sex ,int type) {
		SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JSONObject jsonObject = new JSONObject();
		List<Integer>	 city =null;
		if(city_id!=0){
			city = talkingDao.SelectCampus_idByCity_id(city_id);//城市节点id集合
		}
		List<Map<String, Object>> talkings =	talkingDao.SelectTalkingByCf(LeafNode_id, RootNode_id, campus_id,city,pageCount*12-12,12,sex,type);
		int size = talkingDao.SelectTalkingsSize(LeafNode_id, RootNode_id, campus_id,city,sex,type);
		//组合查询课程,type==0
		if(type == 0){
			//List<Map<String, Object>> talkingeds = new ArrayList<Map<String,Object>>();
			//处理过期课程
			for (int i = 0; i < talkings.size(); i++) {
				Iterator<String> iter = talkings.get(i).keySet().iterator();
				
				while (iter.hasNext()) {
					String key = (String) iter.next();
					if(key.equals("talking_end_time")){
						try {
							if(CommonUtil.compareDate(new Date(),dateFormater.parse(talkings.get(i).get(key).toString()))){
								talkingDao.updateTalkingOverdue((int) talkings.get(i).get("talking_id"));
								//talkings.remove(i);
							}
						} catch (ParseException e) {
							
							e.printStackTrace();
						}
					}
				}
				
			}
			List<Map<String, Object>> talkingeds =	talkingDao.SelectTalkingByCf(LeafNode_id, RootNode_id, campus_id,city,pageCount*12-12,12,sex,type);
			int talkingedssize = talkingDao.SelectTalkingsSize(LeafNode_id, RootNode_id, campus_id,city,sex,type);

			//int size1 = talkingDao.SelectTalkingsSize(LeafNode_id, RootNode_id, campus_id,city,sex,type);
			jsonObject.put("talking", talkingeds);
			jsonObject.put("size",talkingedssize);
			
			return jsonObject;
		}
		//组合查询牛人,type==1
		else{
			List<Map<String, Object>> talkings_user = new ArrayList<Map<String,Object>>(); 
			for(int i = 0; i <talkings.size() ; i++){
				Map<String,Object> map = talkings.get(i);	
				map.put("user_SkillArea",userDao.getUserSkillArea((int) map.get("user_id")));
				//map.put("user_count", talkingDao.getUserTalkingCount((int) map.get("user_id"),10));
				talkings_user.add(map);
			}
			jsonObject.put("talking",talkings_user);
			jsonObject.put("size",size);
			return jsonObject;
		}
		
	}

	@Override   //谢谢大神们，别修改我的代码了，想用自己去写啊，大神们！
	public List<HashMap<String, Object>> getUserTalking(int userId, Integer page,Integer state) {
		// TODO unimplements method stub
		List<HashMap<String, Object>> list=null;
		if (page == null || page<1){
			page = 1;
		}
		if(state==1){
				state=0;   //表示获取用户删除的课程     陆栋写的，这个方法别再修改了，自己要用的话另外写一个
				list=talkingDao.getDeleUserTalking(userId,(page-1)*10,10,state);
			}else{    
				state=0;   //表示获取用户未删除的课程     陆栋写的，这个方法别再修改了，自己要用的话另外写一个
				list=talkingDao.getUserTalking(userId, (page-1)*10, 10, state);
		}
			
		for(int i=0;i<list.size();i++){
			list.get(i).put("talking_now_persion", (int)list.get(i).get("talking_max_persion")-(int)list.get(i).get("talking_now_persion"));
			if(Integer.parseInt(list.get(i).get("talking_state").toString())!=100){
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				long str = date.getTime();
				long str1 = 0;
				try {
					str1 = sdf.parse(list.get(i).get("talking_end_time").toString()).getTime();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(str>str1&&(int)list.get(i).get("talking_state")!=0){
					try{
						talkingDao.updateout((int)list.get(i).get("talking_id"));
						list.get(i).put("talking_state", 100);
					}catch(Exception e){
						e.printStackTrace();
						try {
							throw new Exception("数据库错误");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
			}

		}
		return list;
	}

	@Override
	public List<HashMap<String, Object>> getUserBuyTalking(int userId,
			Integer page,Integer state) {
		// TODO unimplements method stub
		if (page == null || page<1)
			page = 1;
		if (state == null)
			state = 10;
		List<HashMap<String, Object>> list=talkingDao.getUserBuyTalking(userId,(page-1)*10,10,state);
		for(int i=0;i<list.size();i++){
			if(Integer.parseInt(list.get(i).get("talking_state").toString())!=100){
				Date date = new Date();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				long str = date.getTime();
				long str1 = 0;
				try {
					str1 = sdf.parse(list.get(i).get("talking_end_time").toString()).getTime();
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(str>str1){
					try{
						talkingDao.updateout((int)list.get(i).get("talking_id"));
						list.get(i).put("talking_state", 100);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			}

		}
		return list;
	}

	@Override
	public int getUserBuyTalkingCount(int userId, Integer state) {
		if (state == null)
			state = 10;
		return talkingDao.getUserBuyTalkingCount(userId,state);
	}

	@Override
	public boolean delTalking(int userId, int talkingId) {
		return talkingDao.delTalking(talkingId) == 1;
	}

	@Override
	public int getUserTalkingCount(int userId,Integer state) {
		// TODO unimplements method stub
		if(state==1){ //表示要查找用户删除的
			state=0;
			return talkingDao.getUserDeleTalkingCount(userId,state);
		}else{   //表示需要除了删除的其他所有   陆栋修改，希望别动了！
			state=0;
			return talkingDao.getUserTalkingCount(userId,state);
			
		}


	}


	@Override
	public JSONObject ShowFriendsSaidByCfOrCyOrCs(int LeafNode_id,
			int RootNode_id, int Node_id, int city_id, int campus_id,
			int pageCount) {
		
		return null;
	}

	/**
	 * 
	 * @Title: checkTalkingStatus 
	 * @Description: 检查该课程当前状态
	 * @param talkingId
	 * @return
	 * @throws Exception 
	 * @return: TalkingStatus 
	 */
	@Override
	public TalkingStatus checkTalkingStatus(Integer talkingId) throws Exception {
		// 的到该id的课程的信息
		Map<String, Object> takingclass = talkingDao.getTaking(talkingId);
		if (takingclass == null) {
			// 没找到该课程
			return TalkingStatus.NOTFOUND;
		} else {
			
			
		Date talking_start_time =(Date)	takingclass.get("talking_start_time");
		
	    Date nowDate =	CommonUtil.getSystemTime();
			
			
	     if(!talking_start_time.before(nowDate)){
	    	// 10表示正在开课,100表示课程结束,0表示课程被删除 -1 未知
	    				int talking_state = -1;
	    				try {
	    					talking_state = (int) takingclass.get("talking_state");
	    					if (talking_state == 0) {
	    						// 没找到该课程
	    						return TalkingStatus.NOTFOUND;
	    					} else if (talking_state == 100) {
	    						return TalkingStatus.HADOVERDUE;
	    					} else if (talking_state == 10) {
	    						try {
	    							// 课程最多人数
	    							int talking_max_persion = (int) takingclass.get("talking_max_persion");
	    							// 现在已经参加课程人数
	    							int talking_now_persion = (int) takingclass.get("talking_now_persion");
	    							if (talking_max_persion - talking_now_persion <= 0) {
	    								return TalkingStatus.HADFULL;
	    							} else {
	    								return TalkingStatus.CANUSER;
	    							}
	    						} catch (Exception e) {
	    							throw new Exception("talking_max_persion或者talking_now_persion转换数字出错");
	    						}
	    					} else {
	    						return TalkingStatus.UNKOWNERRRO;
	    					}
	    				} catch (Exception e) {
	    					throw new Exception("talking_state转换数字出错");
	    				}
	       }else{
	    	
	    	    talkingDao.updateTalkingStatus(Talking.TALKING_HAD_PASS, talkingId);
	    	    return TalkingStatus.HADOVERDUE;
	    	
	    	
	    }
			
			
			
		}
	}

	
	@Override
	public HashMap<String, Object> getTalkingInfoForCreateOrder(int talkingId) {
		// TODO Auto-generated method stub
		return talkingDao.getTalkingInfoForCreateOrder(talkingId);
		
	}

	@Override
	public String SelectCityNameByCity_id(int city_id) {
		
		return talkingDao.SelectCityNameByCity_id(city_id);
	}

	@Override
	public List<Map<String, Object>> SelectT_taking_leafNodesByNode_id(
			int taking_type_id) {
		
		return talkingDao.SelectT_taking_leafNodesByNode_id(taking_type_id);
	}

	@Override
	public HashMap<String, Object> getTalkingInfoAndPhoneNUM(int talkingId) {
		
		try {
			return (HashMap<String, Object>)talkingDao.getTalkingInfoAndPhoneNUM(talkingId);
		} catch (Exception e) {
			return null;
		}
		
	}
	@Override
	public int judgeIsDelTalking(int talking_id) {
		// TODO Auto-generated method stub
		return talkingDao.judgeIsDelTalking(talking_id);
	}
	@Override
	public Map<String, Object> getUserTalkingInfo(int talking_id) {
		// TODO Auto-generated method stub
		return talkingDao.getUserTalkingInfo(talking_id);
	}
	
	
  


}
