package com.campussay.model;

/**
 * Created by wangwenxiang on 15-12-25.
 */
public class PhoneMessage {
    private String phoneNumber; //接收短信的手机号码
    private String code; //验证码
    /**
     * 短信签名，传入的短信签名必须是在阿里大鱼“管理中心-短信签名管理”中的可用签名。
     * 注册验证、身份验证、登录验证、变更验证、活动验证
     */
    private String smsFreeSignName;
    /**
     * 短信模板
     * 身份验证验证码--SMS_2685136
     * 登录确认验证码--SMS_2685135
     * 登录异常验证码--SMS_2685134
     * 用户注册验证码--SMS_2685133
     * 活动确认验证码--SMS_2685132
     * 修改密码验证码--SMS_2685131
     * 信息变更验证码--SMS_2685130
     */
    private String smsTemplateCode;

    public PhoneMessage(String phoneNumber, String code, String smsFreeSignName,String smsTemplateCode) {
        this.phoneNumber = phoneNumber;
        this.code = code;
        this.smsFreeSignName = smsFreeSignName;
        this.smsTemplateCode = smsTemplateCode;
    }

    public PhoneMessage() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSmsFreeSignName() {
        return smsFreeSignName;
    }

    public void setSmsFreeSignName(String smsFreeSignName) {
        this.smsFreeSignName = smsFreeSignName;
    }

    public String getSmsTemplateCode() {
        return smsTemplateCode;
    }

    public void setSmsTemplateCode(String smsTemplateCode) {
        this.smsTemplateCode = smsTemplateCode;
    }
}
