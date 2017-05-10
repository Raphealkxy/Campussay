package com.campussay.dao;

import com.campussay.model.Answer;
import com.campussay.model.Comments;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface AnswerDao {
	
      /*
       * 根据文章Id查询文章相关信息
       * */
	List<HashMap> selectAnswersByTopicId(int topic_id,int head,int size);
	/*
	 * 根据user_id得到回答
	 * */
	List<HashMap> selectAllAnswerByUserID(int user_id,int head,int size);

	void insertOneAnswerByUserID(Comments comm);

	HashMap<String,Object> getTopAnswerByTopicId(Integer id);

	HashMap<String,Object> getDetailAnswerById(int answer_Id);
	//得到topic下面answer数量
	HashMap<String,Object> getAnswerNumByTopicId(int topic_id);
	//对答案进行点赞
	public int addIsLike(int topic_id);
	//对答案进行减赞
	public int disIsLike(int topic_id);
	//添加topic答案
	public int addAnswer(Answer answer);

	//通过topic_id得到最新的一个答案
	HashMap getNewAnswerByTopic(int topic_id);

	//新添加一个答案后，跳转到最后一页，返回当前answer总条数
	public int getLastPageByTopicId(int topic_id);
	
	/*
	 * 消息推送，通过id获取answer信息
	 */
	public HashMap<String,Object> getAnswerById(int answerId);
	
	//用户是否对答案点过赞
    public Integer getUserIsLike(int userId,int answerId);
    
    
    //获取点赞量
    public int getAnswerLikeNum(int answerId);
    //获取减赞量
    public int getAnswerDisLikeNum(int answerId);
    
    public HashMap<String,Object> getAnswerDetailById(int answerId);
    

}
