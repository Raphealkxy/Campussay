package com.campussay.model;

/**
 * Created by ld on 2016-1-26.
 * 擅长领域实体
 */

public class Skill {
    private int skillUser;  //对应用户id

    private int skillTalkingType;  //擅长领域Id

    public int getSkillUser() {
        return skillUser;
    }

    public void setSkillUser(int skillUser) {
        this.skillUser = skillUser;
    }

    public int getSkillTalkingType() {
        return skillTalkingType;
    }

    public void setSkillTalkingType(int skillTalkingType) {
        this.skillTalkingType = skillTalkingType;
    }

}
