package com.campussay.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Prize;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public interface PrizeService {
    /**
     * 个人设置，获奖成果 查询
     * @return
     */
    public JSONObject getUserSettingPrize(int userId,int state);

    /**
     * 获取用户获奖信息
     * @return
     */
    List<HashMap<String,Objects>> getUserPrize(int userId,int state);

    /**
     * 个人设置，获奖成果 添加
     * @param prize
     * @return
     */
    public int userSettingPrize(Prize prize);

    void updatePrize(JSONArray changeArray,int userId);

    void addPrize(JSONArray addArray,int userId);

    void delPrize(String del,int userId);
}
