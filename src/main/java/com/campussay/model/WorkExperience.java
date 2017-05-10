package com.campussay.model;

/**
 * 工作经历
 * Created by wangwenxiang on 15-12-3.
 */
public class WorkExperience {
    private int workExperienceId;
    private int workExperienceUser;//用户
    private String workExperienceTime;//工作经历时间段，格式：2012.09-2012.11或者2012.09-至今
    private String workExperiencePlace;//工作地点
    private String workExperienceRole;//工作中所处的角色，如组长，项目负责人
    private String workExperienceDescript;//工作内容描述
    private String workExperienceState;//状态

    public int getWorkExperienceId() {
        return workExperienceId;
    }

    public void setWorkExperienceId(int workExperienceId) {
        this.workExperienceId = workExperienceId;
    }

    public int getWorkExperienceUser() {
        return workExperienceUser;
    }

    public void setWorkExperienceUser(int workExperienceUser) {
        this.workExperienceUser = workExperienceUser;
    }

    public String getWorkExperienceTime() {
        return workExperienceTime;
    }

    public void setWorkExperienceTime(String workExperienceTime) {
        this.workExperienceTime = workExperienceTime;
    }

    public String getWorkExperiencePlace() {
        return workExperiencePlace;
    }

    public void setWorkExperiencePlace(String workExperiencePlace) {
        this.workExperiencePlace = workExperiencePlace;
    }

    public String getWorkExperienceRole() {
        return workExperienceRole;
    }

    public void setWorkExperienceRole(String workExperienceRole) {
        this.workExperienceRole = workExperienceRole;
    }

    public String getWorkExperienceDescript() {
        return workExperienceDescript;
    }

    public void setWorkExperienceDescript(String workExperienceDescript) {
        this.workExperienceDescript = workExperienceDescript;
    }

    public String getWorkExperienceState() {
        return workExperienceState;
    }

    public void setWorkExperienceState(String workExperienceState) {
        this.workExperienceState = workExperienceState;
    }
}
