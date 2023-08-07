package com.android.speaker.study;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LearnInfo {
    // 当日学习时长
    public int currentLearnTime;
    // 累计时长(单位:分)
    public int grantTotal;
    // 打卡时间列表
    public List<String> punchTimeList;
    // 今日目标时间
    public int targetTime;

    public LearnInfo parse(JSONObject obj) throws JSONException {
        currentLearnTime = obj.optInt("currentLearnTime");
        grantTotal = obj.optInt("grantTotal");
        JSONArray array = obj.optJSONArray("punchTime");
        if(array != null && array.length() > 0) {
            punchTimeList = new ArrayList<>();
            for(int i = 0; i < array.length(); i++) {
                punchTimeList.add(array.getString(i));
            }
        }
        targetTime = obj.optInt("targetTime");

        return this;
    }
}
