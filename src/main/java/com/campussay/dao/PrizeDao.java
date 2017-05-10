package com.campussay.dao;

import com.alibaba.fastjson.JSONObject;
import com.campussay.model.Prize;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by wangwenxiang on 15-12-4.
 */
@Repository
public interface PrizeDao {

    public int userSettingPrize(Prize prize);

    public List getUserSettingPrize(int userId,int state);

    List<HashMap<String,Objects>> getUserPrize(int userId,int state);

    void updatePrize(JSONObject jsonObject);

    void addPrize(JSONObject jsonObject);

    void delPrize(@Param("del") String[] del, int useId);
}
