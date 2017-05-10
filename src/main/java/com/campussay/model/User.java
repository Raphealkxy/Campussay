package com.campussay.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户
 * @author wangwenxiang
 */
public class User implements Serializable{

	private static final long serialVersionUID = 1390366007623566260L;

	private int userId;
	private String userName; //用户昵称、真实姓名
	private int userSex;  //0-女，1-男
	private String userPassword;  //密码
	private String userPhoto;  //用户头像
	private String userQQ;  //qq登录或者绑定qq
	private String userPhone; //已验证电话
	private String userMail; //已验证邮箱
	private String userWechat; //绑定微信，分享
	private Date userRegisterTime; //注册时间
	private Date userBirthday; //生日
	private int userStudentCheckResult; //学生信息验证结果，0-未验证、正在验证、验证未通过，1-验证通过
	private String userCampusName;//用户所属学校，学生信息验证通过后，记录学校名称
	private String userPayAccount;//用户第三方支付账户
	private String userPayName;   //用户第三方支付账户填写的真实姓名
	private int userTalkingCount;  //用户发布talking数量
	private boolean userIsStar;//是否牛人，相当于是否加V，默认值为0
	private String userDescription;//个人说明
	private String userAcademe; //学院
	private String userMajor;  //专业
	private int userState;//状态
	private double userBalance;//用户余额
	private double userNeedCash;//用户提现金额
	private int userGetcashStatus;//用户提现状态

	public String getUserTilte() {
		return userTilte;
	}

	public void setUserTilte(String userTilte) {
		this.userTilte = userTilte;
	}

	private String userTilte;   //用户头衔

	
	
	public double getUserBalance() {
		return userBalance;
	}

	public void setUserBalance(double userBalance) {
		this.userBalance = userBalance;
	}

	public double getUserNeedCash() {
		return userNeedCash;
	}

	public void setUserNeedCash(double userNeedCash) {
		this.userNeedCash = userNeedCash;
	}

	public int getUserGetcashStatus() {
		return userGetcashStatus;
	}

	public void setUserGetcashStatus(int userGetcashStatus) {
		this.userGetcashStatus = userGetcashStatus;
	}

	public int getUserTalkingCount() {
		return userTalkingCount;
	}

	public void setUserTalkingCount(int userTalkingCount) {
		this.userTalkingCount = userTalkingCount;
	}

	public String getUserMajor() {
		return userMajor;
	}

	public String getUserAcademe() {
		return userAcademe;
	}

	public void setUserAcademe(String userAcademe)
	{this.userAcademe=userAcademe;}

	public void setUserMajor(String userMajor)
	{this.userMajor=userMajor;}

	public String getUserPayName() {
		return userPayName;
	}

	public void setUserPayName(String userPayName) {
		this.userPayName = userPayName;
	}

	public int getUserSex() {
		return userSex;
	}

	public void setUserSex(int userSex) {
		this.userSex = userSex;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}

	public void setUserQQ(String userQQ) {
		this.userQQ = userQQ;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public void setUserWechat(String userWechat) {
		this.userWechat = userWechat;
	}

	public void setUserRegisterTime(Date userRegisterTime) {
		this.userRegisterTime = userRegisterTime;
	}

	public void setUserBirthday(Date userBirthday) {
		this.userBirthday = userBirthday;
	}

	public void setUserStudentCheckResult(int userStudentCheckResult) {
		this.userStudentCheckResult = userStudentCheckResult;
	}

	public void setUserCampusName(String userCampusName) {
		this.userCampusName = userCampusName;
	}

	public void setUserPayAccount(String userPayAccount) {
		this.userPayAccount = userPayAccount;
	}

	public void setUserIsStar(boolean userIsStar) {
		this.userIsStar = userIsStar;
	}

	public void setUserDescription(String userDescription) {
		this.userDescription = userDescription;
	}

	public void setUserState(int userState) {
		this.userState = userState;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getUserPhoto() {
		return userPhoto;
	}

	public String getUserQQ() {
		return userQQ;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public String getUserMail() {
		return userMail;
	}

	public String getUserWechat() {
		return userWechat;
	}

	public Date getUserRegisterTime() {
		return userRegisterTime;
	}

	public Date getUserBirthday() {
		return userBirthday;
	}

	public int getUserStudentCheckResult() {
		return userStudentCheckResult;
	}

	public String getUserCampusName() {
		return userCampusName;
	}

	public String getUserPayAccount() {
		return userPayAccount;
	}

	public boolean isUserIsStar() {
		return userIsStar;
	}

	public String getUserDescription() {
		return userDescription;
	}

	public int getUserState() {
		return userState;
	}
}
