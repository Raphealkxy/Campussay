package com.campussay.model;

/**
 * 用户教育经历表，个人资料（简历）
 * Created by wangwenxiang on 15-12-3.
 */
public class Education {
    private int educationId;
    private Integer educationUser; //用户
    private String educationTime; //教育时间，格式：2012.09-2016.06或2012.09-至今
    private String educationCampusName; //教育学校，用户填写时可以从t_campus表获取学校选择，选择后只需要存储学校名称
    private String educationAcademe;//学院
    private String educationMajor;//专业
    private String educationDegree;//学位
    private Integer educationRanking;//专业排名，填写两位数字，如20表示前20%
    private int educationState;//状态

    public int getEducationId() {
        return educationId;
    }

    public void setEducationId(int educationId) {
        this.educationId = educationId;
    }

    public int getEducationUser() {
        return educationUser;
    }

    public void setEducationUser(int educationUser) {
        this.educationUser = educationUser;
    }

    public String getEducationTime() {
        return educationTime;
    }

    public void setEducationTime(String educationTime) {
        this.educationTime = educationTime;
    }

    public String getEducationCampusName() {
        return educationCampusName;
    }

    public void setEducationCampusName(String educationCampusName) {
        this.educationCampusName = educationCampusName;
    }

    public String getEducationAcademe() {
        return educationAcademe;
    }

    public void setEducationAcademe(String educationAcademe) {
        this.educationAcademe = educationAcademe;
    }

    public String getEducationMajor() {
        return educationMajor;
    }

    public void setEducationMajor(String educationMajor) {
        this.educationMajor = educationMajor;
    }

    public String getEducationDegree() {
        return educationDegree;
    }

    public void setEducationDegree(String educationDegree) {
        this.educationDegree = educationDegree;
    }

    public int getEducationRanking() {
        return educationRanking;
    }

    public void setEducationRanking(int educationRanking) {
        this.educationRanking = educationRanking;
    }

    public int getEducationState() {
        return educationState;
    }

    public void setEducationState(int educationState) {
        this.educationState = educationState;
    }
}
