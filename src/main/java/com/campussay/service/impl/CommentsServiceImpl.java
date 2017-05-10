package com.campussay.service.impl;

import com.campussay.dao.AnswerDao;
import com.campussay.dao.CommentsDao;
import com.campussay.model.Comments;
import com.campussay.service.CommentsService;
import com.campussay.util.InfomationUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

@Service("commentsService")
@Transactional(rollbackFor=Exception.class)
public class CommentsServiceImpl implements CommentsService{
	@Resource
	private CommentsDao commentsDao;
	
	@Autowired
	private AnswerDao answerDao;
	
	@Autowired 
	private InfomationUtil infomationUtil;
	
	@Override
	public int addAComments(Comments comm,int commentUserId,String commentUser) throws Exception {
		// TODO Auto-generated method stub
		int n=0;
		HashMap<String,Object> list=null;
		StringBuffer strBuffer=new StringBuffer();
		try{
			n=commentsDao.insertSelective(comm);
			list=answerDao.getAnswerById(comm.getAnswerId());
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		strBuffer.append("<a href='/topic/htIndex?user="+commentUserId+"'>"+commentUser+"</a>评论了");
		strBuffer.append("<a href='/topic/answerdetail?topicid="+list.get("topic_id")+"'>"+"你的回答"+"</a>");
		try{
			infomationUtil.createInformation(strBuffer.toString(), list.get("uid").toString(), 4);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return n;
	}
	public HashMap getCommentsByAnswerId(int answerId,int page,int size){
		List<HashMap> list=null;
		HashMap<String,Object> result=new HashMap<>();
		try{
			list=commentsDao.getCommentsByAnswerId(answerId,page,size);
		}catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		if(list!=null&&list.size()>0) {
			HashMap first = list.get(0);
			result.put("rows", first.get("totalLine"));
		}else{
			result.put("rows",0);
		}
		if(list!=null&&list.size()>0) {
			for(HashMap<String,Object> mp:list){
				mp.remove("totalLine");
				if(mp.get("context")!=null){
					byte[] b=(byte[]) mp.get("context");
					mp.put("context",new String(b,Charset.forName("utf-8")));
				}
			}
		}
		result.put("list",list);
		return result;
	}
}