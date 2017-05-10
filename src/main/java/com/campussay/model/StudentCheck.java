package com.campussay.model;

/**
 * 用户学生信息验证
 * Created by wangwenxiang on 15-12-3.
 */
public class StudentCheck {
    private int studentCheck;
    private int studentCheckUer;//用户
    private String studentCheckName;//真实姓名
    private int studentCheckCampus;//学校，t_campus外键
    private String studentCheckTime;//入学年月，存储格式201209
    private String studentCheckPicture;//图片地址
    private int studentCheckState;//状态

    public int getStudentCheck() {
        return studentCheck;
    }

    public void setStudentCheck(int studentCheck) {
        this.studentCheck = studentCheck;
    }

    public int getStudentCheckUer() {
        return studentCheckUer;
    }

    public void setStudentCheckUer(int studentCheckUer) {
        this.studentCheckUer = studentCheckUer;
    }

    public String getStudentCheckName() {
        return studentCheckName;
    }

    public void setStudentCheckName(String studentCheckName) {
        this.studentCheckName = studentCheckName;
    }

    public int getStudentCheckCampus() {
        return studentCheckCampus;
    }

    public void setStudentCheckCampus(int studentCheckCampus) {
        this.studentCheckCampus = studentCheckCampus;
    }

    public String getStudentCheckTime() {
        return studentCheckTime;
    }

    public void setStudentCheckTime(String studentCheckTime) {
        this.studentCheckTime = studentCheckTime;
    }

    public String getStudentCheckPicture() {
        return studentCheckPicture;
    }

    public void setStudentCheckPicture(String studentCheckPicture) {
        this.studentCheckPicture = studentCheckPicture;
    }

    public int getStudentCheckState() {
        return studentCheckState;
    }

    public void setStudentCheckState(int studentCheckState) {
        this.studentCheckState = studentCheckState;
    }
}
