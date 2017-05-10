package com.campussay.controller;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Topic;
import com.campussay.model.User;
import com.campussay.service.TopicService;
import com.campussay.util.CommonUtil;
import com.campussay.util.EnumUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/topic")
public class TopicController {

	@Autowired
	private TopicService topicService;

	private static final int pageSize = 10;

	/*
	 * 得到最新topic
	 * 
	 * @param page 分页，为null时默认为1
	 * 
	 * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
	 */
	@RequestMapping("getNewTopics")
	@ResponseBody
	public JSONObject getNewTopics(HttpSession session,
			@RequestParam(required = false) Integer page) {
		User user = (User) session.getAttribute("user");
		Map<String, Object> list = null;
		if (page == null || page < 0) {
			page = 1;
		}
		if (user == null) {
			list = (Map<String, Object>) topicService.getNewTopicsUnLogin(
					(page - 1) * pageSize, pageSize);
			return CommonUtil.constructResponse(-1, "用户未登录！", list);
		}
		try {
			list = topicService.getNewTopics(user.getUserId(), (page - 1)
					* pageSize, pageSize);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,
					"数据库操作异常", null);
		}
		if (list == null) {
			return CommonUtil.constructResponse(1, "数据库没有数据", list);
		}
		return CommonUtil.constructResponse(1, "获取数据成功", list);
	}

	/*
	 * 根据用户关注领域得到最新topic
	 * 
	 * @param page 分页，为null时默认为1
	 * 
	 * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
	 */
	@RequestMapping("getNewTopicsByUser")
	@ResponseBody
	public JSONObject getNewTopicsByUser(HttpServletRequest request,
			@RequestParam(required = false) Integer page) {
		User user = (User) request.getAttribute("user");
		Map<String, Object> list = null;
		if (user == null) {
			return CommonUtil.constructResponse(-1, "用户未登录！", null);
		}
		if (page == null || page < 0) {
			page = 1;
		}
		try {
			list = topicService.getNewTopicsByUser(user.getUserId(), (page - 1)
					* pageSize, pageSize);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,
					"数据库操作错误", null);
		}
		if (list == null) {
			return CommonUtil.constructResponse(1, "数据库没有数据", list);
		}
		return CommonUtil.constructResponse(1, "获取数据成功", list);

	}

	/*
	 * 根据topic_id得到topic详细信息
	 * 
	 * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
	 */
	@RequestMapping("getDetailTopic")
	@ResponseBody
	public JSONObject getDetailTopic(HttpSession session, int topic_id) {
		User user = null;
		user = (User) session.getAttribute("user");
		int userId=0;
		List<Map<String, Object>> list = null;
		if (topic_id < 0) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,
					"传入参数非法", null);
		}
		try {
			if (user == null) {
				userId=-1;
			}else{
				userId=user.getUserId();
			}
			list = topicService.getDetailTopic(userId, topic_id);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,
					"数据库操作错误", null);
		}
		if (list == null) {
			return CommonUtil.constructResponse(1, "数据库没有数据", list);
		}
		return CommonUtil.constructResponse(1, "获取数据成功", list);

	}

	/*
	 * 添加一個topic
	 * 
	 * @Param title，intro，field_id
	 * 
	 * @return json格式 code: 1 成功 , 0 未返回结果; msg 返回的记录条数; data 返回的结果集
	 */
	@RequestMapping("AddATopic")
	@ResponseBody
	public JSONObject AddATopic(HttpSession session, String title,
			@RequestParam(required = false) String intro,
			@RequestParam(required = false) String imageUrl,
			Integer takingTypeId) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return CommonUtil.constructResponse(-1, "用户未登录！", null);
		}
		Topic topic = new Topic();
		if (imageUrl == null) {
			String pictureUrl = null;
			try {
				pictureUrl = topicService.getPictureUrl(takingTypeId);
			} catch (Exception e) {
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,
						"数据库操作错误", null);
			}
			topic.setCoverImg(pictureUrl);
		} else {
			topic.setCoverImg(imageUrl);
		}
		topic.setTile(title);
		topic.setTakingTypeId(takingTypeId);
		if (intro != null) {
			try {
				topic.setIntro(intro.getBytes("utf-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		topic.setUserId(user.getUserId());
		topic.setCreateTime(new Date());
		topic.setUpdateTime(new Date());
		topic.setState(1);
		int n = 0;
		try {
			n = topicService.AddATopic(topic,user.getUserId());
			
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,
					"数据库操作错误", null);
		}
		if (n == 0) {
			return CommonUtil.constructResponse(0, "添加失败！", null);
		}
		Map<String,Object> mp=new HashMap<String,Object>(); 
		mp.put("topicId", n);
		return CommonUtil.constructResponse(1, "添加成功！", mp);
	}

	/*
	 * 根据user_id得到话题
	 */
	@RequestMapping("getAllTopicByUserId")
	@ResponseBody
	public JSONObject getAllTopicByUserId(HttpSession session,
			@RequestParam(required = false) Integer userId,
			@RequestParam(required = false) Integer page) {
		Map<String, Object> list = null;
		if (page == null || page < 0) {
			page = 1;
		}
		User user = (User) session.getAttribute("user");
		// if (user == null) {
		// return CommonUtil.constructResponse(-1, "用户未登录！", null);
		// }
		if (userId > 0) {
			if (user == null) {
				try {
					list = topicService.getAllTopicByUserID(-1, userId,
							(page - 1) * pageSize, pageSize);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return CommonUtil.constructResponse(1, "获取数据成功", list);
				
			} else {
				try {
					list = topicService.getAllTopicByUserID(user.getUserId(), userId,
							(page - 1) * pageSize, pageSize);
				} catch (Exception e) {
					return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,
							"数据库操作错误", null);
				}
				if (list == null) {
					return CommonUtil.constructResponse(1, "数据库没有数据", list);
				}
				return CommonUtil.constructResponse(1, "获取数据成功", list);
			}
		}
		if (userId == 0) {
			if (user == null) {
				return CommonUtil.constructResponse(-1, "用户未登录！", null);
			}
			try {
				list = topicService.getAllTopicByUserID(user.getUserId(),
						user.getUserId(), (page - 1) * pageSize, pageSize);
			} catch (Exception e) {
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,
						"数据库操作错误", null);
			}
			if (list == null) {
				return CommonUtil.constructResponse(1, "数据库没有数据", list);
			}
			return CommonUtil.constructResponse(1, "获取数据成功", list);
		}
		return CommonUtil.constructResponse(1, "获取用户信息失败", null);
	}

	/*
	 * 通过二级领域id得到领域下面的话题
	 */
	@RequestMapping("getAllTopicByFieldID")
	@ResponseBody
	public JSONObject getAllTopicByFieldID(Integer field_id,
			@RequestParam(required = false) Integer page) {
		Map<String, Object> list = null;
		if (page == null || page < 0) {
			page = 1;
		}
		try {
			list = topicService.getAllTopicByFieldID(field_id, (page - 1)
					* pageSize, page * pageSize);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,
					"数据库操作异常", null);
		}
		if (list == null) {
			return CommonUtil.constructResponse(1, "数据库没有数据", list);
		}
		return CommonUtil.constructResponse(1, "获取数据成功", list);
	}

	/*
	 * 得到用戶的詳細信息通過userID
	 */
	@RequestMapping("getUserDetailMsgById")
	@ResponseBody
	public JSONObject getUserDetailMsgById(HttpSession session,
			@RequestParam(required = false) Integer userId) {
		if (userId > 0) { // 大于0，表示访问别人的话题社 等于0，自己的话题社
			User user = (User) session.getAttribute("user");
			Map<String, Object> hmp = null;
			if (user == null) {
				try {
					hmp = topicService.getUserDetailMsgByOtherId(0, userId);
				} catch (Exception e) {
					return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,
							"数据库操作异常", null);
				}
			} else {
				try {
					hmp = topicService.getUserDetailMsgByOtherId(
							user.getUserId(), userId);
				} catch (Exception e) {
					return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,
							"数据库操作异常", null);
				}
			}
			return CommonUtil.constructResponse(1, null, hmp);
		}
		if (userId == 0) {
			Map<String, Object> hmp = null;
			User user = (User) session.getAttribute("user");
			if (user == null) {
				return CommonUtil.constructResponse(-1, "用户未登录！", null);
			}
			try {
				hmp = topicService.getUserDetailMsgById(user.getUserId());
			} catch (Exception e) {
				return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,
						"数据库操作异常", null);
			}
			if (hmp == null) {
				return CommonUtil.constructResponse(1, "数据库没有数据", hmp);
			}
			return CommonUtil.constructResponse(1, "获取数据成功", hmp);
		}
		return CommonUtil.constructResponse(0, "传入参数异常", null);
	}

	/*
	 * 通过一级领域id得到领域下面的话题
	 */
	@RequestMapping("getAllTopicBySecondFieldID")
	@ResponseBody
	public JSONObject getAllTopicByFirstFieldID(Integer fieldId,
			@RequestParam(required = false) Integer page) {
		Map<String, Object> list = null;
		if (page == null || page < 0) {
			page = 1;
		}
		try {
			list = topicService.getAllTopicByFirstFieldID(fieldId, (page - 1)
					* pageSize, page * pageSize);
		} catch (Exception e) {
			return CommonUtil.constructResponse(EnumUtil.SYSTEM_ERROR,
					"数据库操作异常", null);
		}
		if (list == null) {
			return CommonUtil.constructResponse(1, "数据库没有数据", list);
		}
		return CommonUtil.constructResponse(1, "获取数据成功", list);
	}

}
