/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50628
Source Host           : localhost:3306
Source Database       : campussay

Target Server Type    : MYSQL
Target Server Version : 50628
File Encoding         : 65001

Date: 2015-12-08 19:23:59
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_area
-- ----------------------------
DROP TABLE IF EXISTS `t_area`;
CREATE TABLE `t_area` (
  `area_id` int(11) NOT NULL AUTO_INCREMENT,
  `area_city` int(11) NOT NULL COMMENT '所属城市，外键',
  `area_name` varchar(15) NOT NULL COMMENT '名字',
  `area_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`area_id`),
  KEY `area_city` (`area_city`),
  CONSTRAINT `t_area_ibfk_1` FOREIGN KEY (`area_city`) REFERENCES `t_city` (`city_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='城市下的地区';

-- ----------------------------
-- Records of t_area
-- ----------------------------

-- ----------------------------
-- Table structure for t_attention
-- ----------------------------
DROP TABLE IF EXISTS `t_attention`;
CREATE TABLE `t_attention` (
  `attention_id` int(11) NOT NULL AUTO_INCREMENT,
  `attention_owner` int(11) NOT NULL COMMENT '关注者',
  `attention_user` int(11) NOT NULL COMMENT '被关注者',
  PRIMARY KEY (`attention_id`),
  KEY `attention_owner` (`attention_owner`),
  KEY `attention_user` (`attention_user`),
  CONSTRAINT `t_attention_ibfk_1` FOREIGN KEY (`attention_owner`) REFERENCES `t_user` (`user_id`),
  CONSTRAINT `t_attention_ibfk_2` FOREIGN KEY (`attention_user`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户关注表，关注者与被关注者';

-- ----------------------------
-- Records of t_attention
-- ----------------------------

-- ----------------------------
-- Table structure for t_campus
-- ----------------------------
DROP TABLE IF EXISTS `t_campus`;
CREATE TABLE `t_campus` (
  `campus_id` int(11) NOT NULL,
  `campus_area` int(11) NOT NULL COMMENT '大学所属地区',
  `campus_city` int(11) NOT NULL COMMENT '所属城市',
  `campus_province` int(11) NOT NULL COMMENT '所属省份',
  `campus_name` varchar(50) NOT NULL COMMENT '学校名称',
  `campus_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`campus_id`),
  KEY `t_campus_ibfk_1` (`campus_area`),
  CONSTRAINT `t_campus_ibfk_1` FOREIGN KEY (`campus_area`) REFERENCES `t_area` (`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='城市下的大学信息';

-- ----------------------------
-- Records of t_campus
-- ----------------------------

-- ----------------------------
-- Table structure for t_campus_experience
-- ----------------------------
DROP TABLE IF EXISTS `t_campus_experience`;
CREATE TABLE `t_campus_experience` (
  `campus_experience_id` int(11) NOT NULL AUTO_INCREMENT,
  `campus_experience_user` int(11) NOT NULL COMMENT '用户',
  `campus_experience_time` varchar(15) NOT NULL COMMENT '经历时间，格式：2012.09-2016.06或2012.09-至今',
  `campus_experience_title` varchar(100) NOT NULL COMMENT '经历简单说明，标题',
  `campus_experience_descript` varchar(255) NOT NULL COMMENT '经历描述',
  `campus_experience_state` int(2) NOT NULL COMMENT '校园经历状态',
  PRIMARY KEY (`campus_experience_id`),
  KEY `campus_experience_user` (`campus_experience_user`),
  CONSTRAINT `t_campus_experience_ibfk_1` FOREIGN KEY (`campus_experience_user`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户校园经历表，个人简历时填写';

-- ----------------------------
-- Records of t_campus_experience
-- ----------------------------

-- ----------------------------
-- Table structure for t_city
-- ----------------------------
DROP TABLE IF EXISTS `t_city`;
CREATE TABLE `t_city` (
  `city_id` int(11) NOT NULL,
  `city_province` int(11) NOT NULL COMMENT '所属省份',
  `city_name` varchar(8) NOT NULL COMMENT '城市名称',
  `city_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`city_id`),
  KEY `city_province` (`city_province`),
  CONSTRAINT `t_city_ibfk_1` FOREIGN KEY (`city_province`) REFERENCES `t_province` (`province_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='省份下城市信息';

-- ----------------------------
-- Records of t_city
-- ----------------------------

-- ----------------------------
-- Table structure for t_collection
-- ----------------------------
DROP TABLE IF EXISTS `t_collection`;
CREATE TABLE `t_collection` (
  `collection_id` int(11) NOT NULL AUTO_INCREMENT,
  `collection_user` int(11) NOT NULL COMMENT '收藏者',
  `collection_taking` int(11) NOT NULL COMMENT '被收藏的说文',
  PRIMARY KEY (`collection_id`),
  KEY `collection_user` (`collection_user`),
  KEY `collection_taking` (`collection_taking`),
  CONSTRAINT `t_collection_ibfk_1` FOREIGN KEY (`collection_user`) REFERENCES `t_user` (`user_id`),
  CONSTRAINT `t_collection_ibfk_2` FOREIGN KEY (`collection_taking`) REFERENCES `t_taking` (`taking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户收藏表';

-- ----------------------------
-- Records of t_collection
-- ----------------------------

-- ----------------------------
-- Table structure for t_education
-- ----------------------------
DROP TABLE IF EXISTS `t_education`;
CREATE TABLE `t_education` (
  `education_id` int(11) NOT NULL AUTO_INCREMENT,
  `education_user` int(11) NOT NULL COMMENT '用户',
  `education_time` varchar(15) NOT NULL COMMENT '教育时间，格式：2012.09-2016.06或2012.09-至今',
  `education_campus_name` varchar(50) NOT NULL COMMENT '教育学校，用户填写时可以从t_campus表获取学校选择，选择后只需要存储学校名称',
  `education_academe` varchar(50) NOT NULL COMMENT '学院',
  `education_major` varchar(50) NOT NULL COMMENT '专业',
  `education_degree` varchar(20) NOT NULL COMMENT '学位',
  `education_ranking` int(2) DEFAULT NULL COMMENT '专业排名，填写两位数字，如20表示前20%',
  `education_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`education_id`),
  KEY `education_user` (`education_user`),
  CONSTRAINT `t_education_ibfk_1` FOREIGN KEY (`education_user`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户教育经历表，个人资料（简历）';

-- ----------------------------
-- Records of t_education
-- ----------------------------

-- ----------------------------
-- Table structure for t_message
-- ----------------------------
DROP TABLE IF EXISTS `t_message`;
CREATE TABLE `t_message` (
  `message_id` int(11) NOT NULL AUTO_INCREMENT,
  `message_from_user` int(11) NOT NULL COMMENT '消息来源，可以是用户也可以是系统，为0时表示用户',
  `message_to_user` int(11) NOT NULL COMMENT '消息接收用户',
  `message_title` varchar(20) NOT NULL COMMENT '消息标题，消息类型，like系统消息，用户消息',
  `message_content` varchar(255) NOT NULL COMMENT '消息内容',
  `message_time` datetime NOT NULL COMMENT '创建时间',
  `message_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_message
-- ----------------------------

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_user` int(11) NOT NULL COMMENT '用户',
  `order_taking` int(11) NOT NULL COMMENT '说文',
  `order_price` double(10,2) NOT NULL COMMENT '价格，因为用户购买后说文可能会改变，所以这里记录一下该说文的价格',
  `order_creat_time` datetime NOT NULL COMMENT '订单生成时间',
  `order_pay_time` datetime DEFAULT NULL COMMENT '用户付款时间，用户奖钱打入我们的账户中',
  `order_confirm_time` datetime DEFAULT NULL COMMENT '订单确认付款时间，确认付款后将钱打入说者账户',
  `order_pay_ways` int(1) DEFAULT NULL COMMENT '第三方支付方法，1-支付宝，2-微信支付',
  `order_pay_code` varchar(255) DEFAULT NULL COMMENT '第三方支付平台的订单编号',
  `order_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`order_id`),
  KEY `order_user` (`order_user`),
  KEY `order_taking` (`order_taking`),
  CONSTRAINT `t_order_ibfk_1` FOREIGN KEY (`order_user`) REFERENCES `t_user` (`user_id`),
  CONSTRAINT `t_order_ibfk_2` FOREIGN KEY (`order_taking`) REFERENCES `t_taking` (`taking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单表';

-- ----------------------------
-- Records of t_order
-- ----------------------------

-- ----------------------------
-- Table structure for t_prize
-- ----------------------------
DROP TABLE IF EXISTS `t_prize`;
CREATE TABLE `t_prize` (
  `prize_id` int(11) NOT NULL AUTO_INCREMENT,
  `prize_user` int(11) NOT NULL COMMENT '用户',
  `prize_time` varchar(7) NOT NULL COMMENT '获奖时间，格式：2012.09',
  `prize_title` varchar(100) NOT NULL COMMENT '获奖标题，简单说明',
  `prize_descript` varchar(255) NOT NULL COMMENT '描述',
  `prize_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`prize_id`),
  KEY `prize_user` (`prize_user`),
  CONSTRAINT `t_prize_ibfk_1` FOREIGN KEY (`prize_user`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人获奖情况，简历填写';

-- ----------------------------
-- Records of t_prize
-- ----------------------------

-- ----------------------------
-- Table structure for t_province
-- ----------------------------
DROP TABLE IF EXISTS `t_province`;
CREATE TABLE `t_province` (
  `province_id` int(11) NOT NULL AUTO_INCREMENT,
  `province_name` varchar(6) NOT NULL COMMENT '省份名字',
  `province_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`province_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='省份信息';

-- ----------------------------
-- Records of t_province
-- ----------------------------

-- ----------------------------
-- Table structure for t_special_subject
-- ----------------------------
DROP TABLE IF EXISTS `t_special_subject`;
CREATE TABLE `t_special_subject` (
  `special_subject_id` int(11) NOT NULL AUTO_INCREMENT,
  `special_subject_title` varchar(50) NOT NULL COMMENT '专题标题',
  `special_subject_picture` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `special_subject_start_time` datetime NOT NULL COMMENT '专题活动开始时间，可以预先设置开始时间',
  `special_subject_end_time` datetime NOT NULL COMMENT '活动结束时间',
  `special_subject_level` int(1) NOT NULL COMMENT '优先级，优先级越高展示越靠前',
  `special_subject_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`special_subject_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='精品说说专题';

-- ----------------------------
-- Records of t_special_subject
-- ----------------------------

-- ----------------------------
-- Table structure for t_special_subject_taking
-- ----------------------------
DROP TABLE IF EXISTS `t_special_subject_taking`;
CREATE TABLE `t_special_subject_taking` (
  `special_subject_taking_id` int(11) NOT NULL AUTO_INCREMENT,
  `special_subject_taking_subject` int(11) NOT NULL COMMENT '精品说说专题，外键',
  `special_subject_taking_taking` int(11) NOT NULL COMMENT '说说，外键',
  `special_subject_taking_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`special_subject_taking_id`),
  KEY `special_subject_taking_subject` (`special_subject_taking_subject`),
  KEY `special_subject_taking_taking` (`special_subject_taking_taking`),
  CONSTRAINT `t_special_subject_taking_ibfk_1` FOREIGN KEY (`special_subject_taking_subject`) REFERENCES `t_special_subject` (`special_subject_id`),
  CONSTRAINT `t_special_subject_taking_ibfk_2` FOREIGN KEY (`special_subject_taking_taking`) REFERENCES `t_taking` (`taking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='精品说说专题的具体说说';

-- ----------------------------
-- Records of t_special_subject_taking
-- ----------------------------

-- ----------------------------
-- Table structure for t_star
-- ----------------------------
DROP TABLE IF EXISTS `t_star`;
CREATE TABLE `t_star` (
  `star_id` int(11) NOT NULL AUTO_INCREMENT,
  `star_user` int(11) NOT NULL COMMENT '用户，外键',
  `star_name` varchar(20) NOT NULL COMMENT '姓名',
  `star_campus_name` varchar(50) NOT NULL COMMENT '学校名称',
  `star_picture` varchar(255) DEFAULT NULL COMMENT '牛人专区图片',
  `star_info` varchar(50) NOT NULL COMMENT '简介一，不能空',
  `star_info1` varchar(50) DEFAULT NULL COMMENT '简介二',
  `star_info2` varchar(50) DEFAULT NULL COMMENT '简介三',
  `star_info3` varchar(50) DEFAULT NULL COMMENT '简介四',
  `star_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`star_id`),
  KEY `star_user` (`star_user`),
  CONSTRAINT `t_star_ibfk_1` FOREIGN KEY (`star_user`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='大牛专题';

-- ----------------------------
-- Records of t_star
-- ----------------------------

-- ----------------------------
-- Table structure for t_student_check
-- ----------------------------
DROP TABLE IF EXISTS `t_student_check`;
CREATE TABLE `t_student_check` (
  `student_check_id` int(11) NOT NULL AUTO_INCREMENT,
  `student_check_user` int(11) NOT NULL COMMENT '用户',
  `studnet_check_name` varchar(20) NOT NULL COMMENT '真实姓名',
  `student_check_campus` int(11) NOT NULL COMMENT '学校，t_campus外键',
  `student_check_academe` varchar(50) NOT NULL COMMENT '学院',
  `studnet_check_major` varchar(50) NOT NULL COMMENT '专业',
  `student_check_degree` varchar(20) NOT NULL COMMENT '学位',
  `student_check_date` varchar(6) NOT NULL COMMENT '入学年月，存储格式201209',
  `student_check_picture` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `student_check_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`student_check_id`),
  KEY `student_check_user` (`student_check_user`),
  KEY `student_check_campus` (`student_check_campus`),
  CONSTRAINT `t_student_check_ibfk_1` FOREIGN KEY (`student_check_user`) REFERENCES `t_user` (`user_id`),
  CONSTRAINT `t_student_check_ibfk_2` FOREIGN KEY (`student_check_campus`) REFERENCES `t_campus` (`campus_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户学生信息验证，';

-- ----------------------------
-- Records of t_student_check
-- ----------------------------

-- ----------------------------
-- Table structure for t_subject_area
-- ----------------------------
DROP TABLE IF EXISTS `t_subject_area`;
CREATE TABLE `t_subject_area` (
  `subject_area_id` int(11) NOT NULL AUTO_INCREMENT,
  `subject_area_name` varchar(20) NOT NULL COMMENT '专题名称，如精品说说、牛人',
  `subject_area_start_time` datetime DEFAULT NULL COMMENT '设置该专区开始时间',
  `subject_area_end_time` datetime DEFAULT NULL COMMENT '设置结束时间',
  `subject_area_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`subject_area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='记录首页各个活动专区是否展示以及展示时间';

-- ----------------------------
-- Records of t_subject_area
-- ----------------------------

-- ----------------------------
-- Table structure for t_taking
-- ----------------------------
DROP TABLE IF EXISTS `t_taking`;
CREATE TABLE `t_taking` (
  `taking_id` int(11) NOT NULL AUTO_INCREMENT,
  `taking_user` int(11) NOT NULL COMMENT '发布者',
  `taking_title` varchar(255) NOT NULL COMMENT '标题',
  `taking_type` int(11) NOT NULL COMMENT '类型，外键，可以是没有展示出来的类型',
  `taking_max_persion` int(11) NOT NULL COMMENT '人数上限',
  `taking_now_persion` int(11) NOT NULL COMMENT '已经报名的人数',
  `taking_price` double(10,2) NOT NULL COMMENT '价格，每人',
  `taking_is_online` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否线上',
  `taking_is_line` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否线下',
  `taking_show_qq` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否展示qq',
  `taking_show_wechat` tinyint(1) NOT NULL DEFAULT '0' COMMENT '公开微信',
  `taking_show_mail` tinyint(1) NOT NULL COMMENT '是否展示邮箱',
  `taking_show_phone` tinyint(1) NOT NULL COMMENT '是否展示手机',
  `taking_address` varchar(255) DEFAULT NULL COMMENT '线下交流时预留地址',
  `taking_main_picture` varchar(255) DEFAULT NULL COMMENT '主页展示图片',
  `taking_target` varchar(100) DEFAULT NULL COMMENT '交流目标',
  `taking_people` varchar(100) DEFAULT NULL COMMENT '适合人群',
  `taking_info` text NOT NULL COMMENT '详细说明，图文并茂',
  `taking_campus` int(1) NOT NULL COMMENT '学校编号，从发布用户获取，方便后面查询',
  `taking_state` int(2) NOT NULL,
  PRIMARY KEY (`taking_id`),
  KEY `course_publisher` (`taking_user`) USING BTREE,
  KEY `taking_type` (`taking_type`),
  CONSTRAINT `t_taking_ibfk_1` FOREIGN KEY (`taking_user`) REFERENCES `t_user` (`user_id`),
  CONSTRAINT `t_taking_ibfk_2` FOREIGN KEY (`taking_type`) REFERENCES `t_taking_type` (`taking_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='说友发表的说文产品';

-- ----------------------------
-- Records of t_taking
-- ----------------------------

-- ----------------------------
-- Table structure for t_taking_class
-- ----------------------------
DROP TABLE IF EXISTS `t_taking_class`;
CREATE TABLE `t_taking_class` (
  `class_id` int(11) NOT NULL AUTO_INCREMENT,
  `class_taking` int(11) NOT NULL COMMENT '所属说文',
  `class_start_time` datetime NOT NULL COMMENT '开始时间',
  `class_finish_time` datetime NOT NULL COMMENT '结束时间',
  `class_content` varchar(100) NOT NULL COMMENT '该节课程简单说明',
  `class_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`class_id`),
  KEY `class_taking` (`class_taking`),
  CONSTRAINT `t_taking_class_ibfk_1` FOREIGN KEY (`class_taking`) REFERENCES `t_taking` (`taking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='说文的具体课程';

-- ----------------------------
-- Records of t_taking_class
-- ----------------------------

-- ----------------------------
-- Table structure for t_taking_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_taking_comment`;
CREATE TABLE `t_taking_comment` (
  `taking_comment_id` int(11) NOT NULL AUTO_INCREMENT,
  `taking_comment_taking` int(11) NOT NULL COMMENT '所属说文',
  `taking_comment_user` int(11) NOT NULL COMMENT '用户',
  `taking_comment_content` varchar(100) NOT NULL COMMENT '评论内容',
  `taking_comment_content_grade` int(1) NOT NULL COMMENT '干货指数',
  `taking_comment_manner_grade` int(1) NOT NULL COMMENT '交流态度',
  `taking_comment_descript_grade` int(1) NOT NULL COMMENT '描述相符',
  `taking_comment_final_grade` int(1) NOT NULL COMMENT '最综评分评分',
  `taking_comment_time` datetime NOT NULL COMMENT '评论时间',
  `taking_comment_state` int(2) NOT NULL COMMENT '评论状态',
  PRIMARY KEY (`taking_comment_id`),
  KEY `taking_comment_taking` (`taking_comment_taking`),
  KEY `taking_comment_user` (`taking_comment_user`),
  CONSTRAINT `t_taking_comment_ibfk_1` FOREIGN KEY (`taking_comment_taking`) REFERENCES `t_taking` (`taking_id`),
  CONSTRAINT `t_taking_comment_ibfk_2` FOREIGN KEY (`taking_comment_user`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='说文评论，只有购买的用户可以评论';

-- ----------------------------
-- Records of t_taking_comment
-- ----------------------------

-- ----------------------------
-- Table structure for t_taking_picture
-- ----------------------------
DROP TABLE IF EXISTS `t_taking_picture`;
CREATE TABLE `t_taking_picture` (
  `taking_picture_id` int(11) NOT NULL AUTO_INCREMENT,
  `taking_picture_url` varchar(255) NOT NULL COMMENT '图片地址',
  `taking_picture_taking` int(11) NOT NULL COMMENT '图片所属说文',
  `taking_picture_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`taking_picture_id`),
  KEY `taking_picture_taking` (`taking_picture_taking`),
  CONSTRAINT `t_taking_picture_ibfk_1` FOREIGN KEY (`taking_picture_taking`) REFERENCES `t_taking` (`taking_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='说文图片，多张图片时主图片在t_taking表，其余图片在该表';

-- ----------------------------
-- Records of t_taking_picture
-- ----------------------------

-- ----------------------------
-- Table structure for t_taking_question
-- ----------------------------
DROP TABLE IF EXISTS `t_taking_question`;
CREATE TABLE `t_taking_question` (
  `taking_question_id` int(11) NOT NULL AUTO_INCREMENT,
  `taking_question_taking` int(11) NOT NULL COMMENT '所属说文',
  `taking_question_user` int(11) NOT NULL COMMENT '所属用户',
  `taking_question_content` varchar(100) NOT NULL COMMENT '用户问题内容',
  `taking_question_time` datetime NOT NULL COMMENT '时间',
  `taking_question_state` int(11) NOT NULL COMMENT '状态',
  PRIMARY KEY (`taking_question_id`),
  KEY `taking_question_taking` (`taking_question_taking`),
  KEY `taking_question_user` (`taking_question_user`),
  CONSTRAINT `t_taking_question_ibfk_1` FOREIGN KEY (`taking_question_taking`) REFERENCES `t_taking` (`taking_id`),
  CONSTRAINT `t_taking_question_ibfk_2` FOREIGN KEY (`taking_question_user`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='说文提问表';

-- ----------------------------
-- Records of t_taking_question
-- ----------------------------

-- ----------------------------
-- Table structure for t_taking_question_communicat
-- ----------------------------
DROP TABLE IF EXISTS `t_taking_question_communicat`;
CREATE TABLE `t_taking_question_communicat` (
  `taking_question_communicat_id` int(11) NOT NULL AUTO_INCREMENT,
  `taking_question_communicat_question` int(11) NOT NULL COMMENT '所属说文提问',
  `taking_question_communicat_user` int(11) NOT NULL COMMENT '用户',
  `taking_question_communicat_content` varchar(100) NOT NULL COMMENT '内容，50字以内',
  `taking_question_communicat_time` datetime NOT NULL COMMENT '时间',
  `taking_question_communicat_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`taking_question_communicat_id`),
  KEY `taking_question_communicat_question` (`taking_question_communicat_question`),
  KEY `taking_question_communicat_user` (`taking_question_communicat_user`),
  CONSTRAINT `t_taking_question_communicat_ibfk_1` FOREIGN KEY (`taking_question_communicat_question`) REFERENCES `t_taking_question` (`taking_question_id`),
  CONSTRAINT `t_taking_question_communicat_ibfk_2` FOREIGN KEY (`taking_question_communicat_user`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='对说文提问的交流表，想当于问题的楼中楼';

-- ----------------------------
-- Records of t_taking_question_communicat
-- ----------------------------

-- ----------------------------
-- Table structure for t_taking_type
-- ----------------------------
DROP TABLE IF EXISTS `t_taking_type`;
CREATE TABLE `t_taking_type` (
  `taking_type_id` int(11) NOT NULL AUTO_INCREMENT,
  `taking_type_name` varchar(255) NOT NULL COMMENT '类型名称',
  `taking_type_picture` varchar(255) DEFAULT NULL,
  `taking_type_floor` varchar(255) NOT NULL COMMENT '所属层级',
  `taking_type_parent` int(11) NOT NULL COMMENT '父节点',
  `taking_type_is_leaf` tinyint(4) NOT NULL COMMENT '是否是叶子节点，只有叶子节点才能成为说文类型',
  `taking_type_state` int(11) NOT NULL COMMENT '状态',
  PRIMARY KEY (`taking_type_id`),
  KEY `taking_type_parent` (`taking_type_parent`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='说文分类表，树形结构';

-- ----------------------------
-- Records of t_taking_type
-- ----------------------------

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(20) NOT NULL COMMENT '用户昵称、真实姓名',
  `user_sex` enum('0','1') DEFAULT NULL COMMENT '0-女，1-男',
  `user_password` varchar(100) NOT NULL COMMENT '密码',
  `user_photo` varchar(255) DEFAULT NULL COMMENT '用户头像',
  `user_qq` int(10) DEFAULT NULL COMMENT 'qq登录或者绑定qq',
  `user_phone` varchar(20) DEFAULT NULL COMMENT '已验证电话',
  `user_mail` varchar(255) DEFAULT NULL COMMENT '已验证邮箱',
  `user_wechat` varchar(50) DEFAULT NULL COMMENT '绑定微信，分享',
  `user_register_time` datetime NOT NULL COMMENT '注册时间',
  `user_birthday` datetime DEFAULT NULL COMMENT '生日',
  `user_student_check_result` int(1) NOT NULL DEFAULT '0' COMMENT '学生信息验证结果，0-未验证、正在验证、验证未通过，1-验证通过',
  `user_campus_name` varchar(50) DEFAULT NULL COMMENT '用户所属学校，学生信息验证通过后，记录学校名称',
  `user_pay_account` varchar(30) DEFAULT NULL COMMENT '用户第三方支付账户',
  `user_credits` int(11) NOT NULL COMMENT '用户积分',
  `user_is_star` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否牛人，相当于是否加V，默认值为0',
  `user_description` varchar(255) DEFAULT NULL COMMENT '个人说明',
  `user_state` int(3) NOT NULL COMMENT '状态',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of t_user
-- ----------------------------

-- ----------------------------
-- Table structure for t_work_experience
-- ----------------------------
DROP TABLE IF EXISTS `t_work_experience`;
CREATE TABLE `t_work_experience` (
  `work_experience_id` int(11) NOT NULL AUTO_INCREMENT,
  `work_experience_user` int(11) NOT NULL COMMENT '用户',
  `work_experience_time` varchar(255) NOT NULL COMMENT '工作经历时间段，格式：2012.09-2012.11或者2012.09-至今',
  `work_experience_place` varchar(50) NOT NULL COMMENT '工作地点',
  `work_experience_role` varchar(20) DEFAULT NULL COMMENT '工作中所处的角色，如组长，项目负责人',
  `work_experience_descript` varchar(255) NOT NULL COMMENT '工作内容描述',
  `work_experience_state` int(2) NOT NULL COMMENT '状态',
  PRIMARY KEY (`work_experience_id`),
  KEY `work_experience_user` (`work_experience_user`),
  CONSTRAINT `t_work_experience_ibfk_1` FOREIGN KEY (`work_experience_user`) REFERENCES `t_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='工作经历';

-- ----------------------------
-- Records of t_work_experience
-- ----------------------------
