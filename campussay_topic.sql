


-- 领域表
create table t_field(
	id int(11) not null auto_increment primary key comment '领域ID',
	pid int(11) not null  default 0 comment '领域父ID(一级领域父ID为0)',
	name varchar(32) not null comment '领域名称',
	status int(1) not null default 0 comment '是否可用[0不能用|1可用]'
)engine=InnoDB default charset=utf8 comment '话题设领域表'


-- 关注领域表
create table t_follow(
	id int(11) not null  auto_increment primary key comment '关注领域ID',
	uid int(11) not null  comment '用户ID',
	field_id int(11) not null comment '领域ID'
)engine=InnoDB default charset=utf8 comment '关注领域表'


-- 话题表
create table t_topic(
	id int(11) not null  auto_increment primary key comment '话题ID',
	field_id int(11) not null comment '话题领域ID',
	tile varchar(32) not null comment '话题标题',
	cover_img varchar(255)  comment '话题封面图片',
	intro blob comment ’话题详细内容‘,
	create_time datetime default '0000-00-00 00:00:00' comment '创建时间',
	update_time datetime default '0000-00-00 00:00:00' comment '更新时间'
)engine=InnoDB default charset=utf8 comment '话题表'


-- 话题回答表
create table t_answer(
	id int(11) not null auto_increment primary key comment '回答ID',
	uid int(11) not null comment '用户ID',
	topic_id int(11) not null comment '话题ID',
	context blob comment '回答内容',
	islike int(11) default 0 comment '是否赞成/喜欢[0不喜欢|1喜欢]', 
	time datetime default '0000-00-00 00:00:00' comment '回答时间'
)engine=InnoDB default charset=utf8 comment '话题回答表'

-- 评论表
create table t_comments(
	id int(11) not null auto_increment primary key comment '评论ID',
	uid int(11) not null comment '用户ID',
	answer_id int(11) not null comment '话题回答ID',
	context blob comment '评论内容',
	islike int(11) default 0 comment '是否赞成/喜欢[0不喜欢|1喜欢]', 
	time datetime default '0000-00-00 00:00:00' comment '评论时间'
)engine=InnoDB default charset=utf8 comment '话题回答评论表'