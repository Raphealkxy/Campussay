package com.campussay.model;

/**
 * 用户校园经历表，个人简历时填写
 * Created by wangwenxiang on 15-12-3.
 */
public class CampusExperience {
    private int campusExperienceId; //
    private int campusExperienceUser; //用户
    private String campusExperienceTime; //经历时间，格式：2012.09-2016.06或2012.09-至今
    private String campusExperienceTitle; //经历简单说明，标题
    private String campusExperienceDescript; //经历描述
    private int campusExperienceState; //校园经历状态
    private String campusExperienceRole;  //担任角色


    public int getCampusExperienceId() {
        return campusExperienceId;
    }

    public void setCampusExperienceId(int campusExperienceId) {
        this.campusExperienceId = campusExperienceId;
    }

    public int getCampusExperienceUser() {
        return campusExperienceUser;
    }

    public void setCampusExperienceUser(int campusExperienceUser) {
        this.campusExperienceUser = campusExperienceUser;
    }

    public String getCampusExperienceTime() {
        return campusExperienceTime;
    }

    public String getCampusExperienceRole() {
        return campusExperienceRole;
    }

    public void setCampusExperienceRole(String campusExperienceRole){
        this.campusExperienceRole=campusExperienceRole;
    };

    public void setCampusExperienceTime(String campusExperienceTime) {
        this.campusExperienceTime = campusExperienceTime;
    }

    public String getCampusExperienceTitle() {
        return campusExperienceTitle;
    }

    public void setCampusExperienceTitle(String campusExperienceTitle) {
        this.campusExperienceTitle = campusExperienceTitle;
    }

    public String getCampusExperienceDescript() {
        return campusExperienceDescript;
    }

    public void setCampusExperienceDescript(String campusExperienceDescript) {
        this.campusExperienceDescript = campusExperienceDescript;
    }

    public int getCampusExperienceState() {
        return campusExperienceState;
    }

    public void setCampusExperienceState(int campusExperienceState) {
        this.campusExperienceState = campusExperienceState;
    }
}
