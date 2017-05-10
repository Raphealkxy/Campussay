package com.campussay.service.impl;

import com.campussay.dao.AnswerDao;
import com.campussay.dao.FollowTopicDao;
import com.campussay.dao.TopicDao;
import com.campussay.dao.UserAnswerIsLikeDao;
import com.campussay.model.Answer;
import com.campussay.model.Topic;
import com.campussay.service.AnswerService;
import com.campussay.util.InfomationUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
@Transactional(rollbackFor=Exception.class)
public class AnswerServiceImpl implements AnswerService{
	
	@Autowired
	private AnswerDao answ;
	@Autowired
	private TopicDao topicDao;
	@Autowired
	private UserAnswerIsLikeDao userAnswerIsLikeDao;
	@Autowired
	private InfomationUtil infomationUtil;
	@Autowired
	private FollowTopicDao followTopicDao;
	private static final int pageSize=10;

	//得到topic所有答案
	public HashMap selectAnswersByTopicId(int userId,int topic_id,
			Integer page) throws Exception {
		if (page == null || page < 1)
			page = 1;
		HashMap<String,Object> result=new HashMap<>();
		List<HashMap> list=null;
		int k=0;
		try{
			list=answ.selectAnswersByTopicId(topic_id,(page-1)*pageSize,pageSize);
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("数据库操作异常");
		}
		if(list!=null&&list.size()>0) {
			HashMap first = list.get(0);
			result.put("rows", first.get("totalLine"));
		}
		if(list!=null&&list.size()>0) {
			for(HashMap<String,Object> mp:list){
				if(mp.get("context")!=null) {
					byte[] b = (byte[]) mp.get("context");
					mp.put("context", new String(b,Charset.forName("utf-8")));
				}
				mp.remove("totalLine");
				k=answ.getUserIsLike(userId, (int)mp.get("id"));
				if(k>0){
					mp.put("userIsLike", 1);
				}else{
					mp.put("userIsLike", 0);
				}
			}
		}
		result.put("list",list);
		return result;
	}

	//通过topic_id得到answer数量
	public HashMap<String,Object> getAnswerNumByTopicId(int topic_id) throws Exception{
		try{
			return answ.getAnswerNumByTopicId(topic_id);
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception("数据库操作异常");
		}
	}

	@Override
	public HashMap<String,Object> selectAllAnswerByUserID(int user_id, Integer page) throws Exception {
		HashMap<String,Object> result=new HashMap<String,Object>();
		List<HashMap> list=null;
		if (page == null || page < 1)
			page = 1;
		try{
			list = answ.selectAllAnswerByUserID(user_id, (page - 1) * pageSize, pageSize);
		}catch (Exception e) {
			throw new Exception("操作数据库异常");
		}
		if(list!=null&&list.size()>0) {
			HashMap first = list.get(0);
			result.put("rows", first.get("totalLine"));
		}
		if(list!=null&&list.size()>0) {
			for (HashMap mp : list) {
				mp.remove("totalLine");
				String usersname="";
				if (mp.get("context") != null) {
					byte[] b = (byte[]) mp.get("context");
				mp.put("context", new String(b,Charset.forName("utf-8")));
			}
			try{
				List<HashMap> users=topicDao.getAllUserByTopicId((int)mp.get("topicId"));
				for(HashMap user:users){
					if((String) user.get("user_name")!=null)
						usersname += (String) user.get("user_name")+"、";
				}
			}catch (Exception e) {
				throw new Exception("操作数据库异常");
			}
			mp.put("usersname",usersname);
			}
		}
		result.put("list",list);
		return result;
	}
	//点赞
	public int addIsLike(int userId,String username,int answerId) throws Exception {
		int result=0; int n=0;  
		StringBuffer strBuffer=new StringBuffer();
		HashMap topic=topicDao.getTopicByAnswerId(answerId);
		HashMap mp=new HashMap<String,Object>();
		strBuffer.append("<a href='topic/htIndex?user="+userId+"'>"+username+"</a>"+"点赞了<a href='topic/answerdetail?topicid="+topic.get("id")+"'>你的回答</a>");
		try{
			n=userAnswerIsLikeDao.getIsLikeByUser(userId, answerId, 1);
			userAnswerIsLikeDao.delUserAnswerLike(userId,answerId,-1);
			mp=answ.getAnswerById(answerId);
			int uid=(int)mp.get("uid");
			if(n>0){
				return 0;
			}
			userAnswerIsLikeDao.addUserAnswerIsLike(userId, answerId, 1);
			result=answ.addIsLike(answerId);
			try{
				infomationUtil.createInformation(strBuffer.toString(),String.valueOf(uid),4);
			}catch (Exception e) {
				throw new Exception("操作数据库异常");
			}
		}catch (Exception e) {
			throw new Exception("操作数据库异常");
		}
		return result;
	}
	//减赞
	public int disIsLike(int userId,int answerId) throws Exception{
		int result=0;int n=0;
		try{
			n=userAnswerIsLikeDao.getIsLikeByUser(userId, answerId, -1);
			userAnswerIsLikeDao.delUserAnswerLike(userId,answerId,1);
			if(n>0){
				return 0;
			}
			userAnswerIsLikeDao.addUserAnswerIsLike(userId, answerId, -1);
			result=answ.disIsLike(answerId);
		}catch (Exception e) {
			throw new Exception("操作数据库异常");
		}
		return result;
	}
	
	//添加topic答案
	public int addAnswer(Answer answer,int replyId,String replyName) throws Exception{
		int k=0;int n=0;
		StringBuffer strBuffer=new StringBuffer();
		HashMap<String,Object> list=null;
		try{
			n=answ.addAnswer(answer);
			list=topicDao.getTopicById(answer.getTopicId());
		}catch (Exception e) {
			throw new Exception("操作数据库异常");
		}
		strBuffer.append("<a href='/topic/htIndex?user="+replyId+"'>"+replyName+"</a>回答了<a href='/topic/answerdetail?topicid="+list.get("id")+"'>");
		strBuffer.append(list.get("tile")+"</a>");
		try{
			infomationUtil.createInformation(strBuffer.toString(), list.get("user_id").toString(), 4);
		}catch (Exception e) {
			throw new Exception("操作数据库异常");
		}
		if(n>0){
			Topic topic=new Topic();
			topic.setId(answer.getTopicId());
			topic.setUpdateTime(new Date());
			k=topicDao.updateTopicTime(topic);
			//通知关注用户，有新的回答了
			List<Integer> followUsers=followTopicDao.getFollowUsersByTopicId(answer.getTopicId());
			if(followUsers!=null){
				for(Integer followuser:followUsers){
					StringBuffer strBuffer_1=new StringBuffer();
					strBuffer_1.append("<a href='/topic/htIndex?user="+replyId+"'>"+replyName+"</a>回答了你关注的<a href='/topic/answerdetail?topicid="+list.get("id")+"'>");
					strBuffer_1.append(list.get("tile")+"</a>");
					infomationUtil.createInformation(strBuffer_1.toString(),String.valueOf(followuser), 4);
				}
			}
			if(k==0){
				throw new Exception("添加答案异常");
			}
		}
		
		
		return k;
	}


	//新添加一个答案后，跳转到最后一页，返回当前answer总条数
	public int getLastPageByTopicId(int topic_id){
		try{
			return answ.getLastPageByTopicId(topic_id);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public HashMap<String, Object> getAnswerDetailById(int answerId) {
		// TODO Auto-generated method stub
		return answ.getAnswerDetailById(answerId);
	}
}
