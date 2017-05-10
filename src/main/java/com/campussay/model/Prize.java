package com.campussay.model;


/**
 * 个人获奖情况，简历填写
 * Created by wangwenxiang on 15-12-3.
 */
public class Prize {
    private int prizeId;
    private int prizeUser;//用户
    private String prizeTime;//获奖时间，格式：2012.09
    private String prizeTitle;//获奖标题，简单说明
    private String prizeDescript;//描述
    private int prizeState;//状态

    public int getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(int prizeId) {
        this.prizeId = prizeId;
    }

    public int getPrizeUser() {
        return prizeUser;
    }

    public void setPrizeUser(int prizeUser) {
        this.prizeUser = prizeUser;
    }

    public String getPrizeTime() {
        return prizeTime;
    }

    public void setPrizeTime(String prizeTime) {
        this.prizeTime = prizeTime;
    }

    public String getPrizeTitle() {
        return prizeTitle;
    }

    public void setPrizeTitle(String prizeTitle) {
        this.prizeTitle = prizeTitle;
    }

    public String getPrizeDescript() {
        return prizeDescript;
    }

    public void setPrizeDescript(String prizeDescript) {
        this.prizeDescript = prizeDescript;
    }

    public int getPrizeState() {
        return prizeState;
    }

    public void setPrizeState(int prizeState) {
        this.prizeState = prizeState;
    }
}
